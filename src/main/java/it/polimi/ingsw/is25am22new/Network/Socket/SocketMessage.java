package it.polimi.ingsw.is25am22new.Network.Socket;

import java.io.Serializable;

public class SocketMessage implements Serializable {
    private String command;
    private Object object;
    private String payload;

    public SocketMessage(String command, Object object, String payload) {
        this.command = command;
        this.object = object;
        this.payload = payload;
    }
    public String getCommand() {
        return command;
    }
    public Object getObject() {
        return object;
    }
    public void setCommand(String command) {
        this.command = command;
    }
    public void setObject(Object object) {
        this.object = object;
    }
    public String getPayload() {
        return payload;
    }
    public void setPayload(String payload) {
        this.payload = payload;
    }
}
