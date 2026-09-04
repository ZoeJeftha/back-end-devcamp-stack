
package za.co.entelect.devcamp.productcatalog.service;

import za.co.entelect.devcamp.productcatalog.exception.NotFoundException;
import za.co.entelect.devcamp.productcatalog.requests.OrderRequest;
import za.co.entelect.devcamp.productcatalog.responses.OrderResponse;

public interface IOrderService
{
    OrderResponse SaveOrder(OrderRequest request) throws Exception;

    OrderResponse GetOrder(Long orderId) throws Exception, NotFoundException;
}

