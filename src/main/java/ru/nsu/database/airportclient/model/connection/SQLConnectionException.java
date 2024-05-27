package ru.nsu.database.airportclient.model.connection;

public class SQLConnectionException extends RuntimeException{

    public SQLConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SQLConnectionException(String message) {
        super(message);
    }
}
