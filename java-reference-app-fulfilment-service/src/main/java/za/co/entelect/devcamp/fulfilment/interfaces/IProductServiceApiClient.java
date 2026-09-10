
package za.co.entelect.devcamp.fulfilment.interfaces;

import org.springframework.security.core.Authentication;
import za.co.entelect.devcamp.productcatalog.requests.OrderStatusUpdateRequest;
import za.co.entelect.devcamp.fulfilment.responses.OrderResponse;

public interface IProductServiceApiClient
{
    OrderResponse UpdateOrder(String token, OrderStatusUpdateRequest request);
}

