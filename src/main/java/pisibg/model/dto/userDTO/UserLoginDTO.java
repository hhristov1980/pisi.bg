package pisibg.model.dto.userDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Setter
@Getter
@Component
public class UserLoginDTO {
    @NotNull(message = "Email cannot be null!")
    @NotBlank(message ="Email is mandatory!")
    private String email;
    @NotNull(message = "Password cannot be null!")
    @NotBlank(message ="Password is mandatory!")
    private String password;
}
