package bean;

public class Message {

    private int messageType; // 0: without photoData; 1: with photoData

    private Object messageData;

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public Object getMessageData() {
        return messageData;
    }

    public void setMessageData(Object messageData) {
        this.messageData = messageData;
    }
}
