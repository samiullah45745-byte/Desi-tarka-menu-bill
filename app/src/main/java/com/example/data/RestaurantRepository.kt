package com.example.data

import kotlinx.coroutines.flow.Flow

class RestaurantRepository(private val dao: RestaurantDao) {
    val allMenuItems: Flow<List<MenuItem>> = dao.getAllMenuItems()
    val allOrders: Flow<List<Order>> = dao.getAllOrders()
    val activeOrders: Flow<List<Order>> = dao.getActiveOrders()
    val allStaff: Flow<List<Staff>> = dao.getAllStaff()
    val allExpenses: Flow<List<Expense>> = dao.getAllExpenses()
    val allInventory: Flow<List<Inventory>> = dao.getAllInventory()
    val allDemandMasterItems: Flow<List<DemandMasterItem>> = dao.getAllDemandMasterItems()

    suspend fun insertOrderWithItems(order: Order, items: List<OrderItem>) {
        val orderId = dao.insertOrder(order)
        val itemsWithOrderId = items.map { it.copy(orderId = orderId.toInt()) }
        dao.insertOrderItems(itemsWithOrderId)
    }

    suspend fun updateOrderStatus(orderId: Int, status: String) {
        dao.updateOrderStatus(orderId, status)
    }
    
    suspend fun getOrderItems(orderId: Int): List<OrderItem> {
        return dao.getOrderItems(orderId)
    }
    
    suspend fun insertMenuItem(menuItem: MenuItem) = dao.insertMenuItem(menuItem)
    suspend fun insertStaff(staff: Staff) = dao.insertStaff(staff)
    
    fun getStaffAdvances(staffId: Int): Flow<List<StaffAdvance>> = dao.getStaffAdvances(staffId)
    
    suspend fun getStaffAdvancesForPeriod(staffId: Int, startDate: Long, endDate: Long): List<StaffAdvance> = 
        dao.getStaffAdvancesForPeriod(staffId, startDate, endDate)
        
    suspend fun insertStaffAdvance(advance: StaffAdvance) = dao.insertStaffAdvance(advance)
    
    suspend fun insertExpense(expense: Expense) = dao.insertExpense(expense)
    suspend fun insertInventory(inventory: Inventory) = dao.insertInventory(inventory)
    
    suspend fun insertDemandMasterItem(item: DemandMasterItem) = dao.insertDemandMasterItem(item)
    suspend fun insertDemandMasterItems(items: List<DemandMasterItem>) = dao.insertDemandMasterItems(items)
    
