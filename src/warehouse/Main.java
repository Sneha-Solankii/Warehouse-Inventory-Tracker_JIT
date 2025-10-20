package warehouse;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Warehouse warehouse = new Warehouse();

        // Register the alert service as observer
        AlertService alertService = new AlertService();
        warehouse.setStockObserver(alertService);

        System.out.println("=== Add Products to Warehouse ===");
        char choice;

        // Add products (17–18 Oct)
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

            System.out.print("\nAdd another product? (y/n): ");
            choice = sc.nextLine().charAt(0);

        } while (choice == 'y' || choice == 'Y');

        warehouse.displayProducts();

        // Receive shipments (19 Oct)
        System.out.println("\n=== Receive Shipment ===");
        char shipmentChoice;
        do {
            System.out.print("\nEnter Product ID to update: ");
            String id = sc.nextLine();

            System.out.print("Enter quantity received: ");
            int quantityReceived = sc.nextInt();
            sc.nextLine(); // consume newline

            warehouse.receiveShipment(id, quantityReceived);

            System.out.print("\nAdd another shipment? (y/n): ");
            shipmentChoice = sc.nextLine().charAt(0);

        } while (shipmentChoice == 'y' || shipmentChoice == 'Y');

        // Fulfill orders (20 Oct)
        System.out.println("\n=== Fulfill Orders ===");
        char orderChoice;
        do {
            System.out.print("\nEnter Product ID to fulfill: ");
            String id = sc.nextLine();

            System.out.print("Enter quantity to order: ");
            int quantityOrdered = sc.nextInt();
            sc.nextLine(); // consume newline

            warehouse.fulfillOrder(id, quantityOrdered);

            System.out.print("\nFulfill another order? (y/n): ");
            orderChoice = sc.nextLine().charAt(0);

        } while (orderChoice == 'y' || orderChoice == 'Y');

        // Final inventory display
        warehouse.displayProducts();

        sc.close();
    }
}




