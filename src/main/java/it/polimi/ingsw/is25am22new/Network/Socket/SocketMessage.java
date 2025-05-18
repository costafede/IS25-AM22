package it.polimi.ingsw.is25am22new.Network.Socket;

import java.io.Serializable;

/**
 * Represents a message that can be sent or received via a socket connection.
 * This class is used to encapsulate the necessary data for socket-based communication,
 * including a command, an object, and an optional payload.
 */
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
