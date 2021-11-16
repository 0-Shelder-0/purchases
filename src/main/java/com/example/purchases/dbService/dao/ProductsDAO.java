package com.example.purchases.dbService.dao;

import com.example.purchases.dbService.entities.Product;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class ProductsDAO {

    private final Session _session;

    public ProductsDAO(Session session) {
        _session = session;
    }

    public Product[] getProductsByUser(int userId) throws HibernateException {
        Criteria criteria = _session.createCriteria(Product.class);
        List<Product> products = criteria.add(Restrictions.eq("_userId", userId)).list();
        return products.toArray(Product[]::new);
    }

    public void insertProduct(Product product) throws HibernateException {
        _session.save(product);
    }
}
