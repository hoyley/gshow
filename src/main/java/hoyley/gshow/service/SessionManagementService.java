package hoyley.gshow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManagementService {

    private ConcurrentHashMap<String, SessionService> sessionMap = new ConcurrentHashMap<>();
    private final SessionServiceFactory serviceFactory;

    @Autowired
    public SessionManagementService(SessionServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    public SessionService getSession(String sessionKey) {
        SessionService service = sessionMap.get(sessionKey);

        if (service == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Session [%s] does not exist.",
                sessionKey));
        }

        return service;
    }

    public SessionService getSessionSafe(String sessionKey) {
        return sessionMap.computeIfAbsent(sessionKey, k -> serviceFactory.create(sessionKey));
    }

    public SessionService createSession(String sessionKey) {
        SessionService newService = sessionMap.computeIfAbsent(sessionKey, k -> serviceFactory.create(sessionKey));

        if (newService == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                String.format("Could not create session [%s]. Session already exists.", sessionKey));
        }

        return newService;
    }
}
