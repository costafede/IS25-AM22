package it.polimi.ingsw.is25am22new.Client;

import it.polimi.ingsw.is25am22new.Parser.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientParser extends Remote {

    void methodCaller(Message message) throws RemoteException;

    String getName() throws RemoteException;

    void setName(String name) throws RemoteException;
}
