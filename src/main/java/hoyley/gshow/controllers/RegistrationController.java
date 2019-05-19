package hoyley.gshow.controllers;

import hoyley.gshow.Constants;
import hoyley.gshow.helpers.PlayerHelper;
import hoyley.gshow.service.SessionManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RegistrationController {

    @Autowired
    private SessionManagementService sessionService;
    @Autowired
    private HttpServletRequest servletRequest;
    
    @PostMapping("/registerPlayer")
    public void registerPlayer(@RequestBody String playerName, HttpServletRequest request) {
        sessionService.getSessionSafe(Constants.DEFAULT_SESSION).registerPlayer(playerName, request.getSession().getId());
    }

    @GetMapping("/registerAdmin")
    public ModelAndView registerAdmin(@RequestParam String adminKey, HttpServletRequest request) {
        if (sessionService.getSessionSafe(Constants.DEFAULT_SESSION).registerAdmin(adminKey, request.getSession().getId())) {
            return new ModelAndView("redirect:/");
        }
        return null;
    }

    @GetMapping("/logout")
    public void logout() {
        sessionService.getSessionSafe(Constants.DEFAULT_SESSION).logout(getPlayerHelper());
    }

    private PlayerHelper getPlayerHelper() {
        return new PlayerHelper(servletRequest.getSession().getId(),
            sessionService.getSessionSafe(Constants.DEFAULT_SESSION).getGame().getState());
    }
}
