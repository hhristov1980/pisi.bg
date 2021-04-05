package pisibg.model.dto.subcategoryDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pisibg.model.pojo.Category;
import pisibg.model.pojo.Subcategory;

@Setter
@Getter
@NoArgsConstructor
@Component
public class SubcategoryResponseDTO {
    int id;
    String name;
    String categoryName;

    public SubcategoryResponseDTO (Subcategory subcategory){
        id = subcategory.getId();
        name = subcategory.getName();
        categoryName = subcategory.getCategory().getName();
    }
}
