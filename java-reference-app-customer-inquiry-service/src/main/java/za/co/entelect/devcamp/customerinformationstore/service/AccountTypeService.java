package za.co.entelect.devcamp.customerinformationstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import za.co.entelect.devcamp.customerinformationstore.controller.AccountTypesApiDelegate;
import za.co.entelect.devcamp.customerinformationstore.model.AccountType;
import za.co.entelect.devcamp.customerinformationstore.model.AccountTypeDto;
import za.co.entelect.devcamp.customerinformationstore.repository.AccountRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountTypeService implements AccountTypesApiDelegate {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountTypeService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public ResponseEntity<AccountTypeDto> addAccountTypes(AccountTypeDto accountTypeDto) {
        AccountType accountType = new AccountType(accountTypeDto.getDescription(), accountTypeDto.getName());
        AccountType savedAccountType = accountRepository.save(accountType);
        return ResponseEntity.ok(savedAccountType.toAccountTypeDto());
    }

    @Override
    public ResponseEntity<List<AccountTypeDto>> getAccountTypes() {
        List<AccountType> all = accountRepository.findAll();
        List<AccountTypeDto> accountTypeDtos = all.stream().map(AccountType::toAccountTypeDto).collect(Collectors.toList());
        return ResponseEntity.ok(accountTypeDtos);
    }
}
