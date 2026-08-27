
package za.co.entelect.devcamp.productcatalog.service;

import za.co.entelect.devcamp.productcatalog.dto.ProductDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IProductService {

    List<ProductDto> getProducts();

    ProductDto getProductById(Long id);
}

