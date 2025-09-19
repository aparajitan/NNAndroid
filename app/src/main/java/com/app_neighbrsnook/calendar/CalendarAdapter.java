package com.app_neighbrsnook.calendar;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.model.CalendarPOJO;

import java.time.LocalDate;
import java.util.ArrayList;
class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{
    private final ArrayList<CalendarPOJO> days;
    private final OnItemListener onItemListener;
    public CalendarAdapter(ArrayList<CalendarPOJO> days, OnItemListener onItemListener)
    {
        this.days = days;
        this.onItemListener = onItemListener;
    }
    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if(days.size() > 15) //month view
            layoutParams.height = (int) (parent.getHeight() * 0.1222222);
        else // week view
            layoutParams.height = parent.getHeight();
        return new CalendarViewHolder(view, onItemListener, days);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position)
    {
        final LocalDate date = days.get(position).localDate;
        if(date == null)
            holder.dayOfMonth.setText("");
        else
        {
            holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));
            if (days.get(position).isSelected){
                holder.parentView.setBackgroundResource(R.drawable.congratulation_desgin);
            }else if(date.equals(CalendarUtils.selectedDate)) {
                holder.parentView.setBackgroundResource(R.drawable.calendar_by_default_background);
            }
           // holder.parentView.getBackground().setColorFilter(Color.rgb(40, 50, 60), PorterDuff.Mode.SRC_ATOP);
        }
    }
    @Override
    public int getItemCount()
    {
        return days.size();
    }
    public interface  OnItemListener
    {
        void onItemClick(int position, CalendarPOJO date);
    }
}
