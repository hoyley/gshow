package hoyley.gshow.controllers;

import hoyley.gshow.model.Player;
import hoyley.gshow.model.RootState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RegistrationController {

    @Autowired
    private RootState state;

    @PostMapping("/registerPlayer")
    public void registerPlayer(@RequestBody String playerName, HttpServletRequest request) {
        registerPlayer(playerName, request.getSession().getId());
    }

    public void registerPlayer(String playerName, String id) {
        Player player = new Player() {{
            setId(id);
            setNickname(playerName);
        }};

        // If player does not already exist with the given session ID, add the player. We don't want
        // the player to add themselves twice
        if (state.getRegisteredPlayers().stream()
                .anyMatch(p -> p.getId().equals(id)) == false) {
            state.getRegisteredPlayers().add(player);
        }
    }

}
