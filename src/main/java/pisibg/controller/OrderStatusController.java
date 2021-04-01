package pisibg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.exceptions.AuthenticationException;
import pisibg.exceptions.DeniedPermissionException;
import pisibg.model.dto.orderDTO.OrderStatusRequestDTO;
import pisibg.model.dto.orderDTO.OrderStatusEditDTO;
import pisibg.model.dto.orderDTO.OrderStatusResponseDTO;
import pisibg.model.pojo.User;
import pisibg.service.OrderStatusService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
public class OrderStatusController extends AbstractController {
    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private OrderStatusService statusService;


    @PostMapping("/users/orderstatus")
    public OrderStatusResponseDTO add(HttpSession ses,@Valid @RequestBody OrderStatusRequestDTO statusDTO) {
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if (!user.isAdmin()) {
                throw new DeniedPermissionException("You dont have permission for that!");
            }
            int id = user.getId();
            return statusService.add(id, statusDTO);
        }
    }

    @PutMapping("/users/orderstatus")
    public OrderStatusResponseDTO edit(HttpSession ses, @Valid @RequestBody OrderStatusEditDTO statusDTO) {
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if (!user.isAdmin()) {
                throw new DeniedPermissionException("You dont have permission for that!");
            }
            int id = user.getId();
            return statusService.edit(id, statusDTO);
        }
    }

    @GetMapping("/users/orderstatus")
    public List<OrderStatusResponseDTO> getAll(HttpSession ses) {
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if (!user.isAdmin()) {
                throw new DeniedPermissionException("You dont have permission for that!");
            }
            int id = user.getId();
            return statusService.getAll(id);
        }
    }

    @GetMapping("/users/orderstatus/{order_id}")
    public OrderStatusResponseDTO getById(@PathVariable int order_id, HttpSession ses) {
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if (!user.isAdmin()) {
                throw new DeniedPermissionException("You dont have permission for that!");
            }
            int id = user.getId();
            return statusService.getById(id, order_id);
        }
    }
}
