package pisibg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.NotFoundException;
import pisibg.model.dto.ManufacturerRequestDTO;
import pisibg.model.dto.ManufacturerResponseDTO;
import pisibg.model.pojo.Manufacturer;
import pisibg.model.repository.ManufacturerRepository;

@Service
public class ManufacturerService {
    @Autowired
    private ManufacturerRepository manufacturerRepository;

    public ManufacturerResponseDTO addManufacturer(ManufacturerRequestDTO manufacturerRequestDTO){

        String name = manufacturerRequestDTO.getProducerName();

        if(manufacturerRepository.findByProducerName(name) != null){
            throw new BadRequestException("Manufacturer already exists");
        }
        Manufacturer manufacturer = new Manufacturer(manufacturerRequestDTO);
        manufacturer = manufacturerRepository.save(manufacturer);
        return new ManufacturerResponseDTO(manufacturer);
    }
    public ManufacturerResponseDTO еdit(ManufacturerRequestDTO manufacturerRequestDTO){

        String name = manufacturerRequestDTO.getProducerName();

        if(manufacturerRepository.findByProducerName(name) == null){
            throw new NotFoundException("Manufacturer not found");
        }
        Manufacturer manufacturer = new Manufacturer(manufacturerRequestDTO);
        manufacturer = manufacturerRepository.save(manufacturer);
        return new ManufacturerResponseDTO(manufacturer);
    }
}
