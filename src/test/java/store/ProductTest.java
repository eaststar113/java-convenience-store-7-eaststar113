package store;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {
    @DisplayName("from메서드 사용 시 프로모션이 null인 객체가 반환된다.")
    @Test
    void 프로모션이_널인_객체가_반환된다() {
        final String name = "yi";
        final int price = 5000;
        int quantity = 5;


        Product product = Product.from(name,price,quantity);
        assertAll("Product",
                () -> assertEquals(name, product.getName()),
                () -> assertEquals(price, product.getPrice()),
                () -> assertEquals(quantity, product.getQuantity()),
                () -> assertFalse(product.hasPromotion())
        );
    }
}