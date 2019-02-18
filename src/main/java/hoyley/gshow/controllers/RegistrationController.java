package hoyley.gshow.controllers;

import hoyley.gshow.model.Player;
import hoyley.gshow.model.RootState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
public class RegistrationController {

    @Autowired
    private RootState state;

    @PostMapping("/registerPlayer")
    public void registerPlayer(@RequestBody String playerName, HttpServletRequest request) {
        Player newPlayer = registerPlayer(playerName, request.getSession().getId());
        if (newPlayer != null) {
            state.setMyPlayer(newPlayer);
        }
    }

    public Player registerPlayer(String playerName, String sessionId) {
        Player player = new Player() {{
            setSessionId(sessionId);
            setNickname(playerName);
            setId(UUID.randomUUID().toString());
        }};

        // If player does not already exist with the given session ID, add the player. We don't want
        // the player to add themselves twice
        if (state.getRegisteredPlayers().stream()
                .anyMatch(p -> p.getSessionId().equals(sessionId)) == false) {
            state.getRegisteredPlayers().add(player);
            return player;
        } else {
            return null;
        }
    }

}
