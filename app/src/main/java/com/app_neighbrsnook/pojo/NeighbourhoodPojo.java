package com.app_neighbrsnook.pojo;

import java.util.List;

public class NeighbourhoodPojo {
    private boolean success;
    private List<Data> data;

    public boolean isSuccess() {
        return success;
    }

    public List<Data> getData() {
        return data;
    }

    public static class Data {
        private String name;
        private String id;

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }
    }
}
