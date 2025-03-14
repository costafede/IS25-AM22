module it.polimi.ingsw.is25am22new {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires jdk.jdi;


    opens it.polimi.ingsw.is25am22new to javafx.fxml;
    exports it.polimi.ingsw.is25am22new;
}