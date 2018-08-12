package com.example.kankan.adviewkankan;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        txt=findViewById(R.id.txtSplash);
        Typeface font=Typeface.createFromAsset(getAssets(),"MetalMacabre.ttf");
        txt.setTypeface(font);

        Handler handler=new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this,LogInActivity.class));
                finish();
            }
        },5000);
    }
}
