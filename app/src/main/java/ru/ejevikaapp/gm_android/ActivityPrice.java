package ru.ejevikaapp.gm_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityPrice extends AppCompatActivity implements View.OnClickListener {

    Button price_canvases, price_comp, price_mounting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price);

        price_canvases = (Button)findViewById(R.id.price_canvases);
        price_comp = (Button)findViewById(R.id.price_comp);
        price_mounting = (Button)findViewById(R.id.price_mounting);

        price_canvases.setOnClickListener(this);
        price_comp.setOnClickListener(this);
        price_mounting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.price_canvases:
                Intent intent = new Intent(this, Activity_zamer.class);
                startActivity(intent);
                break;
            case R.id.price_comp:
                intent = new Intent(this, Activity_zamer.class);
                startActivity(intent);
                break;
            case R.id.price_mounting:
                intent = new Intent(this, Activity_zamer.class);
                startActivity(intent);
                break;
        }
    }

}