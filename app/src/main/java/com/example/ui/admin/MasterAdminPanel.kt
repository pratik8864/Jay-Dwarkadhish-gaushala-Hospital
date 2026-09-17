package com.example.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Church
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppSettings
import com.example.data.model.AuditLog
import com.example.data.model.Cow
import com.example.data.model.CrackerOrder
import com.example.data.model.CrackerProduct
import com.example.data.model.Donation
import com.example.data.model.Expense
import com.example.data.model.Festival
import com.example.data.model.GaushalaService
import com.example.data.model.PamphletCategory
import com.example.data.model.PamphletTheme
import com.example.data.model.SevaItem
import com.example.data.model.User
import com.example.ui.AdminRole
import com.example.ui.AdminTab
import com.example.ui.theme.GauGreen
import com.example.ui.theme.GoldLight
import com.example.ui.theme.SacredCrimson
import com.example.ui.theme.SacredMaroon
import com.example.ui.theme.SaffronPale
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.TempleGold

@Composable
fun MasterAdminPanel(
  selectedTab: AdminTab,
  adminRole: AdminRole,
  settings: AppSettings,
  users: List<User>,
  donations: List<Donation>,
  festivals: List<Festival>,
  sevaItems: List<SevaItem>,
  cows: List<Cow>,
  crackerProducts: List<CrackerProduct>,
  crackerOrders: List<CrackerOrder>,
  expenses: List<Expense>,
  services: List<GaushalaService>,
  auditLogs: List<AuditLog>,
  themes: List<PamphletTheme> = emptyList(),
  categories: List<PamphletCategory> = emptyList(),
  onTabSelect: (AdminTab) -> Unit,
  onToggleVisibility: (String, Boolean) -> Unit,
  onSaveSettings: (AppSettings) -> Unit,
  onViewPamphlet: (Donation) -> Unit,
  onDeleteDonation: (String) -> Unit,
  onDeleteUser: (Long) -> Unit,
  onAddExpense: (category: String, amount: Double, description: String, addedBy: String, receiptNo: String) -> Unit,
  onDeleteExpense: (Long) -> Unit,
  onSaveFestival: (Festival) -> Unit,
  onDeleteFestival: (Long) -> Unit,
  onSaveCow: (Cow) -> Unit,
  onDeleteCow: (Long) -> Unit,
  onSaveCracker: (CrackerProduct) -> Unit,
  onDeleteCracker: (Long) -> Unit,
  onEditTheme: (PamphletTheme) -> Unit = {},
  onCreateNewTheme: (String) -> Unit = {},
  onDuplicateTheme: (PamphletTheme) -> Unit = {},
  onDeleteTheme: (Long) -> Unit = {},
  onToggleThemeActive: (PamphletTheme) -> Unit = {},
  onSetDefaultTheme: (String, Long) -> Unit = { _, _ -> },
  onSetDefaultThemeForCategoryAndSubCategory: (String, String, Long) -> Unit = { _, _, _ -> },
  onRenameTheme: (PamphletTheme, String) -> Unit = { _, _ -> },
  onReplaceThemeImage: (PamphletTheme, String, String) -> Unit = { _, _, _ -> },
  onSaveCategory: (PamphletCategory) -> Unit = {},
  onDeleteCategory: (Long) -> Unit = {},
  onUpdateCategoryMode: (String, String) -> Unit = { _, _ -> },
  onSaveTheme: (PamphletTheme) -> Unit = {},
  onApplyThemeToAll: (PamphletTheme) -> Unit = {},
  onExitAdmin: () -> Unit
) {
  val tabs = listOf(
    AdminTab.DASHBOARD to "ઓવરવ્યૂ & હિસાબ",
    AdminTab.PAMPHLET_THEMES to "પત્રિકા થીમ્સ (${themes.size})",
    AdminTab.PAMPHLET_HISTORY to "પત્રિકા હિસ્ટ્રી",
    AdminTab.VISIBILITY to "મોડ્યુલ ON/OFF",
    AdminTab.DONATIONS to "દાન રસીદો (${donations.size})",
    AdminTab.USERS to "ભક્તો / દાતા (${users.size})",
    AdminTab.EXPENSES to "ખર્ચ હિસાબ",
    AdminTab.FESTIVALS to "તહેવાર CMS",
    AdminTab.COWS to "ગૌમાતા પ્રોફાઈલ",
    AdminTab.CRACKERS to "ફટાકડા & ઓર્ડર્સ",
    AdminTab.BRAND_SETTINGS to "ગૌશાળા સેટિંગ્સ",
    AdminTab.AUDIT to "ઓડિટ લોગ"
  )

  Column(
    modifier = Modifier
      .fillMaxSize()
      .testTag("master_admin_panel")
  ) {
    // Admin Header Ribbon
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(0.dp),
      colors = CardDefaults.cardColors(containerColor = SacredMaroon)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = GoldLight)
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Text(
              text = "માસ્ટર એડમિન કંટ્રોલ સેન્ટર",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
            Text(
              text = "રોલ: ${adminRole.name} • સંપૂર્ણ સંચાલન",
              color = GoldLight,
              fontSize = 10.sp
            )
          }
        }

        Button(
          onClick = onExitAdmin,
          colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.testTag("admin_exit_btn")
        ) {
          Text(text = "યુઝર એપ જુઓ ↩", fontSize = 11.sp, color = Color.White)
        }
      }
    }

    // Horizontal Scrollable Tabs
    ScrollableTabRow(
      selectedTabIndex = tabs.indexOfFirst { it.first == selectedTab }.coerceAtLeast(0),
      containerColor = MaterialTheme.colorScheme.surface,
      contentColor = SaffronPrimary,
      edgePadding = 12.dp,
      indicator = { tabPositions ->
        val currentIdx = tabs.indexOfFirst { it.first == selectedTab }.coerceAtLeast(0)
        TabRowDefaults.SecondaryIndicator(
          Modifier.tabIndicatorOffset(tabPositions[currentIdx]),
          color = SaffronPrimary
        )
      }
    ) {
      tabs.forEach { (tab, title) ->
        Tab(
          selected = selectedTab == tab,
          onClick = { onTabSelect(tab) },
          text = {
            Text(
              text = title,
              fontSize = 12.sp,
              fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Medium
            )
          }
        )
      }
    }

    // Tab Body
    Box(modifier = Modifier.fillMaxSize().padding(14.dp)) {
      when (selectedTab) {
        AdminTab.DASHBOARD -> AdminDashboardView(donations, crackerOrders, expenses, users)
        AdminTab.PAMPHLET_THEMES -> PamphletThemeManagerScreen(
          themes = themes,
          categories = categories,
          settings = settings,
          onEditTheme = onEditTheme,
          onCreateNewTheme = onCreateNewTheme,
          onDuplicateTheme = onDuplicateTheme,
          onDeleteTheme = onDeleteTheme,
          onToggleActive = onToggleThemeActive,
          onSetDefault = onSetDefaultTheme,
          onSetDefaultThemeForCategoryAndSubCategory = onSetDefaultThemeForCategoryAndSubCategory,
          onRenameTheme = onRenameTheme,
          onReplaceThemeImage = onReplaceThemeImage,
          onSaveCategory = onSaveCategory,
          onDeleteCategory = onDeleteCategory,
          onUpdateCategoryMode = onUpdateCategoryMode,
          onSaveTheme = onSaveTheme,
          onApplyThemeToAll = onApplyThemeToAll
        )
        AdminTab.PAMPHLET_HISTORY -> PamphletHistoryScreen(
          donations = donations,
          themes = themes,
          settings = settings,
          onViewPamphlet = { don, _ -> onViewPamphlet(don) }
        )
        AdminTab.VISIBILITY -> AdminVisibilityView(settings, onToggleVisibility)
        AdminTab.DONATIONS -> AdminDonationsView(donations, onViewPamphlet, onDeleteDonation)
        AdminTab.USERS -> AdminUsersView(users, onDeleteUser)
        AdminTab.EXPENSES -> AdminExpensesView(expenses, onAddExpense, onDeleteExpense)
        AdminTab.FESTIVALS -> AdminFestivalsView(festivals, onSaveFestival, onDeleteFestival)
        AdminTab.COWS -> AdminCowsView(cows, onSaveCow, onDeleteCow)
        AdminTab.CRACKERS -> AdminCrackersView(crackerProducts, crackerOrders, onSaveCracker, onDeleteCracker)
        AdminTab.BRAND_SETTINGS -> AdminSettingsView(settings, onSaveSettings)
        AdminTab.AUDIT -> AdminAuditView(auditLogs)
        else -> AdminDashboardView(donations, crackerOrders, expenses, users)
      }
    }
  }
}

