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

        for (Map.Entry<Product, Integer> product : receipt.getList().entrySet()) {
            String name = product.getKey().getName();
            int price = product.getKey().getPrice();
            int su = product.getValue();
            System.out.println(name + "\t\t" + su + "\t" + String.format("%,d", price * su));
        }
        System.out.println("=============증\t정===============");
        for (Map.Entry<Product, Integer> product : receipt.getOnePlusOne().entrySet()) {
            String name = product.getKey().getName();
            int su = product.getValue();
            System.out.println(name+"\t"+"\t"+su+"\t");
        }
        System.out.println("====================================");
        int price = 0;
        int suSum = 0;
        for (Map.Entry<Product, Integer> product : receipt.getList().entrySet()) {
            int perPrice = product.getKey().getPrice();
            int su = product.getValue();
            price += perPrice * su;
            suSum += su;
        }
        System.out.println("총구매액\t\t" + suSum + "\t" + String.format("%,d", price));
        int perPrice = 0;
        int su = 0;
        int discountPrice = 0;
        for (Map.Entry<Product, Integer> product : receipt.getOnePlusOne().entrySet()) {
            perPrice = product.getKey().getPrice();
            su = product.getValue();
            discountPrice += perPrice*su;
        }
        System.out.println("행사할인\t\t\t\t" + "-" + String.format("%,d", discountPrice));
        System.out.println("멤버십 할인\t\t\t\t" + "-" + String.format("%,d", receipt.getMembershipDiscount()));

        int finalPrice = price - discountPrice - receipt.getMembershipDiscount();
        System.out.println("내실돈\t\t\t\t" + String.format("%,d", finalPrice));
    }
}