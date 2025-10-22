package warehouse;

import java.util.*;

// ✅ Final Version: Covers all features till 24 Oct
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Warehouse warehouse = new Warehouse();
        ArrayList<String> productIds = new ArrayList<>();

        // 21 Oct → Add Observer (Stock Alert)
        StockObserver alertService = new AlertService();
        warehouse.addObserver(alertService);

        System.out.println("=== Add Products to Warehouse ===");
        char choice;

        // 17–18 Oct → Add Products
        do {
            System.out.print("\nEnter Product ID: ");
            String id = sc.nextLine();

            System.out.print("Enter Product Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Initial Quantity: ");
            int quantity = sc.nextInt();

            System.out.print("Enter Reorder Threshold: ");
            int threshold = sc.nextInt();
            sc.nextLine(); // consume newline

            Product product = new Product(id, name, quantity, threshold);
            warehouse.addProduct(product);
            productIds.add(id); // ✅ store user-added ID

            System.out.print("\nAdd another product? (y/n): ");
            choice = sc.nextLine().charAt(0);

        } while (choice == 'y' || choice == 'Y');

        warehouse.displayProducts();

        // 19 Oct → Receive Shipment
        System.out.println("\n=== Receive Shipment ===");
        char shipmentChoice;
        do {
            System.out.print("\nEnter Product ID to receive: ");
            String id = sc.nextLine();

            System.out.print("Enter quantity received: ");
            int quantityReceived = sc.nextInt();
            sc.nextLine();

            warehouse.receiveShipment(id, quantityReceived);

            System.out.print("\nReceive another shipment? (y/n): ");
            shipmentChoice = sc.nextLine().charAt(0);
        } while (shipmentChoice == 'y' || shipmentChoice == 'Y');

        // 20 Oct → Fulfill Order
        System.out.println("\n=== Fulfill Orders ===");
        char orderChoice;
        do {
            System.out.print("\nEnter Product ID to fulfill: ");
            String id = sc.nextLine();

            System.out.print("Enter quantity to order: ");
            int quantityOrdered = sc.nextInt();
            sc.nextLine();

            warehouse.fulfillOrder(id, quantityOrdered);

            System.out.print("\nFulfill another order? (y/n): ");
            orderChoice = sc.nextLine().charAt(0);
        } while (orderChoice == 'y' || orderChoice == 'Y');

        warehouse.displayProducts();

        // 23 Oct → Multi-threading Simulation
        System.out.println("\n=== Starting Multi-Threading Simulation ===");
        if (productIds.size() >= 2) {
            String id1 = productIds.get(0);
            String id2 = productIds.get(1);

            Thread t1 = new Thread(new WarehouseTestSimulation(warehouse, id1, "RECEIVE", 5));
            Thread t2 = new Thread(new WarehouseTestSimulation(warehouse, id1, "FULFILL", 3));
            Thread t3 = new Thread(new WarehouseTestSimulation(warehouse, id2, "RECEIVE", 2));
            Thread t4 = new Thread(new WarehouseTestSimulation(warehouse, id2, "FULFILL", 1));

            t1.start();
            t2.start();
            t3.start();
            t4.start();

            try {
                t1.join();
                t2.join();
                t3.join();
                t4.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("⚠ Need at least 2 products for simulation.");
        }

        // 23 Oct → Save inventory to file
        warehouse.saveInventoryToFile("final_inventory.txt");

        // 24 Oct → Final Demo Complete
        System.out.println("\n🎯 Final Demo Complete — All Features Working Perfectly!");

        sc.close();
    }
}



