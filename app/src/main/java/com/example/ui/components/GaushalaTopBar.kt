package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.AdminRole
import com.example.ui.theme.GoldBorder
import com.example.ui.theme.GoldChampagne
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
fun GaushalaTopBar(
  gaushalaName: String,
  tagline: String,
  isAdminMode: Boolean,
  adminRole: AdminRole,
  hasBirthdayToday: Boolean,
  onAdminClick: () -> Unit,
  onNotificationClick: () -> Unit,
  onProfileClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    color = NavyBackground,
    tonalElevation = 6.dp
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.horizontalGradient(
            colors = listOf(NavyDark, NavyBackground, NavySurface)
          )
        )
        .border(width = 0.8.dp, color = NavyBorder.copy(alpha = 0.5f))
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        // Left: Hamburger Menu + Gaushala Logo & Branding
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f)
        ) {
          IconButton(
            onClick = onProfileClick,
            modifier = Modifier
              .size(36.dp)
              .testTag("topbar_menu_btn")
          ) {
            Icon(
              imageVector = Icons.Default.Menu,
              contentDescription = "મેનુ",
              tint = GoldChampagne,
              modifier = Modifier.size(22.dp)
            )
          }

          Spacer(modifier = Modifier.width(6.dp))

          // Gaushala circular logo with thin golden border
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .border(1.5.dp, GoldChampagne, CircleShape)
              .clickable { onProfileClick() }
          ) {
            Image(
              painter = painterResource(id = R.drawable.img_app_icon),
              contentDescription = "ગૌશાળા લોગો",
              modifier = Modifier.size(42.dp),
              contentScale = ContentScale.Crop
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column {
            Text(
              text = gaushalaName,
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = TextWhite,
              maxLines = 1
            )
            Text(
              text = if (isAdminMode) "👑 માસ્ટર એડમિન પેનલ" else tagline,
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium,
              color = if (isAdminMode) GoldChampagneLight else TextGoldenHighlight,
              maxLines = 1
            )
          }
        }

        // Right actions: Birthday pill, Notification bell, Admin toggle
        Row(verticalAlignment = Alignment.CenterVertically) {
          if (hasBirthdayToday) {
            Box(
              modifier = Modifier
                .padding(end = 6.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(GoldChampagne.copy(alpha = 0.2f))
                .border(1.dp, GoldChampagne.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                .clickable { onNotificationClick() }
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Cake,
                  contentDescription = "જન્મદિવસ",
                  tint = GoldChampagneLight,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "જન્મદિવસ!",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = GoldChampagneLight
                )
              }
            }
          }

          IconButton(
            onClick = onNotificationClick,
            modifier = Modifier
              .size(38.dp)
              .testTag("notification_btn")
          ) {
            BadgedBox(
              badge = {
                Badge(
                  containerColor = GoldChampagne,
                  contentColor = NavyDark
                ) {
                  Text("1", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
              }
            ) {
              Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "સૂચનાઓ",
                tint = GoldCream,
                modifier = Modifier.size(22.dp)
              )
            }
          }

          Spacer(modifier = Modifier.width(4.dp))

          // Mode Switcher Pill (User <-> Admin) with Golden Border
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(14.dp))
              .background(if (isAdminMode) NavySurfaceVariant else NavySurface)
              .border(
                1.dp,
                if (isAdminMode) GoldChampagne else NavyBorder,
                RoundedCornerShape(14.dp)
              )
              .clickable { onAdminClick() }
              .padding(horizontal = 10.dp, vertical = 6.dp)
              .testTag("admin_switch_btn")
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = if (isAdminMode) Icons.Default.Person else Icons.Default.AdminPanelSettings,
                contentDescription = "એડમિન સ્વિચ",
                tint = GoldChampagne,
                modifier = Modifier.size(15.dp)
              )
              Spacer(modifier = Modifier.width(5.dp))
              Text(
                text = if (isAdminMode) "યુઝર એપ" else "એડમિન",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isAdminMode) TextWhite else GoldChampagne
              )
            }
          }
        }
      }
    }
  }
}
