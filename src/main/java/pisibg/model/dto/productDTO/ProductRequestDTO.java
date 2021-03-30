package pisibg.model.dto.productDTO;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pisibg.model.pojo.*;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Component
public class ProductRequestDTO {
    private String name;
    private String description;
    private int manufacturerId;
    private int subcategoryId;
    private int quantity;
    private double price;
    private int discountId;
}
