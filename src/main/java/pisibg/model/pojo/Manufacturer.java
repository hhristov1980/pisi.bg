package pisibg.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pisibg.model.dto.ManufacturerRequestDTO;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="categories")
public class Manufacturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String producerName;

    public Manufacturer(ManufacturerRequestDTO manufacturerRequestDTO){
        producerName = manufacturerRequestDTO.getProducerName();
    }
}
