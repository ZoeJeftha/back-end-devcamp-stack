package za.co.entelect.devcamp.productcatalog.service;

import  za.co.entelect.devcamp.productcatalog.repository.ProductRepository;
import  za.co.entelect.devcamp.productcatalog.dto.ProductDto;
import  za.co.entelect.devcamp.productcatalog.model.Products;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;
import za.co.entelect.devcamp.productcatalog.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ProductService implements IProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository)
    {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDto> getProducts() {
        List<Products> allProducts = productRepository.findAll();

        List<ProductDto> productDtoList = allProducts.stream()
                .map(product -> new ProductDto(
                        product.getProductId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getImageUrl()
                ))
                .collect(Collectors.toList());
        return productDtoList;
    }

    @Override
    public ProductDto getProductById(Long id) {
        Products product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductDto productDto = new ProductDto(
                product.getProductId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl()
        );
        return productDto;
    }

    @Override
    public Page<ProductDto> getProducts(Pageable pageable) {

        Page<Products> products = productRepository.findAll(pageable);

        return products.map(product -> new ProductDto(
                product.getProductId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl()
        ));
    }
}