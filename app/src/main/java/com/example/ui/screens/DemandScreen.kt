package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import com.example.ui.RestaurantViewModel
import com.example.data.DemandMasterItem

data class DemandItem(val name: String, val quantity: String, val unit: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemandScreen(viewModel: RestaurantViewModel, onNavigateBack: () -> Unit) {
    var itemName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var selectedUnit by remember { mutableStateOf("Kilo") }
    
    var expandedName by remember { mutableStateOf(false) }
    var expandedUnit by remember { mutableStateOf(false) }
    
    val units = listOf("Kilo", "Grams", "Liters", "Thela", "Gattu", "Jali", "Packs", "Pieces", "Dozen")
    
    val masterItems by viewModel.demandMasterItems.collectAsState()
    
    var demandItems by remember { mutableStateOf(listOf<DemandItem>()) }
    var showSlipDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Demand List") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            if (demandItems.isNotEmpty()) {
                ExtendedFloatingActionButton(
                    onClick = { showSlipDialog = true },
                    icon = { Icon(Icons.Default.Receipt, contentDescription = null) },
                    text = { Text("Generate Slip") },
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Add Item Form
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Add Demand Item", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    
                    ExposedDropdownMenuBox(
                        expanded = expandedName,
                        onExpandedChange = { expandedName = it },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = itemName,
                            onValueChange = { itemName = it; expandedName = true },
                            label = { Text("Item Name (e.g., Onion, Chicken, Oil)") },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            singleLine = true
                        )
                        // Filter items based on input
                        val filteredItems = masterItems.filter { it.name.contains(itemName, ignoreCase = true) }
                        if (filteredItems.isNotEmpty()) {
                            ExposedDropdownMenu(
                                expanded = expandedName,
                                onDismissRequest = { expandedName = false }
                            ) {
                                filteredItems.forEach { masterItem ->
                                    DropdownMenuItem(
                                        text = { Text(masterItem.name) },
                                        onClick = {
                                            itemName = masterItem.name
                                            selectedUnit = masterItem.defaultUnit
                                            expandedName = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = quantity,
                            onValueChange = { quantity = it },
                            label = { Text("Qty") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                        
                        ExposedDropdownMenuBox(
                            expanded = expandedUnit,
                            onExpandedChange = { expandedUnit = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = selectedUnit,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Unit") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedUnit) },
                                modifier = Modifier.menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedUnit,
                                onDismissRequest = { expandedUnit = false }
                            ) {
                                units.forEach { unit ->
                                    DropdownMenuItem(
                                        text = { Text(unit) },
                                        onClick = {
                                            selectedUnit = unit
                                            expandedUnit = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    
                    Button(
                        onClick = {
                            if (itemName.isNotBlank() && quantity.isNotBlank()) {
                                // Add to demand list
                                demandItems = demandItems + DemandItem(itemName, quantity, selectedUnit)
                                
                                // Save to master list if it doesn't exist
                                if (masterItems.none { it.name.equals(itemName, ignoreCase = true) }) {
                                    viewModel.addDemandMasterItem(DemandMasterItem(name = itemName, defaultUnit = selectedUnit))
                                }
                                
                                itemName = ""
                                quantity = ""
                                expandedName = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Add to List")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // List of Items
            Text("Current Demand List", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            
            if (demandItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                    Text("No items added yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(demandItems) { item ->
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(item.name, fontWeight = FontWeight.Bold)
                                    Text("${item.quantity} ${item.unit}", color = MaterialTheme.colorScheme.primary)
                                }
                                IconButton(onClick = { 
                                    demandItems = demandItems.filter { it != item } 
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }
        }
        
        if (showSlipDialog) {
            AlertDialog(
                onDismissRequest = { showSlipDialog = false },
                title = { Text("Demand Slip Preview", fontWeight = FontWeight.Bold) },
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
                            text = "Daily Demand Slip",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Text("Date: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())}")
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Item", fontWeight = FontWeight.Bold, modifier = Modifier.weight(2f))
                            Text("Qty", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        demandItems.forEach { item ->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(item.name, modifier = Modifier.weight(2f))
                                Text("${item.quantity} ${item.unit}", modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                            }
                        }
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Text("Total Items: ${demandItems.size}", fontWeight = FontWeight.Bold)
                    }
                },
                confirmButton = {
                    Button(onClick = { showSlipDialog = false }) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Share WhatsApp")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showSlipDialog = false }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}
