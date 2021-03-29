package pisibg.service;


import com.sun.xml.bind.v2.TODO;
import org.aspectj.weaver.ast.Or;
import org.hibernate.procedure.spi.ParameterRegistrationImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.NotFoundException;
import pisibg.exceptions.PaymentFailedException;
import pisibg.model.dto.CartPriceResponseDTO;
import pisibg.model.dto.OrderRequestDTO;
import pisibg.model.dto.OrderResponseDTO;
import pisibg.model.dto.ProductOrderResponseDTO;
import pisibg.model.pojo.*;
import pisibg.model.repository.*;
import pisibg.utility.Constants;
import pisibg.utility.RoundFloat;
import pisibg.utility.Validator;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
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

    @Transactional
    public OrderResponseDTO pay(OrderRequestDTO orderRequestDTO, Map<Integer, Queue<ProductOrderResponseDTO>> cart, User user) {
        if (new Random().nextInt(100) < SUCCESS_CHANCE) {
            if (!Validator.isValidInteger(orderRequestDTO.getPaymentMethodId())) {
                throw new BadRequestException("Please enter number greater than 0");
            }
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
                order.setDiscount(calculateDiscountPrice(cart,user));
                order.setNetValue(order.getGrossValue()-order.getDiscount());
                Payment payment = addPayment(order, user.getId());
                order.setPaid(true);
                updateTurnoverAndPersonalDiscountPercent(order.getNetValue(), user.getId());
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
                Product product = productRepository.findById(products.getValue().peek().getId());
                double productPrice = product.getPrice();
                price += RoundFloat.round(quantity * productPrice,Constants.TWO_DECIMAL_PLACES);
            }
        }
        return price;
    }

    private double calculateDiscountPrice(Map<Integer, Queue<ProductOrderResponseDTO>> cart, User user) {
        double priceWithoutDiscount = 0;
        double discountAmount = 0;
        for(Map.Entry<Integer, Queue<ProductOrderResponseDTO>> products: cart.entrySet()){
            int quantity = products.getValue().size();
            if(quantity>0){
                Product product = productRepository.findById(products.getValue().peek().getId());
                double productPrice = product.getPrice();
                Discount discount = product.getDiscount();
                int discountPercent = 0;
                if(discount == null){
                    discountPercent = user.getPersonalDiscount();
                }
                else {
                    discountPercent = discountRepository.findById(discount.getId()).getPercent();
                }
                priceWithoutDiscount+= RoundFloat.round((productPrice*quantity), Constants.TWO_DECIMAL_PLACES);
                discountAmount+=RoundFloat.round(productPrice*quantity*(discountPercent*1.0/100),Constants.TWO_DECIMAL_PLACES);
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

    private void updateTurnoverAndPersonalDiscountPercent(double orderAmount, int userId) {
        User user = userRepository.getOne(userId);
        double currentTurnover = user.getTurnover();
        double newTurnover = currentTurnover + orderAmount;
        user.setTurnover(newTurnover);
        //put comment here
        int coefficientTurnoverToIncreaseStep = (int) (newTurnover / Constants.DISCOUNT_INCREASE_TURNOVER_STEP);
        int currentPersonalDiscountPercent = user.getPersonalDiscount();
        if (currentPersonalDiscountPercent + coefficientTurnoverToIncreaseStep <= Constants.MAX_PERSONAL_DISCOUNT_PERCENT) {
            int newPersonalDiscountPercent = currentPersonalDiscountPercent + coefficientTurnoverToIncreaseStep;
            user.setPersonalDiscount(newPersonalDiscountPercent);
        }
        userRepository.save(user);

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
