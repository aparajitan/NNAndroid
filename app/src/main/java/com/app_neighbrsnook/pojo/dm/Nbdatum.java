package com.app_neighbrsnook.pojo.dm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Nbdatum {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("dttime")
    @Expose
    private String dttime;

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("msg_id")
    @Expose
    private String msg_id;

    @SerializedName("userpic")
    @Expose
    private String userpic;
    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDttime() {
        return dttime;
    }

    public void setDttime(String dttime) {
        this.dttime = dttime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }
}
