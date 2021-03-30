package pisibg.model.dto.productDTO;

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
    private int discountId;


}
