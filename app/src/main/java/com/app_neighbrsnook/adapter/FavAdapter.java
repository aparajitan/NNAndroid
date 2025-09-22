package com.app_neighbrsnook.adapter;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.app_neighbrsnook.profile.MyProfileOtherUser;
import com.app_neighbrsnook.utils.EmojiUtil;
import com.bumptech.glide.Glide;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.model.wall.Emojilistdatum;
import com.app_neighbrsnook.model.wall.Listdatum;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class FavAdapter extends RecyclerView.Adapter {
    private List<Listdatum> mList;
    FavAdapter.WallRequest newRequest;
    FragmentActivity activity;
    private final WallChildAdapter.ImageCallBack icb;
    EmojiAdapter.EmojiCallBack ecb;
    LinearSnapHelper snapHelper = new LinearSnapHelper();
    private final BusinessWallChildAdapter.ImageCallBack bicb;
    Context context;
    List<Emojilistdatum> emojiList = new ArrayList<>();
    private boolean isLongPress = false;
    private boolean showContact=true;
    public FavAdapter(List<Listdatum> list, FragmentActivity activity, WallChildAdapter.ImageCallBack icb,BusinessWallChildAdapter.ImageCallBack bicb, EmojiAdapter.EmojiCallBack ecb, FavAdapter.WallRequest newRequest) {
        this.mList = list;
        this.activity = activity;
        this.icb = icb;
        this.newRequest = newRequest;
        this.ecb = ecb;
        this.bicb = bicb;
    }
    public void setShowContact(boolean showContact) {
        this.showContact = showContact;
        if (isLongPress){
            notifyDataSetChanged();
}
}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case 10:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_row, parent, false);
                context = parent.getContext();
                return new FavAdapter.BusinessViewHolder(view);
            case 20:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poll_row, parent, false);
                return new FavAdapter.PollViewHolder(view);
//
            case 30:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_row, parent, false);
                return new FavAdapter.GroupViewHolder(view);
//
            case 40:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wall_row, parent, false);
                return new FavAdapter.WallViewHolder(view);
//
            case 60:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_row, parent, false);
                context = parent.getContext();
                return new FavAdapter.EventViewHolder(view);
