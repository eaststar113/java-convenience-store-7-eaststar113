package store.view;

import static store.constants.OutputMessage.BENEFIT_MESSAGE;
import static store.constants.OutputMessage.CATEGORY_MESSAGE;
import static store.constants.OutputMessage.DISCOUNT_PRICE_MESSAGE;
import static store.constants.OutputMessage.DISTINCT_LINE_MESSAGE;
import static store.constants.OutputMessage.INTRODUCE_PRODUCT_MESSAGE;
import static store.constants.OutputMessage.MEMBERSHIP_PRICE_MESSAGE;
import static store.constants.OutputMessage.NEXT_LINE;
import static store.constants.OutputMessage.STORE_MESSAGE;
import static store.constants.OutputMessage.TOTAL_PRICE_MESSAGE;
import static store.constants.OutputMessage.WELCOME_MESSAGE;
import static store.constants.OutputMessage.YOU_PAY_PRICE_MESSAGE;

import java.util.Map;
import store.domain.Inventory;
import store.domain.Product;
import store.domain.Receipt;

public class OutputView {

    public static void displayProducts(Inventory products) {
        System.out.println(NEXT_LINE.getMessage()+WELCOME_MESSAGE.getMessage());
        System.out.println(INTRODUCE_PRODUCT_MESSAGE.getMessage()+NEXT_LINE.getMessage());

        for (Product product : Inventory.getProducts()) {
            System.out.println(product);
        }
    }

    public static void displayReceipt(Receipt receipt) {
        System.out.println(NEXT_LINE.getMessage()+STORE_MESSAGE.getMessage());
        System.out.println(CATEGORY_MESSAGE.getMessage());

        printProductItems(receipt);
        System.out.println(BENEFIT_MESSAGE.getMessage());
        printBenefitItems(receipt);
        System.out.println(DISTINCT_LINE_MESSAGE.getMessage());
        printPriceInfo(receipt);
    }

    private static void printPriceInfo(Receipt receipt) {
        printTotalPrice(receipt);
        printDiscountPrice(receipt);
        printFinalPrice(receipt);
    }

    private static void printProductItems(Receipt receipt) {
        for (Map.Entry<Product, Integer> product : receipt.getList().entrySet()) {
            String name = product.getKey().getName();
            int price = product.getKey().getPrice();
            int count = product.getValue();
            System.out.println(name + "\t\t\t" + count + "\t" + String.format("%,d", price * count));
        }
    }

    private static void printBenefitItems(Receipt receipt) {
        for (Map.Entry<Product, Integer> product : receipt.getOnePlusOne().entrySet()) {
            String name = product.getKey().getName();
            int count = product.getValue();
            System.out.println(name+"\t\t"+"\t"+count+"\t");
        }
    }

    private static void printTotalPrice(Receipt receipt) {
        int countSum = receipt.calculateTotalQuantity();
        int price = receipt.calculateTotalPrice();
        System.out.println(TOTAL_PRICE_MESSAGE.format(countSum,price));
    }

    private static void printDiscountPrice(Receipt receipt) {
        int discountPrice = receipt.calculateDiscountPrice();
        System.out.println(DISCOUNT_PRICE_MESSAGE.format(discountPrice));
        System.out.println(MEMBERSHIP_PRICE_MESSAGE.format(receipt.getMembershipDiscount()));
    }

    private static void printFinalPrice(Receipt receipt) {
        int finalPrice = receipt.calculateFinalPrice();
        System.out.println(YOU_PAY_PRICE_MESSAGE.format(finalPrice));
    }
}