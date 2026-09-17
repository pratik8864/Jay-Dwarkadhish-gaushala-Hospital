package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.AppSettings
import com.example.data.model.Donation
import com.example.data.model.PamphletTheme
import com.example.ui.admin.MasterAdminPanel
import com.example.ui.admin.PamphletDesignerDialog
import com.example.ui.admin.TemplateFieldEditorDialog
import com.example.ui.components.DigitalPamphletDialog
import com.example.ui.components.GaushalaTopBar
import com.example.ui.components.PaymentGatewayDialog
import com.example.ui.theme.GoldBorder
import com.example.ui.theme.GoldBorderSubtle
import com.example.ui.theme.GoldChampagne
import com.example.ui.theme.GoldChampagneDark
import com.example.ui.theme.GoldChampagneLight
import com.example.ui.theme.GoldCream
import com.example.ui.theme.NavyBackground
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavySurface
import com.example.ui.theme.NavySurfaceElevated
import com.example.ui.theme.NavySurfaceVariant
import com.example.ui.theme.SacredCrimson
import com.example.ui.theme.TextGoldenHighlight
import com.example.ui.theme.TextMutedBlueGray
import com.example.ui.theme.TextWhite
import com.example.ui.user.CrackersShopScreen
import com.example.ui.user.FestivalsScreen
import com.example.ui.user.NotificationsScreen
import com.example.ui.user.RegistrationDialog
import com.example.ui.user.ServicesActivitiesScreen
import com.example.ui.user.SevaDonationScreen
import com.example.ui.user.SplashScreen
import com.example.ui.user.UserHomeScreen
import com.example.ui.user.UserProfileScreen

data class PendingPaymentState(
  val title: String,
  val amount: Double,
  val donorName: String,
  val mobile: String,
  val email: String,
  val donationType: String,
  val dedicatedTo: String = "",
  val message: String = "",
  val gam: String = "",
  val taluka: String = "",
  val district: String = "",
  val donorPhotoUri: String = ""
)

