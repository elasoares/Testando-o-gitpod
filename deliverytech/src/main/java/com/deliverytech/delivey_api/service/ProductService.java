package com.deliverytech.delivey_api.service;

import com.deliverytech.delivey_api.exception.ResourceNotFoundException;
import com.deliverytech.delivey_api.model.Product;
import com.deliverytech.delivey_api.model.ProductDTO;
import com.deliverytech.delivey_api.model.Restaurant; // Keep this import for the entity
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
    private RestaurantService restaurantService; // This is the correct way to inject and use the service

    /**
     * Registers a new product for a specific restaurant.
     * Performs validations for price, unique name for the restaurant, and restaurant existence.
     *
     * @param product The Product object to be registered.
     * @param restaurantId The ID of the restaurant to which the product belongs.
     * @return ProductDTO of the registered product.
     * @throws IllegalArgumentException If the price is invalid, or the name already exists for the restaurant.
     * @throws ResourceNotFoundException If the restaurant is not found.
     */
    @Transactional
    public ProductDTO createProduct(Product product, Long restaurantId) {
        // 1. Price validation
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço do produto deve ser positivo.");
        }

        // 2. Verify restaurant existence using the service to get the entity
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante com ID " + restaurantId + " não encontrado."));

        // 3. Unique name validation per restaurant
        // Assuming findByRestaurantIdAndNameIgnoreCase is in ProductRepository
        if (productRepository.findByRestaurantIdAndNameIgnoreCase(restaurantId, product.getName()).isPresent()) {
            throw new IllegalArgumentException("Já existe um produto com o nome '" + product.getName() + "' neste restaurante.");
        }

        // Associate the product with the restaurant
        product.setRestaurant(restaurant);
        // Set default availability to true when registering
        product.setAvailable(true);

        Product savedProduct = productRepository.save(product);
        return new ProductDTO(savedProduct);
    }

    /**
     * Finds a product by ID.
     * @param id Product ID.
     * @return 
     */
        @Transactional(readOnly = true)
        public Optional<Product> findProductById(Long id) { 
            return productRepository.findById(id);
        }

        @Transactional(readOnly = true)
        public Optional<ProductDTO> findProductDTOById(Long id) { 
            return productRepository.findById(id)
                    .map(ProductDTO::new);
        }


    /**
     * Updates an existing product's data.
     * @param id Product ID to be updated.
     * @param updatedProduct Product object with updated data.
     * @return Optional of ProductDTO of the updated product. Empty if the product is not found.
     * @throws IllegalArgumentException If the price is invalid, or the name already exists for another product in the same restaurant.
     * @throws ResourceNotFoundException If the product to be updated is not found.
     */
    @Transactional
    public Optional<ProductDTO> updateProduct(Long id, Product updatedProduct) {
        // Price validation - Corrected for BigDecimal
        if (updatedProduct.getPrice() == null || updatedProduct.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço do produto deve ser positivo.");
        }

        return productRepository.findById(id).map(existingProduct -> {
            // Unique name validation for the same restaurant, excluding the product itself
            // Assuming findByRestaurantIdAndNameIgnoreCase is in ProductRepository
            Optional<Product> productWithSameName = productRepository.findByRestaurantIdAndNameIgnoreCase(existingProduct.getRestaurant().getId(), updatedProduct.getName());
            if (productWithSameName.isPresent() && !productWithSameName.get().getId().equals(id)) {
                throw new IllegalArgumentException("Já existe outro produto com o nome '" + updatedProduct.getName() + "' neste restaurante.");
            }

            // Update fields
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setCategory(updatedProduct.getCategory());
            // Availability will be updated by a separate method for clarity

            Product savedProduct = productRepository.save(existingProduct);
            return new ProductDTO(savedProduct);
        });
    }

   

    /**
     * Deletes a product by ID.
     * @param id Product ID to be deleted.
     * @throws ResourceNotFoundException If the product is not found.
     */
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto com ID " + id + " não encontrado para deleção.");
        }
        productRepository.deleteById(id);
    }

    // Method for data initialization (for development/testing with H2)
    @Transactional
    public void initializeMockDataIfEmpty() {
        // You can check if there are ALREADY PRODUCTS, instead of restaurants, or add more logic.
        // It's ideal that RestaurantService.initializeMockDataIfEmpty() is called BEFORE this one,
        // to ensure restaurants exist.
        if (productRepository.count() == 0) { // Check if products already exist
            System.out.println("SERVICE: Criando dados iniciais de produtos...");

            // Ensure the restaurant exists before associating products
            // This assumes initializeMockDataIfEmpty from RestaurantService has already run.
            // For a robust test environment, it would be better to search for the restaurant by a known name or ID.
            // In ProductService.java's initializeMockDataIfEmpty()
    Restaurant mockRestaurant = restaurantService.findRestaurantById(1L)
        .orElseGet(() -> {
            System.out.println("SERVICE: Restaurante mock padrão (ID 1) não encontrado, criando um novo...");
            // 1. Create the new Restaurant entity using its builder
            Restaurant newMockRestaurant = Restaurant.builder()
                .name("Pizzaria Dev Automática")
                .address("Rua dos Testes, 1")
                .phoneNumber("9999-1111")
                .rating(4.0)
                .active(true)
                .build();
            // 2. IMPORTANT: Return the result of saving this new entity.
            //    restaurantService.saveRestaurant MUST return a Restaurant entity.
            return restaurantService.saveRestaurant(newMockRestaurant); // <--- This line must return a Restaurant
        });

            // Use the builder to create products and associate them with the restaurant
            // Note that the product 'id' is null; it will be automatically generated.
            // The 'restaurant' needs to be the complete entity.
           createProduct(Product.builder()
            .name("Pizza Calabresa Teste")
            .description("Massa fina, calabresa, cebola, mussarela")
            .category("Pizza")
            .price(new BigDecimal("45.00"))
            .available(true)
            .restaurant(mockRestaurant) // Correctly associating the Product with the Restaurant entity
            .build(), mockRestaurant.getId());

            createProduct(Product.builder()
                .name("Refrigerante Cola Teste")
                .description("Lata 350ml")
                .category("Bebida")
                .price(new BigDecimal("7.00")) // Use BigDecimal constructor
                .available(true)
                .restaurant(mockRestaurant)
                .build(), mockRestaurant.getId());

            System.out.println("SERVICE: Produtos iniciais inseridos para o Restaurante: " + mockRestaurant.getName());
        } else {
            System.out.println("SERVICE: Banco de dados H2 já possui produtos, pulando inicialização de produtos mock.");
        }
    }
}