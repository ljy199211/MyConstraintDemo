package com.example.ljy.myconstraintdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import widget.AddressPickerDatas;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private AddressPickerDatas addressPickerDatas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_choice_address).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        addressPickerDatas = new AddressPickerDatas();
        addressPickerDatas.selectAddressDialog(this);
        addressPickerDatas.setAddressSelectedListener(new AddressPickerDatas.AddressSelectedListener() {
            @Override
            public void onAddressSelectedListener(String address, String proviceCode, String cityCode, String districtCode) {

            }
        });
    }
}
