package store.util;

import static store.constants.ConstantMessage.REGEX_COMMA;

import java.util.Arrays;
import java.util.List;

public class Parser {
    public static List<String> getNextLine(List<String> lines, int i) {
        String line = lines.get(i);
        return Arrays.asList(line.split(REGEX_COMMA.getMessage()));
    }
}
