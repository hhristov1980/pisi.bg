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
import pisibg.model.dto.orderDTO.*;
import pisibg.model.dto.productDTO.ProductOrderResponseDTO;
import pisibg.model.dto.userDTO.*;
import pisibg.model.pojo.Order;
import pisibg.model.pojo.User;
import pisibg.model.repository.OrderRepository;
import pisibg.model.repository.OrderStatusRepository;
import pisibg.model.repository.UserRepository;
import pisibg.utility.Validator;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private UserDAO userDAO;
    public static final String REGEX_EMAIL = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String PASSWORD_REGEX = "^(?!.*[\\s])(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";


    public List<UserRegisterResponseDTO> getAllUsers(UserReportRequestDTO dto) throws SQLException {
        List<User> users = userDAO.getAllUsers(dto);
        List<UserRegisterResponseDTO> usersDto = new ArrayList<>();
        for (User u : users) {
            usersDto.add(new UserRegisterResponseDTO(u));
        }
        return usersDto;
    }

    public List<OrderReportDTO> getDailyOrders(LocalDateTime from, LocalDateTime to, OrderDailyReportRequestDTO dto) throws SQLException {
        List<Order> orders = userDAO.dailyOrders(from, to, dto);
        List<OrderReportDTO> usersDto = new ArrayList<>();
        for (Order o : orders) {
            usersDto.add(new OrderReportDTO(o));
        }
        return usersDto;
    }

    public List<OrderReportDTO> getMonthlyOrders(OrderMonthlyReportRequestDTO dto) throws SQLException {
        List<Order> orders = userDAO.monthlyOrders(dto);
        List<OrderReportDTO> usersDto = new ArrayList<>();
        for (Order o : orders) {
            usersDto.add(new OrderReportDTO(o));
        }
        return usersDto;
    }

    public List<OrderReportDTO> getYearlyOrders(OrderYearlyReportRequestDTO dto) throws SQLException {
        List<Order> orders = userDAO.yearlyOrders(dto);
        List<OrderReportDTO> usersDto = new ArrayList<>();
        for (Order o : orders) {
            usersDto.add(new OrderReportDTO(o));
        }
        return usersDto;
    }

    public UserRegisterResponseDTO addUser(UserRegisterRequestDTO userDTO) {

        String email = userDTO.getEmail();
        String password = userDTO.getPassword();
        if (!email.matches(REGEX_EMAIL)) {
            throw new BadRequestException("Email is not valid");
        }

        if (!password.matches(PASSWORD_REGEX)) {
            throw new BadRequestException("Password must be " +
                    "minimum eight characters, at least one bigger letter, one lower letter, one number ");
        }

        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new BadRequestException("Email already exists");
        }
        if(userDTO.getPhoneNumber().length()!=10){
            throw new BadRequestException("Phone is not valid!");
        }

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        userDTO.setPassword(encoder.encode(userDTO.getPassword()));
        User user = new User(userDTO);
        user = userRepository.save(user);
        UserRegisterResponseDTO responseUserDTO = new UserRegisterResponseDTO(user);
        return responseUserDTO;
    }


    public UserWithoutPassDTO login(UserLoginDTO dto) throws SQLException {
        User user = userDAO.returnLoginDetails(dto);
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

    public UserEditResponseDTO softDelete(int id) throws SQLException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User u = user.get();
            if (!u.isAdmin()) {

                return new UserEditResponseDTO(userDAO.deleteUser(id));
            } else {
                if (userDAO.countAdmin() > 1) {
                    return new UserEditResponseDTO(userDAO.deleteUser(id));
                }
                else {
                    throw new DeniedPermissionException("You can't delete last one admin");
                }
            }
        }
        else {
            throw new NotFoundException("User not found!");
        }
    }

    public UserEditResponseDTO edit(UserEditRequestDTO userDto, int id) {
        Optional<User> u = userRepository.findById(id);
        if (u.isPresent()) {
            User user = u.get();
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(userDto.getPassword(), user.getPassword())) {
                if(Validator.isValidString(userDto.getTownName())) {
                    user.setTownName(userDto.getTownName());
                }
                if(Validator.isValidString(userDto.getAddress())) {
                    user.setAddress(userDto.getAddress());
                }
                user.setSubscribed(userDto.isSubscribed());
                if(userDto.getPhoneNumber()!=null && userDto.getPhoneNumber().length()==10) {
                    user.setPhoneNumber(userDto.getPhoneNumber());
                }
                if(Validator.isValidString(userDto.getFirstName())) {
                    user.setFirstName(userDto.getFirstName());
                }
                if(Validator.isValidString(userDto.getLastName()))
                user.setLastName(userDto.getLastName());
                if (userDto.getEmail().matches(REGEX_EMAIL)) {
                    if (userRepository.findByEmail(userDto.getEmail()) == null ||
                            userRepository.findByEmail(userDto.getEmail()).getId() == id) {
                        user.setEmail(userDto.getEmail());
                    } else {
                        throw new BadRequestException("Email is already in use!");
                    }
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

    public UserEditResponseDTO deleteUser(int admin_id, int user_id) throws SQLException {
        Optional<User> a = userRepository.findById(admin_id);
        Optional<User> u = userRepository.findById(user_id);
        if (a.isPresent() && u.isPresent()) {
            User admin = a.get();
            User user = u.get();
            if (admin.isAdmin()) {
                return new UserEditResponseDTO(userDAO.deleteUser(user_id));
            } else {
                throw new DeniedPermissionException("You don't have permission for that!");
            }
        } else {
            throw new NotFoundException("User not found!");
        }
    }

    @Transactional
    public OrderEditResponseDTO editOrder(int admin_id, OrderEditRequestDTO orderDto) {
        Optional<User> a = userRepository.findById(admin_id);
        Optional<Order> o = orderRepository.findById(orderDto.getId());
        Optional<User> u = userRepository.findById(o.get().getUser().getId());
        if (o.isPresent() && a.isPresent()) {
            User admin = a.get();
            Order order = o.get();
            User user = u.get();
            if (admin.isAdmin()) {
                if(Validator.isValidString(orderDto.getAddress())) {
                    order.setAddress(orderDto.getAddress());
                }
                if(orderDto.getGrossValue().compareTo(BigDecimal.valueOf(0))>=0) {
                    order.setGrossValue(orderDto.getGrossValue());
                }
                if(orderDto.getNetValue().compareTo(BigDecimal.valueOf(0))>=0) {
                    order.setNetValue(orderDto.getNetValue());
                }
                if(orderDto.getDiscount().compareTo(BigDecimal.valueOf(0))>=0) {
                    order.setDiscount(orderDto.getDiscount());
                }

                order.setPaid(orderDto.isPaid());
                if(Validator.isValidInteger(orderDto.getOrderStatusId())) {
                    order.setOrderStatus(orderStatusRepository.getOne(orderDto.getOrderStatusId()));
                }
                orderRepository.save(order);

                user.setTurnover(user.getTurnover().subtract(order.getNetValue()));
                userRepository.save(user);
                return new OrderEditResponseDTO(order);
            } else {
                throw new DeniedPermissionException("You don't have permission for that!");
            }
        } else {
            throw new NotFoundException("User not found!");
        }
    }

}
