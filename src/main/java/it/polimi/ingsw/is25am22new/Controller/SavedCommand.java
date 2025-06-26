package it.polimi.ingsw.is25am22new.Controller;

import it.polimi.ingsw.is25am22new.Model.AdventureCard.InputCommand;

public class SavedCommand {
    private String type;
    private String nickname;
    private String tilePngName;
    private InputCommand inputCommand;
    private int i;
    private int j;

    /**
     * Constructs a SavedCommand with type, nickname, tile PNG name, and coordinates.
     *
     * @param type the command type
     * @param nickname the player nickname involved in the command
     * @param tilePngName the name of the tile PNG
     * @param i the first coordinate or index
     * @param j the second coordinate or index
     */
    public SavedCommand(String type, String nickname, String tilePngName, int i, int j) {
        this.type = type;
        this.nickname = nickname;
        this.tilePngName = tilePngName;
        this.i = i;
        this.j = j;
    }

    /**
     * Constructs a SavedCommand with type, nickname, and coordinates.
     *
     * @param type the command type
     * @param nickname the player nickname involved in the command
     * @param i the first coordinate or index
     * @param j the second coordinate or index
     */
    public SavedCommand(String type, String nickname, int i, int j) {
        this.type = type;
        this.nickname = nickname;
        this.i = i;
        this.j = j;
    }

    /**
     * Constructs a SavedCommand with type, nickname, and one coordinate/index.
     *
     * @param type the command type
     * @param nickname the player nickname involved in the command
     * @param i the coordinate or index
     */
    public SavedCommand(String type, String nickname, int i) {
        this.type = type;
        this.nickname = nickname;
        this.i = i;
    }

    /**
     * Constructs a SavedCommand with type and nickname.
     *
     * @param type the command type
     * @param nickname the player nickname involved in the command
     */
    public SavedCommand(String type, String nickname) {
        this.type = type;
        this.nickname = nickname;
    }

    /**
     * Constructs a SavedCommand with type and an InputCommand.
     *
     * @param type the command type
     * @param inputCommand the InputCommand associated to this saved command
     */
    public SavedCommand(String type, InputCommand inputCommand) {
        this.type = type;
        this.inputCommand = inputCommand;
    }

    /**
     * Constructs a SavedCommand with only the type.
     *
     * @param type the command type
     */
    public SavedCommand(String type) {
        this.type = type;
    }

    /**
     * Constructs a SavedCommand with type, nickname, and tile PNG name.
     *
     * @param type the command type
     * @param nickname the player nickname involved in the command
     * @param tilePngName the name of the tile PNG
     */
    public SavedCommand(String type, String nickname, String tilePngName) {
        this.type = type;
        this.nickname = nickname;
        this.tilePngName = tilePngName;
    }

    /**
     * Default no-arg constructor.
     */
    public SavedCommand() {}

    /**
     * Gets the command type.
     *
     * @return the command type
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the player nickname involved in the command.
     *
     * @return the player nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the command type.
     *
     * @param type the command type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets the player nickname involved in the command.
     *
     * @param nickname the player nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Sets the tile PNG name.
     *
     * @param tilePngName the tile PNG name
     */
    public void setTilePngName(String tilePngName) {
        this.tilePngName = tilePngName;
    }

    /**
     * Sets the first coordinate or index.
     *
     * @param i the coordinate or index
     */
    public void setI(int i) {
        this.i = i;
    }

    /**
     * Sets the second coordinate or index.
     *
     * @param j the coordinate or index
     */
    public void setJ(int j) {
        this.j = j;
    }

    /**
     * Gets the tile PNG name.
     *
     * @return the tile PNG name
     */
    public String getTilePngName() {
        return tilePngName;
    }

    /**
     * Gets the first coordinate or index.
     *
     * @return the coordinate or index i
     */
    public int getI() {
        return i;
    }

    /**
     * Gets the InputCommand associated with this command.
     *
     * @return the InputCommand
     */
    public InputCommand getInputCommand() {
        return inputCommand;
    }

    /**
     * Sets the InputCommand associated with this command.
     *
     * @param inputCommand the InputCommand to set
     */
    public void setInputCommand(InputCommand inputCommand) {
        this.inputCommand = inputCommand;
    }

    /**
     * Gets the second coordinate or index.
     *
     * @return the coordinate or index j
     */
    public int getJ() {
        return j;
    }
}
