package it.polimi.ingsw.is25am22new.Network;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The HeartbeatManager is responsible for managing and monitoring client heartbeats
 * to detect timeouts and handle client disconnects. It provides mechanisms to register clients,
 * track their heartbeat signals, and automatically detect when a client has timed out
 * by utilizing a scheduled task.
 *
 * Features:
 * - Allows registering and unregistering clients.
 * - Tracks heartbeats for each registered client.
 * - Periodically checks for client timeouts using a configurable timeout duration.
 * - Automatically invokes a custom handler for disconnected clients.
 * - Supports shutting down the monitoring service.
 *
 * Thread Safety:
 * This class is thread-safe as it uses a ConcurrentHashMap for storing client heartbeats
 * and a ScheduledExecutorService for periodic monitoring.
 */
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

    public void unregisterAll() {
        lastHeartbeats.clear();
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