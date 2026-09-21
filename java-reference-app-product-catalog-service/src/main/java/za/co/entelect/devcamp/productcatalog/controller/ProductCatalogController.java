package za.co.entelect.devcamp.productcatalog.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import za.co.entelect.devcamp.productcatalog.dto.ProductDto;
import za.co.entelect.devcamp.productcatalog.responses.ApiResponse;
import za.co.entelect.devcamp.productcatalog.service.IProductService;

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
    public ResponseEntity<ApiResponse<List<ProductDto>>> getProducts(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

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
    public ResponseEntity<ApiResponse<ProductDto>> getProductById(@PathVariable Long id) {
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

}
