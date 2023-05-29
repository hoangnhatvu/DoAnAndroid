package com.example.do_an_android.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.do_an_android.Model.OrderDetailModel;
import com.example.do_an_android.Model.Support;
import com.example.do_an_android.R;

import java.util.ArrayList;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewOrderDetail> {
    Context context;
    int layout;
    ArrayList<OrderDetailModel> orderDetailModelArrayList;

    public OrderDetailAdapter(Context context, int layout, ArrayList<OrderDetailModel> orderDetailModelArrayList) {
        this.context = context;
        this.layout = layout;
        this.orderDetailModelArrayList = orderDetailModelArrayList;
    }

    @NonNull
    @Override
    public ViewOrderDetail onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewOrderDetail(LayoutInflater.from(context).inflate(layout, null));
    }
    @Override
    public void onBindViewHolder(@NonNull ViewOrderDetail holder, int position) {
        OrderDetailModel orderDetailModel = orderDetailModelArrayList.get(position);
        holder.nameProductOrderDetail.setText(orderDetailModel.getNameProduct());
        holder.quantityOrderDetail.setText(orderDetailModel.getQuantity() + "");
        holder.priceOrderDetail.setText(Support.ConvertMoney(orderDetailModel.getPrice()));
        holder.totalOrderDetail.setText(Support.ConvertMoney(orderDetailModel.getTotal()));
    }

    @Override
    public int getItemCount() {
        return orderDetailModelArrayList.size();
    }

    public static class ViewOrderDetail extends RecyclerView.ViewHolder {
        TextView nameProductOrderDetail, quantityOrderDetail, priceOrderDetail
                , totalOrderDetail,unitTotalOrderDetail,unitPriceOrderDetail;

        public ViewOrderDetail(@NonNull View itemView) {
            super(itemView);
            nameProductOrderDetail = itemView.findViewById(R.id.nameProductOrderDetail);
            quantityOrderDetail = itemView.findViewById(R.id.quantityOrderDetail);
            priceOrderDetail = itemView.findViewById(R.id.priceOrderDetail);
            totalOrderDetail = itemView.findViewById(R.id.totalOrderDetail);
            unitPriceOrderDetail = itemView.findViewById(R.id.unitPriceOrderDetail);
            unitTotalOrderDetail = itemView.findViewById(R.id.unitTotalOrderDetail);
            unitPriceOrderDetail.setPaintFlags(unitPriceOrderDetail.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
            unitTotalOrderDetail.setPaintFlags(unitTotalOrderDetail.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        }
    }
}
