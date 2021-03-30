package pisibg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.DeniedPermissionException;
import pisibg.exceptions.NotFoundException;
import pisibg.model.dto.orderDTO.OrderStatusRequestDTO;
import pisibg.model.dto.orderDTO.OrderStatusEditDTO;
import pisibg.model.dto.orderDTO.OrderStatusResponseDTO;
import pisibg.model.pojo.OrderStatus;
import pisibg.model.pojo.User;
import pisibg.model.repository.OrderStatusRepository;
import pisibg.model.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderStatusService {
    @Autowired
    private OrderStatusRepository statusRepository;

    @Autowired
    private UserRepository userRepository;

    public OrderStatusResponseDTO add(int id, OrderStatusRequestDTO statusDTO) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User u = user.get();
            if (u.isAdmin()) {
                if (statusRepository.findByType(statusDTO.getType()) == null) {
                    OrderStatus status = new OrderStatus(statusDTO);
                    statusRepository.save(status);
                    return new OrderStatusResponseDTO(status);
                } else {
                    throw new BadRequestException("Status already exist!");
                }
            } else {
                throw new DeniedPermissionException("You dont have permission for that!");
            }
        } else {
            throw new NotFoundException("User not found!");
        }
    }

    public OrderStatusResponseDTO edit(int id, OrderStatusEditDTO statusDTO) {
        if (statusDTO.getType().equals(statusDTO.getNewType())) {
            throw new BadRequestException("You didn't make any change!");
        }
        if (statusRepository.findByType(statusDTO.getType()) == null) {
            throw new NotFoundException("Order status not found");
        }
        if (statusRepository.findByType(statusDTO.getNewType()) != null) {
            throw new NotFoundException("Order with this name already exists");
        }
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User u = user.get();
            if (u.isAdmin()) {
                OrderStatus status = new OrderStatus();
                status.setId(statusDTO.getId());
                status.setType(statusDTO.getNewType());
                statusRepository.save(status);
                return new OrderStatusResponseDTO(status);
            } else {
                throw new DeniedPermissionException("You dont have permission for that!");
            }
        } else {
            throw new NotFoundException("User not found!");
        }
    }

    public List<OrderStatusResponseDTO> getAll(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User u = user.get();
            if (u.isAdmin()) {
                List<OrderStatus> statuses = new ArrayList<>();
                statuses.addAll(statusRepository.findAll());
                if (statuses.isEmpty()) {
                    throw new NotFoundException("Statuses not found!");
                }
                List<OrderStatusResponseDTO> result = new ArrayList<>();
                for (OrderStatus status : statuses) {
                    result.add(new OrderStatusResponseDTO(status));
                }
                return result;
            } else {
                throw new DeniedPermissionException("You don't have permission for that");
            }
        } else {
            throw new NotFoundException("User not found!");
        }
    }

    public OrderStatusResponseDTO getById(int user_id, int status_id) {
        Optional<User> user = userRepository.findById(user_id);
        Optional<OrderStatus> stat = statusRepository.findById(status_id);
        if (user.isPresent() && stat.isPresent()) {
            User u = user.get();
            OrderStatus status = stat.get();
            if (u.isAdmin()) {
                return new OrderStatusResponseDTO(status);
            } else {
                throw new DeniedPermissionException("You don't have permission for that!");
            }
        } else {
            throw new NotFoundException("Status/user not found!");
        }
    }
}
