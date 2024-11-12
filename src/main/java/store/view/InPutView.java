package store.view;

import static store.constants.InputMessage.MEMBERSHIP_MESSAGE;
import static store.constants.InputMessage.NEXT_LINE;
import static store.constants.InputMessage.ORDER_MESSAGE;
import static store.constants.InputMessage.PROMOTION_BENEFIT_MESSAGE;
import static store.constants.InputMessage.PROMOTION_STOCK_ALERT_MESSAGE;
import static store.constants.InputMessage.REORDER_MESSAGE;

import camp.nextstep.edu.missionutils.Console;
import store.domain.Product;

public class InPutView {

    public static String printOrderMessage() {
        System.out.println(NEXT_LINE.getMessage()+ORDER_MESSAGE.getMessage());
        return Console.readLine();
    }

    public static String printPromotionStockAlertMessage(Product product, int outOfStockCount) {
        System.out.printf(NEXT_LINE.getMessage()+PROMOTION_STOCK_ALERT_MESSAGE.format(product.getName(), outOfStockCount));
        return Console.readLine();
    }

    public static String printPromotionBenefitMessage(Product product) {
        System.out.printf(NEXT_LINE.getMessage()+PROMOTION_BENEFIT_MESSAGE.format(product.getName()));
        return Console.readLine();
    }

    public static String printMembershipMessage() {
        System.out.println(NEXT_LINE.getMessage()+MEMBERSHIP_MESSAGE.getMessage());
        return Console.readLine();
    }

    public static String printReorderMessage() {
        System.out.println(NEXT_LINE.getMessage()+REORDER_MESSAGE.getMessage());
        return Console.readLine();
    }
}
