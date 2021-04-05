package pisibg.model.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
    @ManyToOne
    @JoinColumn(name = "status_id")
    @JsonBackReference
    private OrderStatus orderStatus;
    @OneToOne(mappedBy = "order")
    private Payment payment;
    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    @JsonBackReference
    private PaymentMethod paymentMethod;
    private String address;
    private LocalDateTime createdAt;
    private BigDecimal grossValue;
    @Column(name = "discount_amount")
    private BigDecimal discount;
    private BigDecimal netValue;
    private boolean isPaid;
    @ManyToMany
    @JoinTable(
            name = "orders_have_products",
            joinColumns = {@JoinColumn(name = "order_id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id")}
    )
    @JsonManagedReference
    private Set<Product> products;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
