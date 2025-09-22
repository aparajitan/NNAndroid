package com.app_neighbrsnook.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.app_neighbrsnook.CommentActivity;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.EmojiAdapter1;
import com.app_neighbrsnook.adapter.ViewPagerAdapter;
import com.app_neighbrsnook.intreface.EmojiCallBack;
import com.app_neighbrsnook.model.EmojiModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WallPostDetailFragment extends Fragment implements EmojiAdapter1.EmojiInterface {
    ImageView ic_back,share_imageview;
    RecyclerView emoji_rv;
    EmojiAdapter1 emojiAdapter1;
    EmojiCallBack ecb;
    RecyclerView.LayoutManager layoutManager1;
    Map<Integer, String> mapdata;
    List<EmojiModel> emojiList = new ArrayList<>();
    LinearLayout comment_ll;
//    ReactButton reactButton;
    Context context;
    ViewPager mViewPager;
    RelativeLayout emoji_ll;
    ViewPagerAdapter mViewPagerAdapter;
    int[] images = {R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.background_photo_testing,
            R.drawable.background_img_second, R.drawable.background_img_user};
    Uri uri = Uri.parse("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wall_post_detail, container, false);
        int value = getArguments().getInt("YourKey");
        ic_back = view.findViewById(R.id.cross_imageview);
        emoji_rv = view.findViewById(R.id.emoji_rv);
        comment_ll = view.findViewById(R.id.comment_ll);
//        reactButton = view.findViewById(R.id.reactButton);
        emoji_ll = view.findViewById(R.id.emoji_ll);
        share_imageview = view.findViewById(R.id.share_imageview);
        share_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
            }
            });

        mViewPager = view.findViewById(R.id.viewPagerMain);
        mViewPagerAdapter = new ViewPagerAdapter(getActivity(), images, uri);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(value);
        emoji_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                EmojiBottomSheetFragment bottomSheetDialog = EmojiBottomSheetFragment.newInstance();
//                bottomSheetDialog.show(getActivity().getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");

            }
        });

        comment_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CommentActivity.class);
                startActivity(i);
            }
        });



        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }

        });

//        reactButton.setReactions(FbReactions.reactions);
////        reactButton.setDefaultReaction(FbReactions.defaultReact);
//        reactButton.setEnableReactionTooltip(false);
//        reactButton.setBackgroundColor(Color.TRANSPARENT);
//        reactButton.setOnReactionChangeListener(new ReactButton.OnReactionChangeListener() {
//            @Override
//            public void onReactionChange(Reaction reaction) {
//                Log.d(TAG, "onReactionChange: " + reaction.getReactText());
//            }
//        });

//        reactButton.setOnReactionDialogStateListener(new ReactButton.OnReactionDialogStateListener() {
//            @Override
//            public void onDialogOpened() {
//                Log.d(TAG, "onDialogOpened");
//            }
//
//            @Override
//            public void onDialogDismiss() {
//                Log.d(TAG, "onDialogDismiss");
//            }
//        });
        layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        emoji_rv.setLayoutManager(layoutManager1);
        emoji_rv.setHasFixedSize(false);
        emojiAdapter1  = new EmojiAdapter1(getEmojimapData(), this);
        emoji_rv.setAdapter(emojiAdapter1);
        return view;
    }
    private List<EmojiModel> getEmojimapData()
    {
        mapdata = new HashMap<Integer, String>();
        mapdata.put(R.drawable.ic_angry, "Arsad");
        mapdata.put(R.drawable.ic_like, "sudhanshu");
        mapdata.put(R.drawable.ic_sad, "amit");
//        mapdata.put(R.drawable.ic_happy, "Raj");
//        mapdata.put(R.drawable.ic_sad, "Rakesh");
//        mapdata.put(R.drawable.ic_angry, "Amit");
//        mapdata.put(R.drawable.ic_like, "Ajit");
        for(Map.Entry<Integer, String> entry :mapdata.entrySet())
        {
            emojiList.add(new EmojiModel(entry.getKey(), entry.getValue()));
        }
        return emojiList;
    }


    @Override
    public void onEmojiClickListener(int pos) {
//        EmojiBottomSheetFragment bottomSheetDialog = EmojiBottomSheetFragment.newInstance();
//        bottomSheetDialog.show(getActivity().getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");

    }
}
