public class Product {

    private String name;
    private String sku;
    private String description;
    private Object quantity; // Can handle Integer and String
    private Object defaultPrice; // Can handle Double and String

    public Product(String name, String sku, String description, Object quantity, Object defaultPrice) {
        this.name = name;
        this.sku = sku; //make unique
        this.description = description;
        this.quantity = quantity;
        this.defaultPrice = defaultPrice;
    }

    public String getName() {
        return name;
    }

    public String getSku() {
        return sku;
    }

    public String getDescription() {
        return description;
    }

    public Object getQuantity() {
        return quantity;
    }

    public Object getDefaultPrice() {
        return defaultPrice;
    }

    //Gets the details of the currently saved product
    public String getDetails() {
        return "Product Name: " + name + ", SKU: " + sku + ", Description: " + description + ", Quantity: " + quantity + ", Price: " + defaultPrice;
    }
    //Update quantity
    public void updateStock(Object newQuantity) {
        this.quantity = newQuantity;
    }

}
