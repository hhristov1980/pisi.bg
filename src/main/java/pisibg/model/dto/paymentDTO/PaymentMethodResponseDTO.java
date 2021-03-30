package pisibg.model.dto.paymentDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pisibg.model.pojo.PaymentMethod;

@NoArgsConstructor
@Setter
@Getter
@Component
public class PaymentMethodResponseDTO {

    private String type;

    public PaymentMethodResponseDTO(PaymentMethod method){
        this.type = method.getType();
    }
}
