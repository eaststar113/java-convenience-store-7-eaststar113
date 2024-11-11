package store;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import store.view.InPutView;

public class PromotionStockAlertService {

    Map<String, Integer> imsi;
    Receipt receipt;
    int membershipDiscount = 0;

    public PromotionStockAlertService(Order order, Promotions promotions, Inventory inventory) {
        imsi = new LinkedHashMap<>();
        receipt = new Receipt();
        check(order, promotions, inventory);
        imsi2(inventory);
        onePlusOne(order, promotions, inventory);
    }

    public void check(Order order, Promotions promotions, Inventory inventory) {
        Map<String, Integer> orderItems = order.getOrderItems();
        orderItems.forEach((productName, quantity) -> {
            imsi.put(productName, quantity);
            Product product = inventory.checkOrderIsPromotion(productName);

            if (product == null) {
                Product noProduct = inventory.checkOrderIsNoPromotion(productName);
                if (noProduct != null) {
                    membershipDiscount += noProduct.getPrice() * noProduct.getQuantity();
                }
                return ;
            }
            // 프로모션이 있는 상품 처리
            String orderPromotionName = product.getPromotion();
            Promotion promotion = promotions.findPromotion(orderPromotionName);

            if (promotion != null && promotion.isPromotionActive(LocalDate.from(DateTimes.now()))) {
                if (checkOrderPromotionBenefit(product, promotion, quantity)) {
                    String benefitAnswer = retryCheckBenefit(product);
                    if ("Y".equals(benefitAnswer)) {
                        quantity += 1;
                        imsi.put(productName, quantity);
                    }
                    return ;
                }
                String ans = checkOrderPromotionStatus(product, promotion, quantity);
                if ("N".equals(ans)) {
                    int sum = promotion.getBuy() + promotion.getGet();
                    int stockMok = product.getQuantity() / sum;
                    int orderMok = quantity / sum;
                    int lessMok = Math.min(stockMok, orderMok);
                    int outOfStockCount = quantity - (lessMok * sum);
                    //int outOfStockCount = quantity - (promotion.getBuy() + promotion.getGet());
                    membershipDiscount += outOfStockCount * product.getPrice();
                    //quantity = outOfStockCount;

                    imsi.put(productName, quantity - outOfStockCount);
                }
            }
        });
        //멤버십할인 //while
        String membershipAnswer = retryMembershipDiscount();
        if ("y".equals(membershipAnswer)) {
            membershipDiscount *= 0.3;
            receipt.setMembershipDiscount(membershipDiscount);
        }
    }

    public void onePlusOne(Order order, Promotions promotions, Inventory inventory){
        imsi.forEach((productName, quantity) -> {
            Product product = inventory.checkOrderIsPromotion(productName);
            if(product != null){
                String promotionName = product.getPromotion();
                Promotion promotion = promotions.findPromotion(promotionName);
                if(promotion.isPromotionActive(LocalDate.from(DateTimes.now()))){
                    int sum = promotion.getBuy() + promotion.getGet();
                    int mok = Math.min(product.getQuantity(),quantity) / sum;
                    if (mok != 0) {
                        receipt.putOnePlusOne(product,mok);
                    }
                }
            }
        });
    }

    public void imsi2(Inventory inventory) {
        imsi.forEach((productName, quantity) -> {
            Product product = inventory.checkOrderIsNoPromotion(productName);
            receipt.list.put(product, quantity);
        });
    }

    public String retryMembershipDiscount() {
        while (true) {
            try {
                String ans = InPutView.membershipMessage();
                ans = validate(ans);
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
                String ans = InPutView.promotionBenefitMessage(product);
                ans = validate(ans);
                return ans;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private String checkOrderPromotionStatus(Product product, Promotion promotion, int quantity) {
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
                String ans = InPutView.promotionStockAlertMessage(product, outOfStockCount);
                ans = validate(ans);
                return ans;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private String validate(String ans) {
        if (!("Y".equals(ans) || "N".equals(ans))) {
            throw new IllegalArgumentException("잘못된 입력입니다. 'Y' 또는 'N'을 입력해야 합니다.");
        }
        return ans;
    }
}
