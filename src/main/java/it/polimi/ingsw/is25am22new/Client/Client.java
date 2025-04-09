package it.polimi.ingsw.is25am22new.Client;

import it.polimi.ingsw.is25am22new.Client.View.GameView;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public abstract class Client extends UnicastRemoteObject implements Serializable {

    public GameView gameView;
    public String username;

    protected Client() throws RemoteException {
        super();
        gameView = GalaxyTrucker.gameView;
        boolean connected = false;

        try {
            connect();
            connected = true;
        } catch (NotBoundException | IOException e) {
            // Unable to connect to server, retrying...
        }

        if (connected) {
            gameView.connectionSuccessful();
        } else {
            gameView.connectionFailure();
            System.exit(1);
        }

    }

    public abstract void connect() throws IOException, NotBoundException;

    public void setView(GameView gameView) {
        this.gameView = gameView;
    }
}
