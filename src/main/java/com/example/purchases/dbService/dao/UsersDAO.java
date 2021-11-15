package com.example.purchases.dbService.dao;

import com.example.purchases.dbService.entities.User;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class UsersDAO {

    private final Session _session;

    public UsersDAO(Session session) {
        _session = session;
    }

    public User getUser(int id) throws HibernateException {
        return _session.get(User.class, id);
    }

    public User getUserByLogin(String login) throws HibernateException {
        Criteria criteria = _session.createCriteria(User.class);
        return (User) criteria.add(Restrictions.eq("_login", login)).uniqueResult();
    }

    public void insertUser(User user) throws HibernateException {
        _session.save(user);
    }
}
