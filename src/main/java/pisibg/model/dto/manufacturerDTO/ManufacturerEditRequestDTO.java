package pisibg.model.dto.manufacturerDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@NoArgsConstructor
@Component
public class ManufacturerEditRequestDTO {
    private int id;
    private String currentProducerName;
    private String newProducerName;
}
