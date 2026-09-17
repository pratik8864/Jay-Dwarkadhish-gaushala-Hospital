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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.CrackerOrder
import com.example.data.model.CrackerProduct
import com.example.data.model.User
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

@Composable
fun CrackersShopScreen(
  products: List<CrackerProduct>,
  cart: Map<Long, Int>,
  currentUser: User?,
  isProductVisible: (CrackerProduct) -> Boolean,
  onUpdateCart: (Long, Int) -> Unit,
  onCheckout: (name: String, mobile: String, address: String, items: List<CrackerProduct>, onComplete: (CrackerOrder) -> Unit) -> Unit
) {
  val visibleProducts = products.filter { isProductVisible(it) }
  var selectedCategory by remember { mutableStateOf("બધા (All)") }
  var showCheckoutDialog by remember { mutableStateOf(false) }

  val categories = listOf("બધા (All)") + visibleProducts.map { it.category }.distinct()
  val filteredProducts = if (selectedCategory == "બધા (All)") {
    visibleProducts
  } else {
    visibleProducts.filter { it.category == selectedCategory }
  }

  // Calculate total in cart
  val totalCartAmount = cart.entries.sumOf { (productId, qty) ->
    val p = products.find { it.id == productId }
    (p?.offerPrice ?: 0.0) * qty
  }
  val totalItemsCount = cart.values.sum()

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(NavyBackground, NavyDark)
        )
      )
      .testTag("crackers_screen")
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp, vertical = 12.dp)
        .padding(bottom = if (totalItemsCount > 0) 86.dp else 0.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Header Banner in Dark Navy with Gold Accents
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = NavySurface),
          border = BorderStroke(1.dp, GoldBorder)
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(
                  Brush.radialGradient(
                    listOf(GoldChampagne.copy(alpha = 0.3f), NavyDark)
                  )
                )
                .border(1.2.dp, GoldChampagne, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text(text = "🎆", fontSize = 22.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "ગૌશાળા સ્પેશિયલ દિવાળી ફટાકડા વેચાણ",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = TextWhite
              )
              Text(
                text = "ઓછા ધુમાડાવાળા, સલામત અને પ્રીમિયમ ફટાકડા. તમામ નફો ગૌસેવામાં વપરાશે!",
                fontSize = 11.sp,
                color = TextMutedBlueGray
              )
            }
          }
        }
      }

      // Categories Filter Chips
      item {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          items(categories) { cat ->
            val isSelected = selectedCategory == cat
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .then(
                  if (isSelected) {
                    Modifier.background(
                      Brush.horizontalGradient(
                        listOf(GoldChampagneLight, GoldChampagne, GoldChampagneDark)
                      )
                    )
                  } else {
                    Modifier.background(NavySurface)
                  }
                )
                .border(
                  1.dp,
                  if (isSelected) GoldChampagne else NavyBorder,
                  RoundedCornerShape(20.dp)
                )
                .clickable { selectedCategory = cat }
                .padding(horizontal = 14.dp, vertical = 7.dp)
            ) {
              Text(
                text = cat,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) NavyDark else TextWhite
              )
            }
          }
        }
      }

      if (filteredProducts.isEmpty()) {
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(40.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(text = "આ કેટેગરીમાં કોઈ ફટાકડા ઉપલબ્ધ નથી.", color = TextMutedBlueGray)
          }
        }
      } else {
        items(filteredProducts) { product ->
          val currentQty = cart[product.id] ?: 0
          CrackerItemCard(
            product = product,
            quantityInCart = currentQty,
            onAdd = { onUpdateCart(product.id, 1) },
            onRemove = { onUpdateCart(product.id, -1) }
          )
        }
      }
    }

    // Sticky Bottom Cart Bar in Luxury Navy & Champagne Gold
    if (totalItemsCount > 0) {
      Surface(
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .fillMaxWidth()
          .padding(16.dp)
          .testTag("cart_bottom_bar"),
        shape = RoundedCornerShape(16.dp),
        color = NavyDark,
        border = BorderStroke(1.dp, GoldBorder),
        shadowElevation = 10.dp
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(
                  Brush.radialGradient(
                    listOf(GoldChampagne.copy(alpha = 0.35f), NavySurface)
                  )
                )
                .border(1.dp, GoldChampagne, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                tint = GoldChampagne
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "$totalItemsCount વસ્તુઓ • ₹${totalCartAmount.toInt()}",
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
              )
              Text(
                text = "ગૌશાળા દિવાળી ઓર્ડર",
                color = GoldChampagneLight,
                fontSize = 11.sp
              )
            }
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .background(
                Brush.horizontalGradient(
                  listOf(GoldChampagneLight, GoldChampagne, GoldChampagneDark)
                )
              )
              .clickable { showCheckoutDialog = true }
              .padding(horizontal = 14.dp, vertical = 8.dp)
              .testTag("open_checkout_btn"),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "ઓર્ડર કરો →",
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = NavyDark
            )
          }
        }
      }
    }

    // Checkout Dialog
    if (showCheckoutDialog) {
      CrackerCheckoutDialog(
        totalAmount = totalCartAmount,
        currentUser = currentUser,
        onDismiss = { showCheckoutDialog = false },
        onConfirmOrder = { name, mobile, address ->
          onCheckout(name, mobile, address, products) {
            showCheckoutDialog = false
          }
        }
      )
    }
  }
}

