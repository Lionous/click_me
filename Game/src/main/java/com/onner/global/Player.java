package com.onner.global;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player {
    private String id;
    private String name;
    private int point;
    private Boolean status;
    private String message;

    public Player () {}
    public Player (String id, String name, int point, String message) {
        this.id = id;
        this.name = name;
        this.point = point;
        this.status = true;
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ",name='" + name + '\'' +
                ",point=" + point +
                ",message='" + message + '\'' +
                '}';
    }
}