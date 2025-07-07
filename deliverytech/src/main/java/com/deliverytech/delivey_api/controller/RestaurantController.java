package com.deliverytech.delivey_api.controller;

import com.deliverytech.delivey_api.model.Restaurant;
import com.deliverytech.delivey_api.model.RestaurantDTO;
import com.deliverytech.delivey_api.service.RestaurantService;
import com.deliverytech.delivey_api.exception.ResourceNotFoundException; // Importe sua exceção

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Importa todas as anotações web comuns

import java.util.List;

@RestController // Indica que é um controlador REST
@RequestMapping("/api/v1/restaurants") // Define o caminho base para todos os endpoints deste controlador
public class RestaurantController {

    private final RestaurantService restaurantService; // Usar 'final' é uma boa prática com injeção por construtor

    // Injeção de dependência via construtor (recomendado pelo Spring)
    // A anotação @Autowired no construtor é opcional a partir do Spring 4.3 se houver apenas um construtor
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    /**
     * Endpoint para cadastrar um novo restaurante.
     * POST /api/v1/restaurants
     * @param restaurant Objeto Restaurant enviado no corpo da requisição.
     * @return ResponseEntity com RestaurantDTO do restaurante criado e status 201 Created,
     * ou status 409 Conflict se o nome já estiver em uso.
     */
    @PostMapping
    public ResponseEntity<RestaurantDTO> createRestaurant(@RequestBody Restaurant restaurant) {
        try {
            // Chamada ao método createRestaurant no serviço
            RestaurantDTO newRestaurant = restaurantService.createRestaurant(restaurant);
            return ResponseEntity.status(HttpStatus.CREATED).body(newRestaurant); // Retorna 201 Created
        } catch (IllegalArgumentException e) {
            // Captura a exceção de nome duplicado do serviço
            System.err.println("Erro ao cadastrar restaurante: " + e.getMessage()); // Log para depuração
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Retorna 409 Conflict
        }
    }

    /**
     * Endpoint para buscar todos os restaurantes.
     * GET /api/v1/restaurants
     * @return Lista de RestaurantDTOs de todos os restaurantes.
     */
    @GetMapping
    public List<RestaurantDTO> getAllRestaurants() {
        return restaurantService.findAll(); // Retorna a lista completa
    }

    /**
     * Endpoint para buscar um restaurante pelo ID.
     * GET /api/v1/restaurants/{id}
     * @param id ID do restaurante a ser buscado.
     * @return ResponseEntity com RestaurantDTO do restaurante encontrado e status 200 OK,
     * ou status 404 Not Found se o restaurante não existir.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDTO> getRestaurantById(@PathVariable Long id) {
        return restaurantService.findRestaurantById(id) // Busca o restaurante pelo ID
                .map(ResponseEntity::ok) // Se encontrado, retorna 200 OK com o DTO
                .orElse(ResponseEntity.notFound().build()); // Se não encontrado, retorna 404 Not Found
    }

    /**
     * Endpoint para buscar todos os restaurantes que estão ativos.
     * GET /api/v1/restaurants/active
     * @return Lista de RestaurantDTOs de restaurantes ativos.
     */
    @GetMapping("/active")
    public List<RestaurantDTO> getActiveRestaurants() {
        return restaurantService.findActiveRestaurants();
    }

    /**
     * Endpoint para atualizar os dados de um restaurante existente.
     * PUT /api/v1/restaurants/{id}
     * @param id ID do restaurante a ser atualizado.
     * @param restaurant Objeto Restaurant com os dados atualizados.
     * @return ResponseEntity com RestaurantDTO do restaurante atualizado e status 200 OK,
     * ou status 404 Not Found se o restaurante não existir,
     * ou status 409 Conflict se o novo nome já estiver em uso.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDTO> updateRestaurant(@PathVariable Long id, @RequestBody Restaurant restaurant) {
        try {
            return restaurantService.updateRestaurant(id, restaurant) // Tenta atualizar o restaurante
                    .map(ResponseEntity::ok) // Se encontrado e atualizado, retorna 200 OK
                    .orElse(ResponseEntity.notFound().build()); // Se não encontrado, retorna 404 Not Found
        } catch (IllegalArgumentException e) {
            // Captura a exceção de nome duplicado do serviço
            System.err.println("Erro ao atualizar restaurante: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Retorna 409 Conflict
        }
    }

    /**
     * Endpoint para inativar um restaurante.
     * PATCH /api/v1/restaurants/{id}/deactivate
     * @param id ID do restaurante a ser inativado.
     * @return ResponseEntity com status 204 No Content se inativado com sucesso, false caso contrário (se não encontrado ou já inativo).
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateRestaurant(@PathVariable Long id) {
        boolean deactivated = restaurantService.deactivateRestaurant(id);
        return deactivated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Endpoint para ativar um restaurante.
     * PATCH /api/v1/restaurants/{id}/activate
     * @param id ID do restaurante a ser ativado.
     * @return ResponseEntity com status 204 No Content se ativado com sucesso, false caso contrário (se não encontrado ou já ativo).
     */
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateRestaurant(@PathVariable Long id) {
        boolean activated = restaurantService.activateRestaurant(id);
        return activated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Endpoint para deletar um restaurante.
     * DELETE /api/v1/restaurants/{id}
     * @param id ID do restaurante a ser deletado.
     * @return ResponseEntity com status 204 No Content se deletado com sucesso,
     * ou status 404 Not Found se o restaurante não existir.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        try {
            restaurantService.deleteRestaurant(id);
            return ResponseEntity.noContent().build(); // 204 No Content para deleção bem-sucedida
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Retorna 404 Not Found se não existir
        }
    }
}