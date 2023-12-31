package kr.apartribebackend.global.exception;


public abstract class RootException extends RuntimeException {

    public RootException(String message) {
        super(message);
    }

    abstract public int getStatusCode();

}