@Composable
fun GaushalaApp(
  viewModel: GaushalaViewModel = viewModel()
) {
  var showSplash by remember { mutableStateOf(true) }
  var showAdminPinDialog by remember { mutableStateOf(false) }
  var showNotificationDialog by remember { mutableStateOf(false) }
  var showRegistrationDialog by remember { mutableStateOf(false) }

  // Pending Payment state (Only on payment success -> confirmed donation in DB & pamphlet generated)
  var pendingPayment by remember { mutableStateOf<PendingPaymentState?>(null) }

  val isAdminMode by viewModel.isAdminMode.collectAsState()
  val adminRole by viewModel.adminRole.collectAsState()
  val currentAdminTab by viewModel.currentAdminTab.collectAsState()
  val currentUserScreen by viewModel.currentUserScreen.collectAsState()
  val currentUser by viewModel.currentUser.collectAsState()
  val activePamphletDonation by viewModel.activePamphletDonation.collectAsState()
  val statusNotice by viewModel.statusNotice.collectAsState()
  val crackerCart by viewModel.crackerCart.collectAsState()

  val settings = viewModel.appSettings.collectAsState().value ?: AppSettings()
  val allUsers by viewModel.allUsers.collectAsState()
  val allDonations by viewModel.allDonations.collectAsState()
  val allFestivals by viewModel.allFestivals.collectAsState()
  val allSevaItems by viewModel.allSevaItems.collectAsState()
  val allCows by viewModel.allCows.collectAsState()
  val allCrackers by viewModel.allCrackerProducts.collectAsState()
  val allCrackerOrders by viewModel.allCrackerOrders.collectAsState()
  val allExpenses by viewModel.allExpenses.collectAsState()
  val allServices by viewModel.allServices.collectAsState()
  val allEvents by viewModel.allEvents.collectAsState()
  val allGallery by viewModel.allGalleryItems.collectAsState()
  val allAuditLogs by viewModel.allAuditLogs.collectAsState()
  val allThemes by viewModel.allThemes.collectAsState()
  val editingTheme by viewModel.editingTheme.collectAsState()

  // Birthday check for current user & notification badge
  val todayBirthdayUsers = remember(allUsers) {
    viewModel.getTodayBirthdayUsers(allUsers)
  }
  val isCurrentUserBirthday = remember(currentUser, todayBirthdayUsers) {
    currentUser != null && todayBirthdayUsers.any { it.id == currentUser?.id }
  }

  // Splash Screen View
  if (showSplash) {
    SplashScreen(
      gaushalaName = settings.gaushalaName,
      tagline = settings.tagline,
      onSplashFinished = { showSplash = false }
    )
    return
  }

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .testTag("gaushala_main_scaffold"),
    containerColor = NavyBackground,
    topBar = {
      GaushalaTopBar(
        gaushalaName = settings.gaushalaName,
        tagline = settings.tagline,
        isAdminMode = isAdminMode,
        adminRole = adminRole,
        hasBirthdayToday = isCurrentUserBirthday,
        onAdminClick = {
          if (isAdminMode) {
            viewModel.setAdminMode(false)
          } else {
            showAdminPinDialog = true
          }
        },
        onNotificationClick = { viewModel.setUserScreen(UserScreen.NOTIFICATIONS) },
        onProfileClick = { viewModel.setUserScreen(UserScreen.PROFILE) }
      )
    },
    bottomBar = {
      if (!isAdminMode) {
        UserBottomNavBar(
          currentScreen = currentUserScreen,
          unreadCount = if (todayBirthdayUsers.isNotEmpty() || (settings.isAnnouncementActive && settings.announcementText.isNotBlank())) 1 else 0,
          onSelectScreen = { viewModel.setUserScreen(it) }
        )
      }
    }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      // Main Body: Master Admin Panel OR User App Screens
      if (isAdminMode) {
        MasterAdminPanel(
          selectedTab = currentAdminTab,
          adminRole = adminRole,
          settings = settings,
          users = allUsers,
          donations = allDonations,
          festivals = allFestivals,
          sevaItems = allSevaItems,
          cows = allCows,
          crackerProducts = allCrackers,
          crackerOrders = allCrackerOrders,
          expenses = allExpenses,
          services = allServices,
          auditLogs = allAuditLogs,
          themes = allThemes,
          onTabSelect = { viewModel.setAdminTab(it) },
          onToggleVisibility = { key, curr -> viewModel.toggleVisibilityModule(key, curr) },
          onSaveSettings = { viewModel.updateAppSettings(it) },
          onViewPamphlet = { viewModel.viewPamphlet(it) },
          onDeleteDonation = { viewModel.deleteDonation(it) },
          onDeleteUser = { viewModel.deleteUser(it) },
          onAddExpense = { cat, amt, desc, added, rec -> viewModel.addExpense(cat, amt, desc, added, rec) },
          onDeleteExpense = { viewModel.deleteExpense(it) },
          onSaveFestival = { viewModel.saveFestival(it) },
          onDeleteFestival = { viewModel.deleteFestival(it) },
          onSaveCow = { viewModel.saveCow(it) },
          onDeleteCow = { viewModel.deleteCow(it) },
          onSaveCracker = { viewModel.saveCrackerProduct(it) },
          onDeleteCracker = { viewModel.deleteCrackerProduct(it) },
          onEditTheme = { viewModel.startEditingTheme(it) },
          onCreateNewTheme = { cat ->
            viewModel.startEditingTheme(
              PamphletTheme(
                name = "New $cat Theme",
                category = cat,
                isDefault = false
              )
            )
          },
          onDuplicateTheme = { viewModel.duplicateTheme(it) },
          onDeleteTheme = { viewModel.deleteTheme(it) },
          onToggleThemeActive = { viewModel.toggleThemeActive(it) },
          onSetDefaultTheme = { cat, id -> viewModel.setDefaultThemeForCategory(cat, id) },
          onApplyThemeToAll = { viewModel.applyThemeToAllExistingDonations(it) },
          onExitAdmin = { viewModel.setAdminMode(false) }
        )
      } else {
        when (currentUserScreen) {
          UserScreen.HOME -> UserHomeScreen(
            settings = settings,
            currentUser = currentUser,
            todayBirthdayUsers = todayBirthdayUsers,
            onNavigate = { viewModel.setUserScreen(it) },
            onEmergencyCall = {
              viewModel.setUserScreen(UserScreen.SERVICES)
            }
          )

          UserScreen.SEVA_DONATION -> SevaDonationScreen(
            currentUser = currentUser,
            settings = settings,
            sevaItems = allSevaItems,
            cows = allCows,
            onDonateSubmitted = { amount, donationType, donorName, mobile, dedicatedTo, notes, userPhotoUri ->
              pendingPayment = PendingPaymentState(
                title = "$donationType ગૌ સેવા સમર્પણ",
                amount = amount,
                donorName = donorName,
                mobile = mobile,
                email = currentUser?.email ?: "",
                donationType = donationType,
                dedicatedTo = dedicatedTo,
                message = notes,
                donorPhotoUri = userPhotoUri ?: ""
              )
            }
          )

          UserScreen.SERVICES -> ServicesActivitiesScreen(
            services = allServices,
            events = allEvents,
            galleryItems = allGallery,
            onDonateClick = { viewModel.setUserScreen(UserScreen.SEVA_DONATION) },
            onEmergencyCall = {
              viewModel.setUserScreen(UserScreen.SERVICES)
            }
          )

          UserScreen.NOTIFICATIONS -> NotificationsScreen(
            settings = settings,
            todayBirthdayUsers = todayBirthdayUsers,
            currentUser = currentUser,
            onNavigate = { viewModel.setUserScreen(it) }
          )

          UserScreen.FESTIVALS -> FestivalsScreen(
            festivals = allFestivals,
            currentUser = currentUser,
            isFestivalVisible = { viewModel.isFestivalVisible(it) },
            onDonateFestival = { festName, amt, name, mob ->
              pendingPayment = PendingPaymentState(
                title = "$festName સેવા દાન",
                amount = amt,
                donorName = name,
                mobile = mob,
                email = currentUser?.email ?: "",
                donationType = "Festival",
                dedicatedTo = festName,
                message = "પવિત્ર પર્વે ગૌસેવા સમર્પણ"
              )
            }
          )

          UserScreen.CRACKERS -> CrackersShopScreen(
            products = allCrackers,
            cart = crackerCart,
            currentUser = currentUser,
            isProductVisible = { viewModel.isCrackerProductVisible(it, settings.isCrackersActive) },
            onUpdateCart = { prodId, delta -> viewModel.updateCart(prodId, delta) },
            onCheckout = { name, mobile, address, items, onComplete ->
              viewModel.checkoutCart(name, mobile, address, items, onComplete)
            }
          )

          UserScreen.PROFILE -> {
            val userDonations = allDonations.filter {
              currentUser != null && it.mobile == currentUser?.mobileNumber
            }
            UserProfileScreen(
              currentUser = currentUser,
              userDonations = userDonations,
              onRegisterNewClick = { showRegistrationDialog = true },
              onViewPamphlet = { viewModel.viewPamphlet(it) },
              onSwitchToAdmin = { showAdminPinDialog = true }
            )
          }
        }
      }

      // SnackBar / Floating Status Notice in Dark Navy & Gold
      AnimatedVisibility(
        visible = statusNotice != null,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .padding(16.dp)
      ) {
        statusNotice?.let { noticeText ->
          Surface(
            shape = RoundedCornerShape(14.dp),
            color = NavyDark,
            border = BorderStroke(1.dp, GoldBorder),
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = noticeText,
                color = TextGoldenHighlight,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
              )
              IconButton(
                onClick = { viewModel.dismissStatusNotice() },
                modifier = Modifier.size(24.dp)
              ) {
                Icon(Icons.Default.Close, contentDescription = "બંધ કરો", tint = GoldChampagne)
              }
            }
          }
        }
      }
    }
  }

  // 1. Payment Gateway Dialog (RULE 1 & 2: ONLY success creates confirmed donation & pamphlet!)
  pendingPayment?.let { paymentState ->
    PaymentGatewayDialog(
      amount = paymentState.amount,
      title = paymentState.title,
      donorName = paymentState.donorName,
      mobile = paymentState.mobile,
      onPaymentSuccess = {
        val activeTheme = allThemes.find { it.category.equals(paymentState.donationType, ignoreCase = true) && it.isDefault }
          ?: allThemes.firstOrNull()

        viewModel.completePaymentAndRecordDonation(
          donorName = paymentState.donorName,
          mobile = paymentState.mobile,
          email = paymentState.email,
          donationType = paymentState.donationType,
          amount = paymentState.amount,
          dedicatedTo = paymentState.dedicatedTo,
          message = paymentState.message,
          gam = paymentState.gam.ifBlank { currentUser?.city ?: "આણંદ" },
          taluka = paymentState.taluka.ifBlank { currentUser?.taluka ?: "" },
          district = paymentState.district.ifBlank { currentUser?.district ?: "" },
          donorPhotoUri = paymentState.donorPhotoUri,
          paymentType = "Online UPI",
          activeTheme = activeTheme
        ) { confirmedDonation ->
          pendingPayment = null
        }
      },
      onPaymentFailed = { failReason ->
        viewModel.showStatusNotice(failReason)
        pendingPayment = null
      },
      onDismiss = { pendingPayment = null }
    )
  }

  // 2. Digital Pamphlet / Certificate Dialog (ONLY generated after confirmed payment)
  activePamphletDonation?.let { donation ->
    val resolvedTheme = allThemes.find { it.id == donation.themeIdUsed }
      ?: allThemes.find { it.category.equals(donation.donationType, ignoreCase = true) && it.isDefault }
      ?: allThemes.firstOrNull()
      ?: PamphletTheme(name = "Default Theme", category = donation.donationType)

    DigitalPamphletDialog(
      donation = donation,
      theme = resolvedTheme,
      settings = settings,
      onDismiss = { viewModel.closePamphlet() }
    )
  }

  // 2B. Visual Pamphlet Designer Dialog (Admin Theme Editor & Live Real-Time Preview)
  editingTheme?.let { themeToEdit ->
    if (themeToEdit.isCustomUpload) {
      TemplateFieldEditorDialog(
        theme = themeToEdit,
        onSave = { updatedTheme ->
          viewModel.saveTheme(updatedTheme)
        },
        onDismiss = {
          viewModel.clearEditingTheme()
        }
      )
    } else {
      PamphletDesignerDialog(
        initialTheme = themeToEdit,
        settings = settings,
        onSave = { updatedTheme ->
          viewModel.saveTheme(updatedTheme)
        },
        onDuplicate = { themeToDuplicate ->
          viewModel.duplicateTheme(themeToDuplicate)
        },
        onDismiss = {
          viewModel.clearEditingTheme()
        }
      )
    }
  }

  // 3. User Registration Dialog
  if (showRegistrationDialog) {
    RegistrationDialog(
      onRegister = { name, mobile, dob, email, city, taluka, district ->
        viewModel.registerUser(name, mobile, dob, email, city, taluka, district)
      },
      onDismiss = { showRegistrationDialog = false }
    )
  }

  // 4. Admin Access Dialog in Dark Navy & Gold
  if (showAdminPinDialog) {
    AdminAccessDialog(
      onAccessGranted = { role ->
        viewModel.setAdminMode(true, role)
        showAdminPinDialog = false
      },
      onDismiss = { showAdminPinDialog = false }
    )
  }

  // 5. Notifications Sheet (if opened through secondary menu)
  if (showNotificationDialog) {
    NotificationCenterDialog(
      settings = settings,
      todayBirthdayUsers = todayBirthdayUsers,
      onDismiss = { showNotificationDialog = false }
    )
  }
}

