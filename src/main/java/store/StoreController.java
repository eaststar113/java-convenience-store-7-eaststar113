package store;

import java.io.IOException;
import store.view.InPutView;
import store.view.OutputView;

public class StoreController {
    public static void run(){
        Inventory inventory = displayInventory();
        Promotions promotions = displayPromotion();
        OutputView.displayProducts(inventory);

        Order order = retryOrder();
        PromotionStockAlertService psaservice = new PromotionStockAlertService(order,promotions,inventory);
        OutputView.displayReceipt(psaservice.receipt);
    }

    public static Inventory displayInventory() {
        try {
            return new Inventory();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Promotions displayPromotion() {
        try {
            return new Promotions();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Order retryOrder() {
        while(true){
            try {
                return new Order(InPutView.getOrder());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
