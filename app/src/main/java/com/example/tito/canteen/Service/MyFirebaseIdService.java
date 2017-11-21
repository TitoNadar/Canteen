package com.example.tito.canteen.Service;

import com.example.tito.canteen.Common.Common;
import com.example.tito.canteen.Model.Token;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by tito on 12/11/17.
 */

public class MyFirebaseIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String tokenRefreshed= FirebaseInstanceId.getInstance().getToken();
        if(Common.currentuser!=null)   //error if not added
        {
        updateTokenToFirebase(tokenRefreshed);
    }

    }

    private void updateTokenToFirebase(String tokenRefreshed) {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=database.getReference("Tokens");
        Token token=new Token(tokenRefreshed,false); //false because token send from client side
        databaseReference.child(Common.currentuser.getPhone()).setValue(token);

          }


}
