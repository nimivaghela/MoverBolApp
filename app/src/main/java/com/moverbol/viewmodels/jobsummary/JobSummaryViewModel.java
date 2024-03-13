package com.moverbol.viewmodels.jobsummary;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.moverbol.DataRepository;
import com.moverbol.constants.Constants;
import com.moverbol.model.CrewMetadata;
import com.moverbol.model.CrewMetadataAssigned;
import com.moverbol.model.JobDetailPojo;
import com.moverbol.model.ManPowerPojo;
import com.moverbol.model.MaterialMetadatAssigned;
import com.moverbol.model.MaterialMetadata;
import com.moverbol.model.MaterialPojo;
import com.moverbol.model.RateTypePojo;
import com.moverbol.model.TruckMetadata;
import com.moverbol.model.TruckMetadataAssigned;
import com.moverbol.model.TruckPojo;
import com.moverbol.model.billOfLading.CategoryModel;
import com.moverbol.model.billOfLading.ChargeRateTypeModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.WebServiceManager;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.network.model.BaseResponseModelSecond;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by AkashM on 28/11/17.
 */

public class JobSummaryViewModel extends ViewModel {
    private final MutableLiveData<JobDetailPojo> jobDetailLive = new MutableLiveData<>();
    public MutableLiveData<ManPowerPojo> workerPojoLive = new MutableLiveData<>();
    public MutableLiveData<TruckPojo> truckPojoLive = new MutableLiveData<>();
    public MutableLiveData<MaterialPojo> materialPojoLive = new MutableLiveData<>();
    public MutableLiveData<List<CrewMetadata>> crewMetadataListLive = new MutableLiveData<>();
    public MutableLiveData<List<TruckMetadata>> truckMetadataListLive = new MutableLiveData<>();
    public MutableLiveData<BaseResponseModelSecond<List<MaterialMetadata>, List<RateTypePojo>>> materialMetadataListLive = new MutableLiveData<>();
    public MutableLiveData<ArrayList<ChargeRateTypeModel>> chargeRateTypeListLiveData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<CategoryModel>> categoryTypeListLiveData = new MutableLiveData<>();


