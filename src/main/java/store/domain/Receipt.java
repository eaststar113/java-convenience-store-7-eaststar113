package store.domain;

import java.util.LinkedHashMap;
import java.util.Map;

public class Receipt {

    Map<Product, Integer> orderList;
    Map<Product, Integer> benefitList;
    int membershipDiscount;

    public Receipt() {
        orderList = new LinkedHashMap<>();
        benefitList = new LinkedHashMap<>();
        membershipDiscount = 0;
    }

    public int calculateTotalPrice() {
        int price = 0;
        for (Map.Entry<Product, Integer> product : orderList.entrySet()) {
            int perPrice = product.getKey().getPrice();
            int count = product.getValue();
            price += perPrice * count;
        }
        return price;
    }

    public int calculateTotalQuantity() {
        int countSum = 0;
        for (Map.Entry<Product, Integer> product : orderList.entrySet()) {
            int count = product.getValue();
            countSum += count;
        }
        return countSum;
    }

    public int calculateDiscountPrice() {
        int discountPrice = 0;
        for (Map.Entry<Product, Integer> product : benefitList.entrySet()) {
            int perPrice = product.getKey().getPrice();
            int count = product.getValue();
            discountPrice += perPrice*count;
        }
        return discountPrice;
    }

    public int calculateFinalPrice() {
        int price = calculateTotalPrice();
        int discountPrice = calculateDiscountPrice();
        return price - discountPrice - membershipDiscount;
    }

    public void setMembershipDiscount(int discount) {
        this.membershipDiscount = discount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public void putOnePlusOne(Product productName, int quantity) {
        benefitList.put(productName, quantity);
    }

    public Map<Product, Integer> getList() {
        return orderList;
    }

    public Map<Product, Integer> getOnePlusOne() {
        return benefitList;
    }
}
