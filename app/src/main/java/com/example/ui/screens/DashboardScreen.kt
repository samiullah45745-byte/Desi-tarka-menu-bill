package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.RestaurantViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: RestaurantViewModel,
    onNavigateToPos: () -> Unit,
    onNavigateToKitchen: () -> Unit,
    onNavigateToChatbot: () -> Unit,
    onNavigateToDemand: () -> Unit,
    onNavigateToStaff: () -> Unit
) {
    val activeOrders by viewModel.activeOrders.collectAsState()
    val allOrders by viewModel.allOrders.collectAsState()
    
    val todaySales = allOrders.sumOf { it.totalAmount }
    val netProfit = todaySales * 0.42 // Mock value for net profit

    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.primary,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(MaterialTheme.colorScheme.secondary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("DT", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }
                        Column {
                            Text("Desi Tarka", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                            Text("FAMILY RESTAURANT", fontSize = 10.sp, color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.9f), letterSpacing = 1.sp)
                        }
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToPos,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Order", modifier = Modifier.size(32.dp))
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 0.dp, modifier = Modifier.border(1.dp, Color.LightGray.copy(alpha = 0.3f))) {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Box(modifier = Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape).padding(horizontal = 16.dp, vertical = 4.dp)) { Icon(Icons.Default.Home, contentDescription = null) } },
                    label = { Text("Home", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, selectedTextColor = MaterialTheme.colorScheme.primary)
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToPos,
                    icon = { Icon(Icons.Default.TableRestaurant, contentDescription = null) },
                    label = { Text("Tables", fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToStaff,
                    icon = { Icon(Icons.Default.People, contentDescription = null) },
                    label = { Text("Staff", fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToChatbot,
                    icon = { Icon(Icons.Default.SmartToy, contentDescription = null) },
                    label = { Text("AI Chat", fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Key Metrics Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
                    .padding(12.dp)
                    .border(1.dp, RedLightBorder, RoundedCornerShape(12.dp)),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricCard(
                    modifier = Modifier.weight(1f),
                    title = "TODAY'S SALES",
                    value = "Rs $todaySales",
                    valueColor = MaterialTheme.colorScheme.primary
                )
                MetricCard(
                    modifier = Modifier.weight(1f),
                    title = "NET PROFIT",
                    value = "Rs ${netProfit.toInt()}",
                    valueColor = SuccessGreen
                )
                MetricCard(
                    modifier = Modifier.weight(1f),
                    title = "TOTAL EXP",
                    value = "Rs 12400",
                    valueColor = WarningAmber
                )
            }

            Column(modifier = Modifier.padding(12.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Live Status Grid
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("LIVE STATUS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray, letterSpacing = 0.5.sp)
                        Box(modifier = Modifier.background(RedLightBg, CircleShape).padding(horizontal = 8.dp, vertical = 2.dp)) {
                            Text("${activeOrders.size} New", fontSize = 10.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatusCard(modifier = Modifier.weight(1f), title = "Dine-In (Tables)", count = "12/20", borderColor = WarningAmber)
                        StatusCard(modifier = Modifier.weight(1f), title = "Home Delivery", count = "08", borderColor = Color(0xFF60A5FA))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatusCard(modifier = Modifier.weight(1f), title = "Take Away", count = "05", borderColor = Color(0xFFFB923C))
                        StatusCard(modifier = Modifier.weight(1f), title = "Pending Kitchen", count = "${activeOrders.size}", borderColor = MaterialTheme.colorScheme.primary)
                    }
                }

                // Operational Alerts
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(LightAmberBg, RoundedCornerShape(16.dp))
                        .border(1.dp, LightAmberBorder, RoundedCornerShape(16.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("CRITICAL ALERTS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF92400E))
                    AlertRow(color = MaterialTheme.colorScheme.error, message = "Low Stock: ", boldText = "Chicken (2kg), Oil (5L)", action = "REFILL")
                    AlertRow(color = WarningAmber, message = "Staff Salary Due: ", boldText = "4 Employees", action = "VIEW")
                }

                // Quick Actions
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("QUICK ACCESS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray, letterSpacing = 0.5.sp)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        QuickActionItem(
                            modifier = Modifier.weight(1f),
                            title = "POS BILL",
                            icon = Icons.Default.PointOfSale,
                            isPrimary = true,
                            onClick = onNavigateToPos
                        )
                        QuickActionItem(
                            modifier = Modifier.weight(1f),
                            title = "DEMAND",
                            icon = Icons.Default.Inventory,
                            onClick = onNavigateToDemand
                        )
                        QuickActionItem(
                            modifier = Modifier.weight(1f),
                            title = "KITCHEN",
                            icon = Icons.Default.Kitchen,
                            onClick = onNavigateToKitchen
                        )
                        QuickActionItem(
                            modifier = Modifier.weight(1f),
                            title = "REPORTS",
                            icon = Icons.Default.Analytics,
                            onClick = { }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MetricCard(modifier: Modifier = Modifier, title: String, value: String, valueColor: Color) {
    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, RedLightBorder, RoundedCornerShape(12.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(title, fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
        Spacer(modifier = Modifier.height(2.dp))
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = valueColor)
    }
}

@Composable
fun StatusCard(modifier: Modifier = Modifier, title: String, count: String, borderColor: Color) {
    Row(
        modifier = modifier
            .height(48.dp)
            .background(Color.White, RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
            .border(1.dp, Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
            .padding(start = 0.dp) // Border takes place here
            .clickable { },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.width(4.dp).fillMaxHeight().background(borderColor))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
            Text(count, fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.Black)
        }
    }
}

@Composable
fun AlertRow(color: Color, message: String, boldText: String, action: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(modifier = Modifier.size(8.dp).background(color, CircleShape))
        Row(modifier = Modifier.weight(1f)) {
            Text(message, fontSize = 12.sp, color = Color.DarkGray)
            Text(boldText, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        }
        Text(action, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun QuickActionItem(modifier: Modifier = Modifier, title: String, icon: ImageVector, isPrimary: Boolean = false, onClick: () -> Unit) {
    val bgColor = if (isPrimary) MaterialTheme.colorScheme.primary else Color.White
    val contentColor = if (isPrimary) MaterialTheme.colorScheme.secondary else Color.DarkGray
    val borderColor = if (isPrimary) Color.Transparent else Color.LightGray.copy(alpha = 0.3f)
    
    Column(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(title, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isPrimary) contentColor else Color.Black)
    }
}

