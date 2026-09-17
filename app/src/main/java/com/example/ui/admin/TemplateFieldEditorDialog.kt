package com.example.ui.admin

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.OpenWith
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.data.model.PamphletTheme
import com.example.data.model.TemplateFieldHelper
import com.example.data.model.TemplateFieldSetting
import com.example.ui.components.parseColorHex
import com.example.ui.theme.GauGreen
import com.example.ui.theme.SacredCrimson
import com.example.ui.theme.SacredMaroon
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.TempleGold

/**
 * Visual Interactive Template Field Position & Styling Editor.
 *
 * Allows Gaushala Admin to:
 * 1. Display their EXACT uploaded Canva/Photoshop design without any unwanted distortion or extra graphics.
 * 2. Drag & position dynamic fields (Photo, Name, Date, Gam, Amount, Payment, ID, Seva Type, Dedicated To)
 *    directly on the uploaded template.
 * 3. Control typography, font size, colors, borders, background box, and photo shape.
 * 4. Toggle individual fields ON/OFF.
 * 5. See live preview with sample data: Pratik Baku, 17 September 2026, Rajkot, ₹501, Online, GS-00001.
 * 6. Save the customized template.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TemplateFieldEditorDialog(
  theme: PamphletTheme,
  onSave: (PamphletTheme) -> Unit,
  onDismiss: () -> Unit
) {
  val context = LocalContext.current

  var templateName by remember { mutableStateOf(theme.name.ifBlank { "ગૌશાળા ઓન ડિઝાઇન પત્રિકા" }) }
  var category by remember { mutableStateOf(theme.category.ifBlank { "General" }) }
  var isActive by remember { mutableStateOf(theme.isActive) }
  var isDefault by remember { mutableStateOf(theme.isDefault) }
  var isCategoryDropdownOpen by remember { mutableStateOf(false) }

  // Field settings list
  var fields by remember {
    val initial = if (theme.fieldsConfigJson.isNotBlank()) {
      TemplateFieldHelper.fromJson(theme.fieldsConfigJson)
    } else {
      TemplateFieldHelper.getDefaultFields()
    }
    mutableStateOf(initial)
  }

  // Currently selected field for editing
  var selectedFieldKey by remember { mutableStateOf(fields.firstOrNull()?.fieldKey ?: "NAME") }
  val selectedField = fields.find { it.fieldKey == selectedFieldKey } ?: fields.first()

  // Helper to update a field setting
  fun updateField(newSetting: TemplateFieldSetting) {
    fields = fields.map { if (it.fieldKey == newSetting.fieldKey) newSetting else it }
  }

  val categories = listOf("General", "Birthday", "Festival", "Shradhanjali", "Cow Seva", "Custom")

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxSize()
        .padding(6.dp)
        .testTag("template_field_editor_dialog"),
      shape = RoundedCornerShape(16.dp),
      color = MaterialTheme.colorScheme.background
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(10.dp)
      ) {
        // --- Top Bar ---
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Tune,
              contentDescription = null,
              tint = SaffronPrimary,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "ગૌશાળા ઓન ડિઝાઇન એડિટર (Template Field Editor)",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = SacredMaroon
              )
              Text(
                text = "ડિઝાઇન પર ક્લિક અથવા ડ્રેગ કરીને ફીલ્ડ ગોઠવો",
                fontSize = 11.sp,
                color = Color.Gray
              )
            }
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
              onClick = {
                val updatedTheme = theme.copy(
                  name = templateName,
                  category = category,
                  isActive = isActive,
                  isDefault = isDefault,
                  isCustomUpload = true,
                  fieldsConfigJson = TemplateFieldHelper.toJson(fields)
                )
                onSave(updatedTheme)
              },
              colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.testTag("save_template_button")
            ) {
              Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("SAVE TEMPLATE", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.width(6.dp))
            IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_editor_button")) {
              Icon(imageVector = Icons.Default.Close, contentDescription = "બંધ કરો")
            }
          }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 1.dp, color = Color.LightGray.copy(alpha = 0.5f))

        // --- Template Settings Quick Bar ---
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = templateName,
            onValueChange = { templateName = it },
            label = { Text("ટેમ્પ્લેટ નામ", fontSize = 11.sp) },
            singleLine = true,
            modifier = Modifier.weight(1.4f),
            shape = RoundedCornerShape(8.dp)
          )

          ExposedDropdownMenuBox(
            expanded = isCategoryDropdownOpen,
            onExpandedChange = { isCategoryDropdownOpen = !isCategoryDropdownOpen },
            modifier = Modifier.weight(1f)
          ) {
            OutlinedTextField(
              value = when (category) {
                "General" -> "સામાન્ય સેવા"
                "Birthday" -> "જન્મદિવસ"
                "Festival" -> "તહેવાર"
                "Shradhanjali" -> "શ્રદ્ધાંજલિ"
                "Cow Seva" -> "ગૌ સેવા"
                else -> category
              },
              onValueChange = {},
              readOnly = true,
              label = { Text("સેવા પ્રકાર", fontSize = 11.sp) },
              trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryDropdownOpen) },
              modifier = Modifier.menuAnchor(),
              shape = RoundedCornerShape(8.dp)
            )
            ExposedDropdownMenu(
              expanded = isCategoryDropdownOpen,
              onDismissRequest = { isCategoryDropdownOpen = false }
            ) {
              categories.forEach { cat ->
                DropdownMenuItem(
                  text = {
                    Text(
                      when (cat) {
                        "General" -> "સામાન્ય સેવા (General)"
                        "Birthday" -> "જન્મદિવસ સેવા (Birthday)"
                        "Festival" -> "તહેવાર દાન (Festival)"
                        "Shradhanjali" -> "શ્રદ્ધાંજલિ સેવા (Shradhanjali)"
                        "Cow Seva" -> "ગૌ સેવા (Cow Seva)"
                        else -> cat
                      }
                    )
                  },
                  onClick = {
                    category = cat
                    isCategoryDropdownOpen = false
                  }
                )
              }
            }
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
              checked = isDefault,
              onCheckedChange = { isDefault = it }
            )
            Text(text = "Default", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(
              checked = isActive,
              onCheckedChange = { isActive = it },
              colors = SwitchDefaults.colors(checkedThumbColor = SaffronPrimary)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = if (isActive) "Active" else "Off", fontSize = 11.sp)
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- Field Selector Tab Bar ---
        Text(
          text = "ફીલ્ડ પસંદ કરો અને સ્થાન બદલો (Select Field to Position & Style):",
          fontSize = 11.sp,
          fontWeight = FontWeight.SemiBold,
          color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(4.dp))

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          fields.forEach { f ->
            val isSelected = f.fieldKey == selectedFieldKey
            Card(
              modifier = Modifier.clickable { selectedFieldKey = f.fieldKey },
              shape = RoundedCornerShape(8.dp),
              colors = CardDefaults.cardColors(
                containerColor = if (isSelected) SaffronPrimary else if (!f.isEnabled) Color.LightGray.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surfaceVariant
              ),
              border = if (isSelected) BorderStroke(1.5.dp, TempleGold) else BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f))
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                val icon = when (f.fieldKey) {
                  "PHOTO" -> "👤"
                  "NAME" -> "🏷️"
                  "DATE" -> "📅"
                  "GAM" -> "📍"
                  "AMOUNT" -> "💰"
                  "PAYMENT_TYPE" -> "💳"
                  "DONATION_ID" -> "🧾"
                  "SEVA_TYPE" -> "🕉️"
                  else -> "🌺"
                }
                Text(text = icon, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = f.labelGujarati.substringBefore(" ("),
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  color = if (isSelected) Color.White else if (!f.isEnabled) Color.Gray else Color.Black
                )
                if (!f.isEnabled) {
                  Spacer(modifier = Modifier.width(3.dp))
                  Icon(imageVector = Icons.Default.VisibilityOff, contentDescription = "Hidden", modifier = Modifier.size(12.dp), tint = Color.Gray)
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- Main Content: Live Preview Canvas (Top) & Controls Panel (Bottom) ---
        Column(
          modifier = Modifier
            .weight(1f)
            .verticalScroll(rememberScrollState())
        ) {
          // Live Canvas with Gaushala Uploaded Background + Drag Overlay
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .testTag("interactive_canvas_card"),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
          ) {
            BoxWithConstraints(
              modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f)
                .background(Color(0xFFEEEEEE))
            ) {
              val canvasWidthDp = maxWidth
              val canvasHeightDp = maxHeight

              // 1. EXACT UPLOADED GAUSHALA DESIGN BACKGROUND
              GaushalaDesignBackground(
                templateImageUri = theme.templateImageUri,
                modifier = Modifier.fillMaxSize()
              )

              // 2. DYNAMIC FIELD OVERLAYS
              fields.forEach { field ->
                if (field.isEnabled) {
                  val isSelected = field.fieldKey == selectedFieldKey

                  // Position: percentage to DP offset
                  val leftOffsetDp = canvasWidthDp * (field.xPercent / 100f)
                  val topOffsetDp = canvasHeightDp * (field.yPercent / 100f)

                  Box(
                    modifier = Modifier
                      .offset(x = leftOffsetDp, y = topOffsetDp)
                      .pointerInput(field.fieldKey) {
                        detectDragGestures { change, dragAmount ->
                          change.consume()
                          val newXPercent = ((leftOffsetDp.toPx() + dragAmount.x) / size.width.toFloat()) * 100f
                          val newYPercent = ((topOffsetDp.toPx() + dragAmount.y) / size.height.toFloat()) * 100f
                          updateField(
                            field.copy(
                              xPercent = newXPercent.coerceIn(5f, 95f),
                              yPercent = newYPercent.coerceIn(5f, 95f)
                            )
                          )
                        }
                      }
                      .clickable { selectedFieldKey = field.fieldKey }
                  ) {
                    RenderOverlayFieldItem(
                      field = field,
                      isSelected = isSelected
                    )
                  }
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // --- Field Control Panel for Selected Field ---
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.3f))
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              // Header of Control Panel
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = "સેટિંગ્સ: ${selectedField.labelGujarati}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = SacredMaroon
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "[ X: ${selectedField.xPercent.toInt()}% | Y: ${selectedField.yPercent.toInt()}% ]",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SaffronPrimary
                  )
                }

                // Visibility Toggle
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(text = if (selectedField.isEnabled) "ચાલુ (ON)" else "બંધ (OFF)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                  Spacer(modifier = Modifier.width(4.dp))
                  Switch(
                    checked = selectedField.isEnabled,
                    onCheckedChange = { isEnabled ->
                      updateField(selectedField.copy(isEnabled = isEnabled))
                    },
                    colors = SwitchDefaults.colors(checkedThumbColor = GauGreen)
                  )
                }
              }

              HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp)

              // Position Sliders with Nudge Buttons
              Text(text = "સ્થાન ગોઠવણ (Position Controls):", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
              Spacer(modifier = Modifier.height(4.dp))

              // X Position
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(text = "આડી સ્થિતિ (X%):", fontSize = 11.sp, modifier = Modifier.width(86.dp))
                IconButton(
                  onClick = { updateField(selectedField.copy(xPercent = (selectedField.xPercent - 1f).coerceIn(5f, 95f))) },
                  modifier = Modifier.size(28.dp)
                ) {
                  Icon(Icons.Default.Remove, contentDescription = "ડાબે", modifier = Modifier.size(16.dp))
                }
                Slider(
                  value = selectedField.xPercent,
                  onValueChange = { updateField(selectedField.copy(xPercent = it)) },
                  valueRange = 5f..95f,
                  modifier = Modifier.weight(1f)
                )
                IconButton(
                  onClick = { updateField(selectedField.copy(xPercent = (selectedField.xPercent + 1f).coerceIn(5f, 95f))) },
                  modifier = Modifier.size(28.dp)
                ) {
                  Icon(Icons.Default.Add, contentDescription = "જમણે", modifier = Modifier.size(16.dp))
                }
                Text(text = "${selectedField.xPercent.toInt()}%", fontSize = 11.sp, modifier = Modifier.width(36.dp), textAlign = TextAlign.End)
              }

              // Y Position
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(text = "ઊભી સ્થિતિ (Y%):", fontSize = 11.sp, modifier = Modifier.width(86.dp))
                IconButton(
                  onClick = { updateField(selectedField.copy(yPercent = (selectedField.yPercent - 1f).coerceIn(5f, 95f))) },
                  modifier = Modifier.size(28.dp)
                ) {
                  Icon(Icons.Default.Remove, contentDescription = "ઉપર", modifier = Modifier.size(16.dp))
                }
                Slider(
                  value = selectedField.yPercent,
                  onValueChange = { updateField(selectedField.copy(yPercent = it)) },
                  valueRange = 5f..95f,
                  modifier = Modifier.weight(1f)
                )
                IconButton(
                  onClick = { updateField(selectedField.copy(yPercent = (selectedField.yPercent + 1f).coerceIn(5f, 95f))) },
                  modifier = Modifier.size(28.dp)
                ) {
                  Icon(Icons.Default.Add, contentDescription = "નીચે", modifier = Modifier.size(16.dp))
                }
                Text(text = "${selectedField.yPercent.toInt()}%", fontSize = 11.sp, modifier = Modifier.width(36.dp), textAlign = TextAlign.End)
              }

              Spacer(modifier = Modifier.height(8.dp))

              // Specific Controls for PHOTO vs TEXT FIELDS
              if (selectedField.fieldKey == "PHOTO") {
                Text(text = "ફોટો આકાર અને બોર્ડર (Photo Shape & Border):", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  listOf("CIRCLE" to "⭕ વર્તુળાકાર (Circle)", "ROUNDED" to "🔲 રાઉન્ડેડ (Rounded)", "SQUARE" to "⏹️ ચોરસ (Square)", "GOLD_FRAME" to "👑 ગોલ્ડ ફ્રેમ").forEach { (shapeKey, label) ->
                    val isSelected = selectedField.photoShape == shapeKey
                    Button(
                      onClick = { updateField(selectedField.copy(photoShape = shapeKey)) },
                      modifier = Modifier.weight(1f),
                      colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) SaffronPrimary else Color.White
                      ),
                      shape = RoundedCornerShape(8.dp),
                      border = BorderStroke(1.dp, if (isSelected) TempleGold else Color.LightGray)
                    ) {
                      Text(text = label, fontSize = 9.sp, color = if (isSelected) Color.White else Color.Black)
                    }
                  }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Photo Size Slider
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                  Text(text = "સાઇઝ: ${selectedField.photoSizeDp} dp", fontSize = 11.sp, modifier = Modifier.width(90.dp))
                  Slider(
                    value = selectedField.photoSizeDp.toFloat(),
                    onValueChange = { updateField(selectedField.copy(photoSizeDp = it.toInt())) },
                    valueRange = 40f..140f,
                    modifier = Modifier.weight(1f)
                  )
                }

                // Photo Border Width
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                  Text(text = "બોર્ડર: ${selectedField.photoBorderWidthDp} dp", fontSize = 11.sp, modifier = Modifier.width(90.dp))
                  Slider(
                    value = selectedField.photoBorderWidthDp.toFloat(),
                    onValueChange = { updateField(selectedField.copy(photoBorderWidthDp = it.toInt())) },
                    valueRange = 0f..8f,
                    modifier = Modifier.weight(1f)
                  )
                }
              } else {
                // Typography & Text Design Controls
                Text(text = "લખાણ સ્ટાઇલ અને રંગ (Typography & Color):", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(6.dp))

                // Prefix text (e.g. "શ્રી ", "₹ ", "તા. ")
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                  OutlinedTextField(
                    value = selectedField.prefix,
                    onValueChange = { updateField(selectedField.copy(prefix = it)) },
                    label = { Text("પ્રીફિક્સ લખાણ (દા.ત. શ્રી / ₹ / તા.)", fontSize = 10.sp) },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                  )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Font Size Slider
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                  Text(text = "ફોન્ટ સાઇઝ: ${selectedField.fontSizeSp} sp", fontSize = 11.sp, modifier = Modifier.width(105.dp))
                  Slider(
                    value = selectedField.fontSizeSp.toFloat(),
                    onValueChange = { updateField(selectedField.copy(fontSizeSp = it.toInt())) },
                    valueRange = 10f..32f,
                    modifier = Modifier.weight(1f)
                  )
                }

                // Font Weight selector
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                  listOf("NORMAL" to "Normal", "MEDIUM" to "Medium", "BOLD" to "Bold", "EXTRA_BOLD" to "Extra Bold").forEach { (weightKey, label) ->
                    val isSelected = selectedField.fontWeight == weightKey
                    Button(
                      onClick = { updateField(selectedField.copy(fontWeight = weightKey)) },
                      modifier = Modifier.weight(1f),
                      colors = ButtonDefaults.buttonColors(containerColor = if (isSelected) SaffronPrimary else Color.White),
                      shape = RoundedCornerShape(6.dp),
                      border = BorderStroke(1.dp, if (isSelected) TempleGold else Color.LightGray)
                    ) {
                      Text(text = label, fontSize = 9.sp, color = if (isSelected) Color.White else Color.Black)
                    }
                  }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Text Alignment selector
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                  listOf("LEFT" to "ડાબે (Left)", "CENTER" to "કેન્દ્ર (Center)", "RIGHT" to "જમણે (Right)").forEach { (alignKey, label) ->
                    val isSelected = selectedField.textAlign == alignKey
                    Button(
                      onClick = { updateField(selectedField.copy(textAlign = alignKey)) },
                      modifier = Modifier.weight(1f),
                      colors = ButtonDefaults.buttonColors(containerColor = if (isSelected) SaffronPrimary else Color.White),
                      shape = RoundedCornerShape(6.dp),
                      border = BorderStroke(1.dp, if (isSelected) TempleGold else Color.LightGray)
                    ) {
                      Text(text = label, fontSize = 9.sp, color = if (isSelected) Color.White else Color.Black)
                    }
                  }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Text Color Palette Picker
                Text(text = "લખાણનો રંગ (Text Color):", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  listOf(
                    "#800000" to "મરૂન",
                    "#B71C1C" to "લાલ",
                    "#E65100" to "કેસરી",
                    "#1B5E20" to "લીલો",
                    "#0D47A1" to "વાદળી",
                    "#212121" to "કાળો",
                    "#FFFFFF" to "સફેદ"
                  ).forEach { (hex, name) ->
                    val color = parseColorHex(hex, Color.Black)
                    val isSelected = selectedField.textColorHex.equals(hex, ignoreCase = true)
                    Box(
                      modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                          width = if (isSelected) 3.dp else 1.dp,
                          color = if (isSelected) TempleGold else Color.Gray,
                          shape = CircleShape
                        )
                        .clickable { updateField(selectedField.copy(textColorHex = hex)) },
                      contentAlignment = Alignment.Center
                    ) {
                      if (isSelected) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = if (hex == "#FFFFFF") Color.Black else Color.White, modifier = Modifier.size(16.dp))
                      }
                    }
                  }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Background Box & Highlight
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(8.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(text = "બેકગ્રાઉન્ડ પટ્ટી:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                  Button(
                    onClick = { updateField(selectedField.copy(bgColorHex = "TRANSPARENT", borderWidthDp = 0)) },
                    colors = ButtonDefaults.buttonColors(containerColor = if (selectedField.bgColorHex == "TRANSPARENT") SaffronPrimary else Color.White),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, Color.LightGray)
                  ) {
                    Text("પારદર્શક (Transparent)", fontSize = 9.sp, color = if (selectedField.bgColorHex == "TRANSPARENT") Color.White else Color.Black)
                  }
                  Button(
                    onClick = { updateField(selectedField.copy(bgColorHex = "#FFF9C4", borderWidthDp = 1, borderColorHex = "#D4AF37", cornerRadiusDp = 6)) },
                    colors = ButtonDefaults.buttonColors(containerColor = if (selectedField.bgColorHex == "#FFF9C4") SaffronPrimary else Color.White),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, Color.LightGray)
                  ) {
                    Text("ગોલ્ડન પટ્ટી (Gold Box)", fontSize = 9.sp, color = if (selectedField.bgColorHex == "#FFF9C4") Color.White else Color.Black)
                  }
                  Button(
                    onClick = { updateField(selectedField.copy(bgColorHex = "#FFFFFF", borderWidthDp = 1, borderColorHex = "#B0BEC5", cornerRadiusDp = 6)) },
                    colors = ButtonDefaults.buttonColors(containerColor = if (selectedField.bgColorHex == "#FFFFFF") SaffronPrimary else Color.White),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, Color.LightGray)
                  ) {
                    Text("વ્હાઇટ બોક્સ (White)", fontSize = 9.sp, color = if (selectedField.bgColorHex == "#FFFFFF") Color.White else Color.Black)
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}

/**
 * Loads the Gaushala's exact uploaded design without any modification or unwanted borders.
 */
