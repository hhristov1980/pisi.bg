package pisibg.model.dto.orderDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pisibg.model.pojo.OrderStatus;

import javax.persistence.JoinColumn;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@Component
public class OrderEditRequestDTO {
    @NotNull(message = "Id cannot be null!")
    @Min(value=1, message="Min order id value should be 1")
    private int id;
    @Min(value=1, message="Min order status id value should be 1")
    private int orderStatusId;
    private String address;
    @Min(value=1, message="Min gross value value should be 1")
    private BigDecimal grossValue;
    @Min(value=1, message="Min discount value should be 1")
    private BigDecimal discount;
    @Min(value=1, message="Min net value should be 1")
    private BigDecimal netValue;
    @JsonProperty(value = "isPaid")
    private boolean isPaid;
}

