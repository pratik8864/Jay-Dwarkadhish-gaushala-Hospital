package com.example.ui.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Nature
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.GalleryItem
import com.example.data.model.GaushalaEvent
import com.example.data.model.GaushalaService
import com.example.ui.theme.GoldBorder
import com.example.ui.theme.GoldBorderSubtle
import com.example.ui.theme.GoldChampagne
import com.example.ui.theme.GoldChampagneDark
import com.example.ui.theme.GoldChampagneLight
import com.example.ui.theme.NavyBackground
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavySurface
import com.example.ui.theme.NavySurfaceVariant
import com.example.ui.theme.TextGoldenHighlight
import com.example.ui.theme.TextMutedBlueGray
import com.example.ui.theme.TextWhite

@Composable
fun ServicesActivitiesScreen(
  services: List<GaushalaService>,
  events: List<GaushalaEvent>,
  galleryItems: List<GalleryItem>,
  onDonateClick: () -> Unit,
  onEmergencyCall: () -> Unit
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  val tabTitles = listOf("સેવાઓ (Services)", "ફોટો ગેલેરી (Gallery)", "ઇવેન્ટ્સ (Events)")

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(NavyBackground, NavyDark)
        )
      )
      .testTag("services_activities_screen")
  ) {
    // Top Tabs styled in Deep Navy with Champagne Gold Indicator
    ScrollableTabRow(
      selectedTabIndex = selectedTab,
      containerColor = NavyDark,
      contentColor = GoldChampagne,
      edgePadding = 16.dp,
      indicator = { tabPositions ->
        TabRowDefaults.SecondaryIndicator(
          modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
          color = GoldChampagne,
          height = 3.dp
        )
      },
      divider = {
        HorizontalDivider(color = NavyBorder.copy(alpha = 0.5f))
      }
    ) {
      tabTitles.forEachIndexed { index, title ->
        Tab(
          selected = selectedTab == index,
          onClick = { selectedTab = index },
          text = {
            Text(
              text = title,
              fontSize = 13.sp,
              fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
              color = if (selectedTab == index) GoldChampagne else TextMutedBlueGray
            )
          },
          modifier = Modifier.testTag("services_tab_$index")
        )
      }
    }

    when (selectedTab) {
      0 -> ServicesListView(
        services = services,
        onDonateClick = onDonateClick,
        onEmergencyCall = onEmergencyCall
      )
      1 -> GalleryGridView(galleryItems = galleryItems)
      2 -> EventsListView(events = events, onDonateClick = onDonateClick)
    }
  }
}

