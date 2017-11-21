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
import com.example.tito.canteen.Interfaces.ItemClickListener;
import com.example.tito.canteen.Model.Order;
import com.example.tito.canteen.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by tito on 28/10/17.
 */

class CartViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener,View.OnCreateContextMenuListener {
    public TextView txt_cart_name,txt_cart_price;
    public ImageView cart_count;
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CartViewHolder(View itemView) {
        super(itemView);
        txt_cart_name=(TextView)itemView.findViewById(R.id.cart_item_name);
        txt_cart_price=(TextView)itemView.findViewById(R.id.cart_item_price);
        cart_count=(ImageView)itemView.findViewById(R.id.cart_item_count);
        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle("action to be performed");
            menu.add(0,0,getAdapterPosition(), Common.DELETE);

    }
}

public class CartAdapter  extends RecyclerView.Adapter<CartViewHolder>{
    private List<Order> listdata=new ArrayList<>();
    private Context context;

    public CartAdapter(List<Order> listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View itemView =inflater.inflate(R.layout.card_layout,parent,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        TextDrawable drawable=TextDrawable.builder().buildRound(""+listdata.get(position).getQuantity(), Color.RED);
        holder.cart_count.setImageDrawable(drawable);
        Locale locale=new Locale("en","US");
        NumberFormat numberFormat=NumberFormat.getCurrencyInstance(locale);
        int price=(Integer.parseInt(listdata.get(position).getPrice()))*(Integer.parseInt(listdata.get(position).getQuantity()));
        holder.txt_cart_price.setText(numberFormat.format(price));
        holder.txt_cart_name.setText(listdata.get(position).getProductName());


    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }



}

