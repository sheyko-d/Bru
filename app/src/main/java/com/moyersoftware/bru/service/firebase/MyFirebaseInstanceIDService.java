package com.moyersoftware.bru.service.firebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.moyersoftware.bru.network.ApiFactory;
import com.moyersoftware.bru.util.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        if (!Util.isLoggedIn() || !Util.notificationsEnabled()) return;

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        updateFcmToken(refreshedToken);
    }

    private void updateFcmToken(String refreshedToken) {
        Call<Void> call = ApiFactory.getApiService().updateGoogleToken(refreshedToken,
                Util.getProfile().getToken());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call,
                                   Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }


}
