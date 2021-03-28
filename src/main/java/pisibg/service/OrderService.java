package pisibg.service;


import com.sun.xml.bind.v2.TODO;
import org.hibernate.procedure.spi.ParameterRegistrationImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.NotFoundException;
import pisibg.exceptions.PaymentFailedException;
import pisibg.model.dto.CartPriceResponseDTO;
import pisibg.model.dto.OrderRequestDTO;
import pisibg.model.dto.OrderResponseDTO;
import pisibg.model.pojo.Order;
import pisibg.model.pojo.Payment;
import pisibg.model.pojo.User;
import pisibg.model.repository.*;
import pisibg.utility.Constants;
import pisibg.utility.Validator;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OrderService {
    private static final int SUCCESS_CHANCE=99;
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

    public OrderResponseDTO pay(OrderRequestDTO orderRequestDTO, HttpSession ses, int userId){
        if(new Random().nextInt(100)<SUCCESS_CHANCE) {
            if (ses.getAttribute("cart") == null) {
                throw new NotFoundException("Cart not found!");
            }
            if (!Validator.isValidInteger(orderRequestDTO.getPaymentMethodId())) {
                throw new BadRequestException("Please enter number greater than 0");
            }
            if (!paymentMethodRepository.existsById(orderRequestDTO.getPaymentMethodId())) {
                throw new NotFoundException("Payment method not found!");
            }
            Order order = new Order();
            String address = orderRequestDTO.getAddress();
            if (address.length() == 0) {
                address = userRepository.getOne(userId).getAddress();
            }
            CartPriceResponseDTO cart = cartService.checkout(ses);
            order.setProducts(cart.getProducts());
            order.setUser(userRepository.getOne(userId));
            order.setAddress(address);
            order.setCreatedAt(LocalDateTime.now());
            order.setGrossValue(cartService.checkout(ses).getPriceWithoutDiscount());
            order.setDiscount(cartService.checkout(ses).getDiscountAmount());
            order.setNetValue(cartService.checkout(ses).getPriceAfterDiscount());
            order.setPaymentMethod(paymentMethodRepository.getOne(orderRequestDTO.getPaymentMethodId()));
            order.setOrderStatus(orderStatusRepository.getOne(1)); //status id 1 is PROCESSING
            order.setPaid(false); //DEFAULT VALUE FALSE - TO BE CHANGED FURTHER!
            orderRepository.save(order);
            Payment payment = new Payment();
            payment.setCreatedAt(LocalDateTime.now());
            payment.setOrder(order);
            payment.setUser(userRepository.getOne(userId));
            payment.setAmount(order.getNetValue());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                //TODO
            }
            payment.setProcessedAt(LocalDateTime.now());
            payment.setStatus("OK");
            payment.setTransactionId(payment.transactionIdGenerator());
            paymentRepository.save(payment);
            updateTurnoverAndPersonalDiscountPercent(order.getNetValue(),userId);
            cartService.emptyCart(ses);
            return new OrderResponseDTO(order);
        }
        else {
            cartService.emptyCart(ses);
            throw new PaymentFailedException("Payment failed!");
        }
    }

    private void updateTurnoverAndPersonalDiscountPercent(double orderAmount, int userId){
        User user = userRepository.getOne(userId);
        double currentTurnover = user.getTurnover();
        double newTurnover = currentTurnover+orderAmount;
        user.setTurnover(newTurnover);
        //put comment here
        int coefficientTurnoverToIncreaseStep = (int) (newTurnover/ Constants.DISCOUNT_INCREASE_TURNOVER_STEP);
        int currentPersonalDiscountPercent = user.getPersonalDiscount();
        if(currentPersonalDiscountPercent+coefficientTurnoverToIncreaseStep<=Constants.MAX_PERSONAL_DISCOUNT_PERCENT){
            int newPersonalDiscountPercent = currentPersonalDiscountPercent+coefficientTurnoverToIncreaseStep;
            user.setPersonalDiscount(newPersonalDiscountPercent);
        }
        userRepository.save(user);

    }



}
