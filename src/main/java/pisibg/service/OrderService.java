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
    public OrderResponseDTO pay(OrderRequestDTO orderRequestDTO, Map<Integer, Queue<ProductOrderResponseDTO>> cart, User user) throws SQLException {
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
                order.setNetValue(order.getGrossValue() - order.getDiscount());
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

    private double calculateGrossPrice(Map<Integer, Queue<ProductOrderResponseDTO>> cart) {

        double price = 0;
        for (Map.Entry<Integer, Queue<ProductOrderResponseDTO>> products : cart.entrySet()) {
            int quantity = products.getValue().size();
            if (quantity > 0) {
                Product product = productRepository.getById(products.getValue().peek().getId());
                double productPrice = product.getPrice();
                price += RoundFloat.round(quantity * productPrice, Constants.TWO_DECIMAL_PLACES);
            }
        }
        return price;
    }

    private double calculateDiscountPrice(Map<Integer, Queue<ProductOrderResponseDTO>> cart, User user) {
        double priceWithoutDiscount = 0;
        double discountAmount = 0;
        for (Map.Entry<Integer, Queue<ProductOrderResponseDTO>> products : cart.entrySet()) {
            int quantity = products.getValue().size();
            if (quantity > 0) {
                Product product = productRepository.getById(products.getValue().peek().getId());
                double productPrice = product.getPrice();
                Discount discount = product.getDiscount();
                int discountPercent = 0;
                if (discount == null) {
                    discountPercent = user.getPersonalDiscount();
                } else {
                    discountPercent = discountRepository.getById(discount.getId()).getPercent();
                }
                priceWithoutDiscount += RoundFloat.round((productPrice * quantity), Constants.TWO_DECIMAL_PLACES);
                discountAmount += RoundFloat.round(productPrice * quantity * (discountPercent * 1.0 / 100), Constants.TWO_DECIMAL_PLACES);
            }
        }
        return discountAmount;
    }

    public Payment addPayment(Order order, int userId) {
        Payment payment = new Payment();
        payment.setCreatedAt(LocalDateTime.now());
        payment.setOrder(order);
        payment.setUser(userRepository.getOne(userId));
        payment.setAmount(order.getNetValue());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("Thread sleep interupted!");
        }
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
