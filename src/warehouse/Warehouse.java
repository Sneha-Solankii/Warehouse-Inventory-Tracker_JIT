package warehouse;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Warehouse {
    private HashMap<String, Product> inventory;
    private List<StockObserver> observers;  // 22 Oct → observer list added

    public Warehouse() {
        inventory = new HashMap<>();
        observers = new ArrayList<>();
    }

    // 22 Oct → Register observer
    public void addObserver(StockObserver observer) {
        observers.add(observer);
    }

    // 22 Oct → Notify observers if stock is low
    private void notifyLowStock(Product product) {
        if (product.getQuantity() <= product.getReorderThreshold()) {
            for (StockObserver observer : observers) {
                observer.onLowStock(product);
            }
        }
    }

    // Add product to warehouse (17–18 Oct)
    public void addProduct(Product product) {
        if (inventory.containsKey(product.getId())) {
            System.out.println("⚠ Product with ID " + product.getId() + " already exists. Updating quantity.");
            Product existingProduct = inventory.get(product.getId());
            existingProduct.setQuantity(existingProduct.getQuantity() + product.getQuantity());
        } else {
            inventory.put(product.getId(), product);
            System.out.println("✅ Product added to warehouse: " + product.getName());
        }
        notifyLowStock(product); // 22 Oct → trigger alert if low
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

        notifyLowStock(product); // 22 Oct → check after update
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

        notifyLowStock(product); // 22 Oct → check after deduction
    }

    // Display all products
    public void displayProducts() {
        System.out.println("\n=== Current Warehouse Inventory ===");
        for (Product product : inventory.values()) {
            System.out.println(product);
        }
    }
}

