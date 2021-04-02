package pisibg.model.dto.userDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Component
public class UserRegisterRequestDTO {
    @NotNull(message = "Email cannot be null!")
    @NotBlank(message ="Email is mandatory!")
    private String email;
    @NotNull(message = "Password cannot be null!")
    @NotBlank(message ="Password is mandatory!")
    private String password;
    @NotNull(message = "Confirmation password cannot be null!")
    @NotBlank(message ="Confirmation password is mandatory!")
    private String confirmPassword;
    @NotNull(message = "First name cannot be null!")
    @NotBlank(message ="First name is mandatory!")
    private String firstName;
    @NotNull(message = "Last name cannot be null!")
    @NotBlank(message ="Last name is mandatory!")
    private String lastName;
    @NotNull(message = "Phone number cannot be null!")
    @NotBlank(message ="Phone number is mandatory!")
    private String phoneNumber;
    @NotNull(message = "Town name cannot be null!")
    @NotBlank(message ="Town name is mandatory!")
    private String townName;
    @NotNull(message = "Address cannot be null!")
    @NotBlank(message ="Address is mandatory!")
    private String address;
    @JsonProperty(value = "isSubscribed")
    private boolean isSubscribed;
}
