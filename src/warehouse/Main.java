package warehouse;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // User input for product details
        System.out.println("=== Add New Product ===");
        System.out.print("Enter Product ID: ");
        String id = sc.nextLine();

        System.out.print("Enter Product Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Initial Quantity: ");
        int quantity = sc.nextInt();

        System.out.print("Enter Reorder Threshold: ");
        int threshold = sc.nextInt();

        // Create product object
        Product product = new Product(id, name, quantity, threshold);

        // Display product details (to check input)
        System.out.println("\n✅ Product Created Successfully!");
        System.out.println("ID: " + product.getId());
        System.out.println("Name: " + product.getName());
        System.out.println("Quantity: " + product.getQuantity());
        System.out.println("Reorder Threshold: " + product.getReorderThreshold());
    }
}
