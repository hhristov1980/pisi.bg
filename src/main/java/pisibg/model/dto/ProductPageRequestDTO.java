package pisibg.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@NoArgsConstructor
@Component
public class ProductPageRequestDTO {
    int productID;
    int page; // first page - 0
    int productsPerPage;

}
