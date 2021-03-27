package pisibg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.NotFoundException;
import pisibg.model.dto.*;
import pisibg.model.pojo.Category;
import pisibg.model.pojo.Subcategory;
import pisibg.model.repository.CategoryRepository;
import pisibg.model.repository.SubCategoryRepository;
import pisibg.utility.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SubCategoryService {
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public SubcategoryResponseDTO addSubCategory(SubCategoryRequestDTO subCategoryRequestDTO) {

        if (!Validator.isValidInteger(subCategoryRequestDTO.getCategoryId())){
            throw new BadRequestException("Invalid category id! Please enter number greater than 0");
        }
        else {
            if (!categoryRepository.findById(subCategoryRequestDTO.getCategoryId()).isPresent()) {
                throw new BadRequestException("Category doesn't exists");
            }
            else {
                if(!Validator.isValidString(subCategoryRequestDTO.getName())){
                    throw new BadRequestException("You have entered and empty text!");
                }
                else{
                    Subcategory subcategory = new Subcategory(subCategoryRequestDTO);
                    subcategory.setCategory(categoryRepository.findById(subCategoryRequestDTO.getCategoryId()).get());
                    if(subCategoryRepository.getByNameAndCategory_Id(subcategory.getName(), subCategoryRequestDTO.getCategoryId())!=null){
                        throw new BadRequestException("Combination already exists");
                    }
                    else {
                        subcategory = subCategoryRepository.save(subcategory);
                        return new SubcategoryResponseDTO(subcategory);
                    }
                }
            }
        }

    }

    public SubcategoryResponseDTO edit(SubCategoryEditRequestDTO subCategoryEditRequestDTO){

        if(!Validator.isValidInteger(subCategoryEditRequestDTO.getId())){
            throw new BadRequestException("Please put id greater than 0!");
        }
        else {
            if(!(Validator.isValidString(subCategoryEditRequestDTO.getCurrentSubcategoryName())||(!Validator.isValidString(subCategoryEditRequestDTO.getNewSubcategoryName())))){
                throw new BadRequestException("You have entered and empty text!");
            }
            else {
                if(subCategoryEditRequestDTO.getCurrentSubcategoryName().equals(subCategoryEditRequestDTO.getNewSubcategoryName())
                        && subCategoryEditRequestDTO.getCurrentCategory_id()== subCategoryEditRequestDTO.getNewCategory_id()){
                    throw new BadRequestException("You didn't make any change!");
                }
                else {
                    if(!subCategoryRepository.findById(subCategoryEditRequestDTO.getId()).isPresent()){
                        throw new NotFoundException("Subcategory not found!");
                    }
                    else {
                        if (!categoryRepository.findById(subCategoryEditRequestDTO.getCurrentCategory_id()).isPresent()) {
                            throw new NotFoundException("Category doesn't exists");
                        }
                        else {
                            Subcategory subcategory = new Subcategory();
                            subcategory.setId(subCategoryEditRequestDTO.getId());
                            subcategory.setName(subCategoryEditRequestDTO.getNewSubcategoryName());
                            subcategory.setCategory(categoryRepository.findById(subCategoryEditRequestDTO.getNewCategory_id()).get());
                            if(subCategoryRepository.getByNameAndCategory_Id(subcategory.getName(), subcategory.getCategory().getId())!=null){
                                throw new BadRequestException("Combination already exists");
                            }
                            else {
                                subcategory = subCategoryRepository.save(subcategory);
                                return new SubcategoryResponseDTO(subcategory);
                            }
                        }
                    }
                }
            }
        }
    }

    public List<SubcategoryResponseDTO> getAll() {
        List<Subcategory> subcategories = subCategoryRepository.findAll();
        List<SubcategoryResponseDTO> subcategoryResponseDTOList = new ArrayList<>();
        if(subcategories.isEmpty()){
            throw new NotFoundException("Subcategories not found");
        }
        else {
            for(Subcategory s: subcategories){
                subcategoryResponseDTOList.add(new SubcategoryResponseDTO(s));
            }
            return subcategoryResponseDTOList;
        }

    }
    public SubcategoryResponseDTO getById(int subcategory_id) {
        Optional<Subcategory> temp = subCategoryRepository.findById(subcategory_id);
        if(!temp.isPresent()){
            throw new NotFoundException("Subcategory not found");
        }
        else {
            Subcategory subcategory = temp.get();
            SubcategoryResponseDTO subcategoryResponseDTO = new SubcategoryResponseDTO(subcategory);
            return subcategoryResponseDTO;
        }
    }

}
