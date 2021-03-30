package pisibg.model.pojo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pisibg.model.dto.manufacturerDTO.ManufacturerRequestDTO;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "manufacturer")
public class Manufacturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String producerName;
    @OneToMany(mappedBy = "manufacturer")
    @JsonManagedReference
    private Set<Product> products;

    public Manufacturer(ManufacturerRequestDTO manufacturerRequestDTO) {
        producerName = manufacturerRequestDTO.getProducerName();
    }
}
