package ru.itmo.yofik.webservices.back.api.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.itmo.yofik.webservices.back.api.ws.exceptions.InvalidDataException;
import ru.itmo.yofik.webservices.back.api.ws.exceptions.NotFoundException;
import ru.itmo.yofik.webservices.back.dao.StudentDao;
import ru.itmo.yofik.webservices.back.model.Student;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@WebService(serviceName = "YofikWebService")
public class YofikWebService {
    private final StudentDao studentDao;


    @WebMethod(operationName = "search")
    public List<Student> search(SearchRequest request) throws InvalidDataException {
        log.info("Got SearchStudents request: {}", request);
        var result = studentDao.searchStudents(request);
        log.info("Students found: {}", result);
        return result;
    }

    @WebMethod(operationName = "create")
    public long create(CreateRequest request) throws InvalidDataException {
        log.info("Got CreateRequest: {}", request);
        var result = studentDao.create(request);
        log.info("Created student id: {}", result);
        return result;
    }

    @WebMethod(operationName = "update")
    public boolean update(UpdateRequest request) throws InvalidDataException, NotFoundException {
        log.info("Got UpdateRequest: {}", request);
        var result = studentDao.update(request);
        log.info("Update status: {}", result);
        return result;
    }

    @WebMethod(operationName = "delete")
    public boolean delete(long id) throws NotFoundException {
        log.info("Got DeleteRequest: {}", id);
        var result = studentDao.delete(id);
        log.info("Delete status: {}", result);
        return result;
    }
}
