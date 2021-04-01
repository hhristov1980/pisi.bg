package pisibg.model.dto.subcategoryDTO;

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
public class SubCategoryRequestDTO {
    @NotBlank(message = "Subcategory name is mandatory!")
    String name;
    @NotNull(message = "Category id cannot be null!")
    @Min(value=1, message="Minimum category id should be 1")
    Integer categoryId;

}
