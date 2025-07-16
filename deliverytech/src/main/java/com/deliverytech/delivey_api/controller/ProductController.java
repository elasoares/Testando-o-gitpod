package com.deliverytech.delivey_api.controller;

import com.deliverytech.delivey_api.model.Product;
import com.deliverytech.delivey_api.model.ProductDTO;
import com.deliverytech.delivey_api.service.ProductService;
import com.deliverytech.delivey_api.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/restaurants/{restaurantId}/products")
    public ResponseEntity<ProductDTO> createProduct(
            @PathVariable Long restaurantId,
            @RequestBody Product product) {
        try {
            ProductDTO newProduct = productService.createProduct(product, restaurantId);
            return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
    return productService.findProductById(id)
            .map(product -> new ResponseEntity<>(new ProductDTO(product), HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/restaurants/products")
    public List<Product> getProductAll() {
    return productService.findProductALl();
    }


    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            return productService.updateProduct(id, product)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}