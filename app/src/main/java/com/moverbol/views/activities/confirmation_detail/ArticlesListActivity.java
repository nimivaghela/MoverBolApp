package com.moverbol.views.activities.confirmation_detail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.moverbol.R;
import com.moverbol.adapters.ArticleAdapter;
import com.moverbol.adapters.ArticleRoomAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.custom.dialogs.AddArticleDialog;
import com.moverbol.custom.dialogs.PaymentSignDialog;
import com.moverbol.databinding.ArticlesListBinding;
import com.moverbol.interfaces.ArticleRoomSelectedListener;
import com.moverbol.model.confirmationPage.artialeList.ArticleListItemPojo;
import com.moverbol.model.confirmationPage.artialeList.ArticleRoomModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.DividerItemDecoration;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.ConfirmationDetailsViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by Admin on 22-09-2017.
 */

public class ArticlesListActivity extends BaseAppCompactActivity implements AddArticleDialog.OnNewArticleSubmittedListener, PaymentSignDialog.OnSubmitListener, ArticleRoomSelectedListener {
    private static final int TOTAL_WEIGHT = 3;
    private static final int TOTAL_VOLUME = 2;
    private static final int TOTAL_ACTUAL_QUANTITY = 1;
    private static final int TOTAL_ESTIMATED_QUANTITY = 0;
    private static final int EDIT_ITEM_REQUEST_CODE = 100;
    private static final String MADE_CHANGES_TO_LIST_KEY = "made_changes_to_list";
    private ConfirmationDetailsViewModel viewModel;
    private ArticlesListBinding binding;
    private ArticleAdapter adapter;
    private String opportunityId = "";
    private final ArticleRoomAdapter roomAdapter = new ArticleRoomAdapter();
    private String inventoryId = "";
    private String roomId = "";

    private int listItemCount = 0;

    // private boolean madeChangesToList = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_articles_list);
        binding.rvArticles.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        binding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(this).get(ConfirmationDetailsViewModel.class);

        adapter = new ArticleAdapter();
        binding.setAdapter(adapter);
        binding.rvRoom.setAdapter(roomAdapter);
        roomAdapter.setArticleRoomSelectedListener(this);

        viewModel.articleListItemPojoListLive.observe(this, new Observer<ArrayList<ArticleListItemPojo>>() {
            @Override
            public void onChanged(@Nullable ArrayList<ArticleListItemPojo> articleListItemPojos) {
                if (articleListItemPojos == null) {
                    return;
                }

                /*ArrayList<ArticleListItemPojo> articleListItemPojosForAdapter = new ArrayList<>(articleListItemPojos);

                for (int i = articleListItemPojos.size()-1; i >= 0; i--) {
                    if(articleListItemPojos.get(i).getBolStatus().equals(Constants.BOLStatusForArticleList.STATUS_DELETE)){
                        articleListItemPojosForAdapter.remove(articleListItemPojos.get(i));
                    }
                }*/

                adapter.setHomeList(articleListItemPojos);
                if (!articleListItemPojos.isEmpty() && !(MoversPreferences.getInstance(ArticlesListActivity.this).getBolStarted(MoversPreferences.getInstance(ArticlesListActivity.this).getCurrentJobId()))) {
                    binding.imgEdit.setVisibility(View.VISIBLE);
                    binding.imgAdd.setVisibility(View.VISIBLE);
                }

                setArticleListTotal(articleListItemPojos);
            }
        });


        viewModel.articleRoomListLiveData.observe(this, new Observer<ArrayList<ArticleRoomModel>>() {
            @Override
            public void onChanged(ArrayList<ArticleRoomModel> articleRoomModels) {
                roomAdapter.setHomeList(articleRoomModels);
                if (!articleRoomModels.isEmpty()) {
                    binding.txtRoomNotFound.setVisibility(View.GONE);
                    binding.imgAdd.setVisibility(View.VISIBLE);
                    callGetArticleItemList(true, articleRoomModels.get(0).getInventoryId(), articleRoomModels.get(0).getRoomId());
                } else {
                    binding.txtRoomNotFound.setVisibility(View.VISIBLE);
                    binding.imgEdit.setVisibility(View.GONE);
                    binding.imgAdd.setVisibility(View.GONE);
                }
            }
        });


        setToolbar(binding.toolbar.toolbar, getString(R.string.list_of_articles), R.drawable.ic_arrow_back_white_24dp);

