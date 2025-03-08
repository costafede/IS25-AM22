module it.polimi.ingsw.is25am22new {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens it.polimi.ingsw.is25am22new to javafx.fxml;
    exports it.polimi.ingsw.is25am22new;
}