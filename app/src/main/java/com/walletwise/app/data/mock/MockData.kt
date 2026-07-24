package com.walletwise.app.data.mock

import com.walletwise.app.core.model.*

object MockData {
    val sampleUser = UserProfile(
        id = "usr_101",
        name = "Alex Vance",
        email = "alex.vance@walletwise.ai",
        isPremium = true,
        currencySymbol = "₹",
        themeMode = "Light",
        biometricEnabled = true
    )

    val sampleExpenses = listOf(
        Expense("exp_1", "Organic Supermarket", "Whole Foods", 1450.0, ExpenseCategory.FOOD, "Today", "04:30 PM", "Google Pay", "Fresh fruits, organic milk, oats"),
        Expense("exp_2", "Apple Store Subscription", "Apple Inc", 499.0, ExpenseCategory.BILLS, "Yesterday", "10:15 AM", "Credit Card", "iCloud+ 2TB storage", isRecurring = true),
        Expense("exp_3", "Uber Ride Central", "Uber Tech", 320.0, ExpenseCategory.TRANSPORT, "22 Jul", "08:45 PM", "UPI", "Commute back from tech hub"),
        Expense("exp_4", "Nike Air Zoom Sneakers", "Nike Store", 4890.0, ExpenseCategory.SHOPPING, "20 Jul", "02:10 PM", "Credit Card", "Running shoes for marathon prep"),
        Expense("exp_5", "Pharmacy & Vitamins", "Apollo Med", 850.0, ExpenseCategory.HEALTH, "18 Jul", "11:20 AM", "Debit Card", "Multivitamins & protein bars"),
        Expense("exp_6", "IMAX Movie Night", "PVR Cinemas", 1200.0, ExpenseCategory.ENTERTAINMENT, "15 Jul", "09:00 PM", "Google Pay", "Tickets & caramel popcorn"),
        Expense("exp_7", "Flight to Goa", "IndiGo Air", 6800.0, ExpenseCategory.TRAVEL, "14 Jul", "07:30 AM", "Credit Card", "Weekend getaway flight booking"),
        Expense("exp_8", "Swiggy Gourmet Order", "Truffles Bistro", 890.0, ExpenseCategory.FOOD, "13 Jul", "01:15 PM", "UPI", "Team lunch pasta & burgers"),
        Expense("exp_9", "Electricity Bill", "BESCOM Power", 2450.0, ExpenseCategory.BILLS, "12 Jul", "10:00 AM", "Auto Debit", "Summer AC consumption", isRecurring = true),
        Expense("exp_10", "Uniqlo Linen Shirt", "Uniqlo", 2490.0, ExpenseCategory.SHOPPING, "11 Jul", "05:40 PM", "Credit Card", "Casual workwear"),
        Expense("exp_11", "Fuel Refill", "Shell Petrol", 1850.0, ExpenseCategory.TRANSPORT, "10 Jul", "08:00 AM", "UPI", "Full tank V-Power petrol"),
        Expense("exp_12", "Dental Checkup & Cleaning", "Clove Dental", 1500.0, ExpenseCategory.HEALTH, "09 Jul", "03:30 PM", "Debit Card", "Routine preventive care"),
        Expense("exp_13", "Netflix Premium 4K", "Netflix", 649.0, ExpenseCategory.BILLS, "08 Jul", "12:00 AM", "Credit Card", "Monthly family subscription", isRecurring = true),
        Expense("exp_14", "BookMyShow Theater Play", "Rangashankara", 600.0, ExpenseCategory.ENTERTAINMENT, "07 Jul", "06:30 PM", "Google Pay", "Live drama performance"),
        Expense("exp_15", "Resort Stay Deposit", "Taj Resorts", 8500.0, ExpenseCategory.TRAVEL, "06 Jul", "11:00 AM", "Credit Card", "Advance room reservation"),
        Expense("exp_16", "Artisan Coffee Beans", "Third Wave", 750.0, ExpenseCategory.FOOD, "05 Jul", "09:15 AM", "UPI", "Single origin Arabica roast"),
        Expense("exp_17", "Amazon Office Chair", "Featherlite", 7990.0, ExpenseCategory.SHOPPING, "04 Jul", "04:20 PM", "Credit Card", "Ergonomic mesh chair for home office"),
        Expense("exp_18", "Wi-Fi Broadband Bill", "Airtel Xstream", 1179.0, ExpenseCategory.BILLS, "03 Jul", "09:00 AM", "UPI", "300 Mbps fiber plan", isRecurring = true),
        Expense("exp_19", "Metro Smart Card Recharge", "Namma Metro", 500.0, ExpenseCategory.TRANSPORT, "02 Jul", "08:30 AM", "UPI", "Daily transit top-up"),
        Expense("exp_20", "Gym Membership", "Cult.fit Pass", 3499.0, ExpenseCategory.HEALTH, "01 Jul", "07:00 AM", "Credit Card", "Monthly pass renewal"),
        Expense("exp_21", "Zomato Dinner Party", "Empire Restaurant", 1650.0, ExpenseCategory.FOOD, "30 Jun", "09:30 PM", "UPI", "Biryani & kebabs for friends"),
        Expense("exp_22", "Water Utility Bill", "BWSSB Board", 420.0, ExpenseCategory.BILLS, "29 Jun", "11:15 AM", "UPI", "Monthly water utility"),
        Expense("exp_23", "Zara Denim Jacket", "Zara", 3990.0, ExpenseCategory.SHOPPING, "28 Jun", "06:10 PM", "Credit Card", "Outerwear for travel"),
        Expense("exp_24", "Cab to Airport", "MakeMyTrip Cabs", 1100.0, ExpenseCategory.TRANSPORT, "27 Jun", "04:00 AM", "UPI", "Early morning drop"),
        Expense("exp_25", "Spotify Duo Plan", "Spotify", 149.0, ExpenseCategory.BILLS, "26 Jun", "12:00 AM", "Auto Debit", "Music streaming subscription", isRecurring = true),
        Expense("exp_26", "Gaming Console Accessories", "Sony Center", 2200.0, ExpenseCategory.ENTERTAINMENT, "25 Jun", "05:00 PM", "Credit Card", "DualSense controller charging station"),
        Expense("exp_27", "Weekend Villa Booking", "Airbnb", 5400.0, ExpenseCategory.TRAVEL, "24 Jun", "02:30 PM", "Credit Card", "Coorg homestay booking"),
        Expense("exp_28", "Starbucks Cold Brew", "Starbucks", 410.0, ExpenseCategory.FOOD, "23 Jun", "03:45 PM", "Google Pay", "Afternoon work break drink"),
        Expense("exp_29", "House Cleaning Supplies", "BigBasket", 1280.0, ExpenseCategory.OTHER, "22 Jun", "10:30 AM", "UPI", "Detergents, mops, surface cleaners"),
        Expense("exp_30", "Blood Test & Health Check", "Metropolis Lab", 2100.0, ExpenseCategory.HEALTH, "21 Jun", "08:15 AM", "Debit Card", "Annual blood panel screening"),
        Expense("exp_31", "Concert Ticket", "BookMyShow Live", 2500.0, ExpenseCategory.ENTERTAINMENT, "20 Jun", "07:00 PM", "Credit Card", "Music festival entry ticket"),
        Expense("exp_32", "LPG Cylinder Refill", "Indane Gas", 910.0, ExpenseCategory.BILLS, "19 Jun", "01:00 PM", "UPI", "Cooking gas cylinder refill"),
        Expense("exp_33", "Tailored Suit Fitting", "Raymond Custom", 8900.0, ExpenseCategory.SHOPPING, "18 Jun", "04:45 PM", "Credit Card", "Formal blazer & trousers"),
        Expense("exp_34", "Fastag Toll Topup", "ICICI Fastag", 1000.0, ExpenseCategory.TRANSPORT, "17 Jun", "09:20 AM", "UPI", "Highway toll account balance"),
        Expense("exp_35", "Pet Food & Grooming", "Heads Up For Tails", 1750.0, ExpenseCategory.OTHER, "16 Jun", "05:15 PM", "Google Pay", "Premium dog kibble & shampoo"),
        Expense("exp_36", "Café Brunch", "Glen's Bakehouse", 980.0, ExpenseCategory.FOOD, "15 Jun", "11:45 AM", "UPI", "Sunday pancakes & roasted coffee"),
        Expense("exp_37", "Mobile Postpaid Bill", "Jio Telecom", 699.0, ExpenseCategory.BILLS, "14 Jun", "08:30 AM", "Auto Debit", "5G family postpaid plan", isRecurring = true),
        Expense("exp_38", "Sunscreen & Skincare", "Nykaa Man", 1450.0, ExpenseCategory.HEALTH, "13 Jun", "02:10 PM", "Credit Card", "Daily SPF 50 & face moisturizer"),
        Expense("exp_39", "Bowling & Arcade", "Smaaash Arena", 1300.0, ExpenseCategory.ENTERTAINMENT, "12 Jun", "06:00 PM", "Google Pay", "VR games & 2 bowling rounds"),
        Expense("exp_40", "Train Ticket", "IRCTC Rail", 1850.0, ExpenseCategory.TRAVEL, "11 Jun", "10:45 AM", "UPI", "2nd AC sleeper ticket"),
        Expense("exp_41", "Mechanical Keyboard", "Keychron India", 6490.0, ExpenseCategory.SHOPPING, "10 Jun", "03:20 PM", "Credit Card", "Wireless RGB hot-swappable keyboard"),
        Expense("exp_42", "Tavern Craft Beer", "Toit Brewpub", 1950.0, ExpenseCategory.FOOD, "09 Jun", "09:00 PM", "UPI", "Craft beers & wood-fired pizza"),
        Expense("exp_43", "Car Wash & Detailing", "3M Car Care", 1800.0, ExpenseCategory.TRANSPORT, "08 Jun", "11:00 AM", "Debit Card", "Foam wash & interior vacuuming"),
        Expense("exp_44", "Piped Natural Gas Bill", "GAIL Gas", 380.0, ExpenseCategory.BILLS, "07 Jun", "09:30 AM", "UPI", "Bimonthly piped gas supply"),
        Expense("exp_45", "Physiotherapy Session", "PhysioActive", 1200.0, ExpenseCategory.HEALTH, "06 Jun", "05:30 PM", "UPI", "Posture correction session"),
        Expense("exp_46", "Home Decoration Plants", "Nursery Live", 1400.0, ExpenseCategory.OTHER, "05 Jun", "04:00 PM", "Google Pay", "Indoor Monstera & Snake plants"),
        Expense("exp_47", "Kindle Unlimited Annual", "Amazon Books", 1699.0, ExpenseCategory.ENTERTAINMENT, "04 Jun", "10:00 AM", "Credit Card", "E-book subscription renewal"),
        Expense("exp_48", "Cab to Client Office", "Ola Cabs", 450.0, ExpenseCategory.TRANSPORT, "03 Jun", "01:30 PM", "UPI", "Business travel fare"),
        Expense("exp_49", "Gourmet Bakery Cake", "Magnolia Bakery", 1150.0, ExpenseCategory.FOOD, "02 Jun", "07:15 PM", "UPI", "Banana pudding & cupcakes"),
        Expense("exp_50", "Courier & Shipping", "Blue Dart", 350.0, ExpenseCategory.OTHER, "01 Jun", "11:00 AM", "UPI", "Express document dispatch")
    )

