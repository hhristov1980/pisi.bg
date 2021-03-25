package pisibg.model.dto;


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

    private String producerName;

    public ManufacturerResponseDTO (Manufacturer manufacturer){
        producerName = manufacturer.getProducerName();
    }
}
