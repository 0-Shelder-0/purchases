package com.example.purchases.services;

import com.example.purchases.dbService.DBService;
import com.example.purchases.exceptions.DBException;
import com.example.purchases.models.UserModel;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthorizationService {
    private final DBService _dbService;
    private final Map<String, UserModel> _userBySessionKeyMap = new HashMap<>();

    public AuthorizationService(DBService dbService) {
        _dbService = dbService;
    }

    public void register(UserModel user) throws Exception {
        UserModel existedUser = _dbService.getUserByLogin(user.getLogin());
        if (existedUser != null) {
            throw new Exception("This login already exists");
        }

        _dbService.addUser(user);
    }

    public boolean isLogin(String sessionKey) {
        return _userBySessionKeyMap.containsKey(sessionKey);
    }

    public void login(String sessionKey, UserModel user) throws DBException {
        if (!containsUser(user)) {
            throw new DBException("User not exists");
        }

        if (!_userBySessionKeyMap.containsKey(sessionKey)) {
            _userBySessionKeyMap.put(sessionKey, user);
        }
    }

    public String getLogin(String sessionKey) {
        return _userBySessionKeyMap.get(sessionKey).getLogin();
    }

    public void logout(String sessionKey) {
        _userBySessionKeyMap.remove(sessionKey);
    }

    private boolean containsUser(UserModel user) throws DBException {
        UserModel existedUser = _dbService.getUserByLogin(user.getLogin());
        return existedUser != null && existedUser.equals(user);
    }
}
