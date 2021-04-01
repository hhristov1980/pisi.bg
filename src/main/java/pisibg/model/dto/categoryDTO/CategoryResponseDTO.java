package pisibg.model.dto.categoryDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pisibg.model.pojo.Category;
import pisibg.model.pojo.Subcategory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@NoArgsConstructor
@Setter
@Getter
@Component
public class CategoryResponseDTO {
    private int id;
    private String name;
    private TreeSet<Subcategory> subcategories;

    public  CategoryResponseDTO(Category category){
        id = category.getId();
        name = category.getName();
        subcategories = new TreeSet<>(((o1, o2) -> o1.getName().compareTo(o2.getName())));
        subcategories.addAll(category.getSubcategories());
    }

}
