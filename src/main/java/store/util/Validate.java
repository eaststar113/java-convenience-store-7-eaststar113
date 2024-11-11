package store.util;

public class Validate {
    public static String validate(String ans) {
        if (!("Y".equals(ans) || "N".equals(ans))) {
            throw new IllegalArgumentException("잘못된 입력입니다. 'Y' 또는 'N'을 입력해야 합니다.");
        }
        return ans;
    }
}
