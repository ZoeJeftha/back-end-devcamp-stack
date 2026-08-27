
package za.co.entelect.devcamp.productcatalog.service;

import za.co.entelect.devcamp.productcatalog.dto.ProductDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductService {

    List<ProductDto> getProducts();

    Page<ProductDto> getProducts(Pageable pageable);

    ProductDto getProductById(Long id);
}

