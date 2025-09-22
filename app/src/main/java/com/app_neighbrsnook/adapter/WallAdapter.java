package com.app_neighbrsnook.adapter;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.app_neighbrsnook.MainActivity;
import com.app_neighbrsnook.event.EventAllListCurrentData;
import com.app_neighbrsnook.event.UserViewEvent;
import com.app_neighbrsnook.model.wall.BouquetDataModel;
import com.app_neighbrsnook.model.wall.LikeDataModel;
import com.app_neighbrsnook.profile.MyProfile;
import com.app_neighbrsnook.profile.MyProfileOtherUser;
import com.app_neighbrsnook.utils.EmojiUtil;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.app_neighbrsnook.libraries.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.WebViewActivity;
import com.app_neighbrsnook.model.wall.Emojilistdatum;
import com.app_neighbrsnook.model.wall.Listdatum;
import com.app_neighbrsnook.utils.MyDiffUtilsCallbacks;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class WallAdapter extends RecyclerView.Adapter {
    private List<Listdatum> mList = new ArrayList();
    /*public static final int CITY_TYPE = 0;
    public static final int POLL_TYPE = 1;
    public static final int EVENT_TYPE = 2;
    public static final int GROUP_TYPE = 3;
    public static final int WALL_TYPE = 4;
    public static final int SPONSOR_TYPE = 5;
    public static final int ANNOUNCEMENT_TYPE = 6;
    public static final int WELCOME_TYPE = 7;*///05/may2024
    WallRequest newRequest;
    FragmentActivity activity;
    LinearSnapHelper snapHelper = new LinearSnapHelper();
    SharedPrefsManager sm;
    private final WallChildAdapter.ImageCallBack icb;
    EmojiAdapter.EmojiCallBack ecb;
    Context context;
    int itemPosition;
    ReactionAdapter.ReactionInterface reactionInterface;
    List<Emojilistdatum> emojiList = new ArrayList<>();
    boolean isCheck = true;
    boolean isUserVerified;
    private int likeStatus = 0;
    private static final int currentPage = 0;
    private static final int NUM_PAGES = 0;
    private final BusinessWallChildAdapter.ImageCallBack bicb;
    private boolean isLongPress = false;
    private boolean showContact=true;
    private int cardPosNo =-1;
    public WallAdapter(List<Listdatum> list, FragmentActivity activity, WallChildAdapter.ImageCallBack icb, BusinessWallChildAdapter.ImageCallBack bicb, EmojiAdapter.EmojiCallBack ecb, WallRequest newRequest, ReactionAdapter.ReactionInterface ReactionInterface, boolean isUserVerified) {
        this.mList = list;
        this.activity = activity;
        this.icb = icb;
        this.newRequest = newRequest;
        this.ecb = ecb;
        this.bicb = bicb;
        this.reactionInterface = ReactionInterface;
        this.isUserVerified = isUserVerified;
    }
    public void setShowContact(boolean showContact) {
        this.showContact = showContact;
        if (isLongPress){
            notifyItemChanged(cardPosNo);
}
}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case 10:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_row, parent, false);
                context = parent.getContext();
                return new BusinessViewHolder(view);
            case 20:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poll_row, parent, false);
                context = parent.getContext();
                return new PollViewHolder(view);
            case 30:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_row, parent, false);
                context = parent.getContext();
                return new GroupViewHolder(view);
            case 40:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wall_row, parent, false);
                context = parent.getContext();
                return new WallViewHolder(view);
//
            case 60:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_row, parent, false);
                context = parent.getContext();
                return new EventViewHolder(view);
