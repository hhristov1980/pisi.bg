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

import java.util.Optional;

@Service
public class SubCategoryService {
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public SubcategoryResponseDTO addSubCategory(SubCategoryRequestDTO subCategoryRequestDTO){


        if(!categoryRepository.findById(subCategoryRequestDTO.getCategoryId()).isPresent()){
            throw new BadRequestException("Category doesn't exists");
        }
        Subcategory subcategory = new Subcategory(subCategoryRequestDTO);
        subcategory.setCategory(categoryRepository.findById(subCategoryRequestDTO.getCategoryId()).get());

        subcategory = subCategoryRepository.save(subcategory);
        return new SubcategoryResponseDTO(subcategory);
    }

}
