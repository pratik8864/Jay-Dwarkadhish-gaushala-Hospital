package com.example.ui.admin

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.data.model.AppSettings
import com.example.data.model.Donation
import com.example.data.model.PamphletTheme
import com.example.data.model.TemplateFieldHelper
import com.example.ui.components.CustomUploadedPamphletCanvas
import com.example.ui.components.DynamicPamphletCanvas
import com.example.ui.components.parseColorHex
import com.example.ui.theme.GauGreen
import com.example.ui.theme.SacredCrimson
import com.example.ui.theme.SacredMaroon
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.TempleGold

/**
 * Pamphlet Themes Management Screen.
 *
 * Supports both:
 * 1. Gaushala Own Uploaded Ready-Made Canva / Photoshop Designs (with dynamic field positioning)
 * 2. Standard Programmatic Gaushala Themes (with typography, borders, and cow illustrations)
 */
@Composable
fun PamphletThemeManagerScreen(
  themes: List<PamphletTheme>,
  settings: AppSettings,
  onEditTheme: (PamphletTheme) -> Unit,
  onCreateNewTheme: (String) -> Unit,
  onDuplicateTheme: (PamphletTheme) -> Unit,
  onDeleteTheme: (Long) -> Unit,
  onToggleActive: (PamphletTheme) -> Unit,
  onSetDefault: (String, Long) -> Unit,
  onApplyThemeToAll: (PamphletTheme) -> Unit
) {
  var selectedCategoryTab by remember { mutableIntStateOf(0) }
  val categories = listOf("All", "Birthday", "General", "Festival", "Shradhanjali", "Cow Seva")

  var themeToDelete by remember { mutableStateOf<PamphletTheme?>(null) }
  var themeToApplyToAll by remember { mutableStateOf<PamphletTheme?>(null) }
  var showUploadDesignDialog by remember { mutableStateOf(false) }
  var previewTheme by remember { mutableStateOf<PamphletTheme?>(null) }

  val filteredThemes = remember(themes, selectedCategoryTab) {
    val cat = categories[selectedCategoryTab]
    if (cat == "All") themes else themes.filter { it.category.equals(cat, ignoreCase = true) }
  }

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(16.dp)
      .testTag("pamphlet_theme_manager_screen")
  ) {
    // Header Banner
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
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "ડાયનેમિક પત્રિકા થીમ સિસ્ટમ (Dynamic Pamphlet Generator)",
              fontWeight = FontWeight.ExtraBold,
              fontSize = 16.sp,
              color = SacredMaroon
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "તમારી પોતાની કેનવા/ફોટોશોપ ડિઝાઇન અપલોડ કરો અથવા તૈયાર થીમ પસંદ કરો.",
              fontSize = 12.sp,
              color = Color(0xFF5D4037)
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Action Buttons Row: Upload My Design + New App Theme
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // Upload My Design Button
          Button(
            onClick = { showUploadDesignDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20)),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .weight(1.3f)
              .testTag("upload_my_design_button")
          ) {
            Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(17.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Upload My Design", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }

          // Create Standard Theme Button
          Button(
            onClick = {
              val targetCat = if (categories[selectedCategoryTab] == "All") "General" else categories[selectedCategoryTab]
              onCreateNewTheme(targetCat)
            },
            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .weight(1f)
              .testTag("create_new_theme_button")
          ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("નવી થીમ", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          Text("કુલ ડિઝાઇન: ${themes.size}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = SaffronPrimary)
          Text("ઓન ડિઝાઇન: ${themes.count { it.isCustomUpload }}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1B5E20))
          Text("સક્રિય: ${themes.count { it.isActive }}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = GauGreen)
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Category Filter Tabs
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
              "All" -> "બધી થીમ"
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

    Spacer(modifier = Modifier.height(12.dp))

    // List of Themes
    LazyColumn(
      modifier = Modifier.weight(1f),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      items(filteredThemes, key = { it.id }) { theme ->
        ThemeItemCard(
          theme = theme,
          onEdit = { onEditTheme(theme) },
          onDuplicate = { onDuplicateTheme(theme) },
          onDelete = { themeToDelete = theme },
          onToggleActive = { onToggleActive(theme) },
          onSetDefault = { onSetDefault(theme.category, theme.id) },
          onApplyToAll = { themeToApplyToAll = theme },
          onLivePreview = { previewTheme = theme }
        )
      }
    }
  }

  // Upload Design Flow Dialog
  if (showUploadDesignDialog) {
    UploadDesignDialog(
      onSelectTemplate = { newTheme ->
        showUploadDesignDialog = false
        onEditTheme(newTheme)
      },
      onDismiss = { showUploadDesignDialog = false }
    )
  }

  // Live Sample Preview Dialog
  previewTheme?.let { currentTheme ->
    LiveSamplePreviewDialog(
      theme = currentTheme,
      settings = settings,
      onDismiss = { previewTheme = null }
    )
  }

  // Delete Confirmation Dialog
  if (themeToDelete != null) {
    AlertDialog(
      onDismissRequest = { themeToDelete = null },
      title = { Text("થીમ ડિલીટ કરવી છે?") },
      text = { Text("'${themeToDelete?.name}' થીમ કાયમ માટે હટાવવામાં આવશે.") },
      confirmButton = {
        Button(
          onClick = {
            themeToDelete?.let { onDeleteTheme(it.id) }
            themeToDelete = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = SacredCrimson)
        ) {
          Text("હા, ડિલીટ કરો")
        }
      },
      dismissButton = {
        TextButton(onClick = { themeToDelete = null }) {
          Text("રદ કરો")
        }
      }
    )
  }

  // Apply to all past donations confirmation dialog
  if (themeToApplyToAll != null) {
    AlertDialog(
      onDismissRequest = { themeToApplyToAll = null },
      title = { Text("બધી જૂની પાવતીઓમાં લાગુ કરો?") },
      text = {
        Text(
          "શું તમે અગાઉના તમામ દાન રેકોર્ડ્સ માટે '${themeToApplyToAll?.name}' થીમ લાગુ કરવા માંગો છો? " +
            "આનાથી તમામ જૂની પત્રિકાઓ આ નવા લુકમાં જોવા મળશે."
        )
      },
      confirmButton = {
        Button(
          onClick = {
            themeToApplyToAll?.let { onApplyThemeToAll(it) }
            themeToApplyToAll = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
        ) {
          Text("હા, લાગુ કરો")
        }
      },
      dismissButton = {
        TextButton(onClick = { themeToApplyToAll = null }) {
          Text("રદ કરો")
        }
      }
    )
  }
}

@Composable
private fun ThemeItemCard(
  theme: PamphletTheme,
  onEdit: () -> Unit,
  onDuplicate: () -> Unit,
  onDelete: () -> Unit,
  onToggleActive: () -> Unit,
  onSetDefault: () -> Unit,
  onApplyToAll: () -> Unit,
  onLivePreview: () -> Unit
) {
  val primaryColor = parseColorHex(theme.bgPrimaryColorHex, Color(0xFFFFFDF5))
  val secondaryColor = parseColorHex(theme.bgSecondaryColorHex, Color(0xFFFFF3E0))
  val borderColor = parseColorHex(theme.borderColorHex, TempleGold)

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("theme_card_${theme.id}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = BorderStroke(if (theme.isDefault) 2.dp else 1.dp, if (theme.isDefault) SaffronPrimary else Color.LightGray.copy(alpha = 0.6f))
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      // Top row: Theme Preview swatch + Name + Category + Default Badge + Custom Badge
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Thumbnail or Color Swatch
        if (theme.isCustomUpload) {
          Box(
            modifier = Modifier
              .size(44.dp)
              .clip(RoundedCornerShape(8.dp))
              .border(1.5.dp, TempleGold, RoundedCornerShape(8.dp))
          ) {
            GaushalaDesignBackground(
              templateImageUri = theme.templateImageUri,
              modifier = Modifier.fillMaxSize()
            )
          }
        } else {
          Box(
            modifier = Modifier
              .size(44.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(Brush.linearGradient(listOf(primaryColor, secondaryColor)))
              .border(1.5.dp, borderColor, RoundedCornerShape(8.dp))
          )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = theme.name,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = Color.Black
            )
          }

          Spacer(modifier = Modifier.height(3.dp))

          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            if (theme.isCustomUpload) {
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color(0xFF1B5E20),
                contentColor = Color.White
              ) {
                Text(
                  text = "CANVA / UPLOADED DESIGN",
                  fontSize = 8.sp,
                  fontWeight = FontWeight.ExtraBold,
                  modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
              }
            }

            if (theme.isDefault) {
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = SaffronPrimary,
                contentColor = Color.White
              ) {
                Text(
                  text = "DEFAULT",
                  fontSize = 8.sp,
                  fontWeight = FontWeight.ExtraBold,
                  modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
              }
            }

            Text(
              text = "શ્રેણી: ${theme.category}",
              fontSize = 11.sp,
              color = Color.Gray
            )
          }
        }

        // Active Switch
        Switch(
          checked = theme.isActive,
          onCheckedChange = { onToggleActive() },
          colors = SwitchDefaults.colors(checkedThumbColor = GauGreen),
          modifier = Modifier.size(36.dp)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))
      HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f))
      Spacer(modifier = Modifier.height(8.dp))

      // Status info row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        if (theme.isCustomUpload) {
          Text(
            text = "ઓન ડિઝાઇન બેકગ્રાઉન્ડ • ડાયનેમિક ફીલ્ડ્સ ઓવરલે",
            fontSize = 11.sp,
            color = Color(0xFF1B5E20),
            fontWeight = FontWeight.Medium
          )
        } else {
          Text(
            text = "ગાય: ${theme.cowIllustrationType} • ફોટો: ${if (theme.showDonorPhoto) "ON (${theme.photoShape})" else "OFF"}",
            fontSize = 11.sp,
            color = Color.DarkGray
          )
        }

        if (!theme.isDefault) {
          OutlinedButton(
            onClick = onSetDefault,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.height(28.dp)
          ) {
            Icon(imageVector = Icons.Default.Star, contentDescription = null, modifier = Modifier.size(12.dp), tint = TempleGold)
            Spacer(modifier = Modifier.width(3.dp))
            Text("મુખ્ય બનાવો", fontSize = 10.sp)
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Action Buttons: Edit / Template Setup, Live Preview, Duplicate, Delete
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Button(
          onClick = onEdit,
          colors = ButtonDefaults.buttonColors(
            containerColor = if (theme.isCustomUpload) Color(0xFF1B5E20) else SaffronPrimary
          ),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier
            .weight(1.3f)
            .height(36.dp)
            .testTag("edit_theme_button_${theme.id}")
        ) {
          Icon(
            imageVector = if (theme.isCustomUpload) Icons.Default.Tune else Icons.Default.Brush,
            contentDescription = null,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = if (theme.isCustomUpload) "ફીલ્ડ્સ એડિટ" else "ડિઝાઈનર એડિટ",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }

        OutlinedButton(
          onClick = onLivePreview,
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier
            .weight(1.1f)
            .height(36.dp)
            .testTag("preview_theme_btn_${theme.id}")
        ) {
          Icon(imageVector = Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(3.dp))
          Text("પ્રીવ્યૂ", fontSize = 11.sp)
        }

        OutlinedButton(
          onClick = onDuplicate,
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier
            .weight(0.9f)
            .height(36.dp)
            .testTag("duplicate_theme_btn_${theme.id}")
        ) {
          Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(13.dp))
          Spacer(modifier = Modifier.width(3.dp))
          Text("નકલ", fontSize = 10.sp)
        }

        IconButton(
          onClick = onDelete,
          modifier = Modifier
            .size(36.dp)
            .testTag("delete_theme_btn_${theme.id}")
        ) {
          Icon(imageVector = Icons.Default.Delete, contentDescription = "ડિલીટ", tint = SacredCrimson)
        }
      }
    }
  }
}

