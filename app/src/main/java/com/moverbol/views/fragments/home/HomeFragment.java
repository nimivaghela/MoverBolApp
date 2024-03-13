package com.moverbol.views.fragments.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.moverbol.HomeActivity;
import com.moverbol.R;
import com.moverbol.adapters.HomeListAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.dialogs.FilterDialogFragment;
import com.moverbol.custom.dialogs.RejectDialogFragment;
import com.moverbol.custom.fragment.BaseFragment;
import com.moverbol.databinding.HomeFragmentBinding;
import com.moverbol.interfaces.JobCardOperations;
import com.moverbol.model.billOfLading.BolDetailsPojo;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.network.model.JobPojo;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.HomeViewModel;
import com.moverbol.views.activities.JobSummaryActivity;
import com.moverbol.views.activities.PickupExtraStopActivity;
import com.moverbol.views.activities.jobsummary.MoveProcessActivity;
import com.moverbol.views.activities.moveprocess.PaymentActivity;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import retrofit2.Call;

/**
 * Created by sumeet on 25/8/17.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener, FilterDialogFragment.OnSubmitListener {

    public static final int RESULT_CODE_MOVE_PROCESS_API_CALL_ERROR = 100;
    private HomeFragmentBinding binding;
    private HomeViewModel viewModel;
    private ObservableArrayList<JobPojo> jobPojoObservables;
    private RejectDialogFragment rejectDialogFragment;
    private final String jobStatusCode = "";
    int positionToScroll = 0;
    private boolean mShowOnlyJobsInProgress;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        setHasOptionsMenu(true);
        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void initialise() {
        binding.setAdapter(new HomeListAdapter(new JobCardOperations() {

            @Override
            public void acceptJob(int adapterPosition, String jobId, String opportunityId) {

                if (MoversPreferences.getInstance(mContext).isJobDeleted(jobId)) {
                    showJobDeletedDialog();
                    return;
                }

                HomeFragment.this.acceptJob(adapterPosition);
                MoversPreferences.getInstance(mContext).setOpportunityId(opportunityId);
            }

            @Override
            public void rejectJob(int adapterPosition, String jobId, String opportunityId) {

                if (MoversPreferences.getInstance(mContext).isJobDeleted(jobId)) {
                    showJobDeletedDialog();
                    return;
                }

                showRejectDialog(adapterPosition);
                MoversPreferences.getInstance(mContext).setOpportunityId(opportunityId);
            }

            @Override
            public void showSummary(int adapterPosition, String jobId, String opportunityId) {

                if (MoversPreferences.getInstance(mContext).isJobDeleted(jobId)) {
                    showJobDeletedDialog();
                    return;
                }

                MoversPreferences.getInstance(mContext).setOpportunityId(opportunityId);
                MoversPreferences.getInstance(mContext).setCurrentJobId(jobId);
                Intent jobSummaryIntent = new Intent(mContext, JobSummaryActivity.class);
                jobSummaryIntent.putExtra(Constants.EXTRA_JOB_ID_KEY, jobId);

                startActivity(jobSummaryIntent);
            }

            @Override
            public void startJob(int adapterPosition, String jobId, String opportunityId, boolean isStorage, boolean isMoveInternational, String moveTypeId, String jobStatus) {

                if (MoversPreferences.getInstance(mContext).isJobDeleted(jobId)) {
                    showJobDeletedDialog();
                    return;
                }

                MoversPreferences.getInstance(mContext).setOpportunityId(opportunityId);
                MoversPreferences.getInstance(mContext).setCurrentJobMoveTypeId(moveTypeId);
                MoversPreferences.getInstance(mContext).setCurrentJobId(jobId);

                if (binding.getAdapter().getHomeList().get(adapterPosition).getPaymentStatus()) {
                    callSetBillOfLading(jobId);
                } else {
                    if (shouldMakeNetworkCall(binding.getRoot())) {
                        startMoveProcess(adapterPosition, jobId, opportunityId, isStorage, isMoveInternational, jobStatus);
                    }
                }

            }

            @Override
            public void showExtraStops(int adapterPosition, String jobId, String opportunityId, String addressType) {

                if (MoversPreferences.getInstance(mContext).isJobDeleted(jobId)) {
                    showJobDeletedDialog();
                    return;
                }

                MoversPreferences.getInstance(mContext).setOpportunityId(opportunityId);
                MoversPreferences.getInstance(mContext).setCurrentJobId(jobId);
                String title = "";
                if (addressType.equals(Constants.ADDRESS_TYPE_DELIVERY)) {
                    title = getString(R.string.delivery_extra_stops);
                } else {
                    title = getString(R.string.pickup_extra_stops);
                }

                Intent openExtraStopsIntent = new Intent(mContext, PickupExtraStopActivity.class);
                openExtraStopsIntent.putExtra(Constants.EXTRA_JOB_ID_KEY, jobId);
                openExtraStopsIntent.putExtra(Constants.EXTRA_TITLE, title);

                startActivity(openExtraStopsIntent);
            }

            @Override
            public void showAditionalDates(int adapterPosition, String jobId, String opportunityId, String message) {
                AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                        .setMessage(message)
                        .setCancelable(true)
                        .create();
                alertDialog.show();
            }
        }));

    }

    private void showJobDeletedDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext, R.style.AppTheme_AlertDialogTheme)
                .setCancelable(false)
                .setTitle(getString(R.string.jkob_closed))
                .setMessage(getString(R.string.job_closed_message))
                .setPositiveButton(R.string.refresh_the_page, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        loadData();
                    }
                })
                .setNegativeButton(R.string.later, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();


        alertDialog.show();
    }

    private void setViewModelObservers() {
        viewModel.jobPojoListLive.observe(getViewLifecycleOwner(), jobPojoList -> {
            if (jobPojoList == null || jobPojoList.isEmpty()) {
                return;
            }

            /**
             * Set in preference that all these jobs are not deleted.
             * This is for case when job get's reassigned after closing.
             */
            for (int i = 0; i < jobPojoList.size(); i++) {
                MoversPreferences.getInstance(mContext).setIsJobDeleted(jobPojoList.get(i).jobId, false);
            }

            if (mShowOnlyJobsInProgress) {
                binding.getAdapter().setHomeList(viewModel.getFilteredJobList(Constants.JOB_STATUS_INPROGRESS));
            } else {
                binding.getAdapter().setHomeList(jobPojoList);
            }
        });


        viewModel.billOfLadingPojoLive.observe(getViewLifecycleOwner(), billOfLadingPojo -> {
            if (billOfLadingPojo == null) {
                return;
            }

            billOfLadingPojo.setTotalCalculatedMoveCharges(billOfLadingPojo.getJobConfirmationDetailsPojo().getMoveCharges());
            billOfLadingPojo.setTotalCalculatedValuationCharges(billOfLadingPojo.getJobConfirmationDetailsPojo().getValuation());

            PaymentActivity.startFromHome(getActivity(),
                    billOfLadingPojo.getTotal(getContext()) + billOfLadingPojo.getJobConfirmationDetailsPojo().getDepositeAmount(),
                    billOfLadingPojo.getJobConfirmationDetailsPojo().getDepositeAmount(),
                    billOfLadingPojo.getJobConfirmationDetailsPojo().getBolFinalChargesPojo().getId(),
                    billOfLadingPojo.getJobConfirmationDetailsPojo().getCreditCardConvinienceFeeValue(),
                    !billOfLadingPojo.getJobConfirmationDetailsPojo().getCreditCardConvinienceFeeType(),
                    billOfLadingPojo.getJobConfirmationDetailsPojo().getOpportunityName()
            );

            /**
             * This is because if not nulled then on configuration change this observer will
             * get called again and user will get redirected to the payment activity screen.
             */
            viewModel.billOfLadingPojoLive.setValue(null);

        });

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    private void setActionListener() {
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initialise();

        setViewModelObservers();

        setActionListener();

        if (viewModel.jobPojoListLive.getValue() == null)
            loadData();


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setQueryHint(getString(R.string.search_hint));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (StringUtils.isNumericSpace(query)) {
                    binding.getAdapter().setHomeList(viewModel.getFilteredJobListByJobId(Integer.parseInt(query)));
                } else {
                    binding.getAdapter().setHomeList(viewModel.getFilteredJobListByOpportunityName(query));
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    if (viewModel.jobPojoListLive.getValue() != null) {
                        binding.getAdapter().setHomeList(viewModel.jobPojoListLive.getValue());
                    }
                }
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                openFilterDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadData() {
        String subdomain = MoversPreferences.getInstance(mContext).getSubdomain();
        String user_id = MoversPreferences.getInstance(mContext).getUserId();

        if (shouldMakeNetworkCall(binding.getRoot())) {
            showProgress();
            binding.swipeRefresh.setRefreshing(false);
            viewModel.setRefreshedNewJobList(null, subdomain, user_id, new ResponseListener<BaseResponseModel<List<JobPojo>>>() {
                @Override
                public void onResponse(BaseResponseModel<List<JobPojo>> response, String usedUrlKey) {
                    hideProgress();
                    if (response.getData().isEmpty()) {
                        showNoJobsAllocatedDialog(response.getMessage());
                    }
                }

                @Override
                public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                    hideProgress();
                    if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                        try {
                            Util.showLoginErrorDialog(getContext());
                        } catch (Exception e) {
                            Util.showLog("Error HomeFragment", e.getLocalizedMessage());
                        }

                        return;
                    }
                }

                @Override
                public void onFailure(Call<BaseResponseModel<List<JobPojo>>> call, Throwable t, String message) {
                    hideProgress();
                    Util.showToast(getContext(), message);
                }
            });
        }
    }

    private void showNoJobsAllocatedDialog(String message) {
        AlertDialog alertDialog = new MaterialAlertDialogBuilder(requireContext())
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss())
                .setNegativeButton(R.string.exit, (dialog, which) -> {
                    dialog.dismiss();
                    requireActivity().finish();
                })
                .create();
        alertDialog.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                rejectJob(v);
                break;
            /*case R.id.btn_apply:
                jobStatusCode = (String) v.getTag();
                binding.getAdapter().setHomeList(viewModel.getFilteredJobList(jobStatusCode));
                break;*/
            case R.id.tv_start_move:

                break;
        }
    }

    public void showOnlyJobsInProgress() {
        if (viewModel != null && binding.getAdapter() != null && binding.getAdapter().getHomeList() != null && binding.getAdapter().getHomeList().size() > 0) {
            binding.getAdapter().setHomeList(viewModel.getFilteredJobList(Constants.JOB_STATUS_INPROGRESS));
        } else {
            mShowOnlyJobsInProgress = true;
        }
    }

    public void removeAllJobFilters() {
        if (viewModel != null && binding.getAdapter() != null && viewModel.jobPojoListLive != null) {
            binding.getAdapter().setHomeList(viewModel.jobPojoListLive.getValue());
        } else {
            mShowOnlyJobsInProgress = true;
        }
    }


    private void startMoveProcess(final int adapterPosition, final String job_id, final String opportunityId, boolean isStorage, boolean isMoveInternational, final String jobStatus) {


        if (jobStatus.equals(Constants.JOB_STATUS_INPROGRESS)) {
            Intent openMoveProcessIntent = new Intent(mContext, MoveProcessActivity.class);
            openMoveProcessIntent.putExtra(Constants.EXTRA_JOB_ID_KEY, job_id);
            openMoveProcessIntent.putExtra(Constants.EXTRA_MOVE_HAS_STORAGE_KEY, isStorage);
            openMoveProcessIntent.putExtra(Constants.EXTRA_IS_MOVE_INTERNATIONAL_KEY, isMoveInternational);
            startActivityForResult(openMoveProcessIntent, HomeActivity.RESULT_CODE_MOVE_PROCESS_API_CALL_ERROR);
            return;
        }

        showProgress();
        viewModel.startMoveOfJob(MoversPreferences.getInstance(mContext).getSubdomain(), MoversPreferences.getInstance(mContext).getUserId(), MoversPreferences.getInstance(mContext).getOpportunityId(), adapterPosition, "", new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
                binding.getAdapter().notifyItemChanged(adapterPosition);
                startActivity(new Intent(mContext, MoveProcessActivity.class).putExtra(Constants.EXTRA_JOB_ID_KEY, job_id));
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(HomeFragment.this.getContext());
                    return;
                }

                Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t, String message) {
                hideProgress();
                try {
                    Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
                } catch (Exception e) {
                    Util.showLog(getClass().getSimpleName(), "SnackBar Issue Please provide a valid view.");
                }

            }
        });

    }

    private void rejectJob(View view) {
        final int adapterPosition = (int) view.getTag();
        final String comments = rejectDialogFragment.getmComments();

        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }
        showProgress();
        viewModel.rejectJob(MoversPreferences.getInstance(mContext).getSubdomain(), MoversPreferences.getInstance(mContext).getUserId(), MoversPreferences.getInstance(mContext).getOpportunityId(), comments, adapterPosition, new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
                binding.getAdapter().notifyItemRemoved(adapterPosition);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(HomeFragment.this.getContext());
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

    private void showRejectDialog(int adapterPosition) {
        // Create the fragment and show it as a dialog.
        rejectDialogFragment = new RejectDialogFragment();
        rejectDialogFragment.setHomeAdapterPosition(adapterPosition);
        rejectDialogFragment.setOnClickListener(this);
        rejectDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog");
    }

    private void acceptJob(final int adpterPosition) {

        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        showProgress();
        viewModel.acceptJob(MoversPreferences.getInstance(mContext).getSubdomain(), MoversPreferences.getInstance(mContext).getUserId(), MoversPreferences.getInstance(mContext).getOpportunityId(), adpterPosition, new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
                binding.getAdapter().notifyItemChanged(adpterPosition);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(HomeFragment.this.getContext());
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


    private void callSetBillOfLading(String jobId) {

        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        showProgress();

        String subdomain = MoversPreferences.getInstance(mContext).getSubdomain();
        String userId = MoversPreferences.getInstance(mContext).getUserId();
        String opportunityId = MoversPreferences.getInstance(mContext).getOpportunityId();


        viewModel.setJobConfirmation(subdomain, userId, opportunityId, jobId, "0", new ResponseListener<BaseResponseModel<BolDetailsPojo>>() {
            @Override
            public void onResponse(BaseResponseModel<BolDetailsPojo> response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(HomeFragment.this.getContext());
                    return;
                }
                Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<BolDetailsPojo>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }


    private void openFilterDialog() {
        FilterDialogFragment newFragment = new FilterDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putCharSequence("jobString", jobStatusCode);
        newFragment.setArguments(bundle);
//        newFragment.setOnClickListener(this);
//        newFragment.show(getActivity().getSupportFragmentManager(), "dialog");
        newFragment.show(getChildFragmentManager(), "dialog");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CODE_MOVE_PROCESS_API_CALL_ERROR) {
            if (resultCode == MoveProcessActivity.RESULT_FAILED_TO_GET_DATA) {
                String message = Constants.RESPONSE_FAILURE_MESSAGE;
                if (data.hasExtra(MoveProcessActivity.EXTRA_FAILED_TO_GET_DATA_MESSAGE)) {
                    message = data.getStringExtra(MoveProcessActivity.EXTRA_FAILED_TO_GET_DATA_MESSAGE);
                }

                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void setOnFilterTagSubmitListener(String jobStatusCode) {
        binding.getAdapter().setHomeList(viewModel.getFilteredJobList(jobStatusCode));
    }
}
