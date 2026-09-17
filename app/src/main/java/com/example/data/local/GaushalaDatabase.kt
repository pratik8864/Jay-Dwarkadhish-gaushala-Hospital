package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.AppSettings
import com.example.data.model.AuditLog
import com.example.data.model.Cow
import com.example.data.model.CrackerOrder
import com.example.data.model.CrackerProduct
import com.example.data.model.Donation
import com.example.data.model.Expense
import com.example.data.model.Festival
import com.example.data.model.GalleryItem
import com.example.data.model.GaushalaEvent
import com.example.data.model.GaushalaService
import com.example.data.model.PamphletCategory
import com.example.data.model.PamphletTheme
import com.example.data.model.SevaItem
import com.example.data.model.TemplateFieldHelper
import com.example.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
  entities = [
    User::class,
    Donation::class,
    Festival::class,
    SevaItem::class,
    Cow::class,
    CrackerProduct::class,
    CrackerOrder::class,
    Expense::class,
    GaushalaService::class,
    GaushalaEvent::class,
    GalleryItem::class,
    PamphletCategory::class,
    PamphletTheme::class,
    AppSettings::class,
    AuditLog::class
  ],
  version = 4,
  exportSchema = false
)
abstract class GaushalaDatabase : RoomDatabase() {
  abstract fun gaushalaDao(): GaushalaDao

