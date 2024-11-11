package store.controller;

import java.io.IOException;
import java.util.Objects;
import store.util.Validate;
import store.domain.Inventory;
import store.domain.Order;
import store.domain.Promotions;
import store.service.PromotionStockAlertService;
import store.view.InPutView;
import store.view.OutputView;

public class StoreController {
    public static void run(){
        Inventory inventory = displayInventory();
        Promotions promotions = displayPromotion();
        do{
            OutputView.displayProducts(inventory);

            Order order = retryOrder();
            PromotionStockAlertService promotionStockAlertService = new PromotionStockAlertService(order,promotions,inventory);
            OutputView.displayReceipt(promotionStockAlertService.getReceipt());
        }while(Objects.equals(reOrder(), "Y"));
    }

    public static String reOrder() {
        while (true) {
            try {
                String ans = InPutView.printReorderMessage();
                ans = Validate.validate(ans);
                return ans;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
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
                return new Order(InPutView.printOrderMessage());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