@Composable
fun GaushalaDesignBackground(
  templateImageUri: String,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  when {
    templateImageUri.contains("canva_gaushala_general") -> {
      Image(
        painter = painterResource(id = R.drawable.canva_gaushala_general),
        contentDescription = "Gaushala Canva Design",
        modifier = modifier,
        contentScale = ContentScale.FillBounds
      )
    }
    templateImageUri.contains("canva_gaushala_birthday") -> {
      Image(
        painter = painterResource(id = R.drawable.canva_gaushala_birthday),
        contentDescription = "Gaushala Birthday Design",
        modifier = modifier,
        contentScale = ContentScale.FillBounds
      )
    }
    templateImageUri.contains("canva_gaushala_diwali") -> {
      Image(
        painter = painterResource(id = R.drawable.canva_gaushala_diwali),
        contentDescription = "Gaushala Diwali Design",
        modifier = modifier,
        contentScale = ContentScale.FillBounds
      )
    }
    templateImageUri.startsWith("content://") || templateImageUri.startsWith("file://") || templateImageUri.isNotBlank() -> {
      AsyncImage(
        model = ImageRequest.Builder(context)
          .data(Uri.parse(templateImageUri))
          .crossfade(true)
          .error(R.drawable.canva_gaushala_general)
          .fallback(R.drawable.canva_gaushala_general)
          .build(),
        contentDescription = "Uploaded Gaushala Template",
        modifier = modifier,
        contentScale = ContentScale.FillBounds
      )
    }
    else -> {
      Image(
        painter = painterResource(id = R.drawable.canva_gaushala_general),
        contentDescription = "Default Canva Template",
        modifier = modifier,
        contentScale = ContentScale.FillBounds
      )
    }
  }
}

