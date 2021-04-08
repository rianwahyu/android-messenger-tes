package com.depobangunan.depomessenger.Fragment;

import com.depobangunan.depomessenger.Notification.MyResponse;
import com.depobangunan.depomessenger.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAsUqvwfQ:APA91bFtaOiE-02yL2UWUO64bqmTO6wES5GhBD2Yd0cpfBBmGj7ub9EhDoSqPNV06MPhMenxvQZhT6Zq0S6aGHVNdBtP1-dVJWogk1RCCN2sBNghgZsAF7_hKVoEWS8sop0arXa7Y0E7"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
