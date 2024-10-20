package ru.itmo.yofik.webservices.back.api.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.itmo.yofik.webservices.back.dao.StudentDao;
import ru.itmo.yofik.webservices.back.model.Student;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@WebService(serviceName = "YofikWebService")
public class YofikWebService {
    private final StudentDao studentDao;


    @WebMethod(operationName = "search")
    public List<Student> search(SearchRequest request) {
        var result = studentDao.searchStudents(request);
        log.info(result.toString());
        return result;
    }
}
