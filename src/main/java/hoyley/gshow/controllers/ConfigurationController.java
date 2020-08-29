package hoyley.gshow.controllers;

import hoyley.gshow.model.ServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigurationController {
    private final ServerConfig serverConfig;

    @Autowired
    public ConfigurationController(ServerConfig config) {
        this.serverConfig = config;
    }

    @RequestMapping("/config")
    public ServerConfig config() {
        return serverConfig;
    }
}
