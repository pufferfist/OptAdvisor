package utf8.citicup.restController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import utf8.citicup.restService.exception.ExceptionEntity;
import utf8.citicup.restService.exception.RestInvalidRequestException;

@RestControllerAdvice
public class RestExceptionController {
    private static final Logger logger = LoggerFactory.getLogger(RestExceptionController.class);

    @ExceptionHandler(RestInvalidRequestException.class)
    public ResponseEntity<ExceptionEntity> handleInvalidRequestException(RestInvalidRequestException e) {
        return new ResponseEntity<>(new ExceptionEntity(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
