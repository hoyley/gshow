package hoyley.gshow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Wither;

@Builder(toBuilder = true)
@Getter
@Wither
public class Player {

    @NonNull private String id;
    @NonNull private String nickname;
    private int score;

    @JsonIgnore
    @NonNull
    private String sessionId;
}
