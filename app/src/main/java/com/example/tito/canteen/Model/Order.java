package com.example.tito.canteen.Model;

/**
 * Created by tito on 28/10/17.
 */

public class Order {
    private String ProductID;
    private String ProductName;
    private String Quantity;
    private String Price;
    private String Discount;

    public Order( String productID, String productName, String quantity,  String price,String discount) {

        ProductID = productID;
        ProductName = productName;
        Quantity = quantity;
        Discount = discount;
        Price = price;
    }


    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public Order() {

    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
