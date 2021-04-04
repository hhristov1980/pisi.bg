package pisibg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.exceptions.AuthenticationException;
import pisibg.exceptions.DeniedPermissionException;
import pisibg.model.dto.discountDTO.DiscountEditRequestDTO;
import pisibg.model.dto.discountDTO.DiscountRequestDTO;
import pisibg.model.dto.discountDTO.DiscountResponseDTO;
import pisibg.model.pojo.User;
import pisibg.model.repository.UserRepository;
import pisibg.service.DiscountService;
import pisibg.utility.EmailServiceImpl;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
public class DiscountController extends AbstractController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailServiceImpl emailService;
    @Autowired
    private DiscountService discountService;
    @Autowired
    private SessionManager sessionManager;


    @PostMapping("/discounts")
    public DiscountResponseDTO add(HttpSession ses,@Valid @RequestBody DiscountRequestDTO discountRequestDTO) {
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if (!user.isAdmin()) {
                throw new DeniedPermissionException("You don't have permission for that!");
            } else {
                return discountService.add(discountRequestDTO);
            }
        }

    }

    @PutMapping("/discounts")
    public DiscountResponseDTO edit(HttpSession ses,@Valid @RequestBody DiscountEditRequestDTO discountEditRequestDTO) {
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if (!user.isAdmin()) {
                throw new DeniedPermissionException("You don't have permission for that!");
            }
        }
        return discountService.edit(discountEditRequestDTO);
    }

    @GetMapping("/discounts")
    public List<DiscountResponseDTO> getAll() {
        return discountService.getAll();
    }

    @GetMapping("/discounts/{id}")
    public DiscountResponseDTO getById(@PathVariable(name = "id") int discountId) {
        return discountService.getById(discountId);
    }
}