//
            case 50:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sponsor_row, parent, false);
                context = parent.getContext();
                return new SponsorViewHolder(view);

            case 70:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_row, parent, false);
                return new AnnouncementViewHolder(view);

            //change to wall row to wall welcome row

            case 80:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wall_welcome_row, parent, false);
                return new WelcomeViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (mList.get(position).getType() != null) {
            switch (mList.get(position).getType()) {

                case "Business":
                    ((BusinessViewHolder) holder).businessName.setText(mList.get(position).getBusinessName());
                    //((BusinessViewHolder) holder).rate_now_textview.setText(mList.get(position).getRating());
//                    ((BusinessViewHolder) holder).read_review_textview.setText(String.valueOf(mList.get(position).getReview()));
                    if (!mList.get(position).getRating().equals("0.0")) {
                        ((BusinessViewHolder) holder).rate_now_textview.setText(mList.get(position).getRating());
                    } else {
                        ((BusinessViewHolder) holder).rate_now_textview.setText("--");
                    }
                    try {
                        ((BusinessViewHolder) holder).businessDescription.setText(mList.get(position).getBusinessDesc());
                        ((BusinessViewHolder) holder).business_tagline.setText(mList.get(position).getBusinessTagline());
                        ((BusinessViewHolder) holder).user_tv.setText(mList.get(position).getUsername());
                        ((BusinessViewHolder) holder).created_date_tv.setText(mList.get(position).getNeighborhood() + ", " + mList.get(position).getCreatedOn());
                        ((BusinessViewHolder) holder).businessType.setText(mList.get(position).getCategory());
                        Picasso.get().load(mList.get(position).getUserpic()).fit().centerCrop().error(R.drawable.marketplace_white_background).placeholder(R.drawable.marketplace_white_background)
                                .into(((BusinessViewHolder) holder).profile_imageview);
                    } catch (Exception e) {

                    }

                    RecyclerView.LayoutManager layoutManagerBusiness = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                    ((BusinessViewHolder) holder).recyclerView.setLayoutManager(layoutManagerBusiness);
                    ((BusinessViewHolder) holder).recyclerView.setHasFixedSize(false);

                    if ((mList.get(position).getBusinessImage() != null && mList.get(position).getBusinessImage().size() != 0)) {
                        BusinessWallChildAdapter childRecyclerViewAdapter = new BusinessWallChildAdapter(Integer.parseInt(mList.get(position).getbId()), mList.get(position).getBusinessImage(), ((BusinessViewHolder) holder).recyclerView.getContext(), bicb);
                        ((BusinessViewHolder) holder).recyclerView.setAdapter(childRecyclerViewAdapter);
                        //LinearSnapHelper snapHelper = new LinearSnapHelper();
                        snapHelper.attachToRecyclerView(((BusinessViewHolder) holder).recyclerView);
                    }

                    ((BusinessViewHolder) holder).threedot_imageview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isUserVerified) {
                                newRequest.onThreeDotClick(mList, position);
                            } else {
                                GlobalMethods.getInstance(context).globalDialog(context, context.getString(R.string.unverified_msg));
                            }
                        }
                    });

                    try {
                        if (mList.get(position).getBusinessImage().get(0).getImg().isEmpty()) {
                            Picasso.get().load("https://wallpapers.com/background/professional-photo-background-1920-x-1080-sr6crux4h7m30au5.html").placeholder(R.drawable.business_bg)
                                    .into(((BusinessViewHolder) holder).porduct_image);
                        } else {
                            Picasso.get().load(mList.get(position).getBusinessImage().get(0).getImg()).placeholder(R.drawable.business_bg)
                                    .into(((BusinessViewHolder) holder).porduct_image);

                            Glide.with(context).load(mList.get(position).getBusinessImage().get(0).getImg())
                                    .apply(bitmapTransform(new BlurTransformation(80)))
                                    .into(((BusinessViewHolder) holder).imageview1);
                        }
                    } catch (Exception e) {
                    }
                    ((BusinessViewHolder) holder).root.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            newRequest.onBusinessDetail(position, Integer.parseInt(mList.get(position).getbId()));
                        }
                    });
                    ((BusinessViewHolder) holder).rate_now_textview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            newRequest.onClickRateNow(position);
                            newRequest.onClickRateNow(mList.get(position).getbId());
                        }
                    });
                    ((BusinessViewHolder) holder).profile_ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isUserVerified) {
                                Intent i = new Intent(context, MyProfileOtherUser.class);
                                i.putExtra("user_id", Integer.parseInt(mList.get(position).getCreatedby()));
                                context.startActivity(i);
                            }
                        }
                    });

                    break;

                case "Poll":
                    try {

                        ((PollViewHolder) holder).question_tv.setText(mList.get(position).getPollQuestion());
                        ((PollViewHolder) holder).end_date_value_tv.setText(mList.get(position).getPollEndDate());
                        ((PollViewHolder) holder).start_date_value_tv.setText(mList.get(position).getPollStartDate());
                        ((PollViewHolder) holder).user_tv.setText(mList.get(position).getUsername());
                        ((PollViewHolder) holder).vote_count_tv.setText(mList.get(position).getTotalvote() + " Vote");
                        ((PollViewHolder) holder).created_date_tv.setText(mList.get(position).getNeighborhood() + ", " + mList.get(position).getCreatedOn());

                    } catch (Exception e) {

                    }

                    if (mList.get(position).getIspollrunning().equals("1")) {
                        if (mList.get(position).getIsvoted().equals("1")) {
                            ((PollViewHolder) holder).voted_tv.setVisibility(View.GONE);
                            ((PollViewHolder) holder).voted1_tv.setVisibility(View.VISIBLE);
                        } else {
                            ((PollViewHolder) holder).voted_tv.setVisibility(View.VISIBLE
                            );
                            ((PollViewHolder) holder).voted1_tv.setVisibility(View.GONE);
                        }

                    } else {

                    }
                    try {
                        Picasso.get().load(mList.get(position).getUserpic()).into(((PollViewHolder) holder).profile_imageview);
                        if (mList.get(position).getUserpic().isEmpty()) {
                            ((PollViewHolder) holder).profile_imageview.setImageResource(R.drawable.marketplace_white_background);
                        } else {
                            Picasso.get().load(mList.get(position).getUserpic()).fit().centerCrop().error(R.drawable.marketplace_white_background).placeholder(R.drawable.marketplace_white_background)
                                    .into(((PollViewHolder) holder).profile_imageview);
                        }
                    } catch (Exception e) {

                    }
                    sm = new SharedPrefsManager(context);
                    int id = Integer.parseInt(sm.getString("user_id"));
                    ((PollViewHolder) holder).profile_ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isUserVerified) {
                                if (id == Integer.parseInt(mList.get(position).getCreatedby())) {
                                    Intent i = new Intent(context, MyProfile.class);
                                    context.startActivity(i);
                                }else {

                                    Intent i = new Intent(context, MyProfileOtherUser.class);
                                    i.putExtra("user_id", Integer.parseInt(mList.get(position).getCreatedby()));
                                    context.startActivity(i);
                                }


                            }
                        }
                    });


                    ((PollViewHolder) holder).root.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            newRequest.onDetailPage(position, Integer.parseInt(mList.get(position).getPollid()), mList.get(position).getIsvoted(), mList.get(position).getIspollrunning());
                        }
                    });

                    ((PollViewHolder) holder).threedot_imageview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isUserVerified) {
                                newRequest.onThreeDotClick(mList, position);
                            } else {
                                GlobalMethods.getInstance(context).globalDialog(context, context.getString(R.string.unverified_msg));

                            }
                        }
                    });
