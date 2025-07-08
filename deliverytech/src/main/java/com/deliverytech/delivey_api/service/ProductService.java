package com.deliverytech.delivey_api.service;

import com.deliverytech.delivey_api.exception.ResourceNotFoundException;
import com.deliverytech.delivey_api.model.Product;
import com.deliverytech.delivey_api.model.ProductDTO;
import com.deliverytech.delivey_api.model.Restaurant; // Importe a entidade Restaurant
import com.deliverytech.delivey_api.repository.ProductRepository;
import com.deliverytech.delivey_api.repository.RestaurantRepository; // Importe o RestaurantRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importe este

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RestaurantRepository restaurantRepository; // Injeta o repositório de Restaurante

    /**
     * Cadastra um novo produto para um restaurante específico.
     * Realiza validações de preço, nome único para o restaurante e existência do restaurante.
     *
     * @param product O objeto Product a ser cadastrado.
     * @param restaurantId O ID do restaurante ao qual o produto pertence.
     * @return ProductDTO do produto cadastrado.
     * @throws IllegalArgumentException Se o preço for inválido, o nome já existir para o restaurante.
     * @throws ResourceNotFoundException Se o restaurante não for encontrado.
     */
    @Transactional
    public ProductDTO createProduct(Product product, Long restaurantId) {
        // 1. Validação de preço
        if (product.getPrice() == null || product.getPrice() <= 0) {
            throw new IllegalArgumentException("O preço do produto deve ser positivo.");
        }

        // 2. Verificar existência do restaurante
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante com ID " + restaurantId + " não encontrado."));

        // 3. Validação de nome único por restaurante
        if (productRepository.findByRestaurantIdAndNameIgnoreCase(restaurantId, product.getName()).isPresent()) {
            throw new IllegalArgumentException("Já existe um produto com o nome '" + product.getName() + "' neste restaurante.");
        }

        // Associa o produto ao restaurante
        product.setRestaurant(restaurant);
        // Define a disponibilidade padrão para true ao cadastrar
        product.setAvailable(true);

        Product savedProduct = productRepository.save(product);
        return new ProductDTO(savedProduct);
    }

    /**
     * Busca um produto pelo ID.
     * @param id ID do produto.
     * @return Optional de ProductDTO. Empty se não encontrado.
     */
    public Optional<ProductDTO> findProductById(Long id) {
        return productRepository.findById(id)
                .map(ProductDTO::new);
    }

    /**
     * Busca todos os produtos de um restaurante específico.
     * @param restaurantId ID do restaurante.
     * @return Lista de ProductDTOs dos produtos do restaurante.
     * @throws ResourceNotFoundException Se o restaurante não for encontrado.
     */
    public List<ProductDTO> findProductsByRestaurant(Long restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new IllegalArgumentException("Restaurante com ID " + restaurantId + " não encontrado.");
        }
        return productRepository.findByRestaurantId(restaurantId).stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Busca todos os produtos disponíveis de um restaurante específico.
     * @param restaurantId ID do restaurante.
     * @return Lista de ProductDTOs dos produtos disponíveis do restaurante.
     * @throws ResourceNotFoundException Se o restaurante não for encontrado.
     */
    public List<ProductDTO> findAvailableProductsByRestaurant(Long restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new IllegalArgumentException("Restaurante com ID " + restaurantId + " não encontrado.");
        }
        return productRepository.findByRestaurantIdAndAvailableTrue(restaurantId).stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Atualiza os dados de um produto existente.
     * @param id ID do produto a ser atualizado.
     * @param updatedProduct Objeto Product com os dados atualizados.
     * @return Optional de ProductDTO do produto atualizado. Empty se o produto não for encontrado.
     * @throws IllegalArgumentException Se o preço for inválido, ou o nome já existir para outro produto no mesmo restaurante.
     */
    @Transactional
    public Optional<ProductDTO> updateProduct(Long id, Product updatedProduct) {
        // Validação de preço
        if (updatedProduct.getPrice() == null || updatedProduct.getPrice() <= 0) {
            throw new IllegalArgumentException("O preço do produto deve ser positivo.");
        }

        return productRepository.findById(id).map(existingProduct -> {
            // Validação de nome único para o mesmo restaurante, exceto para o próprio produto
            Optional<Product> productWithSameName = productRepository.findByRestaurantIdAndNameIgnoreCase(existingProduct.getRestaurant().getId(), updatedProduct.getName());
            if (productWithSameName.isPresent() && !productWithSameName.get().getId().equals(id)) {
                throw new IllegalArgumentException("Já existe outro produto com o nome '" + updatedProduct.getName() + "' neste restaurante.");
            }

            // Atualiza os campos
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setCategory(updatedProduct.getCategory());
            // A disponibilidade será atualizada por um método separado para clareza

            Product savedProduct = productRepository.save(existingProduct);
            return new ProductDTO(savedProduct);
        });
    }

    /**
     * Define a disponibilidade de um produto.
     * @param id ID do produto.
     * @param available Status de disponibilidade (true para disponível, false para indisponível).
     * @return True se a disponibilidade foi atualizada, false se o produto não foi encontrado.
     */
    @Transactional
    public boolean setProductAvailability(Long id, boolean available) {
        return productRepository.findById(id).map(product -> {
            if (product.isAvailable() != available) { // Só atualiza se o status for diferente
                product.setAvailable(available);
                productRepository.save(product);
                return true;
            }
            return false; // Status já era o desejado
        }).orElse(false); // Produto não encontrado
    }

    /**
     * Deleta um produto pelo ID.
     * @param id ID do produto a ser deletado.
     * @throws ResourceNotFoundException Se o produto não for encontrado.
     */
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Produto com ID " + id + " não encontrado para deleção.");
        }
        productRepository.deleteById(id);
    }

    // Método de inicialização de dados (para desenvolvimento/testes com H2)
    @Transactional
    public void initializeMockDataIfEmpty() {
        if (restaurantRepository.count() == 0) {
             System.out.println("SERVICE: Criando restaurante mock para produtos...");
             Restaurant mockRestaurant = restaurantRepository.save(new Restaurant(null, "Mock Restaurante Pizzaria", "Rua Mock, 123", "9999-0000", 4.5, true, new ArrayList<>()));
             System.out.println("SERVICE: Restaurante mock criado com ID: " + mockRestaurant.getId());
             // Salvar produtos usando o service para validar e associar
             createProduct(new Product("Pizza Calabresa", "Massa fina, calabresa, cebola, mussarela", "Pizza", 45.00, true ), mockRestaurant.getId());
             createProduct(new Product("Refrigerante Cola", "Lata 350ml", "Bebida", 7.00, true ), mockRestaurant.getId());
             createProduct(new Product("Brownie com Sorvete", "Brownie caseiro com bola de sorvete de creme", "Sobremesa", 22.50, true), mockRestaurant.getId());
             System.out.println("SERVICE: Produtos iniciais inseridos para o Restaurante: " + mockRestaurant.getName());
        } else {
             System.out.println("SERVICE: Restaurantes já existem, pulando criação de produtos mock.");
        }
    }
}