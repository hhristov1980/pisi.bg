package pisibg.model.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
    @OneToOne(mappedBy="order_statuses")
    private OrderStatus orderStatus;
    @OneToOne
    @JoinColumn(name="payment_method_id")
    private PaymentMethod paymentMethod;
    private String address;
    private LocalDateTime createdAt;
    private double grossValue;
    private double discount;
    private double netValue;
    private boolean isPaid;
    @ManyToMany
    @JoinTable(
            name = "orders_have_products",
            joinColumns = { @JoinColumn(name = "order_id") },
            inverseJoinColumns = { @JoinColumn(name = "product_id") }
    )
    @JsonManagedReference
    private Set<Product> products;
}
