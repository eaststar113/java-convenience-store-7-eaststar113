package store;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
