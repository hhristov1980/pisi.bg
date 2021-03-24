package pisibg.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
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
    private double turnover;
    private int personalDiscount;
    private String townName;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private boolean isSubscribed;
    private boolean isAdmin;
}
