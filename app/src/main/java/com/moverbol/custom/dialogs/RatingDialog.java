package com.moverbol.custom.dialogs;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.moverbol.R;
import com.moverbol.databinding.RatingDialogBinding;

/**
 * Created by Admin on 10-10-2017.
 */

public class RatingDialog extends BaseDialogFragment {
    private RatingDialogBinding binding;

    private RatingActionListener ratingActionListener;

    public interface RatingActionListener{
        void onRatingDialogSkipped(RatingDialog ratingDialog);
        void onRatingSubmitted(float rating, String message, RatingDialog ratingDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_rating_dialog, container, false);
        setCancelable(false);

        setActionListeners();

        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            ratingActionListener = (RatingActionListener) context;
        } catch (ClassCastException cce){
            throw new ClassCastException(context.toString()
             + " must implement RatingDialog.RatingActionListener");
        }
    }

    private void setActionListeners() {

        binding.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if(b){
                    binding.btnDone.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    binding.btnDone.setEnabled(true);
                }
            }
        });

        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ratingActionListener!=null){
                    ratingActionListener.onRatingSubmitted(binding.ratingBar.getRating(), binding.editText3.getText().toString(), RatingDialog.this);
                }
            }
        });

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ratingActionListener!=null){
                    ratingActionListener.onRatingDialogSkipped(RatingDialog.this);
                }
            }
        });
    }


    /*private void setRatingActionListener(RatingActionListener ratingActionListener) {
        this.ratingActionListener = ratingActionListener;
    }*/

    public static void start(FragmentManager supportFragmentManager) {
        RatingDialog ratingDialog = new RatingDialog();
        ratingDialog.show(supportFragmentManager, "dialog");
    }

    /*public static void startWithActionListeners(FragmentManager supportFragmentManager, RatingActionListener ratingActionListener) {
        RatingDialog ratingDialog = new RatingDialog();
        ratingDialog.setRatingActionListener(ratingActionListener);
        ratingDialog.show(supportFragmentManager, "dialog");
    }*/

}