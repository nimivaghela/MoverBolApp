package com.moverbol.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.moverbol.R;
import com.moverbol.constants.Constants;
import com.moverbol.databinding.HomeRvRowItemBinding;
import com.moverbol.interfaces.JobCardOperations;
import com.moverbol.model.ActivityDatePojo;
import com.moverbol.network.model.JobPojo;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sumeet on 28/8/17.
 */

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.HomeItemHolder> {

    private static final int MOVE_TYPE_INTERNATIONAL_ID = 1;
    private static final int MOVE_TYPE_HAS_STORAGE_ID = 1;
    private List<JobPojo> homeList;
    //TODO remove this
    private JobCardOperations jobCardOperations;
    private Context context;

    public HomeListAdapter(JobCardOperations jobCardOperations) {
        this.homeList = new ArrayList<>();
        this.jobCardOperations = jobCardOperations;
    }

    @Override
    public HomeItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_rv_item, parent, false);
        return new HomeItemHolder(v);
    }

    @Override
    public void onBindViewHolder(HomeItemHolder holder, int position) {

        JobPojo jobPojo = homeList.get(position);

        holder.homeRvRowItemBinding.setPosition(position);
        holder.homeRvRowItemBinding.setObj(jobPojo);

        if (jobPojo.jobStatus.equals(Constants.JOB_STATUS_ACCEPTED)) {
            holder.homeRvRowItemBinding.tvStartMove.setText(holder.itemView.getContext().getText(R.string.start_move));
        } else if (jobPojo.jobStatus.equals(Constants.JOB_STATUS_INPROGRESS)) {
            holder.homeRvRowItemBinding.tvStartMove.setText(holder.itemView.getContext().getText(R.string.resume_move));
        }

        if (jobPojo.getPaymentStatus()) {
            holder.homeRvRowItemBinding.tvStartMove.setText(holder.itemView.getContext().getText(R.string.complete_payment));
        }

        holder.homeRvRowItemBinding.executePendingBindings();

    }

    public void setHomeList(List<JobPojo> homeList) {
        this.homeList = homeList;
        notifyDataSetChanged();
    }

    public List<JobPojo> getHomeList() {
        return homeList;
    }

    @Override
    public int getItemCount() {
        return homeList == null ? 0 : homeList.size();
    }


    public class HomeItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        HomeRvRowItemBinding homeRvRowItemBinding;

        public HomeItemHolder(View itemView) {
            super(itemView);
            homeRvRowItemBinding = DataBindingUtil.bind(itemView);
            homeRvRowItemBinding.setOnClick(this);
            homeRvRowItemBinding.setCurrencySymbol(MoversPreferences.getInstance(context).getCurrencySymbol());


            homeRvRowItemBinding.txtDeliveryAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String deliveryAddress = homeRvRowItemBinding.txtDeliveryAddress.getText().toString();
                    Util.openMapForGivenAddress(deliveryAddress, v.getContext());
                }
            });


            homeRvRowItemBinding.txtPickupAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pickupAddress = homeRvRowItemBinding.txtPickupAddress.getText().toString();
                    Util.openMapForGivenAddress(pickupAddress, v.getContext());
                }
            });

        }


        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
            JobPojo jobPojo = homeList.get(adapterPosition);

            switch (v.getId()) {

                case R.id.tv_summary:
                    HomeListAdapter.this.jobCardOperations.showSummary(adapterPosition, jobPojo.jobId, jobPojo.opportunityId);
                    break;
                case R.id.tv_summary_big:
                    HomeListAdapter.this.jobCardOperations.showSummary(adapterPosition, jobPojo.jobId, jobPojo.opportunityId);
                    break;
                case R.id.tv_accept:
                    HomeListAdapter.this.jobCardOperations.acceptJob(adapterPosition, jobPojo.jobId, jobPojo.opportunityId);
                    break;
                case R.id.tv_reject:
                    HomeListAdapter.this.jobCardOperations.rejectJob(adapterPosition, jobPojo.jobId, jobPojo.opportunityId);
                    break;
                case R.id.tv_start_move:
                    boolean isMoveInternational = TextUtils.equals(jobPojo.moveTypeId, MOVE_TYPE_INTERNATIONAL_ID + "");
                    boolean isStorage = TextUtils.equals(jobPojo.storage, MOVE_TYPE_HAS_STORAGE_ID + "");
                    HomeListAdapter.this.jobCardOperations.startJob(adapterPosition, jobPojo.jobId, jobPojo.opportunityId, isStorage, isMoveInternational, jobPojo.moveTypeId, jobPojo.jobStatus);
                    break;
                case R.id.tv_pickup_extra_stops_count:
                    HomeListAdapter.this.jobCardOperations.showExtraStops(adapterPosition, jobPojo.jobId, jobPojo.opportunityId, Constants.ADDRESS_TYPE_PICK_UP);
                    break;
                case R.id.tv_delivery_extra_stops_count:
                    HomeListAdapter.this.jobCardOperations.showExtraStops(adapterPosition, jobPojo.jobId, jobPojo.opportunityId, Constants.ADDRESS_TYPE_DELIVERY);
                    break;
                case R.id.txt_more_dates:
//                    HomeListAdapter.this.jobCardOperations.showAditionalDates(getAdapterPosition());
                    openAdditionalDatesDialogue(getAdapterPosition());
                    break;
                case R.id.iv_phone1:
                case R.id.tv_number1:
                    if (!TextUtils.isEmpty(jobPojo.phoneNumber1)) {
                        Util.startDialer(v.getContext(), jobPojo.phoneNumber1);
                    }
                    break;
                case R.id.iv_phone2:
                case R.id.tv_number2:
                    if (!TextUtils.isEmpty(jobPojo.phoneNumber2)) {
                        Util.startDialer(v.getContext(), jobPojo.phoneNumber2);
                    }
                    break;
                case R.id.iv_phone3:
                case R.id.tv_number3:
                    if (!TextUtils.isEmpty(jobPojo.phoneNumber3)) {
                        Util.startDialer(v.getContext(), jobPojo.phoneNumber3);
                    }
                    break;
            }
        }
    }


