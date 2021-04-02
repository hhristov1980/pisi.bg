package pisibg.model.dto.userDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Component
@Getter
@Setter
@NoArgsConstructor
public class UserEditRequestDTO {
    private String email;
    @NotNull(message = "Password cannot be null!")
    @NotBlank(message ="Password is mandatory!")
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String townName;
    private String address;
    @JsonProperty(value = "isSubscribed")
    private boolean isSubscribed;
}
