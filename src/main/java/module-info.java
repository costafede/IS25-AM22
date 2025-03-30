module it.polimi.ingsw.is25am22new {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires jdk.jdi;
    requires com.google.gson;


    opens it.polimi.ingsw.is25am22new to javafx.fxml;
    opens it.polimi.ingsw.is25am22new.Model.Games to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.is25am22new.Model.Miscellaneous to com.fasterxml.jackson.databind;

    exports it.polimi.ingsw.is25am22new;
}