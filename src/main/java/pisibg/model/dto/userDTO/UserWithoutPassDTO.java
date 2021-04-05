package pisibg.model.dto.userDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pisibg.model.pojo.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
@Component
public class UserWithoutPassDTO {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private BigDecimal turnover;
    private int personalDiscount;
    private String townName;
    private String address;
    private LocalDateTime createdAt;
    @JsonProperty(value = "isSubscribed")
    private boolean isSubscribed;

    public UserWithoutPassDTO(User user){
        id = user.getId();
        email = user.getEmail();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        phoneNumber = user.getPhoneNumber();
        turnover = user.getTurnover();
        personalDiscount = user.getPersonalDiscount();
        townName = user.getTownName();
        address = user.getAddress();
        isSubscribed = user.isSubscribed();
        createdAt = user.getCreatedAt();
    }
}
