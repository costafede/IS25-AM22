package it.polimi.ingsw.is25am22new.Client.View;

import it.polimi.ingsw.is25am22new.Model.Miscellaneous.Bank;

public class GameCliView implements GameView{
    Bank bank;

    public GameCliView() {
        this.bank = new Bank();
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Bank getBank() {
        return bank;
    }
}
