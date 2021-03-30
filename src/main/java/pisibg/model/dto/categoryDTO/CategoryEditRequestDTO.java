package pisibg.model.dto.categoryDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@NoArgsConstructor
@Component
public class CategoryEditRequestDTO {
    private int id;
    private String currentCategoryName;
    private String newCategoryName;
}
