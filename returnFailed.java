package com.deshpande.camerademo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class returnFailed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_failed);
    }

    public void sendMessage(View view)
    {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
