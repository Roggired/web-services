package ru.itmo.yofik.webservices.back;

import jakarta.xml.ws.Endpoint;
import ru.itmo.yofik.webservices.back.api.ws.YofikWebService;
import ru.itmo.yofik.webservices.back.config.ServerConfig;
import ru.itmo.yofik.webservices.back.dao.StudentDao;

public class App {
    public static void main(String[] args) {
        var serverConfig = new ServerConfig();
        serverConfig.init();

        var entityManager = serverConfig.provideEntityManager();
        var studentDao = new StudentDao(entityManager);
        var yofikWebService = new YofikWebService(studentDao);

        Endpoint.publish(serverConfig.get(ServerConfig.BASE_URL_KEY) + "/YofikWebService", yofikWebService);
    }
}
