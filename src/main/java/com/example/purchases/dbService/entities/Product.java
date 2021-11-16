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

    @ManyToOne
    @JoinColumn(name = "UserId", referencedColumnName = "Id")
    private User _user;

    public Product() {
    }

    public Product(ProductModel productModel, User user) {
        _description = productModel.getDescription();
        _user = user;
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

    public User getUser() {
        return _user;
    }

    public void setUser(User user) {
        _user = user;
    }
}
