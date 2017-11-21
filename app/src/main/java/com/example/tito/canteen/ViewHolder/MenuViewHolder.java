package com.example.tito.canteen.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tito.canteen.Interfaces.ItemClickListener;
import com.example.tito.canteen.R;

/**
 * Created by tito on 27/10/17.
 */

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
   public TextView textView;
   public ImageView   imageView;
   private ItemClickListener itemClickListener;
    public MenuViewHolder(View itemView) {
        super(itemView);
        textView=(TextView)itemView.findViewById(R.id.menu_name);
        imageView=(ImageView)itemView.findViewById(R.id.menu_image);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}
