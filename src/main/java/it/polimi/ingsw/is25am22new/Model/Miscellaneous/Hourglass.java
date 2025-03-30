package it.polimi.ingsw.is25am22new.Model.Miscellaneous;

import java.util.Timer;
import java.util.TimerTask;

public class Hourglass {
    private Timer timer;
    private int remainingSeconds;
    boolean active;

    public Hourglass(int remainingSeconds) {
        this.timer = new Timer();
        this.remainingSeconds = remainingSeconds;
        this.active = false;
    }

    public void startTimer(Runnable callbackMethod) {
        if (active) return; // So it doesn't start a new timer if one is already active

        active = true;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (remainingSeconds > 0) {
                    remainingSeconds--;
                } else {
                    stopTimer();
                    callbackMethod.run(); // Executes the callback (an action) when it finishes
                }
            }
        }, 0, 1000);
    }

    public void stopTimer() {
        if (active) {
            timer.cancel();
            active = false;
        }
    }

    public int getRemainingSeconds() {
        return remainingSeconds;
    }
}
