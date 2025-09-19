package com.app_neighbrsnook.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.app_neighbrsnook.model.BusinessModel;
import com.app_neighbrsnook.model.ChatModel;
import com.app_neighbrsnook.model.CreateEventModule;
import com.app_neighbrsnook.model.GroupModelP;
import com.app_neighbrsnook.model.CommentModel;
import com.app_neighbrsnook.model.PollModel;
import com.app_neighbrsnook.model.SellerChatModel;
import com.app_neighbrsnook.model.SuggestionModel;

import java.util.List;

@Dao
public interface DAO {

    @Insert
    void insertBusinessData(BusinessModel businessModel);
    @Query("SELECT * FROM business")
    List<BusinessModel> getAllBusinessList();

    @Insert
    void insertPollData(PollModel pollModel);
    @Query("SELECT * FROM pollModel")
    List<PollModel> getAllPollList();

    @Insert
    void insertGroupData(GroupModelP groupModelP);
    @Query("SELECT * FROM groupModel")
    List<GroupModelP> getAllGroupList();


    @Insert
    void insertSuggestionData(SuggestionModel suggestionModel);
    @Query("SELECT * FROM suggestionModels")
    List<SuggestionModel> getallSuggestionList();

    @Insert
    void  insertEvent(CreateEventModule createEventModule);
    @Query("SELECT * FROM createModule")
    List<CreateEventModule>getAllCreateEvent();


    @Insert
    void insertCommentData(CommentModel commentModel);

    @Query("SELECT * FROM commentModel")
    List<CommentModel> getAllCommentList();

    @Insert
    void insertChatData(ChatModel chatModel);

    @Query("SELECT * FROM ChatModel")
    List<ChatModel> getAllChatList();



    @Insert
    void insertSellerChatData(SellerChatModel sellerChatModel);

    @Query("SELECT * FROM sellerChatModel")
    List<SellerChatModel> getAllSellerChatList();


}