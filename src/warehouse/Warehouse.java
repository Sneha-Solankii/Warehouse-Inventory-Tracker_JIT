package warehouse;

//Warehouse.java
import java.util.HashMap;
import java.util.Map;

public class Warehouse {
 private HashMap<String, Product> inventory;

 public Warehouse() {
     inventory = new HashMap<>();
 }

 // Add product to warehouse
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
 }
 
 // Display all products
 public void displayProducts() {
     System.out.println("\n=== Current Warehouse Inventory ===");
     for (Product product : inventory.values()) {
         System.out.println(product);
     }
   }
}
