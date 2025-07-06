package com.deliverytech.delivey_api.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    /* POST /clientes,  */
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

    /*  GET /clientes*/
    @GetMapping
    public List<ClientDTO> listClient(){
        return clientService.findAllClients();
    }

    /* , GET /clientes/{id}, */
    
     @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> findClientById(@PathVariable Long id){
        return clientService.findClientById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }
    
    /* PUT /clientes/{id}, */

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id, @RequestBody Client client){
        try{
            return clientService.updateClient(id, client)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }catch(IllegalArgumentException e){
            System.out.println("Error ao atualizar cliente: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
     /* PATCH /clientes/{id}, */
    @PatchMapping("/{id}/desable")
    public ResponseEntity<Void> desableClient(@PathVariable Long id ){
        boolean desalbe = clientService.desableClient(id);
        return desalbe ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

     /* PATCH /clientes/{id}, */
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void>activateClient(@PathVariable Long id){
        boolean activate = clientService.activeClient(id);
        return activate ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    
    /*  DELETE /clientes/{id}   */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id){
        boolean deleted = clientService.deleteClient(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
