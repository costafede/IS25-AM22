package it.polimi.ingsw.is25am22new.Controller;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

public class SavedCommand {
    private String type;
    private String nickname;
    private String tilePngName;
    private InputCommand inputCommand;
    private int i;
    private int j;

    public SavedCommand(String type, String nickname, String tilePngName, int i, int j) {
        this.type = type;
        this.nickname = nickname;
        this.tilePngName = tilePngName;
        this.i = i;
        this.j = j;
    }

    public SavedCommand(String type, String nickname, int i, int j) {
        this.type = type;
        this.nickname = nickname;
        this.i = i;
        this.j = j;
    }

    public SavedCommand(String type, String nickname, int i) {
        this.type = type;
        this.nickname = nickname;
        this.i = i;
    }

    public SavedCommand(String type, String nickname) {
        this.type = type;
        this.nickname = nickname;
    }

    public SavedCommand(String type, InputCommand inputCommand) {
        this.type = type;
        this.inputCommand = inputCommand;
    }

    public SavedCommand(String type) {
        this.type = type;
    }

    public SavedCommand(String type, String nickname, String tilePngName) {
        this.type = type;
        this.nickname = nickname;
        this.tilePngName = tilePngName;
    }

    public SavedCommand() {}

    public String getType() {
        return type;
    }

    public String getNickname() {
        return nickname;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setTilePngName(String tilePngName) {
        this.tilePngName = tilePngName;
    }

    public void setI(int i) {
        this.i = i;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public String getTilePngName() {
        return tilePngName;
    }

    public int getI() {
        return i;
    }

    public InputCommand getInputCommand() {
        return inputCommand;
    }

    public void setInputCommand(InputCommand inputCommand) {
        this.inputCommand = inputCommand;
    }

    public int getJ() {
        return j;
    }
}
