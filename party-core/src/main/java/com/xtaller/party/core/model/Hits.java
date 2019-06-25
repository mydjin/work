package com.xtaller.party.core.model;
/**
 * 阅读数
 * **/
public class Hits {
    private String id;
    private String type;

    public Hits() {
    }

    public Hits(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
