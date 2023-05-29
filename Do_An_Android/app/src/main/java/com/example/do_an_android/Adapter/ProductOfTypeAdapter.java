package com.example.do_an_android.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.do_an_android.Activity.ProductDetailActivity;
import com.example.do_an_android.Model.ProductModel;
import com.example.do_an_android.Model.Server;
import com.example.do_an_android.Model.Support;
import com.example.do_an_android.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductOfTypeAdapter extends RecyclerView.Adapter<ProductOfTypeAdapter.ViewProductOfType> {
Context context;
int layout;
ArrayList<ProductModel> arrayList;


    public ProductOfTypeAdapter(Context context, int layout, ArrayList<ProductModel> arrayList) {
        this.context = context;
        this.layout = layout;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewProductOfType onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewProductOfType(LayoutInflater.from(context).inflate(layout,null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewProductOfType holder, int position) {
        ProductModel productModel=arrayList.get(position);
        Picasso.get().load(Server.urlImage+productModel.getImage()).into(holder.imageProductOfType);
        holder.nameProductOfType.setText(productModel.getName());
        if(productModel.getPrice_discounted()>0)
        {
            holder.priceSaleProductOfType.setText(Support.ConvertMoney(productModel.getPrice_discounted()));
            holder.priceProductOfType.setText(Support.ConvertMoney(productModel.getPrice()));
            holder.unitPriceProductOfType.setText("đ");
        }
        else
        {
            holder.unitPriceProductOfType.setText("");
            holder.priceProductOfType.setText("");
            holder.priceSaleProductOfType.setText(Support.ConvertMoney(productModel.getPrice()));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ProductDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("productDetail",productModel);
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewProductOfType extends RecyclerView.ViewHolder{

        ImageView imageProductOfType;
        TextView nameProductOfType,priceSaleProductOfType,unitPriceSaleProductOfType
                ,unitPriceProductOfType,priceProductOfType;

        public ViewProductOfType(@NonNull View itemView) {
            super(itemView);
            imageProductOfType = itemView.findViewById(R.id.imageProductOfType);
            nameProductOfType = itemView.findViewById(R.id.nameProductOfType);
            priceSaleProductOfType=itemView.findViewById(R.id.priceSaleProductOfType);
            unitPriceSaleProductOfType=itemView.findViewById(R.id.unitPriceSaleProductOfType);
            unitPriceProductOfType=itemView.findViewById(R.id.unitPriceProductOfType);
            priceProductOfType=itemView.findViewById(R.id.priceProductOfType);
            unitPriceProductOfType.setPaintFlags(unitPriceProductOfType.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
            unitPriceSaleProductOfType.setPaintFlags(unitPriceSaleProductOfType.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
            priceProductOfType.setPaintFlags(priceProductOfType.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        }
    }
}
