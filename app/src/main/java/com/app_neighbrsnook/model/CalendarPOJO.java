package com.app_neighbrsnook.model;


import java.time.LocalDate;

public class CalendarPOJO {
   public LocalDate localDate;
   public Boolean isSelected;

   public CalendarPOJO(LocalDate localDate,Boolean isSelected){
       this.localDate=localDate;
       this.isSelected=isSelected;
   }
}