        if (getIntent() != null || getIntent().hasExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY)) {
            opportunityId = getIntent().getStringExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY);
        }

     /*   if (savedInstanceState != null && savedInstanceState.containsKey(MADE_CHANGES_TO_LIST_KEY)) {
            madeChangesToList = savedInstanceState.getBoolean(MADE_CHANGES_TO_LIST_KEY);
        }

        binding.setMadeChangesToList(madeChangesToList);*/
    }


    private String getTotalAmountFromArrayList(List<ArticleListItemPojo> articleListItemPojos, int totaltType) {
        int integerToReturn = 0;
        try {
            if (totaltType == TOTAL_ESTIMATED_QUANTITY) {
                for (int i = 0; i < articleListItemPojos.size(); i++) {
                    integerToReturn += Double.parseDouble(articleListItemPojos.get(i).getQuantity());
                }
            } else if (totaltType == TOTAL_ACTUAL_QUANTITY) {
                for (int i = 0; i < articleListItemPojos.size(); i++) {
                    integerToReturn += Double.parseDouble(articleListItemPojos.get(i).getActualQty());
                }
            } else if (totaltType == TOTAL_VOLUME) {
                for (int i = 0; i < articleListItemPojos.size(); i++) {
                    integerToReturn += Double.parseDouble(articleListItemPojos.get(i).getTotalVolume());
                }
            } else if (totaltType == TOTAL_WEIGHT) {
                for (int i = 0; i < articleListItemPojos.size(); i++) {
                    integerToReturn += Double.parseDouble(articleListItemPojos.get(i).getBol_weight());
                }
            }
        } catch (NumberFormatException ignore) {

        }


        return integerToReturn + "";
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (viewModel.articleRoomListLiveData.getValue() == null) {
            callGetArticleRoomList();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (viewModel.articleListItemPojoListLive.getValue() != null)
            listItemCount = viewModel.articleListItemPojoListLive.getValue().size();
    }

    private void callGetArticleRoomList() {
        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }
        showProgress();

        viewModel.getArticleRoomList(MoversPreferences.getInstance(this).getSubdomain(), opportunityId, new ResponseListener<BaseResponseModel<ArrayList<ArticleRoomModel>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<ArticleRoomModel>> response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(ArticlesListActivity.this);
                    return;
                }

                Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<ArticleRoomModel>>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }


    private void callGetArticleItemList(boolean shouldShowProgress, String inventoryId, String roomId) {
        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        this.inventoryId = inventoryId;
        this.roomId = roomId;

        if (shouldShowProgress) {
            showProgress();
        }
        viewModel.getArticleItemList(MoversPreferences.getInstance(this).getSubdomain(), inventoryId, roomId, new ResponseListener<BaseResponseModel<ArrayList<ArticleListItemPojo>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<ArticleListItemPojo>> response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(ArticlesListActivity.this);
                    return;
                }

                Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<ArticleListItemPojo>>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void openArticlesEditList(View view) {
        ArticlesListEditActivity.startForResult(this, EDIT_ITEM_REQUEST_CODE, viewModel.articleListItemPojoListLive.getValue());
    }

    public void openArticlesAddActivity(final View view) {
        AddArticleDialog addArticleDialog = new AddArticleDialog();

        /*addArticleDialog.setArticleSubmittedListener(new AddArticleDialog.OnNewArticleSubmittedListener() {
            @Override
            public void onArticleSubmitted(ArticleListItemPojo articleListItemPojo, AddArticleDialog dialog) {

                if (viewModel.articleListResponseDetailsModel == null) {
                    return;
                }

                String inventoryId = viewModel.articleListResponseDetailsModel.getInventoryId();
                String groupId = viewModel.articleListResponseDetailsModel.getGroupId();

                articleListItemPojo.setInventoryId((inventoryId == null ? "" : inventoryId));
                articleListItemPojo.setGroupId((groupId == null ? "" : groupId));
                articleListItemPojo.setId("");
                articleListItemPojo.setBolStatus("1");

                adapter.addToHomeList(articleListItemPojo);

                madeChangesToList = true;
                binding.setMadeChangesToList(madeChangesToList);

                dialog.dismiss();

                setArticleListTotal(adapter.getHomeList());
            }
        });*/
        addArticleDialog.show(getSupportFragmentManager(), "dialog");
    }

    private void callAddArticleListItem(ArrayList<ArticleListItemPojo> articleListItemPojoList, Bitmap signatureBitmap, String inventoryId, String roomId) {
        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();
        String totalVolume = binding.txtTotalVolume.getText().toString();
        String totalWeight = binding.txtTotalWeight.getText().toString();
        showProgress();

        //Bol Status should be one for add and update process.
        for (int i = 0; i < articleListItemPojoList.size(); i++) {
            articleListItemPojoList.get(i).setBolStatus("1");
            articleListItemPojoList.get(i).setInventoryId(inventoryId);
            articleListItemPojoList.get(i).setGroupId(roomId);
            //Backend developer wants rounded up value here
            articleListItemPojoList.get(i).setActualQty(Util.getRoundedUpNumericStringFromDoubleString(articleListItemPojoList.get(i).getActualQty()));
        }

        viewModel.addOrUpdateArticleList(subdomain, userId, opportunityId, articleListItemPojoList, Util.removeFormatAmount(totalVolume), Util.removeFormatAmount(totalWeight), "", jobId, Util.BitmapToFile(this, signatureBitmap), new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
                // madeChangesToList = false;
                //binding.setMadeChangesToList(madeChangesToList);
                Util.showSnackBar(binding.getRoot(), getString(R.string.articles_saved));
                setResult(RESULT_OK);

                callGetArticleItemList(false, inventoryId, roomId);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(ArticlesListActivity.this);
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


    private void setArticleListTotal(List<ArticleListItemPojo> articleListItemPojoList) {
        if (articleListItemPojoList.size() == 0 || MoversPreferences.getInstance(this).getBolStarted(MoversPreferences.getInstance(this).getCurrentJobId())) {
            binding.btnSubmit.setVisibility(View.GONE);
        } else {
            binding.btnSubmit.setVisibility(View.VISIBLE);
        }
        binding.txtTotalWeight.setText(getTotalAmountFromArrayList(articleListItemPojoList, TOTAL_WEIGHT));
        binding.txtTotalEstimatedQuantity.setText(getTotalAmountFromArrayList(articleListItemPojoList, TOTAL_ESTIMATED_QUANTITY));
        binding.txtTotalActualQuantity.setText(getTotalAmountFromArrayList(articleListItemPojoList, TOTAL_ACTUAL_QUANTITY));
        binding.txtTotalVolume.setText(getTotalAmountFromArrayList(articleListItemPojoList, TOTAL_VOLUME));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_ITEM_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra(ArticlesListEditActivity.EXTRA_ARTICLE_LIST)/* && data.hasExtra(ArticlesListEditActivity.EXTRA_ARTICLE_LIST_TO_SEND)*/) {
                    ArrayList<ArticleListItemPojo> listForAdapter = data.getParcelableArrayListExtra(ArticlesListEditActivity.EXTRA_ARTICLE_LIST);
//                    ArrayList<ArticleListItemPojo> listForViewModel = data.<ArticleListItemPojo>getParcelableArrayListExtra(ArticlesListEditActivity.EXTRA_ARTICLE_LIST_TO_SEND);
//                    viewModel.articleListItemPojoListLive.setValue(listForViewModel);
                    viewModel.articleListItemPojoListLive.setValue(listForAdapter);
//                    adapter.notifyDataSetChanged();
                    if (listForAdapter != null && listForAdapter.isEmpty()) {
                        binding.imgEdit.setVisibility(View.GONE);
                    }
//                    adapter.setHomeList(listForAdapter);
                    binding.btnSubmit.setVisibility(View.VISIBLE);

                    setArticleListTotal(adapter.getHomeList());
                }
            }
        }
    }

    public void onSubmitChangesClicked(View view) {
        /*PaymentSignDialog.startWithActionListener(getSupportFragmentManager(), new PaymentSignDialog.OnSubmitListener() {
            @Override
            public void OnSubmit(PaymentSignDialog dialog, Bitmap signatureBitmap) {
                String signatureBase64String = Util.getBase64EncodeStringFromBitmap(signatureBitmap);

                callAddArticleListItem(viewModel.articleListItemPojoListLive.getValue(), signatureBase64String);
                dialog.dismiss();
            }
        }, true);*/

        PaymentSignDialog.start(getSupportFragmentManager(), true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //  outState.putBoolean(MADE_CHANGES_TO_LIST_KEY, madeChangesToList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onArticleSubmitted(ArticleListItemPojo articleListItemPojo, AddArticleDialog dialog) {
        if (viewModel.articleItemList == null) {
            return;
        }

        String inventoryId = articleListItemPojo.getInventoryId();
        String groupId = articleListItemPojo.getGroupId();

        articleListItemPojo.setInventoryId((inventoryId == null ? "" : inventoryId));
        articleListItemPojo.setGroupId((groupId == null ? "" : groupId));
        articleListItemPojo.setId("");
        articleListItemPojo.setBolStatus("1");

        adapter.addToHomeList(articleListItemPojo);

        //  madeChangesToList = true;
        //  binding.setMadeChangesToList(madeChangesToList);

        dialog.dismiss();

        setArticleListTotal(adapter.getHomeList());

    }

    @Override
    public void OnSignatureSubmit(PaymentSignDialog dialog, Bitmap signatureBitmap) {

        callAddArticleListItem((ArrayList<ArticleListItemPojo>) adapter.getHomeList(), signatureBitmap, inventoryId, roomId);
        dialog.dismiss();
    }

    @Override
    public void selectedArticleRoom(ArticleRoomModel articleRoomModel) {
        callGetArticleItemList(false, articleRoomModel.getInventoryId(), articleRoomModel.getRoomId());
    }
}
