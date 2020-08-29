package hoyley.gshow.controllers;

import hoyley.gshow.Constants;
import hoyley.gshow.helpers.PlayerHelper;
import hoyley.gshow.service.GameRoomManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RegistrationController {

    @Autowired
    private GameRoomManagementService sessionService;
    @Autowired
    private HttpServletRequest servletRequest;
    
    @PostMapping("/registerPlayer")
    public void registerPlayer(@RequestBody String playerName, HttpServletRequest request) {
        sessionService.getGameRoom(Constants.DEFAULT_GAME_ROOM).registerPlayer(playerName, request.getSession().getId());
    }

    @GetMapping("/registerAdmin")
    public ModelAndView registerAdmin(@RequestParam String adminKey, HttpServletRequest request) {
        if (sessionService.getGameRoom(Constants.DEFAULT_GAME_ROOM).registerAdmin(adminKey, request.getSession().getId())) {
            return new ModelAndView("redirect:/");
        }
        return null;
    }

    @GetMapping("/logout")
    public void logout() {
        sessionService.getGameRoom(Constants.DEFAULT_GAME_ROOM).logout(getPlayerHelper());
    }

    private PlayerHelper getPlayerHelper() {
        return new PlayerHelper(servletRequest.getSession().getId(),
            sessionService.getGameRoom(Constants.DEFAULT_GAME_ROOM).getGame().getState());
    }
}