    suspend fun prefillMenu() {
        val count = dao.getAllMenuItems()
        val defaultItems = listOf(
            // BBQ
            MenuItem(name = "Chicken Tikka Boti (Per Seekh)", category = "BBQ", price = 200.0),
            MenuItem(name = "Chicken Malai Boti", category = "BBQ", price = 300.0),
            MenuItem(name = "Chicken Kabab", category = "BBQ", price = 150.0),
            MenuItem(name = "Beef Kabab", category = "BBQ", price = 180.0),
            MenuItem(name = "Hariyali Boti", category = "BBQ", price = 300.0),
            MenuItem(name = "Achari Boti", category = "BBQ", price = 220.0),
            MenuItem(name = "Qalmi Tikka (4 Piece)", category = "BBQ", price = 700.0),
            MenuItem(name = "Kofta Kabab", category = "BBQ", price = 350.0),
            MenuItem(name = "Pizza Kabab", category = "BBQ", price = 350.0),
            MenuItem(name = "Reshmi Kabab", category = "BBQ", price = 300.0),
            MenuItem(name = "Leg Piece", category = "BBQ", price = 350.0),
            MenuItem(name = "Chest Piece", category = "BBQ", price = 400.0),
            MenuItem(name = "Malai Chest", category = "BBQ", price = 500.0),
            MenuItem(name = "Malai Leg", category = "BBQ", price = 450.0),

            // Chicken Karahi
            MenuItem(name = "Special Desi Tarka Karahi Half", category = "Chicken Karahi", price = 1050.0),
            MenuItem(name = "Special Desi Tarka Karahi Full", category = "Chicken Karahi", price = 1800.0),
            MenuItem(name = "White Karahi Half", category = "Chicken Karahi", price = 1100.0),
            MenuItem(name = "White Karahi Full", category = "Chicken Karahi", price = 1900.0),
            MenuItem(name = "Achari Karahi Half", category = "Chicken Karahi", price = 1100.0),
            MenuItem(name = "Achari Karahi Full", category = "Chicken Karahi", price = 2100.0),
            MenuItem(name = "Shinwari Karahi Half", category = "Chicken Karahi", price = 1250.0),
            MenuItem(name = "Shinwari Karahi Full", category = "Chicken Karahi", price = 2100.0),
            MenuItem(name = "Afghani Karahi Half", category = "Chicken Karahi", price = 1250.0),
            MenuItem(name = "Afghani Karahi Full", category = "Chicken Karahi", price = 2100.0),
            MenuItem(name = "Chicken Karahi Black Pepper Half", category = "Chicken Karahi", price = 1250.0),
            MenuItem(name = "Chicken Karahi Black Pepper Full", category = "Chicken Karahi", price = 2100.0),
            MenuItem(name = "Chicken Nawabi Karahi Half", category = "Chicken Karahi", price = 1100.0),
            MenuItem(name = "Chicken Nawabi Karahi Full", category = "Chicken Karahi", price = 2100.0),
            MenuItem(name = "Chicken Lemon Mint Karahi Half", category = "Chicken Karahi", price = 1100.0),
            MenuItem(name = "Chicken Lemon Mint Karahi Full", category = "Chicken Karahi", price = 2100.0),
            MenuItem(name = "Chicken Madrasi Karahi Half", category = "Chicken Karahi", price = 1100.0),
            MenuItem(name = "Chicken Madrasi Karahi Full", category = "Chicken Karahi", price = 2100.0),
            MenuItem(name = "Chicken Tikka Karahi Half", category = "Chicken Karahi", price = 1100.0),
            MenuItem(name = "Chicken Tikka Karahi Full", category = "Chicken Karahi", price = 2100.0),
            MenuItem(name = "Chicken Kabab Karahi Half", category = "Chicken Karahi", price = 1100.0),
            MenuItem(name = "Chicken Kabab Karahi Full", category = "Chicken Karahi", price = 2100.0),

            // Beef
            MenuItem(name = "Beef Special Desi Tarka Karahi Half", category = "Beef", price = 1250.0),
            MenuItem(name = "Beef Special Desi Tarka Karahi Full", category = "Beef", price = 2400.0),
            MenuItem(name = "Beef White Karahi Half", category = "Beef", price = 1350.0),
            MenuItem(name = "Beef White Karahi Full", category = "Beef", price = 2600.0),
            MenuItem(name = "Beef Achari Karahi Half", category = "Beef", price = 1250.0),
            MenuItem(name = "Beef Achari Karahi Full", category = "Beef", price = 2400.0),
            MenuItem(name = "Beef Shinwari Karahi Half", category = "Beef", price = 1250.0),
            MenuItem(name = "Beef Shinwari Karahi Full", category = "Beef", price = 2500.0),
            MenuItem(name = "Beef Afghani Karahi Half", category = "Beef", price = 1250.0),
            MenuItem(name = "Beef Afghani Karahi Full", category = "Beef", price = 2500.0),
            MenuItem(name = "Beef Mardan Karahi Half", category = "Beef", price = 1250.0),
            MenuItem(name = "Beef Mardan Karahi Full", category = "Beef", price = 2500.0),
            MenuItem(name = "Beef Boneless Karahi Half", category = "Beef", price = 1250.0),
            MenuItem(name = "Beef Boneless Karahi Full", category = "Beef", price = 2500.0),
            MenuItem(name = "Beef Sulemani Karahi Half", category = "Beef", price = 1250.0),
            MenuItem(name = "Beef Sulemani Karahi Full", category = "Beef", price = 2500.0),
            MenuItem(name = "Beef Namkeen Fried Half", category = "Beef", price = 1250.0),
            MenuItem(name = "Beef Namkeen Fried Full", category = "Beef", price = 2500.0),

            // Mutton
            MenuItem(name = "Mutton Special Desi Tarka Karahi Half", category = "Mutton", price = 2050.0),
            MenuItem(name = "Mutton Special Desi Tarka Karahi Full", category = "Mutton", price = 3800.0),
            MenuItem(name = "Mutton White Karahi Half", category = "Mutton", price = 2000.0),
            MenuItem(name = "Mutton White Karahi Full", category = "Mutton", price = 3900.0),
            MenuItem(name = "Mutton Achari Karahi Half", category = "Mutton", price = 2000.0),
            MenuItem(name = "Mutton Achari Karahi Full", category = "Mutton", price = 3900.0),
            MenuItem(name = "Mutton Shinwari Karahi Half", category = "Mutton", price = 2050.0),
            MenuItem(name = "Mutton Shinwari Karahi Full", category = "Mutton", price = 4000.0),
            MenuItem(name = "Mutton Sulemani Karahi Half", category = "Mutton", price = 2050.0),
            MenuItem(name = "Mutton Sulemani Karahi Full", category = "Mutton", price = 4000.0),
            MenuItem(name = "Mutton Afghani Karahi Half", category = "Mutton", price = 2050.0),
            MenuItem(name = "Mutton Afghani Karahi Full", category = "Mutton", price = 4000.0),
            MenuItem(name = "Mutton Mardan Karahi Half", category = "Mutton", price = 2050.0),
            MenuItem(name = "Mutton Mardan Karahi Full", category = "Mutton", price = 4000.0),

            // Desi Murgh Karahi
            MenuItem(name = "Desi Murgh Karahi Half", category = "Desi Murgh Karahi", price = 2000.0),
            MenuItem(name = "Desi Murgh Karahi Full", category = "Desi Murgh Karahi", price = 3800.0),
            MenuItem(name = "Desi Murgh White Karahi Half", category = "Desi Murgh Karahi", price = 2050.0),
            MenuItem(name = "Desi Murgh White Karahi Full", category = "Desi Murgh Karahi", price = 3900.0),
            MenuItem(name = "Desi Murgh Desi Ghee Main Tayar Full", category = "Desi Murgh Karahi", price = 4200.0),

            // Desi Tarka Special (Per Kg)
            MenuItem(name = "Masala Ran Mutton (Per Kg)", category = "Desi Tarka Special", price = 4500.0),
            MenuItem(name = "Namkeen Roast (Per Kg)", category = "Desi Tarka Special", price = 1400.0),
            MenuItem(name = "Singa Puri Rice", category = "Desi Tarka Special", price = 1200.0),
            MenuItem(name = "Mutton Namkeen Tikka (Per Kg)", category = "Desi Tarka Special", price = 4200.0),
            MenuItem(name = "Mutton Champ (Per Kg)", category = "Desi Tarka Special", price = 4200.0),
            MenuItem(name = "Ranja Gosht (Per Kg)", category = "Desi Tarka Special", price = 4000.0),

            // Daalein (Dal)
            MenuItem(name = "Dal Chana", category = "Daalein", price = 250.0),
            MenuItem(name = "Dal Mash", category = "Daalein", price = 350.0),
            MenuItem(name = "Dal Makhni Chana", category = "Daalein", price = 350.0),
            MenuItem(name = "Dal Mash Makhni", category = "Daalein", price = 500.0),
            MenuItem(name = "Mix Sabzi", category = "Daalein", price = 200.0),

            // Chawal (Rice)
            MenuItem(name = "Chicken Biryani Half", category = "Chawal", price = 250.0),
            MenuItem(name = "Chicken Biryani Full", category = "Chawal", price = 500.0),
            MenuItem(name = "Beef Boneless Biryani Half", category = "Chawal", price = 600.0),
            MenuItem(name = "Beef Boneless Biryani Full", category = "Chawal", price = 1200.0),
            MenuItem(name = "Sada Biryani Half", category = "Chawal", price = 500.0),
            MenuItem(name = "Sada Biryani Full", category = "Chawal", price = 1000.0),
            MenuItem(name = "Afghani Rice Half", category = "Chawal", price = 800.0),
            MenuItem(name = "Afghani Rice Full", category = "Chawal", price = 1600.0),
            MenuItem(name = "Kabuli Rice Half", category = "Chawal", price = 650.0),
            MenuItem(name = "Kabuli Rice Full", category = "Chawal", price = 1200.0),
            MenuItem(name = "Beef Bannu Rice Half", category = "Chawal", price = 550.0),
            MenuItem(name = "Beef Bannu Rice Full", category = "Chawal", price = 1200.0),

            // Chinese Rice
            MenuItem(name = "Fried Rice Half", category = "Chinese Rice", price = 400.0),
            MenuItem(name = "Fried Rice Full", category = "Chinese Rice", price = 800.0),
            MenuItem(name = "Egg Fried Rice Half", category = "Chinese Rice", price = 450.0),
            MenuItem(name = "Egg Fried Rice Full", category = "Chinese Rice", price = 850.0),
            MenuItem(name = "Shashlik with Rice", category = "Chinese Rice", price = 1350.0),
            MenuItem(name = "Manchurian with Rice", category = "Chinese Rice", price = 1300.0),
            MenuItem(name = "Vegetable Fried Rice", category = "Chinese Rice", price = 700.0),

            // Fish
            MenuItem(name = "Fried Fish (Per Kg)", category = "Fish", price = 1800.0),
            MenuItem(name = "Grill Fish (Per Kg)", category = "Fish", price = 2000.0),
            MenuItem(name = "Fish Tikka (Per Kg)", category = "Fish", price = 2000.0),

            // Raita & Salad
            MenuItem(name = "Raita", category = "Raita & Salad", price = 70.0),
            MenuItem(name = "Kachumar Raita", category = "Raita & Salad", price = 100.0),
            MenuItem(name = "Salad", category = "Raita & Salad", price = 70.0),
            MenuItem(name = "Kachumar Salad", category = "Raita & Salad", price = 100.0),

            // Mashrobat (Drinks)
            MenuItem(name = "Chai Special", category = "Mashrobat", price = 100.0),
            MenuItem(name = "Chai", category = "Mashrobat", price = 70.0),

            // Burgers
            MenuItem(name = "Anda Shami Burger", category = "Burgers", price = 170.0),
            MenuItem(name = "Double Anda Shami Burger", category = "Burgers", price = 200.0),
            MenuItem(name = "Zinger Burger", category = "Burgers", price = 350.0),
            MenuItem(name = "Chicken Burger", category = "Burgers", price = 300.0),
            MenuItem(name = "Special Desi Tarka Burger", category = "Burgers", price = 350.0),
            MenuItem(name = "Chicken Kabab Burger", category = "Burgers", price = 250.0),
            MenuItem(name = "Chicken Tikka Burger", category = "Burgers", price = 300.0),
            MenuItem(name = "Tikka Cheese Burger", category = "Burgers", price = 400.0),

            // Shawarma
            MenuItem(name = "Chicken Shawarma", category = "Shawarma", price = 150.0),
            MenuItem(name = "Kabab Shawarma", category = "Shawarma", price = 250.0),
            MenuItem(name = "Zinger Shawarma", category = "Shawarma", price = 250.0),

            // Platters & Deals
            MenuItem(name = "Platter 4 Persons (Special Desi Tarka Chicken)", category = "Deals", price = 3400.0),
            MenuItem(name = "Platter 4 Persons (Half Desi Tarka Chicken)", category = "Deals", price = 2600.0),
            MenuItem(name = "Platter 4 Persons (Half Mutton Desi Tarka)", category = "Deals", price = 3850.0),
            MenuItem(name = "Platter 2 Persons", category = "Deals", price = 1300.0)
        )
        dao.insertMenuItems(defaultItems)
    }
}
