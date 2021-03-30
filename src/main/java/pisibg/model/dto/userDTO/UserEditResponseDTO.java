package pisibg.model.dto.userDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pisibg.model.pojo.User;

import java.time.LocalDateTime;
@Component
@Getter
@Setter
@NoArgsConstructor
public class UserEditResponseDTO {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String townName;
    private String address;
    @JsonProperty(value = "isSubscribed")
    private boolean isSubscribed;


    public UserEditResponseDTO(User user){
        id = user.getId();
        email = user.getEmail();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        phoneNumber = user.getPhoneNumber();
        townName = user.getTownName();
        address = user.getAddress();
        isSubscribed = user.isSubscribed();
    }
}
