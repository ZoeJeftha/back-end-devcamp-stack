package za.co.entelect.devcamp.customerinformationstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import za.co.entelect.devcamp.customerinformationstore.controller.CustomerTypesApiDelegate;
import za.co.entelect.devcamp.customerinformationstore.model.CustomerTypes;
import za.co.entelect.devcamp.customerinformationstore.model.CustomerTypesDto;
import za.co.entelect.devcamp.customerinformationstore.repository.CustomerTypesRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerTypesService implements CustomerTypesApiDelegate {

    private final CustomerTypesRepository customerTypesRepository;

    @Autowired
    public CustomerTypesService(CustomerTypesRepository customerTypesRepository) {
        this.customerTypesRepository = customerTypesRepository;
    }

    @Override
    public ResponseEntity<CustomerTypesDto> addCustomerTypes(CustomerTypesDto customerTypesDto) {
        CustomerTypes customerTypes = new CustomerTypes(customerTypesDto);
        CustomerTypes savedCustomerType = customerTypesRepository.save(customerTypes);
        return new ResponseEntity<>(savedCustomerType.toCustomerTypesDto(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<CustomerTypesDto>> getCustomerTypes() {
        List<CustomerTypes> foundCustomerTypes = customerTypesRepository.findAll();
        List<CustomerTypesDto> customerTypesDtos = foundCustomerTypes.stream().map(CustomerTypes::toCustomerTypesDto).collect(Collectors.toList());
        return new ResponseEntity<>(customerTypesDtos, HttpStatus.OK);
    }


}
