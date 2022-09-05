package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private SessionFactory factory = null;

    public UserDaoHibernateImpl() {
        try {
            factory = Util.getSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    Методы создания и удаления таблицы пользователей в классе UserHibernateDaoImpl должны быть реализованы с
    помощью SQL.
     */
    @Override
    public void createUsersTable() {

        Transaction transaction = null;
        String sql = "CREATE TABLE IF NOT EXISTS Users ("
                + "id BIGINT NOT NULL AUTO_INCREMENT,"
                + "name VARCHAR(64) NULL,"
                + "lastName VARCHAR(64) NULL,"
                + "age TINYINT NULL,"
                + "PRIMARY KEY (`id`));";

        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery(sql).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void dropUsersTable() {

        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS Users;").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;

        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            //session.createQuery("delete from User u where u.id = :id")
            session.createQuery("delete from User where id = :id")
                    .setParameter("id", id)
                    .executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        try (Session session = factory.openSession()) {
            result = (List<User>) session.createQuery("From User").list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void cleanUsersTable() {
        EntityTransaction transaction = null;

        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("delete from User")
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public void cleanUsersTable2() {
        Transaction transaction = null;

        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            List<User> list = getAllUsers();
            for (User user: list) {
                session.remove(user); // Deleting an entity with JPA
            }
            transaction.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
}
