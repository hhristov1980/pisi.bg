package pisibg.model.dto;

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
    String name;
    Category category;

    public SubcategoryResponseDTO (Subcategory subcategory){
        name = subcategory.getName();
        category = subcategory.getCategory();
    }
}
