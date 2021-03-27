package pisibg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.exceptions.AuthenticationException;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.DeniedPermissionException;
import pisibg.model.dto.*;
import pisibg.model.pojo.User;
import pisibg.model.repository.UserRepository;
import pisibg.service.SubCategoryService;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class SubCategoryController extends AbstractController{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubCategoryService subCategoryService;
    @Autowired
    private SessionManager sessionManager;

    @PostMapping("/users/{user_id}/subcategories/add")
    public SubcategoryResponseDTO addNewSubcategory(@PathVariable(name = "user_id") int userId, HttpSession ses, @RequestBody SubCategoryRequestDTO subCategoryRequestDTO){
        if(sessionManager.getLoggedUser(ses)==null){
            throw new AuthenticationException("You have to be logged in!");
        }
        else {
            User user = sessionManager.getLoggedUser(ses);
            if(!user.isAdmin()){
                throw new DeniedPermissionException("You don't have permission for that!");
            }
            else {
                return subCategoryService.addSubCategory(subCategoryRequestDTO);
            }
        }

    }

    @PutMapping("/users/{user_id}/subcategories/edit")
    public SubcategoryResponseDTO edit(@PathVariable(name = "user_id") int userId, HttpSession ses, @RequestBody SubCategoryEditRequestDTO subCategoryEditRequestDTO){
        if(sessionManager.getLoggedUser(ses)==null){
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
        return subCategoryService.edit(subCategoryEditRequestDTO);
    }
    @GetMapping("/subcategory")
    public List<SubcategoryResponseDTO> getAll(){
        return subCategoryService.getAll();
    }
    @GetMapping("/subcategory/{id}")
    public SubcategoryResponseDTO getById(@PathVariable(name = "id") int subCategoryId){
        return subCategoryService.getById(subCategoryId);
    }
}
