package home_work_2.utils;

import home_work_2.models.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactoryUtil {
    private static SessionFactory sessionFactory = null;

    private HibernateSessionFactoryUtil(){}

    public static SessionFactory getSessionFactory() throws Exception {
        if(sessionFactory == null){
            try{
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(User.class);
                StandardServiceRegistryBuilder builder =
                        new StandardServiceRegistryBuilder()
                                .applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
            }
            catch (Exception exception) {
                Throwable rootCause = exception;
                while(rootCause.getCause() != null)
                    rootCause = rootCause.getCause();

                throw new Exception(rootCause.getMessage());
            }
        }
        return sessionFactory;
    }

    public static void shutdown() throws Exception {
        if(sessionFactory != null){
            getSessionFactory().close();
        }
    }

}
