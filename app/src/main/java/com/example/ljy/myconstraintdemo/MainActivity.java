package com.example.ljy.myconstraintdemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import widget.AddressPickerDatas;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private AddressPickerDatas addressPickerDatas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_choice_address).setOnClickListener(this);
        findViewById(R.id.btn_jump_sys).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_choice_address:
                addressPickerDatas = new AddressPickerDatas();
                addressPickerDatas.selectAddressDialog(this);
                addressPickerDatas.setAddressSelectedListener(new AddressPickerDatas.AddressSelectedListener() {
                    @Override
                    public void onAddressSelectedListener(String address, String proviceCode, String cityCode, String districtCode) {

                    }
                });
                break;
            case R.id.btn_jump_sys:
                if (NotificationsUtils.isNotificationEnabled(this)){
                    Toast.makeText(this, "权限开了", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(this, "权限关了", Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(this).setMessage("建议您开启系统通知权限，" +
                            "实时接收最新消息及时掌握优惠信息。")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    jumpSystemSetting();
                                }
                            }).create().show();
                }
                break;
                default:break;
        }

    }

    private void jumpSystemSetting() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 9){
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package",MainActivity.this.getPackageName(),null));
        }else if (Build.VERSION.SDK_INT <= 8){
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName",MainActivity.this.getPackageName());
        }
        startActivity(intent);
    }
}
