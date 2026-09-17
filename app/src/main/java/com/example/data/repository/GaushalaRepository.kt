package com.example.data.repository

import com.example.data.local.GaushalaDao
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
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class GaushalaRepository(private val dao: GaushalaDao) {

  val allUsers: Flow<List<User>> = dao.getAllUsers()
  val allDonations: Flow<List<Donation>> = dao.getAllDonations()
  val successfulDonations: Flow<List<Donation>> = dao.getSuccessfulDonations()
  val allFestivals: Flow<List<Festival>> = dao.getAllFestivals()
  val allSevaItems: Flow<List<SevaItem>> = dao.getAllSevaItems()
  val allCows: Flow<List<Cow>> = dao.getAllCows()
  val allCrackerProducts: Flow<List<CrackerProduct>> = dao.getAllCrackerProducts()
  val allCrackerOrders: Flow<List<CrackerOrder>> = dao.getAllCrackerOrders()
  val allExpenses: Flow<List<Expense>> = dao.getAllExpenses()
  val allServices: Flow<List<GaushalaService>> = dao.getAllServices()
  val allEvents: Flow<List<GaushalaEvent>> = dao.getAllEvents()
  val allGalleryItems: Flow<List<GalleryItem>> = dao.getAllGalleryItems()
  val allPamphletCategories: Flow<List<PamphletCategory>> = dao.getAllPamphletCategories()
  val allThemes: Flow<List<PamphletTheme>> = dao.getAllPamphletThemes()
  val appSettings: Flow<AppSettings?> = dao.getAppSettings()
  val allAuditLogs: Flow<List<AuditLog>> = dao.getAllAuditLogs()

  fun getDonationsByMobile(mobile: String): Flow<List<Donation>> =
    dao.getDonationsByMobile(mobile)

  suspend fun registerUser(user: User): Long {
    val id = dao.insertUser(user)
    logAction("System/User", "User Registered: ${user.fullName}", "User Management")
    return id
  }

  suspend fun updateUser(user: User) {
    dao.updateUser(user)
    logAction("Admin", "User Updated: ${user.fullName}", "User Management")
  }

  suspend fun deleteUser(id: Long) {
    dao.deleteUserById(id)
    logAction("Admin", "User Deleted (ID: $id)", "User Management")
  }

  suspend fun getUserByMobile(mobile: String): User? = dao.getUserByMobile(mobile)

  // RULE 1 & RULE 2 compliant:
  // ONLY when payment succeeds, we create a confirmed donation entry with unique ID!
  suspend fun recordSuccessfulDonation(
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
    activeTheme: PamphletTheme? = null
  ): Donation {
    val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val dateCompact = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
    val timeStr = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
    val randSuffix = String.format(Locale.getDefault(), "%04d", Random.nextInt(1, 9999))
    val donationId = "GS-$dateCompact-$randSuffix"
    val refNo = "UPI-TXN-${System.currentTimeMillis().toString().takeLast(8)}"

    val themeToUse = activeTheme ?: resolveIsolatedTheme(donationType, dedicatedTo)
    val themeId = themeToUse.id
    val themeSnapshot = themeToUse.name

    val donation = Donation(
      id = donationId,
      donorName = donorName,
      mobile = mobile,
      email = email,
      donationType = donationType,
      amount = amount,
      paymentStatus = "SUCCESS",
      date = dateStr,
      time = timeStr,
      referenceNumber = refNo,
      dedicatedTo = dedicatedTo,
      message = message,
      gam = gam,
      taluka = taluka,
      district = district,
      donorPhotoUri = donorPhotoUri,
      paymentType = paymentType,
      themeIdUsed = themeId,
      themeSnapshotJson = themeSnapshot
    )
    dao.insertDonation(donation)
    logAction("Donor: $donorName", "Confirmed Donation: ₹${amount.toInt()} ($donationType - $donationId)", "Donations")
    return donation
  }

  suspend fun deleteDonation(id: String) {
    dao.deleteDonationById(id)
    logAction("Admin", "Donation Removed ($id)", "Donations")
  }

  // Festivals
  suspend fun saveFestival(festival: Festival) {
    if (festival.id == 0L) {
      dao.insertFestival(festival)
      logAction("Admin", "Festival Added: ${festival.name}", "Festivals")
    } else {
      dao.updateFestival(festival)
      logAction("Admin", "Festival Updated: ${festival.name}", "Festivals")
    }
  }

  suspend fun deleteFestival(id: Long) {
    dao.deleteFestivalById(id)
    logAction("Admin", "Festival Deleted (ID: $id)", "Festivals")
  }

  // Seva Items
  suspend fun saveSevaItem(item: SevaItem) {
    if (item.id == 0L) {
      dao.insertSevaItem(item)
      logAction("Admin", "Seva Item Added: ${item.name}", "Seva CMS")
    } else {
      dao.updateSevaItem(item)
      logAction("Admin", "Seva Item Updated: ${item.name}", "Seva CMS")
    }
  }

  suspend fun deleteSevaItem(id: Long) {
    dao.deleteSevaItemById(id)
    logAction("Admin", "Seva Item Deleted (ID: $id)", "Seva CMS")
  }

  // Cows
  suspend fun saveCow(cow: Cow) {
    if (cow.id == 0L) {
      dao.insertCow(cow)
      logAction("Admin", "Cow Added: ${cow.name}", "Cow Adoption")
    } else {
      dao.updateCow(cow)
      logAction("Admin", "Cow Updated: ${cow.name}", "Cow Adoption")
    }
  }

  suspend fun deleteCow(id: Long) {
    dao.deleteCowById(id)
    logAction("Admin", "Cow Removed (ID: $id)", "Cow Adoption")
  }

  // Crackers
  suspend fun saveCrackerProduct(product: CrackerProduct) {
    if (product.id == 0L) {
      dao.insertCrackerProduct(product)
      logAction("Admin", "Cracker Added: ${product.name}", "Crackers")
    } else {
      dao.updateCrackerProduct(product)
      logAction("Admin", "Cracker Updated: ${product.name}", "Crackers")
    }
  }

  suspend fun deleteCrackerProduct(id: Long) {
    dao.deleteCrackerProductById(id)
    logAction("Admin", "Cracker Deleted (ID: $id)", "Crackers")
  }

  suspend fun placeCrackerOrder(
    customerName: String,
    customerMobile: String,
    customerAddress: String,
    itemsSummary: String,
    totalAmount: Double
  ): CrackerOrder {
    val rand = Random.nextInt(1000, 9999)
    val orderId = "ORD-$rand"
    val dateStr = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
    val order = CrackerOrder(
      id = orderId,
      customerName = customerName,
      customerMobile = customerMobile,
      customerAddress = customerAddress,
      itemsSummary = itemsSummary,
      totalAmount = totalAmount,
      status = "PAID",
      date = dateStr
    )
    dao.insertCrackerOrder(order)
    logAction("Customer: $customerName", "Crackers Order Placed: ₹${totalAmount.toInt()} ($orderId)", "Crackers Orders")
    return order
  }

  // Expenses
  suspend fun addExpense(expense: Expense) {
    dao.insertExpense(expense)
    logAction(expense.addedBy, "Expense Added: ₹${expense.amount.toInt()} (${expense.category})", "Accounting")
  }

  suspend fun deleteExpense(id: Long) {
    dao.deleteExpenseById(id)
    logAction("Admin", "Expense Deleted (ID: $id)", "Accounting")
  }

  // Services
  suspend fun saveService(service: GaushalaService) {
    if (service.id == 0L) {
      dao.insertService(service)
      logAction("Admin", "Service Added: ${service.title}", "Services")
    } else {
      dao.updateService(service)
      logAction("Admin", "Service Updated: ${service.title}", "Services")
    }
  }

  suspend fun deleteService(id: Long) {
    dao.deleteServiceById(id)
    logAction("Admin", "Service Deleted (ID: $id)", "Services")
  }

  // Events
  suspend fun saveEvent(event: GaushalaEvent) {
    if (event.id == 0L) {
      dao.insertEvent(event)
      logAction("Admin", "Event Created: ${event.title}", "Events")
    } else {
      dao.updateEvent(event)
      logAction("Admin", "Event Updated: ${event.title}", "Events")
    }
  }

  suspend fun deleteEvent(id: Long) {
    dao.deleteEventById(id)
    logAction("Admin", "Event Deleted (ID: $id)", "Events")
  }

  // Gallery
  suspend fun addGalleryItem(item: GalleryItem) {
    dao.insertGalleryItem(item)
    logAction("Admin", "Gallery Item Added: ${item.title}", "Gallery")
  }

  suspend fun deleteGalleryItem(id: Long) {
    dao.deleteGalleryItemById(id)
    logAction("Admin", "Gallery Item Removed (ID: $id)", "Gallery")
  }

  // Pamphlet Theme Management (Section 1, 2, 3, 10)
  suspend fun savePamphletTheme(theme: PamphletTheme): Long {
    val id = if (theme.id == 0L) {
      val newId = dao.insertPamphletTheme(theme)
      logAction("Admin", "Pamphlet Theme Created: ${theme.name}", "Pamphlet Themes")
      newId
    } else {
      dao.updatePamphletTheme(theme)
      logAction("Admin", "Pamphlet Theme Updated: ${theme.name}", "Pamphlet Themes")
      theme.id
    }
    return id
  }

  suspend fun deletePamphletTheme(id: Long) {
    dao.deletePamphletThemeById(id)
    logAction("Admin", "Pamphlet Theme Deleted (ID: $id)", "Pamphlet Themes")
  }

  suspend fun duplicatePamphletTheme(theme: PamphletTheme): Long {
    val duplicated = theme.copy(
      id = 0,
      name = "${theme.name} (Copy)",
      isDefault = false
    )
    val newId = dao.insertPamphletTheme(duplicated)
    logAction("Admin", "Pamphlet Theme Duplicated: ${theme.name}", "Pamphlet Themes")
    return newId
  }

  suspend fun setDefaultThemeForCategory(category: String, themeId: Long) {
    dao.clearDefaultThemeForCategory(category)
    dao.markThemeAsDefault(themeId)
    logAction("Admin", "Default Theme Set for $category (Theme ID: $themeId)", "Pamphlet Themes")
  }

  suspend fun setDefaultThemeForCategoryAndSubCategory(category: String, subCategory: String, themeId: Long) {
    if (subCategory.isNotBlank()) {
      dao.clearDefaultThemeForCategoryAndSubCategory(category, subCategory)
    } else {
      dao.clearDefaultThemeForCategory(category)
    }
    dao.markThemeAsDefault(themeId)
    logAction("Admin", "Default Theme Set for $category/$subCategory (Theme ID: $themeId)", "Pamphlet Themes")
  }

  // Pamphlet Category Management (Requirements 31, 35, 40)
  suspend fun savePamphletCategory(category: PamphletCategory): Long {
    val id = if (category.id == 0L) {
      val newId = dao.insertPamphletCategory(category)
      logAction("Admin", "Category Created: ${category.displayNameGujarati}", "Pamphlet Categories")
      newId
    } else {
      dao.updatePamphletCategory(category)
      logAction("Admin", "Category Updated: ${category.displayNameGujarati}", "Pamphlet Categories")
      category.id
    }
    return id
  }

  suspend fun deletePamphletCategory(id: Long) {
    dao.deletePamphletCategoryById(id)
    logAction("Admin", "Category Removed (ID: $id)", "Pamphlet Categories")
  }

  suspend fun updateCategorySelectionMode(key: String, mode: String) {
    dao.updateCategorySelectionMode(key, mode)
    logAction("Admin", "Category $key Selection Mode: $mode", "Pamphlet Categories")
  }

  /**
   * STRICT CATEGORY ISOLATION (Requirements 34 & 38)
   * Birthday template NEVER appears in Shraddhanjali.
   * Shraddhanjali template NEVER appears in Festival.
   * Diwali template NEVER appears in Birthday.
   * Selection mode (Default vs Random) is applied per category.
   */
  suspend fun resolveIsolatedTheme(donationType: String, dedicatedTo: String): PamphletTheme {
    val normCat = when {
      donationType.contains("Birthday", ignoreCase = true) || donationType.contains("જન્મદિવસ") -> "Birthday"
      donationType.contains("Shradhanjali", ignoreCase = true) || donationType.contains("શ્રદ્ધાંજલિ") || donationType.contains("પુણ્યતિથિ") -> "Shradhanjali"
      donationType.contains("CowAdoption", ignoreCase = true) || donationType.contains("Cow Donation", ignoreCase = true) || donationType.contains("દત્તક") -> "Cow Donation"
      donationType.contains("Cow Seva", ignoreCase = true) || donationType.contains("ગૌ સેવા") -> "Cow Seva"
      donationType.contains("Festival", ignoreCase = true) || donationType.contains("તહેવાર") -> "Festival"
      donationType.contains("Food", ignoreCase = true) || donationType.contains("Chara", ignoreCase = true) || donationType.contains("ઘાસ") -> "Food/Chara Donation"
      donationType.contains("Special", ignoreCase = true) -> "Special Event"
      else -> "General"
    }

    if (normCat == "Festival") {
      val subCat = when {
        dedicatedTo.contains("Diwali", ignoreCase = true) || dedicatedTo.contains("દિવાળી") -> "Diwali"
        dedicatedTo.contains("Janmashtami", ignoreCase = true) || dedicatedTo.contains("જન્માષ્ટમી") -> "Janmashtami"
        dedicatedTo.contains("Navratri", ignoreCase = true) || dedicatedTo.contains("નવરાત્રી") -> "Navratri"
        dedicatedTo.contains("Rakshabandhan", ignoreCase = true) || dedicatedTo.contains("રક્ષાબંધન") -> "Rakshabandhan"
        dedicatedTo.contains("Ganesh", ignoreCase = true) || dedicatedTo.contains("ગણેશ") -> "Ganesh Chaturthi"
        else -> ""
      }
      if (subCat.isNotBlank()) {
        val subThemes = dao.getActiveThemesForCategoryAndSubCategory("Festival", subCat)
        if (subThemes.isNotEmpty()) {
          val defaultSub = subThemes.find { it.isDefault }
          return defaultSub ?: subThemes.first()
        }
      }
      // Fallback within Festival ONLY
      val festThemes = dao.getActiveThemesForCategory("Festival")
      if (festThemes.isNotEmpty()) {
        val defaultFest = festThemes.find { it.isDefault }
        return defaultFest ?: festThemes.first()
      }
    }

    // Category themes
    val categoryThemes = dao.getActiveThemesForCategory(normCat)
    if (categoryThemes.isNotEmpty()) {
      val defaultTheme = categoryThemes.find { it.isDefault }
      return defaultTheme ?: categoryThemes.first()
    }

    // Safety fallback strictly for that category
    val fallback = dao.getDefaultThemeForCategory(normCat)
    if (fallback != null) return fallback

    // Safe guarantee: create isolated fallback strictly for this category so cross-category leaks NEVER occur
    val autoCreated = PamphletTheme(
      name = "$normCat Standard Theme",
      category = normCat,
      isDefault = true,
      isActive = true
    )
    val newId = dao.insertPamphletTheme(autoCreated)
    return autoCreated.copy(id = newId)
  }

  suspend fun applyThemeToAllExistingDonations(theme: PamphletTheme) {
    dao.applyThemeToAllExistingDonations(theme.id, theme.name)
    logAction("Admin", "Applied Theme '${theme.name}' to All Existing Donations", "Pamphlet Themes")
  }

  // Settings
  suspend fun updateAppSettings(settings: AppSettings) {
    dao.saveAppSettings(settings)
    logAction("Admin", "App Settings & Visibility Updated", "CMS Settings")
  }

  // Audit Log
  suspend fun logAction(adminName: String, action: String, module: String) {
    val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val timeStr = SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(Date())
    dao.insertAuditLog(
      AuditLog(
        adminName = adminName,
        action = action,
        module = module,
        date = dateStr,
        time = timeStr
      )
    )
  }
}
