package com.app_neighbrsnook.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.EmojiListAdapter;
import com.app_neighbrsnook.model.EmojiModel;
import com.app_neighbrsnook.model.wall.Emojilistdatum;

import java.util.ArrayList;
import java.util.List;

public class EmojiBottomSheetFragment extends BottomSheetDialogFragment {
    EmojiListAdapter emojiListAdapter;
    List<EmojiModel> emojiList1 = new ArrayList<>();
    List<Emojilistdatum> emojiList = new ArrayList<>();

    public EmojiBottomSheetFragment(List<Emojilistdatum> pos) {
        this.emojiList = pos;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_emoji_bottom_sheet, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RecyclerView recyclerView = view.findViewById(R.id.user_list_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        emojiListAdapter = new EmojiListAdapter(emojiList);
        recyclerView.setAdapter(emojiListAdapter);

    }


    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.fragment_like_bottom_sheet, null);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.setContentView(contentView);
        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    @Override public int getTheme() {
        return R.style.CustomBottomSheetDialog;
    }



//    private List<EmojiModel> getEmojiData()
//    {
//        emojiList.add(new EmojiModel(R.drawable.ic_angry, "Arsad"));
//        emojiList.add(new EmojiModel(R.drawable.ic_like, "sudhanshu"));
//        emojiList.add(new EmojiModel(R.drawable.ic_sad, "amit"));
//        emojiList.add(new EmojiModel(R.drawable.ic_happy, "Raj"));
//        emojiList.add(new EmojiModel(R.drawable.ic_sad, "Rakesh"));
//        emojiList.add(new EmojiModel(R.drawable.ic_angry, "Amit"));
//        emojiList.add(new EmojiModel(R.drawable.ic_like, "Ajit"));
//
//
//        return emojiList;
//    }

}
