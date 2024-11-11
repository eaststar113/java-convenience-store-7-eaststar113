package store;

public class Product {
    private final String name;
    private final int price;
    private int quantity;
    private final String promotion;

    public Product(String name, int price, int quantity, String promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public static Product from(String name, int price, int quantity) {
        return new Product(name, price, quantity, "");
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPromotion() {
        return promotion;
    }

    public boolean hasPromotion() {
        return !(promotion.isEmpty());
    }

    public boolean isOutOfStock() {
        return quantity == 0;
    }

    @Override
    public String toString() {
        String promotionText = "";
        String quantityText = "재고 없음";
        if(promotion != null){
            promotionText = " " + promotion;
        }
        if(quantity != 0){
            quantityText = quantity + "개";
        }
        String formattedPrice = String.format("%,d", price);
        return "- " + name + " " + formattedPrice + "원 " + quantityText + promotionText;
    }
}
