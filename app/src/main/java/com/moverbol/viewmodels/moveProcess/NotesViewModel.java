package com.moverbol.viewmodels.moveProcess;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.moverbol.DataRepository;
import com.moverbol.model.notes.NotesPojo;
import com.moverbol.model.notes.RawNotesRequest;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;

import java.util.List;

import retrofit2.Call;

/**
 * Created by AkashM on 7/3/18.
 */

public class NotesViewModel extends ViewModel {

    public MutableLiveData<List<NotesPojo>> notesListLive = new MutableLiveData<>();

    public void addNote(String subdomain, String userId, String opportunityId, String description, String jobId, final ResponseListener<BaseResponseModel> responseListener) {
        RawNotesRequest rawNotesRequest = new RawNotesRequest(subdomain, opportunityId, userId, jobId, description);
        DataRepository.getInstance().addNote(rawNotesRequest, responseListener);
    }

    public void getSubmittedNotes(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<List<NotesPojo>>> responseListener) {
        DataRepository.getInstance().getSubmittedNotes(subdomain, userId, opportunityId, jobId, new ResponseListener<BaseResponseModel<List<NotesPojo>>>() {
            @Override
            public void onResponse(BaseResponseModel<List<NotesPojo>> response, String usedUrlKey) {
                if(response.getData()!=null){
                    notesListLive.postValue(response.getData());
                }
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<List<NotesPojo>>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }

}
