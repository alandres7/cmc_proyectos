package es.grupocmc.commons.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String detail) {
        super(detail);
    }
}
