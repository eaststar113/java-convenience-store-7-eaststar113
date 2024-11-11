package store.view;

import static store.InputMessage.NEXT_LINE;
import static store.InputMessage.ORDER_MESSAGE;

import camp.nextstep.edu.missionutils.Console;
import store.Product;

public class InPutView {

    public static String printOrderMessage() {
        System.out.println(NEXT_LINE.getMessage()+ORDER_MESSAGE.getMessage());
        return Console.readLine();
    }

    public static String printPromotionStockAlertMessage(Product product, int outOfStockCount) {
        System.out.printf("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)%n",
                product.getName(), outOfStockCount);
        return Console.readLine();
    }

    public static String printPromotionBenefitMessage(Product product) {
        System.out.println();
        System.out.printf("현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)%n", product.getName());
        return Console.readLine();
    }

    public static String printMembershipMessage() {
        System.out.println();
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        return Console.readLine();
    }

    public static String printReorderMessage() {
        System.out.println();
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        return Console.readLine();
    }
}
