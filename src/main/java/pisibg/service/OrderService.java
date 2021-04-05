package pisibg.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.NotFoundException;
import pisibg.exceptions.PaymentFailedException;
import pisibg.model.dao.UserDAO;
import pisibg.model.dto.orderDTO.OrderRequestDTO;
import pisibg.model.dto.orderDTO.OrderResponseDTO;
import pisibg.model.dto.productDTO.ProductOrderResponseDTO;
import pisibg.model.pojo.*;
import pisibg.model.repository.*;
import pisibg.utility.Constants;
import pisibg.utility.RoundFloat;
import pisibg.utility.Validator;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {
    private static final int SUCCESS_CHANCE = 90;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private OrderStatusRepository orderStatusRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private UserDAO userDAO;

    @Transactional
    public OrderResponseDTO pay(OrderRequestDTO orderRequestDTO, Map<Integer, Queue<ProductOrderResponseDTO>> cart, User user) throws SQLException, InterruptedException {
        if (new Random().nextInt(100) < SUCCESS_CHANCE) {
            if (!paymentMethodRepository.existsById(orderRequestDTO.getPaymentMethodId())) {
                throw new NotFoundException("Payment method not found!");
            }
            if (cartService.checkProductsAndRemoveFromDB(cart)) {
                Order order = new Order();
                String address = orderRequestDTO.getAddress();
                if (address.length() == 0) {
                    address = user.getAddress();
                }
                order.setProducts(createProductSet(cart));
                order.setUser(user);
                order.setAddress(address);
                order.setCreatedAt(LocalDateTime.now());
                order.setGrossValue(calculateGrossPrice(cart));
                order.setDiscount(calculateDiscountPrice(cart, user));
                order.setNetValue(order.getGrossValue().subtract(order.getDiscount()).setScale(2,BigDecimal.ROUND_UP));
                Payment payment = addPayment(order, user.getId());
                order.setPaid(true);
                userDAO.updateTurnoverAndPersonalDiscountPercent(order.getNetValue(), user.getId());
                OrderStatus orderStatus = orderStatusRepository.getOne(Constants.FIRST_ORDER_STATUS);
                order.setOrderStatus(orderStatus);
                Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(orderRequestDTO.getPaymentMethodId());
                if (paymentMethod.isPresent()) {
                    order.setPaymentMethod(paymentMethod.get());

                } else {
                    throw new NotFoundException("Payment method not found!");

                }
                orderRepository.save(order);
                paymentRepository.save(payment);
                return new OrderResponseDTO(order, cart);
            } else {

                throw new NotFoundException("Product/s are already out of stock. Please try again with other products!");
            }
        } else {
            throw new PaymentFailedException("Payment failed!");
        }
    }

    private BigDecimal calculateGrossPrice(Map<Integer, Queue<ProductOrderResponseDTO>> cart) {
        BigDecimal price = new BigDecimal(0);
        for (Map.Entry<Integer, Queue<ProductOrderResponseDTO>> products : cart.entrySet()) {
            int quantity = products.getValue().size();
            if (quantity > 0) {
                Product product = productRepository.getById(products.getValue().peek().getId());
                BigDecimal productPrice = new BigDecimal(0);
                productPrice = BigDecimal.valueOf(product.getPrice());
                price = productPrice.multiply(BigDecimal.valueOf(quantity)).setScale(2,BigDecimal.ROUND_UP);
            }
        }
        return price;
    }

    private BigDecimal calculateDiscountPrice(Map<Integer, Queue<ProductOrderResponseDTO>> cart, User user) {
        BigDecimal priceWithoutDiscount = new BigDecimal(0);
        BigDecimal discountAmount = new BigDecimal(0);
        for (Map.Entry<Integer, Queue<ProductOrderResponseDTO>> products : cart.entrySet()) {
            int quantity = products.getValue().size();
            if (quantity > 0) {
                Product product = productRepository.getById(products.getValue().peek().getId());
                BigDecimal productPrice = BigDecimal.valueOf(product.getPrice());
                Discount discount = product.getDiscount();
                int discountPercent = 0;
                if (discount == null) {
                    discountPercent = user.getPersonalDiscount();
                } else {
                    discountPercent = discountRepository.getById(discount.getId()).getPercent();
                }
                priceWithoutDiscount = priceWithoutDiscount.add(productPrice.multiply(BigDecimal.valueOf(quantity)));
                discountAmount = (discountAmount.add(productPrice.multiply(BigDecimal.valueOf(discountPercent))
                        .multiply(BigDecimal.valueOf(quantity/100.0)))).setScale(2,BigDecimal.ROUND_UP);
            }
        }
        return discountAmount;
    }

    public Payment addPayment(Order order, int userId) throws InterruptedException {
        Payment payment = new Payment();
        payment.setCreatedAt(LocalDateTime.now());
        payment.setOrder(order);
        payment.setUser(userRepository.getOne(userId));
        payment.setAmount(order.getNetValue());
        Thread.sleep(2000);
        payment.setProcessedAt(LocalDateTime.now());
        payment.setStatus("OK");
        payment.setTransactionId(payment.transactionIdGenerator());
        return payment;
    }

    private Set<Product> createProductSet(Map<Integer, Queue<ProductOrderResponseDTO>> cartSet) {
        HashSet<Product> productSet = new HashSet<>();
        for (Map.Entry<Integer, Queue<ProductOrderResponseDTO>> p : cartSet.entrySet()) {
            int quantity = p.getValue().size();
            if (quantity > 0) {
                Product product = productRepository.getOne(p.getKey());
                productSet.add(product);
            }
        }
        return productSet;
    }


}
