package com.app_neighbrsnook.pojo.neighborhoodMember;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Listdatum {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("userpic")
    @Expose
    private String userpic;
    @SerializedName("neighbrhood")
    @Expose
    private String neighbrhood;
    @SerializedName("is_blocked")
    @Expose
    private int is_blocked;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    public String getNeighbrhood() {
        return neighbrhood;
    }

    public void setNeighbrhood(String neighbrhood) {
        this.neighbrhood = neighbrhood;
    }
    public int getIs_blocked() {
        return is_blocked;
    }

    public void setIs_blocked(int is_blocked) {
        this.is_blocked = is_blocked;
    }
}
