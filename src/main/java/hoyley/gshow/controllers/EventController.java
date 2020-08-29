package hoyley.gshow.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hoyley.gshow.Constants;
import hoyley.gshow.helpers.PlayerConnections;
import hoyley.gshow.helpers.PlayerHelper;
import hoyley.gshow.service.GameRoomManagementService;
import hoyley.gshow.service.GameRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@Controller
public class EventController {

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private GameRoomManagementService gameRoomManagementService;
    @Autowired
    private ObjectMapper mapper;
    @Value("${game.events.reconnectTimeMillis}")
    private int reconnectTimeMillis = 2000;
    @Value("${game.events.keepAliveTimeMillis}")
    private int keepAliveTimeMillis = 5000;

    private final Timer timer = new Timer(true);

    public EventController() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                keepAlive();
            }
        }, keepAliveTimeMillis, keepAliveTimeMillis);
    }

    @EventListener
    public void updateGameRoom(String gameRoomId) {
        publishState(gameRoomId);
    }

    @GetMapping(path = "register", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter register(@RequestParam String instanceKey, HttpServletRequest request) {
        if (instanceKey == null || instanceKey.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'instanceKey' is required.");
        }

        String playerSessionId = request.getSession().getId();

        SseEmitter emitter = getPlayerConnections(Constants.DEFAULT_GAME_ROOM).registerPlayer(playerSessionId, instanceKey);
        publishPlayerState(Constants.DEFAULT_GAME_ROOM, playerSessionId);

        return emitter;
    }

    private void publishState(String gameRoomId) {
        PlayerConnections connections = getPlayerConnections(gameRoomId);
        connections.forEach((playerSessionId, instanceKey, emitter) ->
            publishState(connections, playerSessionId, instanceKey, emitter)
        );
    }

    private void publishPlayerState(String gameRoomId, String playerSessionId) {
        PlayerConnections connections = getPlayerConnections(gameRoomId);
        connections.forEach(playerSessionId, (psid, instanceKey, emitter) ->
            publishState(connections, playerSessionId, instanceKey, emitter)
        );
    }

    private void publishState(PlayerConnections connections, String playerSessionId, String instanceKey,
                              SseEmitter emitter) {
        try {
            emitter.send(SseEmitter.event()
                .data(getState(playerSessionId))
                .id(UUID.randomUUID().toString())
                .name("state")
                .reconnectTime(reconnectTimeMillis)
            );
            logger.debug("Published current state to player [{}:{}].", playerSessionId, instanceKey);
        } catch (IOException ex) {
            logger.trace(String.format("IOException when sending event to player [%s:%s].", playerSessionId,
                instanceKey), ex);
            connections.removeConnection(playerSessionId, instanceKey, emitter, "Error " + ex.getClass());
        } catch (Exception ex) {
            logger.error(String.format("Error sending event to player [%s:%s].", playerSessionId,
                instanceKey), ex);
            connections.removeConnection(playerSessionId, instanceKey, emitter, "Error " + ex.getClass());
        }
    }

    private void keepAlive() {
        gameRoomManagementService.forEach(gameRoomService -> {
            keepAlive(gameRoomService.getGameRoomKey());
        });
    }

    private void keepAlive(String gameRoomId) {
        PlayerConnections connections = getPlayerConnections(gameRoomId);
        connections.forEach((sessionKey, instanceKey, emitter) -> {
            try {
                emitter.send(SseEmitter.event()
                    .name("keepAlive")
                    .reconnectTime(reconnectTimeMillis)
                    .build());
            } catch (Exception ex) {
                logger.trace(String.format("Error sending keepalive event player [%s:%s]", sessionKey, instanceKey),
                    ex);
                connections.removeConnection(sessionKey, instanceKey, emitter, "Error " + ex.getClass());
            }
        });
    }

    private String getState(String playerSessionId) {
        try {
            GameRoomService gameRoomService = gameRoomManagementService.getGameRoom(Constants.DEFAULT_GAME_ROOM);
            PlayerHelper helper = getPlayerHelper(playerSessionId);
            return mapper.writeValueAsString(gameRoomService.getState().getSessionState(helper));
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Error writing state to string.", ex);
        }
    }

    private PlayerHelper getPlayerHelper(String playerSessionId) {
        return new PlayerHelper(playerSessionId,
            gameRoomManagementService.getGameRoom(Constants.DEFAULT_GAME_ROOM).getGame().getState());
    }

    private PlayerConnections getPlayerConnections(String gameRoomId) {
        return gameRoomManagementService.getGameRoom(gameRoomId).getPlayerConnections();
    }
}
