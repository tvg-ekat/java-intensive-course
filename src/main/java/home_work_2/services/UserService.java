package home_work_2.services;

import home_work_2.dao.UserDao;
import home_work_2.dao.UserDaoPsqlImpl;
import home_work_2.models.User;
import java.util.List;

import java.util.Optional;

public class UserService {

    private final UserDao userDao = new UserDaoPsqlImpl();

    public Optional<User> findUser(long id){
        return userDao.findById(id);
    }

    public void saveUser(User user){
        userDao.save(user);
    }

    public void deleteUser(User user){
        userDao.delete(user);
    }

    public void updateUser(User user){
        userDao.update(user);
    }

    public List<User> findAllUsers(){
        return userDao.findAll();
    }
}
