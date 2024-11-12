package store.constants;

public enum ErrorMessage {
    PRODUCT_DOESNT_EXIST("[ERROR] 상품명이 목록에 존재하지 않습니다. 다시 입력해 주세요."),
    QUANTITY_OVER_PRODUCT_QUANTITY("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    ORDER_IS_EMPTY("[ERROR] 입력값이 비어있습니다. 다시 입력해 주세요."),
    INVALID_FORMAT("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    WRONG_ANSWER("잘못된 입력입니다. 'Y' 또는 'N'을 입력해야 합니다."),
    ;

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }
}
