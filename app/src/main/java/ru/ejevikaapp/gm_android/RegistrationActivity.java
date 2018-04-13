package ru.ejevikaapp.gm_android;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.ejevikaapp.gm_android.Class.HelperClass;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    EditText phone, mail, name, city;
    Button register;
    DBHelper dbHelper;
    static RequestQueue requestQueue;
    static String data = "";
    static String domen = "calc";
    static JSONObject jsonData = new JSONObject();

    private static final String TAG = "responceUsers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        phone = (EditText) findViewById(R.id.phone);
        mail = (EditText) findViewById(R.id.mail);
        name = (EditText) findViewById(R.id.name);
        city = (EditText) findViewById(R.id.city);

        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

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
            case R.id.register:
                String email = mail.getText().toString();
                String number = phone.getText().toString();
                String city_user = city.getText().toString();
                String name_user = name.getText().toString();

                //SharedPreferences SP = getSharedPreferences("link", MODE_PRIVATE);
                //SharedPreferences.Editor ed = SP.edit();
                //ed.putString("", "test1");
                //ed.commit();

                if (number.length()>9 && number.length()<13 && validateMail(email)) {
                    String id_user = number.substring(1, number.length() - 3);
                    if (HelperClass.isOnline(this)) {
                        try {
                            jsonData.put("android_id", id_user);
                            jsonData.put("email", email);
                            jsonData.put("city", city_user);
                            jsonData.put("name", name_user);
                            jsonData.put("phone", number);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        data = String.valueOf(jsonData);

                        Log.d(TAG, "послал " + data);

                        new SendUsers().execute();
                    } else
                        Toast.makeText(getApplicationContext(), "проверьте подключение к интернету", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(getApplicationContext(), "проверьте введенные данные", Toast.LENGTH_LONG).show();
                break;
        }
    }

    class SendUsers extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.register_from_android";
        Map<String, String> parameters = new HashMap<String, String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(final Void... params) {

            StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String res) {

                    Log.d(TAG, "пришло "  + res);

                    if (res.length() == 130){
                        res = "Такой номер занят";
                    } else if (res.length() == 168){
                        res = "Такая почта занята";
                    } else {
                        res = "спасибо за регистрацию";
                        finish();
                    }

                    final Toast toast = Toast.makeText(getApplicationContext(),
                            res, Toast.LENGTH_LONG);
                    toast.show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("r_data", data);
                    Log.d(TAG, String.valueOf(parameters));
                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }
    }


    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateMail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

}