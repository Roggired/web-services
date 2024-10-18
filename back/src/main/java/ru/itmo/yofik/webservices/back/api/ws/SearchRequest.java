package ru.itmo.yofik.webservices.back.api.ws;

import lombok.Data;

@Data
public class SearchRequest {
    private Filters filters;

    @Data
    public static class Filters {
        private String byFirstName;
        private String byLastName;
        private String byPatronymic;
        private Integer byAgeMin;
        private Integer byAgeMax;
        private Integer byHeightMin;
        private Integer byHeightMax;
    }
}
