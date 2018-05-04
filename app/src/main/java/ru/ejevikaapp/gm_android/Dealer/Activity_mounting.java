package ru.ejevikaapp.gm_android.Dealer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.ejevikaapp.gm_android.DBHelper;
import ru.ejevikaapp.gm_android.R;

public class Activity_mounting extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mounting);

        Button btn_brigade = (Button) findViewById(R.id.btn_brigade);
        btn_brigade.setOnClickListener(this);

        setTitle("Монтажи");

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
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_brigade:
                Intent intent = new Intent(this, Activity_spisok_brigade.class);
                startActivity(intent);
                break;
        }
    }
}
