package pisibg.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Component
public class UserRegisterRequestDTO {

    private String email;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String townName;
    private String address;
    @JsonProperty(value = "isSubscribed")
    private boolean isSubscribed;
}