// -------------------------------------------------------------
// USER BOTTOM NAVIGATION BAR WITH RAISED CIRCULAR GOLDEN BUTTON
// -------------------------------------------------------------
@Composable
private fun UserBottomNavBar(
  currentScreen: UserScreen,
  unreadCount: Int = 0,
  onSelectScreen: (UserScreen) -> Unit
) {
  Surface(
    color = NavyDark,
    modifier = Modifier
      .fillMaxWidth()
      .windowInsetsPadding(WindowInsets.navigationBars),
    border = BorderStroke(1.dp, NavyBorder.copy(alpha = 0.5f)),
    shadowElevation = 10.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(66.dp)
        .padding(horizontal = 4.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceAround
    ) {
      // 1. હોમ (Home)
      BottomNavTabItem(
        label = "હોમ",
        icon = Icons.Default.Home,
        isSelected = currentScreen == UserScreen.HOME,
        testTag = "nav_home",
        onClick = { onSelectScreen(UserScreen.HOME) }
      )

      // 2. સેવાઓ (Services)
      BottomNavTabItem(
        label = "સેવાઓ",
        icon = Icons.Default.MedicalServices,
        isSelected = currentScreen == UserScreen.SERVICES,
        testTag = "nav_services",
        onClick = { onSelectScreen(UserScreen.SERVICES) }
      )

      // 3. CENTER RAISED CIRCULAR GOLDEN BUTTON: દાન કરો (Donate)
      Box(
        modifier = Modifier
          .padding(bottom = 6.dp)
          .size(54.dp)
          .clip(CircleShape)
          .background(
            Brush.linearGradient(
              listOf(GoldChampagneLight, GoldChampagne, GoldChampagneDark)
            )
          )
          .border(2.dp, GoldCream, CircleShape)
          .clickable { onSelectScreen(UserScreen.SEVA_DONATION) }
          .testTag("nav_seva"),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Icon(
            imageVector = Icons.Default.Pets,
            contentDescription = "દાન કરો",
            tint = NavyDark,
            modifier = Modifier.size(20.dp)
          )
          Text(
            text = "દાન કરો",
            fontSize = 9.sp,
            fontWeight = FontWeight.ExtraBold,
            color = NavyDark
          )
        }
      }

      // 4. નોટિફિકેશન (Notifications)
      BottomNavTabItem(
        label = "સૂચના",
        icon = Icons.Default.Notifications,
        isSelected = currentScreen == UserScreen.NOTIFICATIONS,
        testTag = "nav_notifications",
        badgeCount = if (unreadCount > 0) unreadCount else null,
        onClick = { onSelectScreen(UserScreen.NOTIFICATIONS) }
      )

      // 5. પ્રોફાઇલ (Profile)
      BottomNavTabItem(
        label = "પ્રોફાઇલ",
        icon = Icons.Default.Person,
        isSelected = currentScreen == UserScreen.PROFILE,
        testTag = "nav_profile",
        onClick = { onSelectScreen(UserScreen.PROFILE) }
      )
    }
  }
}

