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

    /**
     * Constructs a new SocketMessage object with the specified command, object, and payload.
     *
     * @param command the command associated with this message, typically representing the action to be performed.
     * @param object an optional object associated with this message, used to pass additional information.
     * @param payload an optional string payload associated with this message, used for carrying additional data.
     */
    public SocketMessage(String command, Object object, String payload) {
        this.command = command;
        this.object = object;
        this.payload = payload;
    }
    /**
     * Retrieves the command associated with this message.
     *
     * @return the command as a String, representing the type or purpose of the message.
     */
    public String getCommand() {
        return command;
    }
    /**
     * Retrieves the object encapsulated within the current instance.
     * The returned object is typically used to convey data or payloads
     * in socket-based communication scenarios.
     *
     * @return the object associated with this instance, or null if no object has been set
     */
    public Object getObject() {
        return object;
    }
    /**
     * Sets the command for this message.
     * The command is typically used to indicate the type of operation
     * or instruction for the socket communication.
     *
     * @param command the command string to be set
     */
    public void setCommand(String command) {
        this.command = command;
    }
    /**
     * Sets the object associated with this message.
     *
     * @param object the object to associate with this message
     */
    public void setObject(Object object) {
        this.object = object;
    }
    /**
     * Retrieves the payload associated with this message.
     *
     * @return the payload as a String, which typically contains additional data or context
     *         related to the message.
     */
    public String getPayload() {
        return payload;
    }
    /**
     * Sets the payload for this message.
     *
     * @param payload the payload to be set, typically used to encapsulate additional
     *                information or data associated with the message.
     */
    public void setPayload(String payload) {
        this.payload = payload;
    }
}
