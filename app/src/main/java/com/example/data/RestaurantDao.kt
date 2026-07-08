package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {
    // Menu
    @Query("SELECT * FROM menu_items")
    fun getAllMenuItems(): Flow<List<MenuItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuItem(menuItem: MenuItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuItems(menuItems: List<MenuItem>)

    // Orders
    @Query("SELECT * FROM orders ORDER BY orderTime DESC")
    fun getAllOrders(): Flow<List<Order>>
    
    @Query("SELECT * FROM orders WHERE status = 'PENDING' OR status = 'PREPARING'")
    fun getActiveOrders(): Flow<List<Order>>

    @Insert
    suspend fun insertOrder(order: Order): Long

    @Query("UPDATE orders SET status = :status WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: Int, status: String)

    // Order Items
    @Insert
    suspend fun insertOrderItems(items: List<OrderItem>)

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    suspend fun getOrderItems(orderId: Int): List<OrderItem>

    // Staff
    @Query("SELECT * FROM staff")
    fun getAllStaff(): Flow<List<Staff>>

    @Insert
    suspend fun insertStaff(staff: Staff)
    
    @Query("SELECT * FROM staff_advances WHERE staffId = :staffId ORDER BY date DESC")
    fun getStaffAdvances(staffId: Int): Flow<List<StaffAdvance>>
    
    @Query("SELECT * FROM staff_advances WHERE staffId = :staffId AND date >= :startDate AND date <= :endDate ORDER BY date DESC")
    suspend fun getStaffAdvancesForPeriod(staffId: Int, startDate: Long, endDate: Long): List<StaffAdvance>
    
    @Insert
    suspend fun insertStaffAdvance(advance: StaffAdvance)

    // Expenses
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Insert
    suspend fun insertExpense(expense: Expense)
    
    // Inventory
    @Query("SELECT * FROM inventory")
    fun getAllInventory(): Flow<List<Inventory>>
    
    @Insert
    suspend fun insertInventory(inventory: Inventory)
    // Demand Master Items
    @Query("SELECT * FROM demand_master_items ORDER BY name ASC")
    fun getAllDemandMasterItems(): Flow<List<DemandMasterItem>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDemandMasterItem(item: DemandMasterItem)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDemandMasterItems(items: List<DemandMasterItem>)
}
