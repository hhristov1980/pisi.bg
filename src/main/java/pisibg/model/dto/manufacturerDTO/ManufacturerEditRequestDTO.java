package pisibg.model.dto.manufacturerDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@Component
public class ManufacturerEditRequestDTO {
    @NotNull(message = "id cannot be null!")
    @Min(value=1, message="Minimum manufacturer id should be 1")
    private Integer id;
    @NotBlank(message = "Current manufacturer name is mandatory!")
    private String currentProducerName;
    @NotBlank(message = "New manufacturer name is mandatory!")
    private String newProducerName;
}
