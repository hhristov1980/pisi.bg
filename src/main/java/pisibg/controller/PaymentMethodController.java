package pisibg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.exceptions.AuthenticationException;
import pisibg.exceptions.DeniedPermissionException;
import pisibg.model.dto.paymentDTO.PaymentMethodEditDTO;
import pisibg.model.dto.paymentDTO.PaymentMethodRequestDTO;
import pisibg.model.dto.paymentDTO.PaymentMethodResponseDTO;
import pisibg.model.pojo.User;
import pisibg.service.PaymentMethodService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
public class PaymentMethodController extends AbstractController {
    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private PaymentMethodService paymentService;

    @PostMapping("/paymentmethod")
    public PaymentMethodResponseDTO addPaymentMethod(HttpSession ses,@Valid @RequestBody PaymentMethodRequestDTO methodDTO) {
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        }
        User user = sessionManager.getLoggedUser(ses);
        if (!user.isAdmin()) {
            throw new DeniedPermissionException("You dont have permission for that!");
        }
        int id = user.getId();
        return paymentService.addPayment(id, methodDTO);
    }

    @PutMapping("/paymentmethod")
    public PaymentMethodResponseDTO edit(HttpSession ses,@Valid @RequestBody PaymentMethodEditDTO methodDTO) {
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        }
        User user = sessionManager.getLoggedUser(ses);
        if (!user.isAdmin()) {
            throw new DeniedPermissionException("You dont have permission for that!");
        }
        int id = user.getId();
        return paymentService.edit(id, methodDTO);
    }

    @GetMapping("/paymentmethod")
    public List<PaymentMethodResponseDTO> getAll(HttpSession ses) {
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        }
        User user = sessionManager.getLoggedUser(ses);
        int id = user.getId();
        return paymentService.getAll(id);
    }

    @GetMapping("/paymentmethod/{id}")
    public PaymentMethodResponseDTO getById(@PathVariable int id, HttpSession ses) {
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        }
        User user = sessionManager.getLoggedUser(ses);
        int user_id = user.getId();
        return paymentService.getById(user_id, id);
    }
}