//
                    break;
//
                case "Group":

                    try {
                        ((GroupViewHolder) holder).group_name_tv.setText(mList.get(position).getGroup_name());
                        ((GroupViewHolder) holder).group_desc_tv.setText(mList.get(position).getGroup_description());
                        ((GroupViewHolder) holder).user_tv.setText(mList.get(position).getUsername());
                        ((GroupViewHolder) holder).group_type_tv.setText(mList.get(position).getGroup_type() + " " + "Group");
                        ((GroupViewHolder) holder).created_date_tv.setText(mList.get(position).getNeighborhood() + ", " + mList.get(position).getCreatedOn());
                        if (mList.get(position).getUserpic().isEmpty()) {

//                            GlobalMethods.getInstance(context).getInitialBitmap(((GroupViewHolder) holder).profile_imageview, mList.get(position).getUsername());
                            ((GroupViewHolder) holder).profile_imageview.setImageResource(R.drawable.marketplace_white_background);
                        } else {
                            Picasso.get().load(mList.get(position).getUserpic()).fit().centerCrop().error(R.drawable.marketplace_white_background).placeholder(R.drawable.marketplace_white_background)
                                    .into(((GroupViewHolder) holder).profile_imageview);
                        }

                        if (mList.get(position).getGroupImage().isEmpty()) {

//                            GlobalMethods.getInstance(context).getInitialBitmap(((GroupViewHolder) holder).profile_imageview, mList.get(position).getUsername());
                            ((GroupViewHolder) holder).imgHomeGroup.setImageResource(R.drawable.group_img_app);
                        } else {
                            Picasso.get().load(mList.get(position).getGroupImage()).fit().centerCrop().error(R.drawable.marketplace_white_background).placeholder(R.drawable.group_img_app)
                                    .into(((GroupViewHolder) holder).imgHomeGroup);
                        }

                    } catch (Exception e) {

                    }

                    ((GroupViewHolder) holder).root.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            newRequest.onGroupClick(Integer.parseInt(mList.get(position).getGroupid()), mList.get(position).getGroup_type(), mList.get(position).getGetjoin(), mList.get(position).getCreatedby(), mList.get(position).getNeighborhood());
//                                                                                              int id, String groupType , String getjoin, String userId , String neighbourhood)
                        }
                    });
                    sm = new SharedPrefsManager(context);
                    int id1 = Integer.parseInt(sm.getString("user_id"));
                    ((GroupViewHolder) holder).profile_ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isUserVerified) {
                                if (id1 == Integer.parseInt(mList.get(position).getCreatedby())) {
                                    Intent i = new Intent(context, MyProfile.class);
                                    context.startActivity(i);
                                } else {
                                    Intent i = new Intent(context, MyProfileOtherUser.class);
                                    i.putExtra("user_id", Integer.parseInt(mList.get(position).getCreatedby()));
                                    context.startActivity(i);
                                }


                            }
                        }
                    });

                    ((GroupViewHolder) holder).threedot_imageview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isUserVerified) {
                                newRequest.onThreeDotClick(mList, position);
                            } else {
                                GlobalMethods.getInstance(context).globalDialog(context, context.getString(R.string.unverified_msg));
                            }
                        }
                    });


                    break;
