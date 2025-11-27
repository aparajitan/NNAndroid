package com.app_neighbrsnook.pojo.referal;

public class Referrer {

    private int id;
    private String name;
    private String emailid;
    private String phoneno;

    public String getNbd_name() {
        return nbd_name;
    }

    public void setNbd_name(String nbd_name) {
        this.nbd_name = nbd_name;
    }

    private String nbd_name;

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmailid() { return emailid; }
    public String getPhoneno() { return phoneno; }
}