  companion object {
    @Volatile private var INSTANCE: GaushalaDatabase? = null

    fun getDatabase(context: Context, scope: CoroutineScope): GaushalaDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          GaushalaDatabase::class.java,
          "gaushala_database.db"
        )
        .fallbackToDestructiveMigration()
        .addCallback(DatabaseCallback(scope))
        .build()
        INSTANCE = instance
        instance
      }
    }

    private class DatabaseCallback(private val scope: CoroutineScope) : Callback() {
      override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        INSTANCE?.let { database ->
          scope.launch(Dispatchers.IO) {
            populateInitialData(database.gaushalaDao())
          }
        }
      }

      suspend fun populateInitialData(dao: GaushalaDao) {
        // App Settings
        dao.saveAppSettings(AppSettings())

        // Sample Users (one with today's birthday to test birthday automation!)
        val todayIso = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
        dao.insertUser(
          User(
            fullName = "હિતેશભાઈ પટેલ",
            mobileNumber = "9825012345",
            dateOfBirth = todayIso, // Today is their birthday!
            email = "hitesh.patel@example.com",
            city = "આણંદ",
            taluka = "આણંદ",
            district = "આણંદ"
          )
        )
        dao.insertUser(
          User(
            fullName = "રમેશભાઈ શાહ",
            mobileNumber = "9898011223",
            dateOfBirth = "1988-11-04",
            email = "ramesh.shah@example.com",
            city = "અમદાવાદ",
            taluka = "દસક્રોઈ",
            district = "અમદાવાદ"
          )
        )

        // Seva Items (General Gau Seva)
        dao.insertSevaItem(
          SevaItem(
            name = "ગાય માટે લીલો ઘાસચારો સેવા",
            description = "ગૌમાતા માટે પૌષ્ટિક લીલો ઘાસચારો (100 કિલો)",
            suggestedAmount = 500.0,
            iconName = "grass",
            sortOrder = 1
          )
        )
        dao.insertSevaItem(
          SevaItem(
            name = "સુકો ચારો અને ખાણદાણ સેવા",
            description = "ગાયોના સ્વાસ્થ્ય માટે ઉત્તમ ગુણવત્તાનું પૌષ્ટિક ખાણદાણ",
            suggestedAmount = 1100.0,
            iconName = "grain",
            sortOrder = 2
          )
        )
        dao.insertSevaItem(
          SevaItem(
            name = "ગૌમાતા ઔષધિ અને દવા સારવાર",
            description = "બીમાર અને અશક્ત ગાયોની તાત્કાલિક સારવાર અને ઔષધ સેવા",
            suggestedAmount = 2100.0,
            iconName = "medication",
            sortOrder = 3
          )
        )
        dao.insertSevaItem(
          SevaItem(
            name = "શુદ્ધ પીવાના પાણીની વ્યવસ્થા સેવા",
            description = "ગૌશાળા પરિસરમાં અવિરત ઠંડા અને શુદ્ધ જળની સેવા",
            suggestedAmount = 501.0,
            iconName = "water_drop",
            sortOrder = 4
          )
        )
        dao.insertSevaItem(
          SevaItem(
            name = "ગૌશાળા શેડ નિર્માણ & જાળવણી સેવા",
            description = "ગૌમાતાના આશ્રય માટે છાંયડો અને સુરક્ષિત શેડ બાંધકામ સહયોગ",
            suggestedAmount = 5000.0,
            iconName = "foundation",
            sortOrder = 5
          )
        )
        dao.insertSevaItem(
          SevaItem(
            name = "સામાન્ય ગૌ સેવા દાન (મુક્ત રકમ)",
            description = "તમારી શ્રદ્ધા અનુસાર ગૌસેવા માટે કોઈપણ રકમ અર્પણ કરો",
            suggestedAmount = 251.0,
            iconName = "volunteer_activism",
            sortOrder = 6
          )
        )

        // Festivals
        dao.insertFestival(
          Festival(
            name = "દિવાળી & ગોવર્ધન પૂજા ગૌસેવા",
            dateText = "આસો વદ અમાસ - કારતક સુદ એકમ",
            description = "દિવાળી પર્વે ગૌમાતાનું પૂજન કરી ગોવર્ધન અન્નકૂટ અને લાડુ સેવા અર્પણ કરો.",
            sevaAmounts = "251,501,1100,2100,5100",
            startDate = "2026-09-01",
            endDate = "2026-11-15",
            isDateControlEnabled = false,
            isActive = true,
            sortOrder = 1
          )
        )
        dao.insertFestival(
          Festival(
            name = "પવિત્ર ગોપાષ્ટમી ઉત્સવ",
            dateText = "કારતક સુદ આઠમ",
            description = "ભગવાન શ્રીકૃષ્ણ દ્વારા પ્રથમવાર ગૌચારણ લીલા દિવસ. મહાગૌપૂજન અને વિશેષ ઘાસ અર્પણ.",
            sevaAmounts = "501,1001,2501,5001",
            startDate = "2026-10-01",
            endDate = "2026-11-30",
            isDateControlEnabled = false,
            isActive = true,
            sortOrder = 2
          )
        )
        dao.insertFestival(
          Festival(
            name = "મકરસંક્રાંતિ મહાપુણ્યદાન (ઉત્તરાયણ)",
            dateText = "14 જાન્યુઆરી",
            description = "ઉત્તરાયણ પર્વે ગાયોને લીલો ઘાસચારો, ગોળ અને લાડુ ખવડાવવાનું અનંતગણું પુણ્ય.",
            sevaAmounts = "101,251,501,1001,2100",
            startDate = "2026-12-15",
            endDate = "2027-01-20",
            isDateControlEnabled = false,
            isActive = true,
            sortOrder = 3
          )
        )
        dao.insertFestival(
          Festival(
            name = "શ્રીકૃષ્ણ જન્માષ્ટમી ગૌસેવા",
            dateText = "શ્રાવણ વદ આઠમ",
            description = "નંદલાલાના જન્મોત્સવે કાન્હાની પ્રિય ગાયો માટે માખણ, ગોળ અને સુકો ઘાસચારો ભેટ.",
            sevaAmounts = "251,501,1001,2501",
            startDate = "2026-08-01",
            endDate = "2026-09-30",
            isDateControlEnabled = false,
            isActive = true,
            sortOrder = 4
          )
        )

        // Cows for Adoption
        dao.insertCow(
          Cow(
            name = "કામધેનુ (Kamadhenu)",
            tagNumber = "TAG-001",
            ageYears = 4,
            breed = "ગિર ગાય (Gir Breed)",
            healthStatus = "સંપૂર્ણ સ્વસ્થ અને દૂધ આપતી",
            monthlySevaAmount = 2500.0,
            isAdopted = false
          )
        )
        dao.insertCow(
          Cow(
            name = "નંદિની (Nandini)",
            tagNumber = "TAG-002",
            ageYears = 3,
            breed = "કાંકરેજ (Kankrej Breed)",
            healthStatus = "સ્વસ્થ અને રમતિયાળ વાછરડી",
            monthlySevaAmount = 2000.0,
            isAdopted = false
          )
        )
        dao.insertCow(
          Cow(
            name = "સુરભિ (Surabhi)",
            tagNumber = "TAG-003",
            ageYears = 6,
            breed = "ગિર ગાય (Gir Breed)",
            healthStatus = "વિશેષ પોષણ સારવાર હેઠળ",
            monthlySevaAmount = 3000.0,
            isAdopted = true,
            adopterName = "પ્રકાશભાઈ ગાંધી"
          )
        )
        dao.insertCow(
          Cow(
            name = "ગોપી (Gopi)",
            tagNumber = "TAG-004",
            ageYears = 2,
            breed = "થારપારકર (Tharparkar)",
            healthStatus = "સ્વસ્થ વાછરડું",
            monthlySevaAmount = 1800.0,
            isAdopted = false
          )
        )

        // Diwali Crackers
        dao.insertCrackerProduct(
          CrackerProduct(
            name = "ઇકો-ફ્રેન્ડલી રોયલ સ્પાર્કલર્સ (ફૂલઝરી 50cm)",
            description = "ઓછા ધુમાડાવાળી અને સલામત 50 સેમી સોનેરી સ્પાર્કલર્સ બોક્સ (10 નંગ)",
            price = 250.0,
            offerPrice = 180.0,
            stock = 150,
            category = "Sparklers / ફૂલઝરી",
            startDate = "2026-09-01",
            endDate = "2026-11-20",
            isDateControlEnabled = false,
            isActive = true,
            sortOrder = 1
          )
        )
        dao.insertCrackerProduct(
          CrackerProduct(
            name = "ડિલક્સ કલરફુલ ફ્લાવરપોટ્સ (કોઠી સ્પેશિયલ)",
            description = "ઉંચા મલ્ટી-કલર ફુવારા છોડતી પ્રીમિયમ કોઠીઓ (5 નંગ બોક્સ)",
            price = 400.0,
            offerPrice = 299.0,
            stock = 100,
            category = "Flowerpots / કોઠી",
            startDate = "2026-09-01",
            endDate = "2026-11-20",
            isDateControlEnabled = false,
            isActive = true,
            sortOrder = 2
          )
        )
        dao.insertCrackerProduct(
          CrackerProduct(
            name = "ગોલ્ડન વ્હીલ ગ્રાઉન્ડ ચક્કર (25 પીસ)",
            description = "જમીન પર ગોળ ઘૂમતી તેજસ્વી સોનેરી ચક્કરીઓનું મોટું બોક્સ",
            price = 350.0,
            offerPrice = 260.0,
            stock = 80,
            category = "Chakkar / ચક્કર",
            startDate = "2026-09-01",
            endDate = "2026-11-20",
            isDateControlEnabled = false,
            isActive = true,
            sortOrder = 3
          )
        )
        dao.insertCrackerProduct(
          CrackerProduct(
            name = "સંપૂર્ણ ગૌશાળા દિવાળી ફેમિલી મેગા પેક",
            description = "બાળકો અને પરિવાર માટે સંપૂર્ણ ધમાકા કલેક્શન (32 વિવિધ આઈટમ્સ)",
            price = 2200.0,
            offerPrice = 1650.0,
            stock = 50,
            category = "Family Packs",
            startDate = "2026-09-01",
            endDate = "2026-11-20",
            isDateControlEnabled = false,
            isActive = true,
            sortOrder = 4
          )
        )

        // Services
        dao.insertService(
          GaushalaService(
            title = "24x7 ગૌ એમ્બ્યુલન્સ હેલ્પલાઈન",
            description = "અકસ્માત કે બીમાર ગાયોને તાત્કાલિક સારવાર માટે હાઈડ્રોલિક લિફ્ટ વાળી વિશેષ એમ્બ્યુલન્સ સેવા.",
            contactPhone = "1800 233 4567",
            location = "આણંદ અને આસપાસના 30 કિમી વિસ્તારમાં ઉપલબ્ધ",
            timing = "24 કલાક ઉપલબ્ધ"
          )
        )
        dao.insertService(
          GaushalaService(
            title = "ગૌ ચિકિત્સાલય & હોસ્પિટલ",
            description = "નિષ્ણાત વેટરનરી તબીબો દ્વારા ઓપરેશન થિયેટર, એક્સ-રે અને ICU સારવાર સુવિધા.",
            contactPhone = "02692 234567",
            location = "શ્રી સુરભિ ગૌશાળા પરિસર, આણંદ",
            timing = "સવારે 8:00 થી સાંજે 8:00"
          )
        )
        dao.insertService(
          GaushalaService(
            title = "ગૌશાળા મુલાકાત અને ગૌપૂજન દર્શન",
            description = "પરિવાર સાથે પધારી ગૌમાતાના સાનિધ્યમાં પૂજન, ગોળ-ઘાસ અર્પણ કરો.",
            contactPhone = "+91 98765 43210",
            location = "મુખ્ય ગૌધામ",
            timing = "સવારે 7:00 થી સાંજે 7:00"
          )
        )

        // Sample initial donations to populate Accounting & Hisab
        dao.insertDonation(
          Donation(
            id = "GAU-2026-1001",
            donorName = "વિજયભાઈ પટેલ",
            mobile = "9825012345",
            email = "vijay@example.com",
            donationType = "Festival",
            amount = 2100.0,
            paymentStatus = "SUCCESS",
            date = todayIso,
            time = "09:30 AM",
            referenceNumber = "UPI-REF-9812451",
            dedicatedTo = "દિવાળી ગૌસેવા",
            message = "સર્વે જીવોનું કલ્યાણ થાઓ"
          )
        )
        dao.insertDonation(
          Donation(
            id = "GAU-2026-1002",
            donorName = "મીનાબેન શાહ",
            mobile = "9898011223",
            email = "mina@example.com",
            donationType = "Birthday",
            amount = 1100.0,
            paymentStatus = "SUCCESS",
            date = todayIso,
            time = "11:15 AM",
            referenceNumber = "UPI-REF-9812452",
            dedicatedTo = "મારા જન્મદિવસ નિમિત્તે",
            message = "ગૌમાતાના આશીર્વાદ સદા રહે"
          )
        )
        dao.insertDonation(
          Donation(
            id = "GAU-2026-1003",
            donorName = "હિતેન્દ્રસિંહ ઝાલા",
            mobile = "9426033445",
            email = "hitendra@example.com",
            donationType = "Shradhanjali",
            amount = 5100.0,
            paymentStatus = "SUCCESS",
            date = todayIso,
            time = "02:00 PM",
            referenceNumber = "NET-REF-8899120",
            dedicatedTo = "સ્વ. ગંગાબા ઝાલા ના પુણ્યસ્મરણાર્થે",
            message = "દિવંગત આત્માને મોક્ષ મળે"
          )
        )

        // Sample initial expenses to populate Net Balance Hisab
        dao.insertExpense(
          Expense(
            category = "ઘાસ-ચારો (Fodder)",
            amount = 3200.0,
            date = todayIso,
            description = "લીલો ઘાસચારો 2 ટ્રેક્ટર ખરીદી",
            addedBy = "મુખ્ય વ્યવસ્થાપક",
            receiptNo = "EXP-091"
          )
        )
        dao.insertExpense(
          Expense(
            category = "દવા-સારવાર (Medical)",
            amount = 850.0,
            date = todayIso,
            description = "વાછરડી માટે એન્ટીબાયોટીક અને મલમ",
            addedBy = "ડૉ. પરીખ",
            receiptNo = "EXP-092"
          )
        )

        // Events
        dao.insertEvent(
          GaushalaEvent(
            title = "વિશેષ સમૂહ ગૌપૂજન અને સંતવાણી ડાયરો",
            date = "આગામી રવિવાર",
            time = "સાંજે 6:00 કલાકે",
            location = "શ્રી સુરભિ ગૌશાળા પરિસર, આણંદ",
            description = "ગૌસેવા લાભાર્થે સુપ્રસિદ્ધ કલાકારો દ્વારા ભજન સંતવાણી તેમજ સમૂહ ગૌપૂજા.",
            isRegistrationOpen = true
          )
        )

        // Pre-seeded Pamphlet Categories (Requirements 31-40)
        val defaultCategories = listOf(
          PamphletCategory(key = "Birthday", displayNameGujarati = "જન્મદિવસ પત્રિકા (Birthday)", iconEmoji = "🎂", sortOrder = 1, isSystem = true),
          PamphletCategory(key = "Shradhanjali", displayNameGujarati = "પુણ્યતિથિ / શ્રદ્ધાંજલિ (Shraddhanjali)", iconEmoji = "🕊️", sortOrder = 2, isSystem = true),
          PamphletCategory(key = "Festival", displayNameGujarati = "તહેવાર પત્રિકા (Festival)", iconEmoji = "🪔", sortOrder = 3, isSystem = true),
          // Festival Sub-Categories (Req 35)
          PamphletCategory(key = "Diwali", displayNameGujarati = "દિવાળી પર્વ (Diwali)", iconEmoji = "🪔", parentCategory = "Festival", sortOrder = 10, isSystem = true),
          PamphletCategory(key = "Janmashtami", displayNameGujarati = "જન્માષ્ટમી મહોત્સવ (Janmashtami)", iconEmoji = "🦚", parentCategory = "Festival", sortOrder = 11, isSystem = true),
          PamphletCategory(key = "Navratri", displayNameGujarati = "નવરાત્રી પર્વ (Navratri)", iconEmoji = "🌸", parentCategory = "Festival", sortOrder = 12, isSystem = true),
          PamphletCategory(key = "Rakshabandhan", displayNameGujarati = "રક્ષાબંધન (Rakshabandhan)", iconEmoji = "🪢", parentCategory = "Festival", sortOrder = 13, isSystem = true),
          PamphletCategory(key = "Ganesh Chaturthi", displayNameGujarati = "ગણેશ ચતુર્થી (Ganesh Chaturthi)", iconEmoji = "🐘", parentCategory = "Festival", sortOrder = 14, isSystem = true),
          PamphletCategory(key = "Other Festivals", displayNameGujarati = "અન્ય પવિત્ર પર્વ (Other Festivals)", iconEmoji = "✨", parentCategory = "Festival", sortOrder = 15, isSystem = true),
          // Other Primary Categories (Req 31)
          PamphletCategory(key = "General", displayNameGujarati = "સામાન્ય ગૌદાન પત્રિકા (General Donation)", iconEmoji = "📜", sortOrder = 4, isSystem = true),
          PamphletCategory(key = "Cow Donation", displayNameGujarati = "ગૌ દાન પત્રિકા (Cow Donation)", iconEmoji = "🐄", sortOrder = 5, isSystem = true),
          PamphletCategory(key = "Cow Seva", displayNameGujarati = "ગૌ સેવા પત્રિકા (Gau Seva)", iconEmoji = "🙏", sortOrder = 6, isSystem = true),
          PamphletCategory(key = "Food/Chara Donation", displayNameGujarati = "ઘાસચારો / ખાણદાણ પત્રિકા (Fodder)", iconEmoji = "🌾", sortOrder = 7, isSystem = true),
          PamphletCategory(key = "Special Event", displayNameGujarati = "વિશેષ મહોત્સવ પત્રિકા (Special Event)", iconEmoji = "🎪", sortOrder = 8, isSystem = true),
          PamphletCategory(key = "Other", displayNameGujarati = "અન્ય પત્રિકા (Other Pamphlets)", iconEmoji = "📁", sortOrder = 9, isSystem = true)
        )
        for (cat in defaultCategories) {
          dao.insertPamphletCategory(cat)
        }

        // Dynamic Pamphlet Themes (Section 1 & 2)
        dao.insertPamphletTheme(
          PamphletTheme(
            name = "રાજસી સુવર્ણ જન્મદિવસ થીમ (Royal Gold Birthday)",
            category = "Birthday",
            templateNumber = "01",
            isDefault = true,
            isActive = true,
            layoutStyle = "ROYAL_FRAME",
            backgroundType = "GRADIENT",
            bgPrimaryColorHex = "#FFFDF5",
            bgSecondaryColorHex = "#FFF8E1",
            accentColorHex = "#E65100",
            borderColorHex = "#D4AF37",
            borderWidthDp = 4,
            borderStyle = "ORNATE_GOLD",
            headerTitleGujarati = "॥ જન્મદિવસ મંગલમય ગૌ સેવા આશીર્વાદ પત્રિકા ॥",
            subTitleGujarati = "॥ ગાવો વિશ્વસ્ય માતરઃ • દીર્ઘાયુષ્ય ભવઃ ॥",
            blessingMessageTemplate = "શ્રી {{DONOR_NAME}} ({{GAM}}) તરફથી શુભ જન્મદિવસ નિમિત્તે અર્પણ કરેલ ₹{{AMOUNT}} નું પવિત્ર દાન ગૌશાળા ખાતે સાદર સ્વીકારેલ છે. કામધેનુ ગૌમાતા આપના આયુષ્યને નિરામય, તેજસ્વી અને મંગલમય બનાવે.",
            headingFontSizeSp = 18,
            fontStyle = "SERIF",
            cowIllustrationType = "SURABHI_GOLD",
            cowPosition = "TOP_RIGHT",
            cowSize = "MEDIUM",
            showDonorPhoto = true,
            photoShape = "CIRCLE",
            photoPosition = "TOP_LEFT",
            photoSizeDp = 76,
            showDonorName = true,
            showGam = true,
            showTaluka = true,
            showDistrict = true,
            showDate = true,
            showAmount = true,
            showPaymentType = true,
            showDonationId = true,
            showGaushalaLogo = true,
            showGaushalaPhone = true,
            showGaushalaAddress = true,
            showTaxExemption80G = true,
            showQrVerification = true
          )
        )

        dao.insertPamphletTheme(
          PamphletTheme(
            name = "પાવન કેસરી સામાન્ય દાન થીમ (Sacred Saffron General)",
            category = "General",
            isDefault = true,
            isActive = true,
            layoutStyle = "SACRED_BORDER",
            backgroundType = "GRADIENT",
            bgPrimaryColorHex = "#FFFDF6",
            bgSecondaryColorHex = "#FFE0B2",
            accentColorHex = "#D84315",
            borderColorHex = "#E65100",
            borderWidthDp = 3,
            borderStyle = "DOUBLE_SACRED",
            headerTitleGujarati = "॥ શ્રી સુરભિ ગૌશાળા સેવા રસીદ & પાવતી ॥",
            subTitleGujarati = "॥ ગૌ સેવા એ જ પ્રભુ સેવા • જીવદયા પરમો ધર્મઃ ॥",
            blessingMessageTemplate = "દાનવીર શ્રેષ્ઠી શ્રી {{DONOR_NAME}} (ગામ: {{GAM}}) તરફથી ગૌમાતાના ઘાસચારા અને જતન અર્થે ₹{{AMOUNT}} નું પુણ્ય દાન મળેલ છે. આ સત્કર્મ આપના કુટુંબમાં સુખ, શાંતિ અને સમૃદ્ધિ પ્રદાન કરે.",
            headingFontSizeSp = 18,
            fontStyle = "SERIF",
            cowIllustrationType = "KAMDHENU_FLOWER",
            cowPosition = "TOP_RIGHT",
            cowSize = "MEDIUM",
            showDonorPhoto = true,
            photoShape = "ROUNDED",
            photoPosition = "TOP_LEFT",
            photoSizeDp = 72,
            showDonorName = true,
            showGam = true,
            showTaluka = true,
            showDistrict = true,
            showDate = true,
            showAmount = true,
            showPaymentType = true,
            showDonationId = true,
            showGaushalaLogo = true,
            showGaushalaPhone = true,
            showGaushalaAddress = true,
            showTaxExemption80G = true,
            showQrVerification = true
          )
        )

        dao.insertPamphletTheme(
          PamphletTheme(
            name = "દિવ્ય મહોત્સવ તહેવાર થીમ (Festival Divine Celebration)",
            category = "Festival",
            isDefault = true,
            isActive = true,
            layoutStyle = "ROYAL_FRAME",
            backgroundType = "GRADIENT",
            bgPrimaryColorHex = "#FFFBEA",
            bgSecondaryColorHex = "#FFECB3",
            accentColorHex = "#B71C1C",
            borderColorHex = "#C99700",
            borderWidthDp = 4,
            borderStyle = "ORNATE_GOLD",
            headerTitleGujarati = "॥ પવિત્ર પર્વ ગૌ સેવા સમર્પણ પત્રિકા ॥",
            subTitleGujarati = "॥ સર્વ દેવમયી ગૌમાતા પ્રસન્ના સંતુ ॥",
            blessingMessageTemplate = "આ પાવન પર્વે શ્રી {{DONOR_NAME}} ({{GAM}}) દ્વારા {{SEVA_TYPE}} નિમિત્તે સમર્પિત ₹{{AMOUNT}} નું દાન ગૌશાળા ખાતે કૃતજ્ઞતાપૂર્વક સ્વીકારેલ છે. ભગવાન શ્રીકૃષ્ણ અને ગૌમાતા સર્વ મનોકામના પૂર્ણ કરે.",
            headingFontSizeSp = 18,
            fontStyle = "SERIF",
            cowIllustrationType = "SURABHI_GOLD",
            cowPosition = "TOP_RIGHT",
            cowSize = "MEDIUM",
            showDonorPhoto = true,
            photoShape = "GOLD_FRAME",
            photoPosition = "TOP_LEFT",
            photoSizeDp = 80,
            showDonorName = true,
            showGam = true,
            showTaluka = true,
            showDistrict = true,
            showDate = true,
            showAmount = true,
            showPaymentType = true,
            showDonationId = true,
            showGaushalaLogo = true,
            showGaushalaPhone = true,
            showGaushalaAddress = true,
            showTaxExemption80G = true,
            showQrVerification = true
          )
        )

        dao.insertPamphletTheme(
          PamphletTheme(
            name = "દિવ્ય સ્મૃતિ શ્રદ્ધાંજલિ થીમ (Sacred Shradhanjali Memorial)",
            category = "Shradhanjali",
            isDefault = true,
            isActive = true,
            layoutStyle = "SACRED_BORDER",
            backgroundType = "SOLID",
            bgPrimaryColorHex = "#FAFAFA",
            bgSecondaryColorHex = "#F5F5F5",
            accentColorHex = "#37474F",
            borderColorHex = "#B0BEC5",
            borderWidthDp = 3,
            borderStyle = "CLEAN_SOLID",
            headerTitleGujarati = "॥ પુણ્યસ્મરણાર્થે શ્રદ્ધાંજલિ ગૌ સેવા પત્રિકા ॥",
            subTitleGujarati = "॥ ૐ શાંતિઃ શાંતિઃ શાંતિઃ • દિવંગત આત્માને મોક્ષાર્થે ॥",
            blessingMessageTemplate = "શ્રી {{DONOR_NAME}} ({{GAM}}) દ્વારા {{SEVA_TYPE}} નિમિત્તે અર્પણ થયેલ ₹{{AMOUNT}} નું ગૌદાન પુણ્ય સ્વીકારેલ છે. પરમકૃપાળુ પરમાત્મા દિવંગત પુણ્યવાન આત્માને પરમ શાંતિ અને મોક્ષ પદ અર્પે તેવી પ્રાર્થના.",
            headingFontSizeSp = 17,
            fontStyle = "SERIF",
            cowIllustrationType = "CALF_MOTHER",
            cowPosition = "TOP_RIGHT",
            cowSize = "SMALL",
            showDonorPhoto = true,
            photoShape = "ROUNDED",
            photoPosition = "TOP_LEFT",
            photoSizeDp = 76,
            showDonorName = true,
            showGam = true,
            showTaluka = true,
            showDistrict = true,
            showDate = true,
            showAmount = true,
            showPaymentType = true,
            showDonationId = true,
            showGaushalaLogo = true,
            showGaushalaPhone = true,
            showGaushalaAddress = true,
            showTaxExemption80G = true,
            showQrVerification = true
          )
        )

        dao.insertPamphletTheme(
          PamphletTheme(
            name = "કામધેનુ ગૌમાતા વિશેષ દત્તક થીમ (Kamdhenu Cow Adoption)",
            category = "Cow Seva",
            isDefault = true,
            isActive = true,
            layoutStyle = "ROYAL_FRAME",
            backgroundType = "GRADIENT",
            bgPrimaryColorHex = "#F1F8E9",
            bgSecondaryColorHex = "#DCEDC8",
            accentColorHex = "#2E7D32",
            borderColorHex = "#558B2F",
            borderWidthDp = 3,
            borderStyle = "ORNATE_GOLD",
            headerTitleGujarati = "॥ ગૌમાતા દત્તક સ્વીકાર અભિનંદન પત્રિકા ॥",
            subTitleGujarati = "॥ ગૌપાલનમ્ પરમં સુખમ્ ॥",
            blessingMessageTemplate = "શ્રી {{DONOR_NAME}} ({{GAM}}) દ્વારા ગૌમાતા દત્તક સેવા પેટે માસિક ₹{{AMOUNT}} નું સમર્પણ સ્વીકારેલ છે. આપનું આ મહાદાન ગૌશાળા પરિવાર કાયમ વંદન કરે છે.",
            headingFontSizeSp = 18,
            fontStyle = "SERIF",
            cowIllustrationType = "SURABHI_GOLD",
            cowPosition = "TOP_RIGHT",
            cowSize = "MEDIUM",
            showDonorPhoto = true,
            photoShape = "CIRCLE",
            photoPosition = "TOP_LEFT",
            photoSizeDp = 76,
            showDonorName = true,
            showGam = true,
            showTaluka = true,
            showDistrict = true,
            showDate = true,
            showAmount = true,
            showPaymentType = true,
            showDonationId = true,
            showGaushalaLogo = true,
            showGaushalaPhone = true,
            showGaushalaAddress = true,
            showTaxExemption80G = true,
            showQrVerification = true
          )
        )

        // Gaushala's Own Uploaded Designs (Canva / Photoshop templates with dynamic overlay fields)
        dao.insertPamphletTheme(
          PamphletTheme(
            name = "ગૌશાળા ઓરિજિનલ કેનવા ડિઝાઇન (Canva Gau Seva Template)",
            category = "General",
            isDefault = true,
            isActive = true,
            isCustomUpload = true,
            templateImageUri = "drawable:canva_gaushala_general",
            templateImageName = "Canva_Gau_Seva_General.jpg",
            fieldsConfigJson = TemplateFieldHelper.toJson(TemplateFieldHelper.getDefaultFields())
          )
        )

        dao.insertPamphletTheme(
          PamphletTheme(
            name = "જન્મદિવસ વિશેષ કેનવા ડિઝાઇન (Canva Birthday Template)",
            category = "Birthday",
            isDefault = true,
            isActive = true,
            isCustomUpload = true,
            templateImageUri = "drawable:canva_gaushala_birthday",
            templateImageName = "Canva_Birthday_Special.jpg",
            fieldsConfigJson = TemplateFieldHelper.toJson(
              TemplateFieldHelper.getDefaultFields().map {
                if (it.fieldKey == "AMOUNT") it.copy(textColorHex = "#B71C1C", bgColorHex = "#FFF9C4")
                else it
              }
            )
          )
        )

        dao.insertPamphletTheme(
          PamphletTheme(
            name = "દિવાળી મહોત્સવ કેનવા ડિઝાઇન (Canva Diwali Festival Template)",
            category = "Festival",
            subCategory = "Diwali",
            templateNumber = "01",
            isDefault = true,
            isActive = true,
            isCustomUpload = true,
            templateImageUri = "drawable:canva_gaushala_diwali",
            templateImageName = "Canva_Diwali_Seva.jpg",
            fieldsConfigJson = TemplateFieldHelper.toJson(TemplateFieldHelper.getFieldsForCategory("Festival", "Diwali"))
          )
        )
      }
    }
  }
}
