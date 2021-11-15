package com.example.purchases.dbService;

import com.example.purchases.dbService.dao.UsersDAO;
import com.example.purchases.dbService.entities.User;
import com.example.purchases.exceptions.DBException;
import com.example.purchases.models.UserModel;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.springframework.stereotype.Service;

@Service
public class DBService {

    private final SessionFactory sessionFactory;

    public DBService() {
        Configuration configuration = getMySqlConfiguration();
        sessionFactory = createSessionFactory(configuration);
    }

    private Configuration getMySqlConfiguration() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(User.class);
        String connectionString = "jdbc:mysql://localhost:3306/purchases";

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", connectionString);
        configuration.setProperty("hibernate.connection.username", "root");
        configuration.setProperty("hibernate.connection.password", "root");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");

        return configuration;
    }

    public UserModel getUserByLogin(String login) throws DBException {
        try {
            UserModel userModel = null;
            Session session = sessionFactory.openSession();

            UsersDAO dao = new UsersDAO(session);
            User user = dao.getUserByLogin(login);
            if (user != null) {
                userModel = new UserModel(user);
            }

            session.close();
            return userModel;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public void addUser(UserModel userModel) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            UsersDAO dao = new UsersDAO(session);
            User user = new User(userModel);
            dao.insertUser(user);

            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
}
