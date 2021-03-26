package pisibg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.NotFoundException;
import pisibg.model.dto.*;
import pisibg.model.pojo.Category;
import pisibg.model.pojo.Discount;
import pisibg.model.repository.DiscountRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class DiscountService {
    @Autowired
    private DiscountRepository discountRepository;

    public DiscountResponseDTO add(DiscountRequestDTO discountRequestDTO){

        String description = discountRequestDTO.getDescription();

        if(discountRepository.findByDescription(description) != null){
            throw new BadRequestException("Discount already exists");
        }
        else{
            if(discountRequestDTO.getFromDate().isAfter(discountRequestDTO.getToDate())){
                throw new BadRequestException("You cannot put end date before start date!");
            }
            Discount discount = new Discount(discountRequestDTO);
            discount = discountRepository.save(discount);
            return new DiscountResponseDTO(discount);
        }
    }

    public DiscountResponseDTO edit (DiscountEditRequestDTO discountEditRequestDTO){

        if(discountEditRequestDTO.getCurrentDescription().equals(discountEditRequestDTO.getNewDescription())
            &&discountEditRequestDTO.getCurrentPercent()== discountEditRequestDTO.getNewPercent()
            &&discountEditRequestDTO.getCurrentFromDate().isEqual(discountEditRequestDTO.getNewFromDate())
            &&discountEditRequestDTO.getNewFromDate().isEqual(discountEditRequestDTO.getNewFromDate())
            &&discountEditRequestDTO.getCurrentToDate().isEqual(discountEditRequestDTO.getNewToDate())
            &&discountEditRequestDTO.isCurrentIsActive()== discountEditRequestDTO.isNewIsActive()){
            throw new BadRequestException("You didn't make any change!");
        }
        else {
            if(!discountRepository.findById(discountEditRequestDTO.getId()).isPresent()){
                throw new NotFoundException("Discount not found");
            }
            else {
                //TODO to think about more checks if needed
                if(discountEditRequestDTO.getNewFromDate().isAfter(discountEditRequestDTO.getNewToDate())){
                    throw new BadRequestException("You cannot put end date before start date!");
                }
                else{
                    Discount discount = new Discount();
                    discount.setId(discountEditRequestDTO.getId());
                    discount.setDescription(discountEditRequestDTO.getNewDescription());
                    discount.setPercent(discountEditRequestDTO.getNewPercent());
                    discount.setFromDate(discountEditRequestDTO.getNewFromDate());
                    discount.setToDate(discountEditRequestDTO.getNewToDate());
                    discount.setActive(discountEditRequestDTO.isNewIsActive());
                    discountRepository.save(discount);
                    return new DiscountResponseDTO(discount);

                }

            }
        }
    }
    public List<DiscountResponseDTO> getAll() {
        List<Discount> discounts = discountRepository.findAll();
        List<DiscountResponseDTO> discountResponseDTOList = new ArrayList<>();
        if(discounts.isEmpty()){
            throw new NotFoundException("Discounts not found");
        }
        else {
            for(Discount d: discounts){
                if(d.isActive()){
                    discountResponseDTOList.add(new DiscountResponseDTO(d));
                }
            }
            if(discountResponseDTOList.isEmpty()){
                throw new NotFoundException("No active discounts found");
            }
            else{
                Collections.sort(discountResponseDTOList,((o1, o2) -> Integer.compare(o1.getPercent(),o2.getPercent())));
                return discountResponseDTOList;
            }

        }

    }
    public DiscountResponseDTO getById(int discount_id) {
        Optional<Discount> temp = discountRepository.findById(discount_id);
        if(!temp.isPresent()){
            throw new NotFoundException("Discount not found");
        }
        else {
            Discount discount = temp.get();
            DiscountResponseDTO discountResponseDTO = new DiscountResponseDTO(discount);
            return discountResponseDTO;
        }
    }


}
