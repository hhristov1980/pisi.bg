package pisibg.model.dto.orderDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@NoArgsConstructor
@Component
public class OrderRequestDTO {
    private String address;
    private int paymentMethodId;
}
