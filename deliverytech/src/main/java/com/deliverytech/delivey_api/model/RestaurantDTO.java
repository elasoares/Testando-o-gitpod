package com.deliverytech.delivey_api.model;

import com.deliverytech.delivey_api.validation.ValidTelephone;

import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

import jakarta.validation.constraints.Pattern;

public class RestaurantDTO {
    private Long id;
    private String name;
    private String address;
    @NotBlank(message = "O campo telefone é obrigatório.")
    @Pattern(regexp = "\\d{10,11}", message = "Phone number must be 10 or 11 digits")
    private String phoneNumber;
    private String category;
    private Double rating;
    private boolean active;

    public RestaurantDTO() {
    }

    public RestaurantDTO(Long id, String name, String address, String phoneNumber, String category, Double rating, boolean active) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.category = category;
        this.rating = rating;
        this.active = active;
    }

    public RestaurantDTO(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.address = restaurant.getAddress();
        this.category = restaurant.getCategory();
        this.phoneNumber = restaurant.getPhoneNumber();
        this.rating = restaurant.getRating();
        this.active = restaurant.isActive();
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "RestaurantDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", address='" + address + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", category='" + category + '\'' +
            ", rating=" + rating +
            ", active=" + active +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantDTO that = (RestaurantDTO) o;
        return active == that.active &&
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(address, that.address) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(category, that.category) &&
            Objects.equals(rating, that.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, phoneNumber, category, rating, active);
    }
}