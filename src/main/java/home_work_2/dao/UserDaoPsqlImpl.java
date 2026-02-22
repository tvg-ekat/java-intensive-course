package home_work_2.dao;

import home_work_2.models.User;
import home_work_2.utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class UserDaoPsqlImpl implements UserDao{

    private static final Logger logger = LoggerFactory.getLogger(UserDaoPsqlImpl.class);
    private static final Function<Throwable, String> rootCause = ex -> {
        while(ex.getCause() != null) ex = ex.getCause();
        return ex.getMessage();
    };

    @Override
    public User save(User user) {
        user.setCreated_at(LocalDateTime.now());
        Transaction transaction = null;
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            logger.info("Начало операции создания пользователя {}", user.getName());
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            logger.info("Пользователь {} успешно создан", user.getName());
            return user;
        } catch (Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            var msg = rootCause.apply(e);
            logger.error("Ошибка операции создания пользователя {}: {}", user.getName(), msg);
            throw new RuntimeException(msg);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            logger.info("Запрос на получение пользователя id = {}", id);
            return Optional.ofNullable(session.find(User.class, id));
        } catch (Exception e) {
            var msg = rootCause.apply(e);
            logger.error("Ошибка запроса получить пользователя по id = {}: {}", id, msg);
            throw new RuntimeException(msg);
        }
    }

    @Override
    public void delete(User user) {
        Transaction transaction = null;
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            logger.info("Запрос на удаление пользователя id = {}", user.getId());
            transaction = session.beginTransaction();
            session.remove(user);
            transaction.commit();
            logger.info("Удаление пользователя id = {} выполнено успешно", user.getId());
        } catch (Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            var msg = rootCause.apply(e);
            logger.error("Ошибка удаления пользователя с id = {}: {}", user.getId(), msg);
            throw new RuntimeException(msg);
        }
    }

    @Override
    public void update(User user) {
        Transaction transaction = null;
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            logger.info("Запрос на обновление пользователя id = {}", user.getId());
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
            logger.info("Обновление пользователя выполнено успешно id = {}", user.getId());
        } catch (Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            var msg = rootCause.apply(e);
            logger.error("Ошибка обновления пользователя с id = {}: {}", user.getId(), msg);
            throw new RuntimeException(msg);
        }
    }

    @Override
    public List<User> findAll() {
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            logger.info("Запрос всех пользователей списком");
            return session.createSelectionQuery("from User", User.class).getResultList();
        } catch(Exception e){
            var msg = rootCause.apply(e);
            logger.error("Ошибка при запросе пользователей: {}", msg);
            throw new RuntimeException(msg);
        }
    }
}
