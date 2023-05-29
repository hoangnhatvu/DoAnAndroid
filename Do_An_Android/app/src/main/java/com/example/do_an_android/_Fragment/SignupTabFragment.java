package com.example.do_an_android._Fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.do_an_android.Model.Server;
import com.example.do_an_android.Model.Support;
import com.example.do_an_android.R;
import com.example.do_an_android.Retrofit2.APIUtils;
import com.example.do_an_android.Retrofit2.DataClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;


public class SignupTabFragment extends Fragment implements View.OnClickListener {
    EditText username_sigup, password_signup, password_confirm_signup, name_signup, address_signup, phone_signup;
    Button btnSignup;
    Context context;
    ImageView imageSignup;
    String realPath = "";
    String image="";
    private static final int REQUEST_CODE_IMAGE_SIGNUP = 764;

    public SignupTabFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControl(view);
        btnSignup.setOnClickListener(this);
        imageSignup.setOnClickListener(this);
    }


    private boolean validateEditText(EditText txt, String description) {

        if (txt.getText().toString().trim().length() == 0) {
            Toast.makeText(context, description, Toast.LENGTH_SHORT).show();
            txt.requestFocus();
            return true;
        }
        return false;
    }

    private void setControl(View view) {
        username_sigup = view.findViewById(R.id.username_sigup);
        password_signup = view.findViewById(R.id.password_signup);
        password_confirm_signup = view.findViewById(R.id.password_confirm_signup);
        name_signup = view.findViewById(R.id.name_signup);
        address_signup = view.findViewById(R.id.address_signup);
        phone_signup = view.findViewById(R.id.phone_signup);
        btnSignup = view.findViewById(R.id.btnSignup);
        imageSignup = view.findViewById(R.id.imageSignup);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnSignup:
                if (!(validateEditText(username_sigup, "Tài khoản không được rỗng.")
                        || validateEditText(password_signup, "Mật khẩu không được rỗng.")
                        || validateEditText(name_signup, "Tên khách hàng không được rỗng.")
                        || validateEditText(address_signup, "Địa chỉ không được rỗng.")
                        || validateEditText(phone_signup, "Số điện thoại không được rỗng.")
                )) {
                    if (password_signup.getText().toString().equals(password_confirm_signup.getText().toString()))
                        Sigup();
                    else {
                        Toast.makeText(context, "Nhập lại mật khẩu không trùng nhau.", Toast.LENGTH_SHORT).show();
                        password_confirm_signup.requestFocus();
                    }
                }
                break;
            case R.id.imageSignup:
                if(context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                    //xin quyen
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},111);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CODE_IMAGE_SIGNUP);
                }

                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==111 &&grantResults[0]== PackageManager.PERMISSION_GRANTED)
        {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_IMAGE_SIGNUP);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_IMAGE_SIGNUP && resultCode == ((Activity) context).RESULT_OK && data != null) {
            Uri uri = data.getData();
            realPath = getRealPathFromURI(uri);
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageSignup.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void _Clear() {
        username_sigup.setText("");
        password_signup.setText("");
        name_signup.setText("");
        address_signup.setText("");
        phone_signup.setText("");
        password_confirm_signup.setText("");
        username_sigup.requestFocus();
        imageSignup.setImageResource(R.drawable.no_image);
    }

    private void Sigup() {
        File file = new File(realPath);
        String file_path = file.getAbsolutePath();
        try {
            String[] arrayNameFile = file_path.split("\\.");
            file_path = arrayNameFile[0] + System.currentTimeMillis() + "." + arrayNameFile[1];
        }catch (Exception ex){

        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", file_path, requestBody);
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<String> callback = dataClient.UploadImage(body);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if(response!=null)
                {
                    image =response.body();
                   insertCustomer();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                insertCustomer();

            }
        });

    }

    private void insertCustomer() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlSignup, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.toString().equals("0"))
                    Toast.makeText(context, "Trùng tên tài khoản.", Toast.LENGTH_SHORT).show();
                else if (response.toString().equals("-1"))
                    Toast.makeText(context, "Có lỗi xảy ra.", Toast.LENGTH_SHORT).show();
                else {
                    _Clear();
                    Toast.makeText(context, "Đăng kí thành công.", Toast.LENGTH_SHORT).show();

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
                params.put("username", username_sigup.getText().toString());
                params.put("name", name_signup.getText().toString());
                params.put("password", Support.EndcodeMD5(password_signup.getText().toString()));
                params.put("address", address_signup.getText().toString());
                params.put("phone", phone_signup.getText().toString());
                params.put("image", image);
                image="";
                return params;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }
}