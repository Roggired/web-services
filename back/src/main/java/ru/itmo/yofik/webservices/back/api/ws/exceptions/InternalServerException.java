package ru.itmo.yofik.webservices.back.api.ws.exceptions;

import jakarta.xml.ws.WebFault;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.itmo.yofik.webservices.back.api.ws.ErrorFaultBean;

@Data
@EqualsAndHashCode(callSuper = true)
@WebFault(faultBean = "ru.itmo.yofik.webservices.back.api.ws.ErrorFaultBean")
public class InternalServerException extends RuntimeException {
    private final ErrorFaultBean faultBean;

    public InternalServerException(String message, ErrorFaultBean faultBean) {
        super(message);
        this.faultBean = faultBean;
    }

    public InternalServerException(String message, ErrorFaultBean faultBean, Throwable cause) {
        super(message, cause);
        this.faultBean = faultBean;
    }

    public ErrorFaultBean getFaultInfo() {
        return faultBean;
    }
}
