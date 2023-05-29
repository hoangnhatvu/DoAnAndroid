package com.example.do_an_android.Adapter;

import android.annotation.SuppressLint;
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

public class RecentlyAdapter extends RecyclerView.Adapter<RecentlyAdapter.RecentlyView> {

    Context context;
    int layout;
    ArrayList<ProductModel> recentlyProductsList;

    public RecentlyAdapter(Context context, int layout, ArrayList<ProductModel> recentlyProductsList) {
        this.context = context;
        this.layout = layout;
        this.recentlyProductsList = recentlyProductsList;
    }

    @NonNull
    @Override
    public RecentlyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layout, parent, false);

        return new RecentlyView(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull RecentlyView holder, int position) {

        ProductModel productModel=recentlyProductsList.get(position);
        Log.d("haha", "onBindViewHolder: "+productModel.getImage());
        Picasso.get().load(Server.urlImage+productModel.getImage()).into(holder.recentlyImageView);
        holder.recentlyName.setText(productModel.getName());
        if(productModel.getPrice_discounted()>0) {
            holder.productPriceSaleRecently.setText(Support.ConvertMoney(productModel.getPrice_discounted()));
            holder.productPriceRecently.setText(Support.ConvertMoney(productModel.getPrice()));
            holder.unitPriceRecently.setText("đ");

        }else
        {
            holder.productPriceSaleRecently.setText(Support.ConvertMoney(productModel.getPrice()));
            holder.productPriceRecently.setText("");
            holder.unitPriceRecently.setText("");
        }
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
        return recentlyProductsList.size();
    }

    public  static class RecentlyView extends RecyclerView.ViewHolder{

        ImageView recentlyImageView;
        TextView recentlyName,productPriceSaleRecently,productPriceRecently,unitPriceRecently,unitPriceSaleRecently;

        public RecentlyView(@NonNull View itemView) {
            super(itemView);
            recentlyImageView = itemView.findViewById(R.id.productImageRecently);
            recentlyName = itemView.findViewById(R.id.productNameRecently);
            productPriceSaleRecently=itemView.findViewById(R.id.productPriceSaleRecently);
            unitPriceRecently=itemView.findViewById(R.id.unitPriceRecently);
            unitPriceSaleRecently=itemView.findViewById(R.id.unitPriceSaleRecently);
            productPriceRecently=itemView.findViewById(R.id.productPriceRecently);
            unitPriceRecently.setPaintFlags(unitPriceRecently.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
            unitPriceSaleRecently.setPaintFlags(unitPriceSaleRecently.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
            productPriceRecently.setPaintFlags(productPriceRecently.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

}
