package hoyley.gshow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.With;

@Builder(toBuilder = true)
@Getter
@With
public class Player {

    @NonNull private String id;
    @NonNull private String nickname;
    private int score;

    @JsonIgnore
    @NonNull
    private String sessionId;
}
