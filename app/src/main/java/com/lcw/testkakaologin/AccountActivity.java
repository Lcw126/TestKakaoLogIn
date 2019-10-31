package com.lcw.testkakaologin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.io.InputStream;

public class AccountActivity extends AppCompatActivity {
    private static final int IMG_REQUEST_CODE = 0;
    private static final int STORAGE_REQUEST_CODE = 1;
    ImageView iv_01;
    EditText etName, etMsg;

    String imgPath;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        etName=findViewById(R.id.et_NickName);
        etMsg=findViewById(R.id.et_Msg);

        iv_01=findViewById(R.id.iv_01);
        iv_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(intent, IMG_REQUEST_CODE);
            }
        });

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            int permissionResult= checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(permissionResult== PackageManager.PERMISSION_DENIED){
                String[] permissions= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions,STORAGE_REQUEST_CODE);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case IMG_REQUEST_CODE:
                if(resultCode == RESULT_OK)
                {
                    try{
                        InputStream in = getContentResolver().openInputStream(data.getData());
                        Bitmap img = BitmapFactory.decodeStream(in);
                        in.close();
                        // iv_01.setImageBitmap(img);
                        Uri uri= data.getData();
                        imgPath=getRealPathFromUri(uri);
                        iv_01.setImageURI(uri);

                    }catch(Exception e)
                    {

                    }

                }
                else if(resultCode == RESULT_CANCELED)
                {
                    Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }//onActivityResult() ..

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case STORAGE_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "외부 메모리 읽기/쓰기 사용 가능", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "외부 메모리 읽기/쓰기 제한", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    public void clickLogOut(View view) {
        onClickLogout();
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(this, "계정 로그아웃", Toast.LENGTH_SHORT).show();
        finish();
    }


    private void onClickLogout() {
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                //redirectLoginActivity();
            }
        });
    }

    public void clickFinish(View view) {
        String name=etName.getText().toString();
        String msg=etMsg.getText().toString();

        String serverUrl="http://umul.dothome.co.kr/KakaoLog/insertDB.php";

        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl,new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
               // new AlertDialog.Builder(AccountActivity.this).setMessage("응답:"+response).create().show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AccountActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });


        if(name==null || msg==null || imgPath==null || MainActivity.kakaoIDNUM+""==null) {
            Toast.makeText(this, "정보가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        smpr.addStringParam("name",name);
        smpr.addStringParam("msg",msg);
        smpr.addFile("img",imgPath);
        smpr.addStringParam("id",MainActivity.kakaoIDNUM+"");

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(smpr);

        Intent intent=new Intent(AccountActivity.this, Main2Activity.class);
        startActivity(intent);
        finish();


    }//clickFinish()..

    //Uri -- > 절대경로로 바꿔서 리턴시켜주는 메소드
    String getRealPathFromUri(Uri uri){
        String[] proj= {MediaStore.Images.Media.DATA};
        CursorLoader loader= new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor= loader.loadInBackground();
        int column_index= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result= cursor.getString(column_index);
        cursor.close();
        return  result;
    }
}