// -------------------------------------------------------------
// 1. SERVICES LIST VIEW (Horizontal Luxury Cards)
// -------------------------------------------------------------
@Composable
private fun ServicesListView(
  services: List<GaushalaService>,
  onDonateClick: () -> Unit,
  onEmergencyCall: () -> Unit
) {
  val fallbackServices = if (services.isEmpty()) {
    listOf(
      GaushalaService(
        title = "ગાય સારવાર & સર્જરી",
        description = "અકસ્માત કે બીમારીગ્રસ્ત પશુઓની નિષ્ણાત પશુચિકિત્સક દ્વારા સંપૂર્ણ નિઃશુલ્ક સારવાર.",
        contactPhone = "1800 233 4567",
        timing = "24 કલાક ઉપલબ્ધ"
      ),
      GaushalaService(
        title = "ગૌશાળા પરિચર્યા & આશ્રય",
        description = "વૃદ્ધ, અશક્ત અને નિરાધાર ગૌવંશ માટે પ્રેમપૂર્વકનું કાયમી આશ્રય અને સ્નાન-માવજત.",
        contactPhone = "1800 233 4567",
        timing = "નિયમિત સેવા"
      ),
      GaushalaService(
        title = "ચારો વ્યવસ્થા & ઘાસ વિતરણ",
        description = "દરરોજ હજારો કિલો પૌષ્ટિક લીલો અને સૂકો ચારો, દાણ અને ગોળનું વ્યવસ્થિત વિતરણ.",
        contactPhone = "1800 233 4567",
        timing = "દરરોજ સવાર-સાંજ"
      ),
      GaushalaService(
        title = "ઓનલાઇન દાન & પત્રિકા સેવા",
        description = "ઘેર બેઠા પારદર્શક ગૌદાન કરો અને ત્વરિત સર્ટિફિકેટ/પત્રિકા મેળવો.",
        contactPhone = "1800 233 4567",
        timing = "24x7 ઓનલાઇન"
      ),
      GaushalaService(
        title = "એક ગાય દત્તક યોજના",
        description = "એક ગાયના સમગ્ર માસિક ખોરાક અને સારવારનો ખર્ચ સ્વીકારી વિશેષ આશીર્વાદ પ્રાપ્ત કરો.",
        contactPhone = "1800 233 4567",
        timing = "વાર્ષિક / માસિક"
      ),
      GaushalaService(
        title = "મોબાઈલ પશુ ચિકિત્સા & એમ્બ્યુલન્સ",
        description = "તાકીદના કેસો માટે જીપીએસ સજ્જ એમ્બ્યુલન્સ અને ઘટનાસ્થળે સારવાર માટે ડોક્ટરોની ટીમ.",
        contactPhone = "1800 233 4567",
        timing = "24x7 તાત્કાલિક સેવા"
      )
    )
  } else services

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 24x7 Ambulance Helpline Banner
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onEmergencyCall() }
          .testTag("services_emergency_banner"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NavySurface),
        border = BorderStroke(1.dp, GoldBorder)
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .background(
                Brush.radialGradient(
                  listOf(GoldChampagne.copy(alpha = 0.3f), NavyDark)
                )
              )
              .border(1.dp, GoldChampagne, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Emergency,
              contentDescription = "એમ્બ્યુલન્સ",
              tint = GoldChampagne,
              modifier = Modifier.size(24.dp)
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "24x7 ગૌ એમ્બ્યુલન્સ સેવા",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = TextWhite
            )
            Text(
              text = "કોઈપણ ઈજાગ્રસ્ત ગાય દેખાય તો તુરંત જ કોલ કરો",
              fontSize = 11.sp,
              color = TextMutedBlueGray
            )
            Text(
              text = "📞 ટોલ ફ્રી હેલ્પલાઇન: 1800 233 4567",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = TextGoldenHighlight
            )
          }
        }
      }
    }

    // List of Large Rounded Horizontal Cards
    items(fallbackServices) { service ->
      val icon = when {
        service.title.contains("સારવાર") || service.title.contains("ચિકિત્સા") -> Icons.Default.LocalHospital
        service.title.contains("ચારો") || service.title.contains("ઘાસ") -> Icons.Default.Grass
        service.title.contains("દાન") -> Icons.Default.VolunteerActivism
        service.title.contains("દત્તક") -> Icons.Default.Nature
        service.title.contains("એમ્બ્યુલન્સ") -> Icons.Default.Emergency
        else -> Icons.Default.Pets
      }

      val subtitle = when {
        service.title.contains("સારવાર") -> "Cow Treatment & Medical Care"
        service.title.contains("આશ્રય") -> "Gaushala Seva & Shelter"
        service.title.contains("ચારો") -> "Fodder & Daily Nutrition"
        service.title.contains("દાન") -> "Online Seva & Digital Certificate"
        service.title.contains("દત્તક") -> "Adopt a Cow (Gau Dattak)"
        service.title.contains("એમ્બ્યુલન્સ") -> "Mobile Vet Care & Ambulance"
        else -> service.timing
      }

      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onDonateClick() }
          .testTag("service_horizontal_card_${service.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NavySurface),
        border = BorderStroke(1.dp, NavyBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Golden circular icon
          Box(
            modifier = Modifier
              .size(50.dp)
              .clip(CircleShape)
              .background(
                Brush.radialGradient(
                  listOf(GoldChampagne.copy(alpha = 0.25f), NavyDark)
                )
              )
              .border(1.2.dp, GoldChampagne.copy(alpha = 0.6f), CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = icon,
              contentDescription = service.title,
              tint = GoldChampagne,
              modifier = Modifier.size(24.dp)
            )
          }

          Spacer(modifier = Modifier.width(14.dp))

          // Gujarati title + English subtitle + Short description
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = service.title,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = TextWhite
            )
            Text(
              text = subtitle,
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium,
              color = GoldChampagneLight
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = service.description,
              fontSize = 11.sp,
              color = TextMutedBlueGray,
              lineHeight = 15.sp
            )
          }

          Spacer(modifier = Modifier.width(8.dp))

          // Right arrow
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = GoldChampagne.copy(alpha = 0.8f),
            modifier = Modifier.size(16.dp)
          )
        }
      }
    }
  }
}

