package pisibg.model.dto.productDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@Component
public class ProductDeleteRequestDTO {
    @NotNull(message = "Product id cannot be null!")
    @Min(value=1, message="Minimum product id should be 1")
    private Integer id;
}
