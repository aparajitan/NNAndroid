package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.intreface.CategoryCallBack;
import com.app_neighbrsnook.intreface.TodayCallBack;
import com.app_neighbrsnook.model.OfferModel;
import com.app_neighbrsnook.model.PopularModel;
import com.app_neighbrsnook.model.SellModel;
import com.app_neighbrsnook.model.SellParenetModel;
import com.app_neighbrsnook.libraries.slider.IndicatorView.animation.type.IndicatorAnimationType;
import com.app_neighbrsnook.libraries.slider.SliderAnimations;
import com.app_neighbrsnook.libraries.slider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class NearAdapter extends RecyclerView.Adapter {
        public static final int POPULAR_CAT = 1;
        public static final int TODAY_LIST = 2;
        public static final int WISH_LIST = 3;
        public static final int YOUR_ITEMS = 4;
        public static final int SLIDER = 5;
        private List mList;
        List<SellModel> todaySellList = new ArrayList<>();
        List<SellParenetModel> saleSellList = new ArrayList<>();
        List<PopularModel> popularList = new ArrayList<>();
        List<OfferModel>offersList = new ArrayList<>();
        List<OfferModel> checkoutList = new ArrayList<>();
    private final int[] images;
    private final String[] text;


        Context mcon;
        Activity activity;
        NearAdapter.SellRequest sellRequest;
        TodayCallBack tcb;
        CategoryCallBack ccb;

        public NearAdapter(List<SellParenetModel> saleSellList, List<SellModel> todaySellList, List<PopularModel> popularList, List<OfferModel> offersList, List<OfferModel> checkoutList, int[] images, String[] text) {
            this.sellRequest = sellRequest;
            this.ccb = ccb;
            this.mList = mList;
            this.todaySellList = todaySellList;
            this.saleSellList  = saleSellList;
            this.popularList = popularList;
            this.offersList = offersList;
            this.checkoutList = checkoutList;
            this.tcb = tcb;
            this.images = images;
            this.text = text;


        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {

                case SLIDER:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearby_slider_row, parent, false);
                    return new SliderViewHolder(view);

                case POPULAR_CAT:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.near_row, parent, false);
                    return new PopularCatViewHolder(view);

                case WISH_LIST:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_row, parent, false);
                    return new NearAdapter.OfferViewHolder(view);

                case YOUR_ITEMS:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_row, parent, false);
                    return new NearAdapter.ChekcoutViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            SellParenetModel object = saleSellList.get(position);
            if (object != null) {
                switch (object.getType()) {

                    case SLIDER:
                        ((NearAdapter.SliderViewHolder) holder).adapter=new SliderAdapterExample(images,text);
                        ((NearAdapter.SliderViewHolder) holder).sliderView.setSliderAdapter(  ((NearAdapter.SliderViewHolder) holder).adapter);
                        ((NearAdapter.SliderViewHolder) holder).sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                        ((NearAdapter.SliderViewHolder) holder).sliderView.setIndicatorAnimation(IndicatorAnimationType.SLIDE);
                        ((NearAdapter.SliderViewHolder) holder).sliderView.startAutoCycle();
                        break;
                    case POPULAR_CAT:
                        ((NearAdapter.PopularCatViewHolder) holder).rv_popular.setLayoutManager(new GridLayoutManager(mcon,4, RecyclerView.VERTICAL, false));
                        NearByCategoryAdapter nearByCategoryAdapter = new NearByCategoryAdapter(popularList,((NearAdapter.PopularCatViewHolder) holder).rv_popular.getContext(), ccb);
                        ((NearAdapter.PopularCatViewHolder) holder).rv_popular.setAdapter(nearByCategoryAdapter);
                        ((NearAdapter.PopularCatViewHolder) holder).category_name_tv.setText(saleSellList.get(position).getName());
                        ((NearAdapter.PopularCatViewHolder) holder).view_all_tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sellRequest.onClickViewAllPopular(position, "Popular Categories");
                            }
                        });
                        break;
                    case WISH_LIST:
                        RecyclerView.LayoutManager layoutManagerOffer = new LinearLayoutManager(mcon, LinearLayoutManager.HORIZONTAL, false);
                        ((NearAdapter.OfferViewHolder) holder).rv_offers.setLayoutManager(layoutManagerOffer);
                        OffersAdapter nearByCategoryAdapter1 = new OffersAdapter(offersList,((NearAdapter.OfferViewHolder) holder).rv_offers.getContext(), ccb);
                        ((NearAdapter.OfferViewHolder) holder).rv_offers.setAdapter(nearByCategoryAdapter1);
                        ((NearAdapter.OfferViewHolder) holder).category_name_tv.setText(saleSellList.get(position).getName());
                        ((NearAdapter.OfferViewHolder) holder).view_all_tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sellRequest.onClickViewAllPopular(position, "Popular Categories");
                            }
                        });

                        break;


                    case YOUR_ITEMS:
                        RecyclerView.LayoutManager layoutManagerOffer1 = new LinearLayoutManager(mcon, LinearLayoutManager.HORIZONTAL, false);
                        ((NearAdapter.ChekcoutViewHolder) holder).rv_offers.setLayoutManager(layoutManagerOffer1);
                        CheckoutAdapter nearByCategoryAdapter2 = new CheckoutAdapter(checkoutList,((NearAdapter.ChekcoutViewHolder) holder).rv_offers.getContext(), ccb);
                        ((NearAdapter.ChekcoutViewHolder) holder).rv_offers.setAdapter(nearByCategoryAdapter2);
                        ((NearAdapter.ChekcoutViewHolder) holder).category_name_tv.setText(saleSellList.get(position).getName());

                        break;


                }
            }
        }

        @Override
        public int getItemCount() {
            if (saleSellList == null)
                return 0;
            return saleSellList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (saleSellList != null) {
                SellParenetModel object = saleSellList.get(position);
                if (object != null) {
                    return object.getType();
                }
            }
            return 0;
        }



public class TodayViewHolder extends RecyclerView.ViewHolder {

    TextView view_all_tv, category_name_tv,item_name, time_textview;
    ImageView porduct_image;
    RecyclerView rv_today;
    public TodayViewHolder(@NonNull View itemView) {
        super(itemView);
        view_all_tv = itemView.findViewById(R.id.view_all_tv);
        category_name_tv = itemView.findViewById(R.id.category_name_tv);
        rv_today = itemView.findViewById(R.id.rv_today);
    }

}

public  class PopularCatViewHolder extends RecyclerView.ViewHolder {

    TextView view_all_tv, category_name_tv,item_name, time_textview;
    RecyclerView rv_popular;
    public PopularCatViewHolder(@NonNull View itemView) {
        super(itemView);
        category_name_tv = itemView.findViewById(R.id.category_name_tv);
        rv_popular = itemView.findViewById(R.id.rv_today);
        view_all_tv = itemView.findViewById(R.id.view_all_tv);
    }

}

    public  class OfferViewHolder extends RecyclerView.ViewHolder {

        TextView view_all_tv, category_name_tv,item_name, time_textview;
        RecyclerView rv_offers;
        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            category_name_tv = itemView.findViewById(R.id.category_name_tv);
            rv_offers = itemView.findViewById(R.id.rv_offers);
            view_all_tv = itemView.findViewById(R.id.view_all_tv);
        }

    }

    public  class ChekcoutViewHolder extends RecyclerView.ViewHolder {

        TextView view_all_tv, category_name_tv,item_name, time_textview;
        RecyclerView rv_offers;
        public ChekcoutViewHolder(@NonNull View itemView) {
            super(itemView);
            category_name_tv = itemView.findViewById(R.id.category_name_tv);
            rv_offers = itemView.findViewById(R.id.rv_offers);
            view_all_tv = itemView.findViewById(R.id.view_all_tv);
        }

    }


    public  class SliderViewHolder extends RecyclerView.ViewHolder {
        private SliderAdapterExample adapter;
        private final SliderView sliderView;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);

            sliderView=itemView.findViewById(R.id.imageSlider);


        }

    }



public interface SellRequest
{
    void onClickViewAllPopular(int pos, String title);
    void onClickViewAllTodayListing(int pos, String title);
    void onDetailPopular(int pos);
    void onDetailTodayListing(int pos);
}

    }

