package com.app_neighbrsnook.nearBy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.SliderAdapterExample;
import com.app_neighbrsnook.libraries.slider.IndicatorView.animation.type.IndicatorAnimationType;
import com.app_neighbrsnook.libraries.slider.SliderAnimations;
import com.app_neighbrsnook.libraries.slider.SliderView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class NearByBusinessActivity extends AppCompatActivity {
    ImageView back_btn,searchImageView, addImageView;
    TextView titleTv, business_review, report_tv;
    private SliderAdapterExample adapter;
    private SliderView sliderView;
    private int[] images;
    private String[] text;
    PDFView pdfView;
    LinearLayout review_ll;
    Dialog image_dialog;
    String pdfurl = "https://upload.wikimedia.org/wikiversity/en/5/5e/Sample_Task_Analysis-2.pdf";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_business);
        titleTv = findViewById(R.id.title);
        titleTv.setText("Near By");
        searchImageView = findViewById(R.id.search_imageview);
        addImageView = findViewById(R.id.add_imageview);
        pdfView = findViewById(R.id.pdfView);
        searchImageView.setVisibility(View.GONE);
        addImageView.setVisibility(View.GONE);
        back_btn = findViewById(R.id.back_btn);
        review_ll = findViewById(R.id.review_ll);
        review_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(NearByBusinessActivity.this, NearByReviewActivity.class);
                startActivity(i);
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sliderView=findViewById(R.id.imageSlider);
        images= new int[]{R.drawable.saloon_img, R.drawable.restaurent_img, R.drawable.spa_img};
        text=new String[]{"","",""};
        adapter=new SliderAdapterExample(images,text);
        sliderView.setSliderAdapter(adapter);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.SLIDE);
        sliderView.startAutoCycle();



        pdfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                documentDialog();
            }
        });
        new RetrivePDFfromUrl().execute(pdfurl);
    }

    private void documentDialog() {
        RecyclerView rv;
        TextView confirm;
        ImageView cancel;

        image_dialog = new Dialog(this);
        image_dialog.setContentView(R.layout.open_doc_dialog);
        cancel = image_dialog.findViewById(R.id.cross_imageview);
        pdfView = image_dialog.findViewById(R.id.pdfView);
        int pageNumber = 0;
//        pdfView.fromUri(pdf)
//                .defaultPage(pageNumber)
//                .enableSwipe(true)
//                .swipeHorizontal(true)
//                .enableAnnotationRendering(true)
//                .load();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_dialog.cancel();
            }
        });
        new RetrivePDFfromUrl().execute(pdfurl);
        image_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(NearByBusinessActivity.this, android.R.color.transparent)));
        image_dialog.setCancelable(false);
        image_dialog.show();
    }

    private class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            pdfView.fromStream(inputStream).load();
//            pdfView1.fromStream(inputStream).load();
        }
    }
}