// 1. Dashboard & Hisab Overview
@Composable
private fun AdminDashboardView(
  donations: List<Donation>,
  crackerOrders: List<CrackerOrder>,
  expenses: List<Expense>,
  users: List<User>
) {
  val totalDonationAmt = donations.filter { it.paymentStatus == "SUCCESS" }.sumOf { it.amount }
  val totalCrackersAmt = crackerOrders.sumOf { it.totalAmount }
  val totalExpensesAmt = expenses.sumOf { it.amount }
  val netBalance = (totalDonationAmt + totalCrackersAmt) - totalExpensesAmt

  val todayIso = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
  val todayDonationAmt = donations.filter { it.paymentStatus == "SUCCESS" && it.date == todayIso }.sumOf { it.amount }

  LazyColumn(
    modifier = Modifier.fillMaxSize().testTag("admin_dashboard_view"),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Text(
        text = "ગૌશાળા નાણાકીય હિસાબ & રિપોર્ટિંગ (Financial Hisab)",
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
      )
    }

    // Net Balance Hero Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = if (netBalance >= 0) GauGreen else SacredCrimson),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(text = "ચોખ્ખું ગૌસેવા ફંડ બેલેન્સ (Net Treasury Balance)", color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "₹ ${netBalance.toInt()}",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 28.sp,
            color = Color.White
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "કુલ આવક (દાન + ફટાકડા): ₹${(totalDonationAmt + totalCrackersAmt).toInt()} | કુલ ખર્ચ: ₹${totalExpensesAmt.toInt()}",
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 11.sp
          )
        }
      }
    }

    // Metric Cards Grid
    item {
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        MetricCard(title = "આજનું દાન", amount = "₹ ${todayDonationAmt.toInt()}", subtitle = "${donations.filter { it.date == todayIso }.size} રસીદો", color = SaffronPrimary, modifier = Modifier.weight(1f))
        MetricCard(title = "કુલ ગૌદાન આવક", amount = "₹ ${totalDonationAmt.toInt()}", subtitle = "${donations.size} સફળ દાન", color = GauGreen, modifier = Modifier.weight(1f))
      }
    }

    item {
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        MetricCard(title = "ફટાકડા વેચાણ", amount = "₹ ${totalCrackersAmt.toInt()}", subtitle = "${crackerOrders.size} ઓર્ડર્સ", color = Color(0xFFE64A19), modifier = Modifier.weight(1f))
        MetricCard(title = "કુલ થયેલ ખર્ચ", amount = "₹ ${totalExpensesAmt.toInt()}", subtitle = "${expenses.size} ખર્ચ એન્ટ્રી", color = SacredCrimson, modifier = Modifier.weight(1f))
      }
    }

    // Category Breakdown
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(text = "સેવા પ્રકાર મુજબ વર્ગીકરણ (Category Breakdown)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
          Spacer(modifier = Modifier.height(10.dp))

          listOf("Festival", "Birthday", "Shradhanjali", "Cow Seva", "General").forEach { type ->
            val sum = donations.filter { it.donationType == type }.sumOf { it.amount }
            Row(
              modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(text = "$type સેવા:", fontSize = 12.sp)
              Text(text = "₹ ${sum.toInt()}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SaffronPrimary)
            }
          }
        }
      }
    }
  }
}

