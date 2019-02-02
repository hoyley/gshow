package hoyley.gshow.model;

import lombok.Data;

public abstract class Screen {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
