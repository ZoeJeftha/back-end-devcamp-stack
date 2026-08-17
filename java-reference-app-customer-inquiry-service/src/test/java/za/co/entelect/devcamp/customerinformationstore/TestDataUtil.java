package za.co.entelect.devcamp.customerinformationstore;

import org.jetbrains.annotations.NotNull;
import za.co.entelect.devcamp.customerinformationstore.model.AccountTypeDto;
import za.co.entelect.devcamp.customerinformationstore.model.CustomerDto;
import za.co.entelect.devcamp.customerinformationstore.model.CustomerTypesDto;
import za.co.entelect.devcamp.customerinformationstore.model.DocumentDto;

import java.math.BigDecimal;

public class TestDataUtil {

    public static final int CURRENT_CUSTOMER_TYPES = 5;
    public static final int CURRENT_ACCOUNT_TYPES = 9;
    public static final int CURRENT_CUSTOMERS = 3;
    public static final int CURRENT_DOCUMENT = 2;

    @NotNull
    public static AccountTypeDto getAccountTypeDto() {
        String testAccountType = "Test Account Type";
        String description = "An account type used to confirm that these can be added in to the database";
        return new AccountTypeDto(BigDecimal.valueOf(9), testAccountType, description);
    }

    @NotNull
    public static CustomerTypesDto getCustomerTypesDto() {
        String testCustomerType = "Test Customer Type";
        String description = "Test Customer Type Description";
        return new CustomerTypesDto(testCustomerType, description);
    }

    @NotNull
    public static CustomerDto getCustomerDto() {
        String username = "NewCustomer@email.co.za";
        String firstName = "New";
        String lastName = "Customer";
        String idNumber = "9012315000082";

        return new CustomerDto(username, firstName, lastName, idNumber);
    }

    @NotNull
    public static DocumentDto getDocumentDto() {
        DocumentDto documentDto = new DocumentDto();
        documentDto.setDocument("Document");
        return documentDto;
    }
}
