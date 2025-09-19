package com.app_neighbrsnook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WirteReviewActivity extends AppCompatActivity implements View.OnClickListener{

    RelativeLayout rating1_rl, rating2_rl,rating3_rl,rating4_rl, rating5_rl, review_rl;
    TextView t1,t2,t3,t4,t5;
    EditText write_review_et;
    ImageView back_btn,searchImageView, addImageView, star_iv, star_iv1, star_iv2, star_iv3, star_iv4;
    TextView titleTv, rating_tv, rating_tv2,rating_tv3,rating_tv4,rating_tv1;
    LinearLayout review_image_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wirte_review);

        titleTv = findViewById(R.id.title);
        titleTv.setText("Near By");
        searchImageView = findViewById(R.id.search_imageview);
        addImageView = findViewById(R.id.add_imageview);

        searchImageView.setVisibility(View.GONE);
        addImageView.setVisibility(View.GONE);
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        review_image_ll = findViewById(R.id.review_image_ll);
        review_rl = findViewById(R.id.review_rl);

        rating1_rl = findViewById(R.id.rating1_rl);
        rating2_rl = findViewById(R.id.rating2_rl);
        rating3_rl = findViewById(R.id.rating3_rl);
        rating4_rl = findViewById(R.id.rating4_rl);
        rating5_rl =  findViewById(R.id.rating5_rl);

        rating_tv = findViewById(R.id.rating_tv);
        rating_tv1 = findViewById(R.id.rating_tv1);
        rating_tv2 = findViewById(R.id.rating_tv2);
        rating_tv3 = findViewById(R.id.rating_tv3);
        rating_tv4 = findViewById(R.id.rating_tv4);

        star_iv = findViewById(R.id.star_iv);
        star_iv1 = findViewById(R.id.star_iv1);
        star_iv2 = findViewById(R.id.star_iv2);
        star_iv3 = findViewById(R.id.star_iv3);
        star_iv4 = findViewById(R.id.star_iv4);

        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);
        t4 = findViewById(R.id.t4);
        t5 = findViewById(R.id.t5);

        write_review_et = findViewById(R.id.write_review_et);
        rating1_rl.setOnClickListener(this);
        rating2_rl.setOnClickListener(this);
        rating3_rl.setOnClickListener(this);
        rating4_rl.setOnClickListener(this);
        rating5_rl.setOnClickListener(this);
        t1.setOnClickListener(this);
        t2.setOnClickListener(this);
        t3.setOnClickListener(this);
        t4.setOnClickListener(this);
        t5.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.rating1_rl:
                resetColor();
                hideLayout();
                rating1_rl.setBackgroundResource(R.drawable.rating_selected_round_corner);
                rating_tv.setTextColor(getResources().getColor(R.color.white));
                star_iv.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                break;

            case R.id.rating2_rl:
                resetColor();
                hideLayout();
                rating2_rl.setBackgroundResource(R.drawable.rating_selected_round_corner);
                rating_tv1.setTextColor(getResources().getColor(R.color.white));
                star_iv1.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                break;

            case R.id.rating3_rl:
                resetColor();
                hideLayout();
                rating3_rl.setBackgroundResource(R.drawable.rating_selected_round_corner);
                rating_tv2.setTextColor(getResources().getColor(R.color.white));
                star_iv2.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                break;

            case R.id.rating4_rl:
                resetColor();
                hideLayout();
                rating4_rl.setBackgroundResource(R.drawable.rating_selected_round_corner);
                rating_tv3.setTextColor(getResources().getColor(R.color.white));
                star_iv3.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                break;

            case R.id.rating5_rl:
                resetColor();
                hideLayout();
                rating5_rl.setBackgroundResource(R.drawable.rating_selected_round_corner);
                rating_tv4.setTextColor(getResources().getColor(R.color.white));
                star_iv4.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                break;

            case R.id.t1:

                break;


            case R.id.t2:

                break;

            case R.id.t3:

                break;

            case R.id.t4:

                break;

            case R.id.t5:

                break;

        }


    }

    private void hideLayout() {
        review_image_ll.setVisibility(View.GONE);
        review_rl.setVisibility(View.VISIBLE);
    }

    private void resetColor() {
        rating1_rl.setBackgroundResource(R.drawable.rating_unselected);
        rating2_rl.setBackgroundResource(R.drawable.rating_unselected);
        rating3_rl.setBackgroundResource(R.drawable.rating_unselected);
        rating4_rl.setBackgroundResource(R.drawable.rating_unselected);
        rating5_rl.setBackgroundResource(R.drawable.rating_unselected);

        rating_tv.setTextColor(getResources().getColor(R.color.text_color));
        rating_tv1.setTextColor(getResources().getColor(R.color.text_color));
        rating_tv2.setTextColor(getResources().getColor(R.color.text_color));
        rating_tv3.setTextColor(getResources().getColor(R.color.text_color));
        rating_tv4.setTextColor(getResources().getColor(R.color.text_color));

        rating_tv.setTextColor(getResources().getColor(R.color.text_color));
        rating_tv1.setTextColor(getResources().getColor(R.color.text_color));
        rating_tv2.setTextColor(getResources().getColor(R.color.text_color));
        rating_tv3.setTextColor(getResources().getColor(R.color.text_color));
        rating_tv4.setTextColor(getResources().getColor(R.color.text_color));

        star_iv.setColorFilter(ContextCompat.getColor(this, R.color.text_color), android.graphics.PorterDuff.Mode.MULTIPLY);
        star_iv1.setColorFilter(ContextCompat.getColor(this, R.color.text_color), android.graphics.PorterDuff.Mode.MULTIPLY);
        star_iv2.setColorFilter(ContextCompat.getColor(this, R.color.text_color), android.graphics.PorterDuff.Mode.MULTIPLY);
        star_iv3.setColorFilter(ContextCompat.getColor(this, R.color.text_color), android.graphics.PorterDuff.Mode.MULTIPLY);
        star_iv4.setColorFilter(ContextCompat.getColor(this, R.color.text_color), android.graphics.PorterDuff.Mode.MULTIPLY);

    }
}