package pisibg.model.dto.productDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Setter
@Getter
@NoArgsConstructor
@Component
public class ProductRequestDTO {
    @NotBlank(message = "Product name is mandatory!")
    private String name;
    @NotBlank(message = "Product description is mandatory!")
    private String description;
    @NotNull(message = "Manufacturer id cannot be null!")
    @Min(value=1, message="Manufacturer product id should be 1")
    private Integer manufacturerId;
    @NotNull(message = "Subcategory id cannot be null!")
    @Min(value=1, message="Minimum Subcategory id should be 1")
    private Integer subcategoryId;
    @NotNull(message = "Quantity cannot be null!")
    private Integer quantity;
    @NotNull(message = "Price cannot be null!")
    private Double price;
    private Integer discountId;
}
