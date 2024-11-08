package store;

public enum InputMessage {
    NEXT_LINE("\n"),
    ORDER_MESSAGE("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])"),
    ;

    private final String message;

    InputMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
