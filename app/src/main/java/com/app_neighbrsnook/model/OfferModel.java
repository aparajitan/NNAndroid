package com.app_neighbrsnook.model;

public class OfferModel {
        String name;
        int image;
        int bg_color;
        String offer_percentage;

        public OfferModel(String name, String offer_percentage, int bg_color) {
            this.name = name;
            this.offer_percentage = offer_percentage;
            this.bg_color = bg_color;

        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public int getBg_color() {
            return bg_color;
        }

        public void setBg_color(int bg_color) {
            this.bg_color = bg_color;
        }

    public String getOffer_percentage() {
        return offer_percentage;
    }

    public void setOffer_percentage(String offer_percentage) {
        this.offer_percentage = offer_percentage;
    }
}


