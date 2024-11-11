package store;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.Inventory;
import store.domain.Product;

class InventoryTest {
    private Inventory inventory;

    @BeforeEach
    void setUp() throws IOException {
        inventory = new Inventory();
    }

    @DisplayName("상품 목록이 올바르게 로드되는지 확인")
    @Test
    void 상품이_정상적으로_로드된다() throws IOException {
        List<Product> products = inventory.getProducts();

        boolean containsProduct = false;
        for (Product product : products) {
            if (product.getName().equals("사이다")) {
                containsProduct = true;
                break;
            }
        }
        assertTrue(containsProduct);
    }

    @DisplayName("상품 이름이 다르고 프로모션이 있으면 재고 없음인 상품을 추가한다.")
    @Test
    void 재고_없음인_상품을_추가한다() throws IOException {
        List<Product> products = inventory.getProducts();

        int count = 0;
        for (Product product : products) {
            if (product.getName().equals("탄산수")) {
                count++;
            }
        }
        assertEquals(2, count);
    }

    @DisplayName("프로모션이 없는 상품의 이름이 있으면 재고 없음 상품을 추가하지않는다.")
    @Test
    void 재고_없음_상품을_추가하지_않는다() throws IOException {
        List<Product> products = inventory.getProducts();

        int count = 0;
        for (Product product : products) {
            if (product.getName().equals("정식도시락")) {
                count++;
            }
        }

        assertEquals(1, count);
    }
}