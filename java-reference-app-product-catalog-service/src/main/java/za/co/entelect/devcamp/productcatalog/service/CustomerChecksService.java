package za.co.entelect.devcamp.productcatalog.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import za.co.entelect.devcamp.productcatalog.model.OrderCustomerChecks;
import za.co.entelect.devcamp.productcatalog.repository.OrderCustomerChecksRepository;
import za.co.entelect.devcamp.productcatalog.requests.SaveCustomerChecksRequest;

@Service
public class CustomerChecksService implements ICustomerChecksService {

    public final OrderCustomerChecksRepository orderCustomerChecksRepository;

    @Autowired
    public CustomerChecksService(OrderCustomerChecksRepository orderCustomerChecksRepository)
    {
        this.orderCustomerChecksRepository = orderCustomerChecksRepository;
    }

    @Override
    public List<OrderCustomerChecks> SaveCustomerChecks(List<SaveCustomerChecksRequest> customerChecks) throws Exception
    {
        try
        {
            List<OrderCustomerChecks> orderCustomerChecksList = new ArrayList<>();
            for(SaveCustomerChecksRequest check: customerChecks)
            {
                OrderCustomerChecks newCheck = new OrderCustomerChecks();
                newCheck.setOrderId(check.getOrderId());
                newCheck.setHasPassed(check.isHasPassed());

                int customerCheckEnumValue = check.getCustomerCheck().ordinal() + 1;
                newCheck.setCustomerChecksId((long)customerCheckEnumValue);

                orderCustomerChecksList.add(newCheck);
            }

            List<OrderCustomerChecks> savedChecks = orderCustomerChecksRepository.saveAll(orderCustomerChecksList);
            return savedChecks;
        }
        catch(Exception e)
        {
            System.out.println("--------------------Exception in CustomerChecksService: " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}