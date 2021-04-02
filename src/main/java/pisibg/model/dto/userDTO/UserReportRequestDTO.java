package pisibg.model.dto.userDTO;

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
public class UserReportRequestDTO {
    @NotNull(message = "Page cannot be null!")
    @Min(value=1, message="Min page value should be 1")
    int page;
    @NotNull(message = "Users per page cannot be null!")
    @Min(value=1, message="Min users per page value should be 1")
    int usersPerPage;
}
