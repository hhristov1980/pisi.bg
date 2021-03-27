package pisibg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.exceptions.AuthenticationException;
import pisibg.exceptions.BadRequestException;
import pisibg.model.dto.*;
import pisibg.model.pojo.User;
import pisibg.service.OrderStatusService;
import pisibg.service.PaymentMethodService;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class PaymentMethodController {
    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private PaymentMethodService paymentService;

    @PostMapping("/users/{id}/paymentmethod")
    public PaymentMethodResponseDTO addPaymentMethod(@PathVariable int id, HttpSession ses, @RequestBody PaymentMethodRequestDTO methodDTO){
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if (id != user.getId()) {
                throw new BadRequestException("Users mismatch!");
            }
        }
        return paymentService.addPayment(id,methodDTO);
    }

    @PutMapping("/users/{id}/paymentmethod")
    public PaymentMethodResponseDTO edit(@PathVariable int id, HttpSession ses, @RequestBody PaymentMethodEditDTO methodDTO ){
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if (id != user.getId()) {
                throw new BadRequestException("Users mismatch!");
            }
        }
        return paymentService.edit(id,methodDTO);
    }

    @GetMapping("/users/{id}/paymentmethod")
    public List<PaymentMethodResponseDTO> getAll(@PathVariable int id, HttpSession ses){
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if (id != user.getId()) {
                throw new BadRequestException("Users mismatch!");
            }
        }
        return paymentService.getAll(id);
    }

    @GetMapping("/users/{user_id}/paymentmethod/{id}")
    public PaymentMethodResponseDTO getById(@PathVariable int user_id,@PathVariable int id, HttpSession ses){
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if (user_id != user.getId()) {
                throw new BadRequestException("Users mismatch!");
            }
        }
        return paymentService.getById(user_id,id);
    }
}
