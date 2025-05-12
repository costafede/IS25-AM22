package it.polimi.ingsw.is25am22new.Network;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HeartbeatManager {
    private final Map<String, Long> lastHeartbeats = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final long heartbeatTimeoutMs;
    private final HeartbeatDisconnectHandler disconnectHandler;

    public interface HeartbeatDisconnectHandler {
        void handleDisconnect(String clientId);
    }

    public HeartbeatManager(long heartbeatTimeoutMs, HeartbeatDisconnectHandler disconnectHandler) {
        this.heartbeatTimeoutMs = heartbeatTimeoutMs;
        this.disconnectHandler = disconnectHandler;

        // Start monitoring task
        scheduler.scheduleAtFixedRate(this::checkHeartbeats,
                heartbeatTimeoutMs/2, heartbeatTimeoutMs/2, TimeUnit.MILLISECONDS);
    }

    public void registerClient(String clientId) {
        lastHeartbeats.put(clientId, System.currentTimeMillis());
    }

    public void heartbeat(String clientId) {
        lastHeartbeats.put(clientId, System.currentTimeMillis());
    }

    public void unregisterClient(String clientId) {
        lastHeartbeats.remove(clientId);
    }

    private void checkHeartbeats() {
        long now = System.currentTimeMillis();
        lastHeartbeats.forEach((clientId, lastHeartbeat) -> {
            if (now - lastHeartbeat > heartbeatTimeoutMs) {
                // Client has timed out
                System.out.println("Client " + clientId + " timed out (no heartbeat)");
                disconnectHandler.handleDisconnect(clientId);
                lastHeartbeats.remove(clientId);
            }
        });
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}