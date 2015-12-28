package invertedindex;

public class StopWordException extends RuntimeException {

    public StopWordException() {
        super();
    }

    public StopWordException(final String message) {
        super(message);
    }

    public StopWordException(final Throwable throwable) {
        super(throwable);
    }


    public StopWordException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
