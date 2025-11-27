package com.app_neighbrsnook.pojo.referal;

public class Data {

    private int id;
    private int referrer_user_id;
    private String referred_name;
    private String referred_phone;
    private String referred_email;
    private int neighbourhood_id;
    private String referral_code;
    private String status;
    private String referred_at;
    private String remarks;
    private Referrer referrer;

    // Getters
    public int getId() { return id; }
    public int getReferrer_user_id() { return referrer_user_id; }
    public String getReferred_name() { return referred_name; }
    public String getReferred_phone() { return referred_phone; }
    public String getReferred_email() { return referred_email; }
    public int getNeighbourhood_id() { return neighbourhood_id; }
    public String getReferral_code() { return referral_code; }
    public String getStatus() { return status; }
    public String getReferred_at() { return referred_at; }
    public String getRemarks() { return remarks; }
    public Referrer getReferrer() { return referrer; }
}
