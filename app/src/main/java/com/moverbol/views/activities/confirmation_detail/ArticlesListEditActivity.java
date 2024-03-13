package com.moverbol.views.activities.confirmation_detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.moverbol.R;
import com.moverbol.adapters.ArticleEditAdapter;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.ArticlesListEditBinding;
import com.moverbol.model.confirmationPage.artialeList.ArticleListItemPojo;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.ConfirmationDetailsViewModel;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by Admin on 23-09-2017.
 */

public class ArticlesListEditActivity  extends BaseAppCompactActivity {

    public static final String EXTRA_ARTICLE_LIST = "article_list";
    public static final String EXTRA_ARTICLE_LIST_TO_SEND = "article_list_to_send";
    private ConfirmationDetailsViewModel viewModel;
    private ArticlesListEditBinding binding;
    private ArticleEditAdapter adapter;

    private boolean areItemsChanged = false;

    private ArrayList<Integer> deletedListItemPositionList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_articles_list_edit);

        /**
         * There was delete option. Now I am asked to remove that. It is kept just in case I am asked again to add it.
         */
        adapter = new ArticleEditAdapter(/*new ArticleEditAdapter.OnDeleteClickedListener() {

            @Override
            public void onDeleteClicked(int adapterPosition) {

                ArticleListItemPojo listItemPojo = adapter.getHomeList().get(adapterPosition);

                if(viewModel.articleListItemPojoListLive.getValue()!=null){
                    int adapterPositionFrmViewModelList = viewModel.getArticleObjectListPosition(listItemPojo);
                    if(adapterPositionFrmViewModelList!=(-1)) {
                        viewModel.articleListItemPojoListLive.getValue().get(adapterPositionFrmViewModelList).setBolStatus(Constants.BOLStatusForArticleList.STATUS_DELETE);
                    }
                }
                adapter.removeItem(adapterPosition);
            }
        }*/);
        binding.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(ConfirmationDetailsViewModel.class);

        viewModel.articleListItemPojoListLive.observe(this, new Observer<ArrayList<ArticleListItemPojo>>() {
            @Override
            public void onChanged(@Nullable ArrayList<ArticleListItemPojo> articleListItemPojos) {
                /*ArrayList<ArticleListItemPojo> articleListItemPojosForAdapter = new ArrayList<>(articleListItemPojos);
                adapter.setHomeList(articleListItemPojosForAdapter);*/
                adapter.setHomeList(articleListItemPojos);
            }
        });

        setToolbar(binding.toolbar.toolbar, getString(R.string.list_of_articles), R.drawable.ic_arrow_back_white_24dp);

        deletedListItemPositionList = new ArrayList<>();
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if(viewModel.articleListItemPojoListLive.getValue()==null) {
            if(getIntent().hasExtra(EXTRA_ARTICLE_LIST)){
                viewModel.articleListItemPojoListLive.setValue(getIntent().getParcelableArrayListExtra(EXTRA_ARTICLE_LIST));
            }
        }
    }

    public void updateArticleList(View view) {
        /*if(!shouldMakeNetworkCall(binding.getRoot())){
            return;
        }
        showProgress();
        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();

        if(viewModel.articleListItemPojoListLive.getValue()!=null) {
            for (int i = 0; i < viewModel.articleListItemPojoListLive.getValue().size(); i++) {
                viewModel.articleListItemPojoListLive.getValue().get(i).setWeightUsingVolumeAndQuantity();
            }
        }

        viewModel.addOrUpdateArticleList(subdomain, userId, opportunityId, viewModel.articleListItemPojoListLive.getValue(), new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
                setResultForArticleList();
                finish();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }

        });*/

        if(viewModel.articleListItemPojoListLive.getValue()!=null) {
            for (int i = 0; i < viewModel.articleListItemPojoListLive.getValue().size(); i++) {
                viewModel.articleListItemPojoListLive.getValue().get(i).setWeightUsingVolumeAndQuantity();
            }
        }

        areItemsChanged = true;

        setResultForArticleList();
        finish();
    }


    private void callDeleteArticleItem(final int listItemPosition) {

        if(!shouldMakeNetworkCall(binding.getRoot())){
            return;
        }

        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();
        String itemId = "";

        final ArrayList<ArticleListItemPojo> articleListItemPojoList = viewModel.articleListItemPojoListLive.getValue();

        if(articleListItemPojoList==null){
            return;
        }

        ArticleListItemPojo articleListItemPojo = articleListItemPojoList.get(listItemPosition);

        if(articleListItemPojo==null){
            return;
        }

        itemId = articleListItemPojo.getItemId();
        if(itemId==null){
            itemId = "0";
        }

        String id = articleListItemPojo.getId();

        showProgress();

        viewModel.deleteArticle(subdomain, userId, opportunityId, itemId, id, new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
                /*deletedListItemPositionList.add(listItemPosition);
                adapter.removeItem(listItemPosition);
                */
//                articleListItemPojoList.remove(listItemPosition);
                areItemsChanged = true;
                callGetArticleList(false);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if(serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))){
                    Util.showLoginErrorDialog(ArticlesListEditActivity.this);
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


    private void callGetArticleList(boolean shouldShowProgress) {
        if(!shouldMakeNetworkCall(binding.getRoot())){
            return;
        }

        if(shouldShowProgress){
            showProgress();
        }

        viewModel.getArticleItemList(MoversPreferences.getInstance(this).getSubdomain(), "", "", new ResponseListener<BaseResponseModel<ArrayList<ArticleListItemPojo>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<ArticleListItemPojo>> response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if(serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))){
                    Util.showLoginErrorDialog(ArticlesListEditActivity.this);
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



    public static void startForResult(Activity activity, int requestCode, ArrayList<ArticleListItemPojo> articleListItemPojoArrayList) {
        Intent starter = new Intent(activity, ArticlesListEditActivity.class);
        starter.putParcelableArrayListExtra(EXTRA_ARTICLE_LIST, articleListItemPojoArrayList);
        activity.startActivityForResult(starter, requestCode);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(areItemsChanged)
        setResultForArticleList();
    }

    private void setResultForArticleList() {
        Intent intent = new Intent();
//        intent.putParcelableArrayListExtra(EXTRA_ARTICLE_LIST, viewModel.articleListItemPojoListLive.getValue());
        intent.putParcelableArrayListExtra(EXTRA_ARTICLE_LIST, adapter.getHomeList());
//        intent.putParcelableArrayListExtra(EXTRA_ARTICLE_LIST_TO_SEND, viewModel.articleListItemPojoListLive.getValue());
        setResult(RESULT_OK, intent);
    }

    /*@Override
    public void onStop() {

//        intent.putIntegerArrayListExtra(SELECTION_LIST, selected);
        if(areItemsDeleted){
            setResult(RESULT_OK);
        }else {
            setResult(RESULT_CANCELED);
        }

        super.onStop();
    }*/

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        if(areItemsDeleted){
            setResult(RESULT_OK);
        }else {
            setResult(RESULT_CANCELED);
        }
    }*/

}
