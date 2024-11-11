package store.util;

import static store.constants.ErrorMessage.WRONG_ANSWER;

public class Validate {
    public static String validate(String ans) {
        if (!("Y".equals(ans) || "N".equals(ans))) {
            throw new IllegalArgumentException(WRONG_ANSWER.getMessage());
        }
        return ans;
    }
}
