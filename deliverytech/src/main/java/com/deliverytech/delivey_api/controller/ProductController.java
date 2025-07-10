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

    private final ProductService productService; // Injeção de dependência via construtor

    // Construtor para injeção de dependência
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    /**
     * Endpoint para cadastrar um novo produto para um resta POST 
     * /api/v1/restaurants/{restaurantId}/products
     *
     * @param restaurantId ID do restaurante ao qual o produto pertencerá.
     * @param product      Objeto Product enviado no corpo da requisição.
     * @return ResponseEntity com ProductDTO do produto criado e status 201 Created.
     * @throws HttpStatus.BAD_REQUEST (400) Se a validação de dados falhar (preço, nome duplicado).
     * @throws HttpStatus.NOT_FOUND (404) Se o restaurante não for encontrado.
     */
    @PostMapping("/restaurants/{restaurantId}/products")
    public ResponseEntity<ProductDTO> createProduct(
            @PathVariable Long restaurantId,
            @RequestBody Product product) {
        try {
            ProductDTO newProduct = productService.createProduct(product, restaurantId);
            return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
        } catch (IllegalArgumentException e) {
            // Captura validações de preço ou nome duplicado para este restaurante
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); 
                } catch (ResourceNotFoundException e) {
            // Captura o caso onde o restaurante não existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); 
        }
    }


    /**
     * Endpoint para buscar um produto pelo seu ID global.
     * GET /api/v1/products/{id}
     *
     @param id ID do produto.
     @return ResponseEntity com ProductDTO do produto encontrado e status 200 OK,
     * ou status 404 Not Found se o produto não existir.
     */
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



    /**
     * Endpoint para atualizar os dados de um produto existente.
     * PUT /api/v1/products/{id}
     *
     * @param id           ID do produto a ser atualizado.
     * @param product      Objeto Product com os dados atualizados.
     * @return ResponseEntity com ProductDTO do produto atualizado e status 200 OK.
     * @throws HttpStatus.BAD_REQUEST (400) Se a validação de dados falhar (preço, nome duplicado).
     * @throws HttpStatus.NOT_FOUND (404) Se o produto não for encontrado.
     */
    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            return productService.updateProduct(id, product)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build()); // Retorna 404 se o produto não for encontrado
        } catch (IllegalArgumentException e) {
            // Captura validações de preço ou nome duplicado
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

  
    /**
     * Endpoint para deletar um produto.
     * DELETE /api/v1/products/{id}
     *
     * @param id ID do produto a ser deletado.
     * @return ResponseEntity com status 204 No Content se deletado com sucesso,
     * ou status 404 Not Found se o produto não existir.
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build(); // 204 No Content para deleção bem-sucedida
        } catch (ResourceNotFoundException e) {
            // Captura o caso onde o produto não existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}