package com.app_neighbrsnook.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupDetailsbyNameResponse {
 @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("status1")
        @Expose
        private String status1;
        @SerializedName("groupname")
        @Expose
        private String groupname;
        @SerializedName("groupuserneigh")
        @Expose
        private String groupuserneigh;
        @SerializedName("groupusername")
        @Expose
        private String groupusername;
        @SerializedName("groupuserpic")
        @Expose
        private String groupuserpic;
        @SerializedName("ownerid")
        @Expose
        private String ownerid;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("memberlist")
        @Expose
        private List<GroupDetailsbyNamePojo> memberlist;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getStatus1() {
            return status1;
        }

        public void setStatus1(String status1) {
            this.status1 = status1;
        }

        public String getGroupname() {
            return groupname;
        }

        public void setGroupname(String groupname) {
            this.groupname = groupname;
        }

        public String getGroupuserneigh() {
            return groupuserneigh;
        }

        public void setGroupuserneigh(String groupuserneigh) {
            this.groupuserneigh = groupuserneigh;
        }

        public String getGroupusername() {
            return groupusername;
        }

        public void setGroupusername(String groupusername) {
            this.groupusername = groupusername;
        }

        public String getGroupuserpic() {
            return groupuserpic;
        }

        public void setGroupuserpic(String groupuserpic) {
            this.groupuserpic = groupuserpic;
        }

        public String getOwnerid() {
            return ownerid;
        }

        public void setOwnerid(String ownerid) {
            this.ownerid = ownerid;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<GroupDetailsbyNamePojo> getMemberlist() {
            return memberlist;
        }

        public void setMemberlist(List<GroupDetailsbyNamePojo> memberlist) {
            this.memberlist = memberlist;
        }

    }
