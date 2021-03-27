package pisibg.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@NoArgsConstructor
@Component
public class CartPriceResponseDTO {
    private double priceWithoutDiscount;
    private double discountAmount;
    private double priceAfterDiscount;

}
