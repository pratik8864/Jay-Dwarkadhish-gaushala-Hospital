package com.example.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FormatColorFill
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AppSettings
import com.example.data.model.Donation
import com.example.data.model.PamphletTheme
import com.example.ui.components.DynamicPamphletCanvas
import com.example.ui.components.parseColorHex
import com.example.ui.theme.GauGreen
import com.example.ui.theme.SacredCrimson
import com.example.ui.theme.SacredMaroon
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.TempleGold

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PamphletDesignerDialog(
  initialTheme: PamphletTheme,
  settings: AppSettings,
  onSave: (PamphletTheme) -> Unit,
  onDuplicate: (PamphletTheme) -> Unit,
  onDismiss: () -> Unit
) {
  // Designer working draft state
  var name by remember { mutableStateOf(initialTheme.name) }
  var category by remember { mutableStateOf(initialTheme.category) }
  var isCategoryDropdownOpen by remember { mutableStateOf(false) }

  var layoutStyle by remember { mutableStateOf(initialTheme.layoutStyle) }
  var backgroundType by remember { mutableStateOf(initialTheme.backgroundType) }
  var bgPrimaryHex by remember { mutableStateOf(initialTheme.bgPrimaryColorHex) }
  var bgSecondaryHex by remember { mutableStateOf(initialTheme.bgSecondaryColorHex) }
  var accentColorHex by remember { mutableStateOf(initialTheme.accentColorHex) }
  var borderColorHex by remember { mutableStateOf(initialTheme.borderColorHex) }
  var borderWidth by remember { mutableIntStateOf(initialTheme.borderWidthDp) }
  var borderStyle by remember { mutableStateOf(initialTheme.borderStyle) }

  var headerTitle by remember { mutableStateOf(initialTheme.headerTitleGujarati) }
  var subTitle by remember { mutableStateOf(initialTheme.subTitleGujarati) }
  var blessingMessage by remember { mutableStateOf(initialTheme.blessingMessageTemplate) }
  var headingFontSize by remember { mutableIntStateOf(initialTheme.headingFontSizeSp) }
  var fontStyle by remember { mutableStateOf(initialTheme.fontStyle) }

  var cowIllustrationType by remember { mutableStateOf(initialTheme.cowIllustrationType) }
  var cowPosition by remember { mutableStateOf(initialTheme.cowPosition) }
  var cowSize by remember { mutableStateOf(initialTheme.cowSize) }

  var showDonorPhoto by remember { mutableStateOf(initialTheme.showDonorPhoto) }
  var photoShape by remember { mutableStateOf(initialTheme.photoShape) }
  var photoPosition by remember { mutableStateOf(initialTheme.photoPosition) }
  var photoSizeDp by remember { mutableIntStateOf(initialTheme.photoSizeDp) }

  var showDonorName by remember { mutableStateOf(initialTheme.showDonorName) }
  var showGam by remember { mutableStateOf(initialTheme.showGam) }
  var showTaluka by remember { mutableStateOf(initialTheme.showTaluka) }
  var showDistrict by remember { mutableStateOf(initialTheme.showDistrict) }
  var showDate by remember { mutableStateOf(initialTheme.showDate) }
  var showAmount by remember { mutableStateOf(initialTheme.showAmount) }
  var showPaymentType by remember { mutableStateOf(initialTheme.showPaymentType) }
  var showDonationId by remember { mutableStateOf(initialTheme.showDonationId) }
  var showGaushalaLogo by remember { mutableStateOf(initialTheme.showGaushalaLogo) }
  var showGaushalaPhone by remember { mutableStateOf(initialTheme.showGaushalaPhone) }
  var showGaushalaAddress by remember { mutableStateOf(initialTheme.showGaushalaAddress) }
  var showTaxExemption80G by remember { mutableStateOf(initialTheme.showTaxExemption80G) }
  var showQrVerification by remember { mutableStateOf(initialTheme.showQrVerification) }
  var footerText by remember { mutableStateOf(initialTheme.footerText) }

  // Tabs for Controls: 0 = "લાઇવ પ્રિવ્યૂ & રંગો", 1 = "ટેક્સ્ટ & હેડર", 2 = "તસવીરો & ગાય", 3 = "ફીલ્ડ સ્વિચ (ON/OFF)"
  var selectedTab by remember { mutableIntStateOf(0) }

  // Sample donation object for real-time live preview
  val sampleDonation = remember {
    Donation(
      id = "GS-20260917-8821",
      donorName = "શ્રી રાજેશભાઈ પટેલ",
      mobile = "9825012345",
      email = "rajesh.patel@example.com",
      donationType = category,
      amount = 2500.0,
      paymentStatus = "SUCCESS",
      date = "2026-09-17",
      time = "10:30 AM",
      referenceNumber = "UPI-TXN-8849201",
      dedicatedTo = "શુભ જન્મદિવસ નિમિત્તે",
      message = "ગૌમાતાની કૃપા સદા રહે",
      gam = "આણંદ",
      taluka = "આણંદ",
      district = "આણંદ",
      paymentType = "Online UPI"
    )
  }

  // Live updated theme draft
  val liveTheme = PamphletTheme(
    id = initialTheme.id,
    name = name,
    category = category,
    isDefault = initialTheme.isDefault,
    isActive = initialTheme.isActive,
    layoutStyle = layoutStyle,
    backgroundType = backgroundType,
    bgPrimaryColorHex = bgPrimaryHex,
    bgSecondaryColorHex = bgSecondaryHex,
    accentColorHex = accentColorHex,
    borderColorHex = borderColorHex,
    borderWidthDp = borderWidth,
    borderStyle = borderStyle,
    headerTitleGujarati = headerTitle,
    subTitleGujarati = subTitle,
    blessingMessageTemplate = blessingMessage,
    headingFontSizeSp = headingFontSize,
    fontStyle = fontStyle,
    cowIllustrationType = cowIllustrationType,
    cowPosition = cowPosition,
    cowSize = cowSize,
    showDonorPhoto = showDonorPhoto,
    photoShape = photoShape,
    photoPosition = photoPosition,
    photoSizeDp = photoSizeDp,
    showDonorName = showDonorName,
    showGam = showGam,
    showTaluka = showTaluka,
    showDistrict = showDistrict,
    showDate = showDate,
    showAmount = showAmount,
    showPaymentType = showPaymentType,
    showDonationId = showDonationId,
    showGaushalaLogo = showGaushalaLogo,
    showGaushalaPhone = showGaushalaPhone,
    showGaushalaAddress = showGaushalaAddress,
    showTaxExemption80G = showTaxExemption80G,
    showQrVerification = showQrVerification,
    footerText = footerText
  )

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.98f)
        .padding(8.dp)
        .testTag("pamphlet_visual_designer_dialog"),
      shape = RoundedCornerShape(20.dp),
      color = MaterialTheme.colorScheme.background,
      tonalElevation = 6.dp
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(14.dp)
      ) {
        // Title Bar
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "વિઝ્યુઅલ પત્રિકા ડિઝાઈનર (Visual Pamphlet Designer)",
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = SacredMaroon
            )
            Text(
              text = "રીઅલ-ટાઇમ લાઇવ પ્રિવ્યૂ સાથે થીમ કસ્ટમાઇઝ કરો",
              fontSize = 11.sp,
              color = Color.Gray
            )
          }
          IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_designer_btn")) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "બંધ કરો")
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Basic Info: Theme Name & Category Selector
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("થીમનું નામ (Theme Name)") },
            modifier = Modifier.weight(1.3f).testTag("theme_name_input"),
            singleLine = true
          )

          // Category Dropdown
          val categories = listOf("Birthday", "General", "Festival", "Shradhanjali", "Cow Seva", "Custom")
          ExposedDropdownMenuBox(
            expanded = isCategoryDropdownOpen,
            onExpandedChange = { isCategoryDropdownOpen = !isCategoryDropdownOpen },
            modifier = Modifier.weight(1f)
          ) {
            OutlinedTextField(
              value = category,
              onValueChange = {},
              readOnly = true,
              label = { Text("શ્રેણી (Category)") },
              trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryDropdownOpen) },
              modifier = Modifier.menuAnchor().testTag("theme_category_dropdown")
            )
            ExposedDropdownMenu(
              expanded = isCategoryDropdownOpen,
              onDismissRequest = { isCategoryDropdownOpen = false }
            ) {
              categories.forEach { cat ->
                DropdownMenuItem(
                  text = { Text(cat) },
                  onClick = {
                    category = cat
                    isCategoryDropdownOpen = false
                  }
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ==========================================
        // 1. LIVE PREVIEW SECTION (Always in sync)
        // ==========================================
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("live_pamphlet_preview_card"),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF7F0)),
          border = BorderStroke(1.5.dp, TempleGold)
        ) {
          Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "🔴 લાઇવ પત્રિકા પ્રિવ્યૂ (Live Real-Time Canvas)",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = SacredMaroon
              )
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = SaffronPrimary.copy(alpha = 0.15f)
              ) {
                Text(
                  text = category,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = SaffronPrimary,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // The actual live Canvas
            DynamicPamphletCanvas(
              donation = sampleDonation.copy(donationType = category),
              theme = liveTheme,
              settings = settings,
              isPreview = true
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Control Tabs
        TabRow(
          selectedTabIndex = selectedTab,
          containerColor = MaterialTheme.colorScheme.surfaceVariant
        ) {
          Tab(
            selected = selectedTab == 0,
            onClick = { selectedTab = 0 },
            text = { Text("રંગો & બોર્ડર", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
          )
          Tab(
            selected = selectedTab == 1,
            onClick = { selectedTab = 1 },
            text = { Text("ટેક્સ્ટ & હેડર", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
          )
          Tab(
            selected = selectedTab == 2,
            onClick = { selectedTab = 2 },
            text = { Text("તસવીર & ગાય", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
          )
          Tab(
            selected = selectedTab == 3,
            onClick = { selectedTab = 3 },
            text = { Text("ફીલ્ડ સ્વિચ", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ==========================================
        // TAB 0: COLORS, BACKGROUND & BORDER
        // ==========================================
        if (selectedTab == 0) {
          Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Background Presets
            Text("બેકગ્રાઉન્ડ પ્રકાર (Background Style):", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              listOf("GRADIENT" to "ગ્રેડિએન્ટ", "SOLID" to "પ્લેન", "PATTERN" to "પેટર્ન").forEach { (type, label) ->
                val selected = backgroundType == type
                OutlinedButton(
                  onClick = { backgroundType = type },
                  border = BorderStroke(if (selected) 2.dp else 1.dp, if (selected) SaffronPrimary else Color.LightGray),
                  colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (selected) SaffronPrimary.copy(alpha = 0.1f) else Color.Transparent
                  )
                ) {
                  Text(label, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
                }
              }
            }

            // Background Palette Quick Presets
            Text("બેકગ્રાઉન્ડ રંગ પેલેટ:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            val bgPresets = listOf(
              Triple("#FFFDF5", "#FFF8E1", "સુવર્ણ ક્રિમ"),
              Triple("#FFFDF6", "#FFE0B2", "પાવન કેસરી"),
              Triple("#FFFBEA", "#FFECB3", "તહેવાર પીળો"),
              Triple("#F1F8E9", "#DCEDC8", "હરિયાળી લીલો"),
              Triple("#FAFAFA", "#F5F5F5", "શાંતિ ગ્રે"),
              Triple("#FFFFFF", "#FFFFFF", "શુદ્ધ શ્વેત")
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
              bgPresets.forEach { (pColor, sColor, title) ->
                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = parseColorHex(pColor, Color.White),
                  border = BorderStroke(1.5.dp, if (bgPrimaryHex == pColor) SaffronPrimary else Color.LightGray),
                  modifier = Modifier.clickable {
                    bgPrimaryHex = pColor
                    bgSecondaryHex = sColor
                  }
                ) {
                  Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = if (bgPrimaryHex == pColor) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                  )
                }
              }
            }

            // Accent & Border Colors
            Text("મુખ્ય આકર્ષક રંગ (Accent Color):", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            val accentPresets = listOf("#E65100", "#D84315", "#B71C1C", "#D4AF37", "#2E7D32", "#1565C0", "#37474F")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              accentPresets.forEach { hex ->
                val col = parseColorHex(hex, Color.Red)
                Box(
                  modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(col)
                    .border(if (accentColorHex == hex) 3.dp else 1.dp, if (accentColorHex == hex) Color.Black else Color.White, CircleShape)
                    .clickable { accentColorHex = hex }
                )
              }
            }

            // Border Style & Width
            Text("બોર્ડર સ્ટાઇલ & જાડાઈ (Border Width: ${borderWidth}dp):", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              listOf("ORNATE_GOLD" to "રાજસી ગોલ્ડ", "DOUBLE_SACRED" to "બેવડી બોર્ડર", "CLEAN_SOLID" to "સાદી બોર્ડર").forEach { (style, label) ->
                val selected = borderStyle == style
                OutlinedButton(
                  onClick = { borderStyle = style },
                  border = BorderStroke(if (selected) 2.dp else 1.dp, if (selected) SaffronPrimary else Color.LightGray)
                ) {
                  Text(label, fontSize = 11.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
                }
              }
            }

            Slider(
              value = borderWidth.toFloat(),
              onValueChange = { borderWidth = it.toInt() },
              valueRange = 1f..8f,
              steps = 6,
              modifier = Modifier.testTag("border_width_slider")
            )
          }
        }

        // ==========================================
        // TAB 1: TEXT, HEADINGS & BLESSING
        // ==========================================
        if (selectedTab == 1) {
          Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
              value = headerTitle,
              onValueChange = { headerTitle = it },
              label = { Text("મુખ્ય શીર્ષક (Header Title)") },
              modifier = Modifier.fillMaxWidth().testTag("theme_header_title_input")
            )

            OutlinedTextField(
              value = subTitle,
              onValueChange = { subTitle = it },
              label = { Text("ઉપ-શીર્ષક / મંત્ર (Subtitle / Mantra)") },
              modifier = Modifier.fillMaxWidth()
            )

            // Dynamic Placeholders Helper Chips
            Text("પ્લેસહોલ્ડર ઝડપી ઉમેરો (Click to insert):", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SaffronPrimary)
            val placeholderChips = listOf("{{DONOR_NAME}}", "{{GAM}}", "{{AMOUNT}}", "{{SEVA_TYPE}}", "{{DONATION_ID}}", "{{DATE}}")
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
              placeholderChips.forEach { chip ->
                Surface(
                  shape = RoundedCornerShape(12.dp),
                  color = SaffronPrimary.copy(alpha = 0.12f),
                  border = BorderStroke(0.8.dp, SaffronPrimary),
                  modifier = Modifier.clickable {
                    blessingMessage += " $chip"
                  }
                ) {
                  Text(
                    text = chip,
                    fontSize = 11.sp,
                    color = SaffronPrimary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                  )
                }
              }
            }

            OutlinedTextField(
              value = blessingMessage,
              onValueChange = { blessingMessage = it },
              label = { Text("આશીર્વાદ સંદેશ ટેમ્પલેટ (Blessing Message Template)") },
              modifier = Modifier.fillMaxWidth().height(100.dp).testTag("theme_blessing_input"),
              maxLines = 4
            )

            OutlinedTextField(
              value = footerText,
              onValueChange = { footerText = it },
              label = { Text("ફૂટર નોંધ (Footer Note)") },
              modifier = Modifier.fillMaxWidth()
            )

            // Font & Size
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text("ફોન્ટ સાઈઝ: ${headingFontSize}sp", fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Slider(
                value = headingFontSize.toFloat(),
                onValueChange = { headingFontSize = it.toInt() },
                valueRange = 14f..24f,
                steps = 9,
                modifier = Modifier.weight(1f)
              )
            }
          }
        }

        // ==========================================
        // TAB 2: DONOR PHOTO & COW ILLUSTRATION
        // ==========================================
        if (selectedTab == 2) {
          Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Cow Illustration Selection
            Text("ગૌમાતા ચિત્ર પ્રકાર (Cow Illustration):", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            val cowOptions = listOf(
              "SURABHI_GOLD" to "સુવર્ણ સુરભિ",
              "KAMDHENU_FLOWER" to "કામધેનુ પુષ્પ",
              "CALF_MOTHER" to "ગાય & વાછરડું",
              "VINTAGE_RADHA_KRISHNA" to "રાધા-કૃષ્ણ ગૌસેવા",
              "NONE" to "ચિત્ર વગર"
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
              cowOptions.forEach { (type, label) ->
                val selected = cowIllustrationType == type
                OutlinedButton(
                  onClick = { cowIllustrationType = type },
                  border = BorderStroke(if (selected) 2.dp else 1.dp, if (selected) SaffronPrimary else Color.LightGray)
                ) {
                  Text(label, fontSize = 11.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
                }
              }
            }

            // Cow Size
            Text("ગાય ચિત્ર સાઈઝ (Cow Size):", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              listOf("SMALL" to "નાની", "MEDIUM" to "મધ્યમ", "LARGE" to "મોટી").forEach { (sz, lbl) ->
                val sel = cowSize == sz
                OutlinedButton(
                  onClick = { cowSize = sz },
                  border = BorderStroke(if (sel) 2.dp else 1.dp, if (sel) SaffronPrimary else Color.LightGray)
                ) {
                  Text(lbl, fontSize = 11.sp)
                }
              }
            }

            HorizontalDivider()

            // Donor Photo Controls
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text("દાતાશ્રીની તસવીર દર્શાવો (Show Donor Photo):", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("દાતાનો ફોટો પત્રિકામાં પ્રદર્શિત કરવો કે નહીં", fontSize = 11.sp, color = Color.Gray)
              }
              Switch(
                checked = showDonorPhoto,
                onCheckedChange = { showDonorPhoto = it },
                colors = SwitchDefaults.colors(checkedThumbColor = GauGreen)
              )
            }

            if (showDonorPhoto) {
              Text("તસવીર આકાર (Photo Shape):", fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                  "CIRCLE" to "વર્તુળ (Circle)",
                  "ROUNDED" to "રાઉન્ડેડ (Square-Curve)",
                  "GOLD_FRAME" to "ગોલ્ડન ફ્રેમ (Gold Frame)"
                ).forEach { (shape, lbl) ->
                  val sel = photoShape == shape
                  OutlinedButton(
                    onClick = { photoShape = shape },
                    border = BorderStroke(if (sel) 2.dp else 1.dp, if (sel) SaffronPrimary else Color.LightGray)
                  ) {
                    Text(lbl, fontSize = 11.sp)
                  }
                }
              }

              Text("તસવીર સાઈઝ (Photo Size: ${photoSizeDp}dp):", fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Slider(
                value = photoSizeDp.toFloat(),
                onValueChange = { photoSizeDp = it.toInt() },
                valueRange = 50f..100f,
                steps = 10
              )
            }
          }
        }

        // ==========================================
        // TAB 3: FIELD VISIBILITY SWITCHES (RULE 8)
        // ==========================================
        if (selectedTab == 3) {
          Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
              text = "પત્રિકા ફિલ્ડ કંટ્રોલ (Field Visibility Toggles - Rule 8):",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = SacredMaroon
            )
            Text(
              text = "એડમિન દરેક ફિલ્ડને ચાલુ કે બંધ (Show/Hide) કરી શકે છે:",
              fontSize = 11.sp,
              color = Color.Gray
            )

            VisibilityRow(label = "દાતાનું નામ (Donor Name)", checked = showDonorName) { showDonorName = it }
            VisibilityRow(label = "ગામ / શહેર (Village / Gam)", checked = showGam) { showGam = it }
            VisibilityRow(label = "દાન રકમ (Donation Amount)", checked = showAmount) { showAmount = it }
            VisibilityRow(label = "ચુકવણી મોડ (Payment Type)", checked = showPaymentType) { showPaymentType = it }
            VisibilityRow(label = "રસીદ નંબર (Receipt ID)", checked = showDonationId) { showDonationId = it }
            VisibilityRow(label = "તારીખ & સમય (Date & Time)", checked = showDate) { showDate = it }
            VisibilityRow(label = "ગૌશાળા લોગો (Gaushala Logo)", checked = showGaushalaLogo) { showGaushalaLogo = it }
            VisibilityRow(label = "ગૌશાળા ફોન નંબર (Phone)", checked = showGaushalaPhone) { showGaushalaPhone = it }
            VisibilityRow(label = "ગૌશાળા સરનામું (Address)", checked = showGaushalaAddress) { showGaushalaAddress = it }
            VisibilityRow(label = "80G આવકવેરા મુક્તિ નોટિસ", checked = showTaxExemption80G) { showTaxExemption80G = it }
            VisibilityRow(label = "QR કોડ વેરિફિકેશન સીલ", checked = showQrVerification) { showQrVerification = it }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(12.dp))

        // Save & Action Buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Button(
            onClick = {
              onSave(liveTheme)
            },
            modifier = Modifier.weight(1.2f).testTag("save_theme_button"),
            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
          ) {
            Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("થીમ સાચવો (Save Theme)", fontWeight = FontWeight.Bold)
          }

          OutlinedButton(
            onClick = {
              onDuplicate(liveTheme)
            },
            modifier = Modifier.weight(1f).testTag("duplicate_theme_button")
          ) {
            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("નકલ (Copy)")
          }
        }
      }
    }
  }
}

@Composable
private fun VisibilityRow(
  label: String,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 3.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    Switch(
      checked = checked,
      onCheckedChange = onCheckedChange,
      colors = SwitchDefaults.colors(checkedThumbColor = GauGreen)
    )
  }
}
