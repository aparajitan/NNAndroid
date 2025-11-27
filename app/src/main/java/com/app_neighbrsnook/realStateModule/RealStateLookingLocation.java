package com.app_neighbrsnook.realStateModule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app_neighbrsnook.R;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class RealStateLookingLocation extends AppCompatActivity {
    EditText edt_search_city;
    TextView tv_delhi;
    RelativeLayout rl_location_post;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_state_looking_location);
        tv_delhi=findViewById(R.id.tv_delhi);
        rl_location_post=findViewById(R.id.location_rl_location);
        rl_location_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MapBottomSheetFragment bottomSheetDialog = MapBottomSheetFragment.newInstance();
//                bottomSheetDialog.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,
                        Place.Field.ADDRESS, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields).setCountry("IN")
                        .build(RealStateLookingLocation.this);
//                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
//                        .build(NearByActivity.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });
        tv_delhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RealStateLookingLocation.this,PGListingPages.class));
            }
        });
    }
}