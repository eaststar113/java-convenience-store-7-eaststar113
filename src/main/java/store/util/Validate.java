package store.util;

import static store.constants.ConstantMessage.ANSWER_NO;
import static store.constants.ConstantMessage.ANSWER_YES;
import static store.constants.ErrorMessage.WRONG_ANSWER;

public class Validate {
    public static String validate(String ans) {
        if (!(ANSWER_YES.getMessage().equals(ans) || ANSWER_NO.getMessage().equals(ans))) {
            throw new IllegalArgumentException(WRONG_ANSWER.getMessage());
        }
        return ans;
    }
}
