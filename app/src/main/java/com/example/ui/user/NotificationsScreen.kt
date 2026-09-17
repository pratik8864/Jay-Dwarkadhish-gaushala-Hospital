package com.example.ui.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.VolunteerActivism
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppSettings
import com.example.data.model.User
import com.example.ui.UserScreen
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class NotificationModel(
  val id: String,
  val title: String,
  val description: String,
  val date: String,
  val icon: ImageVector,
  val isUnread: Boolean = false,
  val targetScreen: UserScreen? = null
)

@Composable
fun NotificationsScreen(
  settings: AppSettings,
  todayBirthdayUsers: List<User>,
  currentUser: User?,
  onNavigate: (UserScreen) -> Unit
) {
  val todayFormatted = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())

  val notifications = buildList {
    if (settings.isAnnouncementActive && settings.announcementText.isNotBlank()) {
      add(
        NotificationModel(
          id = "announcement",
          title = "તાજી જાહેરાત (Gaushala Announcement)",
          description = settings.announcementText,
          date = todayFormatted,
          icon = Icons.Default.Campaign,
          isUnread = true,
          targetScreen = UserScreen.HOME
        )
      )
    }

    if (todayBirthdayUsers.isNotEmpty()) {
      val names = todayBirthdayUsers.joinToString(", ") { it.fullName }
      add(
        NotificationModel(
          id = "birthday",
          title = "આજના જન્મદિવસી ગૌભક્તો 🎂",
          description = "ગૌમાતાના આશીર્વાદ સાથે જન્મદિવસની હાર્દિક શુભેચ્છાઓ: $names",
          date = todayFormatted,
          icon = Icons.Default.Cake,
          isUnread = true,
          targetScreen = UserScreen.SEVA_DONATION
        )
      )
    }

    add(
      NotificationModel(
        id = "daily_seva",
        title = "ગૌ સેવા ઘાસચારો સમર્પણ",
        description = "આજના દિવસે ૬૫૦+ નિરાધાર અને અશક્ત ગાયો માટે લીલો ચારો સેવા અર્પણ કરો.",
        date = todayFormatted,
        icon = Icons.Default.VolunteerActivism,
        isUnread = false,
        targetScreen = UserScreen.SEVA_DONATION
      )
    )

    add(
      NotificationModel(
        id = "festivals",
        title = "પાવન પર્વ ગૌપૂજન મહોત્સવ",
        description = "આગામી પવિત્ર ઉત્સવે ગૌશાળામાં વિશેષ અર્ચના અને દાન યોજના શરૂ થઈ ચૂકી છે.",
        date = "15 સપ્ટે 2026",
        icon = Icons.Default.AutoAwesome,
        isUnread = false,
        targetScreen = UserScreen.FESTIVALS
      )
    )

    add(
      NotificationModel(
        id = "ambulance",
        title = "24x7 ગૌ એમ્બ્યુલન્સ સેવા સક્રિય",
        description = "કોઈપણ અકસ્માત કે બીમાર ગાય માટે ત્વરિત હેલ્પલાઇન 1800 233 4567 પર સંપર્ક કરો.",
        date = "10 સપ્ટે 2026",
        icon = Icons.Default.Emergency,
        isUnread = false,
        targetScreen = UserScreen.SERVICES
      )
    )
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(NavyBackground, NavyDark)
        )
      )
      .padding(horizontal = 16.dp, vertical = 14.dp)
      .testTag("notifications_screen"),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(NavySurfaceVariant),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Notifications,
              contentDescription = null,
              tint = GoldChampagne,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "સૂચનાઓ અને સંદેશાઓ",
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp,
              color = TextWhite
            )
            Text(
              text = "ગૌશાળાના તાજા સમાચાર & અપડેટ્સ",
              fontSize = 11.sp,
              color = TextMutedBlueGray
            )
          }
        }
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(NavySurfaceVariant)
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Text(
            text = "${notifications.size} નવી",
            fontSize = 11.sp,
            color = GoldChampagneLight,
            fontWeight = FontWeight.Bold
          )
        }
      }
      Spacer(modifier = Modifier.height(4.dp))
    }

    items(notifications) { notif ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { notif.targetScreen?.let { onNavigate(it) } }
          .testTag("notification_item_${notif.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
          containerColor = if (notif.isUnread) NavySurfaceVariant else NavySurface
        ),
        border = BorderStroke(
          width = if (notif.isUnread) 1.5.dp else 1.dp,
          color = if (notif.isUnread) GoldChampagne.copy(alpha = 0.6f) else NavyBorder
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (notif.isUnread) 4.dp else 2.dp)
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
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
              imageVector = notif.icon,
              contentDescription = null,
              tint = GoldChampagne,
              modifier = Modifier.size(22.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = notif.title,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = TextWhite,
                modifier = Modifier.weight(1f)
              )
              if (notif.isUnread) {
                Box(
                  modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(GoldChampagne)
                )
              }
            }

            Spacer(modifier = Modifier.height(3.dp))
            Text(
              text = notif.description,
              fontSize = 11.sp,
              color = TextMutedBlueGray,
              lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = notif.date,
              fontSize = 10.sp,
              color = TextGoldenHighlight,
              fontWeight = FontWeight.Medium
            )
          }

          Spacer(modifier = Modifier.width(8.dp))

          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = GoldChampagne.copy(alpha = 0.7f),
            modifier = Modifier.size(14.dp)
          )
        }
      }
    }
  }
}
