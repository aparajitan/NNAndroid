package com.app_neighbrsnook.pojo.eventDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventListComentPojo {



        @SerializedName("ec_id")
        @Expose
        private String ecId;
        @SerializedName("event_id")
        @Expose
        private String eventId;
        @SerializedName("userid")
        @Expose
        private String userid;
        @SerializedName("commenttext")
        @Expose
        private String commenttext;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("createon")
        @Expose
        private String createon;
        @SerializedName("userpic")
        @Expose
        private String userpic;
        @SerializedName("neighbrhood")
        @Expose
        private String neighbrhood;

        public String getEcId() {
            return ecId;
        }

        public void setEcId(String ecId) {
            this.ecId = ecId;
        }

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getCommenttext() {
            return commenttext;
        }

        public void setCommenttext(String commenttext) {
            this.commenttext = commenttext;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getCreateon() {
            return createon;
        }

        public void setCreateon(String createon) {
            this.createon = createon;
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


}
