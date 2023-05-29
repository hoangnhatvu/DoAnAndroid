package com.example.do_an_android.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.do_an_android.Adapter.ProductOfTypeAdapter;
import com.example.do_an_android.Model.CategoryModel;
import com.example.do_an_android.Model.GridSpacingItemDecoration;
import com.example.do_an_android.Model.ProductModel;
import com.example.do_an_android.Model.Server;
import com.example.do_an_android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductsOfTypeActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recycleviewProductsOfType;
    ProductOfTypeAdapter productOfTypeAdapter;
    ArrayList<ProductModel> productModelArrayList;
    ImageView backProductOfType;
    TextView txtTileProductsOfType;
    private CategoryModel categoryModel;
    private boolean check;
    private String query="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_of_type);
        setControl();

        Intent intent = getIntent();
        check = intent.getBooleanExtra("check", true);

        if (check) {
            categoryModel = (CategoryModel) intent.getSerializableExtra("typeProduct");
            txtTileProductsOfType.setText("Danh sách sản phẩm của loại " + categoryModel.getName());
        } else {
            query = intent.getStringExtra("query");
            txtTileProductsOfType.setText("Tìm kiếm theo " + query);
        }
        setRecyclerAdapter();
        loadDataProductOfType();
        backProductOfType.setOnClickListener(this);


    }

    private void loadDataProductOfType() {
        String url;
        if (check)
            url = Server.urlProductsOfType + "?type_code=" + categoryModel.getCode();
        else
            url = Server.urlSearch + "?keyword=" + query;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        productModelArrayList.add(
                                new ProductModel(
                                        jsonObject.getString("code"),
                                        jsonObject.getString("name"),
                                        jsonObject.getLong("price"),
                                        jsonObject.getInt("quantity"),
                                        jsonObject.getLong("price_discounted"),
                                        jsonObject.getString("description"),
                                        jsonObject.getString("image"),
                                        jsonObject.getString("date_update"),
                                        jsonObject.getString("type_code")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                productOfTypeAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonArrayRequest);
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    private void setRecyclerAdapter() {

        productModelArrayList = new ArrayList<>();
        productOfTypeAdapter = new ProductOfTypeAdapter(this, R.layout.item_product_of_type, productModelArrayList);
        recycleviewProductsOfType.addItemDecoration(new GridSpacingItemDecoration(2,dpToPx(2),false));
        recycleviewProductsOfType.setAdapter(productOfTypeAdapter);
        recycleviewProductsOfType.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void setControl() {
        recycleviewProductsOfType = findViewById(R.id.recycleviewProductsOfType);
        backProductOfType = findViewById(R.id.backProductOfType);
        txtTileProductsOfType = findViewById(R.id.txtTileProductsOfType);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.backProductOfType:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}