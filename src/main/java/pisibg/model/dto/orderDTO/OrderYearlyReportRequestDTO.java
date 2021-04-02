package pisibg.model.dto.orderDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@Component
public class OrderYearlyReportRequestDTO {
    @NotNull(message = "Year cannot be null!")
    @Min(value=1, message="Min year value should be 1")
    int year;
    @NotNull(message = "Page cannot be null!")
    @Min(value=1, message="Min page value should be 1")
    int page;
    @NotNull(message = "Orders per page cannot be null!")
    @Min(value=1, message="Min orders per page value should be 1")
    int ordersPerPage;
}