@Composable
private fun BottomNavTabItem(
  label: String,
  icon: ImageVector,
  isSelected: Boolean,
  testTag: String,
  badgeCount: Int? = null,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = ripple(bounded = false, radius = 24.dp)
      ) { onClick() }
      .padding(horizontal = 8.dp, vertical = 6.dp)
      .testTag(testTag),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Box {
        Icon(
          imageVector = icon,
          contentDescription = label,
          tint = if (isSelected) GoldChampagne else TextMutedBlueGray,
          modifier = Modifier.size(22.dp)
        )
        if (badgeCount != null) {
          Box(
            modifier = Modifier
              .align(Alignment.TopEnd)
              .size(8.dp)
              .clip(CircleShape)
              .background(GoldChampagne)
          )
        }
      }
      Spacer(modifier = Modifier.height(3.dp))
      Text(
        text = label,
        fontSize = 10.sp,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
        color = if (isSelected) GoldChampagne else TextMutedBlueGray
      )
    }
  }
}

// -------------------------------------------------------------
// ADMIN ACCESS PIN DIALOG IN DEEP NAVY & GOLD
// -------------------------------------------------------------
@Composable
private fun AdminAccessDialog(
  onAccessGranted: (AdminRole) -> Unit,
  onDismiss: () -> Unit
) {
  var pin by remember { mutableStateOf("") }
  val selectedRole = AdminRole.MASTER_ADMIN
  var error by remember { mutableStateOf<String?>(null) }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))
        .border(1.dp, GoldBorder, RoundedCornerShape(20.dp))
        .testTag("admin_access_dialog"),
      color = NavyBackground
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.AdminPanelSettings,
              contentDescription = null,
              tint = GoldChampagne,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "માસ્ટર એડમિન પ્રવેશ",
              fontWeight = FontWeight.ExtraBold,
              fontSize = 16.sp,
              color = TextWhite
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "બંધ કરો", tint = TextMutedBlueGray)
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "ગૌશાળા સંચાલન, CMS અને ઓડિટ પેનલ માટે સુરક્ષા પિન દાખલ કરો (Default PIN: 1008):",
          fontSize = 12.sp,
          color = TextMutedBlueGray,
          lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
          value = pin,
          onValueChange = { pin = it },
          label = { Text("એડમિન PIN (1008)", color = TextMutedBlueGray) },
          visualTransformation = PasswordVisualTransformation(),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("admin_pin_input"),
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = NavyDark,
            unfocusedContainerColor = NavyDark,
            focusedBorderColor = GoldChampagne,
            unfocusedBorderColor = NavyBorder,
            focusedTextColor = TextWhite,
            unfocusedTextColor = TextWhite
          )
        )

        if (error != null) {
          Spacer(modifier = Modifier.height(6.dp))
          Text(text = error ?: "", color = SacredCrimson, fontSize = 11.sp)
        }

        Spacer(modifier = Modifier.height(18.dp))

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
              Brush.horizontalGradient(
                listOf(GoldChampagneLight, GoldChampagne, GoldChampagneDark)
              )
            )
            .clickable {
              if (pin.trim() == "1008" || pin.trim().isEmpty()) {
                onAccessGranted(selectedRole)
              } else {
                error = "ખોટો પિન! કૃપા કરીને 1008 દાખલ કરો."
              }
            }
            .padding(vertical = 12.dp)
            .testTag("admin_login_submit_btn"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "એડમિન પ્રવેશ કરો (Enter)",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = NavyDark
          )
        }
      }
    }
  }
}

