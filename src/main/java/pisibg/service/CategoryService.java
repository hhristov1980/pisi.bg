package pisibg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pisibg.exceptions.BadRequestException;
import pisibg.model.dto.CategoryRequestDTO;
import pisibg.model.dto.CategoryResponseDTO;
import pisibg.model.dto.ManufacturerRequestDTO;
import pisibg.model.dto.ManufacturerResponseDTO;
import pisibg.model.pojo.Category;
import pisibg.model.pojo.Manufacturer;
import pisibg.model.repository.CategoryRepository;
import pisibg.model.repository.ManufacturerRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryResponseDTO addManufacturer(CategoryRequestDTO categoryRequestDTO){

        String name = categoryRequestDTO();

        if(categoryRepository.findByProducerName(name) != null){
            throw new BadRequestException("Manufacturer already exists");
        }
        Category category = new Category(categoryRequestDTO);
        category = categoryRepository.save(category);
        return new ManufacturerResponseDTO(category);
    }
}
