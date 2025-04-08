module it.polimi.ingsw.is25am22new {
        requires javafx.controls;
        requires javafx.fxml;
        requires com.fasterxml.jackson.databind;
        requires java.desktop;
        requires jdk.jdi;
        requires com.google.gson;
        requires java.rmi;
        requires javafx.graphics;
        requires java.base;
        requires java.logging;

        opens it.polimi.ingsw.is25am22new to javafx.fxml;

        exports it.polimi.ingsw.is25am22new.Model.Games;
        exports it.polimi.ingsw.is25am22new.Controller;
        exports it.polimi.ingsw.is25am22new.Network to java.rmi;
        exports it.polimi.ingsw.is25am22new.Model.Miscellaneous to java.rmi;
        exports it.polimi.ingsw.is25am22new.Model.ComponentTiles to java.rmi;
        exports it.polimi.ingsw.is25am22new.Model.Shipboards to java.rmi;
        exports it.polimi.ingsw.is25am22new.Model.Flightboards to java.rmi;
        exports it.polimi.ingsw.is25am22new.Model.AdventureCard to java.rmi;

        // Open packages for deep reflection needed by RMI
        opens it.polimi.ingsw.is25am22new.Network to java.rmi;
        opens it.polimi.ingsw.is25am22new.Model.Miscellaneous to java.rmi;
        opens it.polimi.ingsw.is25am22new.Model.ComponentTiles to java.rmi;
        opens it.polimi.ingsw.is25am22new.Model.Shipboards to java.rmi;
        opens it.polimi.ingsw.is25am22new.Model.Flightboards to java.rmi;
        opens it.polimi.ingsw.is25am22new.Model.AdventureCard to java.rmi;

        exports it.polimi.ingsw.is25am22new;
        exports it.polimi.ingsw.is25am22new.Network.RMI.Client to java.rmi;
        opens it.polimi.ingsw.is25am22new.Network.RMI.Client to java.rmi;
        exports it.polimi.ingsw.is25am22new.Network.RMI.Server to java.rmi;
        opens it.polimi.ingsw.is25am22new.Network.RMI.Server to java.rmi;
}