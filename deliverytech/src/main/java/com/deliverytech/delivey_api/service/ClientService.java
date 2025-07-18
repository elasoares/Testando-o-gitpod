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

            createClient(new Client("Ana", "ana@gmail.com", "1111-1111", "hashed_password_ana", "Rua das Oliveiras, 10", true));
            createClient(new Client("Pedro", "pedro@gmail.com", "2222-2222", "hashed_password_pedro", "Av. Central, 250", false));
            createClient(new Client("Elaine", "elaine@gmail.com", "3333-3333", "hashed_password_elaine", "Travessa dos Sonhos, 5", true));

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