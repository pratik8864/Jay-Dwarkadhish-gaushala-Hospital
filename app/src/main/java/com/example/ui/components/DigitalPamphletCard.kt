package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AppSettings
import com.example.data.model.Donation
import com.example.data.model.PamphletTheme
import com.example.ui.theme.GoldBorder
import com.example.ui.theme.GoldChampagne
import com.example.ui.theme.NavyBackground
import com.example.ui.theme.TextMutedBlueGray
import com.example.ui.theme.TextWhite

@Composable
fun DigitalPamphletDialog(
  donation: Donation,
  theme: PamphletTheme? = null,
  settings: AppSettings? = null,
  gaushalaName: String = "શ્રી સુરભિ ગૌશાળા ટ્રસ્ટ",
  tagline: String = "ગૌ સેવા • માનવ સેવા • જીવદયા",
  address: String = "ગૌધામ માર્ગ, આણંદ, ગુજરાત - 388001",
  onDismiss: () -> Unit
) {
  val resolvedSettings = settings ?: AppSettings(
    gaushalaName = gaushalaName,
    tagline = tagline,
    address = address
  )

  val resolvedTheme = theme ?: PamphletTheme(
    name = "Default Theme",
    category = donation.donationType
  )

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.96f)
        .padding(12.dp)
        .clip(RoundedCornerShape(20.dp))
        .border(1.dp, GoldBorder, RoundedCornerShape(20.dp))
        .testTag("digital_pamphlet_dialog"),
      shape = RoundedCornerShape(20.dp),
      color = NavyBackground,
      tonalElevation = 8.dp
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Dialog Top Bar with Verified Seal & Golden Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Verified,
              contentDescription = "વેરિફાઈડ",
              tint = GoldChampagne,
              modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "ડિજિટલ ગૌસેવા પત્રિકા / સર્ટિફિકેટ",
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = TextWhite
            )
          }
          IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_pamphlet_button")) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "બંધ કરો",
              tint = TextMutedBlueGray
            )
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Fully Dynamic Canvas Rendering the Theme & Data
        DynamicPamphletCanvas(
          donation = donation,
          theme = resolvedTheme,
          settings = resolvedSettings
        )
      }
    }
  }
}
