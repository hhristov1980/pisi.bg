package pisibg.model.pojo;

import java.time.LocalDateTime;

public class Order {
    private int id;
    private User user;
    private Status status;
    private PaymentMethod paymentMethod;
    private String address;
    private LocalDateTime createdAt;
    private double grossValue;
    private double discount;
    private double netValue;
    private boolean isPayed;
}