//
            case 50:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sponsor_row, parent, false);
                context = parent.getContext();
                return new FavAdapter.SponsorViewHolder(view);

            case 70:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_row, parent, false);
                return new FavAdapter.AnnouncementViewHolder(view);

            case 80:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.welcome_row, parent, false);
                return new FavAdapter.WelcomeViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (mList.get(position).getType() != null) {
            switch (mList.get(position).getType()) {

                case "Business":
                    ((FavAdapter.BusinessViewHolder) holder).businessName.setText(mList.get(position).getBusinessName());
                 //   ((FavAdapter.BusinessViewHolder) holder).rate_now_textview.setText(String.valueOf(mList.get(position).getRating()));
                    if (!mList.get(position).getRating().equals("0.0")) {
                        ((FavAdapter.BusinessViewHolder) holder).rate_now_textview.setText(mList.get(position).getRating());
                    } else {
                        ((FavAdapter.BusinessViewHolder) holder).rate_now_textview.setText("--");
    }
                    try {
                        ((FavAdapter.BusinessViewHolder) holder).businessDescription.setText(mList.get(position).getBusinessDesc());
                        ((FavAdapter.BusinessViewHolder) holder).business_tagline.setText(mList.get(position).getBusinessTagline());
                        ((FavAdapter.BusinessViewHolder) holder).user_tv.setText(mList.get(position).getUsername());
                        ((FavAdapter.BusinessViewHolder) holder).created_date_tv.setText(mList.get(position).getNeighborhood() + ", " + mList.get(position).getCreatedOn());
                        Picasso.get().load(mList.get(position).getUserpic()).fit().centerCrop().error(R.drawable.profile).placeholder(R.drawable.profile)
                                .into(((FavAdapter.BusinessViewHolder) holder).profile_imageview);

                    } catch (Exception e) {

                    }

                    RecyclerView.LayoutManager layoutManagerBusiness = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                    ((BusinessViewHolder) holder).recyclerView.setLayoutManager(layoutManagerBusiness);
                   // ((BusinessViewHolder) holder).recyclerView.setHasFixedSize(false);
                    ((BusinessViewHolder) holder).recyclerView.setHasFixedSize(false);

                    if ((mList.get(position).getBusinessImage() != null && mList.get(position).getBusinessImage().size() != 0)) {
                        BusinessWallChildAdapter businessWallChildAdapter = new BusinessWallChildAdapter(Integer.parseInt(mList.get(position).getbId()), mList.get(position).getBusinessImage(), ((BusinessViewHolder) holder).recyclerView.getContext(), bicb);
                        ((BusinessViewHolder) holder).recyclerView.setAdapter(businessWallChildAdapter);
                        /*LinearSnapHelper snapHelper = new LinearSnapHelper();
                        snapHelper.attachToRecyclerView( ((BusinessViewHolder) holder).recyclerView);*/
                    }

                    ((FavAdapter.BusinessViewHolder) holder).threedot_imageview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            newRequest.onThreeDotClick(mList, position);
                        }
                    });
                    ((FavAdapter.BusinessViewHolder) holder).root.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            newRequest.onBusinessDetail(position, Integer.parseInt(mList.get(position).getbId()));
                        }
                    });
                    ((FavAdapter.BusinessViewHolder) holder).rate_now_textview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            newRequest.onClickRateNow(position);
                            newRequest.onClickRateNow(mList.get(position).getbId());
                        }
                    });

                    break;

                case "Poll":
                    try {
                        ((FavAdapter.PollViewHolder) holder).question_tv.setText(mList.get(position).getPollQuestion());
                        ((FavAdapter.PollViewHolder) holder).end_date_value_tv.setText(mList.get(position).getPollEndDate());
                        ((FavAdapter.PollViewHolder) holder).start_date_value_tv.setText(mList.get(position).getPollStartDate());
                        ((FavAdapter.PollViewHolder) holder).user_tv.setText(mList.get(position).getUsername());
                        ((FavAdapter.PollViewHolder) holder).vote_count_tv.setText(mList.get(position).getTotalvote() + " Vote");
                        ((FavAdapter.PollViewHolder) holder).created_date_tv.setText(mList.get(position).getNeighborhood() + ", " + mList.get(position).getCreatedOn());

                    } catch (Exception e) { }

                    if (mList.get(position).getIspollrunning().equals("1")) {
                        if (mList.get(position).getIsvoted().equals("1")) {
                            ((FavAdapter.PollViewHolder) holder).voted_tv.setVisibility(View.GONE);
                            ((FavAdapter.PollViewHolder) holder).voted1_tv.setVisibility(View.VISIBLE);
                        } else {
                            ((FavAdapter.PollViewHolder) holder).voted_tv.setVisibility(View.VISIBLE);
                            ((FavAdapter.PollViewHolder) holder).voted1_tv.setVisibility(View.GONE);
                        }

                    } else { }

                    Picasso.get().load(mList.get(position).getUserpic()).into(((FavAdapter.PollViewHolder) holder).profile_imageview);
                    if (mList.get(position).getUserpic().isEmpty()) {
                        ((FavAdapter.PollViewHolder) holder).profile_imageview.setImageResource(R.drawable.profile);
                    } else {
                        Picasso.get().load(mList.get(position).getUserpic()).fit().centerCrop().error(R.drawable.profile).placeholder(R.drawable.profile)
                                .into(((FavAdapter.PollViewHolder) holder).profile_imageview);
                    }

                    ((PollViewHolder) holder).threedot_imageview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            newRequest.onThreeDotClick(mList, position);
                        }
                    });

                    ((FavAdapter.PollViewHolder) holder).root.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            newRequest.onDetailPage(position, Integer.parseInt(mList.get(position).getPollid()), mList.get(position).getIsvoted(), mList.get(position).getIspollrunning());
                        }
                    });

                    break;

                case "Group":

                    try {
                        ((FavAdapter.GroupViewHolder) holder).group_name_tv.setText(mList.get(position).getGroup_name());
                        ((FavAdapter.GroupViewHolder) holder).group_desc_tv.setText(mList.get(position).getGroup_description());
                        ((FavAdapter.GroupViewHolder) holder).user_tv.setText(mList.get(position).getUsername());
                        ((FavAdapter.GroupViewHolder) holder).group_type_tv.setText(mList.get(position).getGroup_type());
                        ((FavAdapter.GroupViewHolder) holder).created_date_tv.setText(mList.get(position).getNeighborhood() + ", " + mList.get(position).getCreatedOn());
                        if (mList.get(position).getUserpic().isEmpty()) {
                            ((FavAdapter.GroupViewHolder) holder).profile_imageview.setImageResource(R.drawable.profile);
                        } else {
                            Picasso.get().load(mList.get(position).getUserpic()).fit().centerCrop().error(R.drawable.profile).placeholder(R.drawable.profile)
                                    .into(((FavAdapter.GroupViewHolder) holder).profile_imageview);
                        }

                        if (mList.get(position).getGroupImage().isEmpty()) {

//                            GlobalMethods.getInstance(context).getInitialBitmap(((GroupViewHolder) holder).profile_imageview, mList.get(position).getUsername());
                            ((FavAdapter.GroupViewHolder) holder).imgHomeGroup.setImageResource(R.drawable.group_default_image);
                        } else {
                            Picasso.get().load(mList.get(position).getGroupImage()).fit().centerCrop().error(R.drawable.marketplace_white_background).placeholder(R.drawable.group_default_image)
                                    .into(((FavAdapter.GroupViewHolder) holder).imgHomeGroup);
         }

                    } catch (Exception e) { }

                    ((GroupViewHolder) holder).threedot_imageview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            newRequest.onThreeDotClick(mList, position);
                        }
                    });
                    ((FavAdapter.GroupViewHolder) holder).root.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            newRequest.onGroupClick(Integer.parseInt(mList.get(position).getGroupid()), mList.get(position).getGroup_type(), mList.get(position).getGetjoin());
                        }
                    });

                    break;
