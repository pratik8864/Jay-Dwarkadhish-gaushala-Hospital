package com.example.ui.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.GoldBorder
import com.example.ui.theme.GoldChampagne
import com.example.ui.theme.GoldChampagneDark
import com.example.ui.theme.GoldChampagneLight
import com.example.ui.theme.NavyBackground
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavySurface
import com.example.ui.theme.SacredCrimson
import com.example.ui.theme.TextGoldenHighlight
import com.example.ui.theme.TextMutedBlueGray
import com.example.ui.theme.TextWhite
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RegistrationDialog(
  onRegister: (name: String, mobile: String, dob: String, email: String, city: String, taluka: String, district: String) -> Unit,
  onDismiss: () -> Unit
) {
  val todayIso = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

  var fullName by remember { mutableStateOf("") }
  var mobileNumber by remember { mutableStateOf("") }
  var dateOfBirth by remember { mutableStateOf(todayIso) }
  var email by remember { mutableStateOf("") }
  var city by remember { mutableStateOf("આણંદ") }
  var taluka by remember { mutableStateOf("આણંદ") }
  var district by remember { mutableStateOf("આણંદ") }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  val textFieldColors = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = NavyDark,
    unfocusedContainerColor = NavyDark,
    focusedBorderColor = GoldChampagne,
    unfocusedBorderColor = NavyBorder,
    focusedTextColor = TextWhite,
    unfocusedTextColor = TextWhite,
    focusedLabelColor = GoldChampagne,
    unfocusedLabelColor = TextMutedBlueGray
  )

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(22.dp))
        .border(1.dp, GoldBorder, RoundedCornerShape(22.dp))
        .testTag("registration_dialog"),
      color = NavyBackground
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(20.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "નવી ગૌભક્ત નોંધણી",
              fontWeight = FontWeight.ExtraBold,
              fontSize = 17.sp,
              color = TextWhite
            )
            Text(
              text = "ગૌશાળા પરિવાર સાથે જોડાઓ",
              fontSize = 11.sp,
              color = GoldChampagneLight
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "બંધ કરો",
              tint = TextMutedBlueGray
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
          value = fullName,
          onValueChange = { fullName = it },
          label = { Text("પૂરું નામ (Full Name) *") },
          leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = GoldChampagne) },
          modifier = Modifier.fillMaxWidth().testTag("reg_name_input"),
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          colors = textFieldColors
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = mobileNumber,
          onValueChange = { if (it.length <= 10 && it.all { ch -> ch.isDigit() }) mobileNumber = it },
          label = { Text("મોબાઈલ નંબર (Mobile Number) *") },
          leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = GoldChampagne) },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
          modifier = Modifier.fillMaxWidth().testTag("reg_mobile_input"),
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          colors = textFieldColors
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = dateOfBirth,
          onValueChange = { dateOfBirth = it },
          label = { Text("જન્મ તારીખ (YYYY-MM-DD) *") },
          supportingText = { Text("જન્મદિવસે ઓટોમેટિક ગૌમાતાના આશીર્વાદ અને શુભેચ્છા માટે", color = TextMutedBlueGray, fontSize = 10.sp) },
          leadingIcon = { Icon(Icons.Default.Cake, contentDescription = null, tint = GoldChampagne) },
          modifier = Modifier.fillMaxWidth().testTag("reg_dob_input"),
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          colors = textFieldColors
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = email,
          onValueChange = { email = it },
          label = { Text("ઈમેલ એડ્રેસ (Email - વૈકલ્પિક)") },
          leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = GoldChampagne) },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
          modifier = Modifier.fillMaxWidth().testTag("reg_email_input"),
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          colors = textFieldColors
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("ગામ / શહેર") },
            modifier = Modifier.weight(1f).testTag("reg_city_input"),
            shape = RoundedCornerShape(12.dp),
            colors = textFieldColors
          )
          OutlinedTextField(
            value = taluka,
            onValueChange = { taluka = it },
            label = { Text("તાલુકો") },
            modifier = Modifier.weight(1f).testTag("reg_taluka_input"),
            shape = RoundedCornerShape(12.dp),
            colors = textFieldColors
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = district,
          onValueChange = { district = it },
          label = { Text("જિલ્લો (District)") },
          leadingIcon = { Icon(Icons.Default.LocationCity, contentDescription = null, tint = GoldChampagne) },
          modifier = Modifier.fillMaxWidth().testTag("reg_district_input"),
          shape = RoundedCornerShape(12.dp),
          colors = textFieldColors
        )

        if (errorMessage != null) {
          Spacer(modifier = Modifier.height(8.dp))
          Text(text = errorMessage ?: "", color = SacredCrimson, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
              Brush.horizontalGradient(
                listOf(GoldChampagneLight, GoldChampagne, GoldChampagneDark)
              )
            )
            .border(1.dp, GoldChampagne, RoundedCornerShape(12.dp))
            .padding(vertical = 12.dp)
            .testTag("submit_registration_btn"),
          contentAlignment = Alignment.Center
        ) {
          Row(
            modifier = Modifier.clickable {
              if (fullName.isBlank() || mobileNumber.isBlank() || dateOfBirth.isBlank()) {
                errorMessage = "કૃપા કરીને નામ, મોબાઈલ અને જન્મતારીખ દાખલ કરો."
              } else {
                onRegister(fullName, mobileNumber, dateOfBirth, email, city, taluka, district)
                onDismiss()
              }
            }
          ) {
            Text(
              text = "પ્રોફાઈલ બનાવો અને જોડાઓ",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = NavyDark
            )
          }
        }
      }
    }
  }
}
