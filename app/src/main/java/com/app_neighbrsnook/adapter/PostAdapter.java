package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.TypedValue;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.model.post.Listdatum;
import com.app_neighbrsnook.model.wall.Emojilistdatum;
import com.app_neighbrsnook.utils.EmojiUtil;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.BankViewHolder> {
    Context mcon;
    WallChildAdapter.ImageCallBack icb;
    PostRequest newRequest;
    private List<Listdatum> mList = new ArrayList();
    String source;
    LinearSnapHelper snapHelper = new LinearSnapHelper();
    SharedPrefsManager sm;
    Boolean isUserVerified;
    private boolean isLongPress = false;
    private boolean showContact = true;

    public PostAdapter(List<Listdatum> lists, PostRequest newRequest, String source, WallChildAdapter.ImageCallBack icb, boolean isUserVerified) {
        this.mList = lists;
        this.newRequest = newRequest;
        this.source = source;
        this.icb = icb;
        this.isUserVerified = isUserVerified;
    }

    public void setShowContact(boolean showContact) {
        this.showContact = showContact;
        if (isLongPress) {
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public PostAdapter.BankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wall_row, parent, false);
        mcon = parent.getContext();
        return new PostAdapter.BankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostAdapter.BankViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            holder.post_type.setText(mList.get(position).getPostType());
            holder.post_msg.setText(mList.get(position).getPostMessage());
            holder.comment_textview.setText(String.valueOf(mList.get(position).getTotcomment()));
            holder.user_tv.setText(mList.get(position).getUsername());
            holder.created_date_tv.setText(mList.get(position).getNeighborhood() + ", " + mList.get(position).getCreatedOn());

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mcon, LinearLayoutManager.HORIZONTAL, false);
            holder.wall_image_rv.setLayoutManager(layoutManager);
            holder.wall_image_rv.setHasFixedSize(false);

            if (source.equals("profile")) {
                holder.threedot_imageview.setImageResource(R.drawable.ic_baseline_more_vert_24);
                holder.threedot_imageview.setVisibility(View.VISIBLE);
            } else {
                holder.threedot_imageview.setVisibility(View.VISIBLE);
            }

            if (mList.get(position).getFavouritstatus() == 0) {
                holder.iv_favorite.setImageResource(R.drawable.svg_bookmark_outline);
            } else {
                holder.iv_favorite.setImageResource(R.drawable.svg_bookmark_fill);
            }
            if ((mList.get(position).getPostImages() != null && mList.get(position).getPostImages().size() != 0)) {
                WallChildAdapter childRecyclerViewAdapter = new WallChildAdapter(mList.get(position).getPostImages(), holder.wall_image_rv.getContext(), icb);
                holder.wall_image_rv.setAdapter(childRecyclerViewAdapter);
                snapHelper.attachToRecyclerView(holder.wall_image_rv);
            }
            if (mList.get(position).getTotallike() == 0) {
                holder.reaction_count.setText("");
            } else {
                holder.reaction_count.setText(String.valueOf(mList.get(position).getTotallike()));
            }
            if (mList.get(position).getUserpic().isEmpty()) {
                holder.profile_imageview.setImageResource(R.drawable.profile);
            } else {
                Picasso.get().load(mList.get(position).getUserpic()).fit().centerCrop().error(R.drawable.profile).placeholder(R.drawable.profile).into(holder.profile_imageview);
            }
            if (mList.get(position).getPostlike().equals("1")) {

                if (mList.get(position).getPostemojiunicode() != null && !mList.get(position).getPostemojiunicode().equals("")) {
                    Bitmap emojiBitmap = EmojiUtil.getEmojiBitmap(mcon, mList.get(position).getPostemojiunicode(), 32);
                    holder.reactButton.setImageBitmap(emojiBitmap);
                } else {
                    holder.reactButton.setImageResource(R.drawable.baseline_favorite_24);
                }
            } else {
                holder.reactButton.setImageResource(R.drawable.baseline_favorite_border_24);
            }

        } catch (Exception e) {
        }
        if (!showContact) {
            holder.reactions_card.setVisibility(View.GONE);
            isLongPress = false;
        }

        holder.comment_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUserVerified) {
                    newRequest.onClickComment(mList, mList.get(position).getPostid(), 0);
                } else {
                    GlobalMethods.getInstance(mcon).globalDialog(mcon, mcon.getString(R.string.unverified_msg));
                }
            }
        });

        holder.ll_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUserVerified) {
                    newRequest.onFavSelect(Integer.parseInt(mList.get(position).getPostid()), mList.get(position).getNeighborhood(), mList.get(position).getFavouritstatus(), position);
                } else {
                    GlobalMethods.getInstance(mcon).globalDialog(mcon, mcon.getString(R.string.unverified_msg));
                }
            }
        });

        holder.ll_reactCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUserVerified) {
                    if (mList.get(position).getTotalEmojis() != 0) {
                        newRequest.onClickEmoji1(Integer.parseInt(mList.get(position).getPostid()));
                    }
                } else {
                    GlobalMethods.getInstance(mcon).globalDialog(mcon, mcon.getString(R.string.unverified_msg));
                }
            }
        });

        holder.threedot_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUserVerified) {
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mcon, R.style.BottomSheetDialogTheme);
                    View bottomSheetView = LayoutInflater.from(mcon).inflate(R.layout.fragment_bottom_sheet_wall, null);
                    bottomSheetDialog.setContentView(bottomSheetView);
                    bottomSheetView.setBackgroundResource(R.drawable.rounded_bottom_sheet_background);
                    bottomSheetView.post(() -> {
                        View parent = (View) bottomSheetView.getParent();
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) parent.getLayoutParams();
                        int margin = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 20, mcon.getResources().getDisplayMetrics());
                        params.leftMargin = margin;
                        params.rightMargin = margin;
                        parent.setLayoutParams(params);
                    });
                    RelativeLayout sharePost = bottomSheetView.findViewById(R.id.share_rl);
                    RelativeLayout favoritePost = bottomSheetView.findViewById(R.id.fav_rl);
                    RelativeLayout reportPost = bottomSheetView.findViewById(R.id.report_rl);
                    RelativeLayout directMessage = bottomSheetView.findViewById(R.id.chat_rl);
                    RelativeLayout deletePost = bottomSheetView.findViewById(R.id.delete_rl);
                    RelativeLayout blockUser = bottomSheetView.findViewById(R.id.block_rl);
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

                    sm = new SharedPrefsManager(mcon);

                    int id = Integer.parseInt(sm.getString("user_id"));
                    if (id == Integer.parseInt(mList.get(position).getCreatedby())) {
                        reportPost.setVisibility(View.GONE);
                        directMessage.setVisibility(View.GONE);
                        dmView.setVisibility(View.GONE);
                        deletePost.setVisibility(View.VISIBLE);
                        blockUser.setVisibility(View.GONE);
                    }

                    sharePost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isUserVerified) {
                                newRequest.onClickShare(position);
                            } else {
                                GlobalMethods.getInstance(mcon).globalDialog(mcon, mcon.getString(R.string.unverified_msg));
                            }
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
                            new AlertDialog.Builder(mcon)
                                    .setTitle("Confirm Block")
                                    .setMessage("Are you sure you want to block " + holder.user_tv.getText().toString() + "?")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", (dialog, which) -> {
//                                        Toast.makeText(mcon, "Block User!", Toast.LENGTH_SHORT).show();
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
                    GlobalMethods.getInstance(mcon).globalDialog(mcon, mcon.getString(R.string.unverified_msg));
                }
            }
        });

        holder.share_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUserVerified) {
                    newRequest.onClickShare(position);
                } else {
                    GlobalMethods.getInstance(mcon).globalDialog(mcon, mcon.getString(R.string.unverified_msg));
                }
            }
        });

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.reactions_card.setVisibility(View.GONE);
            }
        });

        holder.ll_reactButton.setOnTouchListener(new View.OnTouchListener() {
            private Handler handler = new Handler();
            private Runnable longPressRunnable = new Runnable() {
                @Override
                public void run() {
                    if (isUserVerified) {
                        isLongPress = true;
                        holder.reactions_card.setVisibility(View.VISIBLE);
                    } else {
                        GlobalMethods.getInstance(mcon).globalDialog(mcon, mcon.getString(R.string.unverified_msg));
                    }
                }
            };

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (isUserVerified) {
                            isLongPress = false;
                            handler.postDelayed(longPressRunnable, 500);
                            return true;
                        } else {
                            GlobalMethods.getInstance(mcon).globalDialog(mcon, mcon.getString(R.string.unverified_msg));
                        }
                    case MotionEvent.ACTION_UP:
                        if (isUserVerified) {
                            handler.removeCallbacks(longPressRunnable);
                            if (!isLongPress) {
                                toggleLike(holder, position, "❤");
                            } else {
                                holder.reactionsVisible = true;
                            }
                        } else {
                            GlobalMethods.getInstance(mcon).globalDialog(mcon, mcon.getString(R.string.unverified_msg));
                        }
                        return true;
                }
                return false;
            }

        });

        setReactionClickListener(holder.reactionLike, "👍", holder, position);
        setReactionClickListener(holder.reactionLove, "❤", holder, position);
        setReactionClickListener(holder.reactionHaha, "😂", holder, position);
        setReactionClickListener(holder.reactionWow, "😮", holder, position);
        setReactionClickListener(holder.reactionSad, "🥳", holder, position);
        setReactionClickListener(holder.reactionAngry, "😎", holder, position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class BankViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_imageview, threedot_imageview, reactButton, share_imageview, iv_favorite;
        TextView post_type, created_date_tv, like_textview, comment_textview, user_tv, reaction_count;
        TextView post_msg;
        LinearLayout root, comment_ll, emoji_ll, profile_ll, ll_reactButton, ll_reactCount, ll_favorite;
        RelativeLayout rl;
        RecyclerView wall_image_rv, emoji_rv;
        CardView reactions_card;
        ImageView reactionLike, reactionLove, reactionHaha, reactionWow, reactionSad, reactionAngry;
        boolean reactionsVisible = false;

        public BankViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            ll_favorite = itemView.findViewById(R.id.ll_favorite);
            iv_favorite = itemView.findViewById(R.id.iv_favorite);
            ll_reactButton = itemView.findViewById(R.id.ll_react_button);
            ll_reactCount = itemView.findViewById(R.id.ll_react_count);
            user_tv = itemView.findViewById(R.id.user_tv);
            post_type = itemView.findViewById(R.id.post_type_tv);
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
            emoji_ll = itemView.findViewById(R.id.like_ll);
            rl = itemView.findViewById(R.id.rl);
            wall_image_rv = itemView.findViewById(R.id.wall_image_rv);
            reactButton = itemView.findViewById(R.id.reactButton);
//            emoji_image = itemView.findViewById(R.id.emojiButton);
            reaction_count = itemView.findViewById(R.id.reaction_count);
            profile_imageview = itemView.findViewById(R.id.profile_imageview);
            profile_ll = itemView.findViewById(R.id.profile_ll);
        }

    }

    public interface PostRequest {

        void threeDot(int pos, int postId);

        void onDeletePost(int postId);

        void onSetEmoji(int pos, String postlike);

        void onReportSelect(List<Listdatum> mList, int pos);

        void onClickEmoji(List<Emojilistdatum> pos);

        void onClickEmoji1(int postid);

        void onDmSelect(List<Listdatum> mList, int pos);

        void onFavSelect(int postid, String neighbourhood, int favoriteStatus, int postPos);

        void onSetEmoji1(int pos, String postlike, String reactionCode, int likePos);

        void onClickComment(List<Listdatum> mList, String postId, int pos);

        void onClickShare(int pos);

        void onClickBlock(int pos);
    }

    public void filterList(List<Listdatum> filteredList) {
        mList = filteredList;
        notifyDataSetChanged();
    }

    private void toggleLike(BankViewHolder holder, int pos, final String reactionCode) {

        if (mList.get(pos).getPostlike().equals("0")) {
            newRequest.onSetEmoji1(Integer.parseInt(mList.get(pos).getPostid()), "1", "", pos);
        } else {
            newRequest.onSetEmoji1(Integer.parseInt(mList.get(pos).getPostid()), "0", "", pos);
        }
    }

    private void setReactionClickListener(ImageView reaction, final String reactionCode, final BankViewHolder holder, final int pos) {
        reaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.reactions_card.setVisibility(View.GONE);
                holder.reactionsVisible = false;
                newRequest.onSetEmoji1(Integer.parseInt(mList.get(pos).getPostid()), "1", reactionCode, pos);
            }
        });
    }

    public void updateItemText(int position, int totalLike, String likeStatus, String emojiUnicode) {
        mList.get(position).setTotallike(totalLike);
        mList.get(position).setTotalEmojis(totalLike);
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