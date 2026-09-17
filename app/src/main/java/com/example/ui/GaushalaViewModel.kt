package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.GaushalaDatabase
import com.example.data.model.AppSettings
import com.example.data.model.AuditLog
import com.example.data.model.Cow
import com.example.data.model.CrackerOrder
import com.example.data.model.CrackerProduct
import com.example.data.model.Donation
import com.example.data.model.Expense
import com.example.data.model.Festival
import com.example.data.model.GalleryItem
import com.example.data.model.GaushalaEvent
import com.example.data.model.GaushalaService
import com.example.data.model.PamphletCategory
import com.example.data.model.PamphletTheme
import com.example.data.model.SevaItem
import com.example.data.model.User
import com.example.data.repository.GaushalaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class AdminRole {
  NONE, STAFF, ADMIN, MASTER_ADMIN
}

enum class UserScreen {
  HOME, SEVA_DONATION, FESTIVALS, CRACKERS, SERVICES, PROFILE, NOTIFICATIONS
}

enum class AdminTab {
  DASHBOARD, USERS, DONATIONS, PAMPHLET_THEMES, PAMPHLET_HISTORY, SEVA, FESTIVALS, BIRTHDAYS, SHRADHANJALI, COWS, CRACKERS, EXPENSES, SERVICES, VISIBILITY, BRAND_SETTINGS, AUDIT
}

class GaushalaViewModel(application: Application) : AndroidViewModel(application) {

  private val database = GaushalaDatabase.getDatabase(application, viewModelScope)
  private val repository = GaushalaRepository(database.gaushalaDao())

