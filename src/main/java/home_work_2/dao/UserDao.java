package home_work_2.dao;

import home_work_2.models.User;
import java.util.List;
import java.util.Optional;

/**
 * CRUD interface for User
 */
public interface UserDao {
    User save(User user);
    Optional<User> findById(Long id);
    List<User> findAll();
    void update(User user);
    void delete(User user);
}