package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// ==========================================
// 🎨 MASTER COLOR THEME: LUXURY DARK NAVY & CHAMPAGNE GOLD
// ==========================================

// Deep Navy Blue Canvas & Surfaces
val NavyBackground = Color(0xFF062B40) // #062B40 Master Deep Navy Blue
val NavyDark = Color(0xFF041E2E)       // Dark Royal Navy base
val NavySurface = Color(0xFF0A364F)    // Card background slightly lighter than canvas
val NavySurfaceVariant = Color(0xFF0E4362)
val NavySurfaceElevated = Color(0xFF134E72)

// Subtle Blue & Golden Borders
val NavyBorder = Color(0xFF1A5377)        // Subtle blue border
val NavyBorderLight = Color(0xFF26658E)
val GoldBorder = Color(0x66D4AF37)        // Thin golden border
val GoldBorderSubtle = Color(0x33D4AF37)

// Soft Golden / Champagne Gold Luxury Accents
val GoldChampagne = Color(0xFFD4AF37)      // Soft Champagne Gold
val GoldChampagneLight = Color(0xFFE8C87A) // Light Champagne highlight
val GoldChampagneDark = Color(0xFFB8932C)
val GoldCream = Color(0xFFF7E7CE)          // Light cream for icons
val GoldGlow = Color(0x33D4AF37)           // Subtle aura

// Typography Hierarchy
val TextWhite = Color(0xFFFFFFFF)          // White text for headings
val TextOffWhite = Color(0xFFF1F6F9)
val TextMutedBlueGray = Color(0xFF90A8B8)  // Light muted blue-gray for descriptions
val TextGoldenHighlight = Color(0xFFE5C158)// Golden for numbers & highlights

// Subtle Semantic Accents (Muted, Never Neon)
val StatusGreenMuted = Color(0xFF388E3C)
val StatusRedMuted = Color(0xFFC0392B)
val StatusRedDark = Color(0xFF5A1A22)

// ==========================================
// BACKWARD COMPATIBILITY TOKENS
// (Mapped to Master Navy + Gold Palette)
// ==========================================
val SaffronPrimary = GoldChampagne
val SaffronVariant = GoldChampagneDark
val SaffronLight = GoldChampagneLight
val SaffronPale = NavySurfaceVariant

val TempleGold = GoldChampagne
val GoldLight = GoldChampagneLight
val SacredCrimson = StatusRedMuted
val SacredMaroon = StatusRedDark

val GauGreen = StatusGreenMuted
val GauGreenLight = Color(0xFF133626)

val WarmIvory = NavyBackground
val WarmSurface = NavySurface
val WarmSurfaceVariant = NavySurfaceVariant
val WarmBorder = NavyBorder

val DarkBackground = NavyBackground
val DarkSurface = NavySurface
val DarkSurfaceVariant = NavySurfaceVariant
val DarkGold = GoldChampagne
