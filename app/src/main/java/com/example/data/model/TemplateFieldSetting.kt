package com.example.data.model

import org.json.JSONArray
import org.json.JSONObject

/**
 * Represents a single dynamic field placed on top of an uploaded Gaushala template.
 * Enables the Gaushala Admin to drag, position, resize, style, and toggle each dynamic field.
 */
data class TemplateFieldSetting(
  val fieldKey: String, // "PHOTO", "NAME", "DATE", "GAM", "AMOUNT", "PAYMENT_TYPE", "DONATION_ID", "SEVA_TYPE", "DEDICATED_TO"
  val labelGujarati: String,
  val isEnabled: Boolean = true,
  val xPercent: Float = 50f, // 0..100 (horizontal position percentage from left edge)
  val yPercent: Float = 50f, // 0..100 (vertical position percentage from top edge)
  val widthPercent: Float = 0f, // 0 = auto wrap content
  val fontSizeSp: Int = 16,
  val fontWeight: String = "BOLD", // "NORMAL", "MEDIUM", "SEMI_BOLD", "BOLD", "EXTRA_BOLD"
  val fontStyle: String = "SANS", // "SANS", "SERIF", "CURSIVE", "MONOSPACE"
  val textAlign: String = "CENTER", // "LEFT", "CENTER", "RIGHT"
  val textColorHex: String = "#800000",
  val bgColorHex: String = "TRANSPARENT", // "TRANSPARENT" or Hex e.g. "#FFFFFF"
  val borderColorHex: String = "#D4AF37",
  val borderWidthDp: Int = 0,
  val cornerRadiusDp: Int = 4,
  val prefix: String = "",
  val suffix: String = "",
  // Photo specific settings
  val photoShape: String = "CIRCLE", // "CIRCLE", "SQUARE", "ROUNDED", "GOLD_FRAME"
  val photoSizeDp: Int = 84,
  val photoBorderWidthDp: Int = 3,
  val photoBorderColorHex: String = "#D4AF37"
)

object TemplateFieldHelper {

  fun getAvailableFieldCatalog(): List<Pair<String, String>> {
    return listOf(
      "PHOTO" to "દાતા / વ્યક્તિ ફોટો (Photo)",
      "NAME" to "દાતાનું પૂરું નામ (Donor Name)",
      "DECEASED_NAME" to "દિવંગત પૂજ્યશ્રીનું નામ (Deceased Name)",
      "FAMILY_NAME" to "પરિવાર / ગામ (Family / Gam)",
      "FESTIVAL_NAME" to "તહેવારનું નામ (Festival Name)",
      "DATE" to "તારીખ (Date)",
      "AMOUNT" to "દાન રાશિ (Donation Amount)",
      "MESSAGE" to "શુભેચ્છા / શ્રદ્ધાંજલિ સંદેશ (Blessing Message)",
      "LOGO" to "ગૌશાળા સત્તાવાર લોગો (Trust Logo)",
      "GAM" to "ગામ / શહેર (Village / Gam)",
      "SEVA_TYPE" to "સેવા પ્રકાર (Seva Category)",
      "DONATION_ID" to "રસીદ ID (Donation ID)",
      "PAYMENT_TYPE" to "ચુકવણી માધ્યમ (Payment Type)",
      "DEDICATED_TO" to "સંકલ્પ / સ્મરણાર્થ (Dedication)"
    )
  }

