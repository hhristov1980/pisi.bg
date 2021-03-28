package pisibg.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Setter
@Getter
@Component
public class ProductEditRequestDTO {
    private int id;
    private String currentName;
    private String newName;
    private String currentDescription;
    private String newDescription;
    private int currentManufacturerId;
    private int newManufacturerId;
    private int currentSubcategoryId;
    private int newSubcategoryId;
    private int currentQuantity;
    private int newQuantity;
    private double currentPrice;
    private double newPrice;
    private int currentDiscountId;
    private int newDiscountId;
}
