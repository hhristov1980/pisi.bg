package pisibg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pisibg.exceptions.AuthenticationException;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.DeniedPermissionException;
import pisibg.model.dto.CategoryRequestDTO;
import pisibg.model.dto.CategoryResponseDTO;
import pisibg.model.dto.ManufacturerRequestDTO;
import pisibg.model.dto.ManufacturerResponseDTO;
import pisibg.model.pojo.User;
import pisibg.model.repository.UserRepository;
import pisibg.service.CategoryService;

import javax.servlet.http.HttpSession;


@RestController
public class CategoryController extends AbstractController{

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserRepository userRepository;

    @PutMapping("/users/{user_id}/category/add")
    public CategoryResponseDTO addNewCategory(@PathVariable(name = "user_id") int userId, HttpSession ses, @RequestBody CategoryRequestDTO categoryRequestDTO){
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
        return categoryService.addCategory(categoryRequestDTO);
    }
}
