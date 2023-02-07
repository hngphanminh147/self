package com.self.uaa.model.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.util.List;

@Getter
@Setter
public class ApiError extends AbstractThrowableProblem {
    private String detail;
    private HttpStatus httpStatus;
    private String title;
    private List<String> errors;

    public ApiError(Integer status, String title, String message) {
        super(null, title, Status.valueOf(status), message);
        this.httpStatus = HttpStatus.valueOf(status);
        this.title = title;
    }
}
