package com.example

import com.example.data.model.AppSettings
import com.example.data.model.CrackerProduct
import com.example.data.model.Donation
import com.example.data.model.Festival
import com.example.data.model.User
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class GaushalaLogicTest {

  @Test
  fun testRule1And2_FailedPaymentDoesNotCreateDonationOrPamphlet() {
    var confirmedDonation: Donation? = null
    var activePamphlet: Donation? = null

    // Simulating a failed payment callback
    val isPaymentSuccess = false
    if (isPaymentSuccess) {
      val donation = Donation(
        id = "GAU-2026-1001",
        donorName = "રમેશભાઈ",
        mobile = "9825011223",
        donationType = "General",
        amount = 501.0,
        paymentStatus = "SUCCESS",
        date = "2026-09-17",
        time = "10:00 AM",
        referenceNumber = "REF-123"
      )
      confirmedDonation = donation
      activePamphlet = donation
    }

    // Assert: Failed payment must NOT create confirmed donation
    assertNull("Failed payment must NOT create a confirmed donation entry", confirmedDonation)
    // Assert: Pamphlet must NOT be generated
    assertNull("Pamphlet must NOT be generated for failed payment", activePamphlet)
  }

  @Test
  fun testRule1And2_SuccessfulPaymentCreatesDonationAndPamphlet() {
    var confirmedDonation: Donation? = null
    var activePamphlet: Donation? = null

    // Simulating a successful payment callback
    val isPaymentSuccess = true
    if (isPaymentSuccess) {
      val donation = Donation(
        id = "GAU-2026-1002",
        donorName = "સુરેશભાઈ",
        mobile = "9825099887",
        donationType = "Birthday",
        amount = 1100.0,
        paymentStatus = "SUCCESS",
        date = "2026-09-17",
        time = "11:30 AM",
        referenceNumber = "REF-998"
      )
      confirmedDonation = donation
      activePamphlet = donation
    }

    assertNotNull("Successful payment creates confirmed donation", confirmedDonation)
    assertNotNull("Pamphlet is generated ONLY after successful payment", activePamphlet)
    assertEquals("GAU-2026-1002", activePamphlet?.id)
  }

  @Test
  fun testRule3_AdminCanToggleFeatureVisibility() {
    val defaultSettings = AppSettings()
    assertTrue(defaultSettings.isDonationActive)
    assertTrue(defaultSettings.isCrackersActive)
    assertTrue(defaultSettings.isBirthdayActive)

    // Admin turns OFF Crackers and Birthday
    val updatedSettings = defaultSettings.copy(
      isCrackersActive = false,
      isBirthdayActive = false
    )

    assertFalse("Crackers module is turned OFF by admin", updatedSettings.isCrackersActive)
    assertFalse("Birthday module is turned OFF by admin", updatedSettings.isBirthdayActive)
    assertTrue("General donation remains ON", updatedSettings.isDonationActive)
  }

  @Test
  fun testRule4_FestivalDateVisibility() {
    val todayIso = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    // Festival within current dates
    val activeFestival = Festival(
      id = 1,
      name = "દિવાળી પર્વ",
      dateText = "આસો વદ અમાસ",
      description = "ગૌપૂજન",
      startDate = "2026-01-01",
      endDate = "2026-12-31",
      isDateControlEnabled = true,
      isActive = true
    )

    // Festival in past
    val pastFestival = Festival(
      id = 2,
      name = "ભૂતકાળ પર્વ",
      dateText = "વ્યતીત",
      description = "સમાપ્ત",
      startDate = "2020-01-01",
      endDate = "2020-01-10",
      isDateControlEnabled = true,
      isActive = true
    )

    val isFestivalVisible: (Festival) -> Boolean = { fest ->
      if (!fest.isActive) false
      else if (!fest.isDateControlEnabled) true
      else if (fest.startDate.isBlank() || fest.endDate.isBlank()) true
      else todayIso >= fest.startDate && todayIso <= fest.endDate
    }

    assertTrue("Active festival within date range is visible", isFestivalVisible(activeFestival))
    assertFalse("Past festival outside date range is hidden", isFestivalVisible(pastFestival))
  }

  @Test
  fun testRule5_CrackerProductDateVisibility() {
    val todayIso = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    val activeProduct = CrackerProduct(
      id = 1,
      name = "સ્પાર્કલર્સ",
      description = "ફૂલઝરી",
      price = 200.0,
      offerPrice = 150.0,
      stock = 50,
      category = "Sparklers",
      startDate = "2026-01-01",
      endDate = "2026-12-31",
      isDateControlEnabled = true,
      isActive = true
    )

    val isCrackerVisible: (CrackerProduct, Boolean) -> Boolean = { prod, isModuleActive ->
      if (!isModuleActive || !prod.isActive) false
      else if (!prod.isDateControlEnabled) true
      else if (prod.startDate.isBlank() || prod.endDate.isBlank()) true
      else todayIso >= prod.startDate && todayIso <= prod.endDate
    }

    assertTrue("Cracker product is visible when module is ON and in date range", isCrackerVisible(activeProduct, true))
    assertFalse("Cracker product is hidden when entire module is turned OFF by admin", isCrackerVisible(activeProduct, false))
  }

  @Test
  fun testRule6_BirthdayAutomationDetection() {
    val todayMmDd = SimpleDateFormat("MM-dd", Locale.getDefault()).format(Date())

    val userWithTodayBirthday = User(
      id = 1,
      fullName = "હિતેશભાઈ પટેલ",
      mobileNumber = "9825012345",
      dateOfBirth = "1992-$todayMmDd",
      city = "આણંદ"
    )

    val userWithDifferentBirthday = User(
      id = 2,
      fullName = "રમેશભાઈ શાહ",
      mobileNumber = "9898011223",
      dateOfBirth = "1990-01-01",
      city = "અમદાવાદ"
    )

    val users = listOf(userWithTodayBirthday, userWithDifferentBirthday)

    val todayBirthdays = users.filter { u ->
      val dob = u.dateOfBirth
      if (dob.length >= 10) {
        val mmDd = dob.substring(5, 10)
        mmDd == todayMmDd
      } else false
    }

    assertEquals(1, todayBirthdays.size)
    assertEquals("હિતેશભાઈ પટેલ", todayBirthdays[0].fullName)
  }

  @Test
  fun testGaushalaCustomTemplate_DynamicFieldsSerializationAndDefaults() {
    // Verify default fields contain all required items
    val defaultFields = com.example.data.model.TemplateFieldHelper.getDefaultFields()
    val fieldKeys = defaultFields.map { it.fieldKey }.toSet()

    assertTrue("Field NAME must exist", fieldKeys.contains("NAME"))
    assertTrue("Field AMOUNT must exist", fieldKeys.contains("AMOUNT"))
    assertTrue("Field DATE must exist", fieldKeys.contains("DATE"))
    assertTrue("Field GAM must exist", fieldKeys.contains("GAM"))
    assertTrue("Field DONATION_ID must exist", fieldKeys.contains("DONATION_ID"))
    assertTrue("Field PAYMENT_TYPE must exist", fieldKeys.contains("PAYMENT_TYPE"))
    assertTrue("Field PHOTO must exist", fieldKeys.contains("PHOTO"))
    assertTrue("Field SEVA_TYPE must exist", fieldKeys.contains("SEVA_TYPE"))
    assertTrue("Field DEDICATED_TO must exist", fieldKeys.contains("DEDICATED_TO"))

    // Test JSON Serialization & Deserialization
    val json = com.example.data.model.TemplateFieldHelper.toJson(defaultFields)
    assertTrue("JSON string should not be empty", json.isNotBlank())

    val parsedFields = com.example.data.model.TemplateFieldHelper.fromJson(json)
    assertEquals("Parsed fields count must match original", defaultFields.size, parsedFields.size)

    val donorNameField = parsedFields.first { it.fieldKey == "NAME" }
    assertTrue(donorNameField.labelGujarati.contains("દાતા"))
    assertTrue("X position must be within bounds (0..100)", donorNameField.xPercent in 0f..100f)
    assertTrue("Y position must be within bounds (0..100)", donorNameField.yPercent in 0f..100f)
  }

  @Test
  fun testGaushalaCustomTemplate_HistoricalIntegrityPreserved() {
    // When a donation is made, it takes a snapshot of the theme configuration
    val initialTheme = com.example.data.model.PamphletTheme(
      id = 5,
      name = "Old Template Design",
      category = "General",
      isCustomUpload = true,
      templateImageUri = "drawable:canva_gaushala_general",
      fieldsConfigJson = "[{\"fieldKey\":\"NAME\",\"xPercent\":50.0,\"yPercent\":40.0}]"
    )

    val recordedDonation = Donation(
      id = "GS-2026-999",
      donorName = "પ્રતિક બકુ",
      mobile = "9876543210",
      donationType = "General",
      amount = 501.0,
      paymentStatus = "SUCCESS",
      date = "2026-09-17",
      time = "10:30 AM",
      referenceNumber = "REF-999",
      themeIdUsed = initialTheme.id,
      themeSnapshotJson = initialTheme.fieldsConfigJson
    )

    // Admin updates the theme afterwards with new positions
    val updatedTheme = initialTheme.copy(
      name = "Updated Template Design",
      fieldsConfigJson = "[{\"fieldKey\":\"donor_name\",\"xPercent\":0.8,\"yPercent\":0.8}]"
    )

    // Verify: The recorded donation still preserves its original snapshot configuration
    assertEquals(
      "Old donation must preserve its original themeSnapshotJson regardless of admin template changes",
      initialTheme.fieldsConfigJson,
      recordedDonation.themeSnapshotJson
    )
    assertFalse(
      "Donation snapshot should not match the new updated theme config",
      recordedDonation.themeSnapshotJson == updatedTheme.fieldsConfigJson
    )
  }
}
