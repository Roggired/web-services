package ru.itmo.yofik.webservices.back.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceConfiguration;
import org.hibernate.jpa.HibernatePersistenceProvider;
import ru.itmo.yofik.webservices.back.model.Student;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class ServerConfig {
    public static final String BASE_URL_KEY = "server.base-url";


    private Properties properties;

    public void init() {
        this.properties = new Properties();
        try(var input = ServerConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public EntityManager provideEntityManager() {
        var persistenceConfig = new PersistenceConfiguration("default");
        persistenceConfig.provider(HibernatePersistenceProvider.class.getName());
        persistenceConfig.managedClass(Student.class);;
        persistenceConfig.properties(
                properties.entrySet()
                        .stream()
                        .collect(Collectors.toMap(entry -> (String) entry.getKey(), Map.Entry::getValue))
        ); // just because of types incompatibility
        try(var entityManagerFactory = Persistence.createEntityManagerFactory(persistenceConfig)) {
            return entityManagerFactory.createEntityManager();
        }
    }
}
