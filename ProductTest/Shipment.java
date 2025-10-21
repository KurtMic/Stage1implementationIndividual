public class Shipment {
    private ProductCatalog catalog;

    public Shipment(ProductCatalog catalog) {
        this.catalog = catalog;
    }

    // Simulates receiving a shipment and updating stock
    public void receiveShipment(String sku, int quantityReceived) {
        Product product = catalog.getProductBySku(sku);
        if (product == null) {
            System.out.println("Shipment Error: Product with SKU " + sku + " not found.");
            return;
        }

        if (!(product.getQuantity() instanceof Integer)) {
            System.out.println("Invalid quantity type.");
            return;
        }

        int currentQty = (Integer) product.getQuantity();
        int newQty = currentQty + quantityReceived;

        // Update quantity and auto-fulfill if full version
        catalog.updateProductQuantity(sku, newQty);

        // Confirmation message
        System.out.println("Shipment Received: " + quantityReceived + " units added to " + product.getName());
        System.out.println("Updated Quantity: " + newQty);
    }
}
