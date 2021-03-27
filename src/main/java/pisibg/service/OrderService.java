package pisibg.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.NotFoundException;
import pisibg.model.dto.OrderRequestDTO;
import pisibg.model.dto.OrderResponseDTO;
import pisibg.model.dto.ProductOrderResponseDTO;
import pisibg.model.pojo.Order;
import pisibg.model.pojo.PaymentMethod;
import pisibg.model.repository.OrderRepository;
import pisibg.model.repository.OrderStatusRepository;
import pisibg.model.repository.PaymentMethodRepository;
import pisibg.model.repository.UserRepository;
import pisibg.utility.SessionChecker;
import pisibg.utility.Validator;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;

@Service
public class OrderService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private OrderStatusRepository orderResponseDTO;

    public OrderResponseDTO checkout (OrderRequestDTO orderRequestDTO, HttpSession ses, int userId){
        if(ses.getAttribute("cart")==null){
            throw new NotFoundException("Cart not found!");
        }
        if(!Validator.isValidInteger(orderRequestDTO.getPaymentMethodId())){
            throw new BadRequestException("Please enter number greater than 0");
        }
        if(!paymentMethodRepository.existsById(orderRequestDTO.getPaymentMethodId())){
            throw new NotFoundException("Payment method not found!");
        }
        Order order = new Order();
        String address = orderRequestDTO.getAddress();
        if(address.length()==0){
            address = userRepository.getOne(userId).getAddress();
        }
        order.setUser(userRepository.getOne(userId));
        order.setAddress(address);
        order.setCreatedAt(LocalDateTime.now());
        order.setGrossValue(cartService.calculatePrice(ses).getPriceWithoutDiscount());
        order.setDiscount(cartService.calculatePrice(ses).getDiscountAmount());
        order.setNetValue(cartService.calculatePrice(ses).getPriceAfterDiscount());
        order.setPaymentMethod(paymentMethodRepository.getOne(orderRequestDTO.getPaymentMethodId()));
        order.setOrderStatus(orderResponseDTO.getOne(1)); //status id 1 is PROCESSING
        order.setPaid(false); //DEFAULT VALUE FALSE - TO BE CHANGED FURTHER!
        orderRepository.save(order);
        //CLEAR CART AFTER SUCCESSFUL PAYMENT OR PAYMENT FAIL
        return new OrderResponseDTO(order);
    }


}
