package ru.itmo.yofik.webservices.back.api.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import lombok.RequiredArgsConstructor;
import ru.itmo.yofik.webservices.back.dao.StudentDao;
import ru.itmo.yofik.webservices.back.model.Student;

import java.util.List;

@RequiredArgsConstructor
@WebService(serviceName = "YofikWebService")
public class YofikWebService {
    private final StudentDao studentDao;


    @WebMethod(operationName = "search")
    public List<Student> search(SearchRequest request) {
        return studentDao.searchStudents(request);
    }

    @WebMethod(operationName = "create")
    public long create(CreateRequest request) {
        return studentDao.create(request);
    }

    @WebMethod(operationName = "update")
    public boolean update(UpdateRequest request) {
        return studentDao.update(request);
    }

    @WebMethod(operationName = "delete")
    public boolean delete(long id) {
        return studentDao.delete(id);
    }
}
