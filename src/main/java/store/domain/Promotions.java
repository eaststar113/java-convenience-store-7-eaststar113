package store.domain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.util.Parser;

public class Promotions {

    private static List<Promotion> promotions;

    public Promotions() throws IOException {
        this.promotions = new ArrayList<>();
        loadPromotions("src/main/resources/promotions.md");
    }

    public void loadPromotions(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        for (int i = 1; i < lines.size(); i++) {
            List<String> fields = Parser.getNextLine(lines, i);
            promotions.add(createPromotion(fields));
        }
    }

    private Promotion createPromotion(List<String> fields) {
        String name = fields.get(0);
        int buy = Integer.parseInt(fields.get(1));
        int get = Integer.parseInt(fields.get(2));
        LocalDate startDate = LocalDate.parse(fields.get(3));
        LocalDate endDate = LocalDate.parse(fields.get(4));

        return new Promotion(name, buy, get, startDate, endDate);
    }

    public Promotion findPromotion(String promotionName){
        for(Promotion promotion : promotions){
            if(promotionName!=null&&promotionName.equals(promotion.getName())){
                return promotion;
            }
        }
        return null;
    }

    public static List<Promotion> getPromotions() {
        return promotions;
    }
}

