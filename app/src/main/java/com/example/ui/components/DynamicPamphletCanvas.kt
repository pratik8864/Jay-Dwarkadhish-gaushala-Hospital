package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.remember
import com.example.data.model.TemplateFieldHelper
import com.example.data.model.TemplateFieldSetting
import com.example.ui.admin.GaushalaDesignBackground
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.data.model.AppSettings
import com.example.data.model.Donation
import com.example.data.model.PamphletTheme
import com.example.ui.theme.GauGreen
import com.example.ui.theme.SacredCrimson
import com.example.ui.theme.SacredMaroon
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.TempleGold

/**
 * Utility to parse hex color safely with default fallback
 */
fun parseColorHex(hex: String, fallback: Color): Color {
  return try {
    val clean = hex.removePrefix("#")
    if (clean.length == 6) {
      Color((0xFF000000 or clean.toLong(16)))
    } else if (clean.length == 8) {
      Color(clean.toLong(16))
    } else {
      fallback
    }
  } catch (_: Exception) {
    fallback
  }
}

/**
 * Dynamic placeholder resolution engine for Pamphlet Templates
 * Replaces all {{KEY}} tags with actual donation and branding data.
 */
fun resolvePlaceholders(
  template: String,
  donation: Donation,
  settings: AppSettings
): String {
  var result = template
  val gamDisplay = if (donation.gam.isNotBlank()) donation.gam else "આણંદ"
  val talukaDisplay = if (donation.taluka.isNotBlank()) donation.taluka else ""
  val districtDisplay = if (donation.district.isNotBlank()) donation.district else ""
  val villageFull = listOf(gamDisplay, talukaDisplay, districtDisplay).filter { it.isNotBlank() }.joinToString(", ")

  result = result.replace("{{DONOR_NAME}}", donation.donorName.ifBlank { "શ્રી દાતાશ્રી" })
  result = result.replace("{{GAM}}", villageFull)
  result = result.replace("{{VILLAGE}}", villageFull)
  result = result.replace("{{TALUKA}}", talukaDisplay)
  result = result.replace("{{DISTRICT}}", districtDisplay)
  result = result.replace("{{AMOUNT}}", donation.amount.toInt().toString())
  result = result.replace("{{DATE}}", donation.date)
  result = result.replace("{{TIME}}", donation.time)
  result = result.replace("{{DONATION_ID}}", donation.id)
  result = result.replace("{{SEVA_TYPE}}", donation.donationType)
  result = result.replace("{{PAYMENT_TYPE}}", donation.paymentType)
  result = result.replace("{{DEDICATED_TO}}", donation.dedicatedTo.ifBlank { "ગૌસેવા સમર્પણ" })
  result = result.replace("{{FESTIVAL_NAME}}", donation.dedicatedTo.ifBlank { "મહામંગલ પર્વ" })
  result = result.replace("{{GAUSHALA_NAME}}", settings.gaushalaName)
  result = result.replace("{{GAUSHALA_PHONE}}", settings.contactPhone)
  result = result.replace("{{GAUSHALA_ADDRESS}}", settings.address)
  result = result.replace("{{GAUSHALA_EMAIL}}", settings.email)
  return result
}

/**
 * Core Dynamic Gaushala Pamphlet Canvas
 * Fully reflects the Theme configured by Admin with Live Placeholders, Custom Colors,
 * Border Styles, Cow Illustrations, and Donor Photo layouts.
 */
