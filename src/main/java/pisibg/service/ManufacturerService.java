package pisibg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.NotFoundException;
import pisibg.model.dto.CategoryResponseDTO;
import pisibg.model.dto.ManufacturerEditRequestDTO;
import pisibg.model.dto.ManufacturerRequestDTO;
import pisibg.model.dto.ManufacturerResponseDTO;
import pisibg.model.pojo.Category;
import pisibg.model.pojo.Manufacturer;
import pisibg.model.repository.ManufacturerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ManufacturerService {
    @Autowired
    private ManufacturerRepository manufacturerRepository;

    public ManufacturerResponseDTO add(ManufacturerRequestDTO manufacturerRequestDTO) {

        String name = manufacturerRequestDTO.getProducerName();

        if (manufacturerRepository.findByProducerName(name) != null) {
            throw new BadRequestException("Manufacturer already exists");
        }
        else {
            Manufacturer manufacturer = new Manufacturer(manufacturerRequestDTO);
            manufacturer = manufacturerRepository.save(manufacturer);
            return new ManufacturerResponseDTO(manufacturer);
        }
    }
    public ManufacturerResponseDTO edit (ManufacturerEditRequestDTO manufacturerEditRequestDTO){

        if(manufacturerEditRequestDTO.getCurrentProducerName().equals(manufacturerEditRequestDTO.getNewProducerName())){
            throw new BadRequestException("You didn't make any change!");
        }
        else {
            if(manufacturerRepository.findById(manufacturerEditRequestDTO.getId()).isPresent()){
                throw new NotFoundException("Manufacturer not found");
            }
            else {
                if(manufacturerRepository.findByProducerName(manufacturerEditRequestDTO.getNewProducerName()) != null){
                    throw new NotFoundException("Manufacturer with this name already exists");
                }
                else {
                    Manufacturer manufacturer = new Manufacturer();
                    manufacturer.setId(manufacturerEditRequestDTO.getId());
                    manufacturer.setProducerName(manufacturerEditRequestDTO.getNewProducerName());
                    manufacturerRepository.save(manufacturer);
                    return new ManufacturerResponseDTO(manufacturer);
                }
            }
        }

    }
    public List<ManufacturerResponseDTO> getAll() {
        List<Manufacturer> manufacturers = manufacturerRepository.findAll();
        List<ManufacturerResponseDTO> manufacturerResponseDTOList = new ArrayList<>();
        if(manufacturers.isEmpty()){
            throw new NotFoundException("Manufacturers not found");
        }
        else {
            for(Manufacturer m: manufacturers){
                manufacturerResponseDTOList.add(new ManufacturerResponseDTO(m));
            }
            return manufacturerResponseDTOList;
        }

    }
    //TODO FIX EXCEPTION
    public ManufacturerResponseDTO getById(int manufacturer_id) {
        Optional<Manufacturer> temp = manufacturerRepository.findById(manufacturer_id);
        if(!temp.isPresent()){
            throw new NotFoundException("Manufacturer not found");
        }
        else {
            Manufacturer manufacturer = temp.get();
            ManufacturerResponseDTO manufacturerResponseDTO = new ManufacturerResponseDTO(manufacturer);
            return manufacturerResponseDTO;
        }
    }
}
