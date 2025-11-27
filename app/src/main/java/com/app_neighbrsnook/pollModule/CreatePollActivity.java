package com.app_neighbrsnook.pollModule;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app_neighbrsnook.abusive.BadWordFilter;
import com.app_neighbrsnook.apiService.UrlClass;
import com.app_neighbrsnook.event.CreateEvent;
import com.app_neighbrsnook.model.pollDetailResponce.Option;
import com.app_neighbrsnook.model.pollDetailResponce.PollDetailPojo;
import com.google.gson.JsonElement;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.model.PollModel;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.SharedPrefsManager;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePollActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView back_btn, add_imageview, search_imageview;
    TextView titleTv, piblish_textview, start_date_tv, end_date_tv;
    EditText poll_question_et, poll_title_et, option_one_et, option_two_et, option_three_et, option_four_et;
    PollModel pollModel;
    HashMap<String, Object> hm = new HashMap<>();
    SharedPrefsManager sm;
    Boolean isStartDate = false;
    String sdate, edate;
    ProgressDialog progressDialog;
    LinearLayout root;
    Context context;
    Activity activity;
    String title, from, pollId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = activity = this;
        setContentView(R.layout.activity_create_poll);

        item();

        sm = new SharedPrefsManager(this);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        from = intent.getStringExtra("from");
        pollId = intent.getStringExtra("pollId");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        titleTv.setText("Create Poll");

        if (from.equals("PollEdit")) {
            pollDetilApi();
        }

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard1();
                return false;
            }
        });
    }

    private void pollDetilApi() {
        progressDialog.show();
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("poll_id", pollId);
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<PollDetailPojo> call = service.pollLDetail("polldetail", hm);
        call.enqueue(new Callback<PollDetailPojo>() {
            @Override
            public void onResponse(Call<PollDetailPojo> call, Response<PollDetailPojo> response) {
                PollDetailPojo rootObject = response.body();
                try {
                    pollId = rootObject.getpId();
                    poll_question_et.setText(rootObject.getPollQues());
                    start_date_tv.setText(rootObject.getStartDate());
                    end_date_tv.setText(rootObject.getEndDate());

                    List<Option> options = rootObject.getOptions();
                    if (options != null && options.size() > 0) {
                        option_one_et.setText(options.size() > 0 ? options.get(0).getOption() : "");
                        option_two_et.setText(options.size() > 1 ? options.get(1).getOption() : "");
                        option_three_et.setText(options.size() > 2 ? options.get(2).getOption() : "");
                        option_four_et.setText(options.size() > 3 ? options.get(3).getOption() : "");
                    }

                } catch (Exception e) {

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<PollDetailPojo> call, Throwable t) {
                Toast.makeText(CreatePollActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    private void hideKeyboard1() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    private void item() {
        root = findViewById(R.id.root);
        titleTv = findViewById(R.id.title);
        back_btn = findViewById(R.id.back_btn);
        piblish_textview = findViewById(R.id.piblish_textview);
        search_imageview = findViewById(R.id.search_imageview);
        add_imageview = findViewById(R.id.add_imageview);
        start_date_tv = findViewById(R.id.start_date_tv);
        end_date_tv = findViewById(R.id.end_date_tv);
        poll_title_et = findViewById(R.id.poll_title_et);
        poll_question_et = findViewById(R.id.poll_question_et);
        option_one_et = findViewById(R.id.option_one_et);
        option_two_et = findViewById(R.id.option_two_et);
        option_three_et = findViewById(R.id.option_three_et);
        option_four_et = findViewById(R.id.option_four_et);

        search_imageview.setVisibility(View.GONE);
        add_imageview.setVisibility(View.GONE);

        back_btn.setOnClickListener(this);
        start_date_tv.setOnClickListener(this);
        end_date_tv.setOnClickListener(this);
        piblish_textview.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.back_btn:
                onBackPressed();
                break;

            case R.id.piblish_textview:

                if (from.equals("PollEdit")){
                    if (CheckAllFields()) {
                        SimpleDateFormat dateTimeInGMT = new SimpleDateFormat("dd MMM hh:mm aa");
                        dateTimeInGMT.setTimeZone(TimeZone.getDefault());
                        pollModel = new PollModel();
                        pollModel.setCurrent_date_time(dateTimeInGMT.format(new Date()));
                        pollModel.setPool_start_date(start_date_tv.getText().toString());
                        pollModel.setPool_end_date(end_date_tv.getText().toString());
                        pollModel.setPoll_question(poll_question_et.getText().toString());
                        pollModel.setPoll_option1(option_one_et.getText().toString());
                        pollModel.setPoll_option2(option_two_et.getText().toString());
                        pollModel.setPoll_option3(option_three_et.getText().toString());
                        pollModel.setPoll_option4(option_four_et.getText().toString());
                        sdate = convertToStandardDateFormat(start_date_tv.getText().toString());
                        edate = convertToStandardDateFormat(end_date_tv.getText().toString());
                        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
                        hm.put("poll_id", pollId);
                        hm.put("start_date", sdate);
                        hm.put("end_date", edate);
                        if (poll_question_et.getText().toString().matches(".*\\?.*")) {
                            hm.put("title", poll_question_et.getText().toString());
                            Log.d("poll_ques", poll_question_et.getText().toString());
                        } else {
                            hm.put("title", poll_question_et.getText().toString() + " ?");
                            Log.d("poll_ques", poll_question_et.getText().toString() + " ?");
                        }
                        if (poll_question_et.getText().toString().matches(".*\\?.*")) {
                            hm.put("poll_ques", poll_question_et.getText().toString());
                            Log.d("poll_ques", poll_question_et.getText().toString());
                        } else {
                            hm.put("poll_ques", poll_question_et.getText().toString() + " ?");
                            Log.d("poll_ques", poll_question_et.getText().toString() + " ?");
                        }
                        hm.put("firstoption", option_one_et.getText().toString());
                        hm.put("secondoption", option_two_et.getText().toString());
                        hm.put("thirdoption", option_three_et.getText().toString());
                        hm.put("fourthoption", option_four_et.getText().toString());

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            if (compairDate()) {
                                editPollApi(hm);
                            }
                        }
                    }
                } else {
                    if (CheckAllFields()) {
                        SimpleDateFormat dateTimeInGMT = new SimpleDateFormat("dd MMM hh:mm aa");
                        dateTimeInGMT.setTimeZone(TimeZone.getDefault());
                        pollModel = new PollModel();
                        pollModel.setCurrent_date_time(dateTimeInGMT.format(new Date()));
                        pollModel.setPool_start_date(start_date_tv.getText().toString());
                        pollModel.setPool_end_date(end_date_tv.getText().toString());
                        pollModel.setPoll_question(poll_question_et.getText().toString());
                        pollModel.setPoll_option1(option_one_et.getText().toString());
                        pollModel.setPoll_option2(option_two_et.getText().toString());
                        pollModel.setPoll_option3(option_three_et.getText().toString());
                        pollModel.setPoll_option4(option_four_et.getText().toString());
                        sdate = formateDate(start_date_tv.getText().toString());
                        edate = formateDate(end_date_tv.getText().toString());
                        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
                        hm.put("title", poll_title_et.getText().toString().trim());
                        hm.put("start_date", sdate);
                        hm.put("end_date", edate);
                        if (poll_question_et.getText().toString().matches(".*\\?.*")) {
                            hm.put("poll_ques", poll_question_et.getText().toString());
                            Log.d("poll_ques", poll_question_et.getText().toString());
                        } else {
                            hm.put("poll_ques", poll_question_et.getText().toString() + " ?");
                            Log.d("poll_ques", poll_question_et.getText().toString() + " ?");
                        }
                        hm.put("firstoption", option_one_et.getText().toString());
                        hm.put("secondoption", option_two_et.getText().toString());
                        hm.put("thirdoption", option_three_et.getText().toString());
                        hm.put("fourthoption", option_four_et.getText().toString());
                        hm.put("neighbrhood", sm.getString("neighbrhood"));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            if (compairDate()) {
                                createPollApi(hm);
                            }
                        }
                    }
                }
                break;
            case R.id.start_date_tv:
                GlobalMethods.calenderPicker2(this, start_date_tv);
                break;

            case R.id.end_date_tv:
                GlobalMethods.calenderPicker2(this, end_date_tv);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean compairDate() {
        String startDate = sdate;
        String endDate = edate;

        LocalDate d1 = LocalDate.parse(startDate);
        LocalDate d2 = LocalDate.parse(endDate);

        if (d1.isAfter(d2)) {
            GlobalMethods.getInstance(CreatePollActivity.this).globalDialog(CreatePollActivity.this, "End date should be greater than start date");
            return false;
        } else if (d1.isBefore(d2)) {
            return true;
        } else if (d1.isEqual(d2)) {
//            GlobalMethods.getInstance(CreatePollActivity.this).globalDialog(CreatePollActivity.this, "End date should be greater than start date");
            return true;
        }
        return true;
    }

    private boolean CheckAllFields() {
        if (poll_question_et.getText().toString().matches("")) {
            return GlobalMethods.setError(poll_question_et, "Please enter poll title");
        }else if (BadWordFilter.containsBadWord(poll_question_et.getText().toString())) {
            GlobalMethods.getInstance(CreatePollActivity.this).globalDialogAbusiveWord(CreatePollActivity.this, getString(R.string.abusive_msg));
            return false;
        } else if (start_date_tv.getText().toString().equals("Start date")) {
            Toast.makeText(CreatePollActivity.this, "Please select poll open date", Toast.LENGTH_SHORT).show();
            return false;
        } else if (end_date_tv.getText().toString().equals("End date")) {
            Toast.makeText(CreatePollActivity.this, "Please select poll close date", Toast.LENGTH_SHORT).show();
            return false;
        } else if (start_date_tv.getText().toString().matches(""))  {
            start_date_tv.setError("Please enter poll start date");
            return true;
        } else if (end_date_tv.getText().toString().matches("")) {
            end_date_tv.setError("Please enter poll end date");
            return true;
        } else if (poll_question_et.getText().toString().matches("")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                compairDate();
            }
            return GlobalMethods.setError(poll_question_et, "Please enter poll question");
        } else if (option_one_et.getText().toString().matches("")) {
            return GlobalMethods.setError(option_one_et, "Please enter poll option 1");
        } else if (option_two_et.getText().toString().matches("")) {
            return GlobalMethods.setError(option_two_et, "Please enter poll option 2");
        } else if (option_one_et.getText().toString().equals(option_two_et.getText().toString())) {
            return GlobalMethods.setError(option_two_et, "Poll option 1 and 2 cannot be the same");
        } else if (option_one_et.getText().toString().equals(option_three_et.getText().toString())) {
            return GlobalMethods.setError(option_three_et, "Poll option 1 and 3 cannot be the same");
        } else if (option_one_et.getText().toString().equals(option_four_et.getText().toString())) {
            return GlobalMethods.setError(option_four_et, "Poll option 1 and 4 cannot be the same");
        } else if (option_two_et.getText().toString().equals(option_three_et.getText().toString())) {
            return GlobalMethods.setError(option_three_et, "Poll option 2 and 3 cannot be the same");
        } else if (option_two_et.getText().toString().equals(option_four_et.getText().toString())) {
            return GlobalMethods.setError(option_four_et, "Poll option 2 and 4 cannot be the same");
        } else if (!option_three_et.getText().toString().isEmpty() &&
                !option_four_et.getText().toString().isEmpty() &&
                option_three_et.getText().toString().equals(option_four_et.getText().toString())) {
            return GlobalMethods.setError(option_four_et, "Poll option 3 and 4 cannot be the same");
        }

        return true;
    }

    private void createPollApi(HashMap<String, Object> hm) {
        progressDialog.show();
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<JsonElement> call = service.createPoll("createpoll", hm);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    checkOtp();
                    onBackPressed();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(CreatePollActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res---", t.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    private void editPollApi(HashMap<String, Object> hm) {
        progressDialog.show();
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<JsonElement> call = service.createPoll("editpoll", hm);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    checkOtp();
                    Intent pollDetailIntent = new Intent(CreatePollActivity.this, PollActivity.class);
                    finish();
                    pollDetailIntent.putExtra("from", "drawar");
                    startActivity(pollDetailIntent);
               /*     Intent pollDetailIntent = new Intent(CreatePollActivity.this, PollDetailActivity.class);
                    finish();
                    pollDetailIntent.putExtra("id", Integer.parseInt(pollId));
                    startActivity(pollDetailIntent);*/
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(CreatePollActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res---", t.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    String returnDate;

    private String formateDate(String convertDate) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate date = LocalDate.parse(convertDate, inputFormatter);
            returnDate = date.format(outputFormatter);
            Log.d("returnDate....", returnDate);
        }

        return returnDate;
    }

    private String convertToStandardDateFormat(String dateText) {
        String[] inputFormats = {"dd-MM-yyyy", "dd MMM, yyyy"};
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = null;

        for (String format : inputFormats) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat(format, Locale.getDefault());
                date = inputFormat.parse(dateText);
                if (date != null) break;
            } catch (ParseException e) {
                // Continue to the next format
            }
        }

        return date != null ? outputFormat.format(date) : "";
    }

    public void checkOtp() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlClass.MAIL_API, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("dfadfkjkkjkjksa", response);
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    status.equals("success");


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("fasdfafsd", error.toString());
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(context).add(stringRequest);
    }
}