
package warehouse;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Multiple warehouses map
        Map<String, Warehouse> warehouses = new HashMap<>();
        warehouses.put("Khargone", new Warehouse());
        warehouses.put("Indore", new Warehouse());
        warehouses.put("Pune", new Warehouse());

        // Register observer for each warehouse
        for (Warehouse w : warehouses.values()) {
            w.registerObserver(new AlertService());
        }

        boolean running = true;

        while (running) {
            System.out.println("\n=====================================");
            System.out.println("      🏭 Multi-Warehouse System");
            System.out.println("=====================================");
            System.out.println("Select a Warehouse:");
            System.out.println("1. Kharagpur");
            System.out.println("2. Indore");
            System.out.println("3. Pune");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            String whChoice = sc.nextLine().trim();

            Warehouse selectedWarehouse = null;
            String selectedName = "";

            switch (whChoice) {
                case "1":
                    selectedWarehouse = warehouses.get("Khargone");
                    selectedName = "Khargone";
                    break;
                case "2":
                    selectedWarehouse = warehouses.get("Indore");
                    selectedName = "Indore";
                    break;
                case "3":
                    selectedWarehouse = warehouses.get("Pune");
                    selectedName = "Pune";
                    break;
                case "4":
                    running = false;
                    continue;
                default:
                    System.out.println("⚠ Invalid choice! Try again.");
                    continue;
            }

            // File path based on warehouse name
            String filePath = "inventory_" + selectedName.toLowerCase() + ".csv";

            // Load data if file exists
            selectedWarehouse.loadFromFile(filePath);

            System.out.println("\n🧭 You are now managing: " + selectedName + " Warehouse");

            // Ask for product addition
            System.out.print("Do you want to add new products in " + selectedName + "? (yes/no): ");
            String addChoice = sc.nextLine().trim().toLowerCase();

            if (addChoice.equals("yes")) {
                boolean addMore = true;
                while (addMore) {
                    try {
                        System.out.print("\nEnter Product ID: ");
                        int id = Integer.parseInt(sc.nextLine());

                        System.out.print("Enter Product Name: ");
                        String name = sc.nextLine();

                        System.out.print("Enter Quantity: ");
                        int qty = Integer.parseInt(sc.nextLine());

                        System.out.print("Enter Reorder Threshold: ");
                        int threshold = Integer.parseInt(sc.nextLine());

                        selectedWarehouse.addProduct(new Product(id, name, qty, threshold));
                        System.out.println("✅ Product added successfully!");

                    } catch (NumberFormatException e) {
                        System.out.println("⚠ Invalid number format!");
                    } catch (IllegalArgumentException e) {
                        System.out.println("⚠ " + e.getMessage());
                    }

                    System.out.print("\nAdd another product? (yes/no): ");
                    addMore = sc.nextLine().trim().equalsIgnoreCase("yes");
                }
            }

            // Warehouse operations menu
            boolean manageWarehouse = true;
            while (manageWarehouse) {
                System.out.println("\n===============================");
                System.out.println("Warehouse: " + selectedName);
                System.out.println("1. Receive Shipment");
                System.out.println("2. Fulfill Order");
                System.out.println("3. Print Inventory");
                System.out.println("4. Switch Warehouse");
                System.out.println("5. Save & Exit");
                System.out.println("===============================");
                System.out.print("Enter your choice: ");

                int option = 0;
                try {
                    option = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("⚠ Enter a valid number!");
                    continue;
                }

                switch (option) {
                    case 1:
                        try {
                            System.out.print("Enter Product ID: ");
                            int rId = Integer.parseInt(sc.nextLine());
                            System.out.print("Enter Quantity to Receive: ");
                            int rQty = Integer.parseInt(sc.nextLine());
                            selectedWarehouse.receiveShipment(rId, rQty);
                        } catch (NumberFormatException e) {
                            System.out.println("⚠ Invalid input!");
                        }
                        break;

                    case 2:
                        try {
                            System.out.print("Enter Product ID: ");
                            int fId = Integer.parseInt(sc.nextLine());
                            System.out.print("Enter Quantity to Fulfill: ");
                            int fQty = Integer.parseInt(sc.nextLine());
                            selectedWarehouse.fulfillOrder(fId, fQty);
                        } catch (NumberFormatException e) {
                            System.out.println("⚠ Invalid input!");
                        }
                        break;

                    case 3:
                        selectedWarehouse.printInventory();
                        break;

                    case 4:
                        // Save before switching
                        selectedWarehouse.saveToFile(filePath);
                        System.out.println("💾 Saved " + selectedName + " warehouse data!");
                        manageWarehouse = false; // break to select another warehouse
                        break;

                    case 5:
                        // Save and exit application
                        selectedWarehouse.saveToFile(filePath);
                        System.out.println("💾 Data saved for " + selectedName + " warehouse.");
                        running = false;
                        manageWarehouse = false;
                        break;

                    default:
                        System.out.println("⚠ Invalid option! Please choose 1–5.");
                }
            }
        }

        System.out.println("\n🎯 All warehouses saved successfully! Exiting system...");
        sc.close();
    }
}