// -------------------------------------------------------------
// 2. GALLERY VIEW (2-Column Grid with Golden Filter Buttons)
// -------------------------------------------------------------
@Composable
private fun GalleryGridView(galleryItems: List<GalleryItem>) {
  val categories = listOf("બધા (All)", "ગાયો (Cows)", "સેવા (Seva)", "ઇવેન્ટ (Events)")
  var selectedCategory by remember { mutableStateOf("બધા (All)") }

  val fallbackGallery = if (galleryItems.isEmpty()) {
    listOf(
      GalleryItem(title = "કામધેનુ ગૌમાતા દર્શન", category = "ગાયો", imageUrl = "drawable:img_gaumata_hero"),
      GalleryItem(title = "ગૌશાળા ઘાસચારો વિતરણ", category = "સેવા", imageUrl = "drawable:canva_gaushala_general"),
      GalleryItem(title = "ગોપાષ્ટમી મહાપૂજન", category = "ઇવેન્ટ", imageUrl = "drawable:canva_gaushala_diwali"),
      GalleryItem(title = "વાછરડાંનું રમતિયાળ સ્વરૂપ", category = "ગાયો", imageUrl = "drawable:img_gaumata_hero"),
      GalleryItem(title = "જન્મદિવસ ગૌસેવા ઉત્સવ", category = "સેવા", imageUrl = "drawable:canva_gaushala_birthday"),
      GalleryItem(title = "દીપોત્સવી ગૌશાળા દર્શન", category = "ઇવેન્ટ", imageUrl = "drawable:img_diwali_crackers")
    )
  } else galleryItems

  val filteredGallery = fallbackGallery.filter { item ->
    when (selectedCategory) {
      "બધા (All)" -> true
      "ગાયો (Cows)" -> item.category.contains("ગાય") || item.title.contains("ગાય") || item.title.contains("વાછરડું")
      "સેવા (Seva)" -> item.category.contains("સેવા") || item.title.contains("સેવા") || item.title.contains("ચારો")
      "ઇવેન્ટ (Events)" -> item.category.contains("ઇવેન્ટ") || item.title.contains("ઉત્સવ") || item.title.contains("પૂજન") || item.title.contains("દીપોત્સવી")
      else -> true
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 14.dp, vertical = 10.dp)
  ) {
    // Golden Category Filter Buttons
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      contentPadding = PaddingValues(vertical = 6.dp)
    ) {
      items(categories) { category ->
        val isSelected = selectedCategory == category
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .then(
              if (isSelected) {
                Modifier.background(
                  Brush.horizontalGradient(
                    listOf(GoldChampagneLight, GoldChampagne, GoldChampagneDark)
                  )
                )
              } else {
                Modifier.background(NavySurface)
              }
            )
            .border(
              1.dp,
              if (isSelected) GoldChampagne else NavyBorder,
              RoundedCornerShape(20.dp)
            )
            .clickable { selectedCategory = category }
            .padding(horizontal = 14.dp, vertical = 7.dp)
            .testTag("gallery_filter_${category.substringBefore(" ")}")
        ) {
          Text(
            text = category,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) NavyDark else TextWhite
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // 2-Column Grid with Rounded Image Cards
    LazyVerticalGrid(
      columns = GridCells.Fixed(2),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp),
      modifier = Modifier
        .fillMaxSize()
        .testTag("gallery_grid")
    ) {
      items(filteredGallery) { item ->
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = NavySurface),
          border = BorderStroke(1.dp, GoldBorderSubtle),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
            ) {
              val imageRes = when {
                item.imageUrl.contains("diwali") -> R.drawable.canva_gaushala_diwali
                item.imageUrl.contains("birthday") -> R.drawable.canva_gaushala_birthday
                item.imageUrl.contains("crackers") -> R.drawable.img_diwali_crackers
                item.imageUrl.contains("general") -> R.drawable.canva_gaushala_general
                else -> R.drawable.img_gaumata_hero
              }

              Image(
                painter = painterResource(id = imageRes),
                contentDescription = item.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
              )

              Box(
                modifier = Modifier
                  .align(Alignment.TopStart)
                  .padding(6.dp)
                  .clip(RoundedCornerShape(6.dp))
                  .background(NavyDark.copy(alpha = 0.75f))
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text(
                  text = item.category,
                  fontSize = 9.sp,
                  color = GoldChampagneLight,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            Column(modifier = Modifier.padding(10.dp)) {
              Text(
                text = item.title,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = TextWhite,
                maxLines = 1
              )
              Text(
                text = item.category,
                fontSize = 10.sp,
                color = TextMutedBlueGray,
                maxLines = 1
              )
            }
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// 3. EVENTS LIST VIEW
// -------------------------------------------------------------
@Composable
private fun EventsListView(
  events: List<GaushalaEvent>,
  onDonateClick: () -> Unit
) {
  val fallbackEvents = if (events.isEmpty()) {
    listOf(
      GaushalaEvent(
        title = "ગોપાષ્ટમી મહા મહોત્સવ",
        date = "આસો સુદ આઠમ",
        location = "શ્રી સુરભિ ગૌશાળા પરિસર",
        description = "સમસ્ત ગૌવંશનું વૈદિક મંત્રોચ્ચાર સાથે પૂજન, ગો-પરિક્રમા અને વિશેષ મહાપ્રસાદ.",
        time = "સવારે 08:30 થી"
      ),
      GaushalaEvent(
        title = "દિવાળી લક્ષ્મી-ગૌપૂજન ઉત્સવ",
        date = "દિવાળી પર્વ",
        location = "મુખ્ય ગૌધામ",
        description = "દીપ પ્રાગટ્ય, ગૌમાતાને ગોળ-લાડુ અર્પણ અને ભક્તિ સંગીત સંધ્યા.",
        time = "સાંજે 05:00 થી"
      ),
      GaushalaEvent(
        title = "મકરસંક્રાંતિ ગૌ સેવા મહાદાન પર્વ",
        date = "14 જાન્યુઆરી",
        location = "સમસ્ત ગૌશાળા કેન્દ્રો",
        description = "અક્ષય પુણ્ય અર્થે લીલા ઘાસચારાના વિશેષ રથનું સ્વાગત અને સામૂહિક ગૌસેવા.",
        time = "આખો દિવસ"
      )
    )
  } else events

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    items(fallbackEvents) { event ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("event_card_${event.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NavySurface),
        border = BorderStroke(1.dp, GoldBorderSubtle),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = event.title,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = TextWhite
              )
              Spacer(modifier = Modifier.height(3.dp))
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.CalendarMonth,
                  contentDescription = null,
                  tint = GoldChampagne,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "${event.date} • ${event.time}",
                  fontSize = 11.sp,
                  color = TextGoldenHighlight,
                  fontWeight = FontWeight.Medium
                )
              }
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(GoldChampagne.copy(alpha = 0.2f))
                .border(1.dp, GoldChampagne.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = "પાવન ઉત્સવ",
                fontSize = 10.sp,
                color = GoldChampagneLight,
                fontWeight = FontWeight.Bold
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = event.description,
            fontSize = 12.sp,
            color = TextMutedBlueGray,
            lineHeight = 16.sp
          )

          Spacer(modifier = Modifier.height(12.dp))

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(10.dp))
              .background(
                Brush.horizontalGradient(
                  listOf(GoldChampagneLight, GoldChampagne, GoldChampagneDark)
                )
              )
              .clickable { onDonateClick() }
              .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.VolunteerActivism,
                contentDescription = null,
                tint = NavyDark,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "આ ઉત્સવે ગૌસેવા અર્પણ કરો",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = NavyDark
              )
            }
          }
        }
      }
    }
  }
}
