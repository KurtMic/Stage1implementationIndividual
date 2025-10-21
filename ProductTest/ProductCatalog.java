import java.io.*;
import java.util.LinkedList;

public class ProductCatalog {
    private LinkedList<Product> list;

    public ProductCatalog() {
        this.list = new LinkedList<>();
    }

    public void store(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String name = data[0];
                String sku = data[1];
                String description = data[2];
                Object quantity = parseQuantity(data[3]);
                Object defaultPrice = parsePrice(data[4]);
                Product product = new Product(name, sku, description, quantity, defaultPrice);
                list.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object parseQuantity(String quantity) {
        try { return Integer.parseInt(quantity); }
        catch (NumberFormatException e) { return quantity; }
    }

    private Object parsePrice(String price) {
        try { return Double.parseDouble(price); }
        catch (NumberFormatException e) { return price; }
    }

    public void getAllProductInfo(PrintStream outputStream) {
        for (Product product : list) {
            outputStream.println(product.getDetails());
        }
    }

    public Product getProduct(int index) {
        if (index >= 0 && index < list.size()) {
            return list.get(index);
        }
        return null;
    }

    public Product getProductBySku(String sku) {
        int index = search(sku);
        if (index != -1) return list.get(index);
        return null;
    }

    public int search(String sku) {
        for (int x = 0; x < list.size(); x++) {
            if (list.get(x).getSku().equals(sku)) return x;
        }
        return -1;
    }

    public String getProductDetails(int index) {
        if (index >= 0 && index < list.size()) {
            return list.get(index).getDetails();
        }
        return "Product not found.";
    }

    public void addProduct(Product product) {
        list.add(product);
        try (PrintWriter writer = new PrintWriter(new FileWriter("productcatalog.txt", true))) {
            writer.println(product.getName() + "," + product.getSku() + "," + product.getDescription() + "," + product.getQuantity() + "," + product.getDefaultPrice());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean updateProductQuantity(String sku, Object newQuantity) {
        int index = search(sku);
        if (index != -1 && newQuantity instanceof Integer) {
            Product product = list.get(index);

            int oldQty = (product.getQuantity() instanceof Integer) ? (Integer) product.getQuantity() : 0;
            int newQty = (Integer) newQuantity;

            product.updateStock(newQty);

            // Notify waitlist if quantity goes from 0 to >0
            notifyWaitlistIfStockArrives(sku, oldQty, newQty);

            updateFile();
            return true;
        }
        return false;
    }


    private void updateFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("productcatalog.txt"))) {
            for (Product product : list) {
                writer.println(product.getName() + "," + product.getSku() + "," + product.getDescription() + "," + product.getQuantity() + "," + product.getDefaultPrice());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Add customer to waitlist
    public void addToWaitlist(String sku, String customerName) {
        Product product = getProductBySku(sku);
        if (product != null) {
            product.addToWaitlist(customerName);
            System.out.println("Customer " + customerName + " added to waitlist for SKU: " + sku);
        } else {
            System.out.println("SKU not found.");
        }
    }
    // File path for waitlist entries
    private final String WAITLIST_FILE = "waitlist.txt";

    // Add user to waitlist file
    public void addToWaitlistFile(String sku, String email, String clientID) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(WAITLIST_FILE, true))) {
            writer.println(sku + "," + email + "," + clientID);
            System.out.println("Waitlist: Added " + email + " (ID: " + clientID + ") for SKU: " + sku);
        } catch (IOException e) {
            System.out.println("Error writing to waitlist file: " + e.getMessage());
        }
    }

    // Notify users and remove fulfilled waitlist entries when stock goes from 0 to > 0
    public void notifyWaitlistIfStockArrives(String sku, int oldQty, int newQty) {
        if (oldQty == 0 && newQty > 0) {
            File waitlistFile = new File(WAITLIST_FILE);
            if (!waitlistFile.exists()) {
                return;
            }

            File tempFile = new File("waitlist_temp.txt");

            try (
                    BufferedReader reader = new BufferedReader(new FileReader(waitlistFile));
                    PrintWriter writer = new PrintWriter(new FileWriter(tempFile))
            ) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length < 3) continue;

                    String skuInFile = parts[0];
                    String email = parts[1];
                    String clientID = parts[2];

                    if (skuInFile.equals(sku)) {
                        // Notify user
                        System.out.println(" Waitlist Notification Sent to " + email + " (ClientID: " + clientID + ") for SKU: " + sku);
                    } else {
                        writer.println(line); // Keep entry if it's not fulfilled
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Replace original waitlist with updated one
            if (!waitlistFile.delete() || !tempFile.renameTo(waitlistFile)) {
                System.out.println("⚠️ Error updating waitlist file.");
            }
        }
    }

}
