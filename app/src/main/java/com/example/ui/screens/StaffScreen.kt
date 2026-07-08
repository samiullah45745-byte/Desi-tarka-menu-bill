package com.example.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.window.Dialog
import com.example.data.Staff
import com.example.data.StaffAdvance
import com.example.ui.RestaurantViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffScreen(
    viewModel: RestaurantViewModel,
    onNavigateBack: () -> Unit
) {
    val staffList by viewModel.staff.collectAsState()
    var showAddStaffDialog by remember { mutableStateOf(false) }
    var selectedStaff by remember { mutableStateOf<Staff?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Staff Khata (Ledger)") },
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
            ExtendedFloatingActionButton(
                onClick = { showAddStaffDialog = true },
                icon = { Icon(Icons.Default.PersonAdd, contentDescription = null) },
                text = { Text("Add Staff") }
            )
        }
    ) { paddingValues ->
        if (selectedStaff == null) {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
                if (staffList.isEmpty()) {
                    item {
                        Text("No staff added yet.", modifier = Modifier.padding(16.dp))
                    }
                }
                items(staffList) { staff ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { selectedStaff = staff },
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(40.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(staff.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                Text(staff.designation, style = MaterialTheme.typography.bodyMedium)
                            }
                            Text("Daily: Rs ${staff.salary}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            StaffLedgerScreen(
                staff = selectedStaff!!,
                viewModel = viewModel,
                onBack = { selectedStaff = null }
            )
        }
        
        if (showAddStaffDialog) {
            var name by remember { mutableStateOf("") }
            var designation by remember { mutableStateOf("") }
            var dailyWage by remember { mutableStateOf("") }
            
            AlertDialog(
                onDismissRequest = { showAddStaffDialog = false },
                title = { Text("Add New Staff") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = designation,
                            onValueChange = { designation = it },
                            label = { Text("Designation/Role") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = dailyWage,
                            onValueChange = { dailyWage = it },
                            label = { Text("Daily Wage (Rs)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        val wage = dailyWage.toDoubleOrNull() ?: 0.0
                        if (name.isNotBlank() && wage > 0) {
                            viewModel.addStaff(Staff(name = name, phone = "", designation = designation, salary = wage))
                            showAddStaffDialog = false
                        }
                    }) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddStaffDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun StaffLedgerScreen(
    staff: Staff,
    viewModel: RestaurantViewModel,
    onBack: () -> Unit
) {
    var showAdvanceDialog by remember { mutableStateOf(false) }
    var showWeeklySlipDialog by remember { mutableStateOf(false) }
    
    // Derived state for advances
    val advances by viewModel.getStaffAdvances(staff.id).collectAsState(initial = emptyList())
    
    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Text("${staff.name}'s Ledger", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
                Text("Designation: ${staff.designation}", modifier = Modifier.padding(start = 48.dp))
                Text("Daily Wage: Rs ${staff.salary}", modifier = Modifier.padding(start = 48.dp), fontWeight = FontWeight.Bold)
            }
        }
        
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { showAdvanceDialog = true },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.MoneyOff, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Give Advance")
            }
            Button(
                onClick = { showWeeklySlipDialog = true },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Icon(Icons.Default.Receipt, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Weekly Slip")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Text("Advance History", fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp))
        
        LazyColumn(modifier = Modifier.weight(1f).padding(16.dp)) {
            items(advances) { advance ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Advance Given", fontWeight = FontWeight.Bold)
                            Text(SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(advance.date)), style = MaterialTheme.typography.bodySmall)
                            if (advance.notes.isNotEmpty()) {
                                Text(advance.notes, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Text("-Rs ${advance.amount}", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
    
    if (showAdvanceDialog) {
        var amount by remember { mutableStateOf("") }
        var notes by remember { mutableStateOf("") }
        
        AlertDialog(
            onDismissRequest = { showAdvanceDialog = false },
            title = { Text("Give Advance (Pesy Dena)") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text("Amount (Rs)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Notes (Optional)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    val advAmount = amount.toDoubleOrNull() ?: 0.0
                    if (advAmount > 0) {
                        viewModel.addStaffAdvance(StaffAdvance(staffId = staff.id, amount = advAmount, notes = notes))
                        showAdvanceDialog = false
                    }
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAdvanceDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    
    if (showWeeklySlipDialog) {
        var daysWorked by remember { mutableStateOf("7") }
        
        // Calculate weekly total
        val calendar = Calendar.getInstance()
        val endDate = calendar.timeInMillis
        // Go back 7 days
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val startDate = calendar.timeInMillis
        
        // Filter advances for the last 7 days
        val weeklyAdvances = advances.filter { it.date in startDate..endDate }
        val totalAdvance = weeklyAdvances.sumOf { it.amount }
        val dWorked = daysWorked.toIntOrNull() ?: 0
        val totalSalary = staff.salary * dWorked
        val netPayable = totalSalary - totalAdvance
        
        Dialog(onDismissRequest = { showWeeklySlipDialog = false }) {
            Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "WEEKLY SALARY SLIP",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Name: ${staff.name}")
                    Text("Designation: ${staff.designation}")
                    Text("Daily Wage: Rs ${staff.salary}")
                    
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Days Worked: ", fontWeight = FontWeight.Bold)
                        OutlinedTextField(
                            value = daysWorked,
                            onValueChange = { daysWorked = it },
                            modifier = Modifier.width(80.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                    }
                    
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total Salary ($dWorked days):")
                        Text("Rs $totalSalary", fontWeight = FontWeight.Bold)
                    }
                    
                    if (weeklyAdvances.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Advances this week:", fontWeight = FontWeight.Bold)
                        weeklyAdvances.forEach { adv ->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("- ${SimpleDateFormat("EEE", Locale.getDefault()).format(Date(adv.date))}")
                                Text("-Rs ${adv.amount}", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                    
                    Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total Advances:")
                        Text("-Rs $totalAdvance", color = MaterialTheme.colorScheme.error)
                    }
                    
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("NET PAYABLE:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Text("Rs $netPayable", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = if (netPayable >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { showWeeklySlipDialog = false }) {
                            Text("Close")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = { /* Print logic */ }) {
                            Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Print / Share")
                        }
                    }
                }
            }
        }
    }
}
