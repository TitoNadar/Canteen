package com.example.tito.canteen.Model;



import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by tito on 12/11/17.
 */

public class Sender
{
    public String to;
    public Notification notification;

    public Sender(String to, Notification notification) {
        this.to = to;
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
