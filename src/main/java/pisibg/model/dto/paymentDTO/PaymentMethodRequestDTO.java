package pisibg.model.dto.paymentDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;


@NoArgsConstructor
@Setter
@Getter
@Component
public class PaymentMethodRequestDTO {
    @NotBlank(message = "Payment method type is mandatory field!")
    private String type;

}
