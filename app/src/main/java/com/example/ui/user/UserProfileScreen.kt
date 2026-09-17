package com.example.ui.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Donation
import com.example.data.model.User
import com.example.ui.theme.GauGreen
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
import com.example.ui.theme.TextWhite

@Composable
fun UserProfileScreen(
  currentUser: User?,
  userDonations: List<Donation>,
  onRegisterNewClick: () -> Unit,
  onViewPamphlet: (Donation) -> Unit,
  onSwitchToAdmin: () -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(NavyBackground, NavyDark, Color(0xFF02131D))
        )
      )
      .padding(16.dp)
      .testTag("user_profile_screen"),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. PROFILE HEADER CARD (Circular Photo, User Name, Golden borders)
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = NavySurface),
        border = BorderStroke(1.dp, GoldBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            // Circular profile photo with thin golden border
            Box(
              modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(2.dp, GoldChampagne, CircleShape)
                .background(
                  Brush.radialGradient(
                    listOf(GoldChampagne.copy(alpha = 0.3f), NavyDark)
                  )
                ),
              contentAlignment = Alignment.Center
            ) {
              Image(
                painter = painterResource(id = R.drawable.sample_donor_photo),
                contentDescription = "પ્રોફાઈલ ફોટો",
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.Crop
              )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
              Text(
                text = currentUser?.fullName ?: "પરમ ગૌભક્ત",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 17.sp,
                color = TextWhite
              )
              Text(
                text = "મોબાઈલ: ${currentUser?.mobileNumber ?: "નોંધણી જરૂરી"}",
                fontSize = 12.sp,
                color = TextMutedBlueGray
              )
              if (!currentUser?.city.isNullOrBlank()) {
                Text(
                  text = "નિવાસ: ${currentUser?.city}, ${currentUser?.district}",
                  fontSize = 12.sp,
                  color = TextGoldenHighlight,
                  fontWeight = FontWeight.Medium
                )
              }
            }
          }

          if (currentUser?.dateOfBirth != null) {
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = NavyBorder.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Cake,
                contentDescription = null,
                tint = GoldChampagne,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "પાવન જન્મ તારીખ: ${currentUser.dateOfBirth}",
                fontSize = 12.sp,
                color = GoldChampagneLight,
                fontWeight = FontWeight.Medium
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Action Buttons: Register New & Switch to Admin
          Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(
              onClick = onRegisterNewClick,
              modifier = Modifier
                .weight(1f)
                .testTag("profile_register_btn"),
              shape = RoundedCornerShape(12.dp),
              border = BorderStroke(1.dp, GoldChampagne)
            ) {
              Icon(
                Icons.Default.PersonAdd,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = GoldChampagne
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text("નવી પ્રોફાઈલ", color = GoldChampagne, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(
                  Brush.horizontalGradient(
                    listOf(GoldChampagneLight, GoldChampagne, GoldChampagneDark)
                  )
                )
                .clickable { onSwitchToAdmin() }
                .padding(vertical = 10.dp)
                .testTag("profile_admin_btn"),
              contentAlignment = Alignment.Center
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  Icons.Default.AdminPanelSettings,
                  contentDescription = null,
                  modifier = Modifier.size(16.dp),
                  tint = NavyDark
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("એડમિન પેનલ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NavyDark)
              }
            }
          }
        }
      }
    }

    // 2. ROUNDED MENU CARDS (અમારા વિષે, સંપર્ક, સેટિંગ્સ, ભાષા, લોગઆઉટ)
    item {
      Text(
        text = "એપ્લિકેશન માહિતી & સેટિંગ્સ",
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = TextWhite
      )
    }

    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NavySurface),
        border = BorderStroke(1.dp, NavyBorder)
      ) {
        Column {
          ProfileMenuItem(
            icon = Icons.Default.Info,
            title = "અમારા વિષે (About Us)",
            subtitle = "ગૌશાળા ટ્રસ્ટનો ઇતિહાસ, હેતુ અને ગૌસેવા સંકલ્પ",
            onClick = {}
          )
          HorizontalDivider(color = NavyBorder.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 14.dp))
          ProfileMenuItem(
            icon = Icons.Default.Phone,
            title = "સંપર્ક & દર્શન (Contact & Visit)",
            subtitle = "ગૌધામ સરનામું, હેલ્પલાઇન અને દર્શન સમય",
            onClick = {}
          )
          HorizontalDivider(color = NavyBorder.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 14.dp))
          ProfileMenuItem(
            icon = Icons.Default.Settings,
            title = "સેટિંગ્સ & પ્રાઇવસી (Settings)",
            subtitle = "સૂચના સેટિંગ્સ અને રસીદ ડાઉનલોડ વિકલ્પો",
            onClick = {}
          )
          HorizontalDivider(color = NavyBorder.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 14.dp))
          ProfileMenuItem(
            icon = Icons.Default.Language,
            title = "ભાષા (Language)",
            subtitle = "ગુજરાતી (Gujarati) • પ્રાથમિક",
            onClick = {}
          )
          HorizontalDivider(color = NavyBorder.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 14.dp))
          ProfileMenuItem(
            icon = Icons.AutoMirrored.Filled.Logout,
            title = "લોગઆઉટ (Logout)",
            subtitle = "પ્રોફાઈલ સત્ર સમાપ્ત કરો",
            onClick = onRegisterNewClick
          )
        }
      }
    }

    // 3. DONATION HISTORY & RECEIPTS
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "મારી ગૌસેવા રસીદો & પત્રિકા (${userDonations.size})",
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = TextWhite
        )
        Text(
          text = "કુલ સેવા: ₹${userDonations.sumOf { it.amount }.toInt()}",
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          color = TextGoldenHighlight
        )
      }
    }

    if (userDonations.isEmpty()) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "હજુ સુધી કોઈ દાન નોંધાયેલ નથી.",
            color = TextMutedBlueGray,
            fontSize = 13.sp
          )
        }
      }
    } else {
      items(userDonations) { donation ->
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("donation_history_card_${donation.id}"),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = NavySurface),
          border = BorderStroke(1.dp, GoldBorderSubtle)
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "${donation.donationType} સેવા",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = TextWhite
              )
              Text(
                text = "ID: ${donation.id} • ${donation.date}",
                fontSize = 11.sp,
                color = TextMutedBlueGray
              )
              if (donation.dedicatedTo.isNotBlank()) {
                Text(
                  text = "હેતુ: ${donation.dedicatedTo}",
                  fontSize = 11.sp,
                  color = TextGoldenHighlight
                )
              }
            }

            Column(horizontalAlignment = Alignment.End) {
              Text(
                text = "₹ ${donation.amount.toInt()}",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = TextGoldenHighlight
              )
              Spacer(modifier = Modifier.height(4.dp))
              OutlinedButton(
                onClick = { onViewPamphlet(donation) },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, GoldChampagne),
                modifier = Modifier.testTag("view_pamphlet_btn_${donation.id}")
              ) {
                Icon(
                  Icons.Default.Description,
                  contentDescription = null,
                  modifier = Modifier.size(14.dp),
                  tint = GoldChampagne
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "પત્રિકા જુઓ",
                  fontSize = 10.sp,
                  color = GoldChampagne,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun ProfileMenuItem(
  icon: ImageVector,
  title: String,
  subtitle: String,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .padding(horizontal = 14.dp, vertical = 12.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(
      modifier = Modifier
        .size(36.dp)
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

    Spacer(modifier = Modifier.width(12.dp))

    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        color = TextWhite
      )
      Text(
        text = subtitle,
        fontSize = 10.sp,
        color = TextMutedBlueGray
      )
    }

    Icon(
      imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
      contentDescription = null,
      tint = TextMutedBlueGray.copy(alpha = 0.6f),
      modifier = Modifier.size(13.dp)
    )
  }
}
