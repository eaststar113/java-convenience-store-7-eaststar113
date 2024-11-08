package store.view;

import static store.InputMessage.NEXT_LINE;
import static store.InputMessage.ORDER_MESSAGE;

import camp.nextstep.edu.missionutils.Console;

public class InPutView {

    public static String getOrder() {
        System.out.println(NEXT_LINE.getMessage()+ORDER_MESSAGE.getMessage());
        return Console.readLine();
    }
}
