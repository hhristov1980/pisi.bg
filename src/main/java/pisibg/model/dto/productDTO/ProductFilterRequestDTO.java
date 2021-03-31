package pisibg.model.dto.productDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.Nullable;
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
    //@Nullable
    private int manufacturerId;
    @Nullable
    private int categoryId;
    @Nullable
    private int subcategoryId;
    @Nullable
    private Integer quantity;
    @Nullable
    private int discountId;
    @Nullable
    @JsonProperty(value = "isAsc")
    private Boolean isAsc;
    @Nullable
    private String keyword;

}
