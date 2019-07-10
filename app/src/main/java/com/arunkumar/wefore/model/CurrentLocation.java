package com.arunkumar.wefore.model;

import com.google.gson.annotations.SerializedName;

public class CurrentLocation {
    @SerializedName("name")
    private String name;

    public CurrentLocation name(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }
}
