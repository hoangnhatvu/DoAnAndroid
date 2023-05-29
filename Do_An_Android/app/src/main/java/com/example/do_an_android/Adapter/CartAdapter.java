package com.example.do_an_android.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.do_an_android.Activity.MainActivity;
import com.example.do_an_android.Model.CartModel;
import com.example.do_an_android.Model.ProductModel;
import com.example.do_an_android.Model.Server;
import com.example.do_an_android.Model.Support;
import com.example.do_an_android.R;
import com.example.do_an_android._Fragment.CartFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    Context context;
    int layout;
    ArrayList<CartModel> lstCart;
    public CartAdapter(Context context, int layout, ArrayList<CartModel> lstCart) {
        this.context = context;
        this.layout = layout;
        this.lstCart = lstCart;


    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(context).inflate(layout, null));
    }
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CartModel cart = lstCart.get(position);

        long price = 0;
        if (cart.getProductModel().getPrice_discounted() > 0)
            price = cart.getProductModel().getPrice_discounted();
        else
            price = cart.getProductModel().getPrice();
        Picasso.get().load(Server.urlImage + cart.getProductModel().getImage()).into(holder.image);
        holder.quantity.setText(cart.getQuantity() + "");
        holder.subtotal.setText(Support.ConvertMoney(cart.getQuantity() * price));
        holder.price.setText(Support.ConvertMoney(price));
        holder.name.setText(cart.getProductModel().getName());
        long finalPrice = price;
        holder.quantity.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() != KeyEvent.ACTION_DOWN)
                    return true;
                if (i == KeyEvent.KEYCODE_ENTER) {
                    if(holder.quantity.getText().toString().equals(""))
                        return false;
                    int quantity = Integer.parseInt(holder.quantity.getText().toString());
                    if (quantity == 0) {
                        lstCart.remove(lstCart.get(position));
                        CartFragment.updateCart(lstCart);
                        notifyDataSetChanged();
                    } else {
                        int quantityRemain=cart.getQuantityRemain()+cart.getQuantity();
                        if(quantity>quantityRemain)
                        {
                            Toast.makeText(context,"Không đủ hàng.",Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        holder.subtotal.setText(Support.ConvertMoney(quantity * finalPrice));
                        lstCart.get(position).setQuantity(quantity);
                        lstCart.get(position).setQuantityRemain(quantityRemain-quantity);
                        CartFragment.updateCart(lstCart);
                    }
                }
                return false;
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                confirmDetele(position);
                return true;
            }
        });


    }

    private void confirmDetele(int position) {
        CartModel cartModel = lstCart.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông báo!");
        builder.setMessage("Bạn có muốn xoá sản phẩm " + cartModel.getProductModel().getName() + "?");
        builder.setIcon(R.drawable.icon);
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteCartModel(position);
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    private void deleteCartModel(int position) {
        lstCart.remove(position);
        CartFragment.updateCart(lstCart);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return lstCart.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, price, subtotal,unitMoneyItemCartPrice,unitMoneyItemCartSubtotal;
        EditText quantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_detail_cart);
            name = itemView.findViewById(R.id.name_detail_cart);
            price = itemView.findViewById(R.id.price_deatail_cart);
            subtotal = itemView.findViewById(R.id.subtotal_detail_cart);
            quantity = itemView.findViewById(R.id.quantity_detail_cart);
            unitMoneyItemCartPrice = itemView.findViewById(R.id.unitMoneyItemCartPrice);
            unitMoneyItemCartSubtotal = itemView.findViewById(R.id.unitMoneyItemCartSubtotal);
            unitMoneyItemCartPrice.setPaintFlags(unitMoneyItemCartPrice.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
            unitMoneyItemCartSubtotal.setPaintFlags(unitMoneyItemCartSubtotal.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        }
    }
}
