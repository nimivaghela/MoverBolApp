package com.moverbol.viewmodels;

import android.util.Log;

import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.moverbol.DataRepository;
import com.moverbol.constants.Constants;
import com.moverbol.database.JobDao;
import com.moverbol.model.billOfLading.BillOfLadingPojo;
import com.moverbol.model.billOfLading.BolDetailsPojo;
import com.moverbol.model.notification.NotificationListResponse;
import com.moverbol.model.notification.NotificationModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.network.model.JobPojo;
import com.moverbol.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;

/**
 * Created by sumeet on 26/8/17.
 */

public class HomeViewModel extends ViewModel {

    private final DataRepository mDataRepository = DataRepository.getInstance();
    public MutableLiveData<List<JobPojo>> jobPojoListLive = new MutableLiveData<>();
    public MutableLiveData<List<NotificationModel>> notificationListLive = new MutableLiveData<>();

    public MutableLiveData<BillOfLadingPojo> billOfLadingPojoLive = new MutableLiveData<>();

    //    public String jobIdLive = "";
    public int positionToScrollHomeList = 0;
    private JobDao jobDao;

    public void setRefreshedNewJobList(JobDao jobDao, String subdomain, String user_id, final ResponseListener<BaseResponseModel<List<JobPojo>>> responsePojoResponseListener) {
        mDataRepository.getRefreshedJobList(jobDao, subdomain, user_id, new ResponseListener<BaseResponseModel<List<JobPojo>>>() {
            @Override
            public void onResponse(BaseResponseModel<List<JobPojo>> response, String usedUrlKey) {
//                jobPojoObservableArrayList.clear();
//                jobPojoObservableArrayList.addAll(response.getData());
                jobPojoListLive.postValue(response.getData());
                responsePojoResponseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responsePojoResponseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<List<JobPojo>>> call, Throwable t, String message) {
                responsePojoResponseListener.onFailure(call, t, message);
            }
        });
    }

    /*public void setNewJobList(JobDao jobDao) {
        List<JobPojo> jobPojoList = mDataRepository.getJobListFromDB(jobDao);
        jobPojoObservableArrayList.addAll(jobPojoList);
    }

    public ObservableArrayList<JobPojo> getJobPojoObservableArrayList() {
        return jobPojoObservableArrayList;
    }*/