//    public void openAcceptFilter(final View view) {
//        onClickListener.onClick(view);
//    }

//    public void openRejectFilter(View view) {
//        onClickListener.onClick(view);
//    }

   /* //TODO remove this
    public void showSummary(int adapterPosition) {

        String job_id = homeList.get(adapterPosition).jobId;

        HomeListAdapter.this.jobCardOperations.showSummary(job_id);
        *//*Intent jobSummaryIntent = new Intent(context, JobSummaryActivity.class);
        jobSummaryIntent.putExtra(Constants.EXTRA_JOB_ID_KEY, job_id);

        context.startActivity(jobSummaryIntent);*//*
    }


    public void startJob(int adapterPosition) {
        String job_id = homeList.get(adapterPosition).jobId;
        String jobStatus = homeList.get(adapterPosition).jobStatus;

        HomeListAdapter.this.jobCardOperations.startJob( adapterPosition, job_id, jobStatus);
    }

    public void showExtraStops(int adapterPosition) {
        String title = "";
        if (view.getId() == R.id.textView5) {
            title = context.getString(R.string.delivery_extra_stops);
        } else {
            title = context.getString(R.string.pickup_extra_stops);
        }

        int listPosition = (int) view.getTag();
        String job_id = homeList.get(listPosition).jobId;
        Intent openExtraStopsIntent = new Intent(context, PickupExtraStopActivity.class);
        openExtraStopsIntent.putExtra(Constants.EXTRA_JOB_ID_KEY, job_id);
        openExtraStopsIntent.putExtra(Constants.EXTRA_TITLE, title);

        context.startActivity(openExtraStopsIntent);
    }*/


    public void openAdditionalDatesDialogue(int adapterPosition) {

        String additionalDatesString = null;
        List<ActivityDatePojo> activityDatePojoList = homeList.get(adapterPosition).additionalDates;

        for (int i = 0; i < activityDatePojoList.size(); i++) {
            ActivityDatePojo pojo = activityDatePojoList.get(i);
            if (additionalDatesString == null) {
                additionalDatesString = pojo.toString();
            } else {
                additionalDatesString = additionalDatesString + "\n \n" + pojo.toString();
            }
        }

        HomeListAdapter.this.jobCardOperations.showAditionalDates(adapterPosition, homeList.get(adapterPosition).jobId, homeList.get(adapterPosition).opportunityId, additionalDatesString);

        /*AlertDialog alertDialog = new AlertDialog.Builder(view.getContext())
                .setMessage(additionalDatesString)
                .setCancelable(true)
                .create();

        alertDialog.show();*/
    }

}
