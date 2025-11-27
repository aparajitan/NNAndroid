package com.app_neighbrsnook.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocsDataPojo {


        private String id;
        private String passport_front;
        private String passport_back;
        private String aadhar_front;
        private String aadhar_back;
        private String voterid_front;
        private String voterid_back;
        private String driving_license_front;
        private String driving_license_back;

        public String getRent_docs() {
                return rent_docs;
        }

        public void setRent_docs(String rent_docs) {
                this.rent_docs = rent_docs;
        }

        private String rent_docs;

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getPassport_front() { return passport_front; }
        public void setPassport_front(String passport_front) { this.passport_front = passport_front; }
        public String getPassport_back() { return passport_back; }
        public void setPassport_back(String passport_back) { this.passport_back = passport_back; }
        public String getAadhar_front() { return aadhar_front; }
        public void setAadhar_front(String aadhar_front) { this.aadhar_front = aadhar_front; }
        public String getAadhar_back() { return aadhar_back; }
        public void setAadhar_back(String aadhar_back) { this.aadhar_back = aadhar_back; }
        public String getVoterid_front() { return voterid_front; }
        public void setVoterid_front(String voterid_front) { this.voterid_front = voterid_front; }
        public String getVoterid_back() { return voterid_back; }
        public void setVoterid_back(String voterid_back) { this.voterid_back = voterid_back; }
        public String getDriving_license_front() { return driving_license_front; }
        public void setDriving_license_front(String driving_license_front) { this.driving_license_front = driving_license_front; }
        public String getDriving_license_back() { return driving_license_back; }
        public void setDriving_license_back(String driving_license_back) { this.driving_license_back = driving_license_back; }


}

