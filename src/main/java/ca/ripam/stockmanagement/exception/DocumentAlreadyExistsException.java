package ca.ripam.stockmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class DocumentAlreadyExistsException extends RuntimeException {
    private String message;

    public DocumentAlreadyExistsException(String message) {
        super(message);
        this.message = message;
    }
}
