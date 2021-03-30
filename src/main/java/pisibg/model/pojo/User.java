package pisibg.model.pojo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pisibg.model.dto.userDTO.UserRegisterRequestDTO;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;


@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    @Transient
    private double turnover;
    private int personalDiscount;
    private String townName;
    private String address;
    private LocalDateTime createdAt;
    @Transient
    private LocalDateTime deletedAt;
    private boolean isSubscribed;
    private boolean isAdmin;
    @OneToMany(mappedBy = "user")
    private Set<Payment> payments;
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<Order> orders;

    public User(UserRegisterRequestDTO userDTO) {
        email = userDTO.getEmail();
        password = userDTO.getPassword();
        firstName = userDTO.getFirstName();
        lastName = userDTO.getLastName();
        phoneNumber = userDTO.getPhoneNumber();
        townName = userDTO.getTownName();
        address = userDTO.getAddress();
        createdAt = LocalDateTime.now();
        isSubscribed = userDTO.isSubscribed();
        personalDiscount = 5;
        isAdmin = false;
    }
}
