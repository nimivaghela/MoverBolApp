package com.moverbol.views.activities.moveprocess;

import android.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;

import com.moverbol.HomeActivity;
import com.moverbol.R;
import com.moverbol.adapters.NotesAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.custom.dialogs.AddNoteDialog;
import com.moverbol.databinding.NotesBinding;
import com.moverbol.model.notes.NotesPojo;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.NotesViewModel;

import java.util.List;

import retrofit2.Call;

/**
 * Created by Admin on 09-10-2017.
 */

public class NotesActivity extends BaseAppCompactActivity implements AddNoteDialog.OnNoteSubmittedListener{

    private NotesViewModel viewModel;
    private NotesAdapter adapter;
    private NotesBinding binding;
    private static final String EXTRA_OPPORTUNITY_ID = "extra_opportunity_id";
    private boolean intentHasCameFromNotification;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialisation();
        setIntentData();
        setActionListeners();
        setViewModelObservers();

        setToolbar(binding.toolbar.toolbar, getString(R.string.notes), R.drawable.ic_arrow_back_white_24dp);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (viewModel.notesListLive.getValue() == null) {
            callGetSubmittedNotes();
        }
    }

    private void initialisation() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notes);
        viewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
        adapter = new NotesAdapter();

        binding.setAdapter(adapter);
    }

    private void setIntentData() {
        if(getIntent().hasExtra(Constants.EXTRA_NOTIFICATION_INTENT) && getIntent().getBooleanExtra(Constants.EXTRA_NOTIFICATION_INTENT, false)){
            intentHasCameFromNotification = true;
        }
    }

    private void setViewModelObservers() {

        viewModel.notesListLive.observe(this, new Observer<List<NotesPojo>>() {
            @Override
            public void onChanged(@Nullable List<NotesPojo> notesPojos) {
                if (notesPojos != null) {
                    adapter.setHomeList(notesPojos);
                }
            }
        });

    }


    private void callGetSubmittedNotes() {

        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        showProgress();

        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();

        viewModel.getSubmittedNotes(subdomain, userId, opportunityId, jobId, new ResponseListener<BaseResponseModel<List<NotesPojo>>>() {
            @Override
            public void onResponse(BaseResponseModel<List<NotesPojo>> response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if(serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))){
                    Util.showLoginErrorDialog(NotesActivity.this);
                    return;
                }

                if(!serverResponseError.getMessage().toLowerCase().contains("empty")) {
                    Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<List<NotesPojo>>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });

    }

    private void setActionListeners() {
        binding.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddNoteDialog();
            }
        });

    }

    public void openAddNoteDialog() {
        AddNoteDialog addNoteDialog = new AddNoteDialog();
        /*addNoteDialog.setOnNoteSubmittedListener(new AddNoteDialog.OnNoteSubmittedListener() {
            @Override
            public void onNewNoteSubmitted(String note) {
                callAddNote(note);
            }
        });*/
        addNoteDialog.show(getSupportFragmentManager(), "dialog");
    }

    private void callAddNote(String note) {
        if(!shouldMakeNetworkCall(binding.getRoot())){
            return;
        }

        showProgress();

        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();
        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();

        viewModel.addNote(subdomain, userId, opportunityId, note, jobId, new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
                callGetSubmittedNotes();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if(serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))){
                    Util.showLoginErrorDialog(NotesActivity.this);
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


    private void showMessageDialog(String message) {

        AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.AppTheme_AlertDialogTheme)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callGetSubmittedNotes();
                        dialog.dismiss();
                    }
                })
                .create();
        alertDialog.show();
    }


    public static void start(Context context, String opportunityId) {
        Intent starter = new Intent(context, NotesActivity.class);
        starter.putExtra(EXTRA_OPPORTUNITY_ID, opportunityId);
        context.startActivity(starter);
    }

    @Override
    public void onBackPressed() {
        if(intentHasCameFromNotification){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        super.onBackPressed();
    }

    @Override
    public void onNewNoteSubmitted(String note) {
        callAddNote(note);
    }
}