// 2. Central Visibility ON/OFF Controls (RULE 3)
@Composable
private fun AdminVisibilityView(
  settings: AppSettings,
  onToggleVisibility: (String, Boolean) -> Unit
) {
  val modules = listOf(
    Triple("home", "મુખ્ય હોમ પેજ (Home Page)", settings.isHomeActive),
    Triple("donation", "ઓનલાઇન સામાન્ય દાન (Online Donations)", settings.isDonationActive),
    Triple("birthday", "જન્મદિવસ સેવા (Birthday Seva)", settings.isBirthdayActive),
    Triple("shradhanjali", "શ્રદ્ધાંજલિ સેવા (Shradhanjali Seva)", settings.isShradhanjaliActive),
    Triple("festival", "તહેવાર સેવા મોડ્યુલ (Festivals)", settings.isFestivalActive),
    Triple("crackers", "દિવાળી ફટાકડા વેચાણ (Crackers Sale)", settings.isCrackersActive),
    Triple("adoption", "ગૌ દત્તક સેવા (Cow Adoption)", settings.isAdoptionActive),
    Triple("ambulance", "24x7 એમ્બ્યુલન્સ હેલ્પલાઈન (Ambulance)", settings.isAmbulanceActive),
    Triple("hospital", "ગૌ હોસ્પિટલ & ચિકિત્સાલય (Hospital)", settings.isHospitalActive),
    Triple("events", "કાર્યક્રમો & ઇવેન્ટ્સ (Events)", settings.isEventsActive),
    Triple("gallery", "ફોટો ગેલેરી (Photo Gallery)", settings.isGalleryActive),
    Triple("contact", "સંપર્ક & મુલાકાત (Contact Details)", settings.isContactActive)
  )

  LazyColumn(
    modifier = Modifier.fillMaxSize().testTag("admin_visibility_view"),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SaffronPale),
        border = BorderStroke(1.dp, SaffronPrimary)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(text = "⚙️ સેન્ટ્રલ મોડ્યુલ ઓન/ઓફ કંટ્રોલર", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = SacredCrimson)
          Text(text = "અહીંથી બંધ કરેલ મોડ્યુલ તાત્કાલિક યુઝર એપમાંથી અદ્રશ્ય થઈ જશે.", fontSize = 11.sp)
        }
      }
    }

    items(modules) { (key, label, isActive) ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp).fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(text = if (isActive) "સક્રિય (ON) • યુઝર્સને દેખાય છે" else "નિષ્ક્રિય (OFF) • છુપાયેલ છે", fontSize = 11.sp, color = if (isActive) GauGreen else SacredCrimson)
          }

          Switch(
            checked = isActive,
            onCheckedChange = { onToggleVisibility(key, isActive) },
            colors = SwitchDefaults.colors(checkedThumbColor = SaffronPrimary, checkedTrackColor = SaffronPale),
            modifier = Modifier.testTag("switch_visibility_$key")
          )
        }
      }
    }
  }
}

