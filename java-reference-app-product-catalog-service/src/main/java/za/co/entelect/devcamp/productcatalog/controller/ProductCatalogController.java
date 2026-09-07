package za.co.entelect.devcamp.productcatalog.controller;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import za.co.entelect.devcamp.productcatalog.dto.CustomerDto;
import za.co.entelect.devcamp.productcatalog.dto.ProductDto;
import za.co.entelect.devcamp.productcatalog.enums.OrderStatusEnum;
import za.co.entelect.devcamp.productcatalog.exception.NotFoundException;
import za.co.entelect.devcamp.productcatalog.producer.MessageProducer;
import za.co.entelect.devcamp.productcatalog.responses.ApiResponse;
import za.co.entelect.devcamp.productcatalog.service.ICustomerService;
import za.co.entelect.devcamp.productcatalog.service.IProductEligibilityService;
import za.co.entelect.devcamp.productcatalog.service.IProductService;
import za.co.entelect.devcamp.productcatalog.service.IOrderService;
import za.co.entelect.devcamp.productcatalog.requests.FulfilmentRequest;
import za.co.entelect.devcamp.productcatalog.requests.OrderRequest;
import za.co.entelect.devcamp.productcatalog.requests.OrderStatusUpdateRequest;
import za.co.entelect.devcamp.productcatalog.responses.OrderResponse;

@Slf4j
@RestController
@RequestMapping("/v1")
public class ProductCatalogController {

    public final IProductService productService;
    public final IProductEligibilityService productEligibilityService;
    public final ICustomerService customerService;
    public final IOrderService orderService;

    @Autowired
    private MessageProducer messageProducer;

