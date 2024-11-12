package com.example.orderrestaurantapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderrestaurantapp.R;
import com.example.orderrestaurantapp.menu.Food;
import com.example.orderrestaurantapp.menu.OrderInfo;

import java.util.List;

public class UmsatzAdapter extends RecyclerView.Adapter<UmsatzAdapter.ViewHolder> {
    private final List<OrderInfo> orderInfoList;
    public Context context;
    public UmsatzAdapter (Context context, List<OrderInfo> orderInfoList){
        this.orderInfoList = orderInfoList;
        this.context = context;
    }


    @NonNull
    @Override
    public UmsatzAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_finance, parent,false);

        return new UmsatzAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UmsatzAdapter.ViewHolder holder, int position) {
        OrderInfo orderInfo = orderInfoList.get(position);
        holder.timestamp.setText(orderInfo.getTimestamp());
        holder.price_of_order_a_day.setText(orderInfo.getTotal_price());
        holder.type_payment.setText(orderInfo.getType_payment());
    }

    @Override
    public int getItemCount() {
        return orderInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView timestamp;
        TextView price_of_order_a_day;
        TextView type_payment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timestamp = itemView.findViewById(R.id.timestamp);
            price_of_order_a_day = itemView.findViewById(R.id.price_of_order_for_the_day);
            type_payment = itemView.findViewById(R.id.type_of_payment);
        }
    }
}