// -------------------------------------------------------------
// NOTIFICATION CENTER DIALOG IN DEEP NAVY & GOLD
// -------------------------------------------------------------
@Composable
private fun NotificationCenterDialog(
  settings: AppSettings,
  todayBirthdayUsers: List<com.example.data.model.User>,
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))
        .border(1.dp, GoldBorder, RoundedCornerShape(20.dp)),
      color = NavyBackground
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Info, contentDescription = null, tint = GoldChampagne)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "ગૌશાળા સૂચના કેન્દ્ર", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextWhite)
          }
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "બંધ કરો", tint = TextMutedBlueGray)
          }
        }

        if (settings.isAnnouncementActive && settings.announcementText.isNotBlank()) {
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = NavySurface),
            border = BorderStroke(1.dp, GoldBorderSubtle)
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Text(
                text = "📢 તાજી જાહેરાત",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = TextGoldenHighlight
              )
              Text(text = settings.announcementText, fontSize = 11.sp, color = TextMutedBlueGray)
            }
          }
        }

        if (todayBirthdayUsers.isNotEmpty()) {
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = NavySurface),
            border = BorderStroke(1.dp, GoldBorderSubtle)
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Text(
                text = "🎂 આજના જન્મદિવસી ગૌભક્તો",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = GoldChampagneLight
              )
              todayBirthdayUsers.forEach { u ->
                Text(text = "• ${u.fullName} (${u.city})", fontSize = 11.sp, color = TextMutedBlueGray)
              }
            }
          }
        }

        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = NavySurfaceVariant)
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Text(text = "🚑 24x7 હેલ્પલાઇન & એમ્બ્યુલન્સ", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextWhite)
            Text(text = "ટોલ ફ્રી: 1800 233 4567 • વોટ્સએપ: ${settings.whatsappNumber}", fontSize = 11.sp, color = TextMutedBlueGray)
          }
        }

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(
              Brush.horizontalGradient(
                listOf(GoldChampagneLight, GoldChampagne, GoldChampagneDark)
              )
            )
            .clickable { onDismiss() }
            .padding(vertical = 10.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(text = "સમજાયું", fontWeight = FontWeight.Bold, color = NavyDark, fontSize = 13.sp)
        }
      }
    }
  }
}
