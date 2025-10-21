import java.util.LinkedList;

public class Product {
    private String name;
    private String sku;
    private String description;
    private Object quantity;
    private Object defaultPrice;
    private LinkedList<String> waitlist = new LinkedList<>();

    public Product(String name, String sku, String description, Object quantity, Object defaultPrice) {
        this.name = name;
        this.sku = sku;
        this.description = description;
        this.quantity = quantity;
        this.defaultPrice = defaultPrice;
    }

    public String getName() { return name; }
    public String getSku() { return sku; }
    public String getDescription() { return description; }
    public Object getQuantity() { return quantity; }
    public Object getDefaultPrice() { return defaultPrice; }

    public String getDetails() {
        return "Product Name: " + name + ", SKU: " + sku + ", Description: " + description + ", Quantity: " + quantity + ", Price: " + defaultPrice;
    }

    public void updateStock(Object newQuantity) {
        this.quantity = newQuantity;
    }

    public void addToWaitlist(String customerName) {
        waitlist.add(customerName);
    }

    public LinkedList<String> getWaitlist() {
        return waitlist;
    }

    public void fulfillWaitlist() {
        if (!(quantity instanceof Integer)) return;
        int stock = (int) quantity;
        while (!waitlist.isEmpty() && stock > 0) {
            String customer = waitlist.removeFirst();
            stock--;
            System.out.println("Waitlist Fulfillment: " + customer + " has been shipped 1 unit of " + name);
            System.out.println("Invoice: " + customer + " | SKU: " + sku + " | Price: " + defaultPrice);
        }
        this.quantity = stock;
    }
}
