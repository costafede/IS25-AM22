package it.polimi.ingsw.is25am22new.Model;

import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.is25am22new.Model.GoodBlock.REDBLOCK;
import static it.polimi.ingsw.is25am22new.Model.GoodBlock.YELLOWBLOCK;
import static it.polimi.ingsw.is25am22new.Model.GoodBlock.GREENBLOCK;
import static it.polimi.ingsw.is25am22new.Model.GoodBlock.BLUEBLOCK;


public class Bank {

    private Map<GoodBlock, Integer> goodblockToNum;

    //inizializza la banca con il numero di blocchi a disposizione per la partita
    public void initBank(){
        goodblockToNum = new HashMap<GoodBlock, Integer>();
        goodblockToNum.put(REDBLOCK, 12);
        goodblockToNum.put(YELLOWBLOCK, 17);
        goodblockToNum.put(GREENBLOCK, 13);
        goodblockToNum.put(BLUEBLOCK, 14);
    }

    //incrementa la quantita' del tipo di blocco dato nella banca
    public void depositGoodBlock(GoodBlock gb){
        if (gb.equals(REDBLOCK)){
            goodblockToNum.put(gb, goodblockToNum.get(gb) + 1);
        }
        else if (gb.equals(YELLOWBLOCK)){
            goodblockToNum.put(gb, goodblockToNum.get(gb) + 1);
        }
        else if (gb.equals(GREENBLOCK)){
            goodblockToNum.put(gb, goodblockToNum.get(gb) + 1);
        }
        else if (gb.equals(BLUEBLOCK)){
            goodblockToNum.put(gb, goodblockToNum.get(gb) + 1);
        }
    }

    //decrementa la quantita' del tipo di blocco dato nella banca
    public void withdrawGoodBlock(GoodBlock gb){
        if (gb.equals(REDBLOCK)){
            goodblockToNum.put(gb, goodblockToNum.get(gb) - 1);
        }
        if (gb.equals(YELLOWBLOCK)){
            goodblockToNum.put(gb, goodblockToNum.get(gb) - 1);
        }
        if (gb.equals(GREENBLOCK)){
            goodblockToNum.put(gb, goodblockToNum.get(gb) - 1);
        }
        if (gb.equals(BLUEBLOCK)){
            goodblockToNum.put(gb, goodblockToNum.get(gb) - 1);
        }
    }
}
