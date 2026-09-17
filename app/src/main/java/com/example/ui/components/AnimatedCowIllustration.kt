package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.GoldBorder
import com.example.ui.theme.GoldChampagne
import com.example.ui.theme.GoldChampagneDark
import com.example.ui.theme.GoldChampagneLight
import com.example.ui.theme.GoldCream
import com.example.ui.theme.GoldGlow
import com.example.ui.theme.NavyBackground
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavySurface
import com.example.ui.theme.NavySurfaceVariant
import com.example.ui.theme.TextGoldenHighlight
import com.example.ui.theme.TextMutedBlueGray
import com.example.ui.theme.TextWhite

@Composable
fun AnimatedCowHeroBanner(
  modifier: Modifier = Modifier,
  gaushalaName: String = "શ્રી સુરભિ ગૌશાળા ટ્રસ્ટ",
  tagline: String = "ગૌ સેવા • માનવ સેવા • જીવદયા",
  hasBirthdayToday: Boolean = false,
  onCtaClick: () -> Unit = {}
) {
  val infiniteTransition = rememberInfiniteTransition(label = "CowAnim")

  // Gentle breathing motion
  val breathingScale by infiniteTransition.animateFloat(
    initialValue = 0.99f,
    targetValue = 1.015f,
    animationSpec = infiniteRepeatable(
      animation = tween(2600, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "Breathing"
  )

  // Glowing diya flame pulsation
  val diyaGlow by infiniteTransition.animateFloat(
    initialValue = 0.75f,
    targetValue = 1.0f,
    animationSpec = infiniteRepeatable(
      animation = tween(1000, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "DiyaGlow"
  )

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("cow_hero_banner"),
    shape = RoundedCornerShape(22.dp),
    colors = CardDefaults.cardColors(containerColor = NavySurface),
    border = BorderStroke(1.dp, GoldBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(240.dp)
    ) {
      // Background Image of Holy Gaumata and calf
      Image(
        painter = painterResource(id = R.drawable.img_gaumata_hero),
        contentDescription = "પવિત્ર ગૌમાતા અને વાછરડું",
        modifier = Modifier
          .fillMaxSize()
          .scale(breathingScale),
        contentScale = ContentScale.Crop
      )

      // Gradient overlay blending into Deep Navy Blue (#062B40 / #041E2E)
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Brush.verticalGradient(
              colors = listOf(
                NavyDark.copy(alpha = 0.35f),
                NavyBackground.copy(alpha = 0.65f),
                NavyDark.copy(alpha = 0.95f)
              )
            )
          )
      )

      // Sacred Golden Diya on top-right
      Box(
        modifier = Modifier
          .align(Alignment.TopEnd)
          .padding(14.dp)
      ) {
        Canvas(modifier = Modifier.size(44.dp)) {
          drawCircle(
            brush = Brush.radialGradient(
              colors = listOf(
                GoldChampagneLight.copy(alpha = 0.6f * diyaGlow),
                GoldChampagneDark.copy(alpha = 0.25f * diyaGlow),
                Color.Transparent
              )
            ),
            radius = size.minDimension / 1.5f
          )
        }
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(NavySurfaceVariant.copy(alpha = 0.9f))
            .border(1.dp, GoldChampagne, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "🪔",
            fontSize = 18.sp,
            modifier = Modifier.scale(diyaGlow)
          )
        }
      }

      // Holy Mantram Pill on top-left
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .align(Alignment.TopStart)
          .padding(12.dp)
          .clip(RoundedCornerShape(14.dp))
          .background(NavyDark.copy(alpha = 0.85f))
          .border(1.dp, GoldChampagne.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
          .padding(horizontal = 10.dp, vertical = 5.dp)
      ) {
        Text(text = "🕉️", fontSize = 12.sp)
        Spacer(modifier = Modifier.width(5.dp))
        Text(
          text = "ગાવો વિશ્વસ્ય માતરઃ",
          color = GoldChampagne,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold
        )
      }

      // Bottom Gaushala Details, Emotional Description & Golden CTA Button
      Column(
        modifier = Modifier
          .align(Alignment.BottomStart)
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        Text(
          text = gaushalaName,
          color = TextWhite,
          fontSize = 20.sp,
          fontWeight = FontWeight.ExtraBold,
          lineHeight = 26.sp
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = tagline,
          color = TextMutedBlueGray,
          fontSize = 12.sp,
          fontWeight = FontWeight.Normal
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Golden CTA Button
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(
                Brush.horizontalGradient(
                  colors = listOf(GoldChampagneLight, GoldChampagne, GoldChampagneDark)
                )
              )
              .clickable { onCtaClick() }
              .padding(horizontal = 16.dp, vertical = 8.dp)
              .testTag("hero_golden_cta_btn")
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
                text = "♥ ગૌ સેવા અર્પણ કરો",
                color = NavyDark,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }

          if (hasBirthdayToday) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(GoldChampagne.copy(alpha = 0.2f))
                .border(1.dp, GoldChampagne, RoundedCornerShape(10.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = "🎂 આજે આપનો જન્મદિવસ",
                color = GoldChampagneLight,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }
  }
}