// 3. Donations View & Hisab
@Composable
private fun AdminDonationsView(
  donations: List<Donation>,
  onViewPamphlet: (Donation) -> Unit,
  onDeleteDonation: (String) -> Unit
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize().testTag("admin_donations_view"),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    item {
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(text = "તમામ દાન રસીદો (${donations.size})", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Text(text = "કુલ: ₹${donations.sumOf { it.amount }.toInt()}", fontWeight = FontWeight.ExtraBold, color = GauGreen, fontSize = 14.sp)
      }
    }

    items(donations) { d ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.35f))
      ) {
        Row(
          modifier = Modifier.padding(12.dp).fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(text = "${d.donorName} • ₹${d.amount.toInt()}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = "${d.donationType} • ID: ${d.id} • ${d.date}", fontSize = 11.sp, color = Color.Gray)
            if (d.dedicatedTo.isNotBlank()) {
              Text(text = "હેતુ: ${d.dedicatedTo}", fontSize = 11.sp, color = SaffronPrimary)
            }
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onViewPamphlet(d) }, modifier = Modifier.testTag("admin_pamphlet_${d.id}")) {
              Icon(Icons.Default.Description, contentDescription = "પત્રિકા", tint = SaffronPrimary)
            }
            IconButton(onClick = { onDeleteDonation(d.id) }, modifier = Modifier.testTag("admin_del_donation_${d.id}")) {
              Icon(Icons.Default.Delete, contentDescription = "ડિલીટ", tint = SacredCrimson)
            }
          }
        }
      }
    }
  }
}

