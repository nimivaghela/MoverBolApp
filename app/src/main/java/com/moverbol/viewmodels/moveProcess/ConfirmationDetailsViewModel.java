package com.moverbol.viewmodels.moveProcess;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.moverbol.DataRepository;
import com.moverbol.constants.Constants;
import com.moverbol.model.confirmationPage.AdditionalChargesPojo;
import com.moverbol.model.confirmationPage.ConfirmationSubmitRequestModel;
import com.moverbol.model.confirmationPage.CratingChargesPojo;
import com.moverbol.model.confirmationPage.JobConfirmationDetailsPojo;
import com.moverbol.model.confirmationPage.MoveChargesManualPricingPojo;
import com.moverbol.model.confirmationPage.PackingChargesPojo;
import com.moverbol.model.confirmationPage.StorageChargesPojo;
import com.moverbol.model.confirmationPage.ValuationChargesPojo;
import com.moverbol.model.confirmationPage.artialeList.ArticleListItemPojo;
import com.moverbol.model.confirmationPage.artialeList.ArticleListResponseDetailsModel;
import com.moverbol.model.confirmationPage.artialeList.ArticleRoomModel;
import com.moverbol.model.confirmationPage.artialeList.RawBodyRequestModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;

/**
 * Created by AkashM on 1/12/17.
 */

public class ConfirmationDetailsViewModel extends ViewModel {

    public MutableLiveData<JobConfirmationDetailsPojo> jobConfirmationLive = new MutableLiveData<>();
    public MutableLiveData<ArrayList<StorageChargesPojo>> storageChargesPojoListLive = new MutableLiveData<>();
    public MutableLiveData<ArrayList<PackingChargesPojo>> packingChargesPojoListLive = new MutableLiveData<>();
    public MutableLiveData<ArrayList<ValuationChargesPojo>> valuationChargesPojoListLive = new MutableLiveData<>();
    public MutableLiveData<ArrayList<AdditionalChargesPojo>> additionaChargesPojoListLive = new MutableLiveData<>();
    public MutableLiveData<ArrayList<MoveChargesManualPricingPojo>> moveChargesPricingPojoListLive = new MutableLiveData<>();
    public MutableLiveData<ArrayList<ArticleListItemPojo>> articleListItemPojoListLive = new MutableLiveData<>();
    public MutableLiveData<ArrayList<ArticleRoomModel>> articleRoomListLiveData = new MutableLiveData<>();

    public ArrayList<ArticleListItemPojo> articleItemList = new ArrayList<>(0);
    public MutableLiveData<ArrayList<CratingChargesPojo>> cratingChargesPojoListLive = new MutableLiveData<>();

