package pisibg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.DeniedPermissionException;
import pisibg.exceptions.NotFoundException;
import pisibg.model.dto.paymentDTO.PaymentMethodEditDTO;
import pisibg.model.dto.paymentDTO.PaymentMethodRequestDTO;
import pisibg.model.dto.paymentDTO.PaymentMethodResponseDTO;
import pisibg.model.pojo.PaymentMethod;
import pisibg.model.pojo.User;
import pisibg.model.repository.PaymentMethodRepository;
import pisibg.model.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentMethodService {
    @Autowired
    private PaymentMethodRepository paymentsRepository;
    @Autowired
    private UserRepository userRepository;

    public PaymentMethodResponseDTO addPayment(int id, PaymentMethodRequestDTO methodDTO) {
        Optional<User> u = userRepository.findById(id);
        if (u.isPresent()) {
            User user = u.get();
            if (user.isAdmin()) {
                PaymentMethod method = new PaymentMethod();
                method.setType(methodDTO.getType());
                paymentsRepository.save(method);
                return new PaymentMethodResponseDTO(method);
            } else {
                throw new DeniedPermissionException("You don't have permission for that!");
            }
        } else {
            throw new NotFoundException("User not found!");
        }
    }

    public PaymentMethodResponseDTO edit(int id, PaymentMethodEditDTO methodDTO) {
        if (methodDTO.getType().equals(methodDTO.getNewType())) {
            throw new BadRequestException("You didn't make any change!");
        }
        if (paymentsRepository.findByType(methodDTO.getType()) == null) {
            throw new NotFoundException("Payment method not found");
        }
        if (paymentsRepository.findByType(methodDTO.getNewType()) != null) {
            throw new NotFoundException("Payment with this name already exists");
        }
        Optional<User> u = userRepository.findById(id);
        if (u.isPresent()) {
            User user = u.get();
            if (user.isAdmin()) {
                PaymentMethod method = new PaymentMethod();
                method.setType(methodDTO.getNewType());
                method.setId(methodDTO.getId());
                paymentsRepository.save(method);
                return new PaymentMethodResponseDTO(method);
            } else {
                throw new DeniedPermissionException("You don't have permission for that!");
            }
        } else {
            throw new NotFoundException("User not found!");
        }
    }

    public List<PaymentMethodResponseDTO> getAll(int id) {
        Optional<User> u = userRepository.findById(id);
        if (u.isPresent()) {
            User user = u.get();
            if (user.isAdmin()) {
                List<PaymentMethod> methods = paymentsRepository.findAll();
                List<PaymentMethodResponseDTO> result = new ArrayList<>();
                for (PaymentMethod m : methods) {
                    result.add(new PaymentMethodResponseDTO(m));
                }
                return result;
            } else {
                throw new DeniedPermissionException("You don't have permission for that!");
            }
        } else {
            throw new NotFoundException("User not found!");
        }
    }

    public PaymentMethodResponseDTO getById(int user_id, int id) {
        Optional<User> u = userRepository.findById(user_id);
        Optional<PaymentMethod> pm = paymentsRepository.findById(id);
        if (u.isPresent() && pm.isPresent()) {
            PaymentMethod method = pm.get();
            return new PaymentMethodResponseDTO(method);
        } else {
            throw new NotFoundException("User/method not found!");
        }
    }
}
