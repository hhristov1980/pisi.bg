package pisibg.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pisibg.model.dto.OrderStatusRequestDTO;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="order_statuses")
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String type;
    @OneToOne
    @JoinColumn(name="status_id")
    private Order order;

    public OrderStatus(OrderStatusRequestDTO status){
        this.type = status.getType();
    }
}
