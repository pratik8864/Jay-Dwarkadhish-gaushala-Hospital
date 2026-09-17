package com.example.ui.user

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Nature
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppSettings
import com.example.data.model.User
import com.example.ui.UserScreen
import com.example.ui.components.AnimatedCowHeroBanner
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
fun UserHomeScreen(
  settings: AppSettings,
  currentUser: User?,
  todayBirthdayUsers: List<User>,
  onNavigate: (UserScreen) -> Unit,
  onEmergencyCall: () -> Unit
) {
  val hasBirthdayToday = todayBirthdayUsers.any { it.id == currentUser?.id }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(NavyBackground, NavyDark, Color(0xFF02131D))
        )
      )
      .padding(horizontal = 16.dp, vertical = 10.dp)
      .testTag("user_home_screen"),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. HERO BANNER
    item {
      AnimatedCowHeroBanner(
        gaushalaName = settings.gaushalaName,
        tagline = settings.tagline,
        hasBirthdayToday = hasBirthdayToday,
        onCtaClick = { onNavigate(UserScreen.SEVA_DONATION) }
      )
    }

    // 2. THREE PREMIUM STATISTIC CARDS
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("stats_cards_row"),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        StatCard(
          modifier = Modifier.weight(1f),
          number = "650+",
          labelGujarati = "ગૌ સેવા",
          labelEnglish = "Cows in Seva",
          icon = Icons.Default.Pets
        )
        StatCard(
          modifier = Modifier.weight(1f),
          number = "5,000+",
          labelGujarati = "સ્વસ્થ ગાયો",
          labelEnglish = "Treated & Well",
          icon = Icons.Default.LocalHospital
        )
        StatCard(
          modifier = Modifier.weight(1f),
          number = "24/7",
          labelGujarati = "સેવા ઉપલબ્ધ",
          labelEnglish = "Emergency Care",
          icon = Icons.Default.SupportAgent
        )
      }
    }

    // 3. ANNOUNCEMENT MARQUEE
    if (settings.isAnnouncementActive && settings.announcementText.isNotBlank()) {
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("home_announcement_card"),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = NavySurfaceVariant),
          border = BorderStroke(1.dp, GoldBorder)
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(GoldChampagne.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Campaign,
                contentDescription = null,
                tint = GoldChampagne,
                modifier = Modifier.size(18.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "ગૌશાળા પાવન સૂચના",
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = GoldChampagneLight
              )
              Text(
                text = settings.announcementText,
                fontSize = 12.sp,
                color = TextWhite,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }
      }
    }

    // 4. BIRTHDAY BLESSING BANNER
    if (todayBirthdayUsers.isNotEmpty()) {
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigate(UserScreen.SEVA_DONATION) }
            .testTag("home_birthday_card"),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = NavySurface),
          border = BorderStroke(1.dp, GoldChampagne.copy(alpha = 0.5f))
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(
                  Brush.radialGradient(
                    listOf(GoldChampagne.copy(alpha = 0.3f), NavyDark)
                  )
                ),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Cake,
                contentDescription = null,
                tint = GoldChampagne,
                modifier = Modifier.size(22.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "🎂 આજના પાવન જન્મદિવસ ગૌસેવકો",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = GoldChampagneLight
              )
              val names = todayBirthdayUsers.take(3).joinToString(", ") { it.fullName }
              Text(
                text = "$names સહિત ભક્તોને શુભકામના! ગૌમાતાના આશીર્વાદ પ્રાપ્ત કરો.",
                fontSize = 11.sp,
                color = TextMutedBlueGray
              )
            }
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = null,
              tint = GoldChampagne,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }
    }

    // 5. "અમારી સેવાઓ" / "OUR SERVICES" 2-COLUMN GRID
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "અમારી સેવાઓ",
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = TextWhite
          )
          Text(
            text = "Our Sacred Seva & Medical Services",
            fontSize = 11.sp,
            color = TextMutedBlueGray
          )
        }
        Text(
          text = "બધી જુઓ →",
          fontSize = 12.sp,
          color = GoldChampagne,
          fontWeight = FontWeight.SemiBold,
          modifier = Modifier
            .clickable { onNavigate(UserScreen.SERVICES) }
            .padding(4.dp)
        )
      }
    }

    item {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Row 1: ગાય સારવાર & ગૌશાળા સેવા
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          ServiceGridCard(
            modifier = Modifier.weight(1f),
            titleGujarati = "ગાય સારવાર",
            titleEnglish = "Cow Treatment",
            description = "નિઃશુલ્ક સારવાર & ઓપરેશન",
            icon = Icons.Default.MedicalServices,
            tag = "service_card_treatment",
            onClick = { onNavigate(UserScreen.SERVICES) }
          )
          ServiceGridCard(
            modifier = Modifier.weight(1f),
            titleGujarati = "ગૌશાળા સેવા",
            titleEnglish = "Gaushala Seva",
            description = "આશ્રય & પવિત્ર પરિચર્યા",
            icon = Icons.Default.Pets,
            tag = "service_card_gaushala",
            onClick = { onNavigate(UserScreen.SERVICES) }
          )
        }

        // Row 2: ચારો વ્યવસ્થા & દાન સેવા
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          ServiceGridCard(
            modifier = Modifier.weight(1f),
            titleGujarati = "ચારો વ્યવસ્થા",
            titleEnglish = "Fodder Seva",
            description = "લીલો-સૂકો ઘાસચારો અર્પણ",
            icon = Icons.Default.Grass,
            tag = "service_card_fodder",
            onClick = { onNavigate(UserScreen.SEVA_DONATION) }
          )
          ServiceGridCard(
            modifier = Modifier.weight(1f),
            titleGujarati = "દાન સેવા",
            titleEnglish = "Donation Seva",
            description = "ઓનલાઈન સુરક્ષિત દાન & પત્રિકા",
            icon = Icons.Default.VolunteerActivism,
            tag = "service_card_donation",
            onClick = { onNavigate(UserScreen.SEVA_DONATION) }
          )
        }

        // Row 3: એક ગાય યોજનામાં & મોબાઈલ પશુ ચિકિત્સા
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          ServiceGridCard(
            modifier = Modifier.weight(1f),
            titleGujarati = "એક ગાય દત્તક",
            titleEnglish = "Cow Adoption",
            description = "માસિક ગૌમાતા વાલી યોજના",
            icon = Icons.Default.Nature,
            tag = "service_card_adoption",
            onClick = { onNavigate(UserScreen.SEVA_DONATION) }
          )
          ServiceGridCard(
            modifier = Modifier.weight(1f),
            titleGujarati = "મોબાઈલ ચિકિત્સા",
            titleEnglish = "Mobile Vet Care",
            description = "24x7 પશુ એમ્બ્યુલન્સ સેવા",
            icon = Icons.Default.Emergency,
            tag = "service_card_ambulance",
            onClick = { onEmergencyCall() }
          )
        }

        // Optional Seasonal Modules (Festival, Crackers)
        if (settings.isFestivalActive || settings.isCrackersActive) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            if (settings.isFestivalActive) {
              ServiceGridCard(
                modifier = Modifier.weight(1f),
                titleGujarati = "પાવન પર્વ સેવા",
                titleEnglish = "Festival Seva",
                description = "દિવાળી, ગોપાષ્ટમી ઉત્સવ",
                icon = Icons.Default.CalendarMonth,
                tag = "service_card_festivals",
                onClick = { onNavigate(UserScreen.FESTIVALS) }
              )
            }
            if (settings.isCrackersActive) {
              ServiceGridCard(
                modifier = Modifier.weight(1f),
                titleGujarati = "દિવાળી ફટાકડા",
                titleEnglish = "Crackers Shop",
                description = "ગૌસેવા લાભાર્થે ફટાકડા",
                icon = Icons.Default.LocalFireDepartment,
                tag = "service_card_crackers",
                onClick = { onNavigate(UserScreen.CRACKERS) }
              )
            }
          }
        }
      }
    }

    // 6. 24x7 EMERGENCY HELPLINE CALL STRIP
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onEmergencyCall() }
          .testTag("emergency_call_strip"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NavySurface),
        border = BorderStroke(1.dp, GoldChampagne.copy(alpha = 0.4f))
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(
                  Brush.radialGradient(
                    listOf(GoldChampagne.copy(alpha = 0.25f), NavyDark)
                  )
                ),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = "ફોન",
                tint = GoldChampagne,
                modifier = Modifier.size(22.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "24x7 ગૌ સેવા & એમ્બ્યુલન્સ હેલ્પલાઇન",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = TextWhite
              )
              Text(
                text = "ઇમરજન્સી: 1800 233 4567 / +91 98250 12345",
                fontSize = 11.sp,
                color = TextGoldenHighlight
              )
            }
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .background(
                Brush.horizontalGradient(
                  listOf(GoldChampagneLight, GoldChampagne)
                )
              )
              .padding(horizontal = 10.dp, vertical = 6.dp)
          ) {
            Text(
              text = "કોલ કરો",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = NavyDark
            )
          }
        }
      }
      Spacer(modifier = Modifier.height(10.dp))
    }
  }
}

