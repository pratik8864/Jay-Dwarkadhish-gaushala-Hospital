package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.GauGreen
import com.example.ui.theme.GoldBorder
import com.example.ui.theme.GoldBorderSubtle
import com.example.ui.theme.GoldChampagne
import com.example.ui.theme.GoldChampagneDark
import com.example.ui.theme.GoldChampagneLight
import com.example.ui.theme.NavyBackground
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavySurface
import com.example.ui.theme.NavySurfaceVariant
import com.example.ui.theme.SacredCrimson
import com.example.ui.theme.TextGoldenHighlight
import com.example.ui.theme.TextMutedBlueGray
import com.example.ui.theme.TextWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PaymentGatewayDialog(
  amount: Double,
  title: String,
  donorName: String,
  mobile: String,
  onPaymentSuccess: () -> Unit,
  onPaymentFailed: (String) -> Unit,
  onDismiss: () -> Unit
) {
  var selectedMethod by remember { mutableStateOf("UPI") }
  var isProcessing by remember { mutableStateOf(false) }
  var failureMessage by remember { mutableStateOf<String?>(null) }
  val scope = rememberCoroutineScope()

  Dialog(onDismissRequest = { if (!isProcessing) onDismiss() }) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(22.dp))
        .border(1.dp, GoldBorder, RoundedCornerShape(22.dp))
        .testTag("payment_dialog"),
      color = NavyBackground
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(20.dp)
      ) {
        // Top Title & Secure Badge
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Lock,
              contentDescription = "સુરક્ષિત પેમેન્ટ",
              tint = GoldChampagne,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "સુરક્ષિત ગૌસેવા પેમેન્ટ ગેટવે",
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = TextWhite
            )
          }
          if (!isProcessing) {
            IconButton(onClick = onDismiss) {
              Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "રદ કરો",
                tint = TextMutedBlueGray
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Order Summary Card
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = NavySurface),
          border = BorderStroke(1.dp, GoldBorderSubtle)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = title,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = GoldChampagneLight
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(text = "દાતાનું નામ:", fontSize = 12.sp, color = TextMutedBlueGray)
              Text(text = donorName, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextWhite)
            }
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(text = "મોબાઈલ:", fontSize = 12.sp, color = TextMutedBlueGray)
              Text(text = mobile, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextWhite)
            }
            HorizontalDivider(
              modifier = Modifier.padding(vertical = 8.dp),
              color = NavyBorder.copy(alpha = 0.5f)
            )
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "ચૂકવવાપાત્ર કુલ સેવા રકમ:",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextWhite
              )
              Text(
                text = "₹ ${amount.toInt()}",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextGoldenHighlight
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Payment Method Options
        Text(
          text = "ચૂકવણી પદ્ધતિ પસંદ કરો (Select Payment Mode):",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = TextWhite
        )
        Spacer(modifier = Modifier.height(8.dp))

        PaymentOptionTile(
          title = "UPI (Google Pay, PhonePe, Paytm, BHIM)",
          subtitle = "ત્વરિત UPI એપ્લિકેશન દ્વારા ચૂકવો",
          icon = Icons.Default.Smartphone,
          isSelected = selectedMethod == "UPI",
          onClick = { selectedMethod = "UPI" }
        )

        PaymentOptionTile(
          title = "QR Code સ્કેનર (Scan & Pay)",
          subtitle = "કોઈપણ UPI એપથી QR સ્કેન કરો",
          icon = Icons.Default.QrCode,
          isSelected = selectedMethod == "QR",
          onClick = { selectedMethod = "QR" }
        )

        PaymentOptionTile(
          title = "ડેબિટ / ક્રેડિટ કાર્ડ (Cards)",
          subtitle = "Visa, MasterCard, RuPay કાર્ડ્સ",
          icon = Icons.Default.CreditCard,
          isSelected = selectedMethod == "CARD",
          onClick = { selectedMethod = "CARD" }
        )

        PaymentOptionTile(
          title = "નેટ બેંકિંગ (Net Banking)",
          subtitle = "SBI, HDFC, ICICI, BoB અને તમામ બેંકો",
          icon = Icons.Default.AccountBalance,
          isSelected = selectedMethod == "NET_BANKING",
          onClick = { selectedMethod = "NET_BANKING" }
        )

        if (selectedMethod == "QR") {
          Spacer(modifier = Modifier.height(10.dp))
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(Color.White, RoundedCornerShape(12.dp))
              .border(1.dp, GoldChampagne, RoundedCornerShape(12.dp))
              .padding(12.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(
                imageVector = Icons.Default.QrCode,
                contentDescription = "ગૌશાળા QR",
                modifier = Modifier.size(100.dp),
                tint = Color.Black
              )
              Text(
                text = "ગૌશાળા અધિકૃત UPI QR કોડ",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
              )
              Text(
                text = "UPI ID: gaushala.trust@sbi",
                fontSize = 11.sp,
                color = NavyBackground,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        // Failure Notice (Rule 1 & 2 verification)
        AnimatedVisibility(visible = failureMessage != null) {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF4A151B)),
            border = BorderStroke(1.dp, SacredCrimson)
          ) {
            Row(
              modifier = Modifier.padding(10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Error,
                contentDescription = "ભૂલ",
                tint = SacredCrimson,
                modifier = Modifier.size(24.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = failureMessage ?: "",
                color = TextWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isProcessing) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            CircularProgressIndicator(color = GoldChampagne)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "બેંક સાથે સુરક્ષિત કનેક્શન થઈ રહ્યું છે...",
              fontSize = 12.sp,
              color = TextMutedBlueGray
            )
          }
        } else {
          // Success & Failure Verification Actions
          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
              onClick = {
                isProcessing = true
                failureMessage = null
                scope.launch {
                  delay(900)
                  isProcessing = false
                  onPaymentSuccess()
                }
              },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("pay_success_btn"),
              colors = ButtonDefaults.buttonColors(containerColor = GauGreen),
              shape = RoundedCornerShape(12.dp)
            ) {
              Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "સફળ")
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "ચૂકવણી પૂર્ણ કરો (Pay ₹${amount.toInt()})",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
              )
            }

            OutlinedButton(
              onClick = {
                isProcessing = true
                scope.launch {
                  delay(600)
                  isProcessing = false
                  val failMsg = "ચૂકવણી નિષ્ફળ થઈ (બેંક સર્વર ટાઈમઆઉટ). કોઈ રકમ કપાયેલ નથી તથા કોઈ પત્રિકા બનશે નહીં."
                  failureMessage = failMsg
                  onPaymentFailed(failMsg)
                }
              },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("pay_fail_btn"),
              border = BorderStroke(1.dp, SacredCrimson.copy(alpha = 0.7f)),
              colors = ButtonDefaults.outlinedButtonColors(contentColor = SacredCrimson),
              shape = RoundedCornerShape(12.dp)
            ) {
              Icon(imageVector = Icons.Default.Close, contentDescription = "નિષ્ફળ")
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "ટેસ્ટ: ચૂકવણી નિષ્ફળ કરો (Simulate Payment Failure)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun PaymentOptionTile(
  title: String,
  subtitle: String,
  icon: ImageVector,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp)
      .clickable { onClick() },
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isSelected) NavySurfaceVariant else NavySurface
    ),
    border = BorderStroke(
      width = if (isSelected) 1.5.dp else 1.dp,
      color = if (isSelected) GoldChampagne else NavyBorder
    )
  ) {
    Row(
      modifier = Modifier.padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(38.dp)
          .clip(CircleShape)
          .then(
            if (isSelected) {
              Modifier.background(
                Brush.radialGradient(
                  listOf(GoldChampagne.copy(alpha = 0.3f), NavyDark)
                )
              )
            } else {
              Modifier.background(NavyDark)
            }
          ),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = title,
          tint = if (isSelected) GoldChampagne else TextMutedBlueGray,
          modifier = Modifier.size(20.dp)
        )
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          fontWeight = FontWeight.SemiBold,
          fontSize = 13.sp,
          color = if (isSelected) GoldChampagneLight else TextWhite
        )
        Text(
          text = subtitle,
          fontSize = 11.sp,
          color = TextMutedBlueGray
        )
      }
    }
  }
}
