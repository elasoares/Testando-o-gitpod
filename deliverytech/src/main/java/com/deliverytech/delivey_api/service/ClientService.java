package com.deliverytech.delivey_api.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deliverytech.delivey_api.model.Client;
import com.deliverytech.delivey_api.model.ClientDTO;
import com.deliverytech.delivey_api.repository.ClientRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {
    @Autowired
    private ClientRepository repository;

    @Transactional
    public ClientDTO createClient(Client client){
        client.setActive(true);
        Client savedClient = repository.save(client);
        return new ClientDTO(savedClient);
    }

    @Transactional
    public List<ClientDTO>findAllClients(){
        return repository.findAll().stream()
                .map(ClientDTO::new)
                .collect(Collectors.toList());
    }

    /* O uso do Optional<ClientDTO> ajuda a retornar um ClientDTO (se o ID existir no banco),
     * Ou pode não retornar nada (se o ID não existir).
     * É uma forma segura de evitar null e forçar quem chama o método a tratar o
     * caso de “cliente não encontrado”.
     * >>> This method is good for API exposure, but not for internal service logic that needs the entity. <<<
     */
    @Transactional(readOnly = true)
    public Optional<ClientDTO> findClientDTOById(Long id){ 
        return repository.findById(id)
                .map(ClientDTO::new);
    }


    @Transactional(readOnly = true)
    public Optional<Client> findClientById(Long id) { 
        return repository.findById(id);
    }
   


        @Transactional
        public Optional<Client> updateClient(Long id, Client updatedClient) { 
            return repository.findById(id)
                    .map(client -> {
                        client.setName(updatedClient.getName());
                        client.setEmail(updatedClient.getEmail());
                        client.setPhoneNumber(updatedClient.getPhoneNumber());

                        if (updatedClient.getPassword() != null && !updatedClient.getPassword().isEmpty()) {
                            client.setPassword(updatedClient.getPassword()); 
                        }
                        if (updatedClient.getDeliveryAddress() != null && !updatedClient.getDeliveryAddress().isEmpty()) {
                            client.setDeliveryAddress(updatedClient.getDeliveryAddress());
                        }
                        client.setActive(updatedClient.isActive()); 

                        return repository.save(client);
                    });
        }


    @Transactional
    public void initializeMockDataIfEmpty(){
    if(repository.count() == 0){
        System.out.println("SERVICE: Clientes iniciais inseridos: " + repository.count());

        createClient(Client.builder()
            .name("Ana")
            .email("ana@gmail.com")
            .password("hashed_password_ana")
            .phoneNumber("1111-1111")
            .deliveryAddress("Rua das Oliveiras, 10")
            .active(true)
            .build());

        createClient(Client.builder()
            .name("Pedro")
            .email("pedro@gmail.com")
            .password("hashed_password_pedro")
            .phoneNumber("2222-2222")
            .deliveryAddress("Av. Central, 250")
            .active(false)
            .build());

        createClient(Client.builder()
            .name("Elaine")
            .email("elaine@gmail.com")
            .password("hashed_password_elaine")
            .phoneNumber("3333-3333")
            .deliveryAddress("Travessa dos Sonhos, 5")
            .active(true)
            .build());

        System.out.println("SERVICE: Clientes iniciais inseridos: " + repository.count());
    } else {
        System.out.println("SERVICE: Banco H2 já possui dados cadastrados, pulando inicialização de clientes mock.");
    }
}
  
    @Transactional
    public boolean deleteClient(Long id){
        if(repository.existsById(id)){
            repository.deleteById(id);
            return true;
        }
        return false;

    }

}