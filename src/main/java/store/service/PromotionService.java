package store.service;

import static store.constants.ConstantMessage.ANSWER_NO;
import static store.constants.ConstantMessage.ANSWER_YES;
import static store.domain.Inventory.checkOrderIsNoPromotion;
import static store.domain.Inventory.checkOrderIsPromotion;

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

public class PromotionService {

    Map<String, Integer> updateProductList;
    Receipt receipt;
    int membershipDiscount = 0;

    public PromotionService(Order order, Promotions promotions) {
        updateProductList = new LinkedHashMap<>();
        receipt = new Receipt();
        updateProductAndCheckPromotion(order, promotions);
        updateReceipt();
        updateBenefitProduct(promotions);
        updateInventory(promotions);
    }

    public void updateProductAndCheckPromotion(Order order, Promotions promotions) {
        Map<String, Integer> orderItems = order.getOrderItems();
        orderItems.forEach((productName, quantity) -> {
            updateProductList.put(productName, quantity);
            Product product = checkOrderIsPromotion(productName);

            if (product == null) {
                checkNoPromotionDiscount(productName);
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

    private void checkNoPromotionDiscount(String productName) {
        Product noProduct = checkOrderIsNoPromotion(productName);
        if (noProduct != null) {
            membershipDiscount += noProduct.getPrice() * noProduct.getQuantity();
        }
    }

    private void calculateMembershipDiscount() {
        String membershipAnswer = retryMembershipDiscount();
        if (ANSWER_YES.getMessage().equals(membershipAnswer)) {
            membershipDiscount *= 0.3;
            receipt.setMembershipDiscount(membershipDiscount);
        }
    }

    private void checkPromotionCondition(Product product, Promotion promotion, int quantity,
                                          Map<String, Integer> updateProductList) {
        if (checkOrderPromotionBenefit(product, promotion, quantity)) {
            String benefitAnswer = retryCheckBenefit(product);
            if (ANSWER_YES.getMessage().equals(benefitAnswer)) {
                canGetOneBenefit(product, quantity, updateProductList);
            }
            return;
        }
        checkPromotionOutOfStock(product, promotion, quantity, updateProductList);
    }

    private void canGetOneBenefit(Product product, int quantity, Map<String, Integer> updateProductList) {
        quantity += 1;
        updateProductList.put(product.getName(), quantity);
    }

    private void checkPromotionOutOfStock(Product product, Promotion promotion, int quantity,
                                         Map<String, Integer> updateProductList) {
        String ans = getOutOfStockStatus(product, promotion, quantity);
        if (ANSWER_NO.getMessage().equals(ans)) {
            int outOfStockCount = calculateOutOfStockCount(product, promotion, quantity);
            membershipDiscount += outOfStockCount * product.getPrice();
            updateProductList.put(product.getName(), quantity - outOfStockCount);
        }
    }

    private int calculateOutOfStockCount(Product product, Promotion promotion, int quantity) {
        int sum = promotion.getBuy() + promotion.getGet();
        int stockShare = product.getQuantity() / sum;
        int orderShare = quantity / sum;
        int lessShare = Math.min(stockShare, orderShare);
        return quantity - (lessShare * sum);
    }

    public void updateBenefitProduct(Promotions promotions){
        updateProductList.forEach((productName, quantity) -> {
            Product product = checkOrderIsPromotion(productName);
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

    public void updateInventory(Promotions promotions){
        updateProductList.forEach((productName, quantity) -> {
            Product product = checkOrderIsPromotion(productName);
            if(product != null){
                String promotionName = product.getPromotion();
                Promotion promotion = promotions.findPromotion(promotionName);
                if(promotion.isPromotionActive(LocalDate.from(DateTimes.now()))){
                    Inventory.decreasePromotionProduct(product,productName,quantity);
                }
                return ;
            }
            Inventory.decreaseNoPromotionProduct(productName,quantity);
        });
    }

    public void updateReceipt() {
        updateProductList.forEach((productName, quantity) -> {
            Product product = checkOrderIsNoPromotion(productName);
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
        int stockShare = product.getQuantity() / sum;
        int orderShare = quantity / sum;
        int lessShare = Math.min(stockShare, orderShare);
        int outOfStockCount = quantity - (lessShare * sum);
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