/**
 * Renders a single dynamic field in the Live Preview / Editor canvas.
 * Uses official sample data: Pratik Baku, 17 September 2026, Rajkot, ₹501, Online, GS-00001.
 */
@Composable
private fun RenderOverlayFieldItem(
  field: TemplateFieldSetting,
  isSelected: Boolean
) {
  val textColor = parseColorHex(field.textColorHex, Color.Black)
  val borderColor = parseColorHex(field.borderColorHex, TempleGold)
  val bgColor = if (field.bgColorHex == "TRANSPARENT") Color.Transparent else parseColorHex(field.bgColorHex, Color.White)

  val fontFamily = when (field.fontStyle) {
    "SERIF" -> FontFamily.Serif
    "CURSIVE" -> FontFamily.Cursive
    "MONOSPACE" -> FontFamily.Monospace
    else -> FontFamily.SansSerif
  }

  val fontWeight = when (field.fontWeight) {
    "NORMAL" -> FontWeight.Normal
    "MEDIUM" -> FontWeight.Medium
    "SEMI_BOLD" -> FontWeight.SemiBold
    "EXTRA_BOLD" -> FontWeight.ExtraBold
    else -> FontWeight.Bold
  }

  val textAlign = when (field.textAlign) {
    "LEFT" -> TextAlign.Start
    "RIGHT" -> TextAlign.End
    else -> TextAlign.Center
  }

  // Selection styling
  val selectionBorder = if (isSelected) {
    BorderStroke(2.dp, SaffronPrimary)
  } else {
    BorderStroke(0.dp, Color.Transparent)
  }

  if (field.fieldKey == "PHOTO") {
    val photoShape: Shape = when (field.photoShape) {
      "CIRCLE" -> CircleShape
      "SQUARE" -> RectangleShape
      "ROUNDED" -> RoundedCornerShape(12.dp)
      "GOLD_FRAME" -> RoundedCornerShape(10.dp)
      else -> CircleShape
    }

    Box(
      modifier = Modifier
        .size(field.photoSizeDp.dp)
        .border(selectionBorder)
        .padding(if (isSelected) 2.dp else 0.dp),
      contentAlignment = Alignment.Center
    ) {
      Image(
        painter = painterResource(id = R.drawable.sample_donor_photo),
        contentDescription = "Sample Donor Photo",
        modifier = Modifier
          .fillMaxSize()
          .clip(photoShape)
          .border(field.photoBorderWidthDp.dp, borderColor, photoShape),
        contentScale = ContentScale.Crop
      )
    }
  } else {
    // Sample dynamic values matching the user's explicit specification
    val sampleValue = when (field.fieldKey) {
      "NAME" -> "Pratik Baku"
      "DECEASED_NAME" -> "સ્વ. શાંતિલાલ પટેલ"
      "FAMILY_NAME" -> "પટેલ પરિવાર, રાજકોટ"
      "FESTIVAL_NAME" -> "શુભ દિવાળી પાવન પર્વ"
      "MESSAGE" -> "ગૌમાતા આપના જીવનમાં સુખ, શાંતિ, સ્વાસ્થ્ય અને દીર્ઘાયુ અર્પે."
      "LOGO" -> "॥ શ્રી સુરભિ ગૌશાળા ટ્રસ્ટ ॥"
      "DATE" -> "17 September 2026"
      "GAM" -> "Rajkot"
      "AMOUNT" -> "501"
      "PAYMENT_TYPE" -> "Online"
      "DONATION_ID" -> "GS-00001"
      "SEVA_TYPE" -> "સામાન્ય ગૌ સેવા"
      "DEDICATED_TO" -> "શુભ ગૌ સમર્પણ"
      else -> "માહિતી"
    }

    val displayText = "${field.prefix}$sampleValue${field.suffix}"

    Box(
      modifier = Modifier
        .clip(RoundedCornerShape(field.cornerRadiusDp.dp))
        .background(bgColor)
        .then(
          if (field.borderWidthDp > 0) {
            Modifier.border(field.borderWidthDp.dp, borderColor, RoundedCornerShape(field.cornerRadiusDp.dp))
          } else {
            Modifier
          }
        )
        .then(
          if (isSelected) {
            Modifier.border(1.5.dp, SaffronPrimary, RoundedCornerShape(field.cornerRadiusDp.dp))
          } else {
            Modifier
          }
        )
        .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
      Text(
        text = displayText,
        fontSize = field.fontSizeSp.sp,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        color = textColor,
        textAlign = textAlign
      )
    }
  }
}
