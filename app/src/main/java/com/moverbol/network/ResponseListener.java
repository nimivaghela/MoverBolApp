package com.moverbol.network;

import com.moverbol.network.model.BaseResponseModel;

import retrofit2.Call;

/**
 * Created by aayushi on 24/8/17.
 */

public interface ResponseListener< T> {
    void onResponse(T response, String usedUrlKey);

    void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey);

    void onFailure(Call<T> call, Throwable t, String message);
}
