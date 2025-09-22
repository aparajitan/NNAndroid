package com.app_neighbrsnook.utils;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.app_neighbrsnook.model.wall.Listdatum;

import java.util.List;

public class MyDiffUtilsCallbacks extends DiffUtil.Callback {

//    ArrayList<Contact> oldContacts;
//    ArrayList<Contact> newContacts;
    List<Listdatum> oldContacts;
    List<Listdatum> newContacts;

    public MyDiffUtilsCallbacks(List<Listdatum> oldContacts, List<Listdatum> newContacts) {
        this.oldContacts = oldContacts;
        this.newContacts = newContacts;
    }

    @Override
    public int getOldListSize() {
        return oldContacts !=null ? oldContacts.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newContacts != null ? newContacts.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return true;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
//        int result = newContacts.get(newItemPosition).getFavouritstatus().compareTo(oldContacts.get(oldItemPosition).getFavouritstatus());
        int result =0;
        return result == 0;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        Listdatum newContact = newContacts.get(newItemPosition);
        Listdatum oldContact = oldContacts.get(oldItemPosition);

        Bundle bundle = new Bundle();

//        if (!newContact.getFavouritstatus().equals(oldContact.getFavouritstatus())){
//            bundle.putString("name",newContact.getFavouritstatus());
//        }

//        if (!newContact.getMobile().equals(oldContact.getMobile())){
//            bundle.putString("mobile",newContact.getMobile());
//        }

        if (bundle.size()==0){
            return null;
        }
        return bundle;
    }
}