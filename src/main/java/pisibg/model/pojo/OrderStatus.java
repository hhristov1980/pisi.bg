package pisibg.model.pojo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pisibg.model.dto.orderDTO.OrderStatusRequestDTO;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "order_statuses")
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String type;
    @OneToMany(mappedBy = "orderStatus")
    @JsonManagedReference
    private List<Order> order;

    public OrderStatus(OrderStatusRequestDTO status) {
        this.type = status.getType();
    }
}
