package hoyley.gshow.controllers;

import hoyley.gshow.helpers.HttpRequestHelper;
import hoyley.gshow.model.Player;
import hoyley.gshow.model.state.GlobalState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.UUID;

@RestController
public class RegistrationController {

    @Autowired
    private GlobalState state;

    @Autowired
    HttpRequestHelper servletHelper;

    @Value("${admin.key}")
    private String adminKey;
    
    @PostMapping("/registerPlayer")
    public void registerPlayer(@RequestBody String playerName, HttpServletRequest request) {
        registerPlayer(playerName, request.getSession().getId());
    }

    @GetMapping("/registerAdmin")
    public ModelAndView registerAdmin(@RequestParam String adminKey, HttpServletRequest request) {
        if (Objects.equals(adminKey, this.adminKey)) {
            state.setAdminSessionId(request.getSession().getId());
            return new ModelAndView("redirect:/");
        }
        return null;
    }

    @GetMapping("/logout")
    public void registerAdmin() {
        if (servletHelper.isAdmin()) {
            state.setAdminSessionId(null);
        }

        if (servletHelper.getPlayer() != null) {
            state.getRegisteredPlayers().remove(servletHelper.getPlayer());
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
