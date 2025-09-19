package com.app_neighbrsnook.pojo;

public class Day {
        private String name;
        private boolean isChecked;

    public Day(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}


//   if(selectedItems.set(position, !selectedItems.get(position)) == true)
//           {
//           closing_day = emailsList.get(position);
//           }
//           else
//           {
//
//           }
//           closingday.add(closing_day);
//           Log.d("closingday data", closingday.toString());
