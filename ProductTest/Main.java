import java.util.Scanner;
import java.io.IOException;
import java.io.*;

public class Main {
    public static void main(String[] args) {

        //constructor
        ProductCatalog p1 = new ProductCatalog();

        // variables test interface
        String test;

        // Scanner to accept user input
        Scanner scanner = new Scanner(System.in);

        System.out.println("****************************Program Start****************************");

        System.out.println("****************************Test OpenFile and getAllProductInfo****************************");
        System.out.print("Enter a file name: ");
        test = scanner.next();
        File inputFile = new File(test);

        if (!inputFile.exists()) {
            System.out.println("Error: data.txt not found!");
        }
        else {
            try (FileInputStream fileInputStream = new FileInputStream(inputFile)) {
                // Store the product data into the LinkedList
                p1.store(fileInputStream); // Call store method to read and store products

                // Display all products in the LinkedList
                p1.getAllProductInfo(System.out);// Call display method to print all products

            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("****************************Test Search and getProductDetails****************************");

        System.out.print("Search for an SKU: ");
        String skuSearch = scanner.next();
        int index = p1.search(skuSearch);

        if (index != -1) {
            System.out.println("SKU " + skuSearch + " found at index: " + index);
            System.out.println("Product Details: " + p1.getProductDetails(index));
        }
        else {
            System.out.println("SKU " + skuSearch + " not found.");
        }

        System.out.println("****************************Test addProduct****************************");
        //!!!(make sure the text file has a new blank line or else it will cause an error)!!!
        // Add a new product
        scanner.nextLine(); // Weird java issue fix so it won't skip over this entry
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter SKU: ");
        String sku = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter quantity: ");
        Object quantity = scanner.nextLine();
        System.out.print("Enter price: ");
        Object price = scanner.nextLine();

        p1.addProduct(new Product(name, sku, description, quantity, price));

        System.out.println("****************************Test updateProductQuantity****************************");
        //!!!(make sure the text file has a new blank line or else it will cause an error)!!!

        System.out.print("Enter SKU to update quantity: ");
        String skuToUpdate = scanner.next();
        System.out.print("Enter new quantity: ");
        Object newQuantity = scanner.next();

        if (p1.updateProductQuantity(skuToUpdate, newQuantity)) {
            System.out.println("Quantity updated successfully.");
        }
        else {
            System.out.println("SKU not found. Quantity not updated.");
        }

        System.out.println("****************************Test Waitlist File Entry and Notification****************************");
        // !!! this will remove the emailed waitlist users, so I will paste them here for a reset !!!
        //pan1r0n,john@email.com,789
        //cat111,alice@email.com,001
        //cat111,bob@email.com,002
        //pan1r0n,tom@email.com,003
        // !!!

        System.out.print("Enter SKU to waitlist for: ");
        String waitlistSkuFile = scanner.next();

        System.out.print("Enter customer email: ");
        String email = scanner.next();

        System.out.print("Enter customer ID: ");
        String clientID = scanner.next();

        // Add to waitlist .txt file
        p1.addToWaitlistFile(waitlistSkuFile, email, clientID);

        // Simulate out of stock and then update it to >0
        Product prod = p1.getProductBySku(waitlistSkuFile);
        if (prod != null) {
            p1.updateProductQuantity(waitlistSkuFile, 0); // Set to 0
            System.out.println("Simulated out-of-stock for SKU: " + waitlistSkuFile);

            System.out.print("Enter restocked quantity: ");
            int restockedQty = scanner.nextInt();

            // Now restock, which should trigger notification
            p1.updateProductQuantity(waitlistSkuFile, restockedQty);
        }

        System.out.println("****************************Program End****************************");
        scanner.close();

    }

}

