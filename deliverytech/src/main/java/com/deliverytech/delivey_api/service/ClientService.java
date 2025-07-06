package com.deliverytech.delivey_api.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deliverytech.delivey_api.model.Client;
import com.deliverytech.delivey_api.model.ClientDTO;
import com.deliverytech.delivey_api.repository.ClientRepository;

import jakarta.transaction.Transactional;

@Service
public class ClientService {
    @Autowired
    private ClientRepository repository;

    @Transactional
    public ClientDTO createClient(Client client){
        if(repository.findByEmail(client.getEmail()).isPresent()){
            throw new IllegalArgumentException("E-mail já cadastrado: " + client.getEmail());
        }
        client.setActive(true);
        Client savedClient = repository.save(client);
        return new ClientDTO(savedClient);
    }

    public List<ClientDTO>findAllClients(){
        return repository.findAll().stream()
                .map(ClientDTO::new)
                .collect(Collectors.toList());
    }

    /*  O uso do Optional<ClientDTO> ajuda a retornar um ClientDTO (se o ID existir no banco),
        🔹 Ou pode não retornar nada (se o ID não existir).
        É uma forma segura de evitar null e forçar quem chama o método a tratar o 
        caso de “cliente não encontrado”. */
    public Optional<ClientDTO> findClientById(Long id){
        return repository.findById(id)
                .map(ClientDTO::new);
    }

  /*   public Optional<ClientDTO> findClientById(Long id) {
        Optional<Client> optionalClient = repository.findById(id);

        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            List<ClientDTO> list = List.of(new ClientDTO(client)); // cria uma lista com um item

            for (ClientDTO dto : list) {
                return Optional.of(dto); // retorna o DTO dentro do Optional
            }
        }

        return Optional.empty(); // se não achou o client
    } */

    public Optional<ClientDTO>findClientByEmail(String email){
        return repository.findByEmail(email)
                .map(ClientDTO::new);
    }


    @Transactional
    public Optional<ClientDTO> updateClient(Long id, Client updatedClient){
        return repository.findById(id).map(client ->{
            if(!client.getEmail().equals(updatedClient.getEmail()) && repository.findByEmail(updatedClient.getEmail()).isPresent()){
                throw new IllegalArgumentException("O novo e-mail já está em uso por outro cliente: " + updatedClient.getEmail());
            }
            client.setName(updatedClient.getName());
            client.setEmail(updatedClient.getEmail());
            client.setTelephone(updatedClient.getTelephone());
            Client savedClient = repository.save(client);
            return new ClientDTO(savedClient);
        });
    }

    @Transactional
    public boolean desableClient(Long id){
        return repository.findById(id).map(client ->{
            if(client.isActive()){
                client.setActive(false);
                repository.save(client);
                return true;
            }
            return false;
        }).orElse(false);
    }

    @Transactional
    public boolean activeClient(Long id){
        return repository.findById(id).map(client->{
            if(!client.isActive()){
                client.setActive(true);
                repository.save(client);
                return true;
            }
            return false;
        }).orElse(false);
    }

    @Transactional
    public void initializeMockDataIfEmpty(){
        if(repository.count() == 0){
            createClient(new Client("Ana", "ana@gmail.com", "1111-1111", true));
            createClient(new Client("Pedro", "pedro@gmail.com", "2222-2222", false));
            createClient(new Client("Elaine", "elaine@gmail.com", "3333-3333", true));
        }else{
            System.out.println("Banco H2 já possui dados cadastrados.");
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
