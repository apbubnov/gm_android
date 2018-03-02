package ru.ejevikaapp.gm_android.Crew;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ru.ejevikaapp.gm_android.MainActivity;
import ru.ejevikaapp.gm_android.R;
import ru.ejevikaapp.gm_android.Service_Sync;
import ru.ejevikaapp.gm_android.Service_Sync_Import;

public class Activity_crew extends AppCompatActivity implements View.OnClickListener {

    Button calendar_work, exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crew);
        calendar_work = (Button)findViewById(R.id.calendar_work);
        calendar_work.setOnClickListener(this);

        exit = (Button)findViewById(R.id.exit);
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.calendar_work:
                Intent intent = new Intent(this, Activity_calendar.class);
                startActivity(intent);
                break;
            case R.id.exit:

                stopService(new Intent(this, Service_Sync.class));
                stopService(new Intent(this, Service_Sync_Import.class));

                SharedPreferences SP = getSharedPreferences("dealer_id", MODE_PRIVATE);
                SharedPreferences.Editor ed = SP.edit();
                ed.putString("", "");
                ed.commit();

                SP = getSharedPreferences("user_id", MODE_PRIVATE);
                ed = SP.edit();
                ed.putString("", "");
                ed.commit();

                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
