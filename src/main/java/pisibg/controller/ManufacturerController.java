package pisibg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.exceptions.AuthenticationException;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.DeniedPermissionException;
import pisibg.exceptions.NotFoundException;
import pisibg.model.dto.ManufacturerRequestDTO;
import pisibg.model.dto.ManufacturerResponseDTO;
import pisibg.model.pojo.User;
import pisibg.model.repository.ManufacturerRepository;
import pisibg.model.repository.UserRepository;
import pisibg.service.ManufacturerService;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
public class ManufacturerController {
    @Autowired
    private ManufacturerService manufacturerService;
    @Autowired
    private UserRepository userRepository;



    @PutMapping("/users/{user_id}/manufacturers/add")
    public ManufacturerResponseDTO addNewManufacturer(@PathVariable(name = "user_id") int userId, HttpSession ses, @RequestBody ManufacturerRequestDTO manufacturerRequestDTO){
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
                throw new DeniedPermissionException("You don't have permission for that!");
            }
        }
        return manufacturerService.addManufacturer(manufacturerRequestDTO);
    }

//    @PostMapping("/users/{user_id}/manufacturers/edit{man_id}")
//    public ManufacturerResponseDTO editManufacturer(@PathVariable(name = "user_id") int userId, HttpSession ses, @RequestBody ManufacturerRequestDTO manufacturerRequestDTO){
////        if(ses.getAttribute("LoggedUser")==null){
////            throw new AuthenticationException("You have to be logged in!");
////        }
////        else {
////            int loggedId = (int)ses.getAttribute("LoggedUser");
////            if(userId!=loggedId){
////                throw new BadRequestException("You don't have permission for that!");
////            }
////        }
//        return manufacturerService.edit(manufacturerRequestDTO);
//    }
}
