package com.example.ui.user

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.Nature
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AppSettings
import com.example.data.model.Cow
import com.example.data.model.SevaItem
import com.example.data.model.User
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
import com.example.ui.theme.NavySurfaceVariant
import com.example.ui.theme.TextGoldenHighlight
import com.example.ui.theme.TextMutedBlueGray
import com.example.ui.theme.TextOffWhite
import com.example.ui.theme.TextWhite

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SevaDonationScreen(
  currentUser: User?,
  settings: AppSettings,
  sevaItems: List<SevaItem>,
  cows: List<Cow>,
  onDonateSubmitted: (
    amount: Double,
    donationType: String,
    donorName: String,
    mobile: String,
    dedicatedTo: String,
    notes: String,
    userPhotoUri: String?
  ) -> Unit
) {
  // Tabs: General Seva, Birthday Seva, Shradhanjali, Cow Adoption
  val activeTabs = buildList {
    add("સામાન્ય ગૌસેવા (General)")
    if (settings.isBirthdayActive) add("જન્મદિવસ સેવા (Birthday)")
    if (settings.isShradhanjaliActive) add("પુણ્યતિથિ / શ્રદ્ધાંજલિ")
    if (settings.isAdoptionActive) add("ગૌ દત્તક યોજના (Adoption)")
  }

  var selectedTabIndex by remember { mutableIntStateOf(0) }
  val currentTab = activeTabs.getOrElse(selectedTabIndex) { activeTabs.first() }

  // Donation Amount State - Preset requested values
  val presetAmounts = listOf(51.0, 101.0, 501.0, 1101.0, 2501.0, 5001.0)
  var selectedAmount by remember { mutableDoubleStateOf(501.0) }
  var customAmountText by remember { mutableStateOf("") }

  // Donor form states
  var donorName by remember { mutableStateOf(currentUser?.fullName ?: "") }
  var mobileNumber by remember { mutableStateOf(currentUser?.mobileNumber ?: "") }
  var dedicatedTo by remember { mutableStateOf("") }
  var notes by remember { mutableStateOf("") }
  var selectedCowId by remember { mutableStateOf<Long?>(null) }
  var formError by remember { mutableStateOf<String?>(null) }

  val resolvedType = when {
    currentTab.contains("Birthday") || currentTab.contains("જન્મદિવસ") -> "Birthday"
    currentTab.contains("શ્રદ્ધાંજલિ") || currentTab.contains("પુણ્યતિથિ") -> "Shradhanjali"
    currentTab.contains("Adoption") || currentTab.contains("દત્તક") -> "CowAdoption"
    else -> "General"
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(NavyBackground, NavyDark, Color(0xFF02131D))
        )
      )
      .padding(16.dp)
      .testTag("seva_donation_screen"),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. HERO COW IMAGE & EMOTIONAL GUJARATI HEADING
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("donation_hero_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = NavySurface),
        border = BorderStroke(1.dp, GoldBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
        ) {
          Image(
            painter = painterResource(id = R.drawable.img_gaumata_hero),
            contentDescription = "ગૌમાતા દર્શન",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
          )
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                Brush.verticalGradient(
                  colors = listOf(
                    Color.Transparent,
                    NavyBackground.copy(alpha = 0.7f),
                    NavyDark.copy(alpha = 0.95f)
                  )
                )
              )
          )
          Column(
            modifier = Modifier
              .align(Alignment.BottomStart)
              .padding(16.dp)
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(GoldChampagne.copy(alpha = 0.25f))
                .border(1.dp, GoldChampagne.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Text(
                text = "પવિત્ર ગૌ સેવા સમર્પણ",
                fontSize = 10.sp,
                color = GoldChampagneLight,
                fontWeight = FontWeight.Bold
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "એક તરણું ઘાસ, અક્ષય પુણ્યનો વાસ",
              fontWeight = FontWeight.ExtraBold,
              fontSize = 18.sp,
              color = TextWhite
            )
            Text(
              text = "ગૌમાતાની સેવાથી ૩૩ કોટિ દેવી-દેવતાઓ પ્રસન્ન થાય છે.",
              fontSize = 11.sp,
              color = TextMutedBlueGray
            )
          }
        }
      }
    }

    // 2. SEVA CATEGORY TABS
    if (activeTabs.size > 1) {
      item {
        ScrollableTabRow(
          selectedTabIndex = selectedTabIndex,
          containerColor = NavyDark,
          contentColor = GoldChampagne,
          edgePadding = 0.dp,
          indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
              modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
              color = GoldChampagne,
              height = 3.dp
            )
          }
        ) {
          activeTabs.forEachIndexed { index, title ->
            Tab(
              selected = selectedTabIndex == index,
              onClick = { selectedTabIndex = index },
              text = {
                Text(
                  text = title,
                  fontSize = 12.sp,
                  fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium,
                  color = if (selectedTabIndex == index) GoldChampagne else TextMutedBlueGray
                )
              },
              modifier = Modifier.testTag("donation_tab_$index")
            )
          }
        }
      }
    }

    // 3. PRESET DONATION AMOUNT BUTTONS (₹ 51, ₹ 101, ₹ 501, ₹ 1,101, ₹ 2,501, ₹ 5,001)
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NavySurface),
        border = BorderStroke(1.dp, NavyBorder)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "ગૌ સેવા રકમ પસંદ કરો (Select Amount):",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = TextWhite
          )
          Spacer(modifier = Modifier.height(12.dp))

          // 2x3 Grid for Preset Amounts
          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              presetAmounts.take(3).forEach { amt ->
                AmountButton(
                  modifier = Modifier.weight(1f),
                  amount = amt,
                  isSelected = selectedAmount == amt && customAmountText.isEmpty(),
                  onClick = {
                    selectedAmount = amt
                    customAmountText = ""
                  }
                )
              }
            }

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              presetAmounts.drop(3).take(3).forEach { amt ->
                AmountButton(
                  modifier = Modifier.weight(1f),
                  amount = amt,
                  isSelected = selectedAmount == amt && customAmountText.isEmpty(),
                  onClick = {
                    selectedAmount = amt
                    customAmountText = ""
                  }
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // CUSTOM AMOUNT FIELD
          OutlinedTextField(
            value = customAmountText,
            onValueChange = { text ->
              if (text.all { it.isDigit() }) {
                customAmountText = text
                text.toDoubleOrNull()?.let { selectedAmount = it }
              }
            },
            label = { Text("અથવા અન્ય ઇચ્છિત રકમ (Custom Amount ₹)", color = TextMutedBlueGray, fontSize = 12.sp) },
            placeholder = { Text("દા.ત. 11000", color = TextMutedBlueGray.copy(alpha = 0.6f)) },
            leadingIcon = {
              Text(
                text = "₹",
                color = GoldChampagne,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 12.dp, end = 4.dp)
              )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("custom_amount_input"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = NavyDark,
              unfocusedContainerColor = NavyDark,
              focusedBorderColor = GoldChampagne,
              unfocusedBorderColor = NavyBorder,
              focusedTextColor = TextWhite,
              unfocusedTextColor = TextWhite
            ),
            singleLine = true
          )
        }
      }
    }

    // 4. DONOR INFORMATION FORM
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NavySurface),
        border = BorderStroke(1.dp, NavyBorder)
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Text(
            text = "ગૌભક્તની વિગતો (Donor Details):",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = TextWhite
          )

          // Donor Full Name
          OutlinedTextField(
            value = donorName,
            onValueChange = { donorName = it },
            label = { Text("દાતાનું પૂરું નામ *", color = TextMutedBlueGray) },
            leadingIcon = {
              Icon(Icons.Default.Person, contentDescription = null, tint = GoldChampagne)
            },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("donor_name_input"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = NavyDark,
              unfocusedContainerColor = NavyDark,
              focusedBorderColor = GoldChampagne,
              unfocusedBorderColor = NavyBorder,
              focusedTextColor = TextWhite,
              unfocusedTextColor = TextWhite
            ),
            singleLine = true
          )

          // Donor Mobile
          OutlinedTextField(
            value = mobileNumber,
            onValueChange = { if (it.length <= 10 && it.all { ch -> ch.isDigit() }) mobileNumber = it },
            label = { Text("મોબાઈલ નંબર (સર્ટિફિકેટ/પત્રિકા માટે) *", color = TextMutedBlueGray) },
            leadingIcon = {
              Icon(Icons.Default.Phone, contentDescription = null, tint = GoldChampagne)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("donor_mobile_input"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = NavyDark,
              unfocusedContainerColor = NavyDark,
              focusedBorderColor = GoldChampagne,
              unfocusedBorderColor = NavyBorder,
              focusedTextColor = TextWhite,
              unfocusedTextColor = TextWhite
            ),
            singleLine = true
          )

          // Dedication (Sankalp / In Memory Of / Birthday Person)
          val dedicationLabel = when (resolvedType) {
            "Birthday" -> "કોના જન્મદિવસ નિમિત્તે સેવા? (Birthday Person)"
            "Shradhanjali" -> "સ્વર્ગસ્થ પૂજ્યશ્રીનું નામ (In Sacred Memory Of)"
            "CowAdoption" -> "ગૌપાલક સંકલ્પ / કુટુંબનું નામ"
            else -> "સંકલ્પ / સ્મરણાર્થ (Optional Dedication)"
          }
          OutlinedTextField(
            value = dedicatedTo,
            onValueChange = { dedicatedTo = it },
            label = { Text(dedicationLabel, color = TextMutedBlueGray) },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("dedicated_to_input"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = NavyDark,
              unfocusedContainerColor = NavyDark,
              focusedBorderColor = GoldChampagne,
              unfocusedBorderColor = NavyBorder,
              focusedTextColor = TextWhite,
              unfocusedTextColor = TextWhite
            ),
            singleLine = true
          )
        }
      }
    }

    // 5. ERROR DISPLAY
    if (formError != null) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF4A151B)),
          border = BorderStroke(1.dp, Color(0xFFC0392B))
        ) {
          Text(
            text = formError ?: "",
            color = TextWhite,
            fontSize = 12.sp,
            modifier = Modifier.padding(12.dp),
            fontWeight = FontWeight.Medium
          )
        }
      }
    }

    // 6. LARGE GOLDEN "♥ હાલમાં દાન કરો" BUTTON
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(
            Brush.horizontalGradient(
              listOf(GoldChampagneLight, GoldChampagne, GoldChampagneDark)
            )
          )
          .clickable {
            if (donorName.isBlank()) {
              formError = "કૃપા કરીને દાતાનું પૂરું નામ દાખલ કરો."
              return@clickable
            }
            if (mobileNumber.length < 10) {
              formError = "કૃપા કરીને 10 અંકનો માન્ય મોબાઈલ નંબર દાખલ કરો."
              return@clickable
            }
            if (selectedAmount <= 0) {
              formError = "માન્ય સેવા રકમ પસંદ કરો."
              return@clickable
            }
            formError = null
            onDonateSubmitted(
              selectedAmount,
              resolvedType,
              donorName.trim(),
              mobileNumber.trim(),
              dedicatedTo.trim(),
              notes.trim(),
              null
            )
          }
          .padding(vertical = 14.dp)
          .testTag("donate_submit_button"),
        contentAlignment = Alignment.Center
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.VolunteerActivism,
            contentDescription = null,
            tint = NavyDark,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "♥ હાલમાં દાન કરો (Pay ₹${selectedAmount.toInt()})",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = NavyDark
          )
        }
      }
      Spacer(modifier = Modifier.height(14.dp))
    }
  }
}

// -------------------------------------------------------------
// HELPER: AMOUNT BUTTON (Selected: Golden bg + dark text; Others: Dark navy + golden border)
// -------------------------------------------------------------
@Composable
private fun AmountButton(
  modifier: Modifier = Modifier,
  amount: Double,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .then(
        if (isSelected) {
          Modifier.background(
            Brush.horizontalGradient(
              listOf(GoldChampagneLight, GoldChampagne, GoldChampagneDark)
            )
          )
        } else {
          Modifier.background(NavySurfaceVariant)
        }
      )
      .border(
        width = if (isSelected) 1.5.dp else 1.dp,
        color = if (isSelected) GoldChampagneLight else GoldBorderSubtle,
        shape = RoundedCornerShape(12.dp)
      )
      .clickable { onClick() }
      .padding(vertical = 12.dp)
      .testTag("preset_amount_${amount.toInt()}"),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = "₹ ${amount.toInt()}",
      fontSize = 13.sp,
      fontWeight = FontWeight.Bold,
      color = if (isSelected) NavyDark else TextWhite
    )
  }
}
