package com.deliverytech.delivey_api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deliverytech.delivey_api.model.Restaurant;
import com.deliverytech.delivey_api.model.RestaurantDTO;
import com.deliverytech.delivey_api.repository.RestaurantRepository;
import com.deliverytech.delivey_api.exception.ResourceNotFoundException; // Importe sua exceção

import jakarta.transaction.Transactional;


@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository repository;

    /**
     * Cadastra um novo restaurante.
     * Realiza validação de nome único.
     * @param restaurant O objeto Restaurant a ser cadastrado.
     * @return RestaurantDTO do restaurante cadastrado.
     * @throws IllegalArgumentException Se o nome do restaurante já estiver em uso.
     */
    @Transactional
    public RestaurantDTO createRestaurant(Restaurant restaurant) {
        // Validação de nome único
        if (repository.findByName(restaurant.getName()).isPresent()) {
            throw new IllegalArgumentException("Nome de restaurante já cadastrado: " + restaurant.getName());
        }
        // Define como ativo por padrão ao criar
        restaurant.setActive(true);
        Restaurant savedRestaurant = repository.save(restaurant);
        return new RestaurantDTO(savedRestaurant);
    }

    /**
     * Busca todos os restaurantes.
     * @return Lista de RestaurantDTOs de todos os restaurantes.
     */
    public List<RestaurantDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(RestaurantDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Busca um restaurante pelo ID.
     * @param id ID do restaurante.
     * @return Optional de RestaurantDTO. Empty se não encontrado.
     */
    public Optional<RestaurantDTO> findRestaurantById(Long id) {
        return repository.findById(id)
                .map(RestaurantDTO::new);
    }

    /**
     * Busca todos os restaurantes que estão ativos.
     * @return Lista de RestaurantDTOs de restaurantes ativos.
     */
    public List<RestaurantDTO> findActiveRestaurants() {
        return repository.findByActiveTrue().stream()
                .map(RestaurantDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Atualiza os dados de um restaurante existente.
     * O nome não pode ser alterado para um nome já existente por outro restaurante.
     * @param id ID do restaurante a ser atualizado.
     * @param updatedRestaurant Objeto Restaurant com os dados atualizados.
     * @return Optional de RestaurantDTO do restaurante atualizado. Empty se o restaurante não for encontrado.
     * @throws IllegalArgumentException Se o novo nome já estiver em uso por outro restaurante.
     */
    @Transactional
    public Optional<RestaurantDTO> updateRestaurant(Long id, Restaurant updatedRestaurant) {
        return repository.findById(id).map(existingRestaurant -> {
            // Valida se o nome foi alterado e se o novo nome já existe para OUTRO restaurante
            if (!existingRestaurant.getName().equals(updatedRestaurant.getName()) &&
                repository.findByName(updatedRestaurant.getName()).isPresent()) {
                throw new IllegalArgumentException("O novo nome já está em uso por outro restaurante: " + updatedRestaurant.getName());
            }

            // Atualiza os campos
            existingRestaurant.setName(updatedRestaurant.getName());
            existingRestaurant.setAddress(updatedRestaurant.getAddress());
            existingRestaurant.setPhoneNumber(updatedRestaurant.getPhoneNumber());
            existingRestaurant.setRating(updatedRestaurant.getRating());
            // A ativação/inativação será feita por um método separado para clareza

            Restaurant savedRestaurant = repository.save(existingRestaurant);
            return new RestaurantDTO(savedRestaurant);
        });
    }

    /**
     * Inativa um restaurante, definindo seu status como false.
     * @param id ID do restaurante a ser inativado.
     * @return True se o restaurante foi inativado com sucesso, false caso contrário (se não encontrado ou já inativo).
     */
    @Transactional
    public boolean deactivateRestaurant(Long id) {
        return repository.findById(id).map(restaurant -> {
            if (restaurant.isActive()) { // Só inativa se estiver ativo
                restaurant.setActive(false);
                repository.save(restaurant);
                return true;
            }
            return false; // Já estava inativo ou não pôde ser inativado
        }).orElse(false); // Restaurante não encontrado
    }

    /**
     * Ativa um restaurante, definindo seu status como true.
     * @param id ID do restaurante a ser ativado.
     * @return True se o restaurante foi ativado com sucesso, false caso contrário (se não encontrado ou já ativo).
     */
    @Transactional
    public boolean activateRestaurant(Long id) {
        return repository.findById(id).map(restaurant -> {
            if (!restaurant.isActive()) { // Só ativa se estiver inativo
                restaurant.setActive(true);
                repository.save(restaurant);
                return true;
            }
            return false; // Já estava ativo ou não pôde ser ativado
        }).orElse(false); // Restaurante não encontrado
    }

    /**
     * Deleta um restaurante pelo ID.
     * @param id ID do restaurante a ser deletado.
     * @throws ResourceNotFoundException Se o restaurante não for encontrado.
     */
    @Transactional
    public void deleteRestaurant(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Restaurante com ID " + id + " não encontrado para deleção.");
        }
        repository.deleteById(id);
    }

    /**
     * Método para inicializar dados mock de restaurantes se o repositório estiver vazio.
     * Útil para testes e desenvolvimento com H2.
     */
    @Transactional
    public void initializeMockDataIfEmpty() {
        if (repository.count() == 0) {
            System.out.println("SERVICE: Inserindo dados iniciais de Restaurantes no H2...");
            // Usar o método createRestaurant do próprio service para garantir validações
            createRestaurant(new Restaurant(null, "Pizzaria Dev", "Rua das Flores, 100", "11987654321", 4.8, true, new ArrayList<>()));
            createRestaurant(new Restaurant(null, "Burger Code", "Avenida dos Dados, 200", "22912345678", 4.5, true, new ArrayList<>()));
            createRestaurant(new Restaurant(null, "Cantina Java", "Travessa do Loop, 300", "33998765432", 4.2, false, new ArrayList<>())); // Inativo
            System.out.println("SERVICE: Restaurantes iniciais inseridos: " + repository.count());
        } else {
            System.out.println("SERVICE: Banco de dados H2 já possui restaurantes, pulando inicialização.");
        }
    }
}