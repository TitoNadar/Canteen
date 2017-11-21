package com.example.tito.canteen.Remote;



import com.example.tito.canteen.Model.MyResponse;
import com.example.tito.canteen.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by tito on 12/11/17.
 */

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:PASTE_YOUR_KEY_HERE"
            }

    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification (@Body Sender body);


}