/**
 * Upload My Design Dialog.
 *
 * Allows Admin to:
 * 1. Upload JPG / PNG / WebP from device gallery or camera.
 * 2. Or choose from ready-made Canva Gaushala templates as a starting point.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadDesignDialog(
  onSelectTemplate: (PamphletTheme) -> Unit,
  onDismiss: () -> Unit
) {
  var templateTitle by remember { mutableStateOf("ગૌશાળા કેનવા ડિઝાઇન") }
  var selectedCategory by remember { mutableStateOf("General") }
  var isCategoryDropdownOpen by remember { mutableStateOf(false) }

  val photoPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri: Uri? ->
    if (uri != null) {
      val customTheme = PamphletTheme(
        name = templateTitle.ifBlank { "ગૌશાળા કેનવા પત્રિકા" },
        category = selectedCategory,
        isDefault = true,
        isActive = true,
        isCustomUpload = true,
        templateImageUri = uri.toString(),
        templateImageName = "Uploaded_Design.png",
        fieldsConfigJson = TemplateFieldHelper.toJson(TemplateFieldHelper.getDefaultFields())
      )
      onSelectTemplate(customTheme)
    }
  }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.94f)
        .padding(12.dp)
        .testTag("upload_design_dialog"),
      shape = RoundedCornerShape(16.dp),
      color = MaterialTheme.colorScheme.surface
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(18.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.CloudUpload,
              contentDescription = null,
              tint = Color(0xFF1B5E20),
              modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Upload My Design (મારી ડિઝાઇન અપલોડ કરો)",
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = SacredMaroon
            )
          }

          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Text(
          text = "કેનવા, ફોટોશોપ અથવા અન્ય સોફ્ટવેરમાં બનાવેલ તૈયાર ડિઝાઇન અહીં અપલોડ કરો. ડિઝાઇન યથાવત રહેશે અને તેના પર દાતાની વિગતો આપોઆપ મુકાશે.",
          fontSize = 11.sp,
          color = Color.DarkGray,
          modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )

        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
        Spacer(modifier = Modifier.height(10.dp))

        // Template metadata inputs
        OutlinedTextField(
          value = templateTitle,
          onValueChange = { templateTitle = it },
          label = { Text("ટેમ્પ્લેટનું નામ (Template Name)") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
          expanded = isCategoryDropdownOpen,
          onExpandedChange = { isCategoryDropdownOpen = !isCategoryDropdownOpen },
          modifier = Modifier.fillMaxWidth()
        ) {
          OutlinedTextField(
            value = when (selectedCategory) {
              "General" -> "સામાન્ય સેવા (General)"
              "Birthday" -> "જન્મદિવસ (Birthday)"
              "Festival" -> "તહેવાર (Festival)"
              "Shradhanjali" -> "શ્રદ્ધાંજલિ (Shradhanjali)"
              "Cow Seva" -> "ગૌ સેવા (Cow Seva)"
              else -> selectedCategory
            },
            onValueChange = {},
            readOnly = true,
            label = { Text("સેવા પ્રકાર (Category)") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryDropdownOpen) },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
          )
          ExposedDropdownMenu(
            expanded = isCategoryDropdownOpen,
            onDismissRequest = { isCategoryDropdownOpen = false }
          ) {
            listOf("General", "Birthday", "Festival", "Shradhanjali", "Cow Seva", "Custom").forEach { cat ->
              DropdownMenuItem(
                text = { Text(cat) },
                onClick = {
                  selectedCategory = cat
                  isCategoryDropdownOpen = false
                }
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Option A: Pick file from Device (JPG, PNG, WebP)
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable {
              photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
              )
            },
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
          border = BorderStroke(1.5.dp, Color(0xFF2E7D32))
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(
              imageVector = Icons.Default.Image,
              contentDescription = null,
              tint = Color(0xFF1B5E20),
              modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "મોબાઇલમાંથી ડિઝાઇન પસંદ કરો (Upload from Device)",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = Color(0xFF1B5E20)
            )
            Text(
              text = "સપોર્ટેડ ફોર્મેટ્સ: JPG, PNG, WebP",
              fontSize = 11.sp,
              color = Color.DarkGray
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
          HorizontalDivider(modifier = Modifier.weight(1f), thickness = 0.5.dp)
          Text(" અથવા તૈયાર કેનવા ટેમ્પલેટ પસંદ કરો ", fontSize = 11.sp, color = Color.Gray)
          HorizontalDivider(modifier = Modifier.weight(1f), thickness = 0.5.dp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Option B: Choose Pre-seeded Canva Gaushala Templates
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          listOf(
            Triple(
              "સામાન્ય ગૌ સેવા કેનવા ડિઝાઇન",
              "canva_gaushala_general",
              R.drawable.canva_gaushala_general
            ),
            Triple(
              "જન્મદિવસ વિશેષ કેનવા ડિઝાઇન",
              "canva_gaushala_birthday",
              R.drawable.canva_gaushala_birthday
            ),
            Triple(
              "દિવાળી પર્વ વિશેષ કેનવા ડિઝાઇન",
              "canva_gaushala_diwali",
              R.drawable.canva_gaushala_diwali
            )
          ).forEach { (title, key, resId) ->
            Card(
              modifier = Modifier
                .width(130.dp)
                .clickable {
                  val presetTheme = PamphletTheme(
                    name = title,
                    category = selectedCategory,
                    isDefault = true,
                    isActive = true,
                    isCustomUpload = true,
                    templateImageUri = "drawable:$key",
                    templateImageName = "$key.jpg",
                    fieldsConfigJson = TemplateFieldHelper.toJson(TemplateFieldHelper.getDefaultFields())
                  )
                  onSelectTemplate(presetTheme)
                },
              shape = RoundedCornerShape(10.dp),
              elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
              border = BorderStroke(1.dp, TempleGold)
            ) {
              Column {
                Image(
                  painter = painterResource(id = resId),
                  contentDescription = title,
                  modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f / 4f),
                  contentScale = ContentScale.Crop
                )
                Text(
                  text = title,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  maxLines = 2,
                  modifier = Modifier.padding(6.dp),
                  color = SacredMaroon
                )
              }
            }
          }
        }
      }
    }
  }
}

/**
 * Live Sample Preview Dialog.
 *
 * Renders the chosen theme using real sample data:
 * Pratik Baku, 17 September 2026, Rajkot, ₹501, Online, GS-00001.
 */
