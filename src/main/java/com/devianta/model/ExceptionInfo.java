package com.devianta.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionInfo {
    private Date timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;

    public static ExceptionInfo getNew(HttpServletRequest request, HttpStatus status, Exception ex) {
        return new ExceptionInfo(new Date(), status.value(), status.getReasonPhrase()
                , ex.getMessage(), request.getRequestURI());
    }
}
