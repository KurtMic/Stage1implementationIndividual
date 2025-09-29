import java.io.*;
import java.util.LinkedList;


public class ProductCatalog {

    private LinkedList<Product> list;

    //**************************Below Is LinkedList For Retrieving Data**************************

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
        try {
            return Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            return quantity; // Return as String if not an Integer
        }
    }

    private Object parsePrice(String price) {
        try {
            return Double.parseDouble(price);
        } catch (NumberFormatException e) {
            return price; // Return as String if not a Double
        }
    }

    //**************************Below Is Methods**************************

    //Displays all data stored in a file
    public void getAllProductInfo(PrintStream outputStream) {
        for (Product product : list) {
            outputStream.println(product.getDetails());
        }
    }

    //Searches through a file using a SKU and stores that data
    public int search(String sku) {
        for (int x = 0; x < list.size(); x++) {
            if (list.get(x).getSku().equals(sku)) {
                return x;
            }
        }
        return -1;
    }

    //Searches through a file using a SKU and stores that data
    public String getProductDetails(int index) {
        if (index >= 0 && index < list.size()) {
            return list.get(index).getDetails();
        }
        return "Product not found.";
    }

    //Add Product to existing catalog file
    public void addProduct(Product product) {
        list.add(product);
        try (PrintWriter writer = new PrintWriter(new FileWriter("productcatalog.txt", true))) {
            //Writer adds a new entry on a new line (make sure the text file has a new blank line)
            writer.println(product.getName() + "," + product.getSku() + "," + product.getDescription() + "," + product.getQuantity() + "," + product.getDefaultPrice());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Update product quantity
    public boolean updateProductQuantity(String sku, Object newQuantity) {
        int index = search(sku);
        if (index != -1) {
            list.get(index).updateStock(newQuantity);
            updateFile();
            return true;
        }
        return false;
    }

    // Update catalog file
    private void updateFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("productcatalog.txt"))) {
            for (Product product : list) {
                writer.println(product.getName() + "," + product.getSku() + "," + product.getDescription() + "," + product.getQuantity() + "," + product.getDefaultPrice());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
