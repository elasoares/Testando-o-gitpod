package com.deliverytech.delivey_api.repository;

import com.deliverytech.delivey_api.model.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat; // Para asserções mais legíveis

// @DataJpaTest configura um ambiente de teste otimizado para testes de repositório JPA
// Ele usa um banco de dados em memória (como H2) por padrão e rollback de transações
@DataJpaTest
public class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TestEntityManager entityManager; // Usado para inserir dados de teste diretamente

    @BeforeEach // Este método é executado antes de cada teste
    void setup() {
        // Limpa o banco de dados antes de cada teste para garantir isolamento
        clientRepository.deleteAll();

        // Insere dados de teste que serão usados pelos métodos de teste
        entityManager.persist(new Client("João Teste","joao@teste.com", "111", true));
        entityManager.persist(new Client("Maria Teste", "maria@teste.com", "222", false));
        entityManager.persist(new Client("Pedro Teste", "pedro@teste.com", "333", true));
        entityManager.flush(); // Garante que os dados sejam salvos no DB
        entityManager.clear(); // Limpa o cache do EntityManager para que as próximas buscas venham do DB
    }

    @Test
    @DisplayName("Deve encontrar um cliente pelo email")
    void shouldFindClientByEmail() {
        Optional<Client> foundClient = clientRepository.findByEmail("joao@teste.com");

        assertThat(foundClient).isPresent(); // Verifica se um cliente foi encontrado
        assertThat(foundClient.get().getName()).isEqualTo("João Teste"); // Verifica o nome
    }


    @Test
    @DisplayName("Deve encontrar todos os clientes ativos")
    void shouldFindAllActiveClients() {
        List<Client> activeClients = clientRepository.findByActiveTrue();

        assertThat(activeClients).hasSize(2); // Deve haver 2 clientes ativos (João, Pedro)
        assertThat(activeClients).extracting(Client::getName).containsExactlyInAnyOrder("João Teste", "Pedro Teste");
    }

    @Test
    @DisplayName("Deve encontrar todos os clientes inativos")
    void shouldFindAllInactiveClients() {
        List<Client> inactiveClients = clientRepository.findByActiveFalse();

        assertThat(inactiveClients).hasSize(1); // Deve haver 1 cliente inativo (Maria)
        assertThat(inactiveClients.get(0).getName()).isEqualTo("Maria Teste");
    }

    @Test
    @DisplayName("Deve encontrar cliente por email e status ativo")
    void shouldFindClientByEmailAndActiveTrue() {
        Optional<Client> foundPedro = clientRepository.findByEmailAndActiveTrue("pedro@teste.com");

        assertThat(foundPedro).isPresent();
        assertThat(foundPedro.get().getName()).isEqualTo("Pedro Teste");
        assertThat(foundPedro.get().isActive()).isTrue();
    }

    @Test
    @DisplayName("Deve encontrar um cliente pelo email")
    void ClientByEmailReturn() {
        Optional<Client> foundClient = clientRepository.findByEmail("joao@teste.com");

        assertThat(foundClient).isPresent();
        System.out.println("TESTE: Cliente encontrado por email: " + foundClient.get().getName()); // Este print aparecerá no terminal do Maven
        assertThat(foundClient.get().getName()).isEqualTo("João Teste");
    }

 
}