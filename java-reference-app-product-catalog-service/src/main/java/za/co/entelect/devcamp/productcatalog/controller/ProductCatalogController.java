package za.co.entelect.devcamp.productcatalog.controller;

import java.time.Instant;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
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
import za.co.entelect.devcamp.productcatalog.client.CustomerApiClient;
import za.co.entelect.devcamp.productcatalog.dto.CustomerDto;
import za.co.entelect.devcamp.productcatalog.dto.ProductDto;
import za.co.entelect.devcamp.productcatalog.producer.MessageProducer;
import za.co.entelect.devcamp.productcatalog.responses.ApiResponse;
import za.co.entelect.devcamp.productcatalog.service.ICustomerService;
import za.co.entelect.devcamp.productcatalog.service.IProductEligibilityService;
import za.co.entelect.devcamp.productcatalog.service.IProductService;
import za.co.entelect.devcamp.productcatalog.request.FulfilmentRequest;

@Slf4j
@RestController
@RequestMapping("/v1")
public class ProductCatalogController {

    public final IProductService productService;
    public final IProductEligibilityService productEligibilityService;
    public final ICustomerService customerService;

    @Autowired
    private MessageProducer messageProducer;

    public ProductCatalogController(IProductService productService,
                                    IProductEligibilityService productEligibilityService,
                                    ICustomerService customerService)
    {
        this.productService = productService;
        this.productEligibilityService = productEligibilityService;
        this.customerService = customerService;
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

    @GetMapping("/customer-eligibility-check/{productId}")
    public ResponseEntity<ApiResponse<Boolean>> CustomerTypeEligibilityCheck(@AuthenticationPrincipal Jwt jwt, @PathVariable Long productId)
    {
        log.info("Customer product eligibility request received");
        try {
            String token = jwt.getTokenValue();
            Boolean isEligible = productEligibilityService.isCustomerEligible(token, productId);
            ApiResponse<Boolean> response = new ApiResponse<Boolean>(true, "Customer Eligibility Result Retrieved",isEligible);
            return ResponseEntity.ok(response);
        }
        catch(Exception e)
        {
            log.info("Failed to check customer eligibility" + e.getMessage());
            ApiResponse<Boolean> response = new ApiResponse<Boolean>(false, "Failed to retrieve customer eligibility: "+ e.getMessage(), null);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/place-order/{productId}")
    public ResponseEntity<ApiResponse<FulfilmentRequest>> PlaceOrder(@AuthenticationPrincipal Jwt jwt, @PathVariable Long productId)
    {
        try
        {
            String token = jwt.getTokenValue();
            Boolean isEligible = productEligibilityService.isCustomerEligible(token, productId);
            log.info("---------------Place order-------------- isEligible:" + isEligible);
            if(!isEligible)
            {
                ApiResponse<FulfilmentRequest> response = new ApiResponse<FulfilmentRequest>(true, "Customer ineligible for selected product", null);
                return ResponseEntity.internalServerError().body(response);
            }

            ResponseEntity<ApiResponse<CustomerDto>> customer = customerService.GetMyProfile(token);
            CustomerDto customerDto = customer.getBody().getResult();
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
            ApiResponse<FulfilmentRequest> response = new ApiResponse<FulfilmentRequest>(true, "Order placed: ",fulfilmentRequest);

            log.info("---------------Place order-------------- order placed");
            return ResponseEntity.ok(response);
        }
        catch(Exception e)
        {
            log.info("Failed to place order: " + e.getMessage());
            ApiResponse<FulfilmentRequest> response = new ApiResponse<FulfilmentRequest>(false, "Failed to place order", null);
            return ResponseEntity.internalServerError().body(response);
        }

    }

    @GetMapping("/my-profile")
    public ResponseEntity<ApiResponse<CustomerDto>> GetMyProfile(@AuthenticationPrincipal Jwt jwt)
    {
        try {
            String token = jwt.getTokenValue();
            ResponseEntity<ApiResponse<CustomerDto>> customer = customerService.GetMyProfile(token);

            return customer;
        }
        catch(Exception e) {
            ApiResponse<CustomerDto> response = new ApiResponse<CustomerDto>(false, "Failed to retrieve my profile: " + e.getMessage(), null);
            return ResponseEntity.internalServerError().body(response);
        }
    }

//    @GetMapping("/place-order/{productId}")
//    public ResponseEntity<ApiResponse<Boolean>> PlaceOrder(@AuthenticationPrincipal Jwt jwt,  @PathVariable Long productId)
//    {
//        try {
//            String token = jwt.getTokenValue();
//            Boolean isEligible = productEligibilityService.isCustomerEligible(token, productId);
//            if(!isEligible)
//            {
//                ApiResponse<Boolean> response = new ApiResponse<Boolean>(success, "Customer ineligible for selected product", null);
//                return response;
//            }
//
//            ApiResponse<Boolean> response = new ApiResponse<Boolean>(success, "Customer ineligible for selected product", null);
//            return response;
//        }
//        catch(Exception e)
//        {
//            log.info("Failed to check customer eligibility" + e.getMessage());
//            ApiResponse<Boolean> response = new ApiResponse<Boolean>(false, "Failed to place order: "+ e.getMessage(), null);
//            return ResponseEntity.internalServerError().body(response);
//        }
//
//    }

}
