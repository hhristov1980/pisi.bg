package pisibg.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Random;

//@NoArgsConstructor
//@Setter
//@Getter
//@Entity
//@Table(name = "payments")
//public class Payment {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//    @ManyToOne
//    @JoinColumn(name="user_id", nullable=false)
//    private User user;
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "order_id", referencedColumnName = "id")
//    private Order order;
//    private LocalDateTime createdAt;
//    private double amount;
//    private String transactionId;
//    private LocalDateTime processedAt;
//    private String status;
//
//
//    protected String getSaltString() {
//        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
//        StringBuilder salt = new StringBuilder();
//        Random rnd = new Random();
//        while (salt.length() < 18) { // length of the random string.
//            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
//            salt.append(SALTCHARS.charAt(index));
//        }
//        String saltStr = salt.toString();
//        return saltStr;
//    }
//}
