package com.deliverytech.delivey_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivey_api.model.Client;
import com.deliverytech.delivey_api.model.ClientDTO;
import com.deliverytech.delivey_api.service.ClientService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequestMapping("/api/clients")
@RestController
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService){
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@RequestBody Client client){
       try{
            ClientDTO newClient = clientService.createClient(client);
            return ResponseEntity.status(HttpStatus.CREATED).body(newClient);
       }catch(IllegalArgumentException e){
            System.out.println("Error to create a new client");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
       }
    }

    @GetMapping
    public List<ClientDTO> listClient(){
        return clientService.findAllClients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> findClientById(@PathVariable Long id){
        return clientService.findClientById(id)
        .map(client -> new ResponseEntity<>(new ClientDTO(client), HttpStatus.OK))
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

@PutMapping("/{id}")
public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id, @RequestBody ClientDTO updatedDto) {
    Client clientToUpdate = new Client();
    clientToUpdate.setName(updatedDto.getName());
    clientToUpdate.setEmail(updatedDto.getEmail());
    clientToUpdate.setPhoneNumber(updatedDto.getPhoneNumber());

    return clientService.updateClient(id, clientToUpdate)
            .map(updatedClient -> new ResponseEntity<>(new ClientDTO(updatedClient), HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
}

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id){
        boolean deleted = clientService.deleteClient(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}