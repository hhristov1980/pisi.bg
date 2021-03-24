package pisibg.model.pojo;

import java.time.LocalDateTime;

public class Payment {
    private int id;
    private User user;
    private Order order;
    private LocalDateTime createdAt;
    private double amount;
    private String transactionId;
    private LocalDateTime processedAt;
    private String status;
}
