package com.example.tito.canteen.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.example.tito.canteen.Common.Common;
import com.example.tito.canteen.Interfaces.ItemClickListener;
import com.example.tito.canteen.R;

/**
 * Created by tito on 3/11/17.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener {
public TextView orderstatus,orderid,orderphone,orderaddress;
private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);
        orderstatus=(TextView)itemView.findViewById(R.id.order_status);
        orderid=(TextView)itemView.findViewById(R.id.order_id);
        orderaddress=(TextView)itemView.findViewById(R.id.order_address);
        orderphone=(TextView)itemView.findViewById(R.id.order_phone);
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
