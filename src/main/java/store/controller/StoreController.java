package store.controller;

import static store.constants.ConstantMessage.ANSWER_YES;

import java.io.IOException;
import java.util.Objects;
import store.util.Validate;
import store.domain.Inventory;
import store.domain.Order;
import store.domain.Promotions;
import store.service.PromotionService;
import store.view.InPutView;
import store.view.OutputView;

public class StoreController {
    public static void run(){
        Inventory inventory = displayInventory();
        Promotions promotions = displayPromotion();
        do{
            OutputView.displayProducts(inventory);
            Order order = retryOrder();
            PromotionService promotionService = new PromotionService(order,promotions,inventory);
            OutputView.displayReceipt(promotionService.getReceipt());
        }while(Objects.equals(reOrder(), ANSWER_YES.getMessage()));
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