@Composable
fun LiveSamplePreviewDialog(
  theme: PamphletTheme,
  settings: AppSettings,
  onDismiss: () -> Unit
) {
  val sampleDonation = remember {
    Donation(
      id = "GS-00001",
      donorName = "Pratik Baku",
      mobile = "9876543210",
      email = "donor@gaushala.org",
      donationType = if (theme.category != "All") theme.category else "સામાન્ય ગૌ સેવા",
      amount = 501.0,
      dedicatedTo = "શુભ ગૌ સમર્પણ",
      message = "ગૌમાતા આપના પરિવાર પર સુખ, શાંતિ અને સમૃદ્ધિ વરસાવે.",
      date = "17 September 2026",
      time = "10:30 AM",
      paymentStatus = "SUCCESS",
      paymentType = "Online",
      referenceNumber = "RCP-2026-00001",
      gam = "Rajkot",
      taluka = "Rajkot",
      district = "Rajkot",
      donorPhotoUri = ""
    )
  }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.92f)
        .padding(10.dp)
        .testTag("live_sample_preview_dialog"),
      shape = RoundedCornerShape(16.dp),
      color = MaterialTheme.colorScheme.background
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp)
          .verticalScroll(rememberScrollState())
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "લાઇવ ટેમ્પ્લેટ પ્રીવ્યૂ (Live Sample Preview)",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = SacredMaroon
            )
            Text(
              text = "સેમ્પલ ડેટા: Pratik Baku • ₹501 • Rajkot",
              fontSize = 11.sp,
              color = Color.Gray
            )
          }

          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Render Pamphlet Canvas
        if (theme.isCustomUpload) {
          CustomUploadedPamphletCanvas(
            donation = sampleDonation,
            theme = theme,
            settings = settings,
            isPreview = true
          )
        } else {
          DynamicPamphletCanvas(
            donation = sampleDonation,
            theme = theme,
            settings = settings,
            isPreview = true
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
          onClick = onDismiss,
          modifier = Modifier.fillMaxWidth(),
          colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
          shape = RoundedCornerShape(10.dp)
        ) {
          Text("બંધ કરો (Close)", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
