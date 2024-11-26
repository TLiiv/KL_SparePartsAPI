package tliiv.dev.SpareParts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpareParts {
    private String seriesNumber;
    private String productName;
    private int stock1;
    private int stock2;
    private int stock3;
    private int stock4;
    private int stock5;
    private int stock6;
    private double price;
    private String partClass;
    private double priceWithTax;

}
