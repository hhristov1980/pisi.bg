package pisibg.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@NoArgsConstructor
@Component
public class OrderMonthlyReportRequestDTO {
    int month;
    int page;
    int ordersPerPage;
}
