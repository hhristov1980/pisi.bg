package pisibg.model.dto.subcategoryDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@NoArgsConstructor
@Component
public class SubCategoryRequestDTO {
    String name;
    int categoryId;

}
