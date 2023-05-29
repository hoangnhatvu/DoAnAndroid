package com.example.do_an_android.Adapter;

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
import java.util.List;

public class DiscountedProductAdapter extends RecyclerView.Adapter<DiscountedProductAdapter.DiscountedProductViewHolder> {

    Context context;
    int layout;
    ArrayList<ProductModel> discountedProductsList;

    public DiscountedProductAdapter(Context context, int layout, ArrayList<ProductModel> discountedProductsList) {
        this.context = context;
        this.layout = layout;

        this.discountedProductsList = discountedProductsList;
    }

    @NonNull
    @Override
    public DiscountedProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new DiscountedProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountedProductViewHolder holder, int position) {
        ProductModel productModel=discountedProductsList.get(position);
      Picasso.get().load(Server.urlImage+productModel.getImage()).into(holder.discountImageView);
      holder.discountName.setText(productModel.getName());
        holder.discountPrice.setText(Support.ConvertMoney(productModel.getPrice()));
        holder.discountSalePrice.setText(Support.ConvertMoney(productModel.getPrice_discounted()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ProductDetailActivity.class);
                intent.putExtra("productDetail",productModel);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return discountedProductsList.size();
    }

    public static class DiscountedProductViewHolder extends RecyclerView.ViewHolder {

        ImageView discountImageView;
        TextView discountName,discountPrice,discountSalePrice,unitMoneyPriceDiscounted,unitMoneyPriceSaleDiscounted;

        public DiscountedProductViewHolder(@NonNull View itemView) {
            super(itemView);
            discountImageView = itemView.findViewById(R.id.productImageDiscount);
            discountName = itemView.findViewById(R.id.productNameDiscount);
            discountPrice=itemView.findViewById(R.id.productPriceDiscount);
            discountSalePrice=itemView.findViewById(R.id.productPriceSaleDiscount);
            unitMoneyPriceSaleDiscounted=itemView.findViewById(R.id.unitMoneyPriceSaleDiscounted);
            unitMoneyPriceDiscounted=itemView.findViewById(R.id.unitMoneyPriceDiscounted);
            discountPrice.setPaintFlags(discountPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            unitMoneyPriceSaleDiscounted.setPaintFlags(unitMoneyPriceSaleDiscounted.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
            unitMoneyPriceDiscounted.setPaintFlags(unitMoneyPriceDiscounted.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        }
    }
}