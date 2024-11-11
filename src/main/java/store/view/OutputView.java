package store.view;

import java.util.Map;
import store.Inventory;
import store.Product;
import store.Receipt;

public class OutputView {

    public static void displayProducts(Inventory products) {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.\n");

        for (Product product : Inventory.getProducts()) {
            System.out.println(product);
        }
    }

    public static void displayReceipt(Receipt receipt) {
        System.out.println("==============W 편의점================");
        System.out.println("상품명\t\t수량\t금액");

        printProductItems(receipt);
        System.out.println("=============증\t정===============");
        printBenefitItems(receipt);
        System.out.println("====================================");
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
            int su = product.getValue();
            System.out.println(name + "\t\t" + su + "\t" + String.format("%,d", price * su));
        }
    }

    private static void printBenefitItems(Receipt receipt) {
        for (Map.Entry<Product, Integer> product : receipt.getOnePlusOne().entrySet()) {
            String name = product.getKey().getName();
            int su = product.getValue();
            System.out.println(name+"\t"+"\t"+su+"\t");
        }
    }

    private static void printTotalPrice(Receipt receipt) {
        int suSum = receipt.calculateTotalQuantity();
        int price = receipt.calculateTotalPrice();
        System.out.println("총구매액\t\t" + suSum + "\t" + String.format("%,d", price));
    }

    private static void printDiscountPrice(Receipt receipt) {
        int discountPrice = receipt.calculateDiscountPrice();
        System.out.println("행사할인\t\t\t\t" + "-" + String.format("%,d", discountPrice));
        System.out.println("멤버십 할인\t\t\t\t" + "-" + String.format("%,d", receipt.getMembershipDiscount()));
    }

    private static void printFinalPrice(Receipt receipt) {
        int finalPrice = receipt.calculateFinalPrice();
        System.out.println("내실돈\t\t\t\t" + String.format("%,d", finalPrice));
    }
}