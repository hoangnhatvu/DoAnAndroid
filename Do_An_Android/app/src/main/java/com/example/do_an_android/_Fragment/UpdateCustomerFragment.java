package com.example.do_an_android._Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.Window;
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
import com.example.do_an_android.Activity.MainActivity;
import com.example.do_an_android.Model.Server;
import com.example.do_an_android.Model.Support;
import com.example.do_an_android.R;
import com.example.do_an_android.Retrofit2.APIUtils;
import com.example.do_an_android.Retrofit2.DataClient;
import com.squareup.picasso.Picasso;

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


public class UpdateCustomerFragment extends Fragment implements View.OnClickListener {

    Context context;
    ImageView imageCustomer;
    TextView textCustomer, logout;
    EditText name_updateCustomer, address_updateCustomer, phone_updateCustomer, passOld, passNew, passNewConfirm;
    Button btnUpdateCustomer, btnChangepass, btnConfirmChangpass, btnCancelChangpass;
    SharedPreferences sharedPreferencesUser;
    String username;
    Dialog dialog;
    String image = "";
    String realPath = "";
    ImageView imageUpdateCustomer;
    private static final int REQUEST_CODE_IMAGE_UPDATE = 714;

    public UpdateCustomerFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_customer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControl(view);
        getDataCustomer();
        setClick();
    }

    private void setClick() {
        logout.setOnClickListener(this);
        btnUpdateCustomer.setOnClickListener(this);
        btnChangepass.setOnClickListener(this);
        imageUpdateCustomer.setOnClickListener(this);
    }

    private void getDataCustomer() {
        sharedPreferencesUser = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        username = sharedPreferencesUser.getString("username", "");
        textCustomer.setText(username);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Server.urlGetCustomerByUsername + "?username=" + username, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    image = response.getString("image");
                    Picasso.get().load(Server.urlImage + image).into(imageCustomer);
                    name_updateCustomer.setText(response.getString("name"));
                    address_updateCustomer.setText(response.getString("address"));
                    phone_updateCustomer.setText(0 + response.getString("phone"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

    private void setControl(View view) {
        imageCustomer = view.findViewById(R.id.imageCustomer);
        textCustomer = view.findViewById(R.id.textCustomer);
        logout = view.findViewById(R.id.logout);
        name_updateCustomer = view.findViewById(R.id.name_updateCustomer);
        address_updateCustomer = view.findViewById(R.id.address_updateCustomer);
        phone_updateCustomer = view.findViewById(R.id.phone_updateCustomer);
        btnUpdateCustomer = view.findViewById(R.id.btnUpdateCustomer);
        btnChangepass = view.findViewById(R.id.btnChangepass);
        imageUpdateCustomer = view.findViewById(R.id.imageUpdateCustomer);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.logout:
                sharedPreferencesUser.edit().clear().commit();
                startActivity(new Intent(context, MainActivity.class));
                break;
            case R.id.btnUpdateCustomer:
                updateInfoCustomer();
                break;
            case R.id.btnChangepass:
                openDialogChangPass();
                break;
            case R.id.btnCancelChangpass:
                dialog.dismiss();
                break;
            case R.id.btnConfirmChangpass:
                if (validateEditText(passOld, "Mật khẩu cũ không được rỗng.")
                        || validateEditText(passNew, "Mật khẩu mới không được rông."))
                    break;
                if (!passNew.getText().toString().equals(passNewConfirm.getText().toString()))
                    Toast.makeText(context, "Nhập lại mật khẩu không trùng nhau.", Toast.LENGTH_SHORT).show();
                else
                    changePass();
                break;
            case R.id.imageUpdateCustomer:
                if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //xin quyen
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 111);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CODE_IMAGE_UPDATE);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_IMAGE_UPDATE);
        }
    }

    private void changePass() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlChangePassCustomer, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.toString().equals("-1"))
                    Toast.makeText(context, "Mật khẩu cũ không chính xác.", Toast.LENGTH_SHORT).show();
                else if (response.toString().equals("0"))
                    Toast.makeText(context, "Có lỗi xảy ra.", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(context, "Đổi mật khẩu thành công.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    sharedPreferencesUser.edit().clear().commit();
                    Intent intent=new Intent(context, MainActivity.class);
                    intent.putExtra("checkChangpass",true);
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
                params.put("username", username);
                params.put("pass_old", Support.EndcodeMD5(passOld.getText().toString()));
                params.put("pass_new", Support.EndcodeMD5(passNew.getText().toString()));
                return params;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);
    }

    private void openDialogChangPass() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.diaglog_change_pass);
        dialog.setCanceledOnTouchOutside(false);
        //ánh xạ
        passOld = dialog.findViewById(R.id.passOld);
        passNew = dialog.findViewById(R.id.passNew);
        passNewConfirm = dialog.findViewById(R.id.passNewConfirm);
        btnConfirmChangpass = dialog.findViewById(R.id.btnConfirmChangpass);
        btnCancelChangpass = dialog.findViewById(R.id.btnCancelChangpass);
        btnConfirmChangpass.setOnClickListener(this);
        btnCancelChangpass.setOnClickListener(this);
        dialog.show();
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_IMAGE_UPDATE && resultCode == ((Activity) context).RESULT_OK && data != null) {
            Uri uri = data.getData();
            realPath = getRealPathFromURI(uri);
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageUpdateCustomer.setImageBitmap(bitmap);
                imageCustomer.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

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

    private void updateInfoCustomer() {
        if (validateEditText(name_updateCustomer, "Tên khách hàng không được rỗng.")
                || validateEditText(address_updateCustomer, "Địa chỉ không được rỗng.")
                || validateEditText(phone_updateCustomer, "Số điện thoại không được rỗng."))
            return;
        File file = new File(realPath);
        String file_path = file.getAbsolutePath();
        try {
            String[] arrayNameFile = file_path.split("\\.");
            file_path = arrayNameFile[0] + System.currentTimeMillis() + "." + arrayNameFile[1];
        } catch (Exception ex) {

        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", file_path, requestBody);
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<String> callback = dataClient.UploadImage(body);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response != null) {
                    image = response.body();
                    updateTextCustomer();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                updateTextCustomer();
            }
        });
    }

    private void updateTextCustomer() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlUpdateCustomer, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                imageUpdateCustomer.setImageResource(R.drawable.no_image);
                if (response.toString().equals("1"))

                    Toast.makeText(context, "Cập nhật thành công.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, "Có lỗi xảy ra.", Toast.LENGTH_SHORT).show();

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
                params.put("name", name_updateCustomer.getText().toString());
                params.put("address", address_updateCustomer.getText().toString());
                params.put("phone", phone_updateCustomer.getText().toString());
                params.put("image", image);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);
    }
}