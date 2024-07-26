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

    public Player () {}
    public Player (String id, String name, int point) {
        this.id = id;
        this.name = name;
        this.point = point;
        this.status = true;
    }

    public String toJson() {
        return "{" +
                "\"id\":\"" + id + "\"," +
                "\"name\":\"" + name + "\"," +
                "\"point\":" + point + "," +
                "\"status\":" + status +
                "}";
    }
}