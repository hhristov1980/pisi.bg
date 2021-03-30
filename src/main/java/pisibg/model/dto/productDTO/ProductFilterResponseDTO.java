package pisibg.model.dto.productDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@NoArgsConstructor
@Component
public class ProductFilterResponseDTO {
    private int id;
    private String name;
    private String description;
    private String producerName;
    private String categoryName;
    private String subcategoryName;
    private double price;
    private int discountPercent;

}
