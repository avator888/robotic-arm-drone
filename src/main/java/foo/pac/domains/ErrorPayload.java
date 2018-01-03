package foo.pac.domains;

import java.util.Date;

public class ErrorPayload {

    public ErrorPayload(String message) {
        this.message = message;
        this.timestamp = new Date().toString();
    }

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String timestamp;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
