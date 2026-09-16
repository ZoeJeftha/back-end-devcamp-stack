package za.co.entelect.devcamp.productcatalog.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import za.co.entelect.devcamp.productcatalog.dto.ProductDto;
import za.co.entelect.devcamp.productcatalog.model.Products;
import za.co.entelect.devcamp.productcatalog.repository.ProductRepository;
import za.co.entelect.devcamp.productcatalog.service.ProductService;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void getProducts_shouldReturnProducts() {

        Products product = new Products();
        product.setProductId(1L);
        product.setName("Product 1");

        when(productRepository.findAll())
                .thenReturn(List.of(product));

        var result = productService.getProducts();

        assertEquals(1, result.size());
        assertEquals("Product 1", result.get(0).getName());
        assertEquals(1L, result.get(0).getProductId());
    }

    @Test
    void getProductById_shouldReturnProduct()
    {
        Products product = new Products();
        product.setProductId(1L);
        product.setName("Product 1");

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        var result = productService.getProductById(1L);

        assertEquals(1L, result.getProductId());
        assertEquals("Product 1", result.getName());
    }

    @Test
    void getProductById_shouldThrowExceptionWhenProductDoesNotExist()
    {
        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> productService.getProductById(1L));

        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    void getProductsPageable_shouldReturrnThreeProductsWhenPageSizeIsThree()
    {
        List<Products> products = new ArrayList<>();
        Products product1 = new Products();
        product1.setProductId(1L);
        product1.setName("Product 1");
        products.add(product1);

        Products product2 = new Products();
        product2.setProductId(2L);
        product2.setName("Product 2");
        products.add(product2);

        Products product3 = new Products();
        product3.setProductId(3L);
        product3.setName("Product 3");
        products.add(product3);

        Products product4 = new Products();
        product4.setProductId(4L);
        product4.setName("Product 4");
        products.add(product4);

        Products product5 = new Products();
        product5.setProductId(5L);
        product5.setName("Product 5");
        products.add(product5);

        Pageable pageable = PageRequest.of(0,3);

        Page<Products> productsPage = new PageImpl<>(
                List.of(product1, product2, product3),
                pageable,
                5
        );

        when(productRepository.findAll(pageable))
                .thenReturn(productsPage);

        Page<ProductDto> result = productService.getProducts(pageable);

        assertEquals(3, result.getContent().size());
        assertEquals(5, result.getTotalElements());
    }
}