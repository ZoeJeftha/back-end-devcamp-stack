
package za.co.entelect.devcamp.productcatalog.service;

import java.util.List;
import za.co.entelect.devcamp.productcatalog.model.OrderCustomerChecks;
import za.co.entelect.devcamp.productcatalog.requests.SaveCustomerChecksRequest;
import za.co.entelect.devcamp.productcatalog.responses.CustomerChecksResponse;

public interface ICustomerChecksService {

    List<OrderCustomerChecks> SaveCustomerChecks(List<SaveCustomerChecksRequest> customerChecks) throws Exception;

    List<CustomerChecksResponse> getCustomerChecks(Long orderId);
}

