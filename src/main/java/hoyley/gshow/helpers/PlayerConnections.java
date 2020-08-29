package hoyley.gshow.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class PlayerConnections {

    private static final Logger logger = LoggerFactory.getLogger(PlayerConnections.class);
    
    // PlayerSessionId -> [ PlayerInstanceKey -> SseEmitter ]
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, SseEmitter>> playerConnections = new ConcurrentHashMap<>();

    public SseEmitter registerPlayer(String playerSessionId, String instanceKey) {
        ConcurrentHashMap<String, SseEmitter> playerInstances = playerConnections.computeIfAbsent(playerSessionId,
            (k) -> new ConcurrentHashMap<>());
        
        SseEmitter emitter = new SseEmitter();

        emitter.onCompletion(() ->
            removeConnection(playerSessionId, instanceKey, emitter, "Completed"));
        emitter.onError(e ->
            removeConnection(playerSessionId, instanceKey, emitter, "Error " + e.getClass()));
        emitter.onTimeout(() ->
            removeConnection(playerSessionId, instanceKey, emitter, "Timeout"));

        if (playerInstances.putIfAbsent(instanceKey, emitter) != null) {
            throw new RuntimeException(String.format("Player session [{}] already has an instance with key [{}].",
                playerSessionId, instanceKey));
        }
        logger.info("Registered player [{}:{}].", playerSessionId, instanceKey);
        return emitter;
    }

    public void forEach(String playerSessionId, SseUpdater updater) {
        ConcurrentHashMap<String, SseEmitter> playerInstances = playerConnections.get(playerSessionId);
        if (playerInstances != null) {
            playerInstances.forEach((instanceKey, sseEmitter) -> {
                updater.update(playerSessionId, instanceKey, sseEmitter);
            });
        }
    }

    public void forEach(SseUpdater updater) {
        playerConnections.forEach((playerSessionId, instanceMap) -> {
            instanceMap.forEach((instanceKey, sseEmitter) -> {
                updater.update(playerSessionId, instanceKey, sseEmitter);
            });
        });
    }

    public boolean removeConnection(String playerSessionId, String instanceKey, SseEmitter emitter, String reason) {
        ConcurrentHashMap<String, SseEmitter> playerInstances = playerConnections.get(playerSessionId);

        if (playerInstances != null && emitter != null) {
            if (playerInstances.remove(instanceKey, emitter)) {
                logger.info("Closing connection to player [{}:{}] due to [{}].",
                    playerSessionId, instanceKey, reason);
                return true;
            }
        }
        return false;
    }

    public interface SseUpdater {
        void update(String playerSessionKey, String instanceKey, SseEmitter emitter);
    }
}
