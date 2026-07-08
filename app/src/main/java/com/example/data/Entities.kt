package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_items")
data class MenuItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String,
    val price: Double,
    val imageRes: Int = 0 // Mocking image with local resources
)

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderType: String, // "DINE_IN", "TAKE_AWAY", "DELIVERY"
    val customerName: String,
    val customerPhone: String,
    val tableNumber: String, // Or address for delivery
    val status: String, // "PENDING", "PREPARING", "READY", "DELIVERED", "CANCELLED"
    val totalAmount: Double,
    val orderTime: Long = System.currentTimeMillis()
)

@Entity(tableName = "order_items")
data class OrderItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderId: Int,
    val menuItemId: Int,
    val menuItemName: String,
    val quantity: Int,
    val price: Double,
    val notes: String
)

@Entity(tableName = "staff")
data class Staff(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val phone: String,
    val designation: String,
    val salary: Double
)

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String,
    val amount: Double,
    val notes: String,
    val date: Long = System.currentTimeMillis()
)

@Entity(tableName = "inventory")
data class Inventory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val itemName: String,
    val quantity: Int,
    val unit: String
)

@Entity(tableName = "staff_advances")
data class StaffAdvance(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val staffId: Int,
    val amount: Double,
    val date: Long = System.currentTimeMillis(),
    val notes: String = ""
)

@Entity(tableName = "demand_master_items")
data class DemandMasterItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val defaultUnit: String = "Kilo"
)
