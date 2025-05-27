package it.polimi.ingsw.is25am22new.Client.View.TUI.DrawableTilesTUI;

import it.polimi.ingsw.is25am22new.Model.ComponentTiles.ShieldGenerator;
import it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side;

import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.*;
import static it.polimi.ingsw.is25am22new.Model.ComponentTiles.Side.UNIVERSALPIPE;

public class DrawableShieldGeneratorTUI extends DrawableComponentTileTUI{

    ShieldGenerator shieldGenerator;

    public DrawableShieldGeneratorTUI(ShieldGenerator shieldGenerator) {
        this.shieldGenerator = shieldGenerator;
    }

    @Override
    public String[] draw(){
        String top = "";
        String bottom = "";
        String left = "";
        String right = "";
        String active = "";

        Side topSide = shieldGenerator.getTopSide();
        Side bottomSide = shieldGenerator.getBottomSide();
        Side leftSide = shieldGenerator.getLeftSide();
        Side rightSide = shieldGenerator.getRightSide();
        boolean topSideShieldable = shieldGenerator.isTopSideShieldable();
        boolean bottomSideShieldable = shieldGenerator.isBottomSideShieldable();
        boolean leftSideShieldable = shieldGenerator.isLeftSideShieldable();
        boolean rightSideShieldable = shieldGenerator.isRightSideShieldable();


        if (topSide.equals(SMOOTH) && !topSideShieldable){top = "   S   ";}
        else if (topSide.equals(ONEPIPE) && !topSideShieldable){top = "   1   ";}
        else if (topSide.equals(TWOPIPES) && !topSideShieldable){top = "   2   ";}
        else if (topSide.equals(UNIVERSALPIPE) && !topSideShieldable){top = "   3   ";}
        else if (topSide.equals(SMOOTH) && topSideShieldable){top = "   S%  ";}
        else if (topSide.equals(ONEPIPE) && topSideShieldable){top = "   1%  ";}
        else if (topSide.equals(TWOPIPES) && topSideShieldable){top = "   2%  ";}
        else if (topSide.equals(UNIVERSALPIPE) && topSideShieldable){top = "   3%  ";}

        if (bottomSide.equals(SMOOTH) && !bottomSideShieldable){bottom = "   S   ";}
        else if (bottomSide.equals(ONEPIPE) && !bottomSideShieldable){bottom = "   1   ";}
        else if (bottomSide.equals(TWOPIPES) && !bottomSideShieldable){bottom = "   2   ";}
        else if (bottomSide.equals(UNIVERSALPIPE) && !bottomSideShieldable){bottom = "   3   ";}
        else if (bottomSide.equals(SMOOTH) && bottomSideShieldable){bottom = "   S%  ";}
        else if (bottomSide.equals(ONEPIPE) && bottomSideShieldable){bottom = "   1%  ";}
        else if (bottomSide.equals(TWOPIPES) && bottomSideShieldable){bottom = "   2%  ";}
        else if (bottomSide.equals(UNIVERSALPIPE) && bottomSideShieldable){bottom = "   3%  ";}

        if (leftSide.equals(SMOOTH) && !leftSideShieldable){left = "S ";}
        else if (leftSide.equals(ONEPIPE) && !leftSideShieldable){left = "1 ";}
        else if (leftSide.equals(TWOPIPES) && !leftSideShieldable){left = "2 ";}
        else if (leftSide.equals(UNIVERSALPIPE) && !leftSideShieldable){left = "3 ";}
        else if (leftSide.equals(SMOOTH) && leftSideShieldable){left = "S%";}
        else if (leftSide.equals(ONEPIPE) && leftSideShieldable){left = "1%";}
        else if (leftSide.equals(TWOPIPES) && leftSideShieldable){left = "2%";}
        else if (leftSide.equals(UNIVERSALPIPE) && leftSideShieldable){left = "3%";}

        if (rightSide.equals(SMOOTH) && !rightSideShieldable){right = " S";}
        else if (rightSide.equals(ONEPIPE) && !rightSideShieldable){right = " 1";}
        else if (rightSide.equals(TWOPIPES) && !rightSideShieldable){right = " 2";}
        else if (rightSide.equals(UNIVERSALPIPE) && !rightSideShieldable){right = " 3";}
        else if (rightSide.equals(SMOOTH) && rightSideShieldable){right = "%S";}
        else if (rightSide.equals(ONEPIPE) && rightSideShieldable){right = "%1";}
        else if (rightSide.equals(TWOPIPES) && rightSideShieldable){right = "%2";}
        else if (rightSide.equals(UNIVERSALPIPE) && rightSideShieldable){right = "%3";}

        if(shieldGenerator.isActivated())
            active = "A";
        else
            active = " ";

        return new String[]{
                top,
                "       ",
                left + " SG" + right,
                "   " + active + "   ",
                bottom,
        };
    }
}
