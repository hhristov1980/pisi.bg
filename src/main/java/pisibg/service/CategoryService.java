package pisibg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.NotFoundException;
import pisibg.model.dto.categoryDTO.CategoryEditRequestDTO;
import pisibg.model.dto.categoryDTO.CategoryRequestDTO;
import pisibg.model.dto.categoryDTO.CategoryResponseDTO;
import pisibg.model.pojo.Category;
import pisibg.model.repository.CategoryRepository;
import pisibg.utility.Validator;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryResponseDTO add(CategoryRequestDTO categoryRequestDTO) {

        String name = categoryRequestDTO.getName();
//        if (!Validator.isValidString(name)) {
//            throw new BadRequestException("You have entered empty text!");
//        }
        if (categoryRepository.findByName(name) != null) {
            throw new BadRequestException("Category already exists");
        }
        Category category = new Category(categoryRequestDTO);
        category = categoryRepository.save(category);
        return new CategoryResponseDTO(category);
    }

    public CategoryResponseDTO edit(CategoryEditRequestDTO categoryEditRequestDTO) {

//        if (!Validator.isValidString(categoryEditRequestDTO.getNewCategoryName())) {
//            throw new BadRequestException("You have entered empty text!");
//        }
        if (categoryEditRequestDTO.getCurrentCategoryName().equals(categoryEditRequestDTO.getNewCategoryName())) {
            throw new BadRequestException("You didn't make any change!");
        }
//        if (!Validator.isValidInteger(categoryEditRequestDTO.getId())) {
//            throw new BadRequestException("Please put number greater than 0!");
//        }
        if (categoryRepository.findById(categoryEditRequestDTO.getId()).isEmpty()) {
            throw new NotFoundException("Category not found");
        }
        if (categoryRepository.findByName(categoryEditRequestDTO.getNewCategoryName()) != null) {
            throw new NotFoundException("Category with this name already exists");
        }

        Category category = new Category();
        category.setId(categoryEditRequestDTO.getId());
        category.setName(categoryEditRequestDTO.getNewCategoryName());
        category.setSubcategories(categoryRepository.findById(categoryEditRequestDTO.getId()).get().getSubcategories());
        categoryRepository.save(category);
        return new CategoryResponseDTO(category);
    }

    public List<CategoryResponseDTO> getAll() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponseDTO> categoryResponseDTOList = new ArrayList<>();
        if (categories.isEmpty()) {
            throw new NotFoundException("Categories not found");
        }
        for (Category c : categories) {
            categoryResponseDTOList.add(new CategoryResponseDTO(c));
        }
        return categoryResponseDTOList;
    }

    public CategoryResponseDTO getById(int category_id) {
        Category category = categoryRepository.getById(category_id);
        if (category == null) {
            throw new NotFoundException("Category not found");
        }
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO(category);
        return categoryResponseDTO;
    }
}


