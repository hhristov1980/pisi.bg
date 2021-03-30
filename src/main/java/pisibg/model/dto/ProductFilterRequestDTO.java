package pisibg.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@NoArgsConstructor
@Component
public class ProductFilterRequestDTO {
    int productID;
    int page; // first page Java - 0, from frontEnd - 1
    int productsPerPage;
    int manufacturerId;
    int categoryId;
    int subcategoryId;
    int discountId;

}
