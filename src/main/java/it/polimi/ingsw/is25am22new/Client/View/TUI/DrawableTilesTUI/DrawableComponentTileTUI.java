package it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI;

/**
 * Represents an abstract base class for drawable text-based UI (TUI) components
 * corresponding to tile models in a game or application.
 *
 * This class is designed to be extended by specific drawable component implementations,
 * each of which defines a visual representation for a particular type of game component.
 * The draw method must be implemented by subclasses to specify how the component should
 * be visually rendered as text.
 */
public abstract class DrawableComponentTileTUI {

    public abstract String[] draw();
}
