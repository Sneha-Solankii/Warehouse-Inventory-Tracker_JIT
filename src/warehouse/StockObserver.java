package warehouse;

// 21 Oct → Observer Interface
public interface StockObserver {
    void onLowStock(Product product);
}
