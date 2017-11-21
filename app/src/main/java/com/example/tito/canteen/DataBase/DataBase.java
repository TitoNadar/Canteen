package com.example.tito.canteen.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.tito.canteen.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tito on 28/10/17.
 */

public class DataBase extends SQLiteAssetHelper {
    private static final String DB_name = "Canteen.db";

    private static final int DB_version = 1;

    public DataBase(Context context) {
        super(context, DB_name, null, DB_version);
    }
    public List<Order> getCarts() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        String[] sqlselect = {"ProductId", "ProductName", "Quantity", "Price", "Discount"};
        String sqltable = "OrderDetail";
        sqLiteQueryBuilder.setTables(sqltable);
        Cursor cursor = sqLiteQueryBuilder.query(sqLiteDatabase, sqlselect, null, null, null, null, null);
        final List<Order> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                result.add(new Order(cursor.getString(cursor.getColumnIndex("ProductId")),
                        cursor.getString(cursor.getColumnIndex("ProductName")),
                        cursor.getString(cursor.getColumnIndex("Quantity")),
                        cursor.getString(cursor.getColumnIndex("Price")),
                        cursor.getString(cursor.getColumnIndex("Discount"))));

            } while (cursor.moveToNext());
        }
        return result;
    }
    public void addtoCart(Order order) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail(ProductId,ProductName,Quantity,Price,Discount) VALUES('%s','%s','%s','%s','%s');",
                order.getProductID(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount());
        sqLiteDatabase.execSQL(query);
    }
    public void cleanCart() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        sqLiteDatabase.execSQL(query);
    }

    public void addtoFavorites(String foodid)
    {
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();
        String query=String.format("INSERT INTO Favorites(Foodid) VALUES('%s');",foodid);
sqLiteDatabase.execSQL(query);

    }
    public void removefromFavorites(String foodid)
    {
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();
        String query=String.format("DELETE FROM Favorites WHERE Foodid ='%s';",foodid);
        sqLiteDatabase.execSQL(query);

    }
    public boolean isFavorites(String foodid)
    {
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();
        String query=String.format("SELECT * FROM Favorites WHERE Foodid ='%s';",foodid);
        Cursor cursor=sqLiteDatabase.rawQuery(query,null);
        if(cursor.getCount()>0)
        {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

}