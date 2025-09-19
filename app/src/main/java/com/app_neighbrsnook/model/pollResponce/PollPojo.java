package com.app_neighbrsnook.model.pollResponce;

import java.util.ArrayList;

public class PollPojo {

    public boolean status;
    public String message;
    public ArrayList<Poll> polls;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Poll> getPolls() {
        return polls;
    }

    public void setPolls(ArrayList<Poll> polls) {
        this.polls = polls;
    }

}