  fun getFieldsForCategory(category: String, subCategory: String = ""): List<TemplateFieldSetting> {
    return when {
      category.contains("Birthday", ignoreCase = true) || category.contains("જન્મદિવસ") -> {
        listOf(
          TemplateFieldSetting(
            fieldKey = "PHOTO",
            labelGujarati = "જન્મદિવસ વ્યક્તિ ફોટો (Birthday Photo)",
            isEnabled = true,
            xPercent = 50f,
            yPercent = 20f,
            photoShape = "GOLD_FRAME",
            photoSizeDp = 92,
            photoBorderWidthDp = 3,
            photoBorderColorHex = "#D4AF37"
          ),
          TemplateFieldSetting(
            fieldKey = "NAME",
            labelGujarati = "જન્મદિવસ વ્યક્તિ / દાતા (Name)",
            isEnabled = true,
            xPercent = 50f,
            yPercent = 37f,
            fontSizeSp = 20,
            fontWeight = "EXTRA_BOLD",
            fontStyle = "SERIF",
            textAlign = "CENTER",
            textColorHex = "#800000",
            prefix = "શ્રી "
          ),
          TemplateFieldSetting(
            fieldKey = "DATE",
            labelGujarati = "જન્મદિવસ તારીખ (Date)",
            isEnabled = true,
            xPercent = 50f,
            yPercent = 44f,
            fontSizeSp = 13,
            fontWeight = "BOLD",
            textAlign = "CENTER",
            textColorHex = "#E65100",
            prefix = "શુભ જન્મતિથિ: "
          ),
          TemplateFieldSetting(
            fieldKey = "AMOUNT",
            labelGujarati = "દાન રાશિ (Amount)",
            isEnabled = true,
            xPercent = 50f,
            yPercent = 52f,
            fontSizeSp = 22,
            fontWeight = "EXTRA_BOLD",
            textAlign = "CENTER",
            textColorHex = "#B71C1C",
            bgColorHex = "#FFF9C4",
            borderColorHex = "#D4AF37",
            borderWidthDp = 1,
            cornerRadiusDp = 8,
            prefix = "ગૌસેવા દાન રાશિ: ₹ "
          ),
          TemplateFieldSetting(
            fieldKey = "MESSAGE",
            labelGujarati = "જન્મદિવસ શુભેચ્છા સંદેશ (Blessing Message)",
            isEnabled = true,
            xPercent = 50f,
            yPercent = 63f,
            fontSizeSp = 12,
            fontWeight = "MEDIUM",
            fontStyle = "SERIF",
            textAlign = "CENTER",
            textColorHex = "#3E2723",
            prefix = "આયુષ્યમાન ભવઃ • ગૌમાતા આપના જીવનમાં સુખ, સ્વાસ્થ્ય અને દીર્ઘાયુ આપે."
          ),
          TemplateFieldSetting(
            fieldKey = "GAM",
            labelGujarati = "ગામ / શહેર (Gam)",
            isEnabled = true,
            xPercent = 30f,
            yPercent = 74f,
            fontSizeSp = 12,
            fontWeight = "BOLD",
            textAlign = "CENTER",
            textColorHex = "#37474F",
            prefix = "ગામ: "
          ),
          TemplateFieldSetting(
            fieldKey = "DONATION_ID",
            labelGujarati = "રસીદ ID (Receipt ID)",
            isEnabled = true,
            xPercent = 70f,
            yPercent = 74f,
            fontSizeSp = 12,
            fontWeight = "BOLD",
            textAlign = "CENTER",
            textColorHex = "#E65100",
            prefix = "રસીદ નં: "
          )
        )
      }

      category.contains("Shradhanjali", ignoreCase = true) || category.contains("શ્રદ્ધાંજલિ") || category.contains("પુણ્યતિથિ") -> {
        listOf(
          TemplateFieldSetting(
            fieldKey = "PHOTO",
            labelGujarati = "દિવંગત સ્વજનનો ફોટો (Memorial Photo)",
            isEnabled = true,
            xPercent = 50f,
            yPercent = 20f,
            photoShape = "ROUNDED",
            photoSizeDp = 90,
            photoBorderWidthDp = 2,
            photoBorderColorHex = "#90A4AE"
          ),
          TemplateFieldSetting(
            fieldKey = "DECEASED_NAME",
            labelGujarati = "દિવંગત પૂજ્યશ્રીનું નામ (In Sacred Memory)",
            isEnabled = true,
            xPercent = 50f,
            yPercent = 37f,
            fontSizeSp = 19,
            fontWeight = "EXTRA_BOLD",
            fontStyle = "SERIF",
            textAlign = "CENTER",
            textColorHex = "#263238",
            prefix = "સ્વ. "
          ),
          TemplateFieldSetting(
            fieldKey = "DATE",
            labelGujarati = "પુણ્યતિથિ તારીખ (Date)",
            isEnabled = true,
            xPercent = 50f,
            yPercent = 44f,
            fontSizeSp = 13,
            fontWeight = "BOLD",
            textAlign = "CENTER",
            textColorHex = "#455A64",
            prefix = "પાવન પુણ્યતિથિ: "
          ),
          TemplateFieldSetting(
            fieldKey = "NAME",
            labelGujarati = "દાતાશ્રીનું નામ (Donor Name)",
            isEnabled = true,
            xPercent = 50f,
            yPercent = 51f,
            fontSizeSp = 14,
            fontWeight = "BOLD",
            textAlign = "CENTER",
            textColorHex = "#37474F",
            prefix = "દાતા: શ્રી "
          ),
          TemplateFieldSetting(
            fieldKey = "AMOUNT",
            labelGujarati = "દાન રાશિ (Amount)",
            isEnabled = true,
            xPercent = 50f,
            yPercent = 58f,
            fontSizeSp = 20,
            fontWeight = "EXTRA_BOLD",
            textAlign = "CENTER",
            textColorHex = "#1B5E20",
            bgColorHex = "#F1F8E9",
            borderColorHex = "#81C784",
            borderWidthDp = 1,
            cornerRadiusDp = 6,
            prefix = "પુણ્ય સ્મરણાર્થે ગૌદાન: ₹ "
          ),
          TemplateFieldSetting(
            fieldKey = "MESSAGE",
            labelGujarati = "શ્રદ્ધાંજલિ સંદેશ (Message)",
            isEnabled = true,
            xPercent = 50f,
            yPercent = 68f,
            fontSizeSp = 12,
            fontWeight = "MEDIUM",
            fontStyle = "SERIF",
            textAlign = "CENTER",
            textColorHex = "#37474F",
            prefix = "॥ ૐ શાંતિઃ શાંતિઃ શાંતિઃ • પ્રભુ દિવંગત આત્માને મોક્ષ પદ અર્પે ॥"
          ),
          TemplateFieldSetting(
            fieldKey = "FAMILY_NAME",
            labelGujarati = "પરિવાર / ગામ (Family / Gam)",
            isEnabled = true,
            xPercent = 50f,
            yPercent = 77f,
            fontSizeSp = 12,
            fontWeight = "SEMI_BOLD",
            textAlign = "CENTER",
            textColorHex = "#546E7A",
            prefix = "શોકગ્રસ્ત પરિવાર: "
          )
        )
      }

      category.contains("Festival", ignoreCase = true) || category.contains("તહેવાર") || category.contains("Diwali") || category.contains("Janmashtami") || category.contains("Navratri") -> {
        val festLabel = if (subCategory.isNotBlank()) subCategory else "પવિત્ર પર્વ"
        listOf(
          TemplateFieldSetting(
            fieldKey = "PHOTO",
            labelGujarati = "દાતા ફોટો (Donor Photo)",
            isEnabled = true,
            xPercent = 50f,
            yPercent = 20f,
            photoShape = "GOLD_FRAME",
            photoSizeDp = 88,
            photoBorderWidthDp = 3,
            photoBorderColorHex = "#D4AF37"
          ),
          TemplateFieldSetting(
            fieldKey = "FESTIVAL_NAME",
            labelGujarati = "તહેવારનું નામ (Festival Name)",
            isEnabled = true,
            xPercent = 50f,
            yPercent = 36f,
            fontSizeSp = 16,
            fontWeight = "EXTRA_BOLD",
            fontStyle = "SERIF",
            textAlign = "CENTER",
            textColorHex = "#B71C1C",
            prefix = "॥ શુભ $festLabel ગૌસેવા સમર્પણ ॥"
          ),
          TemplateFieldSetting(
            fieldKey = "NAME",
            labelGujarati = "દાતાશ્રીનું નામ (Donor Name)",
            isEnabled = true,
            xPercent = 50f,
            yPercent = 44f,
            fontSizeSp = 19,
            fontWeight = "EXTRA_BOLD",
            fontStyle = "SERIF",
            textAlign = "CENTER",
            textColorHex = "#800000",
            prefix = "શ્રી "
          ),
          TemplateFieldSetting(
            fieldKey = "AMOUNT",
            labelGujarati = "દાન રાશિ (Amount)",
            isEnabled = true,
            xPercent = 50f,
            yPercent = 53f,
            fontSizeSp = 22,
            fontWeight = "EXTRA_BOLD",
            textAlign = "CENTER",
            textColorHex = "#B71C1C",
            bgColorHex = "#FFF9C4",
            borderColorHex = "#D4AF37",
            borderWidthDp = 1,
            cornerRadiusDp = 8,
            prefix = "પર્વ દાન રાશિ: ₹ "
          ),
          TemplateFieldSetting(
            fieldKey = "DATE",
            labelGujarati = "તારીખ (Date)",
            isEnabled = true,
            xPercent = 30f,
            yPercent = 63f,
            fontSizeSp = 12,
            fontWeight = "BOLD",
            textAlign = "CENTER",
            textColorHex = "#37474F",
            prefix = "તારીખ: "
          ),
          TemplateFieldSetting(
            fieldKey = "GAM",
            labelGujarati = "ગામ / શહેર (Gam)",
            isEnabled = true,
            xPercent = 70f,
            yPercent = 63f,
            fontSizeSp = 12,
            fontWeight = "BOLD",
            textAlign = "CENTER",
            textColorHex = "#37474F",
            prefix = "ગામ: "
          ),
          TemplateFieldSetting(
            fieldKey = "MESSAGE",
            labelGujarati = "પર્વ આશીર્વાદ સંદેશ (Blessing Message)",
            isEnabled = true,
            xPercent = 50f,
            yPercent = 73f,
            fontSizeSp = 12,
            fontWeight = "MEDIUM",
            fontStyle = "SERIF",
            textAlign = "CENTER",
            textColorHex = "#5D4037",
            prefix = "ગૌમાતા આપના પરિવારને સુખ, શાંતિ, ઐશ્વર્ય અને સમૃદ્ધિ પ્રદાન કરે."
          )
        )
      }

      else -> getDefaultFields()
    }
  }

