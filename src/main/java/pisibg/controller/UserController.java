package pisibg.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.exceptions.AuthenticationException;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.DeniedPermissionException;
import pisibg.exceptions.MySQLException;
import pisibg.model.dto.UserLoginDTO;
import pisibg.model.dto.UserRegisterRequestDTO;
import pisibg.model.dto.UserRegisterResponseDTO;
import pisibg.model.dto.UserWithoutPassDTO;
import pisibg.model.pojo.User;
import pisibg.service.UserService;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@RestController
public class UserController extends AbstractController {
    @Autowired
   private UserService userService;

    @PutMapping("/users")
    public UserRegisterResponseDTO register(@RequestBody UserRegisterRequestDTO userDTO){
        return userService.addUser(userDTO);
    }
    @PostMapping("/users")
    public UserWithoutPassDTO login(@RequestBody UserLoginDTO dto, HttpSession ses){
        UserWithoutPassDTO responseDto = userService.login(dto);
        ses.setAttribute("LoggedUser", responseDto.getId());
        return responseDto;
    }

    @GetMapping("/users/{id}")
    public UserWithoutPassDTO getById(@PathVariable int id, HttpSession ses){
        if(ses.getAttribute("LoggedUser")==null){
            throw new AuthenticationException("You have to be logged in!");
        }
        else {
            int loggedId = (int)ses.getAttribute("LoggedUser");
            if(id!=loggedId){
                throw new DeniedPermissionException("You dont have permission for that!");
            }
        }
        return userService.getById(id);
    }

    @DeleteMapping("/user/{id}")
    public void softDelete(@PathVariable int id, HttpSession ses){
        if(ses.getAttribute("LoggedUser")==null){
            throw new AuthenticationException("You have to be logged in!");
        }
        else {
            int loggedId = (int)ses.getAttribute("LoggedUser");
            if(id!=loggedId){
                throw new DeniedPermissionException("You dont have permission for that!");
            }
        }
        try {
            userService.softDelete(id);
            ses.invalidate();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }
}