@Composable
fun DynamicPamphletCanvas(
  donation: Donation,
  theme: PamphletTheme,
  settings: AppSettings,
  modifier: Modifier = Modifier,
  isPreview: Boolean = false
) {
  // If Gaushala Admin uploaded their own ready-made Canva/Photoshop template,
  // render the exact design with dynamic field overlays!
  if (theme.isCustomUpload) {
    CustomUploadedPamphletCanvas(
      donation = donation,
      theme = theme,
      settings = settings,
      modifier = modifier,
      isPreview = isPreview
    )
    return
  }

  val context = LocalContext.current

  // Resolve Theme Colors
  val primaryBg = parseColorHex(theme.bgPrimaryColorHex, Color(0xFFFFFDF5))
  val secondaryBg = parseColorHex(theme.bgSecondaryColorHex, Color(0xFFFFF3E0))
  val accentColor = parseColorHex(theme.accentColorHex, SaffronPrimary)
  val borderColor = parseColorHex(theme.borderColorHex, TempleGold)

  // Background Brush
  val bgBrush = when (theme.backgroundType) {
    "SOLID" -> SolidColor(primaryBg)
    "PATTERN" -> Brush.verticalGradient(listOf(primaryBg, secondaryBg.copy(alpha = 0.5f), primaryBg))
    else -> Brush.verticalGradient(listOf(primaryBg, secondaryBg, primaryBg))
  }

  // Border Style
  val borderWidth = theme.borderWidthDp.coerceIn(1, 8).dp
  val borderShape = RoundedCornerShape(16.dp)

  // Typography Family
  val fontFamily = when (theme.fontStyle) {
    "SERIF" -> FontFamily.Serif
    "CURSIVE" -> FontFamily.Cursive
    else -> FontFamily.SansSerif
  }

  // Resolved dynamic texts
  val resolvedTitle = resolvePlaceholders(theme.headerTitleGujarati, donation, settings)
  val resolvedSubTitle = resolvePlaceholders(theme.subTitleGujarati, donation, settings)
  val resolvedBlessing = resolvePlaceholders(theme.blessingMessageTemplate, donation, settings)
  val resolvedFooter = resolvePlaceholders(theme.footerText, donation, settings)

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("dynamic_pamphlet_card"),
    shape = borderShape,
    border = BorderStroke(borderWidth, borderColor),
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    colors = CardDefaults.cardColors(containerColor = primaryBg)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(bgBrush)
        .padding(14.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .border(
            width = if (theme.borderStyle == "DOUBLE_SACRED") 1.5.dp else 1.dp,
            color = borderColor.copy(alpha = 0.7f),
            shape = RoundedCornerShape(12.dp)
          )
          .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Ornate Sacred Vedic Invocations Header
        Text(
          text = "॥ શ્રી ગણેશાય નમઃ ॥   ॥ ૐ નમો ભગવતે વાસુદેવાય ॥",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = SacredCrimson,
          fontFamily = fontFamily
        )
        Text(
          text = resolvedSubTitle,
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          color = accentColor,
          textAlign = TextAlign.Center,
          fontFamily = fontFamily,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Gaushala Trust Header + Logo
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          if (theme.showGaushalaLogo) {
            Image(
              painter = painterResource(id = R.drawable.img_app_icon),
              contentDescription = "ગૌશાળા લોગો",
              modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .border(2.dp, borderColor, CircleShape),
              contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(10.dp))
          }

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = settings.gaushalaName,
              fontSize = (theme.headingFontSizeSp).sp,
              fontWeight = FontWeight.ExtraBold,
              color = SacredMaroon,
              textAlign = TextAlign.Center,
              fontFamily = fontFamily
            )
            if (theme.showGaushalaAddress) {
              Text(
                text = settings.address,
                fontSize = 10.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider(color = borderColor.copy(alpha = 0.5f), thickness = 1.dp)
        Spacer(modifier = Modifier.height(10.dp))

        // Title of the specific Seva / Celebration
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = accentColor.copy(alpha = 0.12f),
          border = BorderStroke(1.dp, accentColor.copy(alpha = 0.4f)),
          modifier = Modifier.padding(horizontal = 4.dp)
        ) {
          Text(
            text = resolvedTitle,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = accentColor,
            textAlign = TextAlign.Center,
            fontFamily = fontFamily,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Donor Photo & Cow Illustration Visual Layout
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Donor Photo Section (Configurable Shape & Visibility)
          if (theme.showDonorPhoto) {
            DonorPhotoWidget(
              photoUri = donation.donorPhotoUri,
              donorName = donation.donorName,
              shapeType = theme.photoShape,
              sizeDp = theme.photoSizeDp.dp,
              borderColor = borderColor
            )
          } else {
            Spacer(modifier = Modifier.width(1.dp))
          }

          // Center: Dedicated Note or Seva Type
          if (donation.dedicatedTo.isNotBlank()) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
            ) {
              Text(
                text = "વિશેષ સમર્પણ:",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray
              )
              Text(
                text = donation.dedicatedTo,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = SacredMaroon,
                textAlign = TextAlign.Center,
                fontFamily = fontFamily
              )
            }
          }

          // Cow Illustration (Positioned Right / Center / Custom)
          if (theme.cowIllustrationType != "NONE") {
            CowIllustrationWidget(
              illustrationType = theme.cowIllustrationType,
              cowSize = theme.cowSize,
              borderColor = borderColor
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Spiritual Blessing Box (Template with resolved placeholders)
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = primaryBg.copy(alpha = 0.95f),
          border = BorderStroke(1.dp, borderColor.copy(alpha = 0.4f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = resolvedBlessing,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF37474F),
            textAlign = TextAlign.Center,
            fontFamily = fontFamily,
            lineHeight = 18.sp,
            modifier = Modifier.padding(10.dp)
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Donor & Donation Details Card
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = secondaryBg.copy(alpha = 0.35f),
          border = BorderStroke(1.dp, borderColor.copy(alpha = 0.4f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(10.dp)
          ) {
            // Donor Name & Village
            if (theme.showDonorName) {
              DetailRow(
                icon = Icons.Default.AccountCircle,
                label = "શ્રી દાતાશ્રી:",
                value = donation.donorName.ifBlank { "શ્રી દાતાશ્રી" },
                accentColor = accentColor,
                isBold = true
              )
            }

            if (theme.showGam && donation.gam.isNotBlank()) {
              DetailRow(
                icon = Icons.Default.LocationOn,
                label = "ગામ / શહેર:",
                value = listOf(donation.gam, donation.taluka, donation.district)
                  .filter { it.isNotBlank() }
                  .joinToString(", "),
                accentColor = accentColor
              )
            }

            // Amount Donated (Highlighted in Gold/Crimson)
            if (theme.showAmount) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.Receipt,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "દાન સમર્પણ રકમ:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray
                  )
                }

                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = SacredCrimson,
                  shadowElevation = 2.dp
                ) {
                  Text(
                    text = "₹ ${donation.amount.toInt()} /-",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                  )
                }
              }
            }

            // Seva Type
            DetailRow(
              icon = Icons.Default.CheckCircle,
              label = "સેવા પ્રકાર:",
              value = donation.donationType,
              accentColor = accentColor
            )

            // Payment Mode
            if (theme.showPaymentType) {
              DetailRow(
                icon = Icons.Default.Payment,
                label = "ચુકવણી મોડ:",
                value = "${donation.paymentType} (વેરિફાઈડ)",
                accentColor = accentColor
              )
            }

            // Receipt & Date Row
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              if (theme.showDonationId) {
                Text(
                  text = "રસીદ નં: ${donation.id}",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.DarkGray
                )
              }
              if (theme.showDate) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(12.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "${donation.date} ${donation.time}",
                    fontSize = 10.sp,
                    color = Color.DarkGray
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Security, 80G Tax Exemption & Verification Badges
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          if (theme.showQrVerification) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.QrCode,
                contentDescription = "QR Code",
                tint = Color.DarkGray,
                modifier = Modifier.size(28.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Column {
                Text(
                  text = "100% વેરિફાઈડ ઈ-પાવતી",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = GauGreen
                )
                Text(
                  text = "સિક્યોર ડિજિટલ રેકોર્ડ",
                  fontSize = 9.sp,
                  color = Color.Gray
                )
              }
            }
          }

          if (theme.showTaxExemption80G) {
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = Color(0xFFE8F5E9),
              border = BorderStroke(0.8.dp, GauGreen)
            ) {
              Text(
                text = "આવકવેરા કલમ 80G માન્ય",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = GauGreen,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = borderColor.copy(alpha = 0.4f), thickness = 0.8.dp)
        Spacer(modifier = Modifier.height(6.dp))

        // Footer Text
        Text(
          text = resolvedFooter,
          fontSize = 10.sp,
          color = Color.Gray,
          textAlign = TextAlign.Center,
          modifier = Modifier.padding(horizontal = 4.dp)
        )

        if (theme.showGaushalaPhone) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 2.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Call,
              contentDescription = null,
              tint = SaffronPrimary,
              modifier = Modifier.size(11.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = settings.contactPhone,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = Color.DarkGray
            )
          }
        }
      }
    }
  }

  if (!isPreview) {
    Spacer(modifier = Modifier.height(14.dp))

    // Action Buttons: WhatsApp Share, System Share, Download
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Button(
        onClick = {
          sharePamphletOnWhatsApp(context, donation, settings)
        },
        modifier = Modifier
          .weight(1f)
          .testTag("pamphlet_whatsapp_button"),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
      ) {
        Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("વોટ્સએપ શેર", fontWeight = FontWeight.Bold, fontSize = 13.sp)
      }

      OutlinedButton(
        onClick = {
          sharePamphletGeneric(context, donation, settings)
        },
        modifier = Modifier
          .weight(1f)
          .testTag("pamphlet_share_button")
      ) {
        Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("શેર", fontSize = 13.sp)
      }

      OutlinedButton(
        onClick = {
          Toast.makeText(context, "પત્રિકા સાચવી લેવામાં આવી! (Saved to Gallery)", Toast.LENGTH_SHORT).show()
        },
        modifier = Modifier.testTag("pamphlet_download_button")
      ) {
        Icon(imageVector = Icons.Default.Download, contentDescription = "સાચવો")
      }
    }
  }
}