    public void acceptJob(String subdomain, String userId, String opportunityId, final int jobListPosition, final ResponseListener<BaseResponseModel> responseListener) {


        final JobPojo jobPojo = jobPojoListLive.getValue().get(jobListPosition);

        DataRepository.getInstance().setJobStatus(subdomain, userId, opportunityId, jobPojo.jobId, Constants.JOB_STATUS_ACCEPTED, "", "", new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                jobPojo.jobStatus = Constants.JOB_STATUS_ACCEPTED;
                jobPojoListLive.getValue().set(jobListPosition, jobPojo);

                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }


    public void rejectJob(String subdomain, String userId, String opportunityId, String comments, final int jobListPosition, final ResponseListener<BaseResponseModel> responseListener) {

//        final JobPojo jobPojo = jobPojoObservableArrayList.get(jobListPosition);
        final JobPojo jobPojo = jobPojoListLive.getValue().get(jobListPosition);

        mDataRepository.setJobStatus(subdomain, userId, opportunityId, jobPojo.jobId, Constants.JOB_STATUS_REJECTED, comments, "", new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
//                jobPojo.jobStatus = Constants.JOB_STATUS_REJECTED;
//                jobPojoObservableArrayList.remove(jobListPosition);
                jobPojoListLive.getValue().remove(jobListPosition);


                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }

    public void startMoveOfJob(String subdomain, String userId, String opportunityId, final int jobListPosition, String totalCharges, final ResponseListener<BaseResponseModel> responseListener) {

//        final JobPojo jobPojo = jobPojoObservableArrayList.get(jobListPosition);
        final JobPojo jobPojo = jobPojoListLive.getValue().get(jobListPosition);

        mDataRepository.setJobStatus(subdomain, userId, opportunityId, jobPojo.jobId, Constants.JOB_STATUS_INPROGRESS, "", totalCharges, new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                jobPojo.jobStatus = Constants.JOB_STATUS_INPROGRESS;
//                jobPojoObservableArrayList.set(jobListPosition, jobPojo);
                jobPojoListLive.getValue().set(jobListPosition, jobPojo);

                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }


    public void getNotificationList(String subDomain, String user_id, final ResponseListener<BaseResponseModel<NotificationListResponse>> responseListener) {
        mDataRepository.getNotificationList(subDomain, user_id, new ResponseListener<BaseResponseModel<NotificationListResponse>>() {
            @Override
            public void onResponse(BaseResponseModel<NotificationListResponse> response, String usedUrlKey) {
                notificationListLive.setValue(response.getData().getNotificationModelList());
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<NotificationListResponse>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }

    public void notifcationUpdate(String subDomain, String r_id, String user_id, final ResponseListener<BaseResponseModel> responseListener) {
        mDataRepository.notificationUpdate(subDomain, r_id, user_id, new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }


    public void setJobConfirmation(final String subDomain, String user_id, String opportunityId, String jobId, String totalTimeForMoveChargesCalculation, final ResponseListener<BaseResponseModel<BolDetailsPojo>> responseListener) {

        DataRepository.getInstance().getBOLJobConfirmation(subDomain, user_id, opportunityId, jobId, 0, 0, new ResponseListener<BaseResponseModel<BolDetailsPojo>>() {
            @Override
            public void onResponse(BaseResponseModel<BolDetailsPojo> response, String usedUrlKey) {

                BillOfLadingPojo billOfLadingPojo = new BillOfLadingPojo();

                billOfLadingPojo.setJobConfirmationDetailsPojo(response.getData());

                billOfLadingPojo.setMoveChargesPriceType((int) Util.getDoubleFromString(response.getData().getMoveChargePriceType()));

                billOfLadingPojoLive.setValue(billOfLadingPojo);

                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<BolDetailsPojo>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }


    public void logout(String subdomain, String userId, final ResponseListener<BaseResponseModel> responseListener) {
        mDataRepository.logout(subdomain, userId, responseListener);
    }


    public List<JobPojo> getFilteredJobList(String jobStatusCode) {

        ObservableArrayList<JobPojo> filteredList = new ObservableArrayList<>();

        if (jobPojoListLive.getValue() != null) {
            if (jobStatusCode.equals("")) {
                return jobPojoListLive.getValue();
            }

            for (int i = 0; i < jobPojoListLive.getValue().size(); i++) {
                JobPojo jobPojo = jobPojoListLive.getValue().get(i);
                if (jobPojo.jobStatus.equals(jobStatusCode)) {
                    filteredList.add(jobPojo);
                }
            }
        }

        return filteredList;
    }

    public List<JobPojo> getFilteredJobList(Date startDate, Date endDate) {

        List<JobPojo> listToReturn = new ArrayList<>();

        if (jobPojoListLive.getValue() != null) {

            if (startDate.after(endDate)) {
                Log.e(Constants.BASE_LOG_TAG, "End date comes before start date");
                return listToReturn;
            }

            for (int i = 0; i < jobPojoListLive.getValue().size(); i++) {
                JobPojo jobPojo = jobPojoListLive.getValue().get(i);
                if (!jobPojo.getDateObject().before(startDate) && !jobPojo.getDateObject().after(endDate)) {
                    listToReturn.add(jobPojo);
                }
            }

            //Sort the list by job start time
            Collections.sort(listToReturn, new Comparator<JobPojo>() {
                @Override
                public int compare(JobPojo o1, JobPojo o2) {
                    return o1.getStartTimeHourIn24Format() - o2.getStartTimeHourIn24Format();
                }
            });
        }

        return listToReturn;
    }

    public boolean hasJobOnGivenDate(Date date) {

        if (jobPojoListLive.getValue() != null) {
            for (int i = 0; i < jobPojoListLive.getValue().size(); i++) {
                if (jobPojoListLive.getValue().get(i).getDateObject().equals(date)) {
                    return true;
                }
            }
        }
        return false;
    }

    /*public List<JobPojo> getFilteredJobListByJobIdAndOpportunityName(String query) {

        if (StringUtils.isNumeric(query)) {
            jobId = Integer.parseInt(query);
            return getFilteredJobList(jobId);
        }else {
            return getFilteredJobListByOpportunityName(query);
        }
    }*/

    public List<JobPojo> getFilteredJobListByOpportunityName(String opportunityName) {
        List<JobPojo> listToReturn = new ArrayList<>();

        if (jobPojoListLive.getValue() != null && jobPojoListLive.getValue().size() > 0) {
            for (int i = 0; i < jobPojoListLive.getValue().size(); i++) {
                if (jobPojoListLive.getValue().get(i).opportunityName != null) {
                    if (jobPojoListLive.getValue().get(i).opportunityName.toLowerCase().contains(opportunityName.toLowerCase())) {
                        listToReturn.add(jobPojoListLive.getValue().get(i));
                    }
                }
            }
        }
        return listToReturn;
    }

    public List<JobPojo> getFilteredJobListByJobId(int jobId) {

        List<JobPojo> listToReturn = new ArrayList<>();

        if (jobPojoListLive.getValue() != null || jobPojoListLive.getValue().size() > 0) {
            for (int i = 0; i < jobPojoListLive.getValue().size(); i++) {
                if (jobPojoListLive.getValue().get(i).jobId.equals(jobId + "")) {
                    listToReturn.add(jobPojoListLive.getValue().get(i));
                }
            }
        }
        return listToReturn;
    }

    public void setPositionToScrollHomeList(String jobId) {
        if (jobPojoListLive.getValue() != null) {
            for (int i = 0; i < jobPojoListLive.getValue().size(); i++) {
                if (jobPojoListLive.getValue().get(i).jobId.equals(jobId)) {
                    this.positionToScrollHomeList = i;
                }
            }
        }

    }


}
