package pisibg.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pisibg.model.dto.UserRegisterRequestDTO;
import pisibg.model.dto.UserRegisterResponseDTO;
import pisibg.service.UserService;

import javax.servlet.http.HttpSession;

@RestController
public class UserController extends AbstractController {
    @Autowired
   private UserService userService;

    @PutMapping("/users")
    public UserRegisterResponseDTO register(@RequestBody UserRegisterRequestDTO userDTO){
        return userService.addUser(userDTO);
    }
//    @PostMapping("/users")
//    public UserWithoutPassDTO login(@RequestBody LoginUserDto dto, HttpSession ses){
//        UserWithoutPassDTO responseDto = userService.login(dto);
//        ses.setAttribute("LoggedUser", responseDto.getId());
//        return responseDto;
//    }
}
