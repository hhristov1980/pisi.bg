package pisibg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.NotFoundException;
import pisibg.model.dto.discountDTO.DiscountEditRequestDTO;
import pisibg.model.dto.discountDTO.DiscountRequestDTO;
import pisibg.model.dto.discountDTO.DiscountResponseDTO;
import pisibg.model.pojo.Discount;
import pisibg.model.pojo.User;
import pisibg.model.repository.DiscountRepository;
import pisibg.model.repository.UserRepository;
import pisibg.utility.EmailServiceImpl;
import pisibg.utility.Validator;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class DiscountService {
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailServiceImpl emailService;

    public DiscountResponseDTO add(DiscountRequestDTO discountRequestDTO) {

        String description = discountRequestDTO.getDescription();
//        if (!Validator.isValidString(description)) {
//            throw new BadRequestException("You have entered empty text!");
//        }
        if (discountRepository.findByDescription(description) != null) {
            throw new BadRequestException("Discount already exists");
        }
//        if (!Validator.isValidInteger(discountRequestDTO.getPercent())) {
//            throw new BadRequestException("You put wrong percent!");
//        }
        if (!Validator.isValidDate(discountRequestDTO.getFromDate()) && !Validator.isValidDate(discountRequestDTO.getToDate())) {
            throw new BadRequestException("You put invalid data!");
        }
        if (discountRequestDTO.getFromDate().isAfter(discountRequestDTO.getToDate())) {
            throw new BadRequestException("You cannot put end date before start date!");
        }
        Discount discount = new Discount(discountRequestDTO);
        discount = discountRepository.save(discount);
        List<User> list = userRepository.findAll();
        for (int i = 0; i < list.size(); i++) {
            Optional<User> u = userRepository.findById(i);
            if (u.isPresent()) {
                User user = u.get();
                String email = user.getEmail();
                if (email.matches(UserService.REGEX_EMAIL)) {
                    Discount finalDiscount = discount;
                    Runnable r = new Runnable() {

                        @Override
                        public void run() {
                            emailService.sendSimpleMessage(user.getEmail(), "We have new discount "+ finalDiscount.getPercent()+"% valid from "+
                                    finalDiscount.getFromDate()+" to "+ finalDiscount.getToDate(), finalDiscount.getDescription());
                        }
                    };
                    Thread t = new Thread(r);
                    t.start();
                }
            }
        }
        return new DiscountResponseDTO(discount);
    }

    public DiscountResponseDTO edit(DiscountEditRequestDTO discountEditRequestDTO) {

        if (discountEditRequestDTO.getCurrentDescription().equals(discountEditRequestDTO.getNewDescription())
                && discountEditRequestDTO.getCurrentPercent().equals(discountEditRequestDTO.getNewPercent())
                && discountEditRequestDTO.getCurrentFromDate().isEqual(discountEditRequestDTO.getNewFromDate())
                && discountEditRequestDTO.getNewFromDate().isEqual(discountEditRequestDTO.getNewFromDate())
                && discountEditRequestDTO.getCurrentToDate().isEqual(discountEditRequestDTO.getNewToDate())
                && discountEditRequestDTO.getCurrentIsActive() == discountEditRequestDTO.getNewIsActive()) {
            throw new BadRequestException("You didn't make any change!");
        }
//        if (!Validator.isValidInteger(discountEditRequestDTO.getId())) {
//            throw new BadRequestException("Please put number greater than 0!");
//        }
        if (discountRepository.getById(discountEditRequestDTO.getId()) == null) {
            throw new NotFoundException("Discount not found");
        }
        String description = discountEditRequestDTO.getNewDescription();
//        if (!Validator.isValidString(description)) {
//            throw new BadRequestException("You have entered empty text!");
//        }
//        if (!Validator.isValidInteger(discountEditRequestDTO.getNewPercent())) {
//            throw new BadRequestException("You put wrong percent!");
//        }
        if (!Validator.isValidDate(discountEditRequestDTO.getNewFromDate()) && !Validator.isValidDate(discountEditRequestDTO.getNewToDate())) {
            throw new BadRequestException("You put invalid data!");
        }
        if (discountEditRequestDTO.getNewFromDate().isAfter(discountEditRequestDTO.getNewToDate())) {
            throw new BadRequestException("You cannot put end date before start date!");
        }
        Discount discount = new Discount();
        discount.setId(discountEditRequestDTO.getId());
        discount.setDescription(discountEditRequestDTO.getNewDescription());
        discount.setPercent(discountEditRequestDTO.getNewPercent());
        discount.setFromDate(discountEditRequestDTO.getNewFromDate());
        discount.setToDate(discountEditRequestDTO.getNewToDate());
        discount.setActive(discountEditRequestDTO.getNewIsActive());
        discountRepository.save(discount);
        List<User> list = userRepository.findAll();
        for (int i = 0; i < list.size(); i++) {
            Optional<User> u = userRepository.findById(i);
            if (u.isPresent()) {
                User user = u.get();
                String email = user.getEmail();
                if (email.matches(UserService.REGEX_EMAIL)) {
                    Discount finalDiscount = discount;
                    Runnable r = new Runnable() {

                        @Override
                        public void run() {
                            emailService.sendSimpleMessage(user.getEmail(), "Edited new discount "+ finalDiscount.getPercent()+"% valid from "+
                                    finalDiscount.getFromDate()+" to "+ finalDiscount.getToDate(), finalDiscount.getDescription());
                        }
                    };
                    Thread t = new Thread(r);
                    t.start();
                }
            }
        }
        return new DiscountResponseDTO(discount);
    }

    public List<DiscountResponseDTO> getAll() {
        List<Discount> discounts = discountRepository.findAll();
        List<DiscountResponseDTO> discountResponseDTOList = new ArrayList<>();
        if (discounts.isEmpty()) {
            throw new NotFoundException("Discounts not found");
        }
        for (Discount d : discounts) {
            if (d.isActive()) {
                discountResponseDTOList.add(new DiscountResponseDTO(d));
            }
            if (discountResponseDTOList.isEmpty()) {
                throw new NotFoundException("No active discounts found");
            }
            Collections.sort(discountResponseDTOList, ((o1, o2) -> Integer.compare(o1.getPercent(), o2.getPercent())));

        }
        return discountResponseDTOList;
    }

    public DiscountResponseDTO getById(int discount_id) {
        Discount discount = discountRepository.getById(discount_id);
        if (discount == null) {
            throw new NotFoundException("Discount not found");
        } else {
            DiscountResponseDTO discountResponseDTO = new DiscountResponseDTO(discount);
            return discountResponseDTO;
        }
    }
}
