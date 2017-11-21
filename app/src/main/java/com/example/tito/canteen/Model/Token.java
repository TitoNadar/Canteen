package com.example.tito.canteen.Model;

/**
 * Created by tito on 12/11/17.
 */

public class Token {
    private String token;
    private boolean isServerToken;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isServerToken() {
        return isServerToken;
    }

    public void setServerToken(boolean serverToken) {
        isServerToken = serverToken;
    }

    public Token(String token, boolean isServerToken) {

        this.token = token;
        this.isServerToken = isServerToken;
    }

    public Token() {
    }
}
