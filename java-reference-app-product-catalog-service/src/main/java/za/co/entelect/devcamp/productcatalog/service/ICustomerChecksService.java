
package za.co.entelect.devcamp.productcatalog.service;

import java.util.List;
import za.co.entelect.devcamp.productcatalog.model.OrderCustomerChecks;
import za.co.entelect.devcamp.productcatalog.requests.SaveCustomerChecksRequest;

public interface ICustomerChecksService {

    List<OrderCustomerChecks> SaveCustomerChecks(List<SaveCustomerChecksRequest> customerChecks) throws Exception;
}