    public void setJobDetails(String subDomain, String userid, String opportunityId, String job_id, final ResponseListener<BaseResponseModel<JobDetailPojo>> responseListener) {
        DataRepository.getInstance().setJobDetails(subDomain, userid, opportunityId, job_id, new ResponseListener<BaseResponseModel<JobDetailPojo>>() {
            @Override
            public void onResponse(BaseResponseModel<JobDetailPojo> response, String usedUrlKey) {
//                jobDetailLive.setValue(response.getData());
                jobDetailLive.postValue(response.getData());

                /*setCustomerInfo(response.getData().getCustomerInfo());
                setPickupAddess(response.getData().getPickupadress());
                setDeliveryAddess(response.getData().getDeliveryadress());
                */
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<JobDetailPojo>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }

    public MutableLiveData<JobDetailPojo> getJobDetailLive() {
        return jobDetailLive;
    }


    public void setJobDetailLive() {
//        jobDetailPojoLive = DataRepository.getInstance().getJobDetailsLive();
        jobDetailLive.postValue(DataRepository.getInstance().getJobDetailPojo());
    }


    //For total materials
    public double getTotalMaterialCost(List<MaterialPojo> materialPojoList) {
        double totalMaterialCost = 0;
        for (int i = 0; i < materialPojoList.size(); i++) {
            totalMaterialCost = totalMaterialCost + Double.parseDouble(materialPojoList.get(i).getTotal());
        }
        return totalMaterialCost;
    }


    public void deleteSelectedCrewFromList(String subdomain, String userId, String opportunityId, String jobId, List<ManPowerPojo> manPowerPojoList, ResponseListener<BaseResponseModel> responseListener) {
        String concatinatedIds = null;
        for (ManPowerPojo pojo : manPowerPojoList) {
            if (pojo.isSelectedForDelete()) {
                if (concatinatedIds == null) {
                    concatinatedIds = pojo.getManPowerId();
                } else {

                    concatinatedIds = concatinatedIds + "," + pojo.getManPowerId();
                }
            }
        }
        deleteCrewItems(subdomain, userId, opportunityId, jobId, concatinatedIds, responseListener);
    }

    public void deleteSelectedTruckFromList(String subdomain, String userId, String opportunityId, String jobId, List<TruckPojo> truckPojoList, ResponseListener<BaseResponseModel> responseListener) {
        String concatinatedIds = null;
        for (TruckPojo pojo : truckPojoList) {
            if (pojo.isSelectedForDelete()) {
                if (concatinatedIds == null) {
                    concatinatedIds = pojo.getVehicleId();
                } else {
                    concatinatedIds = concatinatedIds + "," + pojo.getVehicleId();
                }
            }
        }
        deleteTruckItems(subdomain, userId, opportunityId, jobId, concatinatedIds, responseListener);
    }

    public void deleteSelectedMaterialFromList(String subdomain, String userId, String opportunityId, String jobId, List<MaterialPojo> materialPojoList, ResponseListener<BaseResponseModel> responseListener) {
        String concatinatedIds = null;
        for (MaterialPojo pojo : materialPojoList) {
            if (pojo.isSelectedForDelete()) {
                if (concatinatedIds == null) {
                    concatinatedIds = pojo.getMaterialId();
                } else {
                    concatinatedIds = concatinatedIds + "," + pojo.getMaterialId();
                }
            }
        }
        deleteMaterialItems(subdomain, userId, opportunityId, jobId, concatinatedIds, responseListener);
    }


    private void deleteCrewItems(String subdomain, String userId, String opportunityId, String jobId, String concatinatedIds, ResponseListener<BaseResponseModel> responseListener) {
        DataRepository.getInstance().deleteCrewItems(subdomain, userId, opportunityId, jobId, concatinatedIds, responseListener);
    }

    private void deleteTruckItems(String subdomain, String userId, String opportunityId, String jobId, String concatinatedIds, ResponseListener<BaseResponseModel> responseListener) {
        DataRepository.getInstance().deleteTruckItems(subdomain, userId, opportunityId, jobId, concatinatedIds, responseListener);
    }

    private void deleteMaterialItems(String subdomain, String userId, String opportunityId, String jobId, String concatinatedIds, ResponseListener<BaseResponseModel> responseListener) {
        DataRepository.getInstance().deleteMaterialItems(subdomain, userId, opportunityId, jobId, concatinatedIds, responseListener);
    }


    public void getCrewMetadata(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<List<CrewMetadata>>> responseListener) {

        DataRepository.getInstance().getCrewMetadat(subdomain, userId, opportunityId, jobId, new ResponseListener<BaseResponseModel<List<CrewMetadata>>>() {
            @Override
            public void onResponse(BaseResponseModel<List<CrewMetadata>> response, String usedUrlKey) {
                crewMetadataListLive.setValue(response.getData());
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<List<CrewMetadata>>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });

    }

    public void getTruckMetadata(String subdomain, String userId, String opportunityId, final ResponseListener<BaseResponseModel<List<TruckMetadata>>> responseListener) {

        DataRepository.getInstance().getTruckMetadat(subdomain, userId, opportunityId, new ResponseListener<BaseResponseModel<List<TruckMetadata>>>() {
            @Override
            public void onResponse(BaseResponseModel<List<TruckMetadata>> response, String usedUrlKey) {
                truckMetadataListLive.setValue(response.getData());
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<List<TruckMetadata>>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });

    }

    public void getMaterialMetadata(String subdomain, String userId, String opportunityId, final ResponseListener<BaseResponseModelSecond<List<MaterialMetadata>, List<RateTypePojo>>> responseListener) {

        DataRepository.getInstance().getMaterialMetadat(subdomain, userId, opportunityId, new ResponseListener<BaseResponseModelSecond<List<MaterialMetadata>, List<RateTypePojo>>>() {
            @Override
            public void onResponse(BaseResponseModelSecond<List<MaterialMetadata>, List<RateTypePojo>> response, String usedUrlKey) {
                materialMetadataListLive.setValue(response);

                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModelSecond<List<MaterialMetadata>, List<RateTypePojo>>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });

    }


    public void getWorkerById(String subDomain, String userId, String opportunityId, String workerId, String jobId, final ResponseListener<BaseResponseModel<ManPowerPojo>> responseListener) {
        DataRepository.getInstance().getWorkerById(subDomain, userId, opportunityId, workerId, jobId, new ResponseListener<BaseResponseModel<ManPowerPojo>>() {
            @Override
            public void onResponse(BaseResponseModel<ManPowerPojo> response, String usedUrlKey) {
                workerPojoLive.postValue(response.getData());
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ManPowerPojo>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }

    public void getTruckById(String subDomain, String userId, String opportunityId, String truckId, String jobId, final ResponseListener<BaseResponseModel<TruckPojo>> responseListener) {
        DataRepository.getInstance().getTruckById(subDomain, userId, opportunityId, truckId, jobId, new ResponseListener<BaseResponseModel<TruckPojo>>() {
            @Override
            public void onResponse(BaseResponseModel<TruckPojo> response, String usedUrlKey) {
                truckPojoLive.postValue(response.getData());
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<TruckPojo>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }


    public void getChargeRateTypeList(String subDomain, String opportunityId, String moveType, String model, int cId, final ResponseListener<BaseResponseModel<ArrayList<ChargeRateTypeModel>>> responseListener) {

        DataRepository.getInstance().getChargeRateTypeList(subDomain, opportunityId, moveType, model, cId, new ResponseListener<BaseResponseModel<ArrayList<ChargeRateTypeModel>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<ChargeRateTypeModel>> response, String usedUrlKey) {
                chargeRateTypeListLiveData.postValue(response.getData());
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<ChargeRateTypeModel>>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }


    public void getCategoryTypeList(String subDomain, String opportunityId, String moveType, String model, final ResponseListener<BaseResponseModel<ArrayList<CategoryModel>>> responseListener) {

        WebServiceManager.getInstance().getCategoryTypeList(subDomain, opportunityId, moveType, model, new ResponseListener<BaseResponseModel<ArrayList<CategoryModel>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<CategoryModel>> response, String usedUrlKey) {
                categoryTypeListLiveData.postValue(response.getData());
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<CategoryModel>>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }

    public void addCrewItems(String subdomain, String jobId, String opportunityId, String workertype, String responsibleUser, String tempWorker, String remarks, String manpowerId, String userId, final ResponseListener<BaseResponseModel> responseListener) {
        DataRepository.getInstance().addCrewItems(subdomain, jobId, opportunityId, workertype, responsibleUser, tempWorker, remarks, manpowerId, userId, responseListener);
    }

    public void addTruckItems(String subdomain, String jobId, String opportunityId, String vehicleType, String vehicleNumber, String tempVehicle, String remarks, String vehicleId, String userId, final ResponseListener<BaseResponseModel> responseListener) {
        DataRepository.getInstance().addTruckItems(subdomain, jobId, opportunityId, vehicleType, vehicleNumber, tempVehicle, remarks, vehicleId, userId, responseListener);
    }

    public void addMaterialItems(String subdomain, String uiserId, String opportunityId, String jobId, String material, String quantity, String materialUnit, String materialRate, String totalMaterialRate, String remarks, String materialId, int cId, final ResponseListener<BaseResponseModel> responseListener) {
        DataRepository.getInstance().addMaterialItems(subdomain, uiserId, opportunityId, jobId, material, quantity, materialUnit, materialRate, totalMaterialRate, remarks, materialId, cId, responseListener);
    }

    /**
     * Searches for the given text in the available list of crew, truck or metadata based on the modelType provided. Returns the
     * list position of the object which has this designationId.
     *
     * @param textToSearch designationId for crew, vehicleType for truck, materialName for materials
     * @param modelType    crew, truck, or material
     * @return the list position. By default 0.
     */
    public int getMetadatListPosition(String textToSearch, String modelType) {

        if (modelType.equals(Constants.GET_METADATA_MODEL_WORKER)) {
            if (crewMetadataListLive.getValue() != null && crewMetadataListLive.getValue().size() != 0) {
                List<CrewMetadata> crewMetadataList = crewMetadataListLive.getValue();
                for (int i = 0; i < crewMetadataList.size(); i++) {
                    if (TextUtils.equals(crewMetadataList.get(i).getId(), textToSearch)) {
                        return i;
                    }
                }
            }
        }

        if (modelType.equals(Constants.GET_METADATA_MODEL_TRUCK)) {
            if (truckMetadataListLive.getValue() != null && truckMetadataListLive.getValue().size() != 0) {
                List<TruckMetadata> truckMetadataList = truckMetadataListLive.getValue();
                for (int i = 0; i < truckMetadataList.size(); i++) {
                    if (truckMetadataList.get(i).getId().equals(textToSearch)) {
                        return i;
                    }
                }
            }
        }

        return -1;
    }

    public int getMaterialMetadatListPosition(String materialName, String moveType) {

        List<MaterialMetadatAssigned> materialMetadatAssignedList = new ArrayList<>();

        if (materialMetadataListLive.getValue() != null && materialMetadataListLive.getValue().getData().size() != 0) {

            List<MaterialMetadata> materialMetadataList = materialMetadataListLive.getValue().getData();
            for (int i = 0; i < materialMetadataList.size(); i++) {
                if (materialMetadataList.get(i).getMoveType().equals(moveType)) {
                    materialMetadatAssignedList = materialMetadataList.get(i).getMetadatAssignedList();
                }
            }
        }


        for (int i = 0; i < materialMetadatAssignedList.size(); i++) {
            if (materialMetadatAssignedList.get(i).getStockMaterialName().equals(materialName)) {
                return i;
            }
        }

        return -1;
    }


    /**
     * Searches for the assigned object(crew, truck or material) in the selected list object of available crew, truck or metadata list.
     * Returns the list position of the found object. By default returns position of 'Temporary'
     *
     * @param spinTypeSelectionPosition the position of selected list object of available crew, truck or metadata list
     * @param textToSearch              manPowerUserId for crews, vehicleNo for trucks,
     * @param modelType                 the id of assigned crew need to search
     * @return the list position
     */
    public int getMetadataAssignedPosition(int spinTypeSelectionPosition, String textToSearch, String modelType) {

        if (spinTypeSelectionPosition < 0) {
            return -1;
        }

        if (modelType.equals(Constants.GET_METADATA_MODEL_WORKER)) {
            if (crewMetadataListLive.getValue() != null && !crewMetadataListLive.getValue().isEmpty()) {
                List<CrewMetadataAssigned> assignedList = crewMetadataListLive.getValue().get(spinTypeSelectionPosition).getAssignedList();
                for (int i = 0; i < assignedList.size(); i++) {
                    if (assignedList.get(i).getCrewId().equals(textToSearch)) {
                        return i;
                    }
                }
            }

            if (crewMetadataListLive.getValue() != null
                    && crewMetadataListLive.getValue().get(spinTypeSelectionPosition) != null
                    && crewMetadataListLive.getValue().get(spinTypeSelectionPosition).getAssignedList() != null) {
                return (crewMetadataListLive.getValue().get(spinTypeSelectionPosition).getAssignedList().size() + 1);
            }
        }

        if (modelType.equals(Constants.GET_METADATA_MODEL_TRUCK)) {
            if (truckMetadataListLive.getValue() != null && !truckMetadataListLive.getValue().isEmpty()) {
                List<TruckMetadataAssigned> assignedList = truckMetadataListLive.getValue().get(spinTypeSelectionPosition).getTruckMetadataAssignedList();
                for (int i = 0; i < assignedList.size(); i++) {
                    if (assignedList.get(i).getId().equals(textToSearch)) {
                        return i;
                    }
                }
            }

            if (truckMetadataListLive.getValue() != null &&
                    truckMetadataListLive.getValue().get(spinTypeSelectionPosition) != null &&
                    truckMetadataListLive.getValue().get(spinTypeSelectionPosition).getTruckMetadataAssignedList() != null
            ) {
                return (truckMetadataListLive.getValue().get(spinTypeSelectionPosition).getTruckMetadataAssignedList().size() + 1);
            }
        }

        return -1;
    }


    public int getAssignedCrewNamePosition(ArrayList<CrewMetadataAssigned> list, String id) {
        for (int i = 0; i < list.size(); i++) {
            if (TextUtils.equals(id, list.get(i).getCrewId())) {
                return i;
            }
        }
        return 0;
    }

}
