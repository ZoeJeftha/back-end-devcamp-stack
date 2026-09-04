package za.co.entelect.devcamp.productcatalog.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import za.co.entelect.devcamp.productcatalog.dto.CustomerDto;
import za.co.entelect.devcamp.productcatalog.dto.ProductDto;
import za.co.entelect.devcamp.productcatalog.exception.NotFoundException;
import za.co.entelect.devcamp.productcatalog.model.OrderItems;
import za.co.entelect.devcamp.productcatalog.model.Orders;
import za.co.entelect.devcamp.productcatalog.repository.OrderRepository;
import za.co.entelect.devcamp.productcatalog.repository.OrderItemRepository;
import za.co.entelect.devcamp.productcatalog.requests.OrderRequest;
import za.co.entelect.devcamp.productcatalog.responses.OrderResponse;
import za.co.entelect.devcamp.productcatalog.service.IProductService;

@Slf4j
@Service
public class OrderService implements IOrderService
{
    public final OrderRepository orderRepository;
    public final OrderItemRepository orderItemRepository;
    public final IProductService productService;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        IProductService productService)
    {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productService = productService;
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
            orderItems.setProductId(request.getProduct().getProductId());
            orderItems.setOrderId(savedOrder.getOrderId());

            OrderItems savedOrderItems = orderItemRepository.save(orderItems);
            log.info("SaveOrder savedOrderItems: " + orderItems);

            OrderResponse response = new OrderResponse();

            response.setOrderId(order.getOrderId());
            response.setStatus(request.getStatus());
            response.setProduct(request.getProduct());

            log.info("SaveOrder response: " + response);
            return response;
        }
        catch(Exception e)
        {
            throw new Exception("Failed to save order to db: "+ e.getMessage());
        }
    }

    public OrderResponse GetOrder(Long orderId) throws Exception, NotFoundException
    {
        try
        {
            Optional<Orders> orderOp = orderRepository.findById(orderId);

            if(orderOp.isPresent()) {
                Orders order = orderOp.get();

                Optional<OrderItems> orderItemOp = orderItemRepository.findByOrderId(orderId);

                if(orderItemOp.isPresent()) {
                    OrderItems orderItem = orderItemOp.get();

                    OrderResponse orderResponse = new OrderResponse();

                    orderResponse.setOrderId(order.getOrderId());
                    orderResponse.setStatus(order.getStatus());

                    ProductDto product = productService.getProductById(orderItem.getProductId());
                    orderResponse.setProduct(product);

                    return orderResponse;
                }
                else
                {
                    throw new NotFoundException("Order not found");
                }
            }
            else
            {
                throw new NotFoundException("Order not found");
            }
        }
        catch(NotFoundException e)
        {
            throw new NotFoundException(e.getMessage());
        }
        catch(Exception e)
        {
            throw new Exception("Failed to get order: "+ e.getMessage());
        }
    }

    public List<OrderResponse> GetMyOrders(CustomerDto customer) throws Exception, NotFoundException
    {
        try
        {
            Optional<List<Orders>> ordersOp = orderRepository.findByCustomerId(customer.getId());

            if(ordersOp.isPresent())
            {
                List<Orders> orders = ordersOp.get();
                List<OrderResponse> orderResponses = new ArrayList<>();
                for(Orders order: orders)
                {
                    OrderResponse response = GetOrder(order.getOrderId());
                    orderResponses.add(response);
                }
                return orderResponses;
            }
            else
            {
                throw new NotFoundException("Orders not found");
            }
        }
        catch(NotFoundException e)
        {
            throw new NotFoundException(e.getMessage());
        }
        catch(Exception e)
        {
            throw new Exception("Failed to get order: "+ e.getMessage());
        }
    }

}