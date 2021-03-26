package pisibg.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.exceptions.AuthenticationException;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.DeniedPermissionException;
import pisibg.exceptions.MySQLException;
import pisibg.model.dto.*;
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
        emailService.sendSimpleMessage("hristo_ih@abv.bg","Test","Test Test");
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

    @PutMapping("/users/{id}/edit")
    public UserEditResponseDTO editUser(@PathVariable int id, @RequestBody UserEditRequestDTO userDto, HttpSession ses){
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if (id != user.getId()) {
                throw new DeniedPermissionException("You dont have permission for that!");
            }
            return userService.edit(userDto,id);
        }
    }
    @PostMapping("/users/{id}/edit")
    public String editUser(@PathVariable int id, @RequestBody UserEditPasswordDTO userDto, HttpSession ses){
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if (id != user.getId()) {
                throw new DeniedPermissionException("You dont have permission for that!");
            }
            return userService.editPassword(userDto,id);
        }
    }

    @PostMapping("/users/{id_admin}/admin/{id_user}")
    public UserRegisterResponseDTO makeAdmin(@PathVariable int id_admin,@PathVariable int id_user, HttpSession ses){
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if (id_admin != user.getId()) {
                throw new DeniedPermissionException("You dont have permission for that!");
            }
            return userService.makeAdmin(id_admin,id_user);
        }
    }

    @PutMapping("/users/{id_admin}/admin/{id_user}")
    public UserRegisterResponseDTO removeAdmin(@PathVariable int id_admin,@PathVariable int id_user, HttpSession ses){
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if (id_admin != user.getId()) {
                throw new DeniedPermissionException("You dont have permission for that!");
            }
            return userService.removeAdmin(id_admin,id_user);
        }
    }

    @DeleteMapping("/users/{id_admin}/admin/{id_user}")
    public void deleteUser(@PathVariable int id_admin,@PathVariable int id_user, HttpSession ses){
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if (id_admin != user.getId()) {
                throw new DeniedPermissionException("You dont have permission for that!");
            }
            try {
                userService.deleteUser(id_admin,id_user);
                ses.invalidate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
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