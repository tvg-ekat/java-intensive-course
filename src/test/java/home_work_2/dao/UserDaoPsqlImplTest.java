package home_work_2.dao;

import home_work_2.models.User;
import home_work_2.utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserDaoPsqlImplTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");
    private static UserDao userDao;

    @BeforeAll
    static void setupHibernate() {
        Configuration configuration = new Configuration();

        configuration.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", postgres.getUsername());
        configuration.setProperty("hibernate.connection.password", postgres.getPassword());
        configuration.setProperty("hibernate.driver_class", "org.postgresql.Driver");
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        configuration.addAnnotatedClass(User.class);

        SessionFactory sessionFactory = configuration.buildSessionFactory();
        HibernateSessionFactoryUtil.setSessionFactory(sessionFactory);
    }

    @BeforeEach
    void init() {
        userDao = new UserDaoPsqlImpl();
    }

    @AfterEach
    void tearDown() {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery("TRUNCATE TABLE users RESTART IDENTITY", Void.class)
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            System.out.println("Ошибка при очистке базы: " + e.getMessage());
        }
    }

    static Stream<User> dataForeTests(){
        return Stream.of(
                new User("Mike", "mike_test@gmail.com", 27),
                new User("Kate", "kate_user@gmail.com", 33),
                new User("Tom", "tom_cat@cat.tom", 9)
        );
    }

    @ParameterizedTest
    @MethodSource("dataForeTests")
    void save(User user) {
        User savedUser;

        savedUser = userDao.save(user);

        assertNotNull(savedUser.getId());
    }

    @ParameterizedTest
    @MethodSource("dataForeTests")
    void findById(User user) {
        User savedUser = userDao.save(user);

        Optional<User> foundUser = userDao.findById(savedUser.getId());

        assertEquals(user, foundUser.orElseGet(User::new));
    }

    @ParameterizedTest
    @MethodSource("dataForeTests")
    void delete(User user) {
        User savedUser = userDao.save(user);

        userDao.delete(savedUser);
        Optional<User> foundUser = userDao.findById(savedUser.getId());

        assertTrue(foundUser.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("dataForeTests")
    void update(User user) {
        User savedUser = userDao.save(user);
        String testUpdateEmail = savedUser.getEmail() + "_test";
        user.setEmail(testUpdateEmail);

        userDao.update(user);

        assertEquals(user.getEmail(), testUpdateEmail);
    }

    @Test
    void findAll() {
        dataForeTests().forEach(userDao::save);

        List<User> listUsers = userDao.findAll();

        assertEquals(3, listUsers.size());
    }
}