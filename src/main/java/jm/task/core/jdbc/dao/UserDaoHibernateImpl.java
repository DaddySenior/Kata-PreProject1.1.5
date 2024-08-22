package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private static Transaction transaction;


    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        try (Session session = Util.getConnectSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            session.createSQLQuery("" +
                    "CREATE TABLE IF NOT EXISTS users(" +
                    "id bigint NOT NULL AUTO_INCREMENT, " +
                    "name varchar(45) NOT NULL, " +
                    "lastName varchar(45) NOT NULL, " +
                    "age tinyint NOT NULL, " +
                    "PRIMARY KEY (id)) " +
                    "ENGINE=InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8mb3").executeUpdate();
            session.getTransaction().commit();
        }
    }


    @Override
    public void dropUsersTable() {
        try (Session session = Util.getConnectSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS users")
                    .executeUpdate();
            session.getTransaction().commit();
        }
    }


    public void saveUser(String name, String lastName, byte age) {
        try (Session session = Util.getConnectSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            session.save(new User(name, lastName, age));
            session.getTransaction().commit();
            System.out.printf("User с именем – %s добавлен в базу данных%n", name);
        } catch (HibernateException e) {
            transaction.rollback();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getConnectSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            User user = session.get(User.class, id);
            session.delete(user);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            transaction.rollback();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = null;
        try (Session session = Util.getConnectSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            userList = session.createQuery("from User").getResultList();
            session.getTransaction().commit();
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getConnectSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            session.createSQLQuery("TRUNCATE TABLE users")
                    .executeUpdate();
            session.getTransaction().commit();
        }
    }
}


