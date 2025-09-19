package com.app_neighbrsnook.pojo.directmessagelist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Nbdatum {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("userpic")
    @Expose
    private String userpic;
    @SerializedName("dttime")
    @Expose
    private String dttime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    public String getDttime() {
        return dttime;
    }

    public void setDttime(String dttime) {
        this.dttime = dttime;
    }

}
