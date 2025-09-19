package com.app_neighbrsnook.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.CommentLikeListAdapter;
import com.app_neighbrsnook.model.wall.Emojilistdatum;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class CommentLikeBottomSheetFragment  extends BottomSheetDialogFragment {

    //Shubham Upadte
    CommentLikeListAdapter commentLikeListAdapter;
    private Context context;
    List<Emojilistdatum> emojilistdata;

    public CommentLikeBottomSheetFragment(List<Emojilistdatum> emojilistdata) {
        this.emojilistdata = emojilistdata;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_emoji_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final RecyclerView recyclerView = view.findViewById(R.id.user_list_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        commentLikeListAdapter = new CommentLikeListAdapter(context, emojilistdata);
        recyclerView.setAdapter(commentLikeListAdapter);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(context, R.layout.fragment_like_bottom_sheet, null);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(contentView);
        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    @Override
    public int getTheme() {
        return R.style.CustomBottomSheetDialog;
    }
}
