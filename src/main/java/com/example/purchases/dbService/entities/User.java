package com.example.purchases.dbService.entities;

import com.example.purchases.models.UserModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int _id;

    @Column(name = "Login")
    private String _login;

    @Column(name = "Password")
    private String _password;

    @Column(name = "Email")
    private String _email;

    @OneToMany(mappedBy="_user", fetch = FetchType.LAZY)
    private List<Product> _products = new ArrayList<>();

    public User() {
    }

    public User(UserModel userModel) {
        _login = userModel.getLogin();
        _password = userModel.getPassword();
        _email = userModel.getEmail();
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public String getLogin() {
        return _login;
    }

    public void setLogin(String login) {
        _login = login;
    }

    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        _password = password;
    }

    public String getEmail() {
        return _email;
    }

    public void setEmail(String email) {
        _email = email;
    }

    public List<Product> getProducts() {
        return _products;
    }

    public void setProducts(List<Product> products) {
        _products = products;
    }
}
