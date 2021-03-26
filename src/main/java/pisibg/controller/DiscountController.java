package pisibg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.exceptions.AuthenticationException;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.DeniedPermissionException;
import pisibg.model.dto.*;
import pisibg.model.pojo.User;
import pisibg.model.repository.UserRepository;
import pisibg.service.DiscountService;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class DiscountController extends AbstractController{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DiscountService discountService;

    @PostMapping("/users/{user_id}/discounts/add")
    public DiscountResponseDTO add(@PathVariable(name = "user_id") int userId, HttpSession ses, @RequestBody DiscountRequestDTO discountRequestDTO){
        if(ses.getAttribute("LoggedUser")==null){
            throw new AuthenticationException("You have to be logged in!");
        }
        else {
            int loggedId = (int)ses.getAttribute("LoggedUser");
            if(loggedId!=userId){
                throw new BadRequestException("Users mismatch!");
            }
            User user  = userRepository.findById(userId).get();
            if(!user.isAdmin()){
                throw new DeniedPermissionException("You dont have permission for that!");
            }
        }
        return discountService.add(discountRequestDTO);
    }
    @PutMapping("/users/{user_id}/discounts/edit")
    public DiscountResponseDTO edit(@PathVariable(name = "user_id") int userId, HttpSession ses, @RequestBody DiscountEditRequestDTO discountEditRequestDTO){
        if(ses.getAttribute("LoggedUser")==null){
            throw new AuthenticationException("You have to be logged in!");
        }
        else {
            int loggedId = (int)ses.getAttribute("LoggedUser");
            if(loggedId!=userId){
                throw new BadRequestException("Users mismatch!");
            }
            User user  = userRepository.findById(userId).get();
            if(!user.isAdmin()){
                throw new DeniedPermissionException("You dont have permission for that!");
            }
        }
        return discountService.edit(discountEditRequestDTO);
    }
    @GetMapping("/discounts")
    public List<DiscountResponseDTO> getAll(){
        return discountService.getAll();
    }
    @GetMapping("/discounts/{id}")
    public DiscountResponseDTO getById(@PathVariable(name = "id") int discount_id){
        return discountService.getById(discount_id);
    }
}
