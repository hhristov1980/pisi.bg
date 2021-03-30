package pisibg.model.dto.subcategoryDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@NoArgsConstructor
@Component
public class SubCategoryEditRequestDTO {
    private int id;
    private String currentSubcategoryName;
    private String newSubcategoryName;
    private int currentCategory_id;
    private int newCategory_id;

}
