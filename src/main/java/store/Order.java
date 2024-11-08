package store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order {

    private Map<String, Integer> orderItems;

    public Order(String order) {
        this.orderItems = new HashMap<>();
        validate(order);
        parseOrder(order);
    }

    public static void validate(String order) {
        isOrderEmpty(order);
        isValidFormat(order);
    }

    private void parseOrder(String order) {
        List<String> items = List.of(order.split(","));
        for (String item : items) {
            String content = item.replaceAll("[\\[\\]]", "").trim();
            List<String> parts = List.of(content.split("-"));
            String productName = parts.get(0).trim();
            int quantity = Integer.parseInt(parts.get(1).trim());
            orderItems.put(productName, quantity);
        }
    }

    private static void isOrderEmpty(String order) {
        if(order == null || order.isEmpty()){
            throw new IllegalArgumentException("[ERROR] 입력값이 비어있습니다. 다시 입력해 주세요.");
        }
    }

    private static void isValidFormat(String order) {
        List<String> list = List.of(order.split(",", -1));
        for (String item : list) {
            if (!isValidOrder(item.trim())) {
                throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
            }
        }
    }

    private static boolean isValidOrder(String item) {
        beWrappedInParentheses(item);
        String content = item.replaceAll("[\\[\\]]", "");
        List<String> parts = List.of(content.split("-"));
        checkSizeSplitWithDash(parts);
        checkAboutProductName(parts.get(0).trim());
        checkAboutProductQuantity(parts.get(0).trim(),parts.get(1).trim());
        isValidQuantity(parts.get(1).trim());
        Inventory.isStockSufficient(parts.get(0).trim(), Integer.parseInt(parts.get(1).trim()));
        return true;
    }

    private static void checkAboutProductName(String productName) {
        isEmptyProductName(productName);
        Inventory.isProductExist(productName);
    }

    private static void checkAboutProductQuantity(String productName, String productQuantity) {
        isValidQuantity(productQuantity);
        Inventory.isStockSufficient(productName, Integer.parseInt(productQuantity));
    }

    private static void beWrappedInParentheses(String item) {
        if(!(item.startsWith("[") && item.endsWith("]"))){
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.(대괄호)");
        }
    }

    private static void checkSizeSplitWithDash(List<String> parts) {
        if (parts.size() != 2) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.(사이즈)");
        }
    }

    private static void isEmptyProductName(String productName) {
        if(productName.isEmpty()){
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.(상품명 비었음)");
        }
    }

    private static void isValidQuantity(String quantityStr) {
        try {
            int quantity = Integer.parseInt(quantityStr);
            if (quantity < 1) {
                throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.(수량 자연수놉)");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.(수량 자연수놉)");
        }
    }
}
