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
import za.co.entelect.devcamp.productcatalog.requests.CustomerProductEligibilityRequest;
import za.co.entelect.devcamp.productcatalog.requests.CustomerAccountEligibilityRequest;
import za.co.entelect.devcamp.productcatalog.service.IProductEligibilityService;

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
    public ResponseEntity<List<ProductDto>> getProducts()
    {
        log.info("Getting Products");
        return productService.getProducts();
    }


    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id)
    {
        log.info("Getting Product by Id");
        return productService.getProductById(id);
    }

    @PostMapping("/customer-product-eligibility-check")
    public boolean CustomerTypeEligibilityCheck(@RequestBody CustomerProductEligibilityRequest customerProductEligibilityRequest)
    {
        log.info("Customer product eligibility request received");
        return productEligibilityService.isCustomerEligible(customerProductEligibilityRequest);
    }

    @PostMapping("/customer-account-eligibility-check")
    public boolean CustomerAccountEligibilityCheck(@RequestBody CustomerAccountEligibilityRequest customerEligibilityRequest)
    {
        log.info("Customer account eligibility request received");
        return productEligibilityService.isCustomerAccountEligible(customerEligibilityRequest);
    }
}
