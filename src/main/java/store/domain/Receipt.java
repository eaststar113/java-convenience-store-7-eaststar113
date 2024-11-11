package store;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import store.domain.Product;

public class Receipt {

    Map<Product, Integer> list;
    Map<Product, Integer> onePlusOne;
    List<Integer> perPricePurchase;
    int membershipDiscount;

    public Receipt() {
        list = new LinkedHashMap<>();
        onePlusOne = new LinkedHashMap<>();
        perPricePurchase = new ArrayList<>();
        membershipDiscount = 0;
    }

    public int calculateTotalPrice() {
        int price = 0;
        for (Map.Entry<Product, Integer> product : list.entrySet()) {
            int perPrice = product.getKey().getPrice();
            int su = product.getValue();
            price += perPrice * su;
        }
        return price;
    }

    public int calculateTotalQuantity() {
        int suSum = 0;
        for (Map.Entry<Product, Integer> product : list.entrySet()) {
            int su = product.getValue();
            suSum += su;
        }
        return suSum;
    }

    public int calculateDiscountPrice() {
        int discountPrice = 0;
        for (Map.Entry<Product, Integer> product : onePlusOne.entrySet()) {
            int perPrice = product.getKey().getPrice();
            int su = product.getValue();
            discountPrice += perPrice*su;
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
        onePlusOne.put(productName, quantity);
    }

    public Map<Product, Integer> getList() {
        return list;
    }

    public Map<Product, Integer> getOnePlusOne() {
        return onePlusOne;
    }
}
