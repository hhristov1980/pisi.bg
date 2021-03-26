package pisibg.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pisibg.model.dto.DiscountRequestDTO;
import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="sale_discounts")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;
    private int percent;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private boolean isActive;

    public Discount(DiscountRequestDTO discountRequestDTO){
        description = discountRequestDTO.getDescription();
        percent = discountRequestDTO.getPercent();
        fromDate = discountRequestDTO.getFromDate();
        toDate = discountRequestDTO.getToDate();
        isActive = discountRequestDTO.isActive();
    }

}
