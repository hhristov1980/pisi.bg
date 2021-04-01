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
public class SubCategoryEditRequestDTO {
    @NotNull(message = "Subcategory id cannot be null!")
    @Min(value=1, message="Minimum subcategory id should be 1")
    private Integer id;
    @NotBlank(message = "Subcategory current name is mandatory!")
    private String currentSubcategoryName;
    @NotBlank(message = "Subcategory new name is mandatory!")
    private String newSubcategoryName;
    @NotNull(message = "Current category id cannot be null!")
    @Min(value=1, message="Minimum current category id should be 1")
    private Integer currentCategory_id;
    @NotNull(message = "New category id cannot be null!")
    @Min(value=1, message="Minimum new category id should be 1")
    private Integer newCategory_id;

}
