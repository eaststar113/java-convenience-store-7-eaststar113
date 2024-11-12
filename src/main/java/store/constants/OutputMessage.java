package store.constants;

public enum OutputMessage {
    NEXT_LINE("\n"),
    WELCOME_MESSAGE("안녕하세요. W편의점입니다."),
    INTRODUCE_PRODUCT_MESSAGE("현재 보유하고 있는 상품입니다."),
    STORE_MESSAGE("==============W 편의점================"),
    CATEGORY_MESSAGE("상품명\t\t수량\t금액"),
    BENEFIT_MESSAGE("=============증\t정==============="),
    DISTINCT_LINE_MESSAGE("===================================="),
    TOTAL_PRICE_MESSAGE("총구매액\t\t%d\t%,d"),
    DISCOUNT_PRICE_MESSAGE("행사할인\t\t\t\t-%,d"),
    MEMBERSHIP_PRICE_MESSAGE("멤버십 할인\t\t\t\t-%,d"),
    YOU_PAY_PRICE_MESSAGE("내실돈\t\t\t\t%,d"),
    ;

    private final String message;

    OutputMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }
}
