package warehouse;

import java.util.HashMap;

public class Warehouse {
    private HashMap<String, Product> inventory;
    private StockObserver stockObserver; // added observer

    public Warehouse() {
        inventory = new HashMap<>();
    }

    // Register observer (AlertService)
    public void setStockObserver(StockObserver observer) {
        this.stockObserver = observer;
    }

    // Add product to warehouse (18 Oct)
    public void addProduct(Product product) {
        if (inventory.containsKey(product.getId())) {
            System.out.println("⚠ Product with ID " + product.getId() + " already exists. Updating quantity.");
            Product existingProduct = inventory.get(product.getId());
            existingProduct.setQuantity(existingProduct.getQuantity() + product.getQuantity());
        } else {
            inventory.put(product.getId(), product);
            System.out.println("✅ Product added to warehouse: " + product.getName());
        }
    }

    // Receive shipment (19 Oct)
    public void receiveShipment(String productId, int quantityReceived) {
        Product product = inventory.get(productId);

        if (product == null) {
            System.out.println("❌ Invalid Product ID: " + productId);
            return;
        }

        if (quantityReceived <= 0) {
            System.out.println("⚠ Quantity must be greater than zero.");
            return;
        }

        int newQuantity = product.getQuantity() + quantityReceived;
        product.setQuantity(newQuantity);
        System.out.println("📦 Shipment received for " + product.getName() +
                           ". New Quantity: " + newQuantity);

        checkStockLevel(product);
    }

    // Fulfill order (20 Oct)
    public void fulfillOrder(String productId, int quantityOrdered) {
        Product product = inventory.get(productId);

        if (product == null) {
            System.out.println("❌ Invalid Product ID: " + productId);
            return;
        }

        if (quantityOrdered <= 0) {
            System.out.println("⚠ Order quantity must be greater than zero.");
            return;
        }

        int currentQty = product.getQuantity();
        if (currentQty < quantityOrdered) {
            System.out.println("⚠ Insufficient stock for " + product.getName() +
                               ". Available: " + currentQty + ", Requested: " + quantityOrdered);
        } else {
            product.setQuantity(currentQty - quantityOrdered);
            System.out.println("✅ Order fulfilled for " + product.getName() +
                               ". Remaining Quantity: " + product.getQuantity());
        }

        checkStockLevel(product);
    }

    // Check and notify low stock
    private void checkStockLevel(Product product) {
        if (stockObserver != null && product.getQuantity() <= product.getReorderThreshold()) {
            stockObserver.onLowStock(product);
        }
    }

    // Display all products
    public void displayProducts() {
        System.out.println("\n=== Current Warehouse Inventory ===");
        for (Product product : inventory.values()) {
            System.out.println(product);
        }
    }
}

