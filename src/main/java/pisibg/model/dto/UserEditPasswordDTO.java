package pisibg.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@Getter
@Setter
public class UserEditPasswordDTO {
    private String password;
    private String newPassword;
    private String confirmNewPassword;
}
