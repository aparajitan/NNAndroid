package com.app_neighbrsnook.realStateModule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app_neighbrsnook.R;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class RealStateFilterPg extends AppCompatActivity {
    ImageView img_back_arrow;
    FrameLayout frm_button_submit;
    TextView tv_change_location;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    FrameLayout frm_single,frm_sharing,frm_all;
    Boolean is_single_room =false;
    Boolean is_sharing_room=false;
    Boolean is_all_room=false;
    LinearLayout frm_gone;
    String propert_type = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_state_filter_pg);
        img_back_arrow=findViewById(R.id.back_arrow_id);
        frm_button_submit=findViewById(R.id.upload_id);
        tv_change_location=findViewById(R.id.change_location_id_filter);
        frm_single=findViewById(R.id.frm_single_room);
        frm_sharing=findViewById(R.id.frm_sharing_room);
        frm_all=findViewById(R.id.frm_all);
        frm_gone=findViewById(R.id.frm_gone);
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
                    //l1.setVisibility(View.VISIBLE);
                }
            }
        });
        tv_change_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,
                        Place.Field.ADDRESS, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields).setCountry("IN")
                        .build(RealStateFilterPg.this);
//                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
//                        .build(NearByActivity.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });
        frm_button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RealStateFilterPg.this,PgViewProperties.class));
            }
        });
        img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}