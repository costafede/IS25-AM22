package it.polimi.ingsw.is25am22new.Client.View;

import it.polimi.ingsw.is25am22new.Client.Client;

public interface GameView {

    void setClient(Client client);

    Client getClient();

    void startView();

    void initiateConnection();

    void connectionSuccessful();

    void connectionFailure();

}
