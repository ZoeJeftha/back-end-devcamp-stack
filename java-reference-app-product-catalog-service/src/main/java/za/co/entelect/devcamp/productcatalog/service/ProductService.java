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

@Component
public class ProductService implements IProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository)
    {
        this.productRepository = productRepository;
    }

    @Override
    public ResponseEntity<List<ProductDto>> getProducts() {
        try {
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
            return new ResponseEntity<>(productDtoList, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<ProductDto> getProductById(Long id) {
        try
        {
            Products product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            ProductDto productDto = new ProductDto(
                    product.getProductId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getImageUrl()
            );
            return new ResponseEntity<>(productDto, HttpStatus.OK);
        }
        catch(Exception e)
        {
            return ResponseEntity.internalServerError().build();
        }
    }

//    @Override
//    public ResponseEntity<ProductDto> createProduct(ProductDto productDto) {
//        Products product = new Products();
//
//        product.setName(productDto.getName());
//        product.setDescription(productDto.getDescription());
//        product.setPrice(productDto.getPrice());
//        product.setImageUrl(productDto.getImageUrl());
//
//        Products savedProduct = productRepository.save(product);
//
//        ProductDto productDtoResponse = ProductDto(
//                savedProduct.getProductId(),
//                savedProduct.getName(),
//                savedProduct.getDescription(),
//                savedProduct.getPrice(),
//                savedProduct.getImageUrl()
//        );
//        return new ResponseEntity<>(productDtoResponse, HttpStatus.OK);
//    }
}