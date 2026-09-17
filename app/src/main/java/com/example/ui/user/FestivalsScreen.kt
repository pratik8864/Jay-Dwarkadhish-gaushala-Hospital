package com.example.ui.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Festival
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
import com.example.ui.theme.TextWhite

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FestivalsScreen(
  festivals: List<Festival>,
  currentUser: User?,
  isFestivalVisible: (Festival) -> Boolean,
  onDonateFestival: (festivalName: String, amount: Double, donorName: String, mobile: String) -> Unit
) {
  val visibleFestivals = festivals.filter { isFestivalVisible(it) }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(NavyBackground, NavyDark, Color(0xFF02131D))
        )
      )
      .padding(16.dp)
      .testTag("festivals_screen"),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = NavySurface),
        border = BorderStroke(1.dp, GoldBorder)
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(48.dp)
              .clip(CircleShape)
              .background(
                Brush.radialGradient(
                  listOf(GoldChampagne.copy(alpha = 0.3f), NavyDark)
                )
              )
              .border(1.dp, GoldChampagne, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Text(text = "🪔", fontSize = 24.sp)
          }
          Spacer(modifier = Modifier.width(14.dp))
          Column {
            Text(
              text = "પવિત્ર તહેવાર ગૌ સેવા મહોત્સવ",
              fontWeight = FontWeight.ExtraBold,
              fontSize = 15.sp,
              color = TextWhite
            )
            Text(
              text = "દિવાળી, ગોપાષ્ટમી અને ઉત્તરાયણ જેવા પાવન પર્વે ગૌપૂજનનું વિશેષ મહત્વ છે.",
              fontSize = 11.sp,
              color = TextMutedBlueGray,
              lineHeight = 15.sp
            )
          }
        }
      }
    }

    if (visibleFestivals.isEmpty()) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "હાલમાં કોઈ તહેવાર સેવા સક્રિય નથી.",
            color = TextMutedBlueGray,
            fontSize = 13.sp
          )
        }
      }
    } else {
      items(visibleFestivals) { festival ->
        FestivalCard(
          festival = festival,
          currentUser = currentUser,
          onDonate = { amount ->
            val donorName = currentUser?.fullName ?: "શ્રી ગૌભક્ત"
            val mobile = currentUser?.mobileNumber ?: "9825000000"
            onDonateFestival(festival.name, amount, donorName, mobile)
          }
        )
      }
    }
  }
}

@Composable
private fun FestivalCard(
  festival: Festival,
  currentUser: User?,
  onDonate: (Double) -> Unit
) {
  val amounts = festival.sevaAmounts.split(",").mapNotNull { it.trim().toDoubleOrNull() }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("festival_card_${festival.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = NavySurface),
    border = BorderStroke(1.dp, NavyBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = festival.name,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,
            color = TextWhite
          )
          Spacer(modifier = Modifier.height(4.dp))
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.CalendarMonth,
              contentDescription = null,
              tint = GoldChampagne,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = festival.dateText,
              fontSize = 12.sp,
              color = TextGoldenHighlight,
              fontWeight = FontWeight.Medium
            )
          }
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(NavySurfaceVariant)
            .border(1.dp, GoldChampagne.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = "વિશેષ પર્વ",
            fontSize = 10.sp,
            color = GoldChampagneLight,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = festival.description,
        fontSize = 12.sp,
        color = TextMutedBlueGray,
        lineHeight = 16.sp
      )

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = "પર્વ સેવા રકમ પસંદ કરો:",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = TextWhite
      )
      Spacer(modifier = Modifier.height(8.dp))

      // Preset donation pills
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        amounts.forEach { amt ->
          OutlinedButton(
            onClick = { onDonate(amt) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, GoldChampagne)
          ) {
            Text(
              text = "₹${amt.toInt()}",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = GoldChampagneLight
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(
            Brush.horizontalGradient(
              listOf(GoldChampagneLight, GoldChampagne, GoldChampagneDark)
            )
          )
          .border(1.dp, GoldChampagne, RoundedCornerShape(12.dp))
          .padding(vertical = 10.dp)
          .testTag("festival_donate_btn_${festival.id}"),
        contentAlignment = Alignment.Center
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.clickable { onDonate(amounts.firstOrNull() ?: 501.0) }
        ) {
          Icon(
            imageVector = Icons.Default.VolunteerActivism,
            contentDescription = null,
            tint = NavyDark,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "પર્વ ગૌ સેવા સમર્પણ કરો",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = NavyDark
          )
        }
      }
    }
  }
}
