package store;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import store.domain.Product;
import store.util.Parser;

public class Inventory {

    private static List<Product> products;

    public Inventory() throws IOException {
        this.products = new ArrayList<>();
        loadProducts("src/main/resources/products.md");
    }

    public void loadProducts(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        for (int i = 1; i < lines.size(); i++) {
            List<String> fields = Parser.getNextLine(lines, i);
            String name = fields.getFirst();
            if (!products.isEmpty()) {
                checkAndAddOutOfStockProduct(name);
            }
            products.add(createProduct(fields));
        }
    }

    private Product createProduct(List<String> fields) {
        String name = fields.get(0);
        int price = Integer.parseInt(fields.get(1));
        int quantity = Integer.parseInt(fields.get(2));
        String promotion = "";

        if (!fields.get(3).equals("null")) {
            promotion = fields.get(3);
        }
        return new Product(name, price, quantity, promotion);
    }

    private void checkAndAddOutOfStockProduct(String name) {
        Product lastProduct = products.getLast();
        if (!lastProduct.getName().equals(name) && lastProduct.hasPromotion()) {
            Product outOfStockProduct = Product.from(lastProduct.getName(), lastProduct.getPrice(), 0);
            products.add(outOfStockProduct);
        }
    }

    public static void isProductExist(String productName) {
        boolean productFound = false;
        for (Product product : products) {
            if (product.getName().equals(productName)) {
                productFound = true;
                break;
            }
        }
        if (!productFound) {
            throw new IllegalArgumentException("[ERROR] 상품명이 목록에 존재하지 않습니다. 다시 입력해 주세요.");
        }
    }

    public static void isStockSufficient(String productName, int quantity) {
        int sumCount = 0;

        for (Product product : products) {
            if (product.getName().equals(productName)) {
                sumCount += product.getQuantity();
            }
        }
        if (quantity > sumCount) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
    }

    public Product checkOrderIsPromotion(String productName) {
        for (Product product : products) {
            if (productName.equals(product.getName()) && product.hasPromotion()) {
                return product;
            }
        }
        return null;
    }

    public Product checkOrderIsNoPromotion(String productName) {
        for (Product product : products) {
            if (productName.equals(product.getName()) && !product.hasPromotion()) {
                return product;
            }
        }
        return null;
    }

    public static List<Product> getProducts() {
        return products;
    }
}
