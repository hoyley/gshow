package hoyley.gshow.model;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ServerConfig {
    @Value( "${server.sse}" )
    private boolean serverSentEvents;
}
