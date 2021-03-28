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
import pisibg.utility.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ManufacturerService {
    @Autowired
    private ManufacturerRepository manufacturerRepository;

    public ManufacturerResponseDTO add(ManufacturerRequestDTO manufacturerRequestDTO) {

        String name = manufacturerRequestDTO.getProducerName();
        if(!Validator.isValidString(name)){
            throw new BadRequestException("You have entered an empty text!");
        }
        else {
            if (manufacturerRepository.findByProducerName(name) != null) {
                throw new BadRequestException("Manufacturer already exists");
            }
            else {
                Manufacturer manufacturer = new Manufacturer(manufacturerRequestDTO);
                manufacturer = manufacturerRepository.save(manufacturer);
                return new ManufacturerResponseDTO(manufacturer);
            }
        }
    }
    public ManufacturerResponseDTO edit (ManufacturerEditRequestDTO manufacturerEditRequestDTO){
        if(!(Validator.isValidString(manufacturerEditRequestDTO.getCurrentProducerName())||(!Validator.isValidString(manufacturerEditRequestDTO.getNewProducerName())))){
            throw new BadRequestException("You have entered and empty text!");
        }
        else {
            if(manufacturerEditRequestDTO.getCurrentProducerName().equals(manufacturerEditRequestDTO.getNewProducerName())){
                throw new BadRequestException("You didn't make any change!");
            }
            else {
                if(!Validator.isValidInteger(manufacturerEditRequestDTO.getId())){
                    throw new BadRequestException("Please put number greater than 0!");
                }
                else{
                    if(manufacturerRepository.findById(manufacturerEditRequestDTO.getId())==null){
                        throw new NotFoundException("Manufacturer not found");
                    }
                    else {
                        if(!Validator.isValidString(manufacturerEditRequestDTO.getNewProducerName())){
                            throw new BadRequestException("You have entered an empty text!");
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
        Manufacturer manufacturer = manufacturerRepository.findById(manufacturer_id);
        if(manufacturer==null){
            throw new NotFoundException("Manufacturer not found");
        }
        else {
            ManufacturerResponseDTO manufacturerResponseDTO = new ManufacturerResponseDTO(manufacturer);
            return manufacturerResponseDTO;
        }
    }
}