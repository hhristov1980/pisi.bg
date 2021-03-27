package pisibg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.exceptions.AuthenticationException;
import pisibg.exceptions.BadRequestException;
import pisibg.model.dto.OrderStatusRequestDTO;
import pisibg.model.dto.OrderStatusEditDTO;
import pisibg.model.dto.OrderStatusResponseDTO;
import pisibg.model.pojo.User;
import pisibg.service.OrderStatusService;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class OrderStatusController extends AbstractController{
    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private OrderStatusService statusService;


    @PostMapping("/users/{id}/orderstatus")
    public OrderStatusResponseDTO add(@PathVariable int id, HttpSession ses, @RequestBody OrderStatusRequestDTO statusDTO){
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if (id != user.getId()) {
                throw new BadRequestException("Users mismatch!");
            }
        }
        return statusService.add(id,statusDTO);
    }

    @PutMapping("/users/{id}/orderstatus")
    public OrderStatusResponseDTO edit(@PathVariable int id, HttpSession ses, @RequestBody OrderStatusEditDTO statusDTO){
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if (id != user.getId()) {
                throw new BadRequestException("Users mismatch!");
            }
        }
        return statusService.edit(id,statusDTO);
    }

    @GetMapping("/users/{id}/orderstatus")
    public List<OrderStatusResponseDTO> getAll(@PathVariable int id, HttpSession ses){
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if (id != user.getId()) {
                throw new BadRequestException("Users mismatch!");
            }
        }
        return statusService.getAll(id);
    }

    @GetMapping("/users/{user_id}/orderstatus/{id}")
    public OrderStatusResponseDTO getById(@PathVariable int user_id,@PathVariable int id, HttpSession ses){
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if (user_id != user.getId()) {
                throw new BadRequestException("Users mismatch!");
            }
        }
        return statusService.getById(user_id,id);
    }
}
