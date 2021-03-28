package pisibg.model.pojo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import pisibg.model.dto.UserRegisterRequestDTO;

import javax.persistence.*;
import java.time.LocalDateTime;
<<<<<<< HEAD
import java.util.List;
=======
import java.util.Set;
>>>>>>> bbf5c713be89b5b4ebd6df88cbddf7f3eeaaa085

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="users")
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
<<<<<<< HEAD
//    @OneToMany(mappedBy="user")
//    private List<Payment> payments;
=======
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<Order> orders;
>>>>>>> bbf5c713be89b5b4ebd6df88cbddf7f3eeaaa085

    public User(UserRegisterRequestDTO userDTO){
        email = userDTO.getEmail();
        password = userDTO.getPassword();
        firstName = userDTO.getFirstName();
        lastName = userDTO.getLastName();
        phoneNumber  = userDTO.getPhoneNumber();
        townName = userDTO.getTownName();
        address = userDTO.getAddress();
        createdAt = LocalDateTime.now();
        isSubscribed = userDTO.isSubscribed();
        personalDiscount=5;
        isAdmin = false;
    }
}