//
                case "Post":
                    ((WallViewHolder) holder).post_type.setText(mList.get(position).getPost_type());
                    ((WallViewHolder) holder).post_msg.setText(mList.get(position).getPost_message());
                    ((WallViewHolder) holder).reaction_count.setText(String.valueOf(mList.get(position).getTotalEmojis()));
                    ((WallViewHolder) holder).comment_textview.setText(String.valueOf(mList.get(position).getTotcomment()));
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                    ((WallViewHolder) holder).wall_image_rv.setLayoutManager(layoutManager);
                    ((WallViewHolder) holder).wall_image_rv.setHasFixedSize(false);
                    if ((mList.get(position).getPostImages() != null && mList.get(position).getPostImages().size() != 0)) {
                        WallChildAdapter childRecyclerViewAdapter = new WallChildAdapter(mList.get(position).getPostImages(), ((WallViewHolder) holder).wall_image_rv.getContext(), icb);
                        ((WallViewHolder) holder).wall_image_rv.setAdapter(childRecyclerViewAdapter);
                        ((WallViewHolder) holder).wall_image_rv.setVisibility(View.VISIBLE);
                        snapHelper.attachToRecyclerView(((WallViewHolder) holder).wall_image_rv);
                    } else {
                        ((WallViewHolder) holder).wall_image_rv.setVisibility(View.GONE);
                    }
                    ((WallViewHolder) holder).user_tv.setText(mList.get(position).getUsername());
                    ((WallViewHolder) holder).created_date_tv.setText(mList.get(position).getNeighborhood() + ", " + mList.get(position).getCreatedOn());
                    if (mList.get(position).getUserpic().isEmpty()) {
                        ((WallViewHolder) holder).profile_imageview.setImageResource(R.drawable.profile);
                    } else {
                        Picasso.get().load(mList.get(position).getUserpic()).fit().centerCrop().error(R.drawable.profile).placeholder(R.drawable.profile)
                                .into(((FavAdapter.WallViewHolder) holder).profile_imageview);
                    }
                    if (mList.get(position).getFavouritstatus()== 0){
                        ((WallViewHolder) holder).iv_favorite.setImageResource(R.drawable.svg_bookmark_outline);
                    } else {
                        ((WallViewHolder) holder).iv_favorite.setImageResource(R.drawable.svg_bookmark_fill);


                    }
                    if (!showContact) {
                        ((WallViewHolder) holder).reactions_card.setVisibility(View.GONE);
                        isLongPress = false;
                    }
                    emojiList = mList.get(position).getEmojilistdata();

                    RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                    ((WallViewHolder) holder).emoji_rv.setLayoutManager(layoutManager1);
                    ((WallViewHolder) holder).emoji_rv.setHasFixedSize(false);
                    EmojiAdapter emojiAdapter = new EmojiAdapter(emojiList, ((WallViewHolder) holder).emoji_rv.getContext(), ecb);
                    ((WallViewHolder) holder).emoji_rv.setAdapter(emojiAdapter);

                    if (mList.get(position).getTotallike().equals("0")) {
                        //Shubham Upadte
                        ((WallViewHolder) holder).reaction_count.setText("");
                    } else {
                        ((WallViewHolder) holder).reaction_count.setText(mList.get(position).getTotallike());
                    }
                    if (mList.get(position).getPostlike().equals("1")) {
                        //Shubham Upadte
                        if (mList.get(position).getPostemojiunicode() != null && !mList.get(position).getPostemojiunicode().equals("")) {
                            Bitmap emojiBitmap = EmojiUtil.getEmojiBitmap(activity.getApplicationContext(), mList.get(position).getPostemojiunicode(), 32);
                            ((WallViewHolder) holder).reactButton.setImageBitmap(emojiBitmap);
                        } else {
                            ((WallViewHolder) holder).reactButton.setImageResource(R.drawable.baseline_favorite_24);
                        }
                    } else {
                        ((WallViewHolder) holder).reactButton.setImageResource(R.drawable.baseline_favorite_border_24);
                    }

                    ((WallViewHolder) holder).rl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            newRequest.onClickImage(position);
                        }
                    });
                    ((WallViewHolder) holder).comment_ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            newRequest.onClickComment(mList, mList.get(position).getPostid(), 0);
                        }
                    });

                    ((WallViewHolder) holder).ll_favorite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            newRequest.onFavSelect(Integer.parseInt(mList.get(position).getPostid()), mList.get(position).getNeighborhood(), mList.get(position).getFavouritstatus(), position);
                        }


                    });
                    ((WallViewHolder) holder).threedot_imageview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            newRequest.onThreeDotClick(mList, position);
                        }
                    });
                    ((WallViewHolder) holder).share_imageview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            newRequest.onClickShare(position);
                        }
                    });
                    ((WallViewHolder) holder).root.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            ((WallViewHolder) holder).reactions_card.setVisibility(View.GONE);
                        }
                    });
                    ((WallViewHolder) holder).ll_reactCount.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (mList.get(position).getTotalEmojis() != 0) {

                                newRequest.onClickEmoji(Integer.parseInt(mList.get(position).getPostid()));
                            }
                        }
                    });
                    ((WallViewHolder) holder).ll_reactButton.setOnTouchListener(new View.OnTouchListener() {
                        //Shubham Upadte
                     //   private boolean isLongPress = false;
                        private Handler handler = new Handler();
                        private Runnable longPressRunnable = new Runnable() {
                            @Override
                            public void run() {
                                isLongPress = true;
                                ((WallViewHolder) holder).reactions_card.setVisibility(View.VISIBLE);
                            }
                        };

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    isLongPress = false;
                                    handler.postDelayed(longPressRunnable, 500);
                                    return true;
                                case MotionEvent.ACTION_UP:
                                    handler.removeCallbacks(longPressRunnable);
                                    if (!isLongPress) {
                                        toggleLike(((WallViewHolder) holder), position, "❤");
                                    } else {
                                        ((WallViewHolder) holder).reactionsVisible = true;
                                    }
                                    return true;
                            }
                            return false;
                        }
                    });

                    //Shubham Upadte
                    setReactionClickListener(((WallViewHolder) holder).reactionLike, "👍", ((WallViewHolder) holder), position);
                    setReactionClickListener(((WallViewHolder) holder).reactionLove, "❤", ((WallViewHolder) holder), position);
                    setReactionClickListener(((WallViewHolder) holder).reactionHaha, "😂", ((WallViewHolder) holder), position);
                    setReactionClickListener(((WallViewHolder) holder).reactionWow, "😮", ((WallViewHolder) holder), position);
                    setReactionClickListener(((WallViewHolder) holder).reactionSad, "🥳", ((WallViewHolder) holder), position);
                    setReactionClickListener(((WallViewHolder) holder).reactionAngry, "😎", ((WallViewHolder) holder), position);

                    break;

                case "Event":

                    ((FavAdapter.EventViewHolder) holder).mTitle.setText(mList.get(position).getEvent_name());
                    ((FavAdapter.EventViewHolder) holder).mDescription.setText(mList.get(position).getEvent_description());
                    ((FavAdapter.EventViewHolder) holder).user_tv.setText(mList.get(position).getUsername());
                    ((FavAdapter.EventViewHolder) holder).created_date_tv.setText(mList.get(position).getNeighborhood() + ", " + mList.get(position).getCreatedOn());
                    ((FavAdapter.EventViewHolder) holder).start_date_value_tv.setText(mList.get(position).getEvent_start_date());
                    ((FavAdapter.EventViewHolder) holder).end_date_value_tv.setText(mList.get(position).getEvent_end_date());
                    try {
                        if (mList.get(position).getUserpic().isEmpty()) {
                            ((FavAdapter.EventViewHolder) holder).profile_imageview.setImageResource(R.drawable.profile);
                        } else {
                            Picasso.get().load(mList.get(position).getUserpic()).fit().centerCrop().error(R.drawable.profile).placeholder(R.drawable.profile)
                                    .into(((FavAdapter.EventViewHolder) holder).profile_imageview);
                        }

                        if (mList.get(position).getEventCoverImage().isEmpty()) {
                            ((FavAdapter.EventViewHolder) holder).imgHomeEvent.setImageResource(R.drawable.event_background_wall);
                        } else {
                            Picasso.get().load(mList.get(position).getEventCoverImage()).fit().centerCrop().error(R.drawable.marketplace_white_background).placeholder(R.drawable.event_background_wall)
                                    .into(((FavAdapter.EventViewHolder) holder).imgHomeEvent);
         }

                    } catch (Exception e) { }

                    ((FavAdapter.EventViewHolder) holder).profile_ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(context, MyProfileOtherUser.class);
                            i.putExtra("user_id", Integer.parseInt(mList.get(position).getCreatedby()));
                            context.startActivity(i);
                        }
                    });
                    ((FavAdapter.EventViewHolder) holder).root.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            newRequest.onEventClick(Integer.parseInt(mList.get(position).getEventid()));

                        }
                    });
                    ((FavAdapter.EventViewHolder) holder).profile_ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(context, MyProfileOtherUser.class);
                            i.putExtra("user_id", Integer.parseInt(mList.get(position).getCreatedby()));
                            context.startActivity(i);
                        }
                    });
                    ((EventViewHolder) holder).threedot_imageview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            newRequest.onThreeDotClick(mList, position);

                        }
                    });

                    break;

                case "Sponsor":

                    break;

                case "Welcome":

                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 0;
        } else {
            return mList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mList != null) {
            Listdatum wallPojo = mList.get(position);
            if (wallPojo != null) {
                if (mList.get(position).getType().equals("Business"))
                    return 10;
                else if (mList.get(position).getType().equals("Poll"))
                    return 20;
                else if (mList.get(position).getType().equals("Group"))
                    return 30;
                else if (mList.get(position).getType().equals("Post"))
                    return 40;
                else if (mList.get(position).getType().equals("Sponsor"))
                    return 50;
                else if (mList.get(position).getType().equals("Event"))
                    return 60;
                else if (mList.get(position).getType().equals("Announcement"))
                    return 70;
                else if (mList.get(position).getType().equals("Welcome"))
                    return 80;
            }

        }

        return 0;
    }

    public static class CityViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTitle;

        public CityViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.titleTextView);
        }
    }

    public class BusinessViewHolder extends RecyclerView.ViewHolder {
        ImageView threedot_imageview, profile_imageview, porduct_image, imageview1;
        TextView businessName, user_tv, created_date_tv, end_date_value_tv, start_date_value_tv;
        TextView businessType, business_tagline;
        TextView businessDescription, rate_now_textview, write_review_textview, read_review_textview;
        LinearLayout root;
        RecyclerView recyclerView;


        public BusinessViewHolder(@NonNull View itemView) {
            super(itemView);
            threedot_imageview = itemView.findViewById(R.id.threedot_imageview);
            businessName = itemView.findViewById(R.id.business_name_textview);
            businessType = itemView.findViewById(R.id.business_type_textview);
            businessDescription = itemView.findViewById(R.id.business_desc_textview);
            business_tagline = itemView.findViewById(R.id.business_tagline);
            rate_now_textview = itemView.findViewById(R.id.rate_now_textview);
//            read_review_textview= itemView.findViewById(R.id.read_review_textview);
            write_review_textview = itemView.findViewById(R.id.write_review_textview);
            profile_imageview = itemView.findViewById(R.id.profile_imageview);
            created_date_tv = itemView.findViewById(R.id.created_date_tv);
            end_date_value_tv = itemView.findViewById(R.id.end_date_value_tv);
            start_date_value_tv = itemView.findViewById(R.id.start_date_value_tv);
            porduct_image = itemView.findViewById(R.id.porduct_image);
            user_tv = itemView.findViewById(R.id.user_tv);
            root = itemView.findViewById(R.id.root);
            imageview1 = itemView.findViewById(R.id.imageview1);
            recyclerView = itemView.findViewById(R.id.rv_business_list);

        }

    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle, created_date_tv, event_venue_tv, user_tv, start_date_value_tv, end_date_value_tv;
        ImageView profile_imageview, threedot_imageview,imgHomeEvent;
        private final TextView mDescription;
        LinearLayout profile_ll, root;


        public EventViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.event_tv);
            imgHomeEvent = itemView.findViewById(R.id.imgHomeEvent);
            mDescription = itemView.findViewById(R.id.event_desc_textview);
            created_date_tv = itemView.findViewById(R.id.created_date_tv);
            user_tv = itemView.findViewById(R.id.user_tv);
            threedot_imageview = itemView.findViewById(R.id.threedot_imageview);
            profile_imageview = itemView.findViewById(R.id.profile_imageview);
            end_date_value_tv = itemView.findViewById(R.id.end_date_value_tv);
            start_date_value_tv = itemView.findViewById(R.id.start_date_value_tv);
            profile_ll = itemView.findViewById(R.id.profile_ll);
            root = itemView.findViewById(R.id.root);
        }
    }

    public class PollViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_imageview, threedot_imageview;
        TextView user_tv, created_date_tv, end_date_value_tv, question_tv, vote_count_tv, voted_tv, start_date_value_tv, voted1_tv;
        LinearLayout root;

        public PollViewHolder(@NonNull View itemView) {
            super(itemView);
            threedot_imageview = itemView.findViewById(R.id.threedot_imageview);
            profile_imageview = itemView.findViewById(R.id.profile_imageview);
            user_tv = itemView.findViewById(R.id.user_tv);
            created_date_tv = itemView.findViewById(R.id.created_date_tv);
            end_date_value_tv = itemView.findViewById(R.id.end_date_value_tv);
            question_tv = itemView.findViewById(R.id.question_tv);
            vote_count_tv = itemView.findViewById(R.id.vote_count_tv);
            voted_tv = itemView.findViewById(R.id.voted_tv);
            voted1_tv = itemView.findViewById(R.id.voted1_tv);
            start_date_value_tv = itemView.findViewById(R.id.start_date_value_tv);
            root = itemView.findViewById(R.id.root);

        }

    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_imageview, threedot_imageview,imgHomeGroup;
        TextView group_name_tv, created_date_tv, user_tv, group_desc_tv, group_type_tv;
        LinearLayout root;


        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            group_name_tv = itemView.findViewById(R.id.group_name_tv);
