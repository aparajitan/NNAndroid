package com.app_neighbrsnook.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.app_neighbrsnook.model.BusinessModel;
import com.app_neighbrsnook.model.BuyerChatModel;
import com.app_neighbrsnook.model.ChatModel;
import com.app_neighbrsnook.model.CreateEventModule;
import com.app_neighbrsnook.model.GroupModelP;
import com.app_neighbrsnook.model.PollModel;

import com.app_neighbrsnook.model.CommentModel;
import com.app_neighbrsnook.model.SellerChatModel;
import com.app_neighbrsnook.model.SuggestionModel;


@androidx.room.Database(entities = { BusinessModel.class, PollModel.class, CommentModel.class,GroupModelP.class,
        ChatModel.class,SuggestionModel.class, CreateEventModule.class,
        SellerChatModel.class, BuyerChatModel.class},
        version = 3,
        exportSchema = false)

public abstract class Database extends RoomDatabase {

    private static Database db = null;

    public abstract DAO getDao();

    public static Database createDBInstance(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(
                    context.getApplicationContext(),
                    Database.class, "my_advisor"
            )
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return db;
    }
}