    public ProductCatalogController(IProductService productService,
                                    IProductEligibilityService productEligibilityService,
                                    ICustomerService customerService,
                                    IOrderService orderService)
    {
        this.productService = productService;
        this.productEligibilityService = productEligibilityService;
        this.customerService = customerService;
        this.orderService = orderService;
    }

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getProducts(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    )
    {
        log.info("Getting Products");
        try {
            if (page == null || size == null) {
                List<ProductDto> products = productService.getProducts();
                ApiResponse<List<ProductDto>> response = new ApiResponse<List<ProductDto>>(true, "Products retrieved successfully", products);
                return ResponseEntity.ok(response);
            }
            else
            {
                Pageable pageable = PageRequest.of(page, size);

                Page<ProductDto> pagedProducts = productService.getProducts(pageable);
                List<ProductDto> products = pagedProducts.getContent();
                ApiResponse<List<ProductDto>> response = new ApiResponse<List<ProductDto>>(true, "Products retrieved successfully", products);
                return ResponseEntity.ok(response);
            }
        }
        catch(Exception e)
        {
            log.info("Failed to retrieve products: " + e.getMessage());
            ApiResponse<List<ProductDto>> response = new ApiResponse<List<ProductDto>>(false, "Failed to retrieve products: "+ e.getMessage(), null);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> getProductById(@PathVariable Long id)
    {
        log.info("Getting Product by Id");
        try {
            ProductDto product = productService.getProductById(id);
            ApiResponse<ProductDto> response = new ApiResponse<ProductDto>(true, "Product retrieved successfully",product);
            return ResponseEntity.ok(response);
        }
        catch(Exception e)
        {
            log.info("Failed to retrieve product" + e.getMessage());
            ApiResponse<ProductDto> response = new ApiResponse<ProductDto>(false, "Failed to retrieve product: "+ e.getMessage(), null);
            return ResponseEntity.internalServerError().body(response);
        }

    }

    @GetMapping("/my-profile")
    public ResponseEntity<ApiResponse<CustomerDto>> getMyProfile(@AuthenticationPrincipal Jwt jwt)
    {
        log.info("Getting my profile");
        try {
            String username = jwt.getSubject();
            String token = jwt.getTokenValue();

            CustomerDto customerDto = customerService.GetMyProfile(token,username);
            ApiResponse<CustomerDto> response = new ApiResponse<CustomerDto>(true, "Profile retrieved successfully",customerDto);
            return ResponseEntity.ok(response);
        }
        catch(NotFoundException e)
        {
            ApiResponse<CustomerDto> response = new ApiResponse<CustomerDto>(false, "Profile not found",null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch(Exception e)
        {
            log.info("Failed to retrieve profile" + e.getMessage());
            ApiResponse<CustomerDto> response = new ApiResponse<CustomerDto>(false, "Failed to retrieve profile: "+ e.getMessage(), null);
            return ResponseEntity.internalServerError().body(response);
        }

    }


    @GetMapping("/profiles")
    public ResponseEntity<ApiResponse<List<CustomerDto>>> getProfiles(@AuthenticationPrincipal Jwt jwt)
    {
        log.info("Getting profiles");
        try {
            String token = jwt.getTokenValue();
            String role = jwt.getClaimAsString("role");

            if (!"admin".equals(role)) {
                ApiResponse<List<CustomerDto>> response = new ApiResponse<>(false, "Not authorised to retrieve profiles", null);

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(response);
            }

            List<CustomerDto> customerDto = customerService.GetProfiles(token);
            ApiResponse<List<CustomerDto>> response = new ApiResponse<List<CustomerDto>>(true, "Profile retrieved successfully",customerDto);
            return ResponseEntity.ok(response);
        }
        catch(NotFoundException e)
        {
            ApiResponse<List<CustomerDto>> response = new ApiResponse<List<CustomerDto>>(false, "Profile not found",null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch(Exception e)
        {
            log.info("Failed to retrieve profile" + e.getMessage());
            ApiResponse<List<CustomerDto>> response = new ApiResponse<List<CustomerDto>>(false, "Failed to retrieve profile: "+ e.getMessage(), null);
            return ResponseEntity.internalServerError().body(response);
        }

    }


    @GetMapping("/customer-eligibility-check/{productId}")
    public ResponseEntity<ApiResponse<Boolean>> CustomerTypeEligibilityCheck(@AuthenticationPrincipal Jwt jwt, @PathVariable Long productId)
    {
        log.info("Customer product eligibility request received");
        try {
            String token = jwt.getTokenValue();
            String username = jwt.getSubject();
            Boolean isEligible = productEligibilityService.isCustomerEligible(token, username, productId);
            ApiResponse<Boolean> response = new ApiResponse<Boolean>(true, "Customer Eligibility Result Retrieved",isEligible);
            return ResponseEntity.ok(response);
        }
        catch(NotFoundException e)
        {
            ApiResponse<Boolean> response = new ApiResponse<Boolean>(false, "Customer not found",null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch(Exception e)
        {
            log.info("Failed to check customer eligibility" + e.getMessage());
            ApiResponse<Boolean> response = new ApiResponse<Boolean>(false, "Failed to retrieve customer eligibility: "+ e.getMessage(), null);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/order/{productId}")
    public ResponseEntity<ApiResponse<OrderResponse>> PlaceOrder(@AuthenticationPrincipal Jwt jwt, @PathVariable Long productId)
    {
        try
        {
            String token = jwt.getTokenValue();
            String username = jwt.getSubject();

            Boolean isEligible = productEligibilityService.isCustomerEligible(token,username, productId);
            log.info("---------------Place order-------------- isEligible:" + isEligible);
            if(!isEligible)
            {
                ApiResponse<OrderResponse> response = new ApiResponse<OrderResponse>(true, "Customer ineligible for selected product", null);
                return ResponseEntity.internalServerError().body(response);
            }

            CustomerDto customerDto = customerService.GetMyProfile(token, username);

            log.info("---------------Place order-------------- customerDto:" + customerDto);

            FulfilmentRequest fulfilmentRequest = new FulfilmentRequest();
            fulfilmentRequest.setId(customerDto.getId());
            fulfilmentRequest.setIdNumber(customerDto.getIdNumber());
            fulfilmentRequest.setUsername(customerDto.getUsername());

            log.info("---------------Place order-------------- fulfilmentRequest without type:" + fulfilmentRequest);

            ProductDto product = productService.getProductById(productId);
            fulfilmentRequest.setFulfilmentType(product.getFulfilmentType());

            log.info("---------------Place order-------------- fulfilmentRequest with type:" + fulfilmentRequest);

            messageProducer.SendMessage(fulfilmentRequest);

            log.info("---------------Place order-------------- :fulfilmentRequest message sent" + fulfilmentRequest);

            OrderRequest orderRequest = new OrderRequest();
            orderRequest.setCustomerId(customerDto.getId());
            orderRequest.setStatus("PENDING");
            orderRequest.setProduct(product);

            OrderResponse orderResponse = orderService.SaveOrder(orderRequest);
            ApiResponse<OrderResponse> response = new ApiResponse<OrderResponse>(true, "Order placed",orderResponse);

            log.info("---------------Place order-------------- order placed");
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

    @GetMapping("/order-by-id/{orderId}")
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

    @GetMapping("/all-orders")
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


    @GetMapping("/order-status-update")
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