//            member_count_tv = itemView.findViewById(R.id.member_count_tv);
            imgHomeGroup = itemView.findViewById(R.id.imgHomeGroup);
            user_tv = itemView.findViewById(R.id.user_tv);
            group_desc_tv = itemView.findViewById(R.id.group_desc_tv);
            created_date_tv = itemView.findViewById(R.id.created_date_tv);
            threedot_imageview = itemView.findViewById(R.id.threedot_imageview);
            profile_imageview = itemView.findViewById(R.id.profile_imageview);
            root = itemView.findViewById(R.id.root);
            group_type_tv = itemView.findViewById(R.id.group_type_tv);
        }
    }

    public class SponsorViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_imageview, threedot_imageview;
        TextView group_name_tv, created_date_tv, member_count_tv, group_desc_tv;
        LinearLayout root;
        RecyclerView recyclerView;


        public SponsorViewHolder(@NonNull View itemView) {
            super(itemView);
            threedot_imageview = itemView.findViewById(R.id.threedot_imageview);
//            recyclerView = itemView.findViewById(R.id.sponsor_rv);
        }
    }

    public class WelcomeViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_imageview, threedot_imageview;
        TextView group_name_tv, created_date_tv, member_count_tv, group_desc_tv;
        LinearLayout root;
        RecyclerView recyclerView;


        public WelcomeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_imageview, threedot_imageview;
        TextView announcement_tv, created_date_tv, member_count_tv, group_desc_tv;
        LinearLayout root;
        RecyclerView recyclerView;


        public AnnouncementViewHolder(@NonNull View itemView) {
            super(itemView);
            announcement_tv = itemView.findViewById(R.id.announcement_tv);
            root = itemView.findViewById(R.id.root);
        }
    }
    public class WallViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_imageview, threedot_imageview, wall_image, like_imageview, share_imageview, reactButton,iv_favorite;
        TextView post_type, post_msg, created_date_tv, like_textview, comment_textview, user_tv, reaction_count;
        LinearLayout root, comment_ll, emoji_ll, ll_reactButton, ll_reactCount,ll_favorite;
        RelativeLayout rl;
        RecyclerView wall_image_rv, emoji_rv;
        private CardView reactions_card;
        ImageView reactionLike, reactionLove, reactionHaha, reactionWow, reactionSad, reactionAngry;
        boolean reactionsVisible = false;

        public WallViewHolder(@NonNull View itemView) {
            super(itemView);
            user_tv = itemView.findViewById(R.id.user_tv);
            post_type = itemView.findViewById(R.id.post_type_tv);
            post_msg = itemView.findViewById(R.id.post_msg_textview);
            ll_favorite = itemView.findViewById(R.id.ll_favorite);
            iv_favorite = itemView.findViewById(R.id.iv_favorite);
            created_date_tv = itemView.findViewById(R.id.created_date_tv);
            threedot_imageview = itemView.findViewById(R.id.threedot_imageview);
            comment_textview = itemView.findViewById(R.id.comment_textview);
            comment_ll = itemView.findViewById(R.id.comment_ll);
            share_imageview = itemView.findViewById(R.id.share_imageview);
            emoji_rv = itemView.findViewById(R.id.emoji_rv);
            emoji_ll = itemView.findViewById(R.id.emoji_ll);
            rl = itemView.findViewById(R.id.rl);
            wall_image_rv = itemView.findViewById(R.id.wall_image_rv);
            reactButton = itemView.findViewById(R.id.reactButton);
            reaction_count = itemView.findViewById(R.id.reaction_count);
            profile_imageview = itemView.findViewById(R.id.profile_imageview);
            reactions_card = itemView.findViewById(R.id.reactions_card);
            reactionLike = itemView.findViewById(R.id.reaction_like);
            reactionLove = itemView.findViewById(R.id.reaction_love);
            reactionHaha = itemView.findViewById(R.id.reaction_haha);
            reactionWow = itemView.findViewById(R.id.reaction_wow);
            reactionSad = itemView.findViewById(R.id.reaction_sad);
            reactionAngry = itemView.findViewById(R.id.reaction_angry);
            root = itemView.findViewById(R.id.root);
            ll_reactButton = itemView.findViewById(R.id.ll_react_button);
            ll_reactCount = itemView.findViewById(R.id.ll_react_count);
        }

    }

    public interface WallRequest {

        void onThreeDotClick(List<Listdatum> mList, int pos);

        void onDetailPage(int position, int pos, String isVoted, String ispollrunning);

        void onSetEmoji(int pos, String postlike, String reactionCode, int likePos);

        void onClickEmoji(int postid);
        void onClickRateNow(String businessId);
        void onClickComment(List<Listdatum> mList, String postId, int pos);

        void onClickShare(int pos);

        void onBusinessDetail(int pos, int bid);

        void onGroupClick(int id, String groupType, String getJoin);
        void onFavSelect(int postid, String neighbourhood, int favoriteStatus,int postPos);
        void onEventClick(int pos);
    }

    public void filterList(List<Listdatum> filterdNames) {
        this.mList = filterdNames;
        notifyDataSetChanged();
    }

    private void toggleLike(WallViewHolder holder, int pos, final String reactionCode) {
        if (mList.get(pos).getPostlike().equals("0")) {

            newRequest.onSetEmoji(Integer.parseInt(mList.get(pos).getPostid()), "1", "", pos);

        } else {

            newRequest.onSetEmoji(Integer.parseInt(mList.get(pos).getPostid()), "0", "", pos);

        }
    }

    private void setReactionClickListener(ImageView reaction, final String reactionCode, final WallViewHolder holder, final int pos) {
        reaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.reactions_card.setVisibility(View.GONE);
                holder.reactionsVisible = false;
                newRequest.onSetEmoji(Integer.parseInt(mList.get(pos).getPostid()), "1", reactionCode, pos);
            }
        });
    }

    public void updateItemText(int position, String totalLike, String likeStatus, String emojiUnicode) {
        mList.get(position).setTotallike(totalLike);
        mList.get(position).setTotalEmojis(Integer.valueOf(totalLike));
        mList.get(position).setPostlike(likeStatus);
        mList.get(position).setPostemojiunicode(emojiUnicode);
        notifyItemChanged(position);
    }

    public void updatePostItem(int position, int favoriteStaus) {
        mList.get(position).setFavouritstatus(favoriteStaus);
        notifyItemChanged(position);


    }
    public void updateData(List<Listdatum> newData) {
        this.mList = newData;
    }
}
