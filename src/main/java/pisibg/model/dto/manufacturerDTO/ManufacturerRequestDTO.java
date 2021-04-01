package pisibg.model.dto.manufacturerDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@NoArgsConstructor
@Component
public class ManufacturerRequestDTO{
    @NotBlank(message = "Manufacturer name is mandatory!")
    private String producerName;
}
