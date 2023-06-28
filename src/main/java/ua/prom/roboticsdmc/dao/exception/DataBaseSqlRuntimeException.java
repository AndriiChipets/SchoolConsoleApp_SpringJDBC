package ua.prom.roboticsdmc.dao.exception;

public class DataBaseSqlRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DataBaseSqlRuntimeException() {
    }

    public DataBaseSqlRuntimeException(String message) {
        super(message);
    }

    public DataBaseSqlRuntimeException(String message, Exception cause) {
        super(message, cause);
    }
}
