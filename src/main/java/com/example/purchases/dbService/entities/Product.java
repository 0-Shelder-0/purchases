package com.example.purchases.dbService.entities;

import com.example.purchases.models.ProductModel;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Products")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int _id;

    @Column(name = "Description")
    private String _description;

    @Column(name = "IsActive", nullable = false)
    private boolean _isActive;

    @ManyToOne
    @JoinColumn(name = "UserId", referencedColumnName = "Id")
    private User _user;

    public Product() {
    }

    public Product(String description, User user) {
        _description = description;
        _user = user;
        _isActive = true;
    }

    public Product(ProductModel productModel, User user) {
        _description = productModel.getDescription();
        _user = user;
        _isActive = true;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        _description = description;
    }

    public boolean isActive() {
        return _isActive;
    }

    public void setActive(boolean active) {
        _isActive = active;
    }

    public User getUser() {
        return _user;
    }

    public void setUser(User user) {
        _user = user;
    }
}