// 4. Users View
@Composable
private fun AdminUsersView(
  users: List<User>,
  onDeleteUser: (Long) -> Unit
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize().testTag("admin_users_view"),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    item {
      Text(text = "નોંધાયેલ ભક્તો & દાતાઓ (${users.size})", fontWeight = FontWeight.Bold, fontSize = 15.sp)
    }

    items(users) { u ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
      ) {
        Row(
          modifier = Modifier.padding(12.dp).fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(text = u.fullName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = "મોબાઈલ: ${u.mobileNumber} • જન્મ: ${u.dateOfBirth}", fontSize = 11.sp, color = Color.Gray)
            Text(text = "સરનામું: ${u.city}, ${u.district}", fontSize = 11.sp, color = SaffronPrimary)
          }

          IconButton(onClick = { onDeleteUser(u.id) }, modifier = Modifier.testTag("admin_del_user_${u.id}")) {
            Icon(Icons.Default.Delete, contentDescription = "હટાવો", tint = SacredCrimson)
          }
        }
      }
    }
  }
}

// 5. Expenses Management View
@Composable
private fun AdminExpensesView(
  expenses: List<Expense>,
  onAddExpense: (category: String, amount: Double, description: String, addedBy: String, receiptNo: String) -> Unit,
  onDeleteExpense: (Long) -> Unit
) {
  var showAddForm by remember { mutableStateOf(false) }
  var category by remember { mutableStateOf("ઘાસ-ચારો (Fodder)") }
  var amountStr by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var addedBy by remember { mutableStateOf("મુખ્ય વ્યવસ્થાપક") }

  LazyColumn(
    modifier = Modifier.fillMaxSize().testTag("admin_expenses_view"),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(text = "ગૌશાળા ખર્ચ વ્યવસ્થાપન (Expenses)", fontWeight = FontWeight.Bold, fontSize = 15.sp)
          Text(text = "કુલ ખર્ચ: ₹${expenses.sumOf { it.amount }.toInt()}", color = SacredCrimson, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }

        Button(
          onClick = { showAddForm = !showAddForm },
          colors = ButtonDefaults.buttonColors(containerColor = SacredCrimson),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.testTag("toggle_add_expense_btn")
        ) {
          Text(text = if (showAddForm) "બંધ કરો" else "+ નવો ખર્ચ ઉમેરો", fontSize = 11.sp)
        }
      }
    }

    if (showAddForm) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(text = "નવા ખર્ચની એન્ટ્રી કરો:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
              value = category,
              onValueChange = { category = it },
              label = { Text("કેટેગરી (ઘાસ, દવા, પગાર, મેન્ટેનન્સ)") },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(10.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
              value = amountStr,
              onValueChange = { amountStr = it },
              label = { Text("ખર્ચ રકમ (₹)") },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(10.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
              value = description,
              onValueChange = { description = it },
              label = { Text("વિગત / કારણ") },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(10.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
              onClick = {
                val amt = amountStr.toDoubleOrNull() ?: 0.0
                if (amt > 0) {
                  onAddExpense(category, amt, description, addedBy, "EXP-${System.currentTimeMillis().toString().takeLast(4)}")
                  amountStr = ""
                  description = ""
                  showAddForm = false
                }
              },
              colors = ButtonDefaults.buttonColors(containerColor = SacredCrimson),
              modifier = Modifier.fillMaxWidth().testTag("save_expense_btn")
            ) {
              Text("ખર્ચ સેવ કરો")
            }
          }
        }
      }
    }

    items(expenses) { exp ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
      ) {
        Row(
          modifier = Modifier.padding(12.dp).fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(text = "${exp.category} • ₹${exp.amount.toInt()}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = SacredCrimson)
            Text(text = "${exp.description} (${exp.date})", fontSize = 11.sp, color = Color.Gray)
          }
          IconButton(onClick = { onDeleteExpense(exp.id) }, modifier = Modifier.testTag("admin_del_exp_${exp.id}")) {
            Icon(Icons.Default.Delete, contentDescription = "હટાવો", tint = Color.Gray)
          }
        }
      }
    }
  }
}

