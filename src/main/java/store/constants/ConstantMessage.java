package store.constants;

public enum ConstantMessage {
    ANSWER_YES("Y"),
    ANSWER_NO("N"),
    REGEX_DASH("-"),
    REGEX_COMMA(","),
    EMPTY_STORAGE("재고 없음"),
    PER_WON("원 "),
    ;

    private final String message;

    ConstantMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }
}
