package com.example.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppSettings
import com.example.data.model.Donation
import com.example.data.model.PamphletTheme
import com.example.ui.theme.GauGreen
import com.example.ui.theme.SacredCrimson
import com.example.ui.theme.SacredMaroon
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.TempleGold

@Composable
fun PamphletHistoryScreen(
  donations: List<Donation>,
  themes: List<PamphletTheme>,
  settings: AppSettings,
  onViewPamphlet: (Donation, PamphletTheme) -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedCategoryTab by remember { mutableIntStateOf(0) }
  val categories = listOf("All", "Birthday", "General", "Festival", "Shradhanjali", "Cow Seva")

  // Filter donations (Successful donations only)
  val successfulDonations = remember(donations) {
    donations.filter { it.paymentStatus == "SUCCESS" }
  }

  val filteredDonations = remember(successfulDonations, searchQuery, selectedCategoryTab) {
    val cat = categories[selectedCategoryTab]
    successfulDonations.filter { don ->
      val matchesCat = (cat == "All") || don.donationType.equals(cat, ignoreCase = true)
      val query = searchQuery.trim().lowercase()
      val matchesQuery = query.isEmpty() ||
        don.donorName.lowercase().contains(query) ||
        don.mobile.contains(query) ||
        don.gam.lowercase().contains(query) ||
        don.id.lowercase().contains(query)
      matchesCat && matchesQuery
    }
  }

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(16.dp)
      .testTag("pamphlet_history_screen")
  ) {
    // Header
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
      border = BorderStroke(1.5.dp, TempleGold)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "પત્રિકા હિસ્ટ્રી & આર્કાઇવ (Pamphlet History)",
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = SacredMaroon
            )
            Text(
              text = "તમામ દાન પાવતીઓ & આશીર્વાદ પત્રિકાઓ શોધો, ડાઉનલોડ કરો અને શેર કરો.",
              fontSize = 11.sp,
              color = Color(0xFF5D4037)
            )
          }
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = GauGreen,
            contentColor = Color.White
          ) {
            Text(
              text = "કુલ: ${successfulDonations.size}",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Search Box
    OutlinedTextField(
      value = searchQuery,
      onValueChange = { searchQuery = it },
      modifier = Modifier.fillMaxWidth().testTag("pamphlet_search_input"),
      placeholder = { Text("દાતાનું નામ, મોબાઈલ, ગામ અથવા રસીદ ID શોધો...") },
      leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = SaffronPrimary) },
      trailingIcon = {
        if (searchQuery.isNotEmpty()) {
          IconButton(onClick = { searchQuery = "" }) {
            Icon(imageVector = Icons.Default.Clear, contentDescription = "સાફ કરો")
          }
        }
      },
      singleLine = true,
      shape = RoundedCornerShape(12.dp)
    )

    Spacer(modifier = Modifier.height(10.dp))

    // Category Tabs
    ScrollableTabRow(
      selectedTabIndex = selectedCategoryTab,
      edgePadding = 0.dp,
      containerColor = Color.Transparent
    ) {
      categories.forEachIndexed { index, cat ->
        Tab(
          selected = selectedCategoryTab == index,
          onClick = { selectedCategoryTab = index },
          text = {
            val label = when (cat) {
              "All" -> "બધી પત્રિકાઓ"
              "Birthday" -> "જન્મદિવસ"
              "General" -> "સામાન્ય દાન"
              "Festival" -> "તહેવાર"
              "Shradhanjali" -> "શ્રદ્ધાંજલિ"
              "Cow Seva" -> "ગૌ સેવા"
              else -> cat
            }
            Text(label, fontWeight = if (selectedCategoryTab == index) FontWeight.Bold else FontWeight.Normal, fontSize = 12.sp)
          }
        )
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    if (filteredDonations.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.Receipt,
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier.size(56.dp)
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text("કોઈ પત્રિકા મળી નથી", color = Color.Gray, fontSize = 14.sp)
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(filteredDonations, key = { it.id }) { donation ->
          // Resolve theme used for this donation (from themeIdUsed or default for category)
          val resolvedTheme = themes.find { it.id == donation.themeIdUsed }
            ?: themes.find { it.category.equals(donation.donationType, ignoreCase = true) && it.isDefault }
            ?: themes.firstOrNull()
            ?: PamphletTheme(name = "Fallback Theme", category = donation.donationType)

          PamphletHistoryCard(
            donation = donation,
            theme = resolvedTheme,
            onView = { onViewPamphlet(donation, resolvedTheme) }
          )
        }
      }
    }
  }
}

@Composable
private fun PamphletHistoryCard(
  donation: Donation,
  theme: PamphletTheme,
  onView: () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth().testTag("history_pamphlet_card_${donation.id}"),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = donation.donorName,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color.Black
          )
          Spacer(modifier = Modifier.width(8.dp))
          Surface(
            shape = RoundedCornerShape(4.dp),
            color = SaffronPrimary.copy(alpha = 0.12f)
          ) {
            Text(
              text = donation.donationType,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = SaffronPrimary,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "₹ ${donation.amount.toInt()}",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 14.sp,
            color = SacredCrimson
          )

          if (donation.gam.isNotBlank()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(11.dp))
              Text(text = donation.gam, fontSize = 11.sp, color = Color.Gray)
            }
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(11.dp))
            Text(text = donation.date, fontSize = 11.sp, color = Color.Gray)
          }
        }

        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "રસીદ ID: ${donation.id} • થીમ: ${theme.name}",
          fontSize = 10.sp,
          color = Color.DarkGray
        )
      }

      Button(
        onClick = onView,
        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.testTag("view_pamphlet_btn_${donation.id}")
      ) {
        Icon(imageVector = Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("પત્રિકા જુઓ", fontSize = 11.sp, fontWeight = FontWeight.Bold)
      }
    }
  }
}