// 6. Festivals Management View
@Composable
private fun AdminFestivalsView(
  festivals: List<Festival>,
  onSaveFestival: (Festival) -> Unit,
  onDeleteFestival: (Long) -> Unit
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize().testTag("admin_festivals_view"),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    item {
      Text(text = "તહેવાર ગૌસેવા યોજનાઓ (${festivals.size})", fontWeight = FontWeight.Bold, fontSize = 15.sp)
    }

    items(festivals) { fest ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
      ) {
        Row(
          modifier = Modifier.padding(12.dp).fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(text = fest.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = "તારીખ: ${fest.dateText} • રકમ: ${fest.sevaAmounts}", fontSize = 11.sp, color = Color.Gray)
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(
              checked = fest.isActive,
              onCheckedChange = { onSaveFestival(fest.copy(isActive = it)) },
              colors = SwitchDefaults.colors(checkedThumbColor = SaffronPrimary)
            )
            IconButton(onClick = { onDeleteFestival(fest.id) }, modifier = Modifier.testTag("del_fest_${fest.id}")) {
              Icon(Icons.Default.Delete, contentDescription = null, tint = SacredCrimson)
            }
          }
        }
      }
    }
  }
}

// 7. Cows Management View
@Composable
private fun AdminCowsView(
  cows: List<Cow>,
  onSaveCow: (Cow) -> Unit,
  onDeleteCow: (Long) -> Unit
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize().testTag("admin_cows_view"),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    item {
      Text(text = "ગૌશાળા ગૌમાતા ડેટાબેઝ & દત્તક (${cows.size})", fontWeight = FontWeight.Bold, fontSize = 15.sp)
    }

    items(cows) { cow ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
      ) {
        Row(
          modifier = Modifier.padding(12.dp).fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(text = "${cow.name} (${cow.tagNumber})", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = "${cow.breed} • ${cow.ageYears} વર્ષ • ₹${cow.monthlySevaAmount.toInt()}/માસ", fontSize = 11.sp, color = Color.Gray)
            Text(text = if (cow.isAdopted) "દત્તક: ${cow.adopterName}" else "દત્તક માટે પ્રાપ્ય", fontSize = 11.sp, color = if (cow.isAdopted) GauGreen else SaffronPrimary)
          }

          IconButton(onClick = { onDeleteCow(cow.id) }, modifier = Modifier.testTag("del_cow_${cow.id}")) {
            Icon(Icons.Default.Delete, contentDescription = null, tint = SacredCrimson)
          }
        }
      }
    }
  }
}

// 8. Crackers Management View
@Composable
private fun AdminCrackersView(
  products: List<CrackerProduct>,
  orders: List<CrackerOrder>,
  onSaveCracker: (CrackerProduct) -> Unit,
  onDeleteCracker: (Long) -> Unit
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize().testTag("admin_crackers_view"),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Text(text = "દિવાળી ફટાકડા સ્ટોક & ઓર્ડર્સ", fontWeight = FontWeight.Bold, fontSize = 15.sp)
      Text(text = "કુલ ઓર્ડર્સ: ${orders.size} • કુલ આવક: ₹${orders.sumOf { it.totalAmount }.toInt()}", color = GauGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }

    item {
      Text(text = "ફટાકડા પ્રોડક્ટ્સ લિસ્ટ:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
    }

    items(products) { prod ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
      ) {
        Row(
          modifier = Modifier.padding(12.dp).fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(text = prod.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(text = "કિંમત: ₹${prod.offerPrice.toInt()} • સ્ટોક: ${prod.stock}", fontSize = 11.sp, color = Color.Gray)
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(
              checked = prod.isActive,
              onCheckedChange = { onSaveCracker(prod.copy(isActive = it)) },
              colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFE64A19))
            )
            IconButton(onClick = { onDeleteCracker(prod.id) }, modifier = Modifier.testTag("del_cracker_${prod.id}")) {
              Icon(Icons.Default.Delete, contentDescription = null, tint = SacredCrimson)
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(8.dp))
      Text(text = "ગ્રાહક ઓર્ડર્સ લિસ્ટ:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
    }

    items(orders) { ord ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Column(modifier = Modifier.padding(10.dp)) {
          Text(text = "ઓર્ડર #${ord.id} • ₹${ord.totalAmount.toInt()} (${ord.status})", fontWeight = FontWeight.Bold, fontSize = 12.sp)
          Text(text = "ગ્રાહક: ${ord.customerName} (${ord.customerMobile})", fontSize = 11.sp)
          Text(text = "આઈટમ્સ: ${ord.itemsSummary}", fontSize = 11.sp, color = Color.Gray)
        }
      }
    }
  }
}

