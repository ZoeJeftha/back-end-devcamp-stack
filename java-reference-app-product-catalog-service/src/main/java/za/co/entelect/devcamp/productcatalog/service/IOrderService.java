
package za.co.entelect.devcamp.productcatalog.service;

import java.util.List;
import za.co.entelect.devcamp.productcatalog.dto.CustomerDto;
import za.co.entelect.devcamp.productcatalog.exception.NotFoundException;
import za.co.entelect.devcamp.productcatalog.requests.OrderRequest;
import za.co.entelect.devcamp.productcatalog.requests.OrderStatusUpdateRequest;
import za.co.entelect.devcamp.productcatalog.responses.OrderResponse;

public interface IOrderService
{
    OrderResponse SaveOrder(OrderRequest request) throws Exception;

    OrderResponse GetOrder(Long orderId) throws Exception;

    List<OrderResponse> GetMyOrders(CustomerDto customer) throws Exception;

    OrderResponse UpdateOrderStatus(OrderStatusUpdateRequest request) throws Exception;

    boolean CheckForExistingOrder(CustomerDto customer, long productId) throws Exception;
}

