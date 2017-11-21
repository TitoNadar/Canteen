package com.example.tito.canteen.Model;

/**
 * Created by tito on 12/11/17.
 */

public class Rating {
    private String userphone,foodId,rateValue,comment;

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Rating() {

    }

    public Rating(String userphone, String foodId, String rateValue, String comment) {

        this.userphone = userphone;
        this.foodId = foodId;
        this.rateValue = rateValue;
        this.comment = comment;
    }
}
