package za.co.entelect.devcamp.productcatalog.controller;

import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import za.co.entelect.devcamp.productcatalog.dto.CustomerDto;
import za.co.entelect.devcamp.productcatalog.dto.ProductDto;
import za.co.entelect.devcamp.productcatalog.enums.OrderStatusEnum;
import za.co.entelect.devcamp.productcatalog.exception.NotFoundException;
import za.co.entelect.devcamp.productcatalog.producer.MessageProducer;
import za.co.entelect.devcamp.productcatalog.requests.FulfilmentRequest;
import za.co.entelect.devcamp.productcatalog.requests.OrderRequest;
import za.co.entelect.devcamp.productcatalog.requests.OrderStatusUpdateRequest;
import za.co.entelect.devcamp.productcatalog.responses.ApiResponse;
import za.co.entelect.devcamp.productcatalog.responses.OrderResponse;
import za.co.entelect.devcamp.productcatalog.service.*;

@Slf4j
@RestController
@RequestMapping("/v1/order")
public class OrderController {
    public final IProductService productService;
    public final IProductEligibilityService productEligibilityService;
    public final ICustomerService customerService;
    public final IOrderService orderService;

    @Autowired
    private MessageProducer messageProducer;

    public OrderController(IProductService productService,
                           IProductEligibilityService productEligibilityService,
                           ICustomerService customerService,
                           IOrderService orderService)
    {
        this.productService = productService;
        this.productEligibilityService = productEligibilityService;
        this.customerService = customerService;
        this.orderService = orderService;
    }


    @PostMapping("/{productId}")
    public ResponseEntity<ApiResponse<OrderResponse>> PlaceOrder(@AuthenticationPrincipal Jwt jwt, @PathVariable Long productId)
    {
        try
        {
            String token = jwt.getTokenValue();
            String username = jwt.getSubject();

            boolean isEligible = productEligibilityService.isCustomerEligible(token,username, productId);
            if(!isEligible)
            {
                ApiResponse<OrderResponse> response = new ApiResponse<OrderResponse>(true, "Customer ineligible for selected product", null);
                return ResponseEntity.internalServerError().body(response);
            }

            CustomerDto customerDto = customerService.GetMyUnmaskedProfile(token, username);

            ProductDto product = productService.getProductById(productId);

            OrderRequest orderRequest = new OrderRequest();
            orderRequest.setCustomerId(customerDto.getId());
            orderRequest.setStatus("PENDING");
            orderRequest.setProduct(product);

            OrderResponse orderResponse = orderService.SaveOrder(orderRequest);

            FulfilmentRequest fulfilmentRequest = new FulfilmentRequest();
            fulfilmentRequest.setId(customerDto.getId());
            fulfilmentRequest.setIdNumber(customerDto.getIdNumber());
            fulfilmentRequest.setUsername(customerDto.getUsername());
            fulfilmentRequest.setOrderId(orderResponse.getOrderId());
            fulfilmentRequest.setFulfilmentType(product.getFulfilmentType());

            messageProducer.SendMessage(fulfilmentRequest);

            log.info("---------------Order placed--------------");
            ApiResponse<OrderResponse> response = new ApiResponse<OrderResponse>(true, "Order placed",orderResponse);
            return ResponseEntity.ok(response);
        }
        catch(NotFoundException e)
        {
            ApiResponse<OrderResponse> response = new ApiResponse<OrderResponse>(false, "Customer not found",null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch(Exception e)
        {
            log.info("Failed to place order: " + e.getMessage());
            ApiResponse<OrderResponse> response = new ApiResponse<OrderResponse>(false, "Failed to place order", null);
            return ResponseEntity.internalServerError().body(response);
        }

    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> GetOrderById(@PathVariable Long orderId)
    {
        try
        {
            OrderResponse orderResponse = orderService.GetOrder(orderId);
            ApiResponse<OrderResponse> response = new ApiResponse<OrderResponse>(true, "Order retrieved successfully", orderResponse);
            return ResponseEntity.ok(response);
        }
        catch(NotFoundException e)
        {
            ApiResponse<OrderResponse> response = new ApiResponse<OrderResponse>(false, "Order not found",null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch(Exception e) {
            ApiResponse<OrderResponse> response = new ApiResponse<OrderResponse>(false, "Failed to retrieve order: " + e.getMessage(), null);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<OrderResponse>>> GetMyOrders(@AuthenticationPrincipal Jwt jwt)
    {
        try
        {
            String token = jwt.getTokenValue();
            String username = jwt.getSubject();
            CustomerDto customer = customerService.GetMyProfile(token,username);

            List<OrderResponse> orderResponse = orderService.GetMyOrders(customer);
            ApiResponse<List<OrderResponse>> response = new ApiResponse<List<OrderResponse>>(true, "Orders retrieved successfully", orderResponse);
            return ResponseEntity.ok(response);
        }
        catch(NotFoundException e)
        {
            ApiResponse<List<OrderResponse>> response = new ApiResponse<List<OrderResponse>>(false, "Orders not found",null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch(Exception e) {
            ApiResponse<List<OrderResponse>> response = new ApiResponse<List<OrderResponse>>(false, "Failed to retrieve orders: " + e.getMessage(), null);
            return ResponseEntity.internalServerError().body(response);
        }
    }


    @PutMapping("/status")
    public ResponseEntity<ApiResponse<OrderResponse>> UpdateOrderStatus(@AuthenticationPrincipal Jwt jwt,@RequestBody OrderStatusUpdateRequest request)
    {
        try
        {
            String token = jwt.getTokenValue();

            boolean validStatus = Arrays.stream(OrderStatusEnum.values())
                    .anyMatch(s -> s.name().equalsIgnoreCase(request.getStatus().toString()));

            if(!validStatus)
            {
                ApiResponse<OrderResponse> response = new ApiResponse<OrderResponse>(true, "Invalid status", null);
                return ResponseEntity.ok(response);
            }

            OrderResponse orderResponse = orderService.UpdateOrderStatus(request);
            ApiResponse<OrderResponse> response = new ApiResponse<OrderResponse>(true, "Order updated successfully", orderResponse);
            return ResponseEntity.ok(response);
        }
        catch(NotFoundException e)
        {
            ApiResponse<OrderResponse> response = new ApiResponse<OrderResponse>(false, "Order not found",null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch(Exception e) {
            ApiResponse<OrderResponse> response = new ApiResponse<OrderResponse>(false, e.getMessage(), null);
            return ResponseEntity.internalServerError().body(response);
        }
    }

}
