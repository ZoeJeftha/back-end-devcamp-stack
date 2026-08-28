package za.co.entelect.devcamp.productcatalog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.Instant;
import za.co.entelect.devcamp.productcatalog.service.IProductService;
import za.co.entelect.devcamp.productcatalog.dto.ProductDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import za.co.entelect.devcamp.productcatalog.requests.CustomerEligibilityRequest;
import za.co.entelect.devcamp.productcatalog.service.IProductEligibilityService;
import za.co.entelect.devcamp.productcatalog.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RestController
@RequestMapping("/v1")
public class ProductCatalogController {

    public final IProductService productService;
    public final IProductEligibilityService productEligibilityService;

    public ProductCatalogController(IProductService productService, IProductEligibilityService productEligibilityService)
    {
        this.productService = productService;
        this.productEligibilityService = productEligibilityService;
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

    @PostMapping("/customer-eligibility-check")
    public ResponseEntity<ApiResponse<Boolean>> CustomerTypeEligibilityCheck(@RequestBody CustomerEligibilityRequest customerEligibilityRequest)
    {
        log.info("Customer product eligibility request received");
        try {
            Boolean isEligible = productEligibilityService.isCustomerEligible(customerEligibilityRequest);
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

}
