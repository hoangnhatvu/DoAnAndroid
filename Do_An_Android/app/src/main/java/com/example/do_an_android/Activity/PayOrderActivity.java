package com.example.do_an_android.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.do_an_android.Model.CartModel;
import com.example.do_an_android.Model.CustomerModel;
import com.example.do_an_android.Model.Server;
import com.example.do_an_android.Model.Support;
import com.example.do_an_android.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PayOrderActivity extends AppCompatActivity {
    EditText name_order, address_order, phone_order;
    TextView sumProductConfirm, totalConfirm,unitMoneyPayOrderTotal;
    Button btnConfirmOrder;
    SharedPreferences sharedPreferencesCart, sharedPreferencesUser;
    ArrayList<CartModel> lstCart;
    String username;
    ImageView backPayOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_order);
        setControl();
        getDataCustomer();
        getDataCart();
        setDataCart();
        setClick();
    }

    private void setDataCart() {
        int sum = 0;
        long total = 0;
        for (CartModel cartModel : lstCart) {
            sum += cartModel.getQuantity();
            if (cartModel.getProductModel().getPrice_discounted() > 0)
                total += cartModel.getQuantity() * cartModel.getProductModel().getPrice_discounted();
            else
                total += cartModel.getQuantity() * cartModel.getProductModel().getPrice();
        }
        sumProductConfirm.setText(sum + "");
        totalConfirm.setText(Support.ConvertMoney(total));
    }

    private void getDataCart() {
        sharedPreferencesCart = getSharedPreferences("cart", Context.MODE_PRIVATE);
        String cart = sharedPreferencesCart.getString("item_cart", "");
        CartModel[] cartModels = new Gson().fromJson(cart, CartModel[].class);
        lstCart = new ArrayList<CartModel>(Arrays.asList(cartModels));
    }

    private void getDataCustomer() {
        sharedPreferencesUser = getSharedPreferences("user", Context.MODE_PRIVATE);
        username = sharedPreferencesUser.getString("username", "");
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Server.urlGetCustomerByUsername + "?username=" + username, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    name_order.setText(response.getString("name"));
                    address_order.setText(response.getString("address"));
                    phone_order.setText(0 + response.getString("phone"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }

    private void setClick() {
        btnConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertOrder();
            }
        });
        backPayOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("checkBuyNow",true);
                startActivity(intent);
                finish();
            }
        });
    }

    private void updateOrder(String code_order) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlUpdateOrder, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                long total = 0;
                for (CartModel cartModel : lstCart)
                    if (cartModel.getProductModel().getPrice_discounted() > 0)
                        total += cartModel.getQuantity() * cartModel.getProductModel().getPrice_discounted();
                    else
                        total += cartModel.getQuantity() * cartModel.getProductModel().getPrice();
                params.put("total", total + "");
                params.put("code_order", code_order);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void OrderDetail(String code_order) {
        for (CartModel cartModel : lstCart) {
            insertOrderDetail(code_order, cartModel);
        }
    }

    private void insertOrderDetail(String code_order, CartModel cartModel) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlInsertOrderDetail, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                long price=0;
                if(cartModel.getProductModel().getPrice_discounted()>0)
                    price=cartModel.getProductModel().getPrice_discounted();
                else
                    price=cartModel.getProductModel().getPrice();
                params.put("code_order", code_order);
                params.put("code_product", cartModel.getProductModel().getCode());
                params.put("price", price + "");
                params.put("quantity", cartModel.getQuantity() + "");
                params.put("total", price * cartModel.getQuantity() + "");
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void insertOrder() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlInsertOrder, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String code_order = response.toString();
                if (!code_order.equals("0")) {
                    OrderDetail(code_order);
                    updateOrder(code_order);
                    Toast.makeText(getApplicationContext(), "Đặt hàng thành công.", Toast.LENGTH_SHORT).show();
                    getSharedPreferences("cart", Context.MODE_PRIVATE)
                            .edit().clear().commit();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else
                    Toast.makeText(getApplicationContext(), "Đặt hàng thất bại.", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void setControl() {
        name_order = findViewById(R.id.name_order);
        address_order = findViewById(R.id.address_order);
        phone_order = findViewById(R.id.phone_order);
        sumProductConfirm = findViewById(R.id.sumProductConfirm);
        totalConfirm = findViewById(R.id.totalConfirm);
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);
        backPayOrder = findViewById(R.id.backPayOrder);
        unitMoneyPayOrderTotal = findViewById(R.id.unitMoneyPayOrderTotal);
        unitMoneyPayOrderTotal.setPaintFlags(unitMoneyPayOrderTotal.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

    }
}