  fun getDefaultFields(): List<TemplateFieldSetting> {
    return listOf(
      TemplateFieldSetting(
        fieldKey = "PHOTO",
        labelGujarati = "દાતા ફોટો (Donor Photo)",
        isEnabled = true,
        xPercent = 50f,
        yPercent = 22f,
        photoShape = "CIRCLE",
        photoSizeDp = 86,
        photoBorderWidthDp = 3,
        photoBorderColorHex = "#D4AF37"
      ),
      TemplateFieldSetting(
        fieldKey = "NAME",
        labelGujarati = "દાતાશ્રીનું નામ (Donor Name)",
        isEnabled = true,
        xPercent = 50f,
        yPercent = 38f,
        fontSizeSp = 20,
        fontWeight = "EXTRA_BOLD",
        fontStyle = "SERIF",
        textAlign = "CENTER",
        textColorHex = "#800000",
        prefix = "શ્રી "
      ),
      TemplateFieldSetting(
        fieldKey = "SEVA_TYPE",
        labelGujarati = "સેવા પ્રકાર (Seva Category)",
        isEnabled = true,
        xPercent = 50f,
        yPercent = 45f,
        fontSizeSp = 14,
        fontWeight = "BOLD",
        fontStyle = "SANS",
        textAlign = "CENTER",
        textColorHex = "#E65100",
        prefix = "સેવા: "
      ),
      TemplateFieldSetting(
        fieldKey = "AMOUNT",
        labelGujarati = "દાન રાશિ (Donation Amount)",
        isEnabled = true,
        xPercent = 50f,
        yPercent = 54f,
        fontSizeSp = 22,
        fontWeight = "EXTRA_BOLD",
        textAlign = "CENTER",
        textColorHex = "#B71C1C",
        bgColorHex = "#FFF9C4",
        borderColorHex = "#D4AF37",
        borderWidthDp = 1,
        cornerRadiusDp = 8,
        prefix = "દાન રકમ: ₹ "
      ),
      TemplateFieldSetting(
        fieldKey = "DATE",
        labelGujarati = "તારીખ (Date)",
        isEnabled = true,
        xPercent = 28f,
        yPercent = 64f,
        fontSizeSp = 13,
        fontWeight = "BOLD",
        textAlign = "CENTER",
        textColorHex = "#37474F",
        prefix = "તારીખ: "
      ),
      TemplateFieldSetting(
        fieldKey = "GAM",
        labelGujarati = "ગામ / શહેર (Village / Gam)",
        isEnabled = true,
        xPercent = 72f,
        yPercent = 64f,
        fontSizeSp = 13,
        fontWeight = "BOLD",
        textAlign = "CENTER",
        textColorHex = "#37474F",
        prefix = "ગામ: "
      ),
      TemplateFieldSetting(
        fieldKey = "PAYMENT_TYPE",
        labelGujarati = "પેમેન્ટ પ્રકાર (Payment Type)",
        isEnabled = true,
        xPercent = 32f,
        yPercent = 73f,
        fontSizeSp = 12,
        fontWeight = "BOLD",
        textAlign = "CENTER",
        textColorHex = "#1B5E20",
        prefix = "માધ્યમ: "
      ),
      TemplateFieldSetting(
        fieldKey = "DONATION_ID",
        labelGujarati = "રસીદ ID (Donation ID)",
        isEnabled = true,
        xPercent = 68f,
        yPercent = 73f,
        fontSizeSp = 12,
        fontWeight = "BOLD",
        textAlign = "CENTER",
        textColorHex = "#E65100",
        prefix = "રસીદ નં: "
      ),
      TemplateFieldSetting(
        fieldKey = "DEDICATED_TO",
        labelGujarati = "પર્વ / સ્મરણાર્થે (Festival / Dedicated)",
        isEnabled = true,
        xPercent = 50f,
        yPercent = 80f,
        fontSizeSp = 12,
        fontWeight = "MEDIUM",
        textAlign = "CENTER",
        textColorHex = "#5D4037",
        prefix = "વિશેષ સમર્પણ: "
      )
    )
  }

  fun toJson(fields: List<TemplateFieldSetting>): String {
    val array = JSONArray()
    for (f in fields) {
      val obj = JSONObject()
      obj.put("fieldKey", f.fieldKey)
      obj.put("labelGujarati", f.labelGujarati)
      obj.put("isEnabled", f.isEnabled)
      obj.put("xPercent", f.xPercent.toDouble())
      obj.put("yPercent", f.yPercent.toDouble())
      obj.put("widthPercent", f.widthPercent.toDouble())
      obj.put("fontSizeSp", f.fontSizeSp)
      obj.put("fontWeight", f.fontWeight)
      obj.put("fontStyle", f.fontStyle)
      obj.put("textAlign", f.textAlign)
      obj.put("textColorHex", f.textColorHex)
      obj.put("bgColorHex", f.bgColorHex)
      obj.put("borderColorHex", f.borderColorHex)
      obj.put("borderWidthDp", f.borderWidthDp)
      obj.put("cornerRadiusDp", f.cornerRadiusDp)
      obj.put("prefix", f.prefix)
      obj.put("suffix", f.suffix)
      obj.put("photoShape", f.photoShape)
      obj.put("photoSizeDp", f.photoSizeDp)
      obj.put("photoBorderWidthDp", f.photoBorderWidthDp)
      obj.put("photoBorderColorHex", f.photoBorderColorHex)
      array.put(obj)
    }
    return array.toString()
  }

