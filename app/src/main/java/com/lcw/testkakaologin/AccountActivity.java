package com.lcw.testkakaologin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.io.InputStream;

public class AccountActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 0;
    ImageView iv_01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        iv_01=findViewById(R.id.iv_01);
        iv_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                try{
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                   // iv_01.setImageBitmap(img);
                    Uri uri= data.getData();
                    iv_01.setImageURI(uri);

                }catch(Exception e)
                {

                }
            }
            else if(resultCode == RESULT_CANCELED)
            {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
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
    }
}
