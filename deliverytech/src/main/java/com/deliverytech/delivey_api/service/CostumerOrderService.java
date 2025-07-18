package com.deliverytech.delivey_api.service;

import com.deliverytech.delivey_api.model.CostumerOrder;
import com.deliverytech.delivey_api.model.Address;
import com.deliverytech.delivey_api.model.Client;
import com.deliverytech.delivey_api.model.OrderItem;
import com.deliverytech.delivey_api.model.OrderStatus;
import com.deliverytech.delivey_api.model.Product;
import com.deliverytech.delivey_api.model.Restaurant;
import com.deliverytech.delivey_api.repository.CostumerOrderRepository;
import com.deliverytech.delivey_api.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList; 

@Service
public class CostumerOrderService {

    @Autowired
    private CostumerOrderRepository costumerOrderRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Transactional
    public CostumerOrder createOrder(CostumerOrder order) {
        Client client = clientService.findClientById(order.getClient().getId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));
        Restaurant restaurant = restaurantService.findRestaurantById(order.getRestaurant().getId())
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado."));

        order.setClient(client);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());

        BigDecimal totalOrder = BigDecimal.ZERO;

        if (order.getItems() != null && !order.getItems().isEmpty()) {
            List<OrderItem> currentOrderItems = new ArrayList<>(order.getItems()); 

            for (OrderItem item : currentOrderItems) {
                Product product = productService.findProductById(item.getProduct().getId())
                        .orElseThrow(() -> new RuntimeException("Produto não encontrado no item: " + item.getProduct().getId()));

                if (!product.getRestaurant().getId().equals(restaurant.getId())) {
                    throw new RuntimeException("Produto " + product.getName() + " não pertence a este restaurante.");
                }
                if (!product.isAvailable()) {
                    throw new RuntimeException("Produto " + product.getName() + " não está disponível.");
                }

                item.setProduct(product);
                item.setUnitPrice(product.getPrice());
                item.setOrder(order); 
                order.getItems().add(item);

                totalOrder = totalOrder.add(item.getUnitPrice().multiply(new BigDecimal(item.getAmount())));
            }
        }
        order.setTotal(totalOrder);

        return costumerOrderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Optional<CostumerOrder> findOrderById(Long id) {
        return costumerOrderRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<CostumerOrder> findAllOrders() {
        return costumerOrderRepository.findAll();
    }

    @Transactional
    public CostumerOrder updateOrderStatus(Long id, OrderStatus newStatus) {
        CostumerOrder order = costumerOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));
        order.setStatus(newStatus);
        return costumerOrderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(Long id) {
        costumerOrderRepository.deleteById(id);
    }

    @Transactional
    public void initializeMockDataIfEmpty() {
        if (costumerOrderRepository.count() == 0) {
            System.out.println("SERVICE: Inserindo dados iniciais de Pedidos no H2...");

            clientService.initializeMockDataIfEmpty();
            restaurantService.initializeMockDataIfEmpty();
            productService.initializeMockDataIfEmpty();

            Client joao = clientService.findClientById(1L).orElseThrow(() -> new RuntimeException("Cliente João não encontrado"));
            Restaurant pizzaria = restaurantService.findRestaurantById(1L).orElseThrow(() -> new RuntimeException("Pizzaria Dev não encontrada"));
            Product pizzaCalabresa = productService.findProductById(1L).orElseThrow(() -> new RuntimeException("Pizza Calabresa não encontrada"));
            Product cocaCola = productService.findProductById(2L).orElseThrow(() -> new RuntimeException("Coca-Cola não encontrada"));

            Address deliveryAddress1 = new Address("Rua do Sol", "10", "Centro", "Cidade Teste", "TS", "12345-678");

            CostumerOrder order1 = new CostumerOrder();
            order1.setClient(joao);
            order1.setRestaurant(pizzaria);
            order1.setDeliveryAddress(deliveryAddress1);

            OrderItem item1_1 = new OrderItem();
            item1_1.setProduct(pizzaCalabresa);
            item1_1.setAmount(1);

            OrderItem item1_2 = new OrderItem();
            item1_2.setProduct(cocaCola);
            item1_2.setAmount(2);

            List<OrderItem> itemsForOrder1 = new ArrayList<>();
            itemsForOrder1.add(item1_1);
            itemsForOrder1.add(item1_2);
            order1.setItems(itemsForOrder1); 

            createOrder(order1);

            System.out.println("SERVICE: Pedidos iniciais inseridos: " + costumerOrderRepository.count());
        } else {
            System.out.println("SERVICE: Banco de dados H2 já possui pedidos, pulando inicialização.");
        }
    }
}