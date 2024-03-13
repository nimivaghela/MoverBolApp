package com.moverbol.views.activities.moveprocess.bill_of_lading;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.moverbol.R;
import com.moverbol.adapters.CarryForwardDetailsAdapter;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.ActivityCarryForwardDetailsBinding;
import com.moverbol.model.CarryForwardModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.CarryForwardDetailsVM;

import java.util.ArrayList;

import retrofit2.Call;

public class CarryForwardDetailsActivity extends BaseAppCompactActivity {

    private ActivityCarryForwardDetailsBinding mBinding;
    private CarryForwardDetailsVM carryForwardDetailsVM;
    private CarryForwardDetailsAdapter adapter;


    public static void start(Context context) {
        Intent starter = new Intent(context, CarryForwardDetailsActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_carry_forward_details);
        setToolbar(mBinding.toolbar.toolbar, getString(R.string.carried_forward_amounts), R.drawable.ic_arrow_back_white_24dp);
        carryForwardDetailsVM = new ViewModelProvider(this).get(CarryForwardDetailsVM.class);
        adapter = new CarryForwardDetailsAdapter();
        mBinding.rcvCarryForward.setAdapter(adapter);
        loadData();

    }


    private void loadData() {
        if (carryForwardDetailsVM.listLiveData == null) {
            callCarryForwardList();
        }

        carryForwardDetailsVM.listLiveData.observe(this, carryForwardModels -> {
                    mBinding.setIsEmpty(carryForwardModels.isEmpty());
                    adapter.updateList(carryForwardModels);
                }


        );
    }

    private void callCarryForwardList() {
        showProgress();
        carryForwardDetailsVM.callCarryForwardList(
                MoversPreferences.getInstance(this).getSubdomain(),
                MoversPreferences.getInstance(this).getUserId(),
                MoversPreferences.getInstance(this).getOpportunityId(), new ResponseListener<BaseResponseModel<ArrayList<CarryForwardModel>>>() {
                    @Override
                    public void onResponse(BaseResponseModel<ArrayList<CarryForwardModel>> response, String usedUrlKey) {
                        hideProgress();
                        carryForwardDetailsVM.listLiveData.setValue(response.getData());
                    }

                    @Override
                    public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                        hideProgress();
                        Util.showSnackBar(mBinding.getRoot(), serverResponseError.getMessage());
                    }

                    @Override
                    public void onFailure(Call<BaseResponseModel<ArrayList<CarryForwardModel>>> call, Throwable t, String message) {
                        hideProgress();
                        Util.showSnackBar(mBinding.getRoot(), message);
                    }
                });
    }


}