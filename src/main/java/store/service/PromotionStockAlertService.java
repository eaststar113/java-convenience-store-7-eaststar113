package store.service;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import store.util.Validate;
import store.domain.Inventory;
import store.domain.Order;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.Promotions;
import store.domain.Receipt;
import store.view.InPutView;

public class PromotionStockAlertService {

    Map<String, Integer> updateProductList;
    Receipt receipt;
    int membershipDiscount = 0;

    public PromotionStockAlertService(Order order, Promotions promotions, Inventory inventory) {
        updateProductList = new LinkedHashMap<>();
        receipt = new Receipt();
        updateProductAndCheckPromotion(order, promotions, inventory);
        updateReceipt(inventory);
        updateBenefitProduct(order, promotions, inventory);
        updateInventory(promotions,inventory);
    }

    public void updateProductAndCheckPromotion(Order order, Promotions promotions, Inventory inventory) {
        Map<String, Integer> orderItems = order.getOrderItems();
        orderItems.forEach((productName, quantity) -> {
            updateProductList.put(productName, quantity);
            Product product = inventory.checkOrderIsPromotion(productName);

            if (product == null) {
                checkNoPromotionDiscount(productName,inventory);
                return ;
            }
            String orderPromotionName = product.getPromotion();
            Promotion promotion = promotions.findPromotion(orderPromotionName);

            if (promotion != null && promotion.isPromotionActive(LocalDate.from(DateTimes.now()))) {
                checkPromotionCondition(product, promotion, quantity, updateProductList);
            }
        });
        calculateMembershipDiscount();
    }


    private void checkNoPromotionDiscount(String productName, Inventory inventory) {
        Product noProduct = inventory.checkOrderIsNoPromotion(productName);
        if (noProduct != null) {
            membershipDiscount += noProduct.getPrice() * noProduct.getQuantity();
        }
    }

    private void calculateMembershipDiscount() {
        String membershipAnswer = retryMembershipDiscount();
        if ("Y".equals(membershipAnswer)) {
            membershipDiscount *= 0.3;
            receipt.setMembershipDiscount(membershipDiscount);
        }
    }

    private void checkPromotionCondition(Product product, Promotion promotion, int quantity,
                                          Map<String, Integer> imsi) {
        if (checkOrderPromotionBenefit(product, promotion, quantity)) {
            String benefitAnswer = retryCheckBenefit(product);
            if ("Y".equals(benefitAnswer)) {
                canGetOneBenefit(product, quantity, imsi);
            }
            return;
        }
        checkPromotionOutOfStock(product, promotion, quantity, imsi);
    }

    private void canGetOneBenefit(Product product, int quantity, Map<String, Integer> imsi) {
        quantity += 1;
        imsi.put(product.getName(), quantity);
    }

    private void checkPromotionOutOfStock(Product product, Promotion promotion, int quantity,
                                         Map<String, Integer> imsi) {
        String ans = getOutOfStockStatus(product, promotion, quantity);
        if ("N".equals(ans)) {
            int outOfStockCount = calculateOutOfStockCount(product, promotion, quantity);
            membershipDiscount += outOfStockCount * product.getPrice();
            imsi.put(product.getName(), quantity - outOfStockCount);
        }
    }

    private int calculateOutOfStockCount(Product product, Promotion promotion, int quantity) {
        int sum = promotion.getBuy() + promotion.getGet();
        int stockMok = product.getQuantity() / sum;
        int orderMok = quantity / sum;
        int lessMok = Math.min(stockMok, orderMok);
        return quantity - (lessMok * sum);
    }



    public void updateBenefitProduct(Order order, Promotions promotions, Inventory inventory){
        updateProductList.forEach((productName, quantity) -> {
            Product product = inventory.checkOrderIsPromotion(productName);
            if(product != null){
                String promotionName = product.getPromotion();
                Promotion promotion = promotions.findPromotion(promotionName);
                if(promotion.isPromotionActive(LocalDate.from(DateTimes.now()))){
                    int buyPlusGet = promotion.getBuy() + promotion.getGet();
                    int benefit = Math.min(product.getQuantity(),quantity) / buyPlusGet;
                    if (benefit != 0) {
                        receipt.putOnePlusOne(product,benefit);
                    }
                }
            }
        });
    }

    public void updateInventory(Promotions promotions, Inventory inventory){
        updateProductList.forEach((productName, quantity) -> {
            Product product = inventory.checkOrderIsPromotion(productName);
            if(product != null){
                String promotionName = product.getPromotion();
                Promotion promotion = promotions.findPromotion(promotionName);
                if(promotion.isPromotionActive(LocalDate.from(DateTimes.now()))){
                    decreasePromotionProduct(product,inventory,productName,quantity);
                }
                return ;
            }
            decreaseNoPromotionProduct(inventory,productName,quantity);
        });
    }

    public void decreasePromotionProduct(Product product, Inventory inventory, String productName,int quantity){
        int rest = product.getQuantity() - quantity;
        if(rest<0){
            product.setQuantity(0);
            Product nonPromotionProduct = inventory.checkOrderIsNoPromotion(productName);
            int absoluteRest = -1 * rest;
            reduce(nonPromotionProduct,absoluteRest);
            return ;
        }
        reduce(product,quantity);
    }

    public void decreaseNoPromotionProduct(Inventory inventory, String productName,int quantity){
        Product nonPromotionProduct = inventory.checkOrderIsNoPromotion(productName);
        int rest = nonPromotionProduct.getQuantity() - quantity;
        if(rest<0){
            nonPromotionProduct.setQuantity(0);
            Product hasProduct = inventory.checkOrderIsPromotion(productName);
            int absoluteRest = -1 * rest;
            reduce(hasProduct,absoluteRest);
        }
        reduce(nonPromotionProduct,quantity);
    }

    public void reduce(Product product,int quantity) {
        for(int i = 0;i<quantity;i++){
            product.reduceQuantity();
        }
    }

    public void updateReceipt(Inventory inventory) {
        updateProductList.forEach((productName, quantity) -> {
            Product product = inventory.checkOrderIsNoPromotion(productName);
            receipt.getList().put(product, quantity);
        });
    }

    public String retryMembershipDiscount() {
        while (true) {
            try {
                String ans = InPutView.printMembershipMessage();
                ans = Validate.validate(ans);
                return ans;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private boolean checkOrderPromotionBenefit(Product product, Promotion promotion, int quantity) {
        if (product.getQuantity() > quantity) {
            if ((quantity % (promotion.getBuy() + promotion.getGet())) == promotion.getBuy()) {
                return true;
            }
        }
        return false;
    }

    public String retryCheckBenefit(Product product) {
        while (true) {
            try {
                String ans = InPutView.printPromotionBenefitMessage(product);
                ans = Validate.validate(ans);
                return ans;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private String getOutOfStockStatus(Product product, Promotion promotion, int quantity) {
        int sum = promotion.getBuy() + promotion.getGet();
        int stockMok = product.getQuantity() / sum;
        int orderMok = quantity / sum;
        int lessMok = Math.min(stockMok, orderMok);
        int outOfStockCount = quantity - (lessMok * sum);
        if (outOfStockCount == 0) {
            return null;
        }
        String ans = retryCheckStauts(product, outOfStockCount);
        return ans;
    }

    public String retryCheckStauts(Product product, int outOfStockCount) {
        while (true) {
            try {
                String ans = InPutView.printPromotionStockAlertMessage(product, outOfStockCount);
                ans = Validate.validate(ans);
                return ans;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public Receipt getReceipt() {
        return receipt;
    }
}
