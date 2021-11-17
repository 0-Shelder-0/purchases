package com.example.purchases.services;

import com.example.purchases.dbService.DBService;
import com.example.purchases.dbService.entities.User;
import com.example.purchases.exceptions.ValidationException;
import com.example.purchases.models.UserModel;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthorizationService {
    private final DBService _dbService;
    private final Map<String, Integer> _userLoginBySessionKeyMap = new HashMap<>();

    public AuthorizationService(DBService dbService) {
        _dbService = dbService;
    }

    public void register(UserModel user) throws ValidationException {
        User existedUser = _dbService.getUserByLogin(user.getLogin());
        if (existedUser != null) {
            throw new ValidationException("This login already exists");
        }

        _dbService.addUser(user);
    }

    public boolean isLogin(String sessionKey) {
        return _userLoginBySessionKeyMap.containsKey(sessionKey);
    }

    public void login(String sessionKey, UserModel userModel) throws ValidationException {
        User user = _dbService.getUserByLogin(userModel.getLogin());

        if (user == null || !userModel.equalsLoginAndPassword(user)) {
            throw new ValidationException("User not exists");
        }

        if (!_userLoginBySessionKeyMap.containsKey(sessionKey)) {
            _userLoginBySessionKeyMap.put(sessionKey, user.getId());
        }
    }

    public int getUserId(String sessionKey) {
        return _userLoginBySessionKeyMap.get(sessionKey);
    }

    public void logout(String sessionKey) {
        _userLoginBySessionKeyMap.remove(sessionKey);
    }
}
