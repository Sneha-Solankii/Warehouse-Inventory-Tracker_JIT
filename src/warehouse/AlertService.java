package warehouse;

//21 Oct → Concrete implementation of StockObserver
public class AlertService implements StockObserver {

    @Override
    public void onLowStock(Product product) {
        System.out.println("🚨 ALERT: Stock for product '" + product.getName() + 
                           "' (ID: " + product.getId() + ") is LOW! " +
                           "Current Quantity: " + product.getQuantity() +
                           ", Reorder Level: " + product.getReorderThreshold());
    }
}