  // Core Data Flows from Database
  val allUsers: StateFlow<List<User>> = repository.allUsers
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allDonations: StateFlow<List<Donation>> = repository.allDonations
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val successfulDonations: StateFlow<List<Donation>> = repository.successfulDonations
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allFestivals: StateFlow<List<Festival>> = repository.allFestivals
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allSevaItems: StateFlow<List<SevaItem>> = repository.allSevaItems
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allCows: StateFlow<List<Cow>> = repository.allCows
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allCrackerProducts: StateFlow<List<CrackerProduct>> = repository.allCrackerProducts
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allCrackerOrders: StateFlow<List<CrackerOrder>> = repository.allCrackerOrders
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allExpenses: StateFlow<List<Expense>> = repository.allExpenses
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allServices: StateFlow<List<GaushalaService>> = repository.allServices
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allEvents: StateFlow<List<GaushalaEvent>> = repository.allEvents
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allGalleryItems: StateFlow<List<GalleryItem>> = repository.allGalleryItems
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allPamphletCategories: StateFlow<List<PamphletCategory>> = repository.allPamphletCategories
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allThemes: StateFlow<List<PamphletTheme>> = repository.allThemes
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val appSettings: StateFlow<AppSettings?> = repository.appSettings
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppSettings())

  val allAuditLogs: StateFlow<List<AuditLog>> = repository.allAuditLogs
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // App Mode State: User App or Master Admin Panel
  private val _isAdminMode = MutableStateFlow(false)
  val isAdminMode: StateFlow<Boolean> = _isAdminMode.asStateFlow()

  private val _adminRole = MutableStateFlow(AdminRole.NONE)
  val adminRole: StateFlow<AdminRole> = _adminRole.asStateFlow()

  private val _currentAdminTab = MutableStateFlow(AdminTab.DASHBOARD)
  val currentAdminTab: StateFlow<AdminTab> = _currentAdminTab.asStateFlow()

  private val _currentUserScreen = MutableStateFlow(UserScreen.HOME)
  val currentUserScreen: StateFlow<UserScreen> = _currentUserScreen.asStateFlow()

  // Current logged in user (defaults to seeded demo user)
  private val _currentUser = MutableStateFlow<User?>(null)
  val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

  // Last Generated Pamphlet / Confirmed Donation (Rule 2: ONLY after payment success)
  private val _activePamphletDonation = MutableStateFlow<Donation?>(null)
  val activePamphletDonation: StateFlow<Donation?> = _activePamphletDonation.asStateFlow()

  // Toast / Banner Message
  private val _statusNotice = MutableStateFlow<String?>(null)
  val statusNotice: StateFlow<String?> = _statusNotice.asStateFlow()

  // Cart for Diwali Crackers: ProductId -> Quantity
  private val _crackerCart = MutableStateFlow<Map<Long, Int>>(emptyMap())
  val crackerCart: StateFlow<Map<Long, Int>> = _crackerCart.asStateFlow()

  init {
    // Observe users to pick the primary user or prompt registration
    viewModelScope.launch {
      allUsers.collect { users ->
        if (_currentUser.value == null && users.isNotEmpty()) {
          _currentUser.value = users.first()
        }
      }
    }
  }

  fun dismissStatusNotice() {
    _statusNotice.value = null
  }

  fun showStatusNotice(message: String) {
    _statusNotice.value = message
  }

  fun setAdminMode(enabled: Boolean, role: AdminRole = AdminRole.MASTER_ADMIN) {
    _isAdminMode.value = enabled
    _adminRole.value = if (enabled) role else AdminRole.NONE
  }

  fun setAdminTab(tab: AdminTab) {
    _currentAdminTab.value = tab
  }

  fun setUserScreen(screen: UserScreen) {
    _currentUserScreen.value = screen
  }

  fun setCurrentUser(user: User) {
    _currentUser.value = user
  }

  fun registerUser(
    fullName: String,
    mobile: String,
    dob: String,
    email: String,
    city: String,
    taluka: String,
    district: String
  ) {
    viewModelScope.launch {
      val newUser = User(
        fullName = fullName.trim(),
        mobileNumber = mobile.trim(),
        dateOfBirth = dob.trim(),
        email = email.trim(),
        city = city.trim(),
        taluka = taluka.trim(),
        district = district.trim()
      )
      val id = repository.registerUser(newUser)
      _currentUser.value = newUser.copy(id = id)
      showStatusNotice("નોંધણી સફળ થઈ! ગૌશાળા પરિવારમાં આપનું સ્વાગત છે.")
    }
  }

  // Check if today matches any user's birthday (Month-Day match)
  fun getTodayBirthdayUsers(users: List<User>): List<User> {
    val todayMmDd = SimpleDateFormat("MM-dd", Locale.getDefault()).format(Date())
    return users.filter { user ->
      val dob = user.dateOfBirth
      if (dob.length >= 10) {
        val userMmDd = dob.substring(5, 10) // "YYYY-MM-DD" -> "MM-DD"
        userMmDd == todayMmDd
      } else false
    }
  }

  // Festival Date Visibility check (Rule 4)
  fun isFestivalVisible(festival: Festival): Boolean {
    if (!festival.isActive) return false
    if (!festival.isDateControlEnabled) return true
    if (festival.startDate.isBlank() || festival.endDate.isBlank()) return true

    val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    return todayStr >= festival.startDate && todayStr <= festival.endDate
  }

  // Cracker Product Date Visibility check (Rule 5)
  fun isCrackerProductVisible(product: CrackerProduct, isCrackersModuleActive: Boolean): Boolean {
    if (!isCrackersModuleActive || !product.isActive) return false
    if (!product.isDateControlEnabled) return true
    if (product.startDate.isBlank() || product.endDate.isBlank()) return true

    val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    return todayStr >= product.startDate && todayStr <= product.endDate
  }

  // Payment Processing (RULE 1 & RULE 2)
  // ONLY on payment success do we record donation and generate pamphlet!
  fun completePaymentAndRecordDonation(
    donorName: String,
    mobile: String,
    email: String,
    donationType: String,
    amount: Double,
    dedicatedTo: String = "",
    message: String = "",
    gam: String = "",
    taluka: String = "",
    district: String = "",
    donorPhotoUri: String = "",
    paymentType: String = "Online UPI",
    activeTheme: PamphletTheme? = null,
    onSuccess: (Donation) -> Unit
  ) {
    viewModelScope.launch {
      val donation = repository.recordSuccessfulDonation(
        donorName = donorName,
        mobile = mobile,
        email = email,
        donationType = donationType,
        amount = amount,
        dedicatedTo = dedicatedTo,
        message = message,
        gam = gam,
        taluka = taluka,
        district = district,
        donorPhotoUri = donorPhotoUri,
        paymentType = paymentType,
        activeTheme = activeTheme
      )
      _activePamphletDonation.value = donation
      showStatusNotice("દાન સફળતાપૂર્વક સ્વીકારાયું! રસીદ અને પત્રિકા તૈયાર થઈ ગઈ છે.")
      onSuccess(donation)
    }
  }

  fun viewPamphlet(donation: Donation) {
    _activePamphletDonation.value = donation
  }

  fun closePamphlet() {
    _activePamphletDonation.value = null
  }

  // Cart operations for Diwali crackers
  fun updateCart(productId: Long, delta: Int) {
    val current = _crackerCart.value.toMutableMap()
    val qty = (current[productId] ?: 0) + delta
    if (qty <= 0) {
      current.remove(productId)
    } else {
      current[productId] = qty
    }
    _crackerCart.value = current
  }

  fun clearCart() {
    _crackerCart.value = emptyMap()
  }

  fun checkoutCart(
    customerName: String,
    customerMobile: String,
    customerAddress: String,
    itemsList: List<CrackerProduct>,
    onComplete: (CrackerOrder) -> Unit
  ) {
    viewModelScope.launch {
      val cartMap = _crackerCart.value
      var total = 0.0
      val summaryParts = mutableListOf<String>()

      itemsList.forEach { product ->
        val qty = cartMap[product.id] ?: 0
        if (qty > 0) {
          total += product.offerPrice * qty
          summaryParts.add("${product.name} (x$qty)")
        }
      }

      val order = repository.placeCrackerOrder(
        customerName = customerName,
        customerMobile = customerMobile,
        customerAddress = customerAddress,
        itemsSummary = summaryParts.joinToString(", "),
        totalAmount = total
      )
      clearCart()
      showStatusNotice("દિવાળી ફટાકડા ઓર્ડર સફળ થયો! ઓર્ડર નં: ${order.id}")
      onComplete(order)
    }
  }

  // Admin CMS Functions
  fun updateAppSettings(settings: AppSettings) {
    viewModelScope.launch {
      repository.updateAppSettings(settings)
      showStatusNotice("સેટિંગ્સ સફળતાપૂર્વક અપડેટ થયા.")
    }
  }

  fun toggleVisibilityModule(moduleKey: String, currentVal: Boolean) {
    val currentSettings = appSettings.value ?: AppSettings()
    val updated = when (moduleKey) {
      "home" -> currentSettings.copy(isHomeActive = !currentVal)
      "donation" -> currentSettings.copy(isDonationActive = !currentVal)
      "birthday" -> currentSettings.copy(isBirthdayActive = !currentVal)
      "shradhanjali" -> currentSettings.copy(isShradhanjaliActive = !currentVal)
      "festival" -> currentSettings.copy(isFestivalActive = !currentVal)
      "crackers" -> currentSettings.copy(isCrackersActive = !currentVal)
      "adoption" -> currentSettings.copy(isAdoptionActive = !currentVal)
      "ambulance" -> currentSettings.copy(isAmbulanceActive = !currentVal)
      "hospital" -> currentSettings.copy(isHospitalActive = !currentVal)
      "gallery" -> currentSettings.copy(isGalleryActive = !currentVal)
      "events" -> currentSettings.copy(isEventsActive = !currentVal)
      "contact" -> currentSettings.copy(isContactActive = !currentVal)
      else -> currentSettings
    }
    updateAppSettings(updated)
  }

  fun saveFestival(festival: Festival) {
    viewModelScope.launch {
      repository.saveFestival(festival)
      showStatusNotice("તહેવાર વિગત સેવ થઈ.")
    }
  }

  fun deleteFestival(id: Long) {
    viewModelScope.launch {
      repository.deleteFestival(id)
      showStatusNotice("તહેવાર ડિલીટ થયો.")
    }
  }

  fun saveSevaItem(item: SevaItem) {
    viewModelScope.launch {
      repository.saveSevaItem(item)
      showStatusNotice("સેવા વિગત સેવ થઈ.")
    }
  }

  fun deleteSevaItem(id: Long) {
    viewModelScope.launch {
      repository.deleteSevaItem(id)
      showStatusNotice("સેવા ડિલીટ થઈ.")
    }
  }

  fun saveCow(cow: Cow) {
    viewModelScope.launch {
      repository.saveCow(cow)
      showStatusNotice("ગૌમાતા પ્રોફાઈલ સેવ થઈ.")
    }
  }

  fun deleteCow(id: Long) {
    viewModelScope.launch {
      repository.deleteCow(id)
      showStatusNotice("ગૌમાતા પ્રોફાઈલ હટાવી.")
    }
  }

  fun saveCrackerProduct(product: CrackerProduct) {
    viewModelScope.launch {
      repository.saveCrackerProduct(product)
      showStatusNotice("ફટાકડા પ્રોડક્ટ સેવ થઈ.")
    }
  }

  fun deleteCrackerProduct(id: Long) {
    viewModelScope.launch {
      repository.deleteCrackerProduct(id)
      showStatusNotice("ફટાકડા પ્રોડક્ટ હટાવાઈ.")
    }
  }

  fun addExpense(category: String, amount: Double, description: String, addedBy: String, receiptNo: String) {
    viewModelScope.launch {
      val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
      repository.addExpense(
        Expense(
          category = category,
          amount = amount,
          date = dateStr,
          description = description,
          addedBy = addedBy,
          receiptNo = receiptNo
        )
      )
      showStatusNotice("ખર્ચ નોંધાઈ ગયો.")
    }
  }

  fun deleteExpense(id: Long) {
    viewModelScope.launch {
      repository.deleteExpense(id)
      showStatusNotice("ખર્ચ એન્ટ્રી હટાવી.")
    }
  }

  fun saveService(service: GaushalaService) {
    viewModelScope.launch {
      repository.saveService(service)
      showStatusNotice("સેવા હેલ્પલાઈન અપડેટ થઈ.")
    }
  }

  fun deleteService(id: Long) {
    viewModelScope.launch {
      repository.deleteService(id)
      showStatusNotice("સેવા હેલ્પલાઈન હટાવાઈ.")
    }
  }

  fun saveEvent(event: GaushalaEvent) {
    viewModelScope.launch {
      repository.saveEvent(event)
      showStatusNotice("ઇવેન્ટ સેવ થઈ.")
    }
  }

  fun deleteEvent(id: Long) {
    viewModelScope.launch {
      repository.deleteEvent(id)
      showStatusNotice("ઇવેન્ટ હટાવાઈ.")
    }
  }

  fun addGalleryItem(title: String, category: String) {
    viewModelScope.launch {
      repository.addGalleryItem(
        GalleryItem(title = title, category = category)
      )
      showStatusNotice("તસવીર ગેલેરીમાં ઉમેરાઈ.")
    }
  }

  fun deleteGalleryItem(id: Long) {
    viewModelScope.launch {
      repository.deleteGalleryItem(id)
      showStatusNotice("તસવીર હટાવાઈ.")
    }
  }

  // Pamphlet Theme Management & Visual Designer
  private val _editingTheme = MutableStateFlow<PamphletTheme?>(null)
  val editingTheme: StateFlow<PamphletTheme?> = _editingTheme.asStateFlow()

  fun startEditingTheme(theme: PamphletTheme) {
    _editingTheme.value = theme
  }

  fun clearEditingTheme() {
    _editingTheme.value = null
  }

  fun saveTheme(theme: PamphletTheme) {
    viewModelScope.launch {
      repository.savePamphletTheme(theme)
      _editingTheme.value = null
      showStatusNotice("પત્રિકા થીમ સફળતાપૂર્વક સાચવી લેવામાં આવી: ${theme.name}")
    }
  }

  fun deleteTheme(id: Long) {
    viewModelScope.launch {
      repository.deletePamphletTheme(id)
      showStatusNotice("થીમ ડિલીટ કરવામાં આવી.")
    }
  }

  fun duplicateTheme(theme: PamphletTheme) {
    viewModelScope.launch {
      repository.duplicatePamphletTheme(theme)
      showStatusNotice("થીમની નકલ બની ગઈ: ${theme.name}")
    }
  }

  fun toggleThemeActive(theme: PamphletTheme) {
    viewModelScope.launch {
      repository.savePamphletTheme(theme.copy(isActive = !theme.isActive))
      showStatusNotice(if (!theme.isActive) "થીમ સક્રિય (ON) થઈ." else "થીમ નિષ્ક્રિય (OFF) થઈ.")
    }
  }

  fun setDefaultThemeForCategory(category: String, themeId: Long) {
    viewModelScope.launch {
      repository.setDefaultThemeForCategory(category, themeId)
      showStatusNotice("$category શ્રેણી માટે મુખ્ય થીમ સેટ થઈ.")
    }
  }

  fun setDefaultThemeForCategoryAndSubCategory(category: String, subCategory: String, themeId: Long) {
    viewModelScope.launch {
      repository.setDefaultThemeForCategoryAndSubCategory(category, subCategory, themeId)
      val label = if (subCategory.isNotBlank()) "$category / $subCategory" else category
      showStatusNotice("$label માટે મુખ્ય (Default) પત્રિકા સેટ થઈ.")
    }
  }

  fun renameTheme(theme: PamphletTheme, newName: String) {
    viewModelScope.launch {
      repository.savePamphletTheme(theme.copy(name = newName.trim()))
      showStatusNotice("થીમનું નામ બદલાયું: $newName")
    }
  }

  fun replaceThemeImage(theme: PamphletTheme, newUri: String, newName: String) {
    viewModelScope.launch {
      repository.savePamphletTheme(theme.copy(
        templateImageUri = newUri,
        templateImageName = newName
      ))
      showStatusNotice("પત્રિકાની ડિઝાઇન તસવીર અપડેટ થઈ.")
    }
  }

  // Pamphlet Category Management (Requirements 31, 35, 40)
  fun saveCategory(category: PamphletCategory) {
    viewModelScope.launch {
      repository.savePamphletCategory(category)
      showStatusNotice("કેટેગરી સેવ થઈ: ${category.displayNameGujarati}")
    }
  }

  fun deleteCategory(id: Long) {
    viewModelScope.launch {
      repository.deletePamphletCategory(id)
      showStatusNotice("કેટેગરી હટાવી દેવામાં આવી.")
    }
  }

  fun updateCategorySelectionMode(key: String, mode: String) {
    viewModelScope.launch {
      repository.updateCategorySelectionMode(key, mode)
      showStatusNotice("$key માટે મોડ બદલાયો: $mode")
    }
  }

  fun applyThemeToAllExistingDonations(theme: PamphletTheme) {
    viewModelScope.launch {
      repository.applyThemeToAllExistingDonations(theme)
      showStatusNotice("બધી જૂની પાવતીઓમાં '${theme.name}' થીમ લાગુ થઈ ગઈ!")
    }
  }

  fun deleteUser(id: Long) {
    viewModelScope.launch {
      repository.deleteUser(id)
      showStatusNotice("યુઝર ડિલીટ થયો.")
    }
  }

  fun deleteDonation(id: String) {
    viewModelScope.launch {
      repository.deleteDonation(id)
      showStatusNotice("દાન એન્ટ્રી હટાવાઈ.")
    }
  }
}
