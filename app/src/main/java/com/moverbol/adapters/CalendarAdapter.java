package com.moverbol.adapters;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.constants.Constants;
import com.moverbol.databinding.CalendarItemBinding;
import com.moverbol.network.model.JobPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sumeet on 18/9/17.
 */

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.HomeItemHolder> {

/*    private ObservableArrayList<CalendarPojo> calendarPojos;
    private RecyclerView rvHome;
    private Context context;

    public CalendarAdapter(RecyclerView rvHome, ObservableArrayList<CalendarPojo> calendarPojos) {
        this.calendarPojos = calendarPojos;
        this.rvHome = rvHome;
        this.context = rvHome.getContext();
    }*/

    //    private List<CalendarPojo> calendarPojos;
    private List<JobPojo> calendarPojos;
    private OnJobEventClickListener jobEventClickListener;

    public interface OnJobEventClickListener{
        void onClick(JobPojo jobPojo);
    }

    public CalendarAdapter(OnJobEventClickListener jobEventClickListener) {
        this.calendarPojos = new ArrayList<>();
        this.jobEventClickListener = jobEventClickListener;
    }

    @Override
    public CalendarAdapter.HomeItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_calendar_item, parent, false);
        return new CalendarAdapter.HomeItemHolder(v);
    }

    @Override
    public void onBindViewHolder(CalendarAdapter.HomeItemHolder holder, int position) {
//        holder.calendarItemBinding.setCalendarPojo(calendarPojos.get(position));
        holder.calendarItemBinding.setObj(calendarPojos.get(position));
        Log.d(Constants.BASE_LOG_TAG + "hour", calendarPojos.get(position).getStartTimeHourIn24Format() + "");
        holder.calendarItemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if (calendarPojos == null) {
            return 0;
        } else {
            return calendarPojos.size();
        }
    }


    /*public void setCalendarPojos(List<CalendarPojo> calendarPojos) {
        this.calendarPojos = calendarPojos;
        notifyDataSetChanged();
    }*/

    public void setCalendarPojos(List<JobPojo> calendarPojos) {
        this.calendarPojos = calendarPojos;
        notifyDataSetChanged();
    }

    public class HomeItemHolder extends RecyclerView.ViewHolder {
        public CalendarItemBinding calendarItemBinding;

        public HomeItemHolder(View itemView) {
            super(itemView);
            calendarItemBinding = DataBindingUtil.bind(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jobEventClickListener.onClick(calendarPojos.get(getAdapterPosition()));
                }
            });
        }
    }

}
