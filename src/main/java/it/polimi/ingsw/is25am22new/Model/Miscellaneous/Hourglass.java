package it.polimi.ingsw.is25am22new.Model.Miscellaneous;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The Hourglass class represents a countdown timer that decrements every second and allows
 * actions to be executed upon completion using a callback method. The timer can be started,
 * stopped, or reset as needed.
 *
 * This class is designed to support a simple timer mechanism with the following features:
 * - Start a countdown timer for a predefined duration.
 * - Perform a specific action (callback) when the countdown reaches zero.
 * - Stop and reset the timer.
 *
 * Note: The class implements Serializable to support serialization, but the Timer instance
 * is marked as transient since it cannot be serialized.
 */
public class Hourglass implements Serializable {
    private transient Timer timer;
    private final int timerDuration;
    private int remainingSeconds;
    boolean active;


    public Hourglass(int remainingSeconds) {
        this.timer = new Timer();
        this.remainingSeconds = remainingSeconds;
        this.timerDuration = remainingSeconds;
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
            timer = new Timer();
            active = false;
            remainingSeconds = timerDuration;
        }
    }

    public int getRemainingSeconds() {
        return remainingSeconds;
    }
}
