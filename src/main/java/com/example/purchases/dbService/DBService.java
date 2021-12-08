package com.example.purchases.dbService;

import com.example.purchases.dbService.dao.ProductsDAO;
import com.example.purchases.dbService.dao.UsersDAO;
import com.example.purchases.dbService.entities.Product;
import com.example.purchases.dbService.entities.User;
import com.example.purchases.exceptions.ValidationException;
import com.example.purchases.models.ProductModel;
import com.example.purchases.models.UserModel;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class DBService {

    private final SessionFactory _sessionFactory;
    private final Environment _environment;

    public DBService(Environment environment) {
        _environment = environment;
        Configuration configuration = getMySqlConfiguration();
        _sessionFactory = createSessionFactory(configuration);
    }

    private Configuration getMySqlConfiguration() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(User.class);

        configuration.setProperty("hibernate.connection.url", _environment.getProperty("spring.datasource.url"));
        configuration.setProperty("hibernate.dialect", _environment.getProperty("spring.jpa.database-platform"));
        configuration.setProperty("hibernate.connection.driver_class", _environment.getProperty("spring.datasource.driver-class-name"));
        configuration.setProperty("hibernate.connection.username", _environment.getProperty("spring.datasource.username"));
        configuration.setProperty("hibernate.connection.password", _environment.getProperty("spring.datasource.password"));
        configuration.setProperty("hibernate.hbm2ddl.auto", _environment.getProperty("spring.jpa.hibernate.ddl-auto"));

        return configuration;
    }

    public User getUserByLogin(String login) throws ValidationException {
        try {
            Session session = _sessionFactory.openSession();

            UsersDAO dao = new UsersDAO(session);
            User user = dao.getUserByLogin(login);

            session.close();
            return user;
        } catch (HibernateException e) {
            throw new ValidationException(e);
        }
    }

    public UserModel getUserModelByLogin(String login) throws ValidationException {
        UserModel userModel = null;
        User user = getUserByLogin(login);
        if (user != null) {
            userModel = new UserModel(user);
        }
        return userModel;
    }

    public User getUser(int userId) throws ValidationException {
        try {
            Session session = _sessionFactory.openSession();

            UsersDAO dao = new UsersDAO(session);
            User user = dao.getUser(userId);

            session.close();
            return user;
        } catch (HibernateException e) {
            throw new ValidationException(e);
        }
    }

    public void addUser(UserModel userModel) throws ValidationException {
        try {
            Session session = _sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            UsersDAO dao = new UsersDAO(session);
            User user = new User(userModel);
            dao.insertUser(user);

            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            throw new ValidationException(e);
        }
    }

    public ProductModel[] getProductsByUser(int userId) throws ValidationException {
        try {
            ProductModel[] productModels = null;
            Session session = _sessionFactory.openSession();

            ProductsDAO dao = new ProductsDAO(session);
            Product[] products = dao.getProductsByUser(userId);
            if (products != null) {
                productModels = Arrays.stream(products)
                                      .map(ProductModel::new)
                                      .toArray(ProductModel[]::new);
            }

            session.close();
            return productModels;
        } catch (HibernateException e) {
            throw new ValidationException(e);
        }
    }

    public void addProduct(Product product) throws ValidationException {
        try {
            Session session = _sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            ProductsDAO dao = new ProductsDAO(session);
            dao.insertProduct(product);

            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            throw new ValidationException(e);
        }
    }

    public void updateProduct(int userId, ProductModel productModel) throws ValidationException {
        try {
            Session session = _sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            ProductsDAO dao = new ProductsDAO(session);
            Product product = dao.getProduct(productModel.getProductId(), userId);
            if (product == null) {
                throw new ValidationException("Product not found");
            }

            product.setDescription(productModel.getDescription());
            product.setCompleted(productModel.isCompleted());

            dao.updateProduct(product);

            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            throw new ValidationException(e);
        }
    }

    public void deleteProduct(int userId, int productId) throws ValidationException {
        try {
            Session session = _sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            ProductsDAO dao = new ProductsDAO(session);
            dao.deleteProduct(productId, userId);

            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            throw new ValidationException(e);
        }
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Product.class)
                .buildSessionFactory(serviceRegistry);
    }
}
