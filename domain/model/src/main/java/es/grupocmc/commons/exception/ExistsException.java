package es.grupocmc.commons.exception;

public class ExistsException extends RuntimeException {
    public ExistsException(String detail) {
        super(detail);
    }
}
