package ru.itmo.weblab3.repository;

import ru.itmo.weblab3.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
public class UserRepository {

    @PersistenceContext(unitName = "studentPU")
    private EntityManager em;

    public Optional<User> findByUsername(String username) {
        return em.createQuery("select u from User u where u.username = :u", User.class)
                .setParameter("u", username)
                .getResultStream().findFirst();
    }

    public Optional<User> findByEmail(String email) {
        return em.createQuery("select u from User u where u.email = :e", User.class)
                .setParameter("e", email)
                .getResultStream().findFirst();
    }

    public boolean existsByUsername(String username) {
        Long c = em.createQuery("select count(u) from User u where u.username = :u", Long.class)
                .setParameter("u", username)
                .getSingleResult();
        return c != null && c > 0;
    }

    public boolean existsByEmail(String email) {
        Long c = em.createQuery("select count(u) from User u where u.email = :e", Long.class)
                .setParameter("e", email)
                .getSingleResult();
        return c != null && c > 0;
    }

    @Transactional
    public User save(User user) {
        em.persist(user);
        em.flush(); // Гарантируем, что ID присвоится до возврата
        return user;
    }
}