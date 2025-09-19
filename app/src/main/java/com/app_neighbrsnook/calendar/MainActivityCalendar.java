package com.app_neighbrsnook.calendar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.database.DAO;
import com.app_neighbrsnook.database.Database;
import com.app_neighbrsnook.event.CreateEvent;
import com.app_neighbrsnook.model.CalendarPOJO;
import com.app_neighbrsnook.model.CreateEventModule;
import com.app_neighbrsnook.utils.UtilityFunction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.app_neighbrsnook.calendar.CalendarUtils.daysInMonthArray;
import static com.app_neighbrsnook.calendar.CalendarUtils.monthYearFromDate;
import static com.app_neighbrsnook.calendar.CalendarUtils.selectedDate;

public class MainActivityCalendar extends AppCompatActivity implements CalendarAdapter.OnItemListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    ImageView img_plus,img_back;
    FrameLayout tv_event_toolbar;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout_activity);
        selectedDate=LocalDate.now();
        mydao = Database.createDBInstance(this).getDao();
        initWidgets();
        //  setMonthView();
    }
    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendar_recycler_view);
        monthYearText = findViewById(R.id.monthYearTV);
        img_plus=findViewById(R.id.create_event_id);
        img_back=findViewById(R.id.img_back);
        tv_event_toolbar=findViewById(R.id.id_event);
    }
    @Override
    protected void onStart() {
        super.onStart();
        ReadData readData = new ReadData();
        readData.execute();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        }
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);
        ArrayList<CalendarPOJO> calendarPOJOS=new ArrayList<>();
        // Here write code to get saved events
        System.out.println(new Gson().toJson(createEventModules));
        for (LocalDate localDate : daysInMonth){
            Boolean isSelected=false;
            if (localDate!=null){
               //Log.e("fdads",localDate.toString());
               String date = UtilityFunction.formatDate(localDate.toString(),"yyyy-MM-dd","dd-MM-yyyy");
               //.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));//day+"-"+month+"-"+year ;
                Log.e("Date",date);
               if (createEventModules.size()>0){
                   for (CreateEventModule event : createEventModules){
                       if (event.getEvent_start_date().equals(date)) {
                           isSelected = true;
                           break;
                       }
                   }
               }
           }
            calendarPOJOS.add(new CalendarPOJO(localDate,isSelected));
        }

        CalendarAdapter calendarAdapter = new CalendarAdapter(calendarPOJOS, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        tv_event_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  startActivity(new Intent(MainActivityCalendar.this,  EventDateVise.class));
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivityCalendar.this, CreateEvent.class));
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousMonthAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextMonthAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, CalendarPOJO date)
    {
        if(date != null && date.localDate != null)
        {
            CalendarUtils.selectedDate = date.localDate;
            setMonthView();
        }
    }
    public void weeklyAction(View view)
    {
        startActivity(new Intent(this, WeekViewActivity.class));
    }
    List<CreateEventModule> createEventModules = new ArrayList<>();
    private DAO mydao;
    private class ReadData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... recurrences) {
            createEventModules = new ArrayList<>();
            createEventModules = mydao.getAllCreateEvent();
           /* Collections.sort(createEventModules, new Comparator<CreateEventModule>() {
                @Override
                public int compare(CreateEventModule lhs, CreateEventModule rhs) {
                    return Integer.compare( rhs.getId(),lhs.getId());
                }
            });*/
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setMonthView();
            }
            //  rv_event.setAdapter(new MyEventsAdapter(createEventModules, MyEvents.this));
        }
    }
}