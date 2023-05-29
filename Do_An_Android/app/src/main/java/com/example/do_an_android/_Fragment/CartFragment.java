package com.example.do_an_android._Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.do_an_android.Activity.MainActivity;
import com.example.do_an_android.Activity.PayOrderActivity;
import com.example.do_an_android.Activity.ProductDetailActivity;
import com.example.do_an_android.Adapter.CartAdapter;
import com.example.do_an_android.Model.CartModel;
import com.example.do_an_android.Model.GridSpacingItemDecoration;
import com.example.do_an_android.Model.Support;
import com.example.do_an_android.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;


public class CartFragment extends Fragment implements View.OnClickListener {
    Context context;
    static ArrayList<CartModel> lstCart = null;
    RecyclerView recyclerView;
    static TextView txttotalquantity_cart, txttotalpay_cart;
    ImageView backCart;
    Button btnPay;
    TextView unitMoneyCartTotal;
    static SharedPreferences sharedPreferencesUser, sharedPreferencesCart;

    public CartFragment(Context context) {
        this.context = context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferencesUser = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        sharedPreferencesCart = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
        String cart = sharedPreferencesCart.getString("item_cart", "");

        CartModel[] cartModels = new Gson().fromJson(cart, CartModel[].class);
        if (cartModels != null)
            lstCart = new ArrayList<CartModel>(Arrays.asList(cartModels));
        else
            lstCart = new ArrayList<>();
        setControl(view);
        setData();
        backCart.setOnClickListener(this);
        btnPay.setOnClickListener(this);

    }


    private void setData() {
        int sumQuantity = 0;
        long total = 0;
        for (CartModel itemCart : lstCart) {
            sumQuantity += itemCart.getQuantity();
            if (itemCart.getProductModel().getPrice_discounted() > 0)
                total += itemCart.getQuantity() * itemCart.getProductModel().getPrice_discounted();
            else
                total += itemCart.getQuantity() * itemCart.getProductModel().getPrice();
        }
        txttotalquantity_cart.setText(sumQuantity + "");
        txttotalpay_cart.setText(Support.ConvertMoney(total));

        CartAdapter cartAdapter = new CartAdapter(getContext(), R.layout.item_cart, lstCart);
        recyclerView.setAdapter(cartAdapter);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1,dpToPx(2),false));
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));

    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    public static void updateCart(ArrayList<CartModel> cartList) {
        lstCart = cartList;
        int sum = 0;
        int total = 0;
        for (CartModel item : lstCart) {
            sum += item.getQuantity();
            if (item.getProductModel().getPrice_discounted() > 0)
                total += item.getQuantity() * item.getProductModel().getPrice_discounted();
            else
                total += item.getQuantity() * item.getProductModel().getPrice();
        }
        txttotalquantity_cart.setText(sum + "");

        txttotalpay_cart.setText(Support.ConvertMoney(total));
        SharedPreferences.Editor editorCart = sharedPreferencesCart.edit();
        editorCart.putString("item_cart", new Gson().toJson(lstCart));
        editorCart.commit();
    }

    private void setControl(View view) {
        recyclerView = view.findViewById(R.id.recycleviewcart);
        txttotalquantity_cart = view.findViewById(R.id.txttotalquantity_cart);
        txttotalpay_cart = view.findViewById(R.id.txttotalpay_cart);
        backCart = view.findViewById(R.id.backCart);
        btnPay = view.findViewById(R.id.btnPay);
        unitMoneyCartTotal = view.findViewById(R.id.unitMoneyCartTotal);
        unitMoneyCartTotal.setPaintFlags(unitMoneyCartTotal.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.backCart: {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.btnPay:
                if (lstCart.size() == 0) {
                    Toast.makeText(context, "Giỏ hàng chưa có gì.", Toast.LENGTH_SHORT).show();

                } else {
                    String username = sharedPreferencesUser.getString("username", "fail");
                    if (username.equals("fail")) {
                        Toast.makeText(context, "Chưa đăng nhập.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("checkPayCart", true);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, PayOrderActivity.class);
                        startActivity(intent);
                    }
                }
                break;
        }
    }
}