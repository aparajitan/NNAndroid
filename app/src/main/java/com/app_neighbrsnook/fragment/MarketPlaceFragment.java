package com.app_neighbrsnook.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.app_neighbrsnook.ConfirmActivity;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.MarketplaceChatAdapter;
import com.app_neighbrsnook.model.HospitalModel;
import com.app_neighbrsnook.model.notification.NotificationModel;

import java.util.ArrayList;
import java.util.List;


public class MarketPlaceFragment extends Fragment implements View.OnClickListener, MarketplaceChatAdapter.UserCallBack{
    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView member_number_tv, title;
    ImageView img_back;
    LinearLayout all_ll, sell_ll, buy_ll, donate_ll;
    List<HospitalModel> hospitalList = new ArrayList<>();
    RecyclerView hospital_rv;
    MarketplaceChatAdapter marketplaceChatAdapter;
    List<NotificationModel> list = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_market_place, container, false);
        all_ll = view.findViewById(R.id.all_ll);
        sell_ll = view.findViewById(R.id.sell_ll);
        buy_ll = view.findViewById(R.id.buy_ll);
        donate_ll = view.findViewById(R.id.donate_ll);
        hospital_rv = view.findViewById(R.id.hospital_rv);
        hospital_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        marketplaceChatAdapter = new MarketplaceChatAdapter(getData(), this);
        all_ll.setBackground(getResources().getDrawable(R.drawable.round_corner_dialog_yellow));
        hospital_rv.setAdapter(marketplaceChatAdapter);

//        back_btn.setOnClickListener(this);
        all_ll.setOnClickListener(this);
        buy_ll.setOnClickListener(this);
        sell_ll.setOnClickListener(this);
        donate_ll.setOnClickListener(this);
        img_back = view.findViewById(R.id.back_btn);
        title = view.findViewById(R.id.title);
        member_number_tv = view.findViewById(R.id.member_number_tv);
        title.setText("Marketplace communication");
        img_back.setOnClickListener(this);
        return view;
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new AllChatFragment(), "All");
        adapter.addFragment(new SellerChatFragment(), "Seller");
        adapter.addFragment(new BuyerChatFragment(), "Buyer");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back_btn:
                getActivity().onBackPressed();
                break;

            case R.id.all_ll:
                resetColor();
                hospitalList.clear();
                all_ll.setBackground(getResources().getDrawable(R.drawable.round_corner_dialog_yellow));
//                hospital_rv = view.findViewB  yId(R.id.hospital_rv);
//                hospital_rv.setLayoutManager(new LinearLayoutManager(getContext()));
                list.clear();
                marketplaceChatAdapter = new MarketplaceChatAdapter(getData(), this);
                hospital_rv.setAdapter(marketplaceChatAdapter);
                break;

            case R.id.sell_ll:
                resetColor();
                hospitalList.clear();
                sell_ll.setBackground(getResources().getDrawable(R.drawable.round_corner_dialog_yellow));
                list.clear();
                 marketplaceChatAdapter = new MarketplaceChatAdapter(getData1(), this);
                hospital_rv.setAdapter(marketplaceChatAdapter);
                break;

            case R.id.buy_ll:
                resetColor();
                hospitalList.clear();
                buy_ll.setBackground(getResources().getDrawable(R.drawable.round_corner_dialog_yellow));
//                hospital_rv = view.findViewById(R.id.hospital_rv);
//                hospital_rv.setLayoutManager(new LinearLayoutManager(getContext()));
                list.clear();
                marketplaceChatAdapter = new MarketplaceChatAdapter(getData2(), this);
                hospital_rv.setAdapter(marketplaceChatAdapter);
                break;

            case R.id.donate_ll:
                resetColor();
                hospitalList.clear();
                donate_ll.setBackground(getResources().getDrawable(R.drawable.round_corner_dialog_yellow));
//                hospital_rv = view.findViewById(R.id.hospital_rv);
//                hospital_rv.setLayoutManager(new LinearLayoutManager(getContext()));
                list.clear();
                marketplaceChatAdapter = new MarketplaceChatAdapter(getData(), this);
                hospital_rv.setAdapter(marketplaceChatAdapter);
                break;

        }
    }

    @Override
    public void onUserClick(int pos) {
        Intent i = new Intent (getActivity(), ConfirmActivity.class);
        i.putExtra("buyer_name",list.get(pos).getTitle());
        i.putExtra("question", "question1");
        i.putExtra("type", "seller");
        startActivity(i);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private List<NotificationModel> getData()
    {
        list.add(new NotificationModel("Arsad Ali", "02 Fab, 2:18 am", "dm",R.color.member, R.drawable.round_corner_dialog_themcolor));
        list.add(new NotificationModel("Amit Singh", "05 Fab, 2:18 am", "poll",R.color.poll, R.drawable.poll_round_corner));
        list.add(new NotificationModel("Rajt", "06 Fab, 5:18 pm", "group",R.color.group, R.drawable.group_round_corner));

        return list;
    }

    private List<NotificationModel> getData1()
    {
        list.add(new NotificationModel("Raj", "02 Fab, 2:18 am", "dm",R.color.member, R.drawable.round_corner_dialog_themcolor));
        list.add(new NotificationModel("Sumit Singh", "05 Fab", "poll",R.color.poll, R.drawable.poll_round_corner));
        list.add(new NotificationModel("Rajt", "06 Fab, 5:18 pm", "group",R.color.group, R.drawable.group_round_corner));

        return list;
    }

    private List<NotificationModel> getData2()
    {
        list.add(new NotificationModel("Arsad Ali", "02 Fab, 2:18 am", "dm",R.color.member, R.drawable.round_corner_dialog_themcolor));
        list.add(new NotificationModel("sudhanshu", "05 Fab, 2:18 am", "poll",R.color.poll, R.drawable.poll_round_corner));
        list.add(new NotificationModel("Rajt", "06 Fab, 5:18 pm", "group",R.color.group, R.drawable.group_round_corner));

        return list;
    }

    private void resetColor() {
        all_ll.setBackground(getResources().getDrawable(R.drawable.round_corner_dialog_themcolor));
        buy_ll.setBackground(getResources().getDrawable(R.drawable.round_corner_dialog_themcolor));
        sell_ll.setBackground(getResources().getDrawable(R.drawable.round_corner_dialog_themcolor));
        donate_ll.setBackground(getResources().getDrawable(R.drawable.round_corner_dialog_themcolor));



    }
}