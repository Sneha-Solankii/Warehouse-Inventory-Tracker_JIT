## Warehouse Inventory Tracker (JIT)
# Project Objective:
    The Warehouse Inventory Tracker is designed to efficiently manage stock levels in real time. It follows the Just-In-Time (JIT) inventory management principle — ensuring that inventory is replenished only when needed, minimizing excess stock while preventing stockouts.
    The system keeps track of all products, alerts users when stock levels are low, and uses an Observer Pattern to automatically update dependent components whenever changes occur in inventory.

# Key Features
   - Add, Update, Delete Products — Manage inventory easily with CRUD operations.
   - Real-Time Stock Tracking — Monitor stock levels efficiently.
   - Low Stock Alerts — Get notified automatically when any product’s quantity drops below the threshold.
   - Observer Pattern — Implements an event-driven approach for real-time updates.
   - Modular Structure — Easy to extend and maintain as the system grows.

# Technologies Used
    Backend	Java (Eclipse IDE)
    Design Pattern	Observer Pattern / Event-Driven Architecture
    Storage	Local File-based Storage (CSV/Text) or MySQL Integration

# Project Structure
    WarehouseInventoryTracker/
    │
    ├── src/
    │   ├── Product.java          # Defines product attributes (ID, name, quantity, threshold)
    │   ├── Warehouse.java        # Core inventory management logic (add, remove, update, notify)
    │   ├── StockObserver.java    # Interface for observers to monitor stock updates
    │   ├── AlertService.java     # Sends notifications when stock is low
    │   └── Main.java             # Entry point of the application
    │
    ├── data/
    │   └── inventory.txt         # Optional: file-based storage for product data
    │
    └── README.md

# How It Works

    Warehouse.java maintains the list of products and their stock levels.
    StockObserver.java defines a contract for classes that need updates on stock changes.
    AlertService.java implements StockObserver and gets notified when a product’s quantity falls below a defined threshold.
    Main.java creates products, adds them to the warehouse, and simulates stock updates in real-time.

# Example Flow
    Product p1 = new Product("P001", "Laptop", 10, 3);
    Product p2 = new Product("P002", "Smartphone", 5, 2);
    
    Warehouse warehouse = new Warehouse();
    AlertService alertService = new AlertService();
    
    warehouse.addObserver(alertService);
    
    warehouse.addProduct(p1);
    warehouse.addProduct(p2);

    // Simulate stock usage
    warehouse.updateStock("P001", 2);
    warehouse.updateStock("P002", 4);


    Output Example:
    
    Product added: Laptop (Stock: 10)
    Product added: Smartphone (Stock: 5)
    Stock updated for Laptop. Remaining: 8
    Stock updated for Smartphone. Remaining: 1
    ⚠️ ALERT: Stock low for Smartphone (Remaining: 1)

# Setup Instructions

    Open Eclipse IDE.
    Create a new Java Project → WarehouseInventoryTracker.
    Add all .java files to the src folder.
    (Optional) Add a data/inventory.txt file for file-based storage.
    Run the Main.java file to start the application.

# Future Enhancements
    Integration with MySQL for persistent data storage.
    Develop a web-based dashboard for real-time inventory monitoring.

📊 Generate reports for stock trends and reorder frequency.

🧠 Use machine learning to predict restocking needs.