@Composable
private fun CrackerItemCard(
  product: CrackerProduct,
  quantityInCart: Int,
  onAdd: () -> Unit,
  onRemove: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("cracker_card_${product.id}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = NavySurface),
    border = BorderStroke(1.dp, NavyBorder)
  ) {
    Row(
      modifier = Modifier.padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(54.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(NavySurfaceVariant)
          .border(1.dp, GoldBorderSubtle, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
      ) {
        Text(text = "🧨", fontSize = 26.sp)
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = product.name,
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = TextWhite
        )
        Text(
          text = product.description,
          fontSize = 11.sp,
          color = TextMutedBlueGray,
          maxLines = 1
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "₹${product.offerPrice.toInt()}",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 15.sp,
            color = TextGoldenHighlight
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "₹${product.price.toInt()}",
            fontSize = 11.sp,
            color = TextMutedBlueGray.copy(alpha = 0.7f),
            textDecoration = TextDecoration.LineThrough
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "સ્ટોક: ${product.stock}",
            fontSize = 10.sp,
            color = GoldChampagneLight,
            fontWeight = FontWeight.Medium
          )
        }
      }

      Spacer(modifier = Modifier.width(8.dp))

      // Quantity Controller
      if (quantityInCart == 0) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(
              Brush.horizontalGradient(
                listOf(GoldChampagneLight, GoldChampagne, GoldChampagneDark)
              )
            )
            .clickable { onAdd() }
            .padding(horizontal = 12.dp, vertical = 7.dp)
            .testTag("add_cracker_btn_${product.id}"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "ઉમેરો +",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = NavyDark
          )
        }
      } else {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(NavySurfaceVariant)
            .border(1.dp, GoldBorder, RoundedCornerShape(10.dp))
            .padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
          IconButton(onClick = onRemove, modifier = Modifier.size(28.dp)) {
            Icon(Icons.Default.Remove, contentDescription = "બાદ કરો", tint = GoldChampagne)
          }
          Text(
            text = "$quantityInCart",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = TextWhite,
            modifier = Modifier.padding(horizontal = 6.dp)
          )
          IconButton(onClick = onAdd, modifier = Modifier.size(28.dp)) {
            Icon(Icons.Default.Add, contentDescription = "ઉમેરો", tint = GoldChampagne)
          }
        }
      }
    }
  }
}

@Composable
private fun CrackerCheckoutDialog(
  totalAmount: Double,
  currentUser: User?,
  onDismiss: () -> Unit,
  onConfirmOrder: (name: String, mobile: String, address: String) -> Unit
) {
  var name by remember { mutableStateOf(currentUser?.fullName ?: "") }
  var mobile by remember { mutableStateOf(currentUser?.mobileNumber ?: "") }
  var address by remember { mutableStateOf(currentUser?.city ?: "") }
  var error by remember { mutableStateOf<String?>(null) }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))
        .border(1.dp, GoldBorder, RoundedCornerShape(20.dp))
        .testTag("cracker_checkout_dialog"),
      color = NavyBackground
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "ફટાકડા ઓર્ડર પુષ્ટિ",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,
            color = TextWhite
          )
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "બંધ કરો", tint = TextMutedBlueGray)
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Total summary box
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(NavySurface)
            .border(1.dp, GoldBorderSubtle, RoundedCornerShape(12.dp))
            .padding(12.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = "કુલ ચુકવણી પાત્ર રકમ:", color = TextMutedBlueGray, fontSize = 12.sp)
            Text(
              text = "₹${totalAmount.toInt()}",
              fontWeight = FontWeight.ExtraBold,
              fontSize = 16.sp,
              color = TextGoldenHighlight
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("તમારું પૂરું નામ *", color = TextMutedBlueGray) },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = NavyDark,
            unfocusedContainerColor = NavyDark,
            focusedBorderColor = GoldChampagne,
            unfocusedBorderColor = NavyBorder,
            focusedTextColor = TextWhite,
            unfocusedTextColor = TextWhite
          )
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = mobile,
          onValueChange = { mobile = it },
          label = { Text("મોબાઇલ નંબર (વોટ્સએપ) *", color = TextMutedBlueGray) },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
          modifier = Modifier.fillMaxWidth(),
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = NavyDark,
            unfocusedContainerColor = NavyDark,
            focusedBorderColor = GoldChampagne,
            unfocusedBorderColor = NavyBorder,
            focusedTextColor = TextWhite,
            unfocusedTextColor = TextWhite
          )
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = address,
          onValueChange = { address = it },
          label = { Text("ડિલિવરી સરનામું / ગામ *", color = TextMutedBlueGray) },
          modifier = Modifier.fillMaxWidth(),
          maxLines = 2,
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = NavyDark,
            unfocusedContainerColor = NavyDark,
            focusedBorderColor = GoldChampagne,
            unfocusedBorderColor = NavyBorder,
            focusedTextColor = TextWhite,
            unfocusedTextColor = TextWhite
          )
        )

        if (error != null) {
          Spacer(modifier = Modifier.height(6.dp))
          Text(text = error ?: "", color = SacredCrimson, fontSize = 11.sp)
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
            .clickable {
              if (name.isBlank() || mobile.isBlank() || address.isBlank()) {
                error = "કૃપા કરીને નામ, મોબાઇલ અને સરનામું ભરો."
              } else {
                onConfirmOrder(name, mobile, address)
              }
            }
            .padding(vertical = 12.dp)
            .testTag("confirm_checkout_button"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "ઓર્ડર કન્ફર્મ કરો (₹${totalAmount.toInt()})",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = NavyDark
          )
        }
      }
    }
  }
}
