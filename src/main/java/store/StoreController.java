package store;

import java.io.IOException;

public class StoreController {
    public static void run(){
        Inventory inventory = displayInventory();
        OutputView.displayProducts(inventory);

        Order order = retryOrder();
    }

    public static Inventory displayInventory() {
        try {
            return new Inventory();
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