/**
 * Renders the Gaushala's OWN uploaded template design (Canva / Photoshop / JPG / PNG / WebP).
 *
 * CRITICAL REQUIREMENTS RESPECTED:
 * 1. The uploaded design is NOT altered or replaced.
 * 2. No unwanted artificial borders, graphics, or layout changes are forced.
 * 3. Only the dynamic fields configured by the Admin (Photo, Name, Date, Gam, Amount, etc.)
 *    are overlaid at the exact positions configured.
 * 4. Old pamphlets remain protected through themeSnapshotJson.
 */
@Composable
fun CustomUploadedPamphletCanvas(
  donation: Donation,
  theme: PamphletTheme,
  settings: AppSettings,
  modifier: Modifier = Modifier,
  isPreview: Boolean = false
) {
  val context = LocalContext.current
  val fields = remember(theme.fieldsConfigJson) {
    TemplateFieldHelper.fromJson(theme.fieldsConfigJson)
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("custom_uploaded_pamphlet_card"),
    shape = RoundedCornerShape(12.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White)
  ) {
    BoxWithConstraints(
      modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(3f / 4f)
        .background(Color.White)
    ) {
      val canvasWidthDp = maxWidth
      val canvasHeightDp = maxHeight

      // 1. EXACT UPLOADED GAUSHALA DESIGN (Preserved 100% as uploaded by Gaushala Admin)
      GaushalaDesignBackground(
        templateImageUri = theme.templateImageUri,
        modifier = Modifier.fillMaxSize()
      )

      // 2. DYNAMIC FIELD OVERLAYS (Admin-configured positions and styling)
      fields.forEach { field ->
        if (field.isEnabled) {
          val leftOffsetDp = canvasWidthDp * (field.xPercent / 100f)
          val topOffsetDp = canvasHeightDp * (field.yPercent / 100f)

          Box(
            modifier = Modifier.offset(x = leftOffsetDp, y = topOffsetDp)
          ) {
            RenderRealDonationFieldItem(
              field = field,
              donation = donation,
              settings = settings
            )
          }
        }
      }
    }
  }

  if (!isPreview) {
    Spacer(modifier = Modifier.height(14.dp))

    // Action Buttons: WhatsApp Share, System Share, Download
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Button(
        onClick = {
          sharePamphletOnWhatsApp(context, donation, settings)
        },
        modifier = Modifier
          .weight(1f)
          .testTag("pamphlet_whatsapp_button"),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
      ) {
        Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("વોટ્સએપ શેર", fontWeight = FontWeight.Bold, fontSize = 13.sp)
      }

      OutlinedButton(
        onClick = {
          sharePamphletGeneric(context, donation, settings)
        },
        modifier = Modifier
          .weight(1f)
          .testTag("pamphlet_share_button")
      ) {
        Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("શેર", fontSize = 13.sp)
      }

      OutlinedButton(
        onClick = {
          Toast.makeText(context, "પત્રિકા સાચવી લેવામાં આવી! (Saved to Gallery)", Toast.LENGTH_SHORT).show()
        },
        modifier = Modifier.testTag("pamphlet_download_button")
      ) {
        Icon(imageVector = Icons.Default.Download, contentDescription = "સાચવો")
      }
    }
  }
}

