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

@Slf4j
@RestController
@RequestMapping("/v1")
public class ProductCatalogController {

    public final IProductService productService;

    public ProductCatalogController(IProductService productService)
    {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getProducts()
    {
        log.info("Getting Products");
        return productService.getProducts();
    }

//    @PostMapping("/create-products")
//    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto)
//    {
//        return productService.createProduct(productDto);
//    }
}
