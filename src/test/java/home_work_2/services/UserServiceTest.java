package home_work_2.services;

import home_work_2.dao.UserDao;
import home_work_2.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @Test
    void findUser() {
        Long userId = 777L;
        User expectedUser = new User("Kate", "kate_spb@gmail.com", 45).setId(userId);
        Mockito.when(userDao.findById(userId)).thenReturn(Optional.of(expectedUser));

        Optional<User> actualUser = userService.findUser(userId);

        assertTrue(actualUser.isPresent());
        assertEquals(expectedUser, actualUser.get());
        Mockito.verify(userDao).findById(userId);
    }

    @Test
    void saveUser() {
        User user = new User("Tom", "tom_cat@mail.ru", 33);
        User savedUser = new User("Tom", "tom_cat@mail.ru", 33).setId(1L);
        Mockito.when(userDao.save(user)).thenReturn(savedUser);

        User result = userService.saveUser(user);

        assertNotNull(result.getId());
        assertEquals("Tom", result.getName());
        Mockito.verify(userDao).save(user);
    }

    @Test
    void deleteUser() {
        User user = new User("Nike", "nile_ekb@mail.ru", 32).setId(1L);

        userService.deleteUser(user);

        Mockito.verify(userDao, Mockito.times(1)).delete(user);
    }

    @Test
    void updateUser() {
        User user = new User("Sam", "sam_ekb@mail.ru", 32).setId(1L);
        user.setName("Sammy");

        userService.updateUser(user);

        Mockito.verify(userDao, Mockito.times(1)).update(user);
    }

    @Test
    void findAllUsers() {
        List<User> users = List.of(
                new User("Tom", "tom_cat@mail.ru", 22).setId(1L),
                new User("Sam", "sam_cat@mail.ru", 43).setId(2L),
                new User("Jane", "jane_cat@mail.ru", 19).setId(3L)
        );
        Mockito.when(userDao.findAll()).thenReturn(users);

        List<User> result = userService.findAllUsers();

        assertEquals(3, result.size());
        Mockito.verify(userDao).findAll();
    }
}