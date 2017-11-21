package com.example.tito.canteen.Model;

/**
 * Created by tito on 27/10/17.
 */

public class Category {

private String name;
private String image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Category() {

    }

    public Category(String name, String image) {

        this.name = name;
        this.image = image;
    }
}