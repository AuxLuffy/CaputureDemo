package com.example.sunzh.caputuredemo.passwordinput;

import android.app.Activity;
import android.os.Bundle;

import com.example.sunzh.caputuredemo.R;

public class PayPsdInputViewAcitvity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_psd_input_view_acitvity);

        PayPsdInputView payPsdInputView = (PayPsdInputView) findViewById(R.id.psw);
        payPsdInputView.requestFocus();
    }
}
