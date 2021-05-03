package io.github.metalturtle18.scanblockhunt.events;

import io.github.metalturtle18.scanblockhunt.ScanBlockHunt;

public class RoundElapsedRunnable implements Runnable {
    @Override
    public void run() {
        if (ScanBlockHunt.runningGame != null) {
            ScanBlockHunt.runningGame.increaseTimeElapsed();
        }
    }
}
