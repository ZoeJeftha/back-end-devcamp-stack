package za.co.entelect.devcamp.customerinformationstore.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.co.entelect.devcamp.customerinformationstore.TestDataUtil;
import za.co.entelect.devcamp.customerinformationstore.model.AccountType;
import za.co.entelect.devcamp.customerinformationstore.model.AccountTypeDto;
import za.co.entelect.devcamp.customerinformationstore.repository.AccountRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static za.co.entelect.devcamp.customerinformationstore.TestDataUtil.CURRENT_ACCOUNT_TYPES;
import static za.co.entelect.devcamp.customerinformationstore.TestDataUtil.getAccountTypeDto;

@SpringBootTest
public class AccountTypeServiceTest {

    private final AccountTypeService accountTypeService;
    private final AccountRepository accountRepository;

    @Autowired
    public AccountTypeServiceTest(AccountTypeService accountTypeService, AccountRepository accountRepository) {
        this.accountTypeService = accountTypeService;
        this.accountRepository = accountRepository;
    }

    @Test
    void givenAccountExistsInDB() {
        ResponseEntity<List<AccountTypeDto>> accountTypes = accountTypeService.getAccountTypes();
        assertEquals(TestDataUtil.CURRENT_ACCOUNT_TYPES, accountTypes.getBody().size(), "Account Status size should be 8");
        assertEquals(HttpStatus.OK, accountTypes.getStatusCode(), "HTTPStatus should be OK");
    }

    @Test
    void givenAccountTypeDoesNotExistInDB_WhenAddingInNewAccountType_ThenResponseCodeShouldBe200() {
        AccountTypeDto accountTypeDto = getAccountTypeDto();
        ResponseEntity<AccountTypeDto> accountTypeDtoResponseEntity = accountTypeService.addAccountTypes(accountTypeDto);
        assertEquals(HttpStatus.OK, accountTypeDtoResponseEntity.getStatusCode());
        AccountTypeDto body = accountTypeDtoResponseEntity.getBody();
        assertEquals(TestDataUtil.CURRENT_ACCOUNT_TYPES, body.getId().intValue());
        assertEquals(accountTypeDto.getName(), body.getName());
        assertEquals(accountTypeDto.getDescription(), body.getDescription());
        Optional<AccountType> byId = accountRepository.findById((long) CURRENT_ACCOUNT_TYPES);
        assertTrue(byId.isPresent());
        AccountType accountType = byId.get();
        assertEquals(accountTypeDto.getName(), accountType.getName());
        assertEquals(accountTypeDto.getDescription(), accountType.getDescription());
    }


}