@Composable
private fun RenderRealDonationFieldItem(
  field: TemplateFieldSetting,
  donation: Donation,
  settings: AppSettings
) {
  val context = LocalContext.current
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

  if (field.fieldKey == "PHOTO") {
    val photoShape: Shape = when (field.photoShape) {
      "CIRCLE" -> CircleShape
      "SQUARE" -> RectangleShape
      "ROUNDED" -> RoundedCornerShape(12.dp)
      "GOLD_FRAME" -> RoundedCornerShape(10.dp)
      else -> CircleShape
    }

    Box(
      modifier = Modifier.size(field.photoSizeDp.dp),
      contentAlignment = Alignment.Center
    ) {
      if (donation.donorPhotoUri.isNotBlank()) {
        AsyncImage(
          model = ImageRequest.Builder(context)
            .data(Uri.parse(donation.donorPhotoUri))
            .crossfade(true)
            .error(R.drawable.sample_donor_photo)
            .fallback(R.drawable.sample_donor_photo)
            .build(),
          contentDescription = "દાતા ફોટો",
          modifier = Modifier
            .fillMaxSize()
            .clip(photoShape)
            .border(field.photoBorderWidthDp.dp, borderColor, photoShape),
          contentScale = ContentScale.Crop
        )
      } else {
        Image(
          painter = painterResource(id = R.drawable.sample_donor_photo),
          contentDescription = "Donor Photo",
          modifier = Modifier
            .fillMaxSize()
            .clip(photoShape)
            .border(field.photoBorderWidthDp.dp, borderColor, photoShape),
          contentScale = ContentScale.Crop
        )
      }
    }
  } else {
    val rawValue = when (field.fieldKey) {
      "NAME" -> donation.donorName.ifBlank { "શ્રી દાતાશ્રી" }
      "DECEASED_NAME" -> donation.dedicatedTo.ifBlank { "સ્વ. ગંગાબા પટેલ" }
      "FAMILY_NAME" -> if (donation.gam.isNotBlank()) "${donation.gam} પરિવાર" else "શ્રી ગૌભક્ત પરિવાર"
      "FESTIVAL_NAME" -> donation.dedicatedTo.ifBlank { "પવિત્ર પર્વ" }
      "MESSAGE" -> donation.message.ifBlank { "ગૌમાતા આપના પરિવાર પર સુખ, શાંતિ અને સમૃદ્ધિના આશીર્વાદ વરસાવે." }
      "DATE" -> donation.date
      "GAM" -> if (donation.gam.isNotBlank()) donation.gam else "આણંદ"
      "AMOUNT" -> donation.amount.toInt().toString()
      "PAYMENT_TYPE" -> donation.paymentType
      "DONATION_ID" -> donation.id
      "SEVA_TYPE" -> donation.donationType
      "DEDICATED_TO" -> donation.dedicatedTo.ifBlank { "ગૌસેવા સમર્પણ" }
      "LOGO" -> "॥ શ્રી સુરભિ ગૌશાળા ॥"
      else -> ""
    }

    val displayText = "${field.prefix}$rawValue${field.suffix}"

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

@Composable
private fun DetailRow(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  label: String,
  value: String,
  accentColor: Color,
  isBold: Boolean = false
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 2.5.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = accentColor,
      modifier = Modifier.size(15.dp)
    )
    Spacer(modifier = Modifier.width(6.dp))
    Text(
      text = label,
      fontSize = 12.sp,
      color = Color.DarkGray,
      modifier = Modifier.width(95.dp)
    )
    Text(
      text = value,
      fontSize = 12.sp,
      fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium,
      color = Color.Black
    )
  }
}