  fun fromJson(json: String?): List<TemplateFieldSetting> {
    if (json.isNullOrBlank()) return getDefaultFields()
    val list = mutableListOf<TemplateFieldSetting>()
    try {
      val array = JSONArray(json)
      for (i in 0 until array.length()) {
        val obj = array.getJSONObject(i)
        list.add(
          TemplateFieldSetting(
            fieldKey = obj.optString("fieldKey", ""),
            labelGujarati = obj.optString("labelGujarati", ""),
            isEnabled = obj.optBoolean("isEnabled", true),
            xPercent = obj.optDouble("xPercent", 50.0).toFloat(),
            yPercent = obj.optDouble("yPercent", 50.0).toFloat(),
            widthPercent = obj.optDouble("widthPercent", 0.0).toFloat(),
            fontSizeSp = obj.optInt("fontSizeSp", 16),
            fontWeight = obj.optString("fontWeight", "BOLD"),
            fontStyle = obj.optString("fontStyle", "SANS"),
            textAlign = obj.optString("textAlign", "CENTER"),
            textColorHex = obj.optString("textColorHex", "#800000"),
            bgColorHex = obj.optString("bgColorHex", "TRANSPARENT"),
            borderColorHex = obj.optString("borderColorHex", "#D4AF37"),
            borderWidthDp = obj.optInt("borderWidthDp", 0),
            cornerRadiusDp = obj.optInt("cornerRadiusDp", 4),
            prefix = obj.optString("prefix", ""),
            suffix = obj.optString("suffix", ""),
            photoShape = obj.optString("photoShape", "CIRCLE"),
            photoSizeDp = obj.optInt("photoSizeDp", 84),
            photoBorderWidthDp = obj.optInt("photoBorderWidthDp", 3),
            photoBorderColorHex = obj.optString("photoBorderColorHex", "#D4AF37")
          )
        )
      }
      if (list.isEmpty()) return getDefaultFields()
      return list
    } catch (_: Exception) {
      return getDefaultFields()
    }
  }
}
