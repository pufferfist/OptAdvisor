package utf8.citicup.restController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import utf8.citicup.restService.exception.ExceptionEntity;
import utf8.citicup.restService.exception.RestInvalidRequestException;

@RestControllerAdvice
public class RestExceptionController {
    @ExceptionHandler(RestInvalidRequestException.class)
    public ResponseEntity<ExceptionEntity> handleInvalidRequestException(RestInvalidRequestException e) {
        return new ResponseEntity<>(new ExceptionEntity(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
