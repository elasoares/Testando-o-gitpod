package com.deliverytech.delivey_api.model;

import java.util.Objects;

public class ClientDTO {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private boolean active;

    public ClientDTO() {
    }

    public ClientDTO(Long id, String name, String email, String phoneNumber, boolean active) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.active = active;
    }

    public ClientDTO(Client c) {
        this.id = c.getId();
        this.name = c.getName();
        this.email = c.getEmail();
        this.phoneNumber = c.getPhoneNumber();
        this.active = c.isActive();
    }

    public ClientDTO(String name, String email, String phoneNumber, boolean active) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "ClientDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", active=" + active +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientDTO clientDTO = (ClientDTO) o;
        return active == clientDTO.active &&
            Objects.equals(id, clientDTO.id) &&
            Objects.equals(name, clientDTO.name) &&
            Objects.equals(email, clientDTO.email) &&
            Objects.equals(phoneNumber, clientDTO.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, phoneNumber, active);
    }
}