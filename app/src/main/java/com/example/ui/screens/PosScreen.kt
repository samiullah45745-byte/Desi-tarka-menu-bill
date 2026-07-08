package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.MenuItem
import com.example.data.OrderItem
import com.example.ui.RestaurantViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PosScreen(
    viewModel: RestaurantViewModel,
    onBack: () -> Unit
) {
    val menuItems by viewModel.menuItems.collectAsState()
    var selectedCategory by remember { mutableStateOf("All") }
    var cartItems by remember { mutableStateOf(mapOf<MenuItem, Int>()) }
    var showReceiptDialog by remember { mutableStateOf(false) }
    
    var orderType by remember { mutableStateOf("Dine-in") }
    val orderTypes = listOf("Dine-in", "Takeaway", "Delivery")
    var tableNumber by remember { mutableStateOf("") }
    
    val categories = listOf("All") + menuItems.map { it.category }.distinct()
    val displayedItems = if (selectedCategory == "All") menuItems else menuItems.filter { it.category == selectedCategory }
    
    val totalAmount = cartItems.entries.sumOf { it.key.price * it.value }

    if (showReceiptDialog) {
        AlertDialog(
            onDismissRequest = { showReceiptDialog = false },
            title = { Text("Receipt Preview", fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "DESI TARKA RESTAURANT",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Receipt",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Date: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())}")
                    Text("Type: $orderType")
                    if (orderType == "Dine-in") {
                        Text("Table: ${tableNumber.ifEmpty { "N/A" }}")
                    }
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Item", fontWeight = FontWeight.Bold, modifier = Modifier.weight(2f))
                        Text("Qty", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                        Text("Price", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    cartItems.forEach { (item, qty) ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(item.name, modifier = Modifier.weight(2f))
                            Text("$qty", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                            Text("${item.price * qty}", modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                        }
                    }
                    
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total:", fontWeight = FontWeight.Bold)
                        Text("Rs $totalAmount", fontWeight = FontWeight.Bold)
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    val orderItems = cartItems.map { (item, qty) ->
                        OrderItem(orderId = 0, menuItemId = item.id, menuItemName = item.name, quantity = qty, price = item.price, notes = "")
                    }
                    viewModel.placeOrder(orderType, tableNumber, "Walk-in", "", orderItems)
                    cartItems = emptyMap()
                    tableNumber = ""
                    showReceiptDialog = false
                }) {
                    Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Print & Save")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showReceiptDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("POS / Billing") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total: Rs $totalAmount", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                        Button(onClick = { showReceiptDialog = true }) {
                            Text("Checkout")
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            // Order Type & Table Selection
            Card(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        orderTypes.forEach { type ->
                            FilterChip(
                                selected = orderType == type,
                                onClick = { orderType = type },
                                label = { Text(type) }
                            )
                        }
                    }
                    
                    if (orderType == "Dine-in") {
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = tableNumber,
                            onValueChange = { tableNumber = it },
                            label = { Text("Table Number") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                }
            }
            
            Row(modifier = Modifier.fillMaxSize()) {
                // Categories
                LazyColumn(
                    modifier = Modifier.weight(1f).padding(8.dp)
                ) {
                    items(categories) { category ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { selectedCategory = category },
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedCategory == category) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Text(category, modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Medium)
                        }
                    }
                }
                
                // Menu Items
                LazyColumn(
                    modifier = Modifier.weight(2f).padding(8.dp)
                ) {
                    items(displayedItems) { item ->
                        val qty = cartItems[item] ?: 0
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(item.name, fontWeight = FontWeight.Bold)
                                    Text("Rs ${item.price}", color = MaterialTheme.colorScheme.primary)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (qty > 0) {
                                        IconButton(onClick = { 
                                            cartItems = cartItems.toMutableMap().apply { 
                                                if (qty == 1) remove(item) else put(item, qty - 1) 
                                            }
                                        }) { Icon(Icons.Default.Remove, "Remove") }
                                        Text("$qty", modifier = Modifier.padding(horizontal = 8.dp))
                                    }
                                    IconButton(onClick = { 
                                        cartItems = cartItems.toMutableMap().apply { put(item, qty + 1) }
                                    }) { Icon(Icons.Default.Add, "Add") }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
