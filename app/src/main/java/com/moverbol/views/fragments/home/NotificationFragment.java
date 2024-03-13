package com.moverbol.views.fragments.home;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.moverbol.HomeActivity;
import com.moverbol.R;
import com.moverbol.adapters.NotificationListAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.fragment.BaseFragment;
import com.moverbol.databinding.NotificationFragmentBinding;
import com.moverbol.model.notification.NotificationListResponse;
import com.moverbol.model.notification.NotificationModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.HomeViewModel;
import com.moverbol.views.activities.jobsummary.MoveProcessActivity;
import com.moverbol.views.activities.moveprocess.NotesActivity;
import com.moverbol.views.activities.moveprocess.bill_of_lading.BillOfLadingActivity;

import java.util.List;

import retrofit2.Call;

/**
 * Created by AkashM on 11/6/18.
 */
public class NotificationFragment extends BaseFragment implements NotificationListAdapter.NotificationItemClickedListener {


    private NotificationFragmentBinding binding;
    private HomeViewModel viewModel;
    private NotificationListAdapter adapter;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false);

        initialise();

        setViewModelObservers();

        setActionListener();

        if (viewModel.notificationListLive.getValue() == null) {
            callGetNotificationList();
        }

        return binding.getRoot();
    }

    private void callGetNotificationList() {
        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        showProgress();
        String subDomain = MoversPreferences.getInstance(getContext()).getSubdomain();
        String userId = MoversPreferences.getInstance(getContext()).getUserId();

        viewModel.getNotificationList(subDomain, userId, new ResponseListener<BaseResponseModel<NotificationListResponse>>() {
            @Override
            public void onResponse(BaseResponseModel<NotificationListResponse> response, String usedUrlKey) {
                hideProgress();
                MoversPreferences.getInstance(getContext()).setNotificationCount(0);
                try {
                    ((HomeActivity) getActivity()).setMenuCounter(MoversPreferences.getInstance(getContext()).getNotificationCount());
                } catch (Exception ignored) {

                }

            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(getContext());
                    return;
                }
                Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<NotificationListResponse>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void initialise() {
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        adapter = new NotificationListAdapter(this);

        binding.setAdapter(adapter);
    }

    private void setViewModelObservers() {
        viewModel.notificationListLive.observe(this.getActivity(), new Observer<List<NotificationModel>>() {
            @Override
            public void onChanged(@Nullable List<NotificationModel> notificationModels) {
                adapter.setHomeList(notificationModels);
            }
        });
    }

    private void setActionListener() {
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callGetNotificationList();
                binding.swipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onNotificationItemClicked(NotificationModel notificationModel, int adapterPosition) {

        if (notificationModel.getReadStatus().equals("0")) {
            callNotificationUpdate(notificationModel, adapterPosition);

        } else {
            startScreen(notificationModel, adapterPosition);
        }


    }

    private void callNotificationUpdate(NotificationModel notificationModel, int adapterPosition) {
        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        showProgress();
        String subDomain = MoversPreferences.getInstance(getContext()).getSubdomain();

        viewModel.notifcationUpdate(subDomain, notificationModel.getrId(), notificationModel.getUserId(), new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
                startScreen(notificationModel, adapterPosition);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(getContext());
                    return;
                }
                Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void startScreen(NotificationModel notificationModel, int adapterPosition) {
        adapter.updateNotificationRead(adapterPosition);
        Intent intent;
        switch (notificationModel.getNotificationType()) {
            case Constants.NotificationTypeIndexes.TYPE_BOL_CONFIRMATION:

                intent = new Intent(this.getContext(), BillOfLadingActivity.class);

                intent.putExtra(Constants.EXTRA_NOTIFICATION_INTENT, true);
                intent.putExtra(BillOfLadingActivity.EXTRA_BOL_APPROVAL_STATUS, notificationModel.getBolStatus());
                intent.putExtra(Constants.EXTRA_JOB_ID_KEY, notificationModel.getJobId());
                intent.putExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY, notificationModel.getOpportunityId());

                startActivity(intent);

                break;

            case Constants.NotificationTypeIndexes.TYPE_BOL_NEW_JOB:

            case Constants.NotificationTypeIndexes.TYPE_JOB_DELETE:
                if (getActivity() != null && getActivity() instanceof HomeActivity) {
                    getActivity().onBackPressed();
                }
                break;

            case Constants.NotificationTypeIndexes.TYPE_NOTES:
                NotesActivity.start(this.getContext(), notificationModel.getOpportunityId());
                break;

            case Constants.NotificationTypeIndexes.TYPE_ESTIMATE_AND_BOL_CHANGED:

                intent = new Intent(this.getContext(), MoveProcessActivity.class);

                intent.putExtra(Constants.EXTRA_NOTIFICATION_INTENT, true);
                intent.putExtra(Constants.EXTRA_JOB_ID_KEY, notificationModel.getJobId());
                intent.putExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY, notificationModel.getOpportunityId());


                MoversPreferences.getInstance(this.getContext()).setCurrentJobId(notificationModel.getJobId());
                MoversPreferences.getInstance(this.getContext()).setOpportunityId(notificationModel.getOpportunityId());


                startActivity(intent);

                break;
        }
    }
}
