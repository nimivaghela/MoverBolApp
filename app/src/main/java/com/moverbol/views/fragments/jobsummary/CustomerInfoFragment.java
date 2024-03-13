package com.moverbol.views.fragments.jobsummary;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.moverbol.R;
import com.moverbol.constants.Constants;
import com.moverbol.custom.fragment.BaseFragment;
import com.moverbol.databinding.CustomerInfoBinding;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.jobsummary.JobSummaryViewModel;
import com.moverbol.views.activities.PickupExtraStopActivity;

/**
 * Created by sumeet on 13/9/17.
 */

public class CustomerInfoFragment extends BaseFragment {

    private JobSummaryViewModel viewModel;
    private CustomerInfoBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(requireActivity()).get(JobSummaryViewModel.class);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_customer_info, container, false);
        binding.setCustomerInfoFragment(this);

        viewModel.getJobDetailLive().observe(getViewLifecycleOwner(), jobDetailPojo -> {
            binding.setCustomerInfo(jobDetailPojo.getCustomerInfo());
            binding.setPickupAdress(jobDetailPojo.getPickupadress());
            binding.setDeliveryAddress(jobDetailPojo.getDeliveryadress());
            binding.setPickUpExtraStopsCount(jobDetailPojo.getPickupExtraStops().size());
            binding.setDeliveryExtraStopsCount(jobDetailPojo.getDeliveryExtraStops().size());
        });


        binding.txtCustomerPhone1.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(binding.txtCustomerPhone1.getText().toString().trim())) {
                Util.startDialer(v.getContext(), binding.txtCustomerPhone1.getText().toString().trim());
            }
        });

        binding.txtCustomerPhone2.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(binding.txtCustomerPhone2.getText().toString().trim())) {
                Util.startDialer(v.getContext(), binding.txtCustomerPhone2.getText().toString().trim());
            }
        });

        binding.txtCustomerPhone3.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(binding.txtCustomerPhone3.getText().toString().trim())) {
                Util.startDialer(v.getContext(), binding.txtCustomerPhone3.getText().toString().trim());
            }
        });

        binding.txtPickupContactPhone.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(binding.txtPickupContactPhone.getText().toString().trim())) {
                Util.startDialer(v.getContext(), binding.txtPickupContactPhone.getText().toString().trim());
            }
        });

        binding.txtDeliveryContactPhone.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(binding.txtDeliveryContactPhone.getText().toString().trim())) {
                Util.startDialer(v.getContext(), binding.txtDeliveryContactPhone.getText().toString().trim());
            }
        });

        binding.txtPickupAddress.setOnClickListener(v -> {
            String address = binding.txtPickupAddress.getText().toString();
            Util.openMapForGivenAddress(address, getContext());
        });


        binding.txtDeliveryAddress.setOnClickListener(v -> {
            String address = binding.txtPickupAddress.getText().toString();
            Util.openMapForGivenAddress(address, getContext());
        });

        return binding.getRoot();
    }



    public void openPickUpExtraStop(View view) {
        String title;
        if (view.getId() == R.id.tv_delivery) {
            title = getString(R.string.delivery_extra_stops);
        } else {
            title = getString(R.string.pickup_extra_stops);
        }
        Intent intent = new Intent(getActivity(), PickupExtraStopActivity.class);
        intent.putExtra(Constants.EXTRA_TITLE, title);
        startActivity(intent);
    }



}
