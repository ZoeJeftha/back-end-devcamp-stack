package za.co.entelect.devcamp.productcatalog.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import za.co.entelect.devcamp.productcatalog.model.OrderItems;
import za.co.entelect.devcamp.productcatalog.model.Orders;
import za.co.entelect.devcamp.productcatalog.repository.OrderRepository;
import za.co.entelect.devcamp.productcatalog.repository.OrderItemRepository;
import za.co.entelect.devcamp.productcatalog.requests.OrderRequest;
import za.co.entelect.devcamp.productcatalog.responses.OrderResponse;

@Slf4j
@Service
public class OrderService implements IOrderService
{
    public final OrderRepository orderRepository;
    public final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository)
    {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public OrderResponse SaveOrder(OrderRequest request) throws Exception
    {
        try
        {
            log.info("SaveOrder");
            Orders order = new Orders();
            order.setCustomerId(request.getCustomerId());
            order.setStatus(request.getStatus());
            order.setContractUrl(request.getContractUrl());

            LocalDateTime now = LocalDateTime.now();
            order.setCreatedAt(now);

            log.info("SaveOrder order: " + order);

            Orders savedOrder = orderRepository.save(order);

            log.info("SaveOrder savedOrder: " + savedOrder);

            OrderItems orderItems = new OrderItems();
            orderItems.setProductId(request.getProductId());
            orderItems.setOrderId(savedOrder.getOrderId());

            OrderItems savedOrderItems = orderItemRepository.save(orderItems);
            log.info("SaveOrder savedOrderItems: " + orderItems);

            OrderResponse response = new OrderResponse();

            response.setOrderId(order.getOrderId());
            response.setStatus(request.getStatus());

            log.info("SaveOrder response: " + response);
            return response;
        }
        catch(Exception e)
        {
            throw new Exception("Failed to save order to db: "+ e.getMessage());
        }
    }
}