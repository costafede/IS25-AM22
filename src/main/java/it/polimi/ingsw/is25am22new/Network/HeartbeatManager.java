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

    /**
     * The HeartbeatDisconnectHandler interface defines a contract for handling disconnection events
     * in a system that monitors client activity through heartbeat signals. Implementations of this
     * interface are expected to provide logic for processing the disconnection of clients that fail
     * to send heartbeat signals within a specified timeout period.
     *
     * This interface is leveraged in heartbeat management systems to decouple the detection of
     * heartbeat timeouts from the logic that manages disconnected clients, enabling a flexible and
     * modular approach to handling client disconnections.
     */
    public interface HeartbeatDisconnectHandler {
        void handleDisconnect(String clientId);
    }

    /**
     * Constructs a HeartbeatManager instance responsible for managing client heartbeats
     * and detecting timeouts. It initializes the heartbeat monitoring task that periodically
     * checks for clients that have timed out.
     *
     * @param heartbeatTimeoutMs the duration in milliseconds after which a client is considered timed out if no heartbeat is received
     * @param disconnectHandler a handler invoked when a client is detected as disconnected
     */
    public HeartbeatManager(long heartbeatTimeoutMs, HeartbeatDisconnectHandler disconnectHandler) {
        this.heartbeatTimeoutMs = heartbeatTimeoutMs;
        this.disconnectHandler = disconnectHandler;

        // Start monitoring task
        scheduler.scheduleAtFixedRate(this::checkHeartbeats,
                heartbeatTimeoutMs/2, heartbeatTimeoutMs/2, TimeUnit.MILLISECONDS);
    }

    /**
     * Registers a client by adding its identifier to the heartbeat tracking system
     * with the current system time as the initial heartbeat timestamp.
     *
     * @param clientId the unique identifier of the client to be registered
     */
    public void registerClient(String clientId) {
        lastHeartbeats.put(clientId, System.currentTimeMillis());
    }

    /**
     * Updates the last heartbeat timestamp for a specific client.
     * The heartbeat method is used to signal that a client is still active,
     * by recording the current system time as the last heartbeat time
     * for the given client identifier.
     *
     * @param clientId The unique identifier of the client sending the heartbeat.
     */
    public void heartbeat(String clientId) {
        lastHeartbeats.put(clientId, System.currentTimeMillis());
    }

    /**
     * Unregisters a client by removing its entry from the heartbeat tracking map.
     *
     * @param clientId the unique identifier of the client to be unregistered
     */
    public void unregisterClient(String clientId) {
        lastHeartbeats.remove(clientId);
    }

    /**
     * Unregisters all currently registered clients by clearing the internal heartbeat tracking data.
     * This method removes all records of the last received heartbeats, effectively resetting the
     * state of the heartbeat manager.
     *
     * Thread Safety:
     * This method is thread-safe as it operates on a thread-safe data structure.
     *
     * Usage Considerations:
     * - Use this method when all clients need to be unregistered simultaneously.
     * - Calling this method does not notify clients or handle any disconnect logic;
     *   it simply clears the internal state.
     */
    public void unregisterAll() {
        lastHeartbeats.clear();
    }

    /**
     * Monitors the heartbeat signals of registered clients and detects timeouts.
     *
     * For each client in the map of heartbeats, this method calculates the time elapsed
     * since the last recorded heartbeat. If the elapsed time exceeds the configured
     * heartbeat timeout duration, the client is considered to have timed out.
     *
     * Upon detecting a timeout, the method triggers the provided disconnect handler to take
     * appropriate actions for the disconnected client and removes the client's entry
     * from the heartbeat tracking map.
     *
     * Thread Safety:
     * This method operates on a thread-safe map (ConcurrentHashMap) to ensure that
     * client heartbeat data can be safely accessed and modified in concurrent scenarios.
     */
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

    /**
     * Shuts down the scheduled monitoring service of the HeartbeatManager.
     * This method stops the ScheduledExecutorService used for periodically
     * checking the heartbeats of registered clients, effectively terminating
     * any further monitoring tasks.
     *
     * It is recommended to call this method when the HeartbeatManager is no longer
     * needed to release any associated resources and to avoid any potential
     * memory leaks or lingering background threads.
     *
     * Once this method is called, the HeartbeatManager will not perform any
     * further timeout detection or client heartbeat tracking.
     */
    public void shutdown() {
        scheduler.shutdown();
    }
}