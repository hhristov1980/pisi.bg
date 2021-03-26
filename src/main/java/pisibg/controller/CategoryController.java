package pisibg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.exceptions.AuthenticationException;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.DeniedPermissionException;
import pisibg.model.dto.*;
import pisibg.model.pojo.User;
import pisibg.model.repository.UserRepository;
import pisibg.service.CategoryService;

import javax.servlet.http.HttpSession;
import java.awt.*;
import java.util.List;


@RestController
public class CategoryController extends AbstractController{

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/users/{user_id}/category/add")
    public CategoryResponseDTO add(@PathVariable(name = "user_id") int userId, HttpSession ses, @RequestBody CategoryRequestDTO categoryRequestDTO){
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
        return categoryService.add(categoryRequestDTO);
    }
    @PutMapping("/users/{user_id}/category/edit")
    public CategoryResponseDTO edit(@PathVariable(name = "user_id") int userId, HttpSession ses, @RequestBody CategoryEditRequestDTO categoryEditRequestDTO){
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
        return categoryService.edit(categoryEditRequestDTO);
    }
    @GetMapping("/category")
    public List<CategoryResponseDTO> getAll(){
        return categoryService.getAll();
    }
    @GetMapping("/category/{id}")
    public CategoryResponseDTO getById(@PathVariable(name = "id") int category_id){
        return categoryService.getById(category_id);
    }
}
