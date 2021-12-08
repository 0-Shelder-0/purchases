package com.example.purchases;

import com.example.purchases.dbService.DBService;
import com.example.purchases.dbService.entities.User;
import com.example.purchases.exceptions.ValidationException;
import com.example.purchases.models.UserModel;
import com.example.purchases.services.AuthorizationService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class AuthorizationServiceTest {

    @Autowired
    private AuthorizationService _authorizationService;

    @MockBean
    private DBService _dbService;

    @Test
    void registerWhenUserAlreadyExistsTest() throws ValidationException {
        String login = "login";
        UserModel userModel = new UserModel(login, "");

        Mockito.doReturn(new User())
               .when(_dbService)
               .getUserByLogin(login);

        ValidationException exception = Assert.assertThrows(ValidationException.class,
                                                            () -> _authorizationService.register(userModel));
        Assertions.assertFalse(exception.getMessage().isEmpty());
    }

    @Test
    void isLoginTest() throws ValidationException {
        String sessionKey = "key";
        String login = "login";
        int userId = 1;
        UserModel userModel = new UserModel(login, "123");

        User user = new User(userModel);
        user.setId(userId);

        Mockito.doReturn(user)
               .when(_dbService)
               .getUserByLogin(login);

        _authorizationService.login(sessionKey, userModel);

        Assertions.assertTrue(_authorizationService.isLogin(sessionKey));
        Assertions.assertEquals(userId, _authorizationService.getUserId(sessionKey));
    }
}