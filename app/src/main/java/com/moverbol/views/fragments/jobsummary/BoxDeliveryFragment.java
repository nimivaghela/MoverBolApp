package com.moverbol.views.fragments.jobsummary;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.moverbol.R;
import com.moverbol.constants.Constants;
import com.moverbol.custom.fragment.BaseFragment;
import com.moverbol.databinding.BoxDeliveryBinding;
import com.moverbol.model.JobDetailPojo;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.jobsummary.JobSummaryViewModel;
import com.moverbol.views.activities.jobsummary.CommentListActivity;
import com.moverbol.views.activities.jobsummary.CrewListActivity;
import com.moverbol.views.activities.jobsummary.MaterialsListActivity;
import com.moverbol.views.activities.jobsummary.TrucksListActivity;

/**
 * Created by sumeet on 13/9/17.
 */

public class BoxDeliveryFragment extends BaseFragment {

    private JobSummaryViewModel viewModel;
    private BoxDeliveryBinding boxDeliveryBinding;
    private boolean shouldRefreshData = false;
    private int mMoveStageDetailsPojoListPosition = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        viewModel = ViewModelProviders.of(getActivity()).get(JobSummaryViewModel.class);

        boxDeliveryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_box_delivery, container, false);
        boxDeliveryBinding.setBoxDeliveryFragment(this);

        if (getArguments() != null) {
            mMoveStageDetailsPojoListPosition = getArguments().getInt(Constants.KEY_LIST_POSITION);
        }

        viewModel.getJobDetailLive().observe(this, new Observer<JobDetailPojo>() {
            @Override
            public void onChanged(@Nullable JobDetailPojo jobDetailPojo) {
                try {
                    boxDeliveryBinding.setMoveInfoDetails(jobDetailPojo.getMoveStageDetailsPojoList().get(mMoveStageDetailsPojoListPosition));

                } catch (Exception e) {
                    Util.showLog(this.getClass().getSimpleName(), e.getLocalizedMessage());
                }

                /*boxDeliveryBinding.setCrewDetailsSize(jobDetailPojo.getCrews().size());
                boxDeliveryBinding.setMaterialDetailsSize(jobDetailPojo.getMaterials().size());
                boxDeliveryBinding.setTruckDetailsSize(jobDetailPojo.getTrucks().size());
                boxDeliveryBinding.setCommentsSize(jobDetailPojo.getComments().size());*/
            }
        });

        if (viewModel.getJobDetailLive().getValue() != null) {
            boxDeliveryBinding.setMoveInfoDetails(viewModel.getJobDetailLive().getValue().getMoveStageDetailsPojoList().get(mMoveStageDetailsPojoListPosition));

        }

        //hide some tabs in case of user being a contractor
        boolean isUserAContractor = false;

        if (MoversPreferences.getInstance(this.getContext()).getUserDesignationType() != null) {
            isUserAContractor = TextUtils.equals(MoversPreferences.getInstance(this.getContext()).getUserDesignationType(), Constants.UserDesignationTypes.DESIGNATION_TYPE_CONTRACTOR);
        }
        boxDeliveryBinding.setIsUserAContractor(isUserAContractor);


        return boxDeliveryBinding.getRoot();
    }


    @Override
    public void onStart() {
        super.onStart();
        //Because when it returns after deleting or adding the crew, truck or material it should
        //get jobdetails again from WebService manager
        if (shouldRefreshData) {
            viewModel.setJobDetailLive();
        }
        shouldRefreshData = false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void openCrewDetail(View view) {
        startActivity(new Intent(getActivity(), CrewListActivity.class).putExtra(Constants.KEY_LIST_POSITION, mMoveStageDetailsPojoListPosition));
        shouldRefreshData = true;
    }

    public void openTruckDetail(View view) {
        startActivity(new Intent(getActivity(), TrucksListActivity.class).putExtra(Constants.KEY_LIST_POSITION, mMoveStageDetailsPojoListPosition));
        shouldRefreshData = true;
    }

    public void openMaterialDetail(View view) {
        startActivity(new Intent(getActivity(), MaterialsListActivity.class).putExtra(Constants.KEY_LIST_POSITION, mMoveStageDetailsPojoListPosition));
        shouldRefreshData = true;
    }

    public void openCommentDetail(View view) {
        startActivity(new Intent(getActivity(), CommentListActivity.class).putExtra(Constants.KEY_LIST_POSITION, mMoveStageDetailsPojoListPosition));
        shouldRefreshData = true;
    }
}
