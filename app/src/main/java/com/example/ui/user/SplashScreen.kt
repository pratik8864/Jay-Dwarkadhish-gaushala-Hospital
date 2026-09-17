package com.example.ui.user

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.GoldBorder
import com.example.ui.theme.GoldChampagne
import com.example.ui.theme.GoldChampagneDark
import com.example.ui.theme.GoldChampagneLight
import com.example.ui.theme.NavyBackground
import com.example.ui.theme.NavyDark
import com.example.ui.theme.TextGoldenHighlight
import com.example.ui.theme.TextWhite
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
  gaushalaName: String = "શ્રી સુરભિ ગૌશાળા ટ્રસ્ટ",
  tagline: String = "ગૌ સેવા • માનવ સેવા • જીવદયા",
  onSplashFinished: () -> Unit
) {
  var isVisible by remember { mutableStateOf(false) }

  val transition = rememberInfiniteTransition(label = "SplashAnim")
  val diyaScale by transition.animateFloat(
    initialValue = 0.88f,
    targetValue = 1.12f,
    animationSpec = infiniteRepeatable(
      animation = tween(900, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "DiyaPulse"
  )

  LaunchedEffect(Unit) {
    isVisible = true
    delay(2000)
    onSplashFinished()
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            NavyBackground,
            NavyDark
          )
        )
      )
      .testTag("splash_screen"),
    contentAlignment = Alignment.Center
  ) {
    AnimatedVisibility(
      visible = isVisible,
      enter = fadeIn(tween(800, easing = FastOutSlowInEasing)),
      exit = fadeOut(tween(400))
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(24.dp)
      ) {
        // Sacred Golden Aura
        Box(
          modifier = Modifier
            .size(130.dp)
            .clip(CircleShape)
            .background(
              Brush.radialGradient(
                colors = listOf(
                  GoldChampagne.copy(alpha = 0.35f),
                  GoldChampagneDark.copy(alpha = 0.15f),
                  Color.Transparent
                )
              )
            ),
          contentAlignment = Alignment.Center
        ) {
          // Gaushala Logo Emblem
          Image(
            painter = painterResource(id = R.drawable.img_app_icon),
            contentDescription = "ગૌશાળા મુદ્રા",
            modifier = Modifier
              .size(96.dp)
              .clip(CircleShape)
              .border(2.5.dp, GoldChampagne, CircleShape),
            contentScale = ContentScale.Crop
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Spiritual Diya & Mantra
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.scale(diyaScale)
        ) {
          Text(text = "🪔", fontSize = 20.sp)
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "॥ ૐ ગૌમાત્રે નમઃ ॥",
            color = GoldChampagneLight,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = "🪔", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Gaushala Title
        Text(
          text = gaushalaName,
          color = TextWhite,
          fontSize = 24.sp,
          fontWeight = FontWeight.ExtraBold,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Tagline
        Text(
          text = tagline,
          color = TextGoldenHighlight,
          fontSize = 14.sp,
          fontWeight = FontWeight.Medium,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Skip button with golden gradient
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
              Brush.horizontalGradient(
                listOf(GoldChampagneLight, GoldChampagne, GoldChampagneDark)
              )
            )
            .clickable { onSplashFinished() }
            .padding(horizontal = 24.dp, vertical = 10.dp)
            .testTag("skip_splash_btn"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "પ્રવેશ કરો (Enter App)",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = NavyDark
          )
        }
      }
    }
  }
}
