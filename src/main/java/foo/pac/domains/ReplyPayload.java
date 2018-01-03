package foo.pac.domains;

import java.util.Date;

public class ReplyPayload {

    public ReplyPayload(Object payload) {
        this.payload = payload;
        this.timestamp = new Date().toString();
    }

    private Object payload;

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
    
    
//    private String payload;
//
//    public String getPayload() {
//        return payload;
//    }
//
//    public void setPayload(String payload) {
//        this.payload = payload;
//    }

    private String timestamp;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
