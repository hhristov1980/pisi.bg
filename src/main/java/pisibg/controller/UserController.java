package pisibg.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.exceptions.AuthenticationException;
import pisibg.exceptions.DeniedPermissionException;
import pisibg.exceptions.MySQLException;
import pisibg.model.dto.*;
//import pisibg.model.pojo.Payment;
import pisibg.model.pojo.User;
import pisibg.service.UserService;
import pisibg.utility.EmailServiceImpl;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@RestController
public class UserController extends AbstractController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailServiceImpl emailService;

    @Autowired
    private SessionManager sessionManager;


    @PostMapping("/users")
    public UserRegisterResponseDTO register(@RequestBody UserRegisterRequestDTO userDTO) {
        return userService.addUser(userDTO);
    }

    @PutMapping("/users")
    public UserWithoutPassDTO login(@RequestBody UserLoginDTO dto, HttpSession ses) {
        UserWithoutPassDTO responseDto = userService.login(dto);
        sessionManager.loginUser(ses, responseDto.getId());
        return responseDto;
    }

    @PostMapping("/logout")
    public void logout(HttpSession ses) {
        sessionManager.logoutUser(ses);
    }

//    @GetMapping("/users/{id}/subscribe")
//    public UserWithoutPassDTO subscribe(@PathVariable int id, HttpSession ses) {
//        if (sessionManager.getLoggedUser(ses) == null) {
//            throw new AuthenticationException("You have to be logged in!");
//        } else {
//            User user = sessionManager.getLoggedUser(ses);
//            if (id != user.getId()) {
//                throw new DeniedPermissionException("You dont have permission for that!");
//            }
//            return userService.subscribe(id);
//        }
//    }

    @PutMapping("/users/edit")
    public UserEditResponseDTO editPasswordUser(@RequestBody UserEditRequestDTO userDto, HttpSession ses){
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            int id = user.getId();
            return userService.edit(userDto,id);
        }
    }
    @PostMapping("/users/edit")
    public String editPasswordUser(@RequestBody UserEditPasswordDTO userDto, HttpSession ses){
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            int id = user.getId();
            return userService.editPassword(userDto,id);
        }
    }

    @PostMapping("/admin/{id_user}")
    public UserRegisterResponseDTO makeAdmin(@PathVariable int id_user, HttpSession ses){
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if(!user.isAdmin()){
                throw new DeniedPermissionException("You dont have permission for that!");
            }
            int admin_id = user.getId();
            return userService.makeAdmin(admin_id,id_user);
        }
    }

    @PutMapping("/admin/{id_user}")
    public UserRegisterResponseDTO removeAdmin(@PathVariable int id_user, HttpSession ses){
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if(!user.isAdmin()){
                throw new DeniedPermissionException("You dont have permission for that!");
            }
            int admin_id = user.getId();
            return userService.removeAdmin(admin_id,id_user);
        }
    }

    @DeleteMapping("/admin/{user_id}")
    public UserEditResponseDTO deleteUser(@PathVariable int user_id, HttpSession ses){
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if(!user.isAdmin()){
                throw new DeniedPermissionException("You dont have permission for that!");
            }
            int admin_id = user.getId();
            try {
                ses.invalidate();
               return userService.deleteUser(admin_id, user_id);
            } catch (SQLException throwables) {
                throw new MySQLException("Something get wrong!");
            }
        }
    }

    @PutMapping("/users/order/{order_id}")
    public OrderEditResponseDTO editOrder(@PathVariable int order_id,@RequestBody OrderEditRequestDTO orderDto, HttpSession ses){
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if(!user.isAdmin()){
                throw new DeniedPermissionException("You dont have permission for that!");
            }
            int admin_id = user.getId();
            return userService.editOrder(admin_id, order_id,orderDto);
        }
    }

//    @GetMapping("/users/{id}/unsubscribe")
//    public UserWithoutPassDTO unsubscribe(@PathVariable int id, HttpSession ses) {
//        if (sessionManager.getLoggedUser(ses) == null) {
//            throw new AuthenticationException("You have to be logged in!");
//        } else {
//            User user = sessionManager.getLoggedUser(ses);
//            if (id != user.getId()) {
//                throw new DeniedPermissionException("You dont have permission for that!");
//            }
//            return userService.unsubscribe(id);
//        }
//    }

    @GetMapping("/users/{id}")
    public UserWithoutPassDTO getById(@PathVariable int id, HttpSession ses) {
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if (id != user.getId()) {
                throw new DeniedPermissionException("You dont have permission for that!");
            }
            return userService.getById(id);
        }
    }

    @DeleteMapping("/users/{id}")
    public void softDelete(@PathVariable int id, HttpSession ses) {
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if (id != user.getId()) {
                throw new DeniedPermissionException("You dont have permission for that!");
            }
            try {
                userService.softDelete(id);
                ses.invalidate();
            } catch (SQLException throwables) {
                System.out.println(throwables.getMessage());
            }
        }
    }
}