//


                case "Post":

                    emojiList = mList.get(position).getEmojilistdata();

                    ((WallViewHolder) holder).post_type.setText(mList.get(position).getPost_type());
                    ((WallViewHolder) holder).post_msg.setText(mList.get(position).getPost_message());

                    if (mList.get(position).getTotallike().equals("0")) {
                        ((WallViewHolder) holder).reaction_count.setText("");
                    } else {
                        ((WallViewHolder) holder).reaction_count.setText(mList.get(position).getTotallike());
                    }

                    if (mList.get(position).getPostlike().equals("1")) {

                        if (mList.get(position).getPostemojiunicode() != null && !mList.get(position).getPostemojiunicode().equals("")) {
                            Bitmap emojiBitmap = EmojiUtil.getEmojiBitmap(context, mList.get(position).getPostemojiunicode(), 32);
                            ((WallViewHolder) holder).reactButton.setImageBitmap(emojiBitmap);
                        } else {
                            ((WallViewHolder) holder).reactButton.setImageResource(R.drawable.baseline_favorite_24);
                        }

                    } else {

                        ((WallViewHolder) holder).reactButton.setImageResource(R.drawable.baseline_favorite_border_24);
                    }

                    if (mList.get(position).getTotcomment().equals("0")) {

                        ((WallViewHolder) holder).comment_textview.setText("");
                    } else {
                        ((WallViewHolder) holder).comment_textview.setText(mList.get(position).getTotcomment());
                    }
                    if (!showContact) {
                        ((WallViewHolder) holder).reactions_card.setVisibility(View.GONE);
                        ((WallViewHolder) holder).reactionsVisible=false;
                        isLongPress = false;
                    }
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                    ((WallViewHolder) holder).wall_image_rv.setLayoutManager(layoutManager);
                    ((WallViewHolder) holder).wall_image_rv.setHasFixedSize(false);

                    if ((mList.get(position).getPostImages() != null && mList.get(position).getPostImages().size() != 0)) {
                        ((WallViewHolder) holder).wall_image_rv.setVisibility(View.VISIBLE);
                        WallChildAdapter childRecyclerViewAdapter = new WallChildAdapter(mList.get(position).getPostImages(), ((WallViewHolder) holder).wall_image_rv.getContext(), icb);
                        ((WallViewHolder) holder).wall_image_rv.setAdapter(childRecyclerViewAdapter);

                        snapHelper.attachToRecyclerView(((WallViewHolder) holder).wall_image_rv);

                    } else {
                        ((WallViewHolder) holder).wall_image_rv.setVisibility(View.GONE);
                    }

                    ((WallViewHolder) holder).user_tv.setText(mList.get(position).getUsername());
                    ((WallViewHolder) holder).created_date_tv.setText(mList.get(position).getNeighborhood() + ", " + mList.get(position).getCreatedOn());

                    try {
                        if (mList.get(position).getUserpic().isEmpty()) {
                            ((WallViewHolder) holder).profile_imageview.setImageResource(R.drawable.marketplace_white_background);
                        } else {
                            Picasso.get().load(mList.get(position).getUserpic()).fit().centerCrop().error(R.drawable.marketplace_white_background).placeholder(R.drawable.marketplace_white_background).into(((WallViewHolder) holder).profile_imageview);
                        }
                    } catch (Exception e) {
                    }
                    if (mList.get(position).getFavouritstatus() == 0) {
                        ((WallViewHolder) holder).iv_favorite.setImageResource(R.drawable.svg_bookmark_outline);
                    } else {
                        ((WallViewHolder) holder).iv_favorite.setImageResource(R.drawable.svg_bookmark_fill);
                    }

                    RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                    ((WallViewHolder) holder).emoji_rv.setLayoutManager(layoutManager1);
                    ((WallViewHolder) holder).emoji_rv.setHasFixedSize(false);
                    EmojiAdapter emojiAdapter = new EmojiAdapter(emojiList, ((WallViewHolder) holder).emoji_rv.getContext(), ecb);
                    ((WallViewHolder) holder).emoji_rv.setAdapter(emojiAdapter);
                    sm = new SharedPrefsManager(context);
                    int id2 = Integer.parseInt(sm.getString("user_id"));
                    ((WallViewHolder) holder).profile_ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isUserVerified) {
                                if (id2 == Integer.parseInt(mList.get(position).getCreatedby())) {
                                    Intent i = new Intent(context, MyProfile.class);
                                    context.startActivity(i);
                                }else {
                                    Intent i = new Intent(context, MyProfileOtherUser.class);
                                    i.putExtra("user_id", Integer.parseInt(mList.get(position).getCreatedby()));
                                    context.startActivity(i);
                                }
                            } else {
                                GlobalMethods.getInstance(context).globalDialog(context, context.getString(R.string.unverified_msg));
                            }
                        }
                    });

                    ((WallViewHolder) holder).rl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //                            newRequest.onClickImage(position);
                        }
                    });


                    ((WallViewHolder) holder).comment_ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            newRequest.onClickComment(mList, mList.get(position).getPostid(), 0);                        }
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

//                                newRequest.onClickEmoji(mList.get(position).getEmojilistdata());

                                newRequest.onClickEmoji(Integer.parseInt(mList.get(position).getPostid()));
                            }
                        }
                    });

                    ((WallViewHolder) holder).threedot_imageview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isUserVerified) {
                                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
                                View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.fragment_bottom_sheet_wall, null);
                                bottomSheetDialog.setContentView(bottomSheetView);
                                bottomSheetView.setBackgroundResource(R.drawable.rounded_bottom_sheet_background);
                                bottomSheetView.post(() -> {
                                    View parent = (View) bottomSheetView.getParent();
                                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) parent.getLayoutParams();
                                    int margin = (int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics());
                                    params.leftMargin = margin;
                                    params.rightMargin = margin;
                                    parent.setLayoutParams(params);
                                });

                                RelativeLayout blockUser = bottomSheetView.findViewById(R.id.block_rl);
                                RelativeLayout sharePost = bottomSheetView.findViewById(R.id.share_rl);
                                RelativeLayout favoritePost = bottomSheetView.findViewById(R.id.fav_rl);
                                RelativeLayout reportPost = bottomSheetView.findViewById(R.id.report_rl);
                                RelativeLayout directMessage = bottomSheetView.findViewById(R.id.chat_rl);
                                RelativeLayout deletePost = bottomSheetView.findViewById(R.id.delete_rl);
                                View deleteView = bottomSheetView.findViewById(R.id.delete_view);
                                View blockView = bottomSheetView.findViewById(R.id.view_block);
                                View dmView = bottomSheetView.findViewById(R.id.chat_view);
                                TextView txtvFav = bottomSheetView.findViewById(R.id.fav_tv);
                                TextView txtvFavSubtitle = bottomSheetView.findViewById(R.id.fav_subtitle_tv);

                                if (mList.get(position).getFavouritstatus() == 0) {
                                    txtvFav.setText("Favourite Post");
                                    txtvFavSubtitle.setText("Add this to my favourite post");
                                } else {
                                    txtvFav.setText("Unfavourite Post");
                                    txtvFavSubtitle.setText("Remove this from my favourite post");
                                }

                                int id = Integer.parseInt(sm.getString("user_id"));
                                if (id == Integer.parseInt(mList.get(position).getCreatedby())) {
                                    reportPost.setVisibility(View.GONE);
                                    directMessage.setVisibility(View.GONE);
                                    favoritePost.setVisibility(View.GONE);
                                    dmView.setVisibility(View.GONE);
                                    dmView.setVisibility(View.GONE);
                                    blockUser.setVisibility(View.GONE);
                                    blockView.setVisibility(View.GONE);
                                    deletePost.setVisibility(View.VISIBLE);
//                                    deleteView.setVisibility(View.VISIBLE);
                                }

                                sharePost.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        newRequest.onClickShare(position);
                                        bottomSheetDialog.dismiss();
                                    }
                                });
                                favoritePost.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        newRequest.onFavSelect(Integer.parseInt(mList.get(position).getPostid()), mList.get(position).getNeighborhood(), mList.get(position).getFavouritstatus(), position);
                                        bottomSheetDialog.dismiss();
                                    }
                                });
                                reportPost.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        newRequest.onReportSelect(mList, position);
                                        bottomSheetDialog.dismiss();
                                    }
                                });
                                directMessage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        newRequest.onDmSelect(mList, position);
                                        bottomSheetDialog.dismiss();
                                    }
                                });
                                deletePost.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        newRequest.onDeletePost(Integer.parseInt(mList.get(position).getPostid()));
                                        bottomSheetDialog.dismiss();
                                    }
                                });
                                blockUser.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        new AlertDialog.Builder(context)
                                                .setTitle("Confirm Block")
                                                .setMessage("Are you sure you want to block " + ((WallViewHolder) holder).user_tv.getText().toString() + "?")
                                                .setCancelable(false)
                                                .setPositiveButton("OK", (dialog, which) -> {
//                                                    Toast.makeText(context, "Block User!", Toast.LENGTH_SHORT).show();
                                                    newRequest.onClickBlock(position);
                                                    bottomSheetDialog.dismiss();
                                                })
                                                .setNegativeButton("Cancel", (dialog, which) -> {
                                                    dialog.dismiss();
                                                    bottomSheetDialog.dismiss();
                                                })
                                                .show();
                                    }
                                });

                                bottomSheetDialog.show();
                            } else {
                                GlobalMethods.getInstance(context).globalDialog(context, context.getString(R.string.unverified_msg));
                            }
                        }
                    });

                    ((WallViewHolder) holder).share_imageview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            newRequest.onClickShare(position);
                        }
                    });


                    ((WallViewHolder) holder).ll_favorite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            newRequest.onFavSelect(Integer.parseInt(mList.get(position).getPostid()), mList.get(position).getNeighborhood(), mList.get(position).getFavouritstatus(), position);
                        }
                    });
                    ((WallViewHolder) holder).ll_reactButton.setOnTouchListener(new View.OnTouchListener() {
                        //Shubham Upadte
                   //     private boolean isLongPress = false;
                        private Handler handler = new Handler();
                        private Runnable longPressRunnable = new Runnable() {
                            @Override
                            public void run() {
                                isLongPress = true;
                                ((WallViewHolder) holder).reactions_card.setVisibility(View.VISIBLE);
                                cardPosNo = position;
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


                    setReactionClickListener(((WallViewHolder) holder).reactionLike, "👍", ((WallViewHolder) holder), position);
                    setReactionClickListener(((WallViewHolder) holder).reactionLove, "❤", ((WallViewHolder) holder), position);
                    setReactionClickListener(((WallViewHolder) holder).reactionHaha, "😂", ((WallViewHolder) holder), position);
                    setReactionClickListener(((WallViewHolder) holder).reactionWow, "😮", ((WallViewHolder) holder), position);
                    setReactionClickListener(((WallViewHolder) holder).reactionSad, "🥳", ((WallViewHolder) holder), position);
                    setReactionClickListener(((WallViewHolder) holder).reactionAngry, "😎", ((WallViewHolder) holder), position);

                    break;


                case "Event":

                    ((EventViewHolder) holder).mTitle.setText(mList.get(position).getEvent_name());
                    ((EventViewHolder) holder).mDescription.setText(mList.get(position).getEvent_description());
                    ((EventViewHolder) holder).user_tv.setText(mList.get(position).getUsername());
                    ((EventViewHolder) holder).created_date_tv.setText(mList.get(position).getNeighborhood() + ", " + mList.get(position).getCreatedOn());
                    ((EventViewHolder) holder).start_date_value_tv.setText(mList.get(position).getEvent_start_date());
                    ((EventViewHolder) holder).end_date_value_tv.setText(mList.get(position).getEvent_end_date());
                    try {


                        if (mList.get(position).getUserpic().isEmpty()) {
                            ((EventViewHolder) holder).profile_imageview.setImageResource(R.drawable.marketplace_white_background);
                        } else {
                            Picasso.get().load(mList.get(position).getUserpic()).fit().centerCrop().error(R.drawable.marketplace_white_background).placeholder(R.drawable.marketplace_white_background)
                                    .into(((EventViewHolder) holder).profile_imageview);
                        }
                        if (mList.get(position).getEventCoverImage().isEmpty()) {
                            ((EventViewHolder) holder).imgHomeEvent.setImageResource(R.drawable.event_for_app);
                        } else {
                            Glide.with(context)
                                    .load(mList.get(position).getEventCoverImage())
                                    .apply(RequestOptions.centerCropTransform())
                                    .into(((EventViewHolder) holder).imgHomeEvent);
                           // Picasso.get().load(mList.get(position).getEventCoverImage()).fit().centerCrop().error(R.drawable.event_background_wall).into(((EventViewHolder) holder).imgHomeEvent);
                        }
                    } catch (Exception e) {

                    }
                    sm = new SharedPrefsManager(context);
                    int id3 = Integer.parseInt(sm.getString("user_id"));
                    ((EventViewHolder) holder).profile_ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isUserVerified) {
                                if (id3 == Integer.parseInt(mList.get(position).getCreatedby())) {
                                    Intent i = new Intent(context, MyProfile.class);
                                    context.startActivity(i);
                                }else {
                                    Intent i = new Intent(context, MyProfileOtherUser.class);
                                    i.putExtra("user_id", Integer.parseInt(mList.get(position).getCreatedby()));
                                    context.startActivity(i);
                                }

                            }
                        }
                    });

                    ((EventViewHolder) holder).root.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            newRequest.onEventClick(Integer.parseInt(mList.get(position).getEventid()));
                        }
                    });

                    ((EventViewHolder) holder).threedot_imageview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isUserVerified) {
                                newRequest.onThreeDotClick(mList, position);
                            } else {
                                GlobalMethods.getInstance(context).globalDialog(context, context.getString(R.string.unverified_msg));

                            }

                        }
                    });

                    break;

                case "Sponsor":
                    ((SponsorViewHolder) holder).user_tv.setText(mList.get(position).getCompany());
                    ((SponsorViewHolder) holder).shop_now_tv.setText(mList.get(position).getAction());
                    ((SponsorViewHolder) holder).created_date_tv.setText(mList.get(position).getType());
                    try {

                        if (mList.get(position).getCompanylogo().isEmpty()) {
                            ((SponsorViewHolder) holder).profile_imageview.setImageResource(R.drawable.marketplace_white_background);
                        } else {
                            Picasso.get().load(mList.get(position).getCompanylogo()).fit().centerCrop().error(R.drawable.marketplace_white_background).placeholder(R.drawable.marketplace_white_background)
                                    .into(((SponsorViewHolder) holder).profile_imageview);
                        }
                    } catch (Exception e) {

                    }

                    if (mList.get(position).getBannerimage().isEmpty()) {
                        ((SponsorViewHolder) holder).image_view.setImageResource(R.drawable.marketplace_white_background);
                    } else {
                        Picasso.get().load(mList.get(position).getBannerimage()).fit().centerCrop().error(R.drawable.marketplace_white_background).placeholder(R.drawable.marketplace_white_background)
                                .into(((SponsorViewHolder) holder).image_view);
                    }

//                    ((SponsorViewHolder) holder).sponser_textview.setText(mList.get(position).getDescription());
                    ((SponsorViewHolder) holder).threedot_imageview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            newRequest.onThreeDotClick(position, "event", "");
                        }
                    });

                    ((SponsorViewHolder) holder).shop_now_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            newRequest.onClickSponser(Integer.parseInt(mList.get(position).getSponsor_id()));
                            Intent i = new Intent(context, WebViewActivity.class);
                            i.putExtra("companylink", mList.get(position).getCompanylink());
                            context.startActivity(i);
                        }
                    });

                    break;

                case "Welcome":

                    ((WelcomeViewHolder) holder).welcome_note_tv.setText(mList.get(position).getWelcome_msg() + "." + "  " + "Let's welcome" + " " + mList.get(position).getFirstname());
                    //    ((WelcomeViewHolder) holder).txtv_firstname.setText("Let's welcome " + mList.get(position).getFirstname());
                    ((WelcomeViewHolder) holder).thusmup_count.setText(mList.get(position).getTotal_like());
                    ((WelcomeViewHolder) holder).flower_count.setText(mList.get(position).getTotal_bokay());
                    ((WelcomeViewHolder) holder).btn_welcome_thumsup.setImageResource(R.drawable.ic_candy);
                    ((WelcomeViewHolder) holder).btn_welcome_flower.setImageResource(R.drawable.flower);

                    ((WelcomeViewHolder) holder).ll_welcome_thumsup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (mList.get(position).getLike_status().equals("0")) {

                                if (mList.get(position).getUser_bokay().equals("0")) {

                                    newRequest.onSetThumsUp(Integer.parseInt(mList.get(position).getWelcomeid()), Integer.parseInt(mList.get(position).getCreatedby()), "1", position);
                                }
                            }
                        }
                    });
                    ((WelcomeViewHolder) holder).ll_welcome_flower.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (mList.get(position).getUser_bokay().equals("0")) {

                                if (mList.get(position).getLike_status().equals("0")) {

                                    newRequest.onSetFlower(Integer.parseInt(mList.get(position).getWelcomeid()), Integer.parseInt(mList.get(position).getCreatedby()), "1", position);
                                }
                            }
                        }
                    });

                    ((WelcomeViewHolder) holder).ll_thumsup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (!mList.get(position).getTotal_like().equals("0")) {

                                newRequest.onClickLike(Integer.parseInt(mList.get(position).getCreatedby()), "Like");
                            }
                        }
                    });

                    ((WelcomeViewHolder) holder).ll_bouquet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (!mList.get(position).getTotal_bokay().equals("0")) {
                                // Toast.makeText(activity, "Welcome Test", Toast.LENGTH_LONG).show();
                                newRequest.onClickBouquet(Integer.parseInt(mList.get(position).getCreatedby()), "Bukay");
                            }
                        }
                    });

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
        TextView businessName, user_tv, created_date_tv;
        TextView businessType, business_tagline;
        TextView businessDescription, rate_now_textview, write_review_textview, read_review_textview;
        LinearLayout root, profile_ll;
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
            porduct_image = itemView.findViewById(R.id.porduct_image);
            recyclerView = itemView.findViewById(R.id.rv_business_list);
            user_tv = itemView.findViewById(R.id.user_tv);
            root = itemView.findViewById(R.id.root);
            imageview1 = itemView.findViewById(R.id.imageview1);
            profile_ll = itemView.findViewById(R.id.profile_ll);

        }

    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle, created_date_tv, event_venue_tv, user_tv, start_date_value_tv, end_date_value_tv;
        ImageView profile_imageview, imgHomeEvent, threedot_imageview;
        private final TextView mDescription;
        LinearLayout profile_ll, root;


        public EventViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.event_tv);
            mDescription = itemView.findViewById(R.id.event_desc_textview);
            imgHomeEvent = itemView.findViewById(R.id.imgHomeEvent);
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
        LinearLayout root, profile_ll;

        public PollViewHolder(@NonNull View itemView) {
            super(itemView);
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
            profile_ll = itemView.findViewById(R.id.profile_ll);
            threedot_imageview = itemView.findViewById(R.id.threedot_imageview);
        }
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_imageview, threedot_imageview, imgHomeGroup;
        TextView group_name_tv, created_date_tv, user_tv, group_desc_tv, group_type_tv;
        LinearLayout root, profile_ll;


        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            group_name_tv = itemView.findViewById(R.id.group_name_tv);
