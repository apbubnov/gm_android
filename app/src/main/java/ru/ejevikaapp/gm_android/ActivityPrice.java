package ru.ejevikaapp.gm_android;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import ru.ejevikaapp.gm_android.Dealer.Activity_price_canvases;
import ru.ejevikaapp.gm_android.Dealer.Activity_price_components;
import ru.ejevikaapp.gm_android.Dealer.Activity_price_mounting;

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

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.price_canvases:
                Intent intent = new Intent(this, Activity_price_canvases.class);
                startActivity(intent);
                break;
            case R.id.price_comp:
                intent = new Intent(this, Activity_price_components.class);
                startActivity(intent);
                break;
            case R.id.price_mounting:
                intent = new Intent(this, Activity_price_mounting.class);
                startActivity(intent);
                break;
        }
    }

}