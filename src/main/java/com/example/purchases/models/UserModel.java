package com.example.purchases.models;

import com.example.purchases.dbService.entities.User;

import java.util.Objects;

public class UserModel {
    private final String _login;
    private final String _password;
    private final String _email;

    public UserModel(String login, String password, String email) {
        _login = login;
        _password = password;
        _email = email;
    }

    public UserModel(String login, String password) {
        _login = login;
        _password = password;
        _email = null;
    }

    public UserModel(User user) {
        _login = user.getLogin();
        _password = user.getPassword();
        _email = user.getEmail();
    }

    public String getLogin() {
        return _login;
    }

    public String getPassword() {
        return _password;
    }

    public String getEmail() {
        return _email;
    }

    public boolean equalsLoginAndPassword(User user) {
        return Objects.equals(user.getLogin(), _login) && Objects.equals(user.getPassword(), _password);
    }
}
