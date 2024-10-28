package ru.itmo.yofik.webservices.back.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.yofik.webservices.back.api.ws.*;
import ru.itmo.yofik.webservices.back.api.ws.exceptions.InternalServerException;
import ru.itmo.yofik.webservices.back.api.ws.exceptions.InvalidDataException;
import ru.itmo.yofik.webservices.back.api.ws.exceptions.NotFoundException;
import ru.itmo.yofik.webservices.back.model.Avatar;
import ru.itmo.yofik.webservices.back.model.Student;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class StudentDao {
    private static final Logger log = LoggerFactory.getLogger(StudentDao.class);
    private final EntityManager entityManager;

    public List<Student> searchStudents(SearchRequest request) throws InvalidDataException {
        final var filters = request.getFilters();
        if (filters.getByAgeMin() != null && filters.getByAgeMax() != null && filters.getByAgeMin() > filters.getByAgeMax()) {
            var faultBean = new ErrorFaultBean();
            faultBean.setMessage("ageMin should be not greater than ageMax");
            throw new InvalidDataException("Invalid data have provided", faultBean);
        }
        if (filters.getByHeightMin() != null && filters.getByHeightMax() != null && filters.getByHeightMin() > filters.getByHeightMax()) {
            var faultBean = new ErrorFaultBean();
            faultBean.setMessage("heightMin should be not greater than heightMax");
            throw new InvalidDataException("Invalid data have provided", faultBean);
        }

        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(Student.class);
        var root = query.from(Student.class);

        query.select(root).where(
                cb.and(
                        Stream.of(
                                createFirstNameSearchPredicate(cb, root, request),
                                createLastNameSearchPredicate(cb, root, request),
                                createPatronymicSearchPredicate(cb, root, request),
                                createAgeSearchPredicate(cb, root, request),
                                createHeightSearchPredicate(cb, root, request)
                        ).filter(Objects::nonNull).toList()
                )
        ).orderBy(cb.asc(root.get(Student.Fields.id)));

        return entityManager.createQuery(query).getResultList();
    }

    public long create(CreateRequest request) throws InvalidDataException {
        if (request.getFirstName() == null || request.getFirstName().isBlank()) {
            var faultBean = new ErrorFaultBean();
            faultBean.setMessage("firstName is mandatory");
            throw new InvalidDataException("Invalid data have provided", faultBean);
        }
        if (request.getLastName() == null || request.getLastName().isBlank()) {
            var faultBean = new ErrorFaultBean();
            faultBean.setMessage("lastName is mandatory");
            throw new InvalidDataException("Invalid data have provided", faultBean);
        }
        if (request.getPatronymic() == null || request.getPatronymic().isBlank()) {
            var faultBean = new ErrorFaultBean();
            faultBean.setMessage("patronymic is mandatory");
            throw new InvalidDataException("Invalid data have provided", faultBean);
        }
        if (request.getAge() <= 0) {
            var faultBean = new ErrorFaultBean();
            faultBean.setMessage("age must be positive");
            throw new InvalidDataException("Invalid data have provided", faultBean);
        }
        if (request.getHeight() <= 0) {
            var faultBean = new ErrorFaultBean();
            faultBean.setMessage("height must be positive");
            throw new InvalidDataException("Invalid data have provided", faultBean);
        }

        var student = new Student();
        student.setId(0L);
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setPatronymic(request.getPatronymic());
        student.setAge(request.getAge());
        student.setHeight(request.getHeight());
        entityManager.getTransaction().begin();
        entityManager.persist(student);
        entityManager.getTransaction().commit();
        return student.getId();
    }

    public boolean update(UpdateRequest request) throws InvalidDataException, NotFoundException {
        if (request.getFirstName() == null || request.getFirstName().isBlank()) {
            var faultBean = new ErrorFaultBean();
            faultBean.setMessage("firstName is mandatory");
            throw new InvalidDataException("Invalid data have provided", faultBean);
        }
        if (request.getLastName() == null || request.getLastName().isBlank()) {
            var faultBean = new ErrorFaultBean();
            faultBean.setMessage("lastName is mandatory");
            throw new InvalidDataException("Invalid data have provided", faultBean);
        }
        if (request.getPatronymic() == null || request.getPatronymic().isBlank()) {
            var faultBean = new ErrorFaultBean();
            faultBean.setMessage("patronymic is mandatory");
            throw new InvalidDataException("Invalid data have provided", faultBean);
        }
        if (request.getAge() <= 0) {
            var faultBean = new ErrorFaultBean();
            faultBean.setMessage("age must be positive");
            throw new InvalidDataException("Invalid data have provided", faultBean);
        }
        if (request.getHeight() <= 0) {
            var faultBean = new ErrorFaultBean();
            faultBean.setMessage("height must be positive");
            throw new InvalidDataException("Invalid data have provided", faultBean);
        }

        entityManager.getTransaction().begin();
        var student = getById(request.getId());
        if (student == null) {
            log.error("Student with id: {} not found", request.getId());
            var faultBean = new ErrorFaultBean();
            faultBean.setMessage("Requested student not found");
            throw new NotFoundException("Not found", faultBean);
        }
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setPatronymic(request.getPatronymic());
        student.setAge(request.getAge());
        student.setHeight(request.getHeight());

        try {
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            log.error("Unexpected error", e);
            var faultBean = new ErrorFaultBean();
            faultBean.setMessage(e.getClass().getName());
            entityManager.getTransaction().rollback();
            throw new InternalServerException("Unexpected error", faultBean);
        }
    }

    public boolean delete(long id) throws NotFoundException {
        entityManager.getTransaction().begin();
        var student = getById(id);
        if (student == null) {
            log.error("Student with id: {} not found", id);
            var faultBean = new ErrorFaultBean();
            faultBean.setMessage("Requested student not found");
            entityManager.getTransaction().rollback();
            throw new NotFoundException("Not found", faultBean);
        }
        try {
            entityManager.remove(student);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            log.error("Unexpected error", e);
            var faultBean = new ErrorFaultBean();
            faultBean.setMessage(e.getClass().getName());
            entityManager.getTransaction().rollback();
            throw new InternalServerException("Unexpected error", faultBean);
        }
    }

    public boolean setAvatar(SetAvatarRequest request) throws NotFoundException {
        entityManager.getTransaction().begin();
        var student = getById(request.getStudentId());
        if (student == null) {
            log.error("Student with id: {} not found", request.getStudentId());
            var faultBean = new ErrorFaultBean();
            faultBean.setMessage("Requested student not found");
            entityManager.getTransaction().rollback();
            throw new NotFoundException("Not found", faultBean);
        }

        var avatar = entityManager.createQuery("SELECT a FROM Avatar a WHERE a.studentId = " + request.getStudentId(), Avatar.class).getSingleResultOrNull();
        if (avatar == null) {
            avatar = new Avatar();
            avatar.setStudentId(request.getStudentId());
            avatar.setContent(request.getContent());

            try {
                entityManager.persist(avatar);
                entityManager.getTransaction().commit();
                return true;
            }  catch (Exception e) {
                log.error("Unexpected error", e);
                var faultBean = new ErrorFaultBean();
                faultBean.setMessage(e.getClass().getName());
                entityManager.getTransaction().rollback();
                throw new InternalServerException("Unexpected error", faultBean);
            }
        }

        try {
            avatar.setContent(request.getContent());
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            log.error("Unexpected error", e);
            var faultBean = new ErrorFaultBean();
            faultBean.setMessage(e.getClass().getName());
            entityManager.getTransaction().rollback();
            throw new InternalServerException("Unexpected error", faultBean);
        }
    }

    public String getAvatar(long id) throws NotFoundException {
        entityManager.getTransaction().begin();
        var avatar = entityManager.createQuery("SELECT a FROM Avatar a WHERE a.studentId = " + id, Avatar.class).getSingleResultOrNull();
        if (avatar == null) {
            log.error("Avatar for student id: {} not found", id);
            var faultBean = new ErrorFaultBean();
            faultBean.setMessage("Requested avatar not found");
            entityManager.getTransaction().rollback();
            throw new NotFoundException("Not found", faultBean);
        }

        var result = avatar.getContent();
        entityManager.getTransaction().commit();

        return result;
    }

    private Student getById(long id) {
        return entityManager.createQuery("SELECT s FROM Student s WHERE s.id = " + id, Student.class).getSingleResultOrNull();
    }

    private Predicate createFirstNameSearchPredicate(CriteriaBuilder cb, Root<Student> root, SearchRequest request) {
        if (request.getFilters() == null || request.getFilters().getByFirstName() == null || request.getFilters().getByFirstName().isBlank()) {
            return null;
        }
        return cb.like(root.get(Student.Fields.firstName), "%" + request.getFilters().getByFirstName() + "%");
    }
    private Predicate createLastNameSearchPredicate(CriteriaBuilder cb, Root<Student> root, SearchRequest request) {
        if (request.getFilters() == null || request.getFilters().getByLastName() == null || request.getFilters().getByLastName().isBlank()) {
            return null;
        }
        return cb.like(root.get(Student.Fields.lastName), "%" + request.getFilters().getByLastName() + "%");
    }
    private Predicate createPatronymicSearchPredicate(CriteriaBuilder cb, Root<Student> root, SearchRequest request) {
        if (request.getFilters() == null || request.getFilters().getByPatronymic() == null || request.getFilters().getByPatronymic().isBlank()) {
            return null;
        }
        return cb.like(root.get(Student.Fields.patronymic), "%" + request.getFilters().getByPatronymic() + "%");
    }
    private Predicate createAgeSearchPredicate(CriteriaBuilder cb, Root<Student> root, SearchRequest request) {
        if (request.getFilters() == null || (request.getFilters().getByAgeMin() == null && request.getFilters().getByAgeMax() == null)) {
            return null;
        }
        if (request.getFilters().getByAgeMin() == null) {
            return cb.lessThanOrEqualTo(root.get(Student.Fields.age), request.getFilters().getByAgeMax());
        } if (request.getFilters().getByAgeMax() == null) {
            return cb.greaterThanOrEqualTo(root.get(Student.Fields.age), request.getFilters().getByAgeMin());
        } else {
            return cb.between(root.get(Student.Fields.age), request.getFilters().getByAgeMin(), request.getFilters().getByAgeMax());
        }
    }
    private Predicate createHeightSearchPredicate(CriteriaBuilder cb, Root<Student> root, SearchRequest request) {
        if (request.getFilters() == null || (request.getFilters().getByHeightMin() == null && request.getFilters().getByHeightMax() == null)) {
            return null;
        }
        if (request.getFilters().getByHeightMin() == null) {
            return cb.lessThanOrEqualTo(root.get(Student.Fields.height), request.getFilters().getByHeightMax());
        } if (request.getFilters().getByHeightMax() == null) {
            return cb.greaterThanOrEqualTo(root.get(Student.Fields.height), request.getFilters().getByHeightMin());
        } else {
            return cb.between(root.get(Student.Fields.height), request.getFilters().getByHeightMin(), request.getFilters().getByHeightMax());
        }
    }
}
