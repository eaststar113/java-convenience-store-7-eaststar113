package store.domain;

import static store.constants.ErrorMessage.PRODUCT_DOESNT_EXIST;
import static store.constants.ErrorMessage.QUANTITY_OVER_PRODUCT_QUANTITY;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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
            throw new IllegalArgumentException(PRODUCT_DOESNT_EXIST.getMessage());
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
            throw new IllegalArgumentException(QUANTITY_OVER_PRODUCT_QUANTITY.getMessage());
        }
    }

    public static Product checkOrderIsPromotion(String productName) {
        for (Product product : products) {
            if (productName.equals(product.getName()) && product.hasPromotion()) {
                return product;
            }
        }
        return null;
    }

    public static Product checkOrderIsNoPromotion(String productName) {
        for (Product product : products) {
            if (productName.equals(product.getName()) && !product.hasPromotion()) {
                return product;
            }
        }
        return null;
    }

    public static void decreasePromotionProduct(Product product, String productName,int quantity){
        int rest = product.getQuantity() - quantity;
        if(rest<0){
            product.setQuantity(0);
            Product nonPromotionProduct = checkOrderIsNoPromotion(productName);
            int absoluteRest = -1 * rest;
            Product.reduce(nonPromotionProduct,absoluteRest);
            return ;
        }
        Product.reduce(product,quantity);
    }

    public static void decreaseNoPromotionProduct(String productName,int quantity){
        Product nonPromotionProduct = checkOrderIsNoPromotion(productName);
        int rest = nonPromotionProduct.getQuantity() - quantity;
        if(rest<0){
            nonPromotionProduct.setQuantity(0);
            Product hasProduct = checkOrderIsPromotion(productName);
            int absoluteRest = -1 * rest;
            Product.reduce(hasProduct,absoluteRest);
        }
        Product.reduce(nonPromotionProduct,quantity);
    }

    public static List<Product> getProducts() {
        return products;
    }
}
