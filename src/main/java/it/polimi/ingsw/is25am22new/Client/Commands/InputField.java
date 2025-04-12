package it.polimi.ingsw.is25am22new.Client.Commands;

public class InputField {
    private final String name;
    private final Class<?> type;  // parameter type (es: Integer.class, String.class)

    public InputField(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }

    public String getName() { return name; }
    public Class<?> getType() { return type; }
}
