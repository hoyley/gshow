package hoyley.gshow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Player {

    private String id;
    private String nickname;
    private int score;
    @JsonIgnore
    private String sessionId;
}