    val sampleBudgets = listOf(
        Budget("bgt_1", ExpenseCategory.FOOD, 15000.0, 9450.0),
        Budget("bgt_2", ExpenseCategory.SHOPPING, 10000.0, 8900.0),
        Budget("bgt_3", ExpenseCategory.BILLS, 12000.0, 11499.0),
        Budget("bgt_4", ExpenseCategory.TRANSPORT, 6000.0, 3200.0),
        Budget("bgt_5", ExpenseCategory.HEALTH, 5000.0, 1850.0),
        Budget("bgt_6", ExpenseCategory.ENTERTAINMENT, 4000.0, 3100.0),
        Budget("bgt_7", ExpenseCategory.TRAVEL, 15000.0, 13750.0),
        Budget("bgt_8", ExpenseCategory.OTHER, 3000.0, 2080.0)
    )

    val sampleNotifications = listOf(
        NotificationItem("n_1", "Shopping Budget Cap Alert", "Shopping category is at 89% capacity.", "10 mins ago", "Today", false, "ALERT"),
        NotificationItem("n_2", "AI Spending Forecast Updated", "New 30-day predicted spending breakdown generated.", "2 hours ago", "Today", false, "AI"),
        NotificationItem("n_3", "Salary Credited", "Salary ₹85,000 received in HDFC Account ****4092.", "Yesterday", "Yesterday", true, "SYSTEM"),
        NotificationItem("n_4", "Large Transaction Detected", "₹8,500 spent on Taj Resorts Deposit.", "3 days ago", "Earlier", true, "ALERT"),
        NotificationItem("n_5", "Monthly Report Ready", "July financial statement is compiled & ready for download.", "5 days ago", "Earlier", true, "SYSTEM")
    )
}