//            member_count_tv = itemView.findViewById(R.id.member_count_tv);
            user_tv = itemView.findViewById(R.id.user_tv);
            imgHomeGroup = itemView.findViewById(R.id.imgHomeGroup);
            group_desc_tv = itemView.findViewById(R.id.group_desc_tv);
            created_date_tv = itemView.findViewById(R.id.created_date_tv);
            threedot_imageview = itemView.findViewById(R.id.threedot_imageview);
            profile_imageview = itemView.findViewById(R.id.profile_imageview);
            root = itemView.findViewById(R.id.root);
            group_type_tv = itemView.findViewById(R.id.group_type_tv);
            profile_ll = itemView.findViewById(R.id.profile_ll);
            threedot_imageview = itemView.findViewById(R.id.threedot_imageview);
        }
    }

    public class SponsorViewHolder extends RecyclerView.ViewHolder {
        ImageView image_view, threedot_imageview, profile_imageview;
        TextView sponser_textview, created_date_tv, user_tv, shop_now_tv, group_desc_tv;
        LinearLayout root;
        RecyclerView recyclerView;

        public SponsorViewHolder(@NonNull View itemView) {
            super(itemView);
            user_tv = itemView.findViewById(R.id.user_tv);
            threedot_imageview = itemView.findViewById(R.id.threedot_imageview);
//            recyclerView = itemView.findViewById(R.id.sponsor_rv);
            image_view = itemView.findViewById(R.id.image_view);
            created_date_tv = itemView.findViewById(R.id.created_date_tv);
            profile_imageview = itemView.findViewById(R.id.profile_imageview);
            shop_now_tv = itemView.findViewById(R.id.shop_now_tv);
        }

    }

    public class WelcomeViewHolder extends RecyclerView.ViewHolder {
        ImageView btn_welcome_thumsup, btn_welcome_flower;
        TextView welcome_note_tv, txtv_firstname, thusmup_count, flower_count;
        LinearLayout ll_thumsup, ll_bouquet, ll_welcome_thumsup, ll_welcome_flower;

        public WelcomeViewHolder(@NonNull View itemView) {
            super(itemView);
            welcome_note_tv = itemView.findViewById(R.id.welcome_note_tv);
            txtv_firstname = itemView.findViewById(R.id.txtv_firstname);
            btn_welcome_thumsup = itemView.findViewById(R.id.btn_welcome_thumsup);
            btn_welcome_flower = itemView.findViewById(R.id.btn_welcome_flower);
            thusmup_count = itemView.findViewById(R.id.thusmup_count);
            flower_count = itemView.findViewById(R.id.flower_count);
            ll_thumsup = itemView.findViewById(R.id.ll_thumsup);
            ll_welcome_thumsup = itemView.findViewById(R.id.ll_welcome_thumsup);
            ll_welcome_flower = itemView.findViewById(R.id.ll_welcome_flower);
            ll_bouquet = itemView.findViewById(R.id.ll_bouquet);
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
        ImageView profile_imageview, threedot_imageview, share_imageview, reactButton, iv_favorite;
        TextView post_type, created_date_tv, like_textview, comment_textview, user_tv, reaction_count;
        TextView post_msg;
        LinearLayout root, comment_ll, emoji_ll1, profile_ll, ll_reactButton, ll_reactCount, ll_favorite;
        RelativeLayout rl;
        RecyclerView wall_image_rv, emoji_rv;
        private CardView reactions_card;
        ImageView reactionLike, reactionLove, reactionHaha, reactionWow, reactionSad, reactionAngry;
        boolean reactionsVisible = false;
        ViewPager viewPagerMain;
//        final ReactButton reactButton;

        public WallViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            ll_reactButton = itemView.findViewById(R.id.ll_react_button);
            ll_reactCount = itemView.findViewById(R.id.ll_react_count);
            user_tv = itemView.findViewById(R.id.user_tv);
            post_type = itemView.findViewById(R.id.post_type_tv);
            ll_favorite = itemView.findViewById(R.id.ll_favorite);
            iv_favorite = itemView.findViewById(R.id.iv_favorite);
            post_msg = itemView.findViewById(R.id.post_msg_textview);
            created_date_tv = itemView.findViewById(R.id.created_date_tv);
            threedot_imageview = itemView.findViewById(R.id.threedot_imageview);
            comment_textview = itemView.findViewById(R.id.comment_textview);
            comment_ll = itemView.findViewById(R.id.comment_ll);
            reactions_card = itemView.findViewById(R.id.reactions_card);
            reactionLike = itemView.findViewById(R.id.reaction_like);
            reactionLove = itemView.findViewById(R.id.reaction_love);
            reactionHaha = itemView.findViewById(R.id.reaction_haha);
            reactionWow = itemView.findViewById(R.id.reaction_wow);
            reactionSad = itemView.findViewById(R.id.reaction_sad);
            reactionAngry = itemView.findViewById(R.id.reaction_angry);
//            like_imageview = itemView.findViewById(R.id.like_imageview);
            share_imageview = itemView.findViewById(R.id.share_imageview);
            emoji_rv = itemView.findViewById(R.id.emoji_rv);
            emoji_ll1 = itemView.findViewById(R.id.like_ll);
            rl = itemView.findViewById(R.id.rl);
            wall_image_rv = itemView.findViewById(R.id.wall_image_rv);
            reactButton = itemView.findViewById(R.id.reactButton);
//            emoji_image = itemView.findViewById(R.id.emojiButton);
            reaction_count = itemView.findViewById(R.id.reaction_count);
            profile_imageview = itemView.findViewById(R.id.profile_imageview);
            profile_ll = itemView.findViewById(R.id.profile_ll);
        }

    }

    public interface WallRequest {
        void onThreeDotClick(List<Listdatum> mList, int pos);

        void onDeletePost(int postId);

        void onReportSelect(List<Listdatum> mList, int pos);

        void onDmSelect(List<Listdatum> mList, int pos);

        void onSetEmoji(int pos, String postlike, String reactionCode, int likePos);

        void onSetThumsUp(int welid, int weluserid, String postlike, int pos);

        void onSetFlower(int welid, int weluserid, String postlike, int pos);

        void onClickEmoji(int postid);
        void onClickRateNow(String businessId);
        void onClickLike(int weluserid, String str);

        void onDetailPage(int position, int pos, String isVoted, String ispollrunning);

        void onClickBouquet(int weluserid, String str);

        void onClickComment(List<Listdatum> mList, String postId, int pos);
        void onFavSelect(int postid, String neighbourhood, int favoriteStatus, int postPos);

        void onClickShare(int pos);

        void onClickSponser(int sponserId);

        void onBusinessDetail(int pos, int bid);

        void onGroupClick(int id, String groupType, String getJoin, String userId, String neighbourhood);

        void onEventClick(int pos);
        void onClickBlock(int pos);
    }

    public void filterList(List<Listdatum> filterdNames) {
        this.mList = filterdNames;
        notifyDataSetChanged();
    }

    public void updateContact(List<Listdatum> newmList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MyDiffUtilsCallbacks(mList, newmList));
        diffResult.dispatchUpdatesTo(this);
        mList.clear();
        mList.addAll(newmList);
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

    public void updateWelcomeThumsup(int position, String totalLike, String likeStatus) {
        mList.get(position).setTotal_like(totalLike);
        mList.get(position).setLike_status(likeStatus);
        mList.get(position).setUser_bokay("1");
        notifyItemChanged(position);
    }

    public void updateWelcomeBokay(int position, String totalBokay, String bokayStatus) {
        mList.get(position).setTotal_bokay(totalBokay);
        mList.get(position).setLike_status("1");
        mList.get(position).setUser_bokay(bokayStatus);
        notifyItemChanged(position);
    }
}
