package com.deliverytech.delivey_api.service;

import com.deliverytech.delivey_api.exception.ResourceNotFoundException;
import com.deliverytech.delivey_api.model.Product;
import com.deliverytech.delivey_api.model.ProductDTO;
import com.deliverytech.delivey_api.model.Restaurant;
import com.deliverytech.delivey_api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RestaurantService restaurantService;

    @Transactional
    public ProductDTO createProduct(Product product, Long restaurantId) {
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço do produto deve ser positivo.");
        }

        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante com ID " + restaurantId + " não encontrado."));

        if (productRepository.findByRestaurantIdAndNameIgnoreCase(restaurantId, product.getName()).isPresent()) {
            throw new IllegalArgumentException("Já existe um produto com o nome '" + product.getName() + "' neste restaurante.");
        }

        product.setRestaurant(restaurant);
        product.setAvailable(true);

        Product savedProduct = productRepository.save(product);
        return new ProductDTO(savedProduct);
    }

    @Transactional(readOnly = true)
    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Product> findProductALl() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<ProductDTO> findProductDTOById(Long id) {
        return productRepository.findById(id)
                    .map(ProductDTO::new);
    }

    @Transactional
    public Optional<ProductDTO> updateProduct(Long id, Product updatedProduct) {
        if (updatedProduct.getPrice() == null || updatedProduct.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço do produto deve ser positivo.");
        }

        return productRepository.findById(id).map(existingProduct -> {
            Optional<Product> productWithSameName = productRepository.findByRestaurantIdAndNameIgnoreCase(existingProduct.getRestaurant().getId(), updatedProduct.getName());
            if (productWithSameName.isPresent() && !productWithSameName.get().getId().equals(id)) {
                throw new IllegalArgumentException("Já existe outro produto com o nome '" + updatedProduct.getName() + "' neste restaurante.");
            }

            existingProduct.setName(updatedProduct.getName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setCategory(updatedProduct.getCategory());

            Product savedProduct = productRepository.save(existingProduct);
            return new ProductDTO(savedProduct);
        });
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto com ID " + id + " não encontrado para deleção.");
        }
        productRepository.deleteById(id);
    }

    @Transactional
    public void initializeMockDataIfEmpty() {
        if (productRepository.count() == 0) {
            System.out.println("SERVICE: Criando dados iniciais de produtos...");

            Restaurant mockRestaurant = restaurantService.findRestaurantById(1L)
                .orElseGet(() -> {
                    System.out.println("SERVICE: Restaurante mock padrão (ID 1) não encontrado, criando um novo...");
                    Restaurant newMockRestaurant = Restaurant.builder()
                        .name("Pizzaria Dev Automática")
                        .address("Rua dos Testes, 1")
                        .phoneNumber("9999-1111")
                        .rating(4.0)
                        .active(true)
                        .build();
                    return restaurantService.saveRestaurant(newMockRestaurant);
                });

            createProduct(Product.builder()
                .name("Pizza Calabresa Teste")
                .description("Massa fina, calabresa, cebola, mussarela")
                .category("Pizza")
                .price(new BigDecimal("45.00"))
                .available(true)
                .restaurant(mockRestaurant)
                .build(), mockRestaurant.getId());

            createProduct(Product.builder()
                .name("Refrigerante Cola Teste")
                .description("Lata 350ml")
                .category("Bebida")
                .price(new BigDecimal("7.00"))
                .available(true)
                .restaurant(mockRestaurant)
                .build(), mockRestaurant.getId());

            System.out.println("SERVICE: Produtos iniciais inseridos para o Restaurante: " + mockRestaurant.getName());
        } else {
            System.out.println("SERVICE: Banco de dados H2 já possui produtos, pulando inicialização de produtos mock.");
        }
    }
}