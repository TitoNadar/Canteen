package com.example.tito.canteen.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.tito.canteen.Common.Common;
import com.example.tito.canteen.FoodList;
import com.example.tito.canteen.Interfaces.ItemClickListener;
import com.example.tito.canteen.Model.Category;
import com.example.tito.canteen.Model.Order;
import com.example.tito.canteen.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by tito on 13/11/17.
 */
class FavViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener,View.OnCreateContextMenuListener {
    public TextView foodname;
    public ImageView foodimage;
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FavViewHolder(View itemView) {
        super(itemView);
      foodname=(TextView)itemView.findViewById(R.id.food_name);
        foodimage=(ImageView)itemView.findViewById(R.id.food_image);
        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.setHeaderTitle("action to be performed");
        menu.add(0,0,getAdapterPosition(), Common.REMOVE);

    }
}

