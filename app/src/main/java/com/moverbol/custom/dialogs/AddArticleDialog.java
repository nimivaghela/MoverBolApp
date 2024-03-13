package com.moverbol.custom.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.moverbol.R;
import com.moverbol.constants.Constants;
import com.moverbol.databinding.AddArticleBinding;
import com.moverbol.model.confirmationPage.artialeList.ArticleListItemPojo;

import java.util.Objects;

public class AddArticleDialog extends BaseDialogFragment {

    private AddArticleBinding binding;
    private OnNewArticleSubmittedListener articleSubmittedListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_article, container, false);
        setCancelable(true);

        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddArticleDialog.this.dismiss();
            }
        });

        binding.txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate() && articleSubmittedListener != null)
                    articleSubmittedListener.onArticleSubmitted(getArticleListItem(), AddArticleDialog.this);

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            articleSubmittedListener = (OnNewArticleSubmittedListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement AddArticleDialog.OnNewArticleSubmittedListener");
        }
    }

    private ArticleListItemPojo getArticleListItem() {
        ArticleListItemPojo articleListItemPojo = new ArticleListItemPojo();
        articleListItemPojo.setItemType(Constants.ArticleListItemTypes.CUSTOM_TYPE);
        articleListItemPojo.setActualQty(Objects.requireNonNull(binding.edtxtQuantity.getText()).toString());
        articleListItemPojo.setQuantity("0");//When Article is added manually then estimated qty should be 0.
        articleListItemPojo.setItemName(Objects.requireNonNull(binding.edtxtName.getText()).toString());
        articleListItemPojo.setIsShipping("1");
        articleListItemPojo.setBol_volume(binding.edtxtVolume.getText().toString());
        if (TextUtils.isEmpty(binding.edtxtVolume.getText().toString())) {
            articleListItemPojo.setVolume("");
        } else {
            articleListItemPojo.setVolume(binding.edtxtVolume.getText().toString());
        }
        articleListItemPojo.setWeightUsingVolumeAndQuantity();
        return articleListItemPojo;
    }

    private boolean validate() {

        if (TextUtils.isEmpty(binding.edtxtName.getText().toString())) {
            binding.edtxtName.setError(getString(R.string.empty_field_error));
            return false;
        } else if (TextUtils.isEmpty(binding.edtxtQuantity.getText().toString())) {
            binding.edtxtQuantity.setError(getString(R.string.empty_field_error));
            return false;
        } /*else if (TextUtils.isEmpty(binding.edtxtVolume.getText().toString())) {
            binding.edtxtVolume.setError(getString(R.string.empty_field_error));
            return false;
        }*//*else if(TextUtils.isEmpty(binding.edtxtWeight.getText().toString())){
            binding.edtxtName.setError(getString(R.string.empty_field_error));
            return false;
        }*/

        return true;
    }

    public interface OnNewArticleSubmittedListener {
        void onArticleSubmitted(ArticleListItemPojo articleListItemPojo, AddArticleDialog dialog);
    }

}