@Composable
private fun DonorPhotoWidget(
  photoUri: String,
  donorName: String,
  shapeType: String,
  sizeDp: Dp,
  borderColor: Color
) {
  val shape: Shape = when (shapeType) {
    "ROUNDED" -> RoundedCornerShape(12.dp)
    "SQUARE" -> RectangleShape
    "GOLD_FRAME" -> RoundedCornerShape(10.dp)
    else -> CircleShape
  }

  val borderStroke = if (shapeType == "GOLD_FRAME") {
    BorderStroke(2.5.dp, Brush.linearGradient(listOf(TempleGold, SacredCrimson, TempleGold)))
  } else {
    BorderStroke(2.dp, borderColor)
  }

  Box(
    modifier = Modifier
      .size(sizeDp)
      .clip(shape)
      .border(borderStroke, shape)
      .background(Color(0xFFFFF8E1)),
    contentAlignment = Alignment.Center
  ) {
    if (photoUri.isNotBlank()) {
      AsyncImage(
        model = photoUri,
        contentDescription = "દાતાશ્રીની તસવીર",
        modifier = Modifier.size(sizeDp),
        contentScale = ContentScale.Crop
      )
    } else {
      // Auspicious Avatar Initial
      val initial = donorName.trim().take(1).ifBlank { "શ્રી" }
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        Icon(
          imageVector = Icons.Default.AccountCircle,
          contentDescription = null,
          tint = TempleGold,
          modifier = Modifier.size(sizeDp * 0.55f)
        )
        Text(
          text = initial,
          fontSize = (sizeDp.value * 0.22f).sp,
          fontWeight = FontWeight.Bold,
          color = SacredMaroon
        )
      }
    }
  }
}

