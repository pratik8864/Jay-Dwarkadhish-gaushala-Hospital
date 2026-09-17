package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

// Unified Premium Dark Luxury Scheme: Deep Navy Blue (#062B40) & Champagne Gold (#D4AF37)
private val MasterNavyGoldColorScheme = darkColorScheme(
  primary = GoldChampagne,
  onPrimary = NavyDark,
  primaryContainer = NavySurfaceVariant,
  onPrimaryContainer = GoldChampagneLight,
  secondary = GoldChampagneLight,
  onSecondary = NavyDark,
  secondaryContainer = NavySurfaceElevated,
  onSecondaryContainer = TextOffWhite,
  tertiary = GoldCream,
  onTertiary = NavyDark,
  tertiaryContainer = NavySurfaceVariant,
  onTertiaryContainer = GoldCream,
  background = NavyBackground,
  onBackground = TextOffWhite,
  surface = NavySurface,
  onSurface = TextWhite,
  surfaceVariant = NavySurfaceVariant,
  onSurfaceVariant = TextMutedBlueGray,
  outline = NavyBorder,
  outlineVariant = GoldBorderSubtle
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Preserve bespoke spiritual branding
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = MasterNavyGoldColorScheme,
    typography = Typography,
    content = content
  )
}
