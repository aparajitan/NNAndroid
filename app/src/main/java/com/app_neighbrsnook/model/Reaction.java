package com.app_neighbrsnook.model;

public class Reaction {
    String reactionName;
    String reactionCode;

    public Reaction(String reactionName, String reactionCode) {
        this.reactionName = reactionName;
        this.reactionCode = reactionCode;
    }

    public String getReactionName() {
        return reactionName;
    }

    public void setReactionName(String reactionName) {
        this.reactionName = reactionName;
    }

    public String getReactionCode() {
        return reactionCode;
    }

    public void setReactionCode(String reactionCode) {
        this.reactionCode = reactionCode;
    }
}
