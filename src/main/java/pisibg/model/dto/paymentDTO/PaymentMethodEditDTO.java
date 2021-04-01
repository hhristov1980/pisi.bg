package pisibg.model.dto.paymentDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Setter
@Getter
@Component
public class PaymentMethodEditDTO {
    @NotNull(message = "id cannot be null!")
    @Min(value=1, message="Minimum payment method id should be 1")
    private Integer id;
    @NotBlank(message = "Current payment method type is mandatory field!")
    private String type;
    @NotBlank(message = "New payment method type is mandatory field!")
    private String newType;
}