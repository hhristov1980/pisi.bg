package pisibg.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pisibg.model.pojo.Category;

@NoArgsConstructor
@Setter
@Getter
@Component
public class CategoryResponseDTO {

    private String name;

    public  CategoryResponseDTO(Category category){
        name = category.getName();
    }

}
