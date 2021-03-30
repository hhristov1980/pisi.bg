package pisibg.model.dto.productDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@NoArgsConstructor
@Component
public class ProductDeleteRequestDTO {
    private int id;
}
