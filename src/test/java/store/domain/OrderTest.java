package store.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.constants.ErrorMessage;

class OrderTest {
    @DisplayName("빈 주문이 주어지면 예외가 발생한다.")
    @Test
    void 빈_주문_예외_발생() {
        String order = "";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Order(order);
        });
        assertEquals(ErrorMessage.ORDER_IS_EMPTY.getMessage(), exception.getMessage());
    }
}