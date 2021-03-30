package pisibg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.exceptions.AuthenticationException;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.DeniedPermissionException;
import pisibg.exceptions.NotFoundException;
import pisibg.model.dto.CategoryResponseDTO;
import pisibg.model.dto.ManufacturerEditRequestDTO;
import pisibg.model.dto.ManufacturerRequestDTO;
import pisibg.model.dto.ManufacturerResponseDTO;
import pisibg.model.pojo.User;
import pisibg.model.repository.ManufacturerRepository;
import pisibg.model.repository.UserRepository;
import pisibg.service.ManufacturerService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RestController
public class ManufacturerController {
    @Autowired
    private ManufacturerService manufacturerService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionManager sessionManager;


    @PostMapping("/manufacturers")
    public ManufacturerResponseDTO add(HttpSession ses, @RequestBody ManufacturerRequestDTO manufacturerRequestDTO){
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        }
        User user = sessionManager.getLoggedUser(ses);
        if (!user.isAdmin()) {
            throw new DeniedPermissionException("You dont have permission for that!");
        }
        return manufacturerService.add(manufacturerRequestDTO);
    }

    @PutMapping("/manufacturers")
    public ManufacturerResponseDTO editManufacturer(HttpSession ses, @RequestBody ManufacturerEditRequestDTO manufacturerEditRequestDTO){
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if (!user.isAdmin()) {
                throw new DeniedPermissionException("You dont have permission for that!");
            }
            int id = user.getId();
            return manufacturerService.edit(manufacturerEditRequestDTO);
        }
    }
    @GetMapping("/manufacturers")
    public List<ManufacturerResponseDTO> getAll(){
        return manufacturerService.getAll();
    }
    //TODO FIX EXCEPTION
    @GetMapping("/manufacturers/{id}")
    public ManufacturerResponseDTO getById(@PathVariable(name = "id") int manufacturerId){
        return manufacturerService.getById(manufacturerId);
    }
}