    public void setJobConfirmation(String subDomain, String user_id, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<JobConfirmationDetailsPojo>> responseListener) {
        DataRepository.getInstance().getJobConfirmation(subDomain, user_id, opportunityId, jobId, new ResponseListener<BaseResponseModel<JobConfirmationDetailsPojo>>() {
            @Override
            public void onResponse(BaseResponseModel<JobConfirmationDetailsPojo> response, String usedUrlKey) {
                jobConfirmationLive.setValue(response.getData());

                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<JobConfirmationDetailsPojo>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }

    public void setStoredJobConfirmation() {
        jobConfirmationLive.postValue(DataRepository.getInstance().getJobConfirmationDetailsPojo());
    }

    public void getStorageChargesDetails(String subdomain, String userId, String opportunityId, String model, String jobId, final ResponseListener<BaseResponseModel<ArrayList<StorageChargesPojo>>> responseListener) {
        DataRepository.getInstance().getStorageChargesDetails(subdomain, userId, opportunityId, model, jobId, new ResponseListener<BaseResponseModel<ArrayList<StorageChargesPojo>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<StorageChargesPojo>> response, String usedUrlKey) {
                storageChargesPojoListLive.postValue(response.getData());
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<StorageChargesPojo>>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }

    public void getPackingChargesDetails(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<ArrayList<PackingChargesPojo>>> responseListener) {
        DataRepository.getInstance().getPackingChargesDetails(subdomain, userId, opportunityId, jobId, new ResponseListener<BaseResponseModel<ArrayList<PackingChargesPojo>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<PackingChargesPojo>> response, String usedUrlKey) {
                packingChargesPojoListLive.postValue(response.getData());
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<PackingChargesPojo>>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }

    public void getAdditionalChargesDetails(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<ArrayList<AdditionalChargesPojo>>> responseListener) {
        DataRepository.getInstance().getAdditionalChargesDetails(subdomain, userId, opportunityId, jobId, new ResponseListener<BaseResponseModel<ArrayList<AdditionalChargesPojo>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<AdditionalChargesPojo>> response, String usedUrlKey) {
                additionaChargesPojoListLive.postValue(response.getData());
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<AdditionalChargesPojo>>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }

    public void getValuationChargesDetails(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<ArrayList<ValuationChargesPojo>>> responseListener) {
        DataRepository.getInstance().getValuationChargesDetails(subdomain, userId, opportunityId, jobId, new ResponseListener<BaseResponseModel<ArrayList<ValuationChargesPojo>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<ValuationChargesPojo>> response, String usedUrlKey) {
                valuationChargesPojoListLive.postValue(response.getData());
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<ValuationChargesPojo>>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }

    public void getMoveChargesDetails(String subdomain, String userId, String opportunityId, String priceType, String jobId, final ResponseListener responseListener) {

        DataRepository.getInstance().getMoveChargesManualPricingDetails(subdomain, userId, opportunityId, jobId, priceType, new ResponseListener<BaseResponseModel<ArrayList<MoveChargesManualPricingPojo>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<MoveChargesManualPricingPojo>> response, String usedUrlKey) {
                moveChargesPricingPojoListLive.postValue(response.getData());
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<MoveChargesManualPricingPojo>>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });

    }

    public void getArticleItemList(String subDomain, String inventoryId, String roomId, final ResponseListener<BaseResponseModel<ArrayList<ArticleListItemPojo>>> responseListener) {

        DataRepository.getInstance().getArticleItemList(subDomain, inventoryId, roomId, new ResponseListener<BaseResponseModel<ArrayList<ArticleListItemPojo>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<ArticleListItemPojo>> response, String usedUrlKey) {

                articleItemList = response.getData();
                articleListItemPojoListLive.postValue(articleItemList);
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<ArticleListItemPojo>>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }

    public void getArticleRoomList(String subDomain, String opportunityId, final ResponseListener<BaseResponseModel<ArrayList<ArticleRoomModel>>> responseListener) {

        DataRepository.getInstance().getArticleRoomList(subDomain, opportunityId, new ResponseListener<BaseResponseModel<ArrayList<ArticleRoomModel>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<ArticleRoomModel>> response, String usedUrlKey) {

                articleRoomListLiveData.postValue(response.getData());

                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<ArticleRoomModel>>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }

    /*public void getStoredArticleList() {
        ArticleListResponseDetailsModel articleListResponseDetailsModel = DataRepository.getInstance().getArticleListResponseDetailsModel();
        ArrayList<ArticleListItemPojo> articleListItemPojoList = new ArrayList<>();

        if(articleListResponseDetailsModel!=null) {
            articleListItemPojoList.addAll(articleListResponseDetailsModel.getNormalItems());
            articleListItemPojoList.addAll(articleListResponseDetailsModel.getCustomItems());
        }
        articleListItemPojoListLive.postValue(articleListItemPojoList);
    }*/


    public void addOrUpdateArticleList(String subdomain, String userId, String opportunityId, ArrayList<ArticleListItemPojo> articleListItemPojoList, String totalVolume, String totalWeight, String signatureBase64String, String jobId, File file, final ResponseListener<BaseResponseModel> responseListener) {

        RawBodyRequestModel<ArticleListResponseDetailsModel> rawBodyRequestModel = new RawBodyRequestModel<>(subdomain, userId, opportunityId, jobId, createArticleListResponseDetailsModelFromArticleList(articleListItemPojoList, signatureBase64String, totalVolume, totalWeight));

        DataRepository.getInstance().addOrUpdateArticleList(rawBodyRequestModel, file, responseListener);
    }

    private ArticleListResponseDetailsModel createArticleListResponseDetailsModelFromArticleList(ArrayList<ArticleListItemPojo> articleListItemPojoList, String signatureBase64String, String totalVolume, String totalWeight) {
        if (articleListItemPojoList == null) {
            return null;
        }
        ArrayList<ArticleListItemPojo> normalArticleListItemPojoList = new ArrayList<>();
        ArrayList<ArticleListItemPojo> customArticleListItemPojoList = new ArrayList<>();


        for (int i = 0; i < articleListItemPojoList.size(); i++) {
            ArticleListItemPojo articleListItemPojo = articleListItemPojoList.get(i);
            if (articleListItemPojo.getItemType() == Constants.ArticleListItemTypes.NORMAL_TYPE) {
                normalArticleListItemPojoList.add(articleListItemPojo);
            } else if (articleListItemPojo.getItemType() == Constants.ArticleListItemTypes.CUSTOM_TYPE) {
                customArticleListItemPojoList.add(articleListItemPojo);
            }
        }

        ArticleListResponseDetailsModel articleListResponseDetailsModel = new ArticleListResponseDetailsModel();
        articleListResponseDetailsModel.setCustomItems(customArticleListItemPojoList);
        articleListResponseDetailsModel.setNormalItems(normalArticleListItemPojoList);

        articleListResponseDetailsModel.setArticleSignature(signatureBase64String);
        articleListResponseDetailsModel.setTotalVolume(totalVolume);
        articleListResponseDetailsModel.setTotalWeight(totalWeight);

        return articleListResponseDetailsModel;
    }


    public void submitConfirmationDetails(String subDomain, String userId, String opportunityId, ConfirmationSubmitRequestModel confirmationSubmitRequestModel, String jobId, MultipartBody.Part file, final ResponseListener<BaseResponseModel> responseListener) {

        RawBodyRequestModel<ConfirmationSubmitRequestModel> rawBodyRequestModel = new RawBodyRequestModel<>(subDomain, userId, opportunityId, jobId, confirmationSubmitRequestModel);

        DataRepository.getInstance().submitConfirmationDetails(rawBodyRequestModel, file, responseListener);
    }


    public void deleteArticle(String subdomain, String userId, String opportunityId, String itemId, String id, final ResponseListener<BaseResponseModel> responseListener) {
        DataRepository.getInstance().deleteArticle(subdomain, userId, opportunityId, itemId, id, responseListener);
    }

    public void getCratingChargesDetails(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<ArrayList<CratingChargesPojo>>> responseListener) {
        DataRepository.getInstance().getCratingChargesDetails(subdomain, userId, opportunityId, jobId, new ResponseListener<BaseResponseModel<ArrayList<CratingChargesPojo>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<CratingChargesPojo>> response, String usedUrlKey) {
                cratingChargesPojoListLive.postValue(response.getData());
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<CratingChargesPojo>>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }

    public int getArticleObjectListPosition(ArticleListItemPojo listItemPojo) {
        int numToReturn = -1;
        if (articleListItemPojoListLive != null && articleListItemPojoListLive.getValue() != null) {
            for (int i = 0; i < articleListItemPojoListLive.getValue().size(); i++) {
                if (articleListItemPojoListLive.getValue().get(i).getId().equals(listItemPojo.getId()) &&
                        articleListItemPojoListLive.getValue().get(i).getItemName().equals(listItemPojo.getItemName()) &&
                        articleListItemPojoListLive.getValue().get(i).getActualQty().equals(listItemPojo.getActualQty()) &&
                        articleListItemPojoListLive.getValue().get(i).getBol_volume().equals(listItemPojo.getBol_volume())) {
                    return i;
                }
            }
        }
        return numToReturn;
    }
}
