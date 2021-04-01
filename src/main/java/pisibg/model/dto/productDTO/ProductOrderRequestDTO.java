package pisibg.model.dto.productDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Setter
@Getter
@Component
public class ProductOrderRequestDTO {
    @NotNull(message = "Product id cannot be null!")
    @Min(value=1, message="Minimum product id should be 1")
    private Integer id;
    @NotNull(message = "Quantity cannot be null!")
    @Min(value=1, message="Minimum quantity should be 1")
    private Integer quantity;
}
