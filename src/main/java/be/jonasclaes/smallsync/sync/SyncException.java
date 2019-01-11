package be.jonasclaes.smallsync.sync;

public class SyncException extends Throwable {
    private String message;

    public SyncException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
