package com.app_neighbrsnook.adapter;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.pojo.ImagePojo;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class WallChildAdapterPostActivity extends RecyclerView.Adapter<WallChildAdapterPostActivity.ViewHolder> {
    Context context;
    ImageCallBack imc;
    int[] images = {R.drawable.background_fifth, R.drawable.background_image, R.drawable.background_image_testing, R.drawable.background_photo_testing,
            R.drawable.background_img_second, R.drawable.background_img_user};
    List<ImagePojo> list = new ArrayList<>();
    public WallChildAdapterPostActivity(List<ImagePojo> list, Context mContext, ImageCallBack imc ) {
       this.context = mContext;
       this.list = list;
       this.imc = imc;

    }

    @NonNull
    @Override
    public WallChildAdapterPostActivity.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item1, parent, false);
        context = parent.getContext();
        return new WallChildAdapterPostActivity.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WallChildAdapterPostActivity.ViewHolder holder, int position) {

            Picasso.get().load(list.get(position).getImg()).fit().centerCrop().into(holder.image_view);
            Glide.with(context).load(list.get(position).getImg())
                    .apply(bitmapTransform(new BlurTransformation(80)))
                    .into(holder.imageview1);
            if(list.size()== 1)
            {
               holder.count.setVisibility(View.GONE);
            }
            else {
                holder.count.setText(position + 1 + "/" + list.size());
            }
 /*//craseh img when i click 21-02-24
        holder.image_view.setOnClickListener(v -> {

            imc.onImageClick(list, position);
        });*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        ImageView image_view, imageview1;
        TextView count;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_view = itemView.findViewById(R.id.image_view);
            count = itemView.findViewById(R.id.image_count);
            imageview1 = itemView.findViewById(R.id.imageview1);
//            linearLayout = itemView.findViewById(R.id.root);

        }
    }
    public interface ImageCallBack {
        void onImageClick(List<ImagePojo> list, int pos);

    }

}
