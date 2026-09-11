
package za.co.entelect.devcamp.fulfilment.interfaces;

import za.co.entelect.devcamp.fulfilment.requests.OrderStatusUpdateRequest;
import za.co.entelect.devcamp.fulfilment.responses.OrderResponse;

public interface IProductService
{
    OrderResponse UpdateOrder(OrderStatusUpdateRequest request) throws Exception;
}

