package com.example.tito.canteen.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.tito.canteen.Model.User;
import com.example.tito.canteen.Remote.APIService;
import com.example.tito.canteen.Remote.RetrofitClient;

/**
 * Created by tito on 27/10/17.
 */

public class Common {
    public static User currentuser;
    private static final String BASE_URL="https://fcm.googleapis.com/";

    public static APIService getFCMService()
    {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

    public static String convertCodetoStatus(String status) {
        if(status.equals("0"))
        {
            return "Placed";
        }
        else if (status.equals("1"))
        {return "On my Way";}
        else
        {return "Shipped";}

    }
    public static String DELETE="Delete";
    public static String REMOVE="Remove";
    public static String USER_KEY="User";
    public static String USER_PASSWORD_KEY="Password";

    public static boolean isConnectedToInternet(Context context)
    {
        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager!=null)
        {
            NetworkInfo[] networkInfos=connectivityManager.getAllNetworkInfo();
            if(networkInfos!=null)
            {
                for(int i=0;i<networkInfos.length;i++)
                {
                    if(networkInfos[i].getState()==NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }

        }
        return false;
    }

}
