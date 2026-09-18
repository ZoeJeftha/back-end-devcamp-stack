package za.co.entelect.devcamp.productcatalog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import za.co.entelect.devcamp.productcatalog.dto.CustomerDto;
import za.co.entelect.devcamp.productcatalog.dto.ProductDto;
import za.co.entelect.devcamp.productcatalog.enums.OrderStatusEnum;
import za.co.entelect.devcamp.productcatalog.exception.NotFoundException;
import za.co.entelect.devcamp.productcatalog.model.OrderItems;
import za.co.entelect.devcamp.productcatalog.model.Orders;
import za.co.entelect.devcamp.productcatalog.repository.OrderItemRepository;
import za.co.entelect.devcamp.productcatalog.repository.OrderRepository;
import za.co.entelect.devcamp.productcatalog.requests.OrderRequest;
import za.co.entelect.devcamp.productcatalog.requests.OrderStatusUpdateRequest;
import za.co.entelect.devcamp.productcatalog.responses.CustomerChecksResponse;
import za.co.entelect.devcamp.productcatalog.responses.OrderResponse;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    public OrderService orderService;

    @Mock
    public OrderRepository orderRepository;

    @Mock
    public OrderItemRepository orderItemRepository;

    @Mock
    public ProductService productService;

    @Mock
    public CustomerChecksService customerChecksService;

    @Test
    void SaveOrder_ShouldReturnSavedOrder() throws Exception {
        OrderRequest request = new OrderRequest();
        request.setCustomerId(1L);
        request.setStatus("ACCEPTED");

        ProductDto product = new ProductDto();
        product.setProductId(1L);
        product.setName("Product 1");
        product.setDescription("Description 1");
        product.setPrice(new BigDecimal("99.99"));
        product.setImageUrl("abc.png");
        product.setFulfilmentType("A");

        request.setProduct(product);

        Orders savedOrder = new Orders();
        savedOrder.setOrderId(1L);
        savedOrder.setCustomerId(1L);
        savedOrder.setStatus("ACCEPTED");
        savedOrder.setContractUrl("url");

        when(orderRepository.save(any(Orders.class)))
                .thenReturn(savedOrder);

        OrderItems savedOrderItems = new OrderItems();
        savedOrderItems.setProductId(1L);
        savedOrderItems.setOrderId(1L);

        when(orderItemRepository.save(any(OrderItems.class)))
                .thenReturn(savedOrderItems);

        OrderResponse result = orderService.SaveOrder(request);

        assertEquals(1L, result.getOrderId());
        assertEquals("ACCEPTED", result.getStatus());
        assertEquals(product, result.getProduct());

        verify(orderRepository).save(any(Orders.class));
        verify(orderItemRepository).save(any(OrderItems.class));
    }

    @Test
    void GetOrder_ShouldReturnAnOrderResponse() throws Exception {
        Long orderId = 1L;

        Orders order = new Orders();
        order.setOrderId(orderId);
        order.setCustomerId(1L);
        order.setStatus("ACCEPTED");
        order.setContractUrl("url");

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        OrderItems orderItem = new OrderItems();
        orderItem.setOrderItemsId(1L);
        orderItem.setProductId(1L);
        orderItem.setOrderId(orderId);

        when(orderItemRepository.findByOrderId(orderId))
                .thenReturn(Optional.of(orderItem));

        ProductDto product = new ProductDto();
        product.setProductId(1L);
        product.setName("Product 1");
        product.setDescription("Description 1");
        product.setPrice(new BigDecimal("99.99"));
        product.setImageUrl("abc.png");
        product.setFulfilmentType("A");

        when(productService.getProductById(orderItem.getProductId()))
                .thenReturn(product);

        CustomerChecksResponse check1 = new CustomerChecksResponse();
        check1.setCustomerCheck("Check1");
        check1.setHasPassed(true);

        CustomerChecksResponse check2 = new CustomerChecksResponse();
        check2.setCustomerCheck("Check2");
        check2.setHasPassed(false);

        List<CustomerChecksResponse> customerChecksResponseList =
                List.of(check1, check2);

        when(customerChecksService.getCustomerChecks(orderId))
                .thenReturn(customerChecksResponseList);

        OrderResponse result = orderService.GetOrder(orderId);

        assertEquals(1L, result.getOrderId());
        assertEquals("ACCEPTED", result.getStatus());

        assertEquals(1L, result.getProduct().getProductId());
        assertEquals("Product 1", result.getProduct().getName());
        assertEquals("Description 1", result.getProduct().getDescription());
        assertEquals(new BigDecimal("99.99"), result.getProduct().getPrice());
        assertEquals("abc.png", result.getProduct().getImageUrl());
        assertEquals("A", result.getProduct().getFulfilmentType());

        assertEquals(
                "Check1",
                result.getCustomerChecks().get(0).getCustomerCheck()
        );
        assertTrue(result.getCustomerChecks().get(0).getHasPassed());

        assertEquals(
                "Check2",
                result.getCustomerChecks().get(1).getCustomerCheck()
        );
        assertFalse(result.getCustomerChecks().get(1).getHasPassed());

        verify(orderRepository).findById(orderId);
        verify(orderItemRepository).findByOrderId(orderId);
        verify(productService).getProductById(orderItem.getProductId());
        verify(customerChecksService).getCustomerChecks(orderId);
    }

    @Test
    void GetOrder_ShouldThrowNotFoundExceptionWhenOrderDoesNotExist() {
        Long orderId = 1L;

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> orderService.GetOrder(orderId)
        );

        assertEquals("Order not found", exception.getMessage());

        verify(orderRepository).findById(orderId);
        verify(orderItemRepository, never()).findByOrderId(orderId);
    }

    @Test
    void GetOrder_ShouldThrowNotFoundExceptionWhenOrderItemDoesNotExist() {
        Long orderId = 1L;

        Orders order = new Orders();
        order.setOrderId(orderId);
        order.setCustomerId(1L);
        order.setStatus("ACCEPTED");

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        when(orderItemRepository.findByOrderId(orderId))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> orderService.GetOrder(orderId)
        );

        assertEquals("Order not found", exception.getMessage());

        verify(orderRepository).findById(orderId);
        verify(orderItemRepository).findByOrderId(orderId);
        verify(productService, never()).getProductById(any());
    }

    @Test
    void GetOrder_ShouldThrowExceptionWhenOrderRepositoryFails() {
        Long orderId = 1L;

        when(orderRepository.findById(orderId))
                .thenThrow(new RuntimeException("Failed"));

        Exception exception = assertThrows(
                Exception.class,
                () -> orderService.GetOrder(orderId)
        );

        assertEquals("Failed to get order: Failed", exception.getMessage());

        verify(orderRepository).findById(orderId);
    }

    @Test
    void GetOrder_ShouldThrowExceptionWhenOrderItemRepositoryFails() {
        Long orderId = 1L;

        Orders order = new Orders();
        order.setOrderId(orderId);
        order.setCustomerId(1L);
        order.setStatus("ACCEPTED");

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        when(orderItemRepository.findByOrderId(orderId))
                .thenThrow(new RuntimeException("Failed"));

        Exception exception = assertThrows(
                Exception.class,
                () -> orderService.GetOrder(orderId)
        );

        assertEquals("Failed to get order: Failed", exception.getMessage());

        verify(orderRepository).findById(orderId);
        verify(orderItemRepository).findByOrderId(orderId);
    }

    @Test
    void GetMyOrders_ShouldReturnOrders() throws Exception {
        CustomerDto customer = new CustomerDto();
        customer.setId(1L);

        Orders order1 = new Orders();
        order1.setOrderId(1L);
        order1.setCustomerId(1L);
        order1.setStatus("ACCEPTED");

        Orders order2 = new Orders();
        order2.setOrderId(2L);
        order2.setCustomerId(1L);
        order2.setStatus("PENDING");

        List<Orders> orders = List.of(order1, order2);

        when(orderRepository.findByCustomerId(customer.getId()))
                .thenReturn(Optional.of(orders));

        // GetOrder(1L)
        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order1));

        OrderItems orderItem1 = new OrderItems();
        orderItem1.setOrderItemsId(1L);
        orderItem1.setOrderId(1L);
        orderItem1.setProductId(1L);

        when(orderItemRepository.findByOrderId(1L))
                .thenReturn(Optional.of(orderItem1));

        ProductDto product1 = new ProductDto();
        product1.setProductId(1L);
        product1.setName("Product 1");
        product1.setDescription("Description 1");
        product1.setPrice(new BigDecimal("99.99"));
        product1.setImageUrl("abc.png");
        product1.setFulfilmentType("A");

        when(productService.getProductById(1L))
                .thenReturn(product1);

        CustomerChecksResponse check1 = new CustomerChecksResponse();
        check1.setCustomerCheck("KYC Check");
        check1.setHasPassed(true);

        when(customerChecksService.getCustomerChecks(1L))
                .thenReturn(List.of(check1));

        // GetOrder(2L)
        when(orderRepository.findById(2L))
                .thenReturn(Optional.of(order2));

        OrderItems orderItem2 = new OrderItems();
        orderItem2.setOrderItemsId(2L);
        orderItem2.setOrderId(2L);
        orderItem2.setProductId(2L);

        when(orderItemRepository.findByOrderId(2L))
                .thenReturn(Optional.of(orderItem2));

        ProductDto product2 = new ProductDto();
        product2.setProductId(2L);
        product2.setName("Product 2");
        product2.setDescription("Description 2");
        product2.setPrice(new BigDecimal("199.99"));
        product2.setImageUrl("def.png");
        product2.setFulfilmentType("B");

        when(productService.getProductById(2L))
                .thenReturn(product2);

        CustomerChecksResponse check2 = new CustomerChecksResponse();
        check2.setCustomerCheck("Fraud Check");
        check2.setHasPassed(false);

        when(customerChecksService.getCustomerChecks(2L))
                .thenReturn(List.of(check2));

        List<OrderResponse> result = orderService.GetMyOrders(customer);

        assertEquals(2, result.size());

        assertEquals(1L, result.get(0).getOrderId());
        assertEquals("ACCEPTED", result.get(0).getStatus());
        assertEquals(1L, result.get(0).getProduct().getProductId());
        assertEquals("Product 1", result.get(0).getProduct().getName());
        assertEquals(1, result.get(0).getCustomerChecks().size());
        assertEquals(
                "KYC Check",
                result.get(0).getCustomerChecks().get(0).getCustomerCheck()
        );
        assertTrue(result.get(0).getCustomerChecks().get(0).getHasPassed());

        assertEquals(2L, result.get(1).getOrderId());
        assertEquals("PENDING", result.get(1).getStatus());
        assertEquals(2L, result.get(1).getProduct().getProductId());
        assertEquals("Product 2", result.get(1).getProduct().getName());
        assertEquals(1, result.get(1).getCustomerChecks().size());
        assertEquals(
                "Fraud Check",
                result.get(1).getCustomerChecks().get(0).getCustomerCheck()
        );
        assertFalse(result.get(1).getCustomerChecks().get(0).getHasPassed());

        verify(orderRepository).findByCustomerId(customer.getId());

        verify(orderRepository).findById(1L);
        verify(orderRepository).findById(2L);

        verify(orderItemRepository).findByOrderId(1L);
        verify(orderItemRepository).findByOrderId(2L);

        verify(productService).getProductById(1L);
        verify(productService).getProductById(2L);

        verify(customerChecksService).getCustomerChecks(1L);
        verify(customerChecksService).getCustomerChecks(2L);
    }

    @Test
    void UpdateOrderStatus_ShouldReturnUpdatedOrder() throws Exception {
        Long orderId = 1L;

        OrderStatusUpdateRequest request = new OrderStatusUpdateRequest();
        request.setOrderId(orderId);
        request.setStatus(OrderStatusEnum.ACCEPTED);
        request.setSaveCustomerChecks(new ArrayList<>());

        Orders order = new Orders();
        order.setOrderId(orderId);
        order.setCustomerId(1L);
        order.setStatus("PENDING");

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        Orders updatedOrder = new Orders();
        updatedOrder.setOrderId(orderId);
        updatedOrder.setCustomerId(1L);
        updatedOrder.setStatus("ACCEPTED");

        when(orderRepository.save(any(Orders.class)))
                .thenReturn(updatedOrder);

        OrderItems orderItem = new OrderItems();
        orderItem.setOrderItemsId(1L);
        orderItem.setOrderId(orderId);
        orderItem.setProductId(1L);

        when(orderItemRepository.findByOrderId(orderId))
                .thenReturn(Optional.of(orderItem));

        ProductDto product = new ProductDto();
        product.setProductId(1L);
        product.setName("Product 1");
        product.setDescription("Description 1");
        product.setPrice(new BigDecimal("99.99"));
        product.setImageUrl("abc.png");
        product.setFulfilmentType("A");

        when(productService.getProductById(1L))
                .thenReturn(product);

        OrderResponse result = orderService.UpdateOrderStatus(request);

        assertEquals(1L, result.getOrderId());
        assertEquals("ACCEPTED", result.getStatus());

        assertEquals(1L, result.getProduct().getProductId());
        assertEquals("Product 1", result.getProduct().getName());
        assertEquals("Description 1", result.getProduct().getDescription());
        assertEquals(new BigDecimal("99.99"), result.getProduct().getPrice());
        assertEquals("abc.png", result.getProduct().getImageUrl());
        assertEquals("A", result.getProduct().getFulfilmentType());

        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(any(Orders.class));
        verify(orderItemRepository).findByOrderId(orderId);
        verify(productService).getProductById(1L);
        verify(customerChecksService)
                .SaveCustomerChecks(request.getSaveCustomerChecks());
    }

    @Test
    void UpdateOrderStatus_ShouldThrowNotFoundExceptionWhenOrderDoesNotExist()
            throws Exception {

        Long orderId = 1L;

        OrderStatusUpdateRequest request = new OrderStatusUpdateRequest();
        request.setOrderId(orderId);
        request.setStatus(OrderStatusEnum.ACCEPTED);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> orderService.UpdateOrderStatus(request)
        );

        assertEquals("Orders not found", exception.getMessage());

        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any(Orders.class));
    }

    @Test
    void UpdateOrderStatus_ShouldThrowNotFoundExceptionWhenOrderItemDoesNotExist()
            throws Exception {

        Long orderId = 1L;

        OrderStatusUpdateRequest request = new OrderStatusUpdateRequest();
        request.setOrderId(orderId);
        request.setStatus(OrderStatusEnum.ACCEPTED);

        Orders order = new Orders();
        order.setOrderId(orderId);
        order.setCustomerId(1L);
        order.setStatus("PENDING");

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        when(orderRepository.save(any(Orders.class)))
                .thenReturn(order);

        when(orderItemRepository.findByOrderId(orderId))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> orderService.UpdateOrderStatus(request)
        );

        assertEquals("Order not found", exception.getMessage());

        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(any(Orders.class));
        verify(orderItemRepository).findByOrderId(orderId);
        verify(productService, never()).getProductById(any());
    }

    @Test
    void UpdateOrderStatus_ShouldUpdateOrderStatus() throws Exception {
        Long orderId = 1L;

        OrderStatusUpdateRequest request = new OrderStatusUpdateRequest();
        request.setOrderId(orderId);
        request.setStatus(OrderStatusEnum.ACCEPTED);
        request.setSaveCustomerChecks(new ArrayList<>());

        Orders order = new Orders();
        order.setOrderId(orderId);
        order.setStatus("PENDING");

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        when(orderRepository.save(any(Orders.class)))
                .thenReturn(order);

        OrderItems orderItem = new OrderItems();
        orderItem.setOrderId(orderId);
        orderItem.setProductId(1L);

        when(orderItemRepository.findByOrderId(orderId))
                .thenReturn(Optional.of(orderItem));

        when(productService.getProductById(1L))
                .thenReturn(new ProductDto());

        orderService.UpdateOrderStatus(request);

        ArgumentCaptor<Orders> orderCaptor =
                ArgumentCaptor.forClass(Orders.class);

        verify(orderRepository).save(orderCaptor.capture());

        Orders savedOrder = orderCaptor.getValue();

        assertEquals(orderId, savedOrder.getOrderId());
        assertEquals("ACCEPTED", savedOrder.getStatus());
    }
}