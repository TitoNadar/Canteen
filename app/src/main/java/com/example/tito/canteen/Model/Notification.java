package com.example.tito.canteen.Model;

/**
 * Created by tito on 12/11/17.
 */

public class Notification {
    public String body;
    public String title;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Notification() {

    }

    public Notification(String body, String title) {

        this.body = body;
        this.title = title;
    }
}
