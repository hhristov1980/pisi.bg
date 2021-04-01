package pisibg.model.dto.categoryDTO;

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
public class CategoryEditRequestDTO {
    @NotNull(message = "id cannot be null!")
    @Min(value=1, message="Minimum category id should be 1")
    private Integer id;
    @NotBlank(message = "Current category name is mandatory!")
    private String currentCategoryName;
    @NotBlank(message = "New category name is mandatory!")
    private String newCategoryName;
}
