
package za.co.entelect.devcamp.productcatalog.service;

import za.co.entelect.devcamp.productcatalog.dto.ProductDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IProductService {

    ResponseEntity<List<ProductDto>> getProducts();

   // ResponseEntity<ProductDto> createProduct(ProductDto productDto);

}

