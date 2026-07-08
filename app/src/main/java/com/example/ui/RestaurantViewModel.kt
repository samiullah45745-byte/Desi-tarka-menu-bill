package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RestaurantViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = RestaurantRepository(database.restaurantDao())

    val menuItems: StateFlow<List<MenuItem>> = repository.allMenuItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val activeOrders: StateFlow<List<Order>> = repository.activeOrders.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    
    val allOrders: StateFlow<List<Order>> = repository.allOrders.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val staff: StateFlow<List<Staff>> = repository.allStaff.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val demandMasterItems: StateFlow<List<DemandMasterItem>> = repository.allDemandMasterItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            // Check if DB is empty and prefill
            launch {
                repository.allMenuItems.collect { items ->
                    if (items.isEmpty()) {
                        repository.prefillMenu()
                    }
                }
            }
            launch {
                repository.allDemandMasterItems.collect { items ->
                    if (items.isEmpty()) {
                        val defaultDemandItems = listOf(
                            DemandMasterItem(name = "Chicken", defaultUnit = "Kilo"),
                            DemandMasterItem(name = "Tomato", defaultUnit = "Kilo"),
                            DemandMasterItem(name = "Onion", defaultUnit = "Kilo"),
                            DemandMasterItem(name = "Ginger", defaultUnit = "Kilo"),
                            DemandMasterItem(name = "Garlic", defaultUnit = "Kilo"),
                            DemandMasterItem(name = "Yogurt", defaultUnit = "Kilo"),
                            DemandMasterItem(name = "Cream", defaultUnit = "Packs"),
                            DemandMasterItem(name = "Garam Masala", defaultUnit = "Grams"),
                            DemandMasterItem(name = "White Cumin", defaultUnit = "Grams"),
                            DemandMasterItem(name = "Black Pepper", defaultUnit = "Grams")
                        )
                        repository.insertDemandMasterItems(defaultDemandItems)
                    }
                }
            }
        }
    }

    fun addDemandMasterItem(item: DemandMasterItem) {
        viewModelScope.launch {
            repository.insertDemandMasterItem(item)
        }
    }

    fun addStaff(staff: Staff) {
        viewModelScope.launch {
            repository.insertStaff(staff)
        }
    }
    
    fun getStaffAdvances(staffId: Int) = repository.getStaffAdvances(staffId)
    
    fun addStaffAdvance(advance: StaffAdvance) {
        viewModelScope.launch {
            repository.insertStaffAdvance(advance)
        }
    }

    fun placeOrder(orderType: String, tableOrAddress: String, customerName: String, customerPhone: String, items: List<OrderItem>) {
        viewModelScope.launch {
            val total = items.sumOf { it.price * it.quantity }
            val order = Order(
                orderType = orderType,
                customerName = customerName,
                customerPhone = customerPhone,
                tableNumber = tableOrAddress,
                status = "PENDING",
                totalAmount = total
            )
            repository.insertOrderWithItems(order, items)
        }
    }

    fun updateOrderStatus(orderId: Int, status: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, status)
        }
    }
}
