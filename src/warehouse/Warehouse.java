package warehouse;

import java.util.*;
import java.io.*;

// 17–23 Oct combined logic
public class Warehouse {
    private HashMap<String, Product> inventory;
    private List<StockObserver> observers;

    public Warehouse() {
        inventory = new HashMap<>();
        observers = new ArrayList<>();
    }

    // 22 Oct → Add observer
    public void addObserver(StockObserver observer) {
        observers.add(observer);
    }

    // 22 Oct → Notify observers
    private void notifyLowStock(Product product) {
        if (product.getQuantity() <= product.getReorderThreshold()) {
            for (StockObserver observer : observers) {
                observer.onLowStock(product);
            }
        }
    }

    // 18 Oct → Add product
    public synchronized void addProduct(Product product) {
        if (inventory.containsKey(product.getId())) {
            System.out.println("⚠ Product with ID " + product.getId() + " already exists. Updating quantity.");
            Product existing = inventory.get(product.getId());
            existing.setQuantity(existing.getQuantity() + product.getQuantity());
        } else {
            inventory.put(product.getId(), product);
            System.out.println("✅ Product added: " + product.getName());
        }
        notifyLowStock(product);
    }

    // 19 Oct → Receive shipment
    public synchronized void receiveShipment(String productId, int quantityReceived) {
        Product product = inventory.get(productId);
        if (product == null) {
            System.out.println("❌ Invalid Product ID: " + productId);
            return;
        }

        if (quantityReceived <= 0) {
            System.out.println("⚠ Quantity must be greater than zero.");
            return;
        }

        int newQty = product.getQuantity() + quantityReceived;
        product.setQuantity(newQty);
        System.out.println("📦 Shipment received for " + product.getName() + ". New Qty: " + newQty);

        notifyLowStock(product);
    }

    // 20 Oct → Fulfill order
    public synchronized void fulfillOrder(String productId, int quantityOrdered) {
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
                               ". Remaining Qty: " + product.getQuantity());
        }

        notifyLowStock(product);
    }

    // 23 Oct → Save inventory to file
    public void saveInventoryToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("=== Warehouse Inventory Report ===");
            for (Product product : inventory.values()) {
                writer.println(product.toString());
            }
            System.out.println("💾 Inventory saved successfully to " + filename);
        } catch (IOException e) {
            System.out.println("❌ Error saving file: " + e.getMessage());
        }
    }

    // Display all products
    public void displayProducts() {
        System.out.println("\n=== Current Warehouse Inventory ===");
        for (Product p : inventory.values()) {
            System.out.println(p);
        }
    }
}