// 9. Brand Settings
@Composable
private fun AdminSettingsView(
  settings: AppSettings,
  onSaveSettings: (AppSettings) -> Unit
) {
  var gaushalaName by remember { mutableStateOf(settings.gaushalaName) }
  var tagline by remember { mutableStateOf(settings.tagline) }
  var contactPhone by remember { mutableStateOf(settings.contactPhone) }
  var whatsappNumber by remember { mutableStateOf(settings.whatsappNumber) }
  var address by remember { mutableStateOf(settings.address) }
  var announcementText by remember { mutableStateOf(settings.announcementText) }
  var bdayMsg by remember { mutableStateOf(settings.birthdayGreetingMessage) }

  LazyColumn(
    modifier = Modifier.fillMaxSize().testTag("admin_settings_view"),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    item {
      Text(text = "ગૌશાળા બ્રાન્ડિંગ & સંપર્ક સેટિંગ્સ", fontWeight = FontWeight.Bold, fontSize = 15.sp)
    }

    item {
      OutlinedTextField(
        value = gaushalaName,
        onValueChange = { gaushalaName = it },
        label = { Text("ગૌશાળાનું નામ") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
      )
    }
    item {
      OutlinedTextField(
        value = tagline,
        onValueChange = { tagline = it },
        label = { Text("ટેગલાઈન (Tagline)") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
      )
    }
    item {
      OutlinedTextField(
        value = contactPhone,
        onValueChange = { contactPhone = it },
        label = { Text("સંપર્ક ફોન / હેલ્પલાઇન") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
      )
    }
    item {
      OutlinedTextField(
        value = whatsappNumber,
        onValueChange = { whatsappNumber = it },
        label = { Text("વોટ્સએપ નંબર") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
      )
    }
    item {
      OutlinedTextField(
        value = address,
        onValueChange = { address = it },
        label = { Text("ગૌધામ સરનામું") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
      )
    }
    item {
      OutlinedTextField(
        value = announcementText,
        onValueChange = { announcementText = it },
        label = { Text("હોમ સ્ક્રીન જાહેરાત / ટિકર") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
      )
    }
    item {
      OutlinedTextField(
        value = bdayMsg,
        onValueChange = { bdayMsg = it },
        label = { Text("જન્મદિવસ શુભેચ્છા સંદેશ (Birthday Message)") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
      )
    }

    item {
      Spacer(modifier = Modifier.height(10.dp))
      Button(
        onClick = {
          onSaveSettings(
            settings.copy(
              gaushalaName = gaushalaName,
              tagline = tagline,
              contactPhone = contactPhone,
              whatsappNumber = whatsappNumber,
              address = address,
              announcementText = announcementText,
              birthdayGreetingMessage = bdayMsg
            )
          )
        },
        modifier = Modifier.fillMaxWidth().testTag("save_brand_settings_btn"),
        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
        shape = RoundedCornerShape(12.dp)
      ) {
        Text("તમામ સેટિંગ્સ સેવ કરો", fontWeight = FontWeight.Bold)
      }
    }
  }
}

// 10. Audit Logs
@Composable
private fun AdminAuditView(logs: List<AuditLog>) {
  LazyColumn(
    modifier = Modifier.fillMaxSize().testTag("admin_audit_view"),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    item {
      Text(text = "ઓડિટ & એક્ટિવિટી લોગ (${logs.size})", fontWeight = FontWeight.Bold, fontSize = 15.sp)
    }

    items(logs) { log ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Column(modifier = Modifier.padding(10.dp)) {
          Text(text = "${log.action}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
          Text(text = "મોડ્યુલ: ${log.module} • દ્વારા: ${log.adminName} • ${log.date} ${log.time}", fontSize = 10.sp, color = Color.Gray)
        }
      }
    }
  }
}

@Composable
private fun MetricCard(
  title: String,
  amount: String,
  subtitle: String,
  color: Color,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, color.copy(alpha = 0.4f)),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Text(text = title, fontSize = 11.sp, color = Color.Gray)
      Spacer(modifier = Modifier.height(4.dp))
      Text(text = amount, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp, color = color)
      Spacer(modifier = Modifier.height(2.dp))
      Text(text = subtitle, fontSize = 10.sp, color = Color.Gray)
    }
  }
}