@Composable
private fun CowIllustrationWidget(
  illustrationType: String,
  cowSize: String,
  borderColor: Color
) {
  val sizeDp = when (cowSize) {
    "SMALL" -> 46.dp
    "LARGE" -> 84.dp
    else -> 64.dp
  }

  val resId = when (illustrationType) {
    "CALF_MOTHER" -> R.drawable.img_gaumata_hero
    "VINTAGE_RADHA_KRISHNA" -> R.drawable.img_app_icon
    else -> R.drawable.img_gaumata_hero
  }

  Box(
    modifier = Modifier
      .size(sizeDp)
      .clip(RoundedCornerShape(12.dp))
      .border(1.5.dp, borderColor.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
      .background(Color.White.copy(alpha = 0.8f))
  ) {
    Image(
      painter = painterResource(id = resId),
      contentDescription = "ગૌમાતા તસવીર",
      modifier = Modifier.size(sizeDp),
      contentScale = ContentScale.Fit
    )
  }
}

/**
 * Share pre-formatted Gujarati text with donation receipt link to WhatsApp
 */
fun sharePamphletOnWhatsApp(context: Context, donation: Donation, settings: AppSettings) {
  val gamText = if (donation.gam.isNotBlank()) " (ગામ: ${donation.gam})" else ""
  val text = """
    🚩 *॥ શ્રી સુરભિ ગૌશાળા ટ્રસ્ટ - પાવતી & આશીર્વાદ પત્રિકા ॥* 🚩
    
    ગાવો વિશ્વસ્ય માતરઃ • ગૌ સેવા એ જ શ્રેષ્ઠ પ્રભુ સેવા
    
    દાતાશ્રી: *${donation.donorName}*$gamText
    દાન રકમ: *₹ ${donation.amount.toInt()} /-*
    સેવા વિગત: *${donation.donationType}*
    રસીદ નંબર: *${donation.id}*
    તારીખ: *${donation.date} ${donation.time}*
    ચુકવણી: *${donation.paymentType} (સફળ)*
    
    _${settings.birthdayGreetingMessage}_
    
    સંપર્ક: ${settings.contactPhone}
    સરનામું: ${settings.address}
    
    ગૌમાતા આપના પરિવાર પર સુખ, શાંતિ અને સમૃદ્ધિના આશીર્વાદ વરસાવે. જય ગૌમાતા! 🙏
  """.trimIndent()

  val intent = Intent(Intent.ACTION_SEND).apply {
    type = "text/plain"
    `package` = "com.whatsapp"
    putExtra(Intent.EXTRA_TEXT, text)
  }

  try {
    context.startActivity(intent)
  } catch (_: Exception) {
    // Fallback if WhatsApp is not installed
    val fallbackIntent = Intent(Intent.ACTION_SEND).apply {
      type = "text/plain"
      putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(fallbackIntent, "પત્રિકા શેર કરો"))
  }
}

/**
 * Generic system share sheet
 */
fun sharePamphletGeneric(context: Context, donation: Donation, settings: AppSettings) {
  val text = """
    ॥ શ્રી સુરભિ ગૌશાળા ટ્રસ્ટ ॥
    દાતાશ્રી: ${donation.donorName}
    દાન રકમ: ₹ ${donation.amount.toInt()}
    રસીદ નંબર: ${donation.id}
    તારીખ: ${donation.date}
    ગૌમાતાની કૃપા આપના પરિવાર પર સદા બની રહે. જય ગૌમાતા!
  """.trimIndent()

  val intent = Intent(Intent.ACTION_SEND).apply {
    type = "text/plain"
    putExtra(Intent.EXTRA_SUBJECT, "ગૌસેવા દાન પત્રિકા - ${donation.donorName}")
    putExtra(Intent.EXTRA_TEXT, text)
  }
  context.startActivity(Intent.createChooser(intent, "ગૌસેવા પત્રિકા શેર કરો"))
}
