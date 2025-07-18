package com.deliverytech.delivey_api.model;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private boolean available;
    private Long restaurantId;

    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, String description, String category, BigDecimal price, boolean available, Long restaurantId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.available = available;
        this.restaurantId = restaurantId;
    }

    public ProductDTO(Product p) {
        this.id = p.getId(); // Assuming Product has an ID to map
        this.name = p.getName();
        this.description = p.getDescription();
        this.category = p.getCategory();
        this.price = p.getPrice();
        this.available = p.isAvailable();
        if(p.getRestaurant() != null){
            this.restaurantId = p.getRestaurant().getId();
        }
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", category='" + category + '\'' +
            ", price=" + price +
            ", available=" + available +
            ", restaurantId=" + restaurantId +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDTO that = (ProductDTO) o;
        return available == that.available &&
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(category, that.category) &&
            Objects.equals(price, that.price) &&
            Objects.equals(restaurantId, that.restaurantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, category, price, available, restaurantId);
    }
}