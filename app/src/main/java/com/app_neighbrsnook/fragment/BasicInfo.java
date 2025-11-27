package com.app_neighbrsnook.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app_neighbrsnook.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BasicInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BasicInfo extends Fragment {

    FrameLayout frm_single,frm_sharing,frm_all;
    Boolean is_single_room =false;
    Boolean is_sharing_room=false;
    LinearLayout frm_gone,lnr_single_room;
    String propert_type = "";
    FrameLayout fully_furnished,semi_furnished,unfurnished;
    LinearLayout lnr_fullyfurnished,lnr_semifurnished;
    TextView tv_add_furnishing;
    Boolean is_all_room=false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public BasicInfo() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BasicInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static BasicInfo newInstance(String param1, String param2) {
        BasicInfo fragment = new BasicInfo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_basic_info, container, false);
        frm_single=view.findViewById(R.id.frm_single_room);
        frm_sharing=view.findViewById(R.id.frm_sharing_room);
        frm_all=view.findViewById(R.id.frm_all);
        frm_gone=view.findViewById(R.id.frm_gone);
        lnr_single_room=view.findViewById(R.id.frm_single_rooms);
        fully_furnished=view.findViewById(R.id.frm_fully_furnished);
        semi_furnished=view.findViewById(R.id.frm_semi_furnished);
        unfurnished=view.findViewById(R.id.frm_unfurnished);
        lnr_fullyfurnished=view.findViewById(R.id.lnr_fully_furnished);
        lnr_semifurnished=view.findViewById(R.id.lnr_semi_furnished);
        tv_add_furnishing=view.findViewById(R.id.text_furnishing);
        frm_single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_single_room) {
                    is_single_room = false;
                    propert_type ="Single Room";
                    frm_single.setBackgroundResource(R.drawable.post_unselect_background);
                } else {
                    is_single_room = true;
                    is_sharing_room = false;
                    is_all_room = false;
                    frm_single.setBackgroundResource(R.drawable.filter_select_background);
                    frm_sharing.setBackgroundResource(R.drawable.post_unselect_background);
                    frm_all.setBackgroundResource(R.drawable.post_unselect_background);
                    frm_gone.setVisibility(View.GONE);
                    lnr_single_room.setVisibility(View.VISIBLE);
                    // frm_gone.setVisibility(View.VISIBLE);
                }
            }
        });
        frm_sharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_sharing_room) {
                    is_sharing_room = false;
                    propert_type ="Sharing Room";
                    frm_sharing.setBackgroundResource(R.drawable.post_unselect_background);
                } else {
                    is_sharing_room = true;
                    is_single_room = false;
                    is_all_room = false;
                    frm_sharing.setBackgroundResource(R.drawable.filter_select_background);
                    frm_single.setBackgroundResource(R.drawable.post_unselect_background);
                    frm_all.setBackgroundResource(R.drawable.post_unselect_background);
                    frm_gone.setVisibility(View.VISIBLE);
                    lnr_single_room.setVisibility(View.GONE);
                }
            }
        });
        frm_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_all_room) {
                    is_all_room = false;
                    propert_type ="All";

                    frm_all.setBackgroundResource(R.drawable.post_unselect_background);
                } else {
                    is_sharing_room = true;
                    is_single_room = false;
                    is_all_room = false;
                    frm_all.setBackgroundResource(R.drawable.filter_select_background);
                    frm_sharing.setBackgroundResource(R.drawable.post_unselect_background);
                    frm_single.setBackgroundResource(R.drawable.post_unselect_background);
                    frm_gone.setVisibility(View.GONE);
                    lnr_single_room.setVisibility(View.GONE);

                    //l1.setVisibility(View.VISIBLE);
                }
            }
        });

        fully_furnished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_single_room) {
                    is_single_room = false;
                   // propert_type ="Single Room";
                    fully_furnished.setBackgroundResource(R.drawable.post_unselect_background);
                } else {
                    is_single_room = true;
                    is_sharing_room = false;
                    is_all_room = false;
                    fully_furnished.setBackgroundResource(R.drawable.filter_select_background);
                    semi_furnished.setBackgroundResource(R.drawable.post_unselect_background);
                    unfurnished.setBackgroundResource(R.drawable.post_unselect_background);
                    lnr_fullyfurnished.setVisibility(View.VISIBLE);
                    lnr_semifurnished.setVisibility(View.GONE);
                    // frm_gone.setVisibility(View.VISIBLE);
                }
            }
        });
        semi_furnished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_sharing_room) {
                    is_sharing_room = false;
                   // propert_type ="Sharing Room";
                    semi_furnished.setBackgroundResource(R.drawable.post_unselect_background);
                } else {
                    is_sharing_room = true;
                    is_single_room = false;
                    is_all_room = false;
                    semi_furnished.setBackgroundResource(R.drawable.filter_select_background);
                    fully_furnished.setBackgroundResource(R.drawable.post_unselect_background);
                    unfurnished.setBackgroundResource(R.drawable.post_unselect_background);
                    lnr_fullyfurnished.setVisibility(View.GONE);
                    lnr_semifurnished.setVisibility(View.VISIBLE);
                }
            }
        });
        unfurnished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_all_room) {
                    is_all_room = false;
                 //   propert_type ="All";
                    unfurnished.setBackgroundResource(R.drawable.post_unselect_background);
                } else {
                    is_sharing_room = true;
                    is_single_room = false;
                    is_all_room = false;
                    unfurnished.setBackgroundResource(R.drawable.filter_select_background);
                    fully_furnished.setBackgroundResource(R.drawable.post_unselect_background);
                    semi_furnished.setBackgroundResource(R.drawable.post_unselect_background);
                    lnr_fullyfurnished.setVisibility(View.GONE);
                    lnr_semifurnished.setVisibility(View.GONE);

                    //l1.setVisibility(View.VISIBLE);
                }
            }
        });

        tv_add_furnishing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  startActivity(new Intent(getContext(), AddFurnishing.class));
            }
        });
        return view;
    }
}