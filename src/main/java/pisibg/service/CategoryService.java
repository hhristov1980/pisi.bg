package pisibg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.NotFoundException;
import pisibg.model.dto.*;
import pisibg.model.pojo.Category;
import pisibg.model.pojo.Manufacturer;
import pisibg.model.repository.CategoryRepository;
import pisibg.model.repository.ManufacturerRepository;
import pisibg.utility.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryResponseDTO add(CategoryRequestDTO categoryRequestDTO){

        String name = categoryRequestDTO.getName();
        if(!Validator.isValidString(name)){
            throw new BadRequestException("You have entered empty text!");
        }
        else {
            if(categoryRepository.findByName(name) != null){
                throw new BadRequestException("Category already exists");
            }
            else{
                Category category = new Category(categoryRequestDTO);
                category = categoryRepository.save(category);
                return new CategoryResponseDTO(category);
            }
        }

    }
    public CategoryResponseDTO edit (CategoryEditRequestDTO categoryEditRequestDTO){

        if(!Validator.isValidString(categoryEditRequestDTO.getNewCategoryName())){
            throw new BadRequestException("You have entered empty text!");
        }
        else {
            if(categoryEditRequestDTO.getCurrentCategoryName().equals(categoryEditRequestDTO.getNewCategoryName())){
                throw new BadRequestException("You didn't make any change!");
            }
            else {
                if(!Validator.isValidInteger(categoryEditRequestDTO.getId())){
                    throw new BadRequestException("Please put number greater than 0!");
                }
                else {
                    if(!categoryRepository.findById(categoryEditRequestDTO.getId()).isPresent()){
                        throw new NotFoundException("Category not found");
                    }
                    else {
                        if(categoryRepository.findByName(categoryEditRequestDTO.getNewCategoryName()) != null){
                            throw new NotFoundException("Category with this name already exists");
                        }
                        else {
                            Category category = new Category();
                            category.setId(categoryEditRequestDTO.getId());
                            category.setName(categoryEditRequestDTO.getNewCategoryName());
                            categoryRepository.save(category);
                            return new CategoryResponseDTO(category);
                        }
                    }
                }

            }
        }

    }
    public List<CategoryResponseDTO> getAll() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponseDTO> categoryResponseDTOList = new ArrayList<>();
        if(categories.isEmpty()){
            throw new NotFoundException("Categories not found");
        }
        else {
            for(Category c: categories){
                categoryResponseDTOList.add(new CategoryResponseDTO(c));
            }
            return categoryResponseDTOList;
        }

    }
    public CategoryResponseDTO getById(int category_id) {
        Optional<Category> temp = categoryRepository.findById(category_id);
        if(!temp.isPresent()){
            throw new NotFoundException("Category not found");
        }
        else {
            Category category = temp.get();
            CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO(category);
            return categoryResponseDTO;
        }
    }
}


