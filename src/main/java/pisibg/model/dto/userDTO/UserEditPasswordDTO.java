package pisibg.model.dto.userDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Component
@NoArgsConstructor
@Getter
@Setter
public class UserEditPasswordDTO {
    @NotNull(message = "Password cannot be null!")
    @NotBlank(message ="Password is mandatory!")
    private String password;
    @NotNull(message = "New password cannot be null!")
    @NotBlank(message ="New password is mandatory!")
    private String newPassword;
    @NotNull(message = "Confirmation password cannot be null!")
    @NotBlank(message ="Confirmation password is mandatory!")
    private String confirmNewPassword;
}
