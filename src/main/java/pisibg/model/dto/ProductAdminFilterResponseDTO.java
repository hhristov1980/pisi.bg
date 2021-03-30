package pisibg.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@NoArgsConstructor
@Component
public class ProductAdminFilterResponseDTO {
    private int id;
    private String name;
    private String description;
    private String producerName;
    private String categoryName;
    private String subcategoryName;
    private double price;
    private int quantity;
    private int discountPercent;
}
