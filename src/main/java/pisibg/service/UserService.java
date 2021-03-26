package pisibg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pisibg.exceptions.AuthenticationException;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.DeniedPermissionException;
import pisibg.exceptions.NotFoundException;
import pisibg.model.dao.UserDAO;
import pisibg.model.dto.*;
import pisibg.model.pojo.User;
import pisibg.model.repository.UserRepository;

import java.sql.SQLException;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDAO userDAO;
    private static final String REGEX_EMAIL = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";


    public UserRegisterResponseDTO addUser(UserRegisterRequestDTO userDTO) {

        String email = userDTO.getEmail();
        if (!email.matches(REGEX_EMAIL)) {
            throw new BadRequestException("Email is not valid");
        }

        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new BadRequestException("Email already exists");
        }

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        userDTO.setPassword(encoder.encode(userDTO.getPassword()));
        User user = new User(userDTO);
        user = userRepository.save(user);
        UserRegisterResponseDTO responseUserDTO = new UserRegisterResponseDTO(user);
        return responseUserDTO;
    }


    public UserWithoutPassDTO login(UserLoginDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail());
        if (user == null) {
            throw new AuthenticationException("Wrong credentials");
        } else {
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(dto.getPassword(), user.getPassword())) {
                return new UserWithoutPassDTO(user);
            } else {
                throw new AuthenticationException("Wrong credentials");
            }
        }
    }


    public UserWithoutPassDTO getById(int id) {
        Optional<User> u = userRepository.findById(id);
        if (u.isPresent()) {
            User user = u.get();
            return new UserWithoutPassDTO(user);
        } else {
            throw new NotFoundException("Not found user!");
        }
    }

    public void softDelete(int id) throws SQLException {
<<<<<<< HEAD
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User u = user.get();
            if (!u.isAdmin()) {
                userDAO.deleteUser(id);
            } else {
                if (userDAO.countAdmin() > 1) {
                    userDAO.deleteUser(id);
                }
            }
        }
    }

   /* public UserWithoutPassDTO subscribe(int id) {
        Optional<User> u = userRepository.findById(id);
        if(u.isPresent()){
            User user = u.get();
            user.setSubscribed(true);
           return new UserWithoutPassDTO(userRepository.save(user));
        }
        else {
            throw new NotFoundException("Not found user!");
        }
    }

    public UserWithoutPassDTO unsubscribe(int id) {
        Optional<User> u = userRepository.findById(id);
        if(u.isPresent()){
            User user = u.get();
            user.setSubscribed(false);
            return new UserWithoutPassDTO(userRepository.save(user));
        }
        else {
            throw new NotFoundException("Not found user!");
        }
    }*/

    public UserEditResponseDTO edit(UserEditRequestDTO userDto, int id) {
        Optional<User> u = userRepository.findById(id);
        if (u.isPresent()) {
            User user = u.get();
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(userDto.getPassword(), user.getPassword())) {
                user.setAddress(userDto.getAddress());
                user.setTownName(userDto.getTownName());
                user.setAddress(userDto.getAddress());
                user.setSubscribed(userDto.isSubscribed());
                user.setPhoneNumber(userDto.getPhoneNumber());
                user.setFirstName(userDto.getFirstName());
                user.setLastName(userDto.getLastName());
                if (userDto.getEmail().matches(REGEX_EMAIL)) {
                    if (userRepository.findByEmail(userDto.getEmail()) == null ||
                            userRepository.findByEmail(userDto.getEmail()).getId() == id) {
                        user.setEmail(userDto.getEmail());
                    } else {
                        throw new BadRequestException("Email is already in use!");
                    }
                } else {
                    throw new BadRequestException("Email is not valid!");
                }
                userRepository.save(user);
                return new UserEditResponseDTO(user);
            } else {
                throw new DeniedPermissionException("You don't have permission for edit this profile!");
            }
        } else {
            throw new NotFoundException("Not found user!");
        }
    }

    public String editPassword(UserEditPasswordDTO userDto, int id) {
        Optional<User> u = userRepository.findById(id);
        if (u.isPresent()) {
            User user = u.get();
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(userDto.getPassword(), user.getPassword())) {
                if (userDto.getNewPassword().equals(userDto.getConfirmNewPassword())) {
                    user.setPassword(encoder.encode(userDto.getNewPassword()));
                    userRepository.save(user);
                    return "Your password is successfully changed!";
                } else {
                    throw new BadRequestException("Your password not match!");
                }
            } else {
                throw new DeniedPermissionException("Wrong password!");
            }
        } else {
            throw new NotFoundException("User not found!");
        }
    }

    public UserRegisterResponseDTO makeAdmin(int id_admin, int id_user) {
        Optional<User> a = userRepository.findById(id_admin);
        Optional<User> u = userRepository.findById(id_user);
        if (a.isPresent() && u.isPresent()) {
            User admin = a.get();
            User user = u.get();
            if (admin.isAdmin()) {
                user.setAdmin(true);
                userRepository.save(user);
                return new UserRegisterResponseDTO(user);
            } else {
                throw new DeniedPermissionException("You don't have permission for that!");
            }
        } else {
            throw new NotFoundException("User not found!");
        }
    }

    public UserRegisterResponseDTO removeAdmin(int id_admin, int id_user) {
        Optional<User> a = userRepository.findById(id_admin);
        Optional<User> u = userRepository.findById(id_user);
        if (a.isPresent() && u.isPresent()) {
            User admin = a.get();
            User user = u.get();
            if (admin.isAdmin()) {
                user.setAdmin(false);
                userRepository.save(user);
                return new UserRegisterResponseDTO(user);
            } else {
                throw new DeniedPermissionException("You don't have permission for that!");
            }
        } else {
            throw new NotFoundException("User not found!");
        }
    }

    public void deleteUser(int id_admin, int id_user) throws SQLException {
        Optional<User> a = userRepository.findById(id_admin);
        Optional<User> u = userRepository.findById(id_user);
        if (a.isPresent() && u.isPresent()) {
            User admin = a.get();
            User user = u.get();
            if (admin.isAdmin()) {
                userDAO.deleteUser(id_user);
            }
            else {
                throw new DeniedPermissionException("You don't have permission for that!");
            }
        }
        else {
            throw new NotFoundException("User not found!");
        }
=======
        userDAO.deleteUser(id);
>>>>>>> 460644343d668668036649a03ecbc6acd1837026
    }
}
