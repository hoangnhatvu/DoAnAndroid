package com.example.do_an_android._Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.do_an_android.Activity.MainActivity;
import com.example.do_an_android.Model.Server;
import com.example.do_an_android.Model.Support;
import com.example.do_an_android.R;

import java.util.HashMap;
import java.util.Map;


public class LoginTabFragment extends Fragment implements View.OnClickListener {

    Context context;
    EditText username_login, password_login;
    CheckBox ckbRemember;
    Button btnLogin;
    SharedPreferences sharedPreferencesRemember;
    SharedPreferences sharedPreferencesUser;

    Boolean checkPayOrder = false;

    public LoginTabFragment(Context context, Boolean checkPayOrder) {
        this.context = context;
        this.checkPayOrder = checkPayOrder;

    }

    public LoginTabFragment(Context context) {
        this.context = context;

    }

    private boolean validateEditText(EditText txt, String description) {

        if (txt.getText().toString().trim().length() == 0) {
            Toast.makeText(context, description, Toast.LENGTH_SHORT).show();
            txt.requestFocus();
            return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControl(view);
        checkRemember();
        sharedPreferencesUser = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        btnLogin.setOnClickListener(this);

    }

    private void checkRemember() {
        sharedPreferencesRemember = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        //load thông tin đã lưu lên textbox
        boolean isRemember = sharedPreferencesRemember.getBoolean("isRemember", false);
        if (isRemember) {
            username_login.setText(sharedPreferencesRemember.getString("username", ""));
            password_login.setText(sharedPreferencesRemember.getString("password", ""));
        }
        ckbRemember.setChecked(isRemember);
    }

    private void setControl(View view) {
        username_login = view.findViewById(R.id.username_login);
        password_login = view.findViewById(R.id.password_login);
        ckbRemember = view.findViewById(R.id.ckbRemember);
        btnLogin = view.findViewById(R.id.btnLogin);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnLogin:
                if (!(validateEditText(username_login, "Tài khoản không được rỗng.")
                        || validateEditText(password_login, "Mật khẩu không được rỗng."))) {
                    Login();
                }
                break;
        }
    }

    private void Login() {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlLogin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.toString().equals("fail"))
                    Toast.makeText(context, "Sai tài khoản hoặc mật khẩu.", Toast.LENGTH_SHORT).show();
                else {
                    SharedPreferences.Editor editorRemember = sharedPreferencesRemember.edit();
                    SharedPreferences.Editor editorUser = sharedPreferencesUser.edit();
                    editorUser.putString("username", username_login.getText().toString());
                    editorUser.commit();
                    if (ckbRemember.isChecked()) {
                        editorRemember.putString("username", username_login.getText().toString());
                        editorRemember.putString("password", password_login.getText().toString());
                    }
                    editorRemember.putBoolean("isRemember", ckbRemember.isChecked());
                    editorRemember.commit();
                    Intent intent=new Intent(context, MainActivity.class);
                    if(checkPayOrder)
                       intent.putExtra("checkBuyNow",true);
                    startActivity(intent);
                }
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
                params.put("username", username_login.getText().toString());
                params.put("password", Support.EndcodeMD5(password_login.getText().toString()));
                return params;
            }
        };
        queue.add(stringRequest);
    }
}