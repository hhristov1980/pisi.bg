package pisibg.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pisibg.model.pojo.Product;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Component
public class CartPriceResponseDTO {
    private Set<Product> products;
    private double priceWithoutDiscount;
    private double discountAmount;
    private double priceAfterDiscount;

}
