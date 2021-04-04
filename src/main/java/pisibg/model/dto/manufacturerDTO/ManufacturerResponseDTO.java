package pisibg.model.dto.manufacturerDTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pisibg.model.pojo.Manufacturer;

@NoArgsConstructor
@Setter
@Getter
@Component
public class ManufacturerResponseDTO {

    private int id;
    private String producerName;

    public ManufacturerResponseDTO (Manufacturer manufacturer){
        id = manufacturer.getId();
        producerName = manufacturer.getProducerName();
    }
}
