package warehouse;

// 23 Oct — Thread simulation for event-driven updates
public class WarehouseTestSimulation implements Runnable {
    private Warehouse warehouse;
    private String productId;
    private String actionType;
    private int quantity;

    public WarehouseTestSimulation(Warehouse warehouse, String productId, String actionType, int quantity) {
        this.warehouse = warehouse;
        this.productId = productId;
        this.actionType = actionType;
        this.quantity = quantity;
    }

    @Override
    public void run() {
        try {
            if (actionType.equalsIgnoreCase("RECEIVE")) {
                warehouse.receiveShipment(productId, quantity);
            } else if (actionType.equalsIgnoreCase("FULFILL")) {
                warehouse.fulfillOrder(productId, quantity);
            }
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