// -------------------------------------------------------------
// HELPER COMPONENT: 3 PREMIUM STATISTIC CARDS
// -------------------------------------------------------------
@Composable
private fun StatCard(
  modifier: Modifier = Modifier,
  number: String,
  labelGujarati: String,
  labelEnglish: String,
  icon: ImageVector
) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = NavySurface),
    border = BorderStroke(1.dp, GoldBorderSubtle),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp, horizontal = 8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Box(
        modifier = Modifier
          .size(34.dp)
          .clip(CircleShape)
          .background(NavySurfaceVariant),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = GoldChampagne,
          modifier = Modifier.size(18.dp)
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = number,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 17.sp,
        color = TextGoldenHighlight
      )

      Text(
        text = labelGujarati,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        color = TextWhite,
        textAlign = TextAlign.Center
      )

      Text(
        text = labelEnglish,
        fontSize = 9.sp,
        color = TextMutedBlueGray,
        textAlign = TextAlign.Center
      )
    }
  }
}

// -------------------------------------------------------------
// HELPER COMPONENT: 2-COLUMN SERVICE GRID CARD
// -------------------------------------------------------------
@Composable
private fun ServiceGridCard(
  modifier: Modifier = Modifier,
  titleGujarati: String,
  titleEnglish: String,
  description: String,
  icon: ImageVector,
  tag: String,
  onClick: () -> Unit
) {
  Card(
    modifier = modifier
      .clickable { onClick() }
      .testTag(tag),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = NavySurface),
    border = BorderStroke(1.dp, NavyBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp)
    ) {
      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(CircleShape)
          .background(
            Brush.radialGradient(
              listOf(GoldChampagne.copy(alpha = 0.25f), NavyDark)
            )
          )
          .border(1.dp, GoldChampagne.copy(alpha = 0.4f), CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = titleGujarati,
          tint = GoldChampagne,
          modifier = Modifier.size(20.dp)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = titleGujarati,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        color = TextWhite
      )

      Text(
        text = titleEnglish,
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium,
        color = GoldChampagneLight
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = description,
        fontSize = 10.sp,
        color = TextMutedBlueGray,
        lineHeight = 14.sp
      )
    }
  }
}
