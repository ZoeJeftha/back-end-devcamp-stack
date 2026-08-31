package za.co.entelect.devcamp.customerinformationstore.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import za.co.entelect.devcamp.customerinformationstore.controller.CustomerApiDelegate;
import za.co.entelect.devcamp.customerinformationstore.model.*;
import za.co.entelect.devcamp.customerinformationstore.repository.*;
import za.co.entelect.devcamp.authenticationservice.helpers.CustomerHelper;
import za.co.entelect.devcamp.customerinformationstore.requests.CustomerEligibilityRequest;
import za.co.entelect.devcamp.customerinformationstore.client.ProductApiClient;
import za.co.entelect.devcamp.customerinformationstore.model.CustomerTypes;
import za.co.entelect.devcamp.customerinformationstore.model.AccountType;
import za.co.entelect.devcamp.customerinformationstore.responses.ApiResponse;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class CustomerService implements CustomerApiDelegate {

    private final CustomerRepository customerRepository;
    private final CustomerTypesRepository customerTypesRepository;
    private final CustomerDocumentRepository customerDocumentRepository;
    private final DocumentRepository documentRepository;
    private final AccountRepository accountRepository;
    private final CustomerAccountsRepository customerAccountsRepository;
    private final ProductApiClient productApiClient;

    @Autowired
    public CustomerService(CustomerRepository customerRepository,
                           CustomerTypesRepository customerTypesRepository,
                           CustomerDocumentRepository customerDocumentRepository,
                           DocumentRepository documentRepository,
                           AccountRepository accountRepository,
                           CustomerAccountsRepository customerAccountsRepository,
                           ProductApiClient productApiClient) {
        this.customerRepository = customerRepository;
        this.customerTypesRepository = customerTypesRepository;
        this.customerDocumentRepository = customerDocumentRepository;
        this.documentRepository = documentRepository;
        this.accountRepository = accountRepository;
        this.customerAccountsRepository = customerAccountsRepository;
        this.productApiClient = productApiClient;
    }

    @Override
    public ResponseEntity<CustomerDto> registerCustomer(CustomerDto customerDto) {
        Customer customer = new Customer(customerDto);

        Integer typeId = customerDto.getCustomerTypeId();
        if (typeId != null) {
            Long customerTypeId = typeId.longValue();
            Optional<CustomerTypes> customerTypesRepositoryById = customerTypesRepository.findById(customerTypeId);
            customerTypesRepositoryById.ifPresent(customer::setCustomerTypes);
        }

        Customer savedCustomer = customerRepository.save(customer);
        return new ResponseEntity<>(savedCustomer.toCustomerDto(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<AccountTypeDto>> getCustomerAccountsByCustomerId(Integer customerId) {
        List<CustomerAccounts> customerAccountsByCustomerId = customerAccountsRepository.findCustomerAccountsByCustomer_CustomerId(customerId.longValue());
        List<AccountTypeDto> accountDtoList = customerAccountsByCustomerId.stream().map(customerAccounts -> customerAccounts.getAccountType().toAccountTypeDto()).collect(Collectors.toList());
        return new ResponseEntity<>(accountDtoList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AddCustomerDocumentByCustomerId200Response> addCustomerDocumentByCustomerId(Integer customerId, DocumentDto documentDto) {
        Optional<Customer> customerById = customerRepository.findById(customerId.longValue());
        if (customerById.isPresent()) {
            Document document = new Document(documentDto);
            Document savedDocument = documentRepository.save(document);
            CustomerDocuments customerDocuments = new CustomerDocuments(customerById.get(), savedDocument);
            CustomerDocuments savedCustomerDocuments = customerDocumentRepository.save(customerDocuments);

            AddCustomerDocumentByCustomerId200Response addCustomerDocumentByCustomerId200Response = new AddCustomerDocumentByCustomerId200Response();
            addCustomerDocumentByCustomerId200Response.setDocumentId(savedCustomerDocuments.getCustomerDocumentsId().intValue());
            addCustomerDocumentByCustomerId200Response.setName(savedCustomerDocuments.getDocument().getName());
            return new ResponseEntity<>(addCustomerDocumentByCustomerId200Response, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<List<DocumentsDtoInner>> getCustomerDocumentsByCustomerId(Integer customerId) {
        List<CustomerDocuments> customerDocumentsByCustomerId = customerDocumentRepository.findCustomerDocumentsByCustomer_CustomerId(customerId.longValue());
        List<DocumentsDtoInner> documentDtoList = customerDocumentsByCustomerId.stream().map(customerDocuments -> customerDocuments.getDocument().toDocumentsDto()).collect(Collectors.toList());
        return new ResponseEntity<>(documentDtoList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DocumentDto> getCustomerDocumentByCustomerId(Integer customerId, Integer documentId) {
        CustomerDocuments customerDocumentByCustomerId = customerDocumentRepository.findCustomerDocumentsByCustomer_CustomerIdAndDocument_DocumentId(customerId.longValue(), documentId.longValue());
        if (customerDocumentByCustomerId == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(customerDocumentByCustomerId.getDocument().toDocumentDto(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CustomerDto> getCustomerById(Integer customerId) {
        Optional<Customer> byId = customerRepository.findById(customerId.longValue());
        if (byId.isPresent()) {
            CustomerDto customerDto = byId.get().toCustomerDto();
            return new ResponseEntity<>(customerDto, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> addCustomerAccountsToCustomerById(Integer customerId, Integer accountTypeId) {
        Optional<Customer> customerById = customerRepository.findById(customerId.longValue());
        Optional<AccountType> accountTypeById = accountRepository.findById(accountTypeId.longValue());
        if (customerById.isPresent() && accountTypeById.isPresent()) {
            Customer customer = customerById.get();
            AccountType accountType = accountTypeById.get();
            CustomerAccounts customerAccounts = new CustomerAccounts(accountType, customer);
            CustomerAccounts savedCustomerAccount = customerAccountsRepository.save(customerAccounts);
            customer.getCustomerAccounts().add(savedCustomerAccount);
            customerRepository.save(customer);
        } else {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> updateCustomerTypeToCustomerById(Integer customerId, Integer customerTypeId) {
        Optional<Customer> customerById = customerRepository.findById(customerId.longValue());
        Optional<CustomerTypes> customerTypesById = customerTypesRepository.findById(customerTypeId.longValue());
        if (customerById.isPresent() && customerTypesById.isPresent()) {
            Customer customer = customerById.get();
            CustomerTypes customerTypes = customerTypesById.get();
            customer.setCustomerTypes(customerTypes);
            customerRepository.save(customer);
        } else {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<CustomerDto> getCustomerByEmailAddress(String emailAddress) {
        Optional<Customer> customerByEmail = customerRepository.findCustomerByEmail(emailAddress);
        if (customerByEmail.isPresent()) {
            Customer customer = customerByEmail.get();
            String maskedIdNumber = CustomerHelper.maskIdNumber(customer.getIdNumber());
            customer.setIdNumber(maskedIdNumber);

            return ResponseEntity.ok(customer.toCustomerDto());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public CustomerDto OpenAccount(String emailAddress, Integer accountTypeId) throws Exception
    {
        try {
            Optional<Customer> customerByEmail = customerRepository.findCustomerByEmail(emailAddress);
            if (customerByEmail.isPresent()) {
                addCustomerAccountsToCustomerById(Math.toIntExact(customerByEmail.get().getCustomerId()), accountTypeId);
                ResponseEntity<CustomerDto> customer = getCustomerByEmailAddress(emailAddress);
                return customer.getBody();
            } else {
                throw new Exception("Customer not found");
            }
        }
        catch(Exception e)
        {
            throw new Exception("Error occurred while trying to open account", e);
        }
    }

    public ResponseEntity<ApiResponse<Boolean>> IsCustomerEligible(String jwt, String username, Long productId) throws Exception
    {
        Optional<Customer> customerByEmail = customerRepository.findCustomerByEmail(username);
        if (customerByEmail.isPresent()) {
            Customer customer = customerByEmail.get();

            CustomerTypes customerType = customer.getCustomerTypes();
            Long customerTypeId = customerType.getCustomerTypesId();

            List<CustomerAccounts> customerAccounts = customer.getCustomerAccounts();

            List<AccountType> accountTypes = customerAccounts.stream()
                    .map(CustomerAccounts::getAccountType)
                    .toList();

            List<Long> accountTypeIds = accountTypes.stream()
                            .map(AccountType::getAccountTypeId)
                            .toList();
            CustomerEligibilityRequest customerEligibilityRequest = new CustomerEligibilityRequest(productId,accountTypeIds, customerTypeId);

            ResponseEntity<ApiResponse<Boolean>> response = productApiClient.IsCustomerEligible(jwt, customerEligibilityRequest);
            return response;

        } else {
            throw new Exception("Customer not found");
        }
    }

}
