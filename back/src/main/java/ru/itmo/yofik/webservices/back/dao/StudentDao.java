package ru.itmo.yofik.webservices.back.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.itmo.yofik.webservices.back.api.ws.SearchRequest;
import ru.itmo.yofik.webservices.back.model.Student;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class StudentDao {
    private final EntityManager entityManager;

    public List<Student> searchStudents(SearchRequest request) {
        log.info("Got searchStudents request: {}", request);
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
