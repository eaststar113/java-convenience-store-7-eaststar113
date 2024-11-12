package store.domain;

import static store.constants.ConstantMessage.REGEX_COMMA;
import static store.constants.ConstantMessage.REGEX_DASH;
import static store.constants.ErrorMessage.INVALID_FORMAT;
import static store.constants.ErrorMessage.ORDER_IS_EMPTY;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Order {

    private Map<String, Integer> orderItems;

    public Order(String order) {
        this.orderItems = new LinkedHashMap<>();
        validate(order);
        parseOrder(order);
    }

    public static void validate(String order) {
        isOrderEmpty(order);
        isValidFormat(order);
    }

    private void parseOrder(String order) {
        List<String> items = List.of(order.split(REGEX_COMMA.getMessage()));
        for (String item : items) {
            String content = item.replaceAll("[\\[\\]]", "").trim();
            List<String> parts = List.of(content.split(REGEX_DASH.getMessage()));
            String productName = parts.get(0).trim();
            int quantity = Integer.parseInt(parts.get(1).trim());
            orderItems.put(productName, quantity);
        }
    }

    private static void isOrderEmpty(String order) {
        if(order == null || order.isEmpty()){
            throw new IllegalArgumentException(ORDER_IS_EMPTY.getMessage());
        }
    }

    private static void isValidFormat(String order) {
        List<String> list = List.of(order.split(REGEX_COMMA.getMessage(), -1));
        for (String item : list) {
            if (!isValidOrder(item.trim())) {
                throw new IllegalArgumentException(INVALID_FORMAT.getMessage());
            }
        }
    }

    private static boolean isValidOrder(String item) {
        beWrappedInParentheses(item);
        String content = item.replaceAll("[\\[\\]]", "");
        List<String> parts = List.of(content.split(REGEX_DASH.getMessage()));
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
            throw new IllegalArgumentException(INVALID_FORMAT.getMessage());
        }
    }

    private static void checkSizeSplitWithDash(List<String> parts) {
        if (parts.size() != 2) {
            throw new IllegalArgumentException(INVALID_FORMAT.getMessage());
        }
    }

    private static void isEmptyProductName(String productName) {
        if(productName.isEmpty()){
            throw new IllegalArgumentException(INVALID_FORMAT.getMessage());
        }
    }

    private static void isValidQuantity(String quantityStr) {
        try {
            int quantity = Integer.parseInt(quantityStr);
            if (quantity < 1) {
                throw new IllegalArgumentException(INVALID_FORMAT.getMessage());
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(INVALID_FORMAT.getMessage());
        }
    }

    public Map<String, Integer> getOrderItems() {
        return orderItems;
    }
}
