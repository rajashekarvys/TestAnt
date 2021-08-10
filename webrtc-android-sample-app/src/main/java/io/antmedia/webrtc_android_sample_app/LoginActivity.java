package io.antmedia.webrtc_android_sample_app;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.runtimepermissions.PermissionCallBack;
import com.runtimepermissions.PermissionsManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LoginActivity extends AppCompatActivity  {
    private List<String> permissionsGrantee;
    private TextView txtName,txtRoomId;
    private Button btnConference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtName = findViewById(R.id.name);
        txtRoomId = findViewById(R.id.roomId);
        btnConference = findViewById(R.id.btnConference);

        checkPermission();

        btnConference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity();
            }
        });

    }



    private void checkPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int camera = checkSelfPermission(Manifest.permission.CAMERA);
            int audioSettings = checkSelfPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS);
            int recordAudio = checkSelfPermission(Manifest.permission.RECORD_AUDIO);

            permissionsGrantee = new ArrayList<>();



            if (audioSettings != PackageManager.PERMISSION_GRANTED) {
                permissionsGrantee.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);
            }
            if (recordAudio != PackageManager.PERMISSION_GRANTED) {
                permissionsGrantee.add(Manifest.permission.RECORD_AUDIO);
            }

            if (camera != PackageManager.PERMISSION_GRANTED) {
                permissionsGrantee.add(Manifest.permission.CAMERA);
            }

            if (!permissionsGrantee.isEmpty()) {
                requestPermissions(permissionsGrantee.toArray(new String[permissionsGrantee.size()]), 11);
            }
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 11) {
            checkPermissionAfterResult(permissions, grantResults);

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void checkPermissionAfterResult(String[] permissions, int[] grantResults) {
        int isAllGranteed = 0;
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                isAllGranteed++;
            } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                isAllGranteed--;
            }
        }
        if (isAllGranteed == permissionsGrantee.size()) {

        } else {
            Toast.makeText(this, getResources().getString(R.string.enable_all_permission), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    void finishActivity(){
        String streamId =txtName.getText().toString().trim();
        String roomId =txtRoomId.getText().toString().trim();
        String randomNum = String.valueOf((new Random().nextInt((999 - 100) + 1) + 100));
        if (streamId.length()>0 && roomId.length()>0) {
            Intent intent = new Intent(this, ConferenceActivity.class);
            intent.putExtra("streamId",streamId + randomNum);
            intent.putExtra("roomId",  roomId);
            startActivity(intent);
        }

    }
}