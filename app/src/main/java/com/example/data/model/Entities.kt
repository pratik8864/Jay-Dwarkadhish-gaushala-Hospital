package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val fullName: String,
  val mobileNumber: String,
  val dateOfBirth: String, // format "YYYY-MM-DD" e.g. "1994-09-17"
  val email: String = "",
  val city: String = "",
  val taluka: String = "",
  val district: String = "",
  val registeredAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "donations")
data class Donation(
  @PrimaryKey val id: String, // Unique e.g. "GS-20260917-4921"
  val donorName: String,
  val mobile: String,
  val email: String = "",
  val donationType: String, // "General", "Birthday", "Shradhanjali", "Festival", "Cow Seva", etc.
  val amount: Double,
  val paymentStatus: String, // "SUCCESS", "FAILED", "PENDING", "REFUNDED"
  val date: String, // "YYYY-MM-DD"
  val time: String, // "HH:mm"
  val referenceNumber: String,
  val dedicatedTo: String = "", // e.g. Name of birthday person or late ancestor for Shradhanjali
  val message: String = "",
  val gam: String = "", // Village / Gam
  val taluka: String = "", // Taluka
  val district: String = "", // District
  val donorPhotoUri: String = "", // Selected or uploaded photo URI
  val paymentType: String = "Online UPI", // "Online UPI", "Card / NetBanking", "Cash / Counter"
  val themeIdUsed: Long = 1, // Snapshot reference to PamphletTheme
  val themeSnapshotJson: String = "", // JSON string of the theme configuration at creation time (Version control)
  val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "festivals")
data class Festival(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val name: String,
  val dateText: String,
  val bannerRes: String = "",
  val description: String,
  val sevaAmounts: String = "101,501,1001,2501,5001",
  val startDate: String = "", // "YYYY-MM-DD"
  val endDate: String = "", // "YYYY-MM-DD"
  val isDateControlEnabled: Boolean = false,
  val isActive: Boolean = true,
  val sortOrder: Int = 0
)

@Entity(tableName = "seva_items")
data class SevaItem(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val name: String, // e.g. "ગાય માટે ઘાસ સેવા"
  val description: String,
  val suggestedAmount: Double,
  val isCustomAllowed: Boolean = true,
  val iconName: String = "grass",
  val isActive: Boolean = true,
  val sortOrder: Int = 0
)

@Entity(tableName = "cows")
data class Cow(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val name: String, // e.g. "ગંગા (Ganga)"
  val tagNumber: String, // e.g. "TAG-108"
  val ageYears: Int,
  val breed: String, // "ગિર ગાય (Gir)", "કાંકરેજ (Kankrej)"
  val healthStatus: String = "સ્વસ્થ (Healthy)",
  val monthlySevaAmount: Double = 2500.0,
  val isAdopted: Boolean = false,
  val adopterName: String = "",
  val photoRes: String = ""
)

@Entity(tableName = "cracker_products")
data class CrackerProduct(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val name: String,
  val description: String,
  val price: Double,
  val offerPrice: Double,
  val stock: Int,
  val category: String, // "Sparklers / ફૂલઝરી", "Chakkar / ચક્કર", "Flowerpots / કોઠી", "Family Packs"
  val startDate: String = "", // "YYYY-MM-DD"
  val endDate: String = "", // "YYYY-MM-DD"
  val isDateControlEnabled: Boolean = false,
  val isActive: Boolean = true,
  val sortOrder: Int = 0
)

@Entity(tableName = "cracker_orders")
data class CrackerOrder(
  @PrimaryKey val id: String, // e.g. "ORD-8742"
  val customerName: String,
  val customerMobile: String,
  val customerAddress: String,
  val itemsSummary: String,
  val totalAmount: Double,
  val status: String = "PAID", // "PAID", "PENDING", "CANCELLED"
  val date: String,
  val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "expenses")
data class Expense(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val category: String, // "ઘાસ-ચારો (Fodder)", "દવા-સારવાર (Medical)", "કર્મચારી વેતન (Staff)", "જાળવણી (Maintenance)"
  val amount: Double,
  val date: String,
  val description: String,
  val addedBy: String = "Admin",
  val receiptNo: String = "",
  val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "gaushala_services")
data class GaushalaService(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String, // "24x7 ગૌ એમ્બ્યુલન્સ"
  val description: String,
  val contactPhone: String,
  val timing: String, // "24 કલાક ઉપલબ્ધ"
  val location: String = "આણંદ & આસપાસના ગ્રામ્ય વિસ્તારો",
  val isActive: Boolean = true
)

@Entity(tableName = "events")
data class GaushalaEvent(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val date: String,
  val time: String,
  val location: String,
  val description: String,
  val isRegistrationOpen: Boolean = true,
  val isActive: Boolean = true
)

@Entity(tableName = "gallery_items")
data class GalleryItem(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val category: String, // "Gaushala", "Cows", "Festivals", "Medical", "Seva"
  val imageUrl: String = "",
  val sortOrder: Int = 0
)

@Entity(tableName = "pamphlet_categories")
data class PamphletCategory(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val key: String, // e.g. "Birthday", "Shradhanjali", "Festival", "General", "Cow Donation", "Cow Seva", "Food/Chara Donation", "Special Event", "Diwali", "Janmashtami", "Navratri", "Other"
  val displayNameGujarati: String,
  val iconEmoji: String = "📁",
  val parentCategory: String = "", // empty for top-level, or "Festival" for festival sub-categories
  val selectionMode: String = "DEFAULT", // "DEFAULT", "RANDOM", "MANUAL"
  val sortOrder: Int = 0,
  val isSystem: Boolean = false
)

@Entity(tableName = "pamphlet_themes")
data class PamphletTheme(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val name: String, // e.g. "Royal Gold Birthday Theme", "Canva Gau Seva Design"
  val category: String, // "Birthday", "General", "Festival", "Shradhanjali", "Cow Seva", "Cow Donation", "Food/Chara Donation", "Special Event", "Diwali", "Janmashtami", "Navratri", "Other"
  val subCategory: String = "", // For Festival: "Diwali", "Janmashtami", "Navratri", "Rakshabandhan", "Ganesh Chaturthi", "Other"
  val templateNumber: String = "01", // e.g. "Template 01", "Design 01"
  val sortOrder: Int = 0,
  val isDefault: Boolean = false, // Default theme for this category/subCategory
  val isActive: Boolean = true, // ON/OFF switch

  // Gaushala Own Uploaded Template Support
  val isCustomUpload: Boolean = false, // True if admin uploaded own Canva/Photoshop design
  val templateImageUri: String = "", // URI or drawable name e.g. "drawable:canva_gaushala_general" or content://...
  val templateImageName: String = "", // e.g. "My_Canva_Design.png"
  val fieldsConfigJson: String = "", // JSON string containing List<TemplateFieldSetting> for overlay coordinates and styling
  
  // Visual Styles (used for standard themes)
  val layoutStyle: String = "ROYAL_FRAME", // "ROYAL_FRAME", "SACRED_BORDER", "MODERN_MINIMAL", "CERTIFICATE_ELEGANT"
  val backgroundType: String = "GRADIENT", // "SOLID", "GRADIENT", "PATTERN"
  val bgPrimaryColorHex: String = "#FFFDF5",
  val bgSecondaryColorHex: String = "#FFF3E0",
  val accentColorHex: String = "#E65100",
  val borderColorHex: String = "#D4AF37",
  val borderWidthDp: Int = 3,
  val borderStyle: String = "ORNATE_GOLD", // "ORNATE_GOLD", "DOUBLE_SACRED", "CLEAN_SOLID", "DOTTED_TEMPLE"
  
  // Header Text
  val headerTitleGujarati: String = "॥ શ્રી સુરભિ ગૌસેવા રસીદ & આશીર્વાદ પત્રિકા ॥",
  val subTitleGujarati: String = "॥ ગાવો વિશ્વસ્ય માતરઃ • ગૌરક્ષાર્થે સમર્પિત ॥",
  val blessingMessageTemplate: String = "શ્રી {{DONOR_NAME}} ({{GAM}}) તરફથી {{SEVA_TYPE}} નિમિત્તે આપેલ ₹{{AMOUNT}} નું પવિત્ર દાન ગૌશાળા ખાતે સ્વીકારેલ છે. ગૌમાતા આપના પરિવાર પર સુખ, શાંતિ અને સમૃદ્ધિના આશીર્વાદ વરસાવે.",
  val headingFontSizeSp: Int = 18,
  val fontStyle: String = "SERIF", // "SERIF", "SANS", "CURSIVE"
  
  // Cow Illustration / Watermark
  val cowIllustrationType: String = "SURABHI_GOLD", // "SURABHI_GOLD", "KAMDHENU_FLOWER", "CALF_MOTHER", "VINTAGE_RADHA_KRISHNA", "NONE"
  val cowPosition: String = "TOP_RIGHT", // "TOP_RIGHT", "HEADER_CENTER", "BACKGROUND_WATERMARK", "BOTTOM_LEFT"
  val cowSize: String = "MEDIUM", // "SMALL", "MEDIUM", "LARGE"
  
  // Field Visibility & Controls (Admin Rule 8)
  val showDonorPhoto: Boolean = true,
  val photoShape: String = "CIRCLE", // "CIRCLE", "ROUNDED", "SQUARE", "GOLD_FRAME"
  val photoPosition: String = "TOP_LEFT", // "TOP_LEFT", "TOP_RIGHT", "CENTER"
  val photoSizeDp: Int = 76,
  val showDonorName: Boolean = true,
  val showGam: Boolean = true,
  val showTaluka: Boolean = true,
  val showDistrict: Boolean = true,
  val showDate: Boolean = true,
  val showAmount: Boolean = true,
  val showPaymentType: Boolean = true,
  val showDonationId: Boolean = true,
  val showGaushalaLogo: Boolean = true,
  val showGaushalaPhone: Boolean = true,
  val showGaushalaAddress: Boolean = true,
  val showTaxExemption80G: Boolean = true,
  val showQrVerification: Boolean = true,
  
  val footerText: String = "સંપર્ક: {{GAUSHALA_PHONE}} • નોંધ: આ કમ્પ્યુટર જનરેટેડ અધિકૃત ઈ-પાવતી છે.",
  val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "app_settings")
data class AppSettings(
  @PrimaryKey val id: Int = 1,
  val gaushalaName: String = "શ્રી સુરભિ ગૌશાળા ટ્રસ્ટ",
  val tagline: String = "ગૌ સેવા • માનવ સેવા • જીવદયા",
  val contactPhone: String = "+91 98765 43210",
  val whatsappNumber: String = "+91 98765 43210",
  val email: String = "seva@gaushala.org",
  val address: String = "ગૌધામ માર્ગ, આણંદ, ગુજરાત - 388001",
  val website: String = "www.gaushala.org",
  val socialMedia: String = "@gaushala_trust",
  val defaultCowImage: String = "SURABHI_GOLD",
  val logoUrl: String = "",
  val birthdayGreetingMessage: String = "ગૌશાળા પરિવાર તરફથી તમને જન્મદિવસની હાર્દિક શુભેચ્છાઓ. ગૌમાતાની કૃપા સદાય આપ પર બની રહે.",
  val announcementText: String = "જય ગૌમાતા! આગામી પવિત્ર ગોપાષ્ટમી નિમિત્તે વિશેષ ગૌપૂજન તથા મહાઆરતીનું આયોજન.",
  val isAnnouncementActive: Boolean = true,
  // Central Visibility Controls
  val isHomeActive: Boolean = true,
  val isDonationActive: Boolean = true,
  val isBirthdayActive: Boolean = true,
  val isShradhanjaliActive: Boolean = true,
  val isFestivalActive: Boolean = true,
  val isCrackersActive: Boolean = true,
  val isAdoptionActive: Boolean = true,
  val isAmbulanceActive: Boolean = true,
  val isHospitalActive: Boolean = true,
  val isGalleryActive: Boolean = true,
  val isEventsActive: Boolean = true,
  val isContactActive: Boolean = true,
  val isThemesActive: Boolean = true,
  val applyThemeToExistingPamphlets: Boolean = false
)

@Entity(tableName = "audit_logs")
data class AuditLog(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val adminName: String,
  val action: String,
  val module: String,
  val date: String,
  val time: String,
  val timestamp: Long = System.currentTimeMillis()
)
