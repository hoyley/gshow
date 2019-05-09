package hoyley.gshow.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hoyley.gshow.helpers.PlayerConnections;
import hoyley.gshow.helpers.PlayerHelper;
import hoyley.gshow.service.SessionManagementService;
import hoyley.gshow.service.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
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
    private SessionManagementService sessionManagementService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private PlayerConnections connections;
    @Value("${game.events.reconnectTimeMillis}")
    private int reconnectTimeMillis = 5000;
    @Value("${game.events.keepAliveTimeMillis}")
    private int keepAliveTimeMillis = 5000;

    private final Timer timer = new Timer(true);

    public EventController() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                keepAlive();
            }
        }, 5000, 5000);
    }


    @EventListener
    public void updateSession(String sessionId) {
        publishState(sessionId);
    }

    @GetMapping(path = "register", produces = "text/event-stream")
    public SseEmitter register(@RequestParam String instanceKey, HttpServletRequest request) {
        if (instanceKey == null || instanceKey.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'instanceKey' is required.");
        }

        String sessionId = request.getSession().getId();

        SseEmitter emitter = connections.registerPlayer(sessionId, instanceKey);
        publishState(sessionId);

        return emitter;
    }

    private void publishState(String playerSessionId) {
        connections.forEach(playerSessionId, (sessionId, instanceKey, emitter) -> {
            try {
                emitter.send(SseEmitter.event()
                    .data(getState(playerSessionId))
                    .id(UUID.randomUUID().toString())
                    .name("state")
                    .reconnectTime(reconnectTimeMillis)
                );
                logger.info("Published current state to player [{}:{}].", playerSessionId, instanceKey);
            } catch (IOException ex) {
                logger.trace(String.format("Error sending event to player [%s:%s].", playerSessionId,
                    instanceKey), ex);
                connections.removeConnection(playerSessionId, instanceKey, emitter, "Error " + ex.getClass());
            }
        });
    }

    private void keepAlive() {
        connections.forEach((sessionKey, instanceKey, emitter) -> {
            try {
                emitter.send(SseEmitter.event()
                    .name("keepAlive")
                    .reconnectTime(reconnectTimeMillis)
                    .build());
            } catch (Exception ex) {
                logger.trace(String.format("Error sending keepalive event player [{}:{}]", sessionKey, instanceKey),
                    ex);
                connections.removeConnection(sessionKey, instanceKey, emitter, "Error " + ex.getClass());
            }
        });
    }

    private String getState(String playerSessionId) {
        try {
            SessionService sessionService = sessionManagementService.getSessionSafe("main");
            PlayerHelper helper = getPlayerHelper(playerSessionId);
            return mapper.writeValueAsString(sessionService.getState().getSessionState(helper));
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Error writing state to string.", ex);
        }
    }

    private PlayerHelper getPlayerHelper(String playerSessionId) {
        return new PlayerHelper(playerSessionId,
            sessionManagementService.getSessionSafe("main").getGame().getState());
    }
}