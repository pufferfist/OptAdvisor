package utf8.citicup.restService.exception;

public class ExceptionEntity {
    private String error;

    public ExceptionEntity() {
    }

    public ExceptionEntity(String err) {
        this.error = err;
    }

    public String getError() {
        return error;
    }
}
