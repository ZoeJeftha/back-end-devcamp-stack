package za.co.entelect.devcamp.productcatalog.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import za.co.entelect.devcamp.productcatalog.enums.CustomerChecksEnum;
import za.co.entelect.devcamp.productcatalog.model.OrderCustomerChecks;
import za.co.entelect.devcamp.productcatalog.repository.OrderCustomerChecksRepository;
import za.co.entelect.devcamp.productcatalog.requests.SaveCustomerChecksRequest;
import za.co.entelect.devcamp.productcatalog.responses.CustomerChecksResponse;
import za.co.entelect.devcamp.productcatalog.service.CustomerChecksService;

@ExtendWith(MockitoExtension.class)
public class CustomerChecksServiceTest {

    @Mock
    private OrderCustomerChecksRepository orderCustomerChecksRepository;

    @InjectMocks
    private CustomerChecksService customerChecksService;

    @BeforeEach
    void setUp()
    {
        customerChecksService = new CustomerChecksService(orderCustomerChecksRepository);
    }

    @Test
    void SaveCustomerChecks_shouldSaveCustomerChecks() throws Exception
    {
        SaveCustomerChecksRequest request = new SaveCustomerChecksRequest();
        request.setOrderId(100L);
        request.setHasPassed(true);
        request.setCustomerCheck(CustomerChecksEnum.KYC_CHECK);

        OrderCustomerChecks savedCheck = new OrderCustomerChecks();
        savedCheck.setOrderId(100L);
        savedCheck.setHasPassed(true);
        savedCheck.setCustomerChecksId(1L);

        when(orderCustomerChecksRepository.saveAll(anyList()))
                .thenReturn(List.of(savedCheck));

        List<OrderCustomerChecks> result =
                customerChecksService.SaveCustomerChecks(List.of(request));

        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getOrderId());
        assertEquals(true, result.get(0).getHasPassed());
        assertEquals(1L, result.get(0).getCustomerChecksId());

        verify(orderCustomerChecksRepository).saveAll(anyList());
    }

    @Test
    void GetCustomerChecks_ShouldReturnCustomerChecksList()
    {
        Long orderId = 100L;

        OrderCustomerChecks kycCheck = new OrderCustomerChecks();
        kycCheck.setCustomerChecksId(1L);
        kycCheck.setHasPassed(true);

        OrderCustomerChecks fraudCheck = new OrderCustomerChecks();
        fraudCheck.setCustomerChecksId(2L);
        fraudCheck.setHasPassed(false);

        when(orderCustomerChecksRepository.findByOrderId(orderId))
                .thenReturn(List.of(kycCheck, fraudCheck));

        List<CustomerChecksResponse> result =
                customerChecksService.getCustomerChecks(orderId);

        assertEquals(2, result.size());

        assertEquals("KYC Check", result.get(0).getCustomerCheck());
        assertEquals(true, result.get(0).getHasPassed());

        assertEquals("Fraud Check", result.get(1).getCustomerCheck());
        assertEquals(false, result.get(1).getHasPassed());

        verify(orderCustomerChecksRepository).findByOrderId(orderId);

    }
}