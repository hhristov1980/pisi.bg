package pisibg.model.dto.productDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@NoArgsConstructor
@Component
public class ProductFilterRequestDTO {
    private int page;
    private int productsPerPage;
    private int manufacturerId;
    private int categoryId;
    private int subcategoryId;
    private Integer quantity;
    private int discountId;
    @JsonProperty(value = "isAsc")
    private Boolean isAsc;
    private String keyword;

}
