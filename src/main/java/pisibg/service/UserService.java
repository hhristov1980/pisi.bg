package pisibg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pisibg.exceptions.BadRequestException;
import pisibg.model.dto.UserRegisterRequestDTO;
import pisibg.model.dto.UserRegisterResponseDTO;
import pisibg.model.pojo.User;
import pisibg.model.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public UserRegisterResponseDTO addUser(UserRegisterRequestDTO userDTO){
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        String email = userDTO.getEmail();
        if(!email.matches(regex)){
            throw new BadRequestException("Email is not valid");
        }

        if(userRepository.findByEmail(userDTO.getEmail()) != null){
            throw new BadRequestException("Email already exists");
        }

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        userDTO.setPassword(encoder.encode(userDTO.getPassword()));
        User user = new User(userDTO);
        user = userRepository.save(user);
        UserRegisterResponseDTO responseUserDTO = new UserRegisterResponseDTO(user);
        return responseUserDTO;
    }

//    public UserWithoutPassDTO login(LoginUserDto dto) {
//        User user = userRepository.findByUsername(dto.getUsername());
//        if(user == null){
//            throw new AuthenticationException("Wrong credentials");
//        }
//        else{
//            PasswordEncoder encoder = new BCryptPasswordEncoder();
//            if(encoder.matches(dto.getPassword(), user.getPassword())){
//                return new UserWithoutPassDTO(user);
//            }
//            else{
//                throw new AuthenticationException("Wrong credentials");
//            }
//        }
//    }
}
