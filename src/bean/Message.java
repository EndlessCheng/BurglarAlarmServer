package bean;

public class Message {

    private String messageType;
    private String fromPhoneNumber;
    private String toPhoneNumber;
    private Object messageData;

    public Message() {
    }

    public Message(String messageType, String fromPhoneNumber, String toPhoneNumber) {
        this.messageType = messageType;
        this.fromPhoneNumber = fromPhoneNumber;
        this.toPhoneNumber = toPhoneNumber;
        messageData = null;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getFromPhoneNumber() {
        return fromPhoneNumber;
    }

    public void setFromPhoneNumber(String fromPhoneNumber) {
        this.fromPhoneNumber = fromPhoneNumber;
    }

    public String getToPhoneNumber() {
        return toPhoneNumber;
    }

    public void setToPhoneNumber(String toPhoneNumber) {
        this.toPhoneNumber = toPhoneNumber;
    }

    public String getMessageHead() {
        return String.join(",", messageType, fromPhoneNumber, toPhoneNumber);
    }

    public Object getMessageData() {
        return messageData;
    }

    public void setMessageData(Object messageData) {
        this.messageData = messageData;
    }
}
