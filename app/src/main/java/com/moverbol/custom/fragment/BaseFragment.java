package com.moverbol.custom.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.moverbol.R;
import com.moverbol.util.Util;

/**
 * Created by sumeet on 18/11/15.
 */
public abstract class BaseFragment extends Fragment {

    private AlertDialog alertDialog;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        alertDialog = new AlertDialog.Builder(requireContext(), R.style.AppTheme_AlertDialogTheme)
                .setView(R.layout.dialog_progress)
                .setCancelable(false)
                .create();
    }

    protected void showProgress() {
        if (alertDialog != null && !alertDialog.isShowing() && !requireActivity().isFinishing()) {
            alertDialog.show();
        }

    }

    protected void hideProgress() {
        try {
            if (alertDialog != null && alertDialog.isShowing() && !requireActivity().isFinishing()) {
                alertDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onStop() {
        super.onStop();
    }

    protected boolean shouldMakeNetworkCall(View viewToShowSnackBar) {
        try {
            Util.hideSoftKeyboard(requireActivity());
            if (Util.isConnected(this.requireActivity().getApplicationContext())) {
                return true;
            } else {
                Snackbar.make(viewToShowSnackBar, getText(R.string.error_no_connection), Snackbar.LENGTH_LONG).show();
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
