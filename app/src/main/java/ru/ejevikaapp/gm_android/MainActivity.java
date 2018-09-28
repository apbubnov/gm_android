package ru.ejevikaapp.gm_android;

import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.tasks.Task;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import ru.ejevikaapp.gm_android.Crew.Activity_crew;
import ru.ejevikaapp.gm_android.Dealer.Dealer_office;

public class MainActivity extends AppCompatActivity implements OnClickListener, TextView.OnEditorActionListener,
        GoogleApiClient.OnConnectionFailedListener {

    EditText login, password;
    Button btn_vhod;
    TextView registration;
    String TAG = "responce";

    DBHelper dbHelper;
    SQLiteDatabase db;

    SharedPreferences SP_end;

    public static ProgressDialog mProgressDialog;

    Map<String, String> parameters = new HashMap<String, String>();
    static RequestQueue requestQueue;
    StringRequest request = null;

    org.json.simple.JSONObject jsonObjectAuth = new org.json.simple.JSONObject();
    org.json.simple.JSONObject jsonMaterial = new org.json.simple.JSONObject();
    org.json.simple.JSONObject jsonMounters = new org.json.simple.JSONObject();
    org.json.simple.JSONObject jsonDealer = new org.json.simple.JSONObject();
    String jsonAuth = "", material = "", mounters = "", dealer = "", yourDealer = "";
    int usergroup = 0;
    String domen;

    private static final int SIGNED_IN = 0;
    private static final int STATE_SIGNING_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;
    private static final int RC_SIGN_IN = 0;
    private GoogleApiClient mGoogleApiClient;
    private int mSignInProgress;
    private PendingIntent mSignInIntent;
    private SignInButton mSignInButton;
    private Button mSignOutButton;
    private Button mRevokeButton;
    private TextView mStatus;
    private ConnectionResult mConnectionResult;
    private static final int OUR_REQUEST_CODE = 49404;

    GoogleSignInOptions GoogleSignInOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignOutButton = (Button) findViewById(R.id.sign_out_button);
        //mRevokeButton = (Button) findViewById(R.id.revoke_access_button);
        mStatus = (TextView) findViewById(R.id.statuslabel);
//
        //// Add click listeners for the buttons
        mSignInButton.setOnClickListener(this);
        mSignOutButton.setOnClickListener(this);
        //mRevokeButton.setOnClickListener(this);
//
        //// Build a GoogleApiClient
        //mGoogleApiClient = buildGoogleApiClient();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        /*
        String offline = "0";
        try {
            offline = getIntent().getExtras().get("offline").toString();
        } catch (Exception e) {
        }

        SP_end = this.getSharedPreferences("user_id", MODE_PRIVATE);
        String user_id = SP_end.getString("", "");

        SP_end = this.getSharedPreferences("version", MODE_PRIVATE);
        String version = SP_end.getString("", "");

        if (version.equals("offline")) {
            offlineVersion();
        } else if (version.equals("online")){
            Intent intent = new Intent(MainActivity.this, ActivityOnlineVersion.class);
            startActivity(intent);
            finish();
        } else {
            if (user_id.equals("")) {
                if (offline.equals("1")) {
                    offlineVersion();
                } else {
                    String[] array = {"Online версия(Требуется интернет)", "Offline версия(Требуется интернет при авторизации)",
                    "Регистрация"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setItems(array, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            // TODO Auto-generated method stub
                            switch (item) {
                                case 0:
                                    Intent intent = new Intent(MainActivity.this, ActivityOnlineVersion.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case 1:
                                    offlineVersion();
                                    break;
                                case 2:
                                    intent = new Intent(MainActivity.this, ActivityOnlineVersion.class);
                                    startActivity(intent);
                                    break;
                            }
                        }
                    });

                    builder.setCancelable(false);
                    builder.create();
                    builder.show();
                }
            } else {
                offlineVersion();
            }
        }
        */

    }

    @Override
    protected void onResume() {
        super.onResume();

        String offline = "0";
        try {
            offline = getIntent().getExtras().get("offline").toString();
        } catch (Exception e) {
        }

        SP_end = this.getSharedPreferences("user_id", MODE_PRIVATE);
        String user_id = SP_end.getString("", "");

        SP_end = this.getSharedPreferences("version", MODE_PRIVATE);
        String version = SP_end.getString("", "");

        if (version.equals("offline") && !user_id.equals("")) {
            offlineVersion();
        } else if (version.equals("online")) {
            Intent intent = new Intent(MainActivity.this, ActivityOnlineVersion.class);
            startActivity(intent);
            finish();
        } else {
            if (user_id.equals("")) {
                if (offline.equals("1")) {
                    offlineVersion();
                } else {

                    String[] array = {"Online версия(Требуется интернет)", "Offline версия(Требуется интернет при авторизации)"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setItems(array, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            // TODO Auto-generated method stub
                            switch (item) {
                                case 0:
                                    Intent intent = new Intent(MainActivity.this, ActivityOnlineVersion.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case 1:
                                    offlineVersion();
                                    break;
                                //case 2:
                                //    intent = new Intent(MainActivity.this, RegistrationActivity.class);
                                //    startActivity(intent);
                                //    break;
                            }
                        }
                    });

                    builder.setCancelable(false);
                    builder.create();
                    builder.show();
                }
            } else {
                offlineVersion();
            }
        }

    }

    void offlineVersion() {

        dbHelper = new DBHelper(MainActivity.this);
        db = dbHelper.getReadableDatabase();
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        SP_end = this.getSharedPreferences("user_id", MODE_PRIVATE);
        String user_id = SP_end.getString("", "");
        ArrayList group_id = new ArrayList();
        String sqlQuewy = "SELECT group_id "
                + "FROM rgzbn_user_usergroup_map" +
                " WHERE user_id = ?";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    group_id.add(c.getString(c.getColumnIndex(c.getColumnName(0))));
                } while (c.moveToNext());
            }
        }
        c.close();

        int count = 0;
        sqlQuewy = "SELECT * FROM rgzbn_users";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    count++;
                } while (c.moveToNext());
            }
        }
        c.close();

        db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS, "_id = ? ", new String[]{"1"});

        if (user_id.equals("")) {
        } else
            for (int g = 0; group_id.size() > g; g++) {
                if (group_id.get(g).equals("11")) {

                    Timer myTimer = new Timer();
                    final Handler uiHandler = new Handler();
                    myTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    startService(new Intent(MainActivity.this, Service_Sync_Import.class));
                                }
                            });
                        }
                    }, 0L, 60L * 1000);

                    Send_All.Alarm.setAlarm(MainActivity.this);
                    startService(new Intent(MainActivity.this, Send_All.class));

                    Intent intent = new Intent(MainActivity.this, Activity_crew.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                    break;

                } else if (group_id.get(g).equals("21") || group_id.get(g).equals("22")) {

                    Timer myTimer = new Timer();
                    final Handler uiHandler = new Handler();
                    myTimer.schedule(new TimerTask() { // Определяем задачу
                        @Override
                        public void run() {
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    startService(new Intent(MainActivity.this, Service_Sync_Import.class));
                                }
                            });
                        }
                    }, 0L, 60L * 1000);

                    Send_All.Alarm.setAlarm(MainActivity.this);
                    startService(new Intent(MainActivity.this, Send_All.class));

                    Intent intent = new Intent(MainActivity.this, Gager_office.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                    break;
                } else if (group_id.get(g).equals("14")) {

                    Timer myTimer = new Timer();
                    final Handler uiHandler = new Handler();
                    myTimer.schedule(new TimerTask() { // Определяем задачу
                        @Override
                        public void run() {
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    startService(new Intent(MainActivity.this, Service_Sync_Import.class));
                                }
                            });
                        }
                    }, 0L, 60L * 1000);

                    Send_All.Alarm.setAlarm(MainActivity.this);
                    startService(new Intent(MainActivity.this, Send_All.class));

                    Intent intent = new Intent(MainActivity.this, Dealer_office.class);
                    startActivity(intent);
                    MainActivity.this.finish();

                    break;
                }
            }

        registration = (TextView) findViewById(R.id.registration);
        registration.setOnClickListener(this);
        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        password.setOnEditorActionListener(this);

        btn_vhod = (Button) findViewById(R.id.btn_vhod);
        btn_vhod.setOnClickListener(this);


        dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

    }

    /*
    private GoogleApiClient buildGoogleApiClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();

        return new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    protected void onStart() {
        super.onStart();
        //mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        //mSignInButton.setEnabled(false);
        mSignOutButton.setEnabled(true);
        mRevokeButton.setEnabled(true);

        // Indicate that the sign in process is complete.
        mSignInProgress = SIGNED_IN;
        OptionalPendingResult opr =
                Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);

        opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
            @Override
            public void onResult(@NonNull GoogleSignInResult result) {
                if (result.isSuccess()) {
                    try {
                        GoogleSignInAccount account = result.getSignInAccount();
                        mStatus.setText(String.format("Signed In to My App as %s", account.getEmail()));
                    } catch (Exception ex) {
                        String exception = ex.getLocalizedMessage();
                        String exceptionString = ex.toString();
                        // Note that you should log these errors in a ‘real' app to aid in debugging
                    }
                }
            }
        });

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mSignInProgress != STATE_IN_PROGRESS) {
            mSignInIntent = result.getResolution();
            if (mSignInProgress == STATE_SIGNING_IN) {
                resolveSignInError();
            }
        }
        // Will implement shortly
        onSignedOut();
    }

    private void resolveSignInError() {
        if (mSignInIntent != null) {
            try {
                mSignInProgress = STATE_IN_PROGRESS;
                mConnectionResult.startResolutionForResult(this, OUR_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                mSignInProgress = STATE_SIGNING_IN;
                mGoogleApiClient.connect();
            }
        } else {
            // You have a play services error -- inform the user
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC_SIGN_IN:
                if (resultCode == RESULT_OK) {
                    mSignInProgress = STATE_SIGNING_IN;
                } else {
                    mSignInProgress = SIGNED_IN;
                }

                if (!mGoogleApiClient.isConnecting()) {
                    mGoogleApiClient.connect();
                }
                break;
        }
    }

    private void onSignedOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.d("mLog", String.valueOf(status));
                        // Update the UI to reflect that the user is signed out.
                        mSignInButton.setEnabled(true);
                        mSignOutButton.setEnabled(false);
                        mRevokeButton.setEnabled(false);
                        mStatus.setText("Signed out");
                    }
                });


    }
*/
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            vhod();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_vhod:
                vhod();
                break;
            case R.id.registration:
                intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
                break;
            case R.id.sign_in_button:
                //mStatus.setText("Signing In");
                //Intent signInIntent =
                //        Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                //startActivityForResult(signInIntent, RC_SIGN_IN);

                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);

                break;
            case R.id.sign_out_button:
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                updateUI(false);
                            }
                        });
                break;
                /*
            case R.id.revoke_access_button:
                Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient);
                mGoogleApiClient = buildGoogleApiClient();
                mGoogleApiClient.connect();
                break;
                */
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            mSignInButton.setVisibility(View.GONE);
            mSignOutButton.setVisibility(View.VISIBLE);
        } else {
            mStatus.setText("out");
            //Bitmap icon =                  BitmapFactory.decodeResource(getContext().getResources(),R.drawable.user_defaolt);
            //imgProfilePic.setImageBitmap(ImageHelper.getRoundedCornerBitmap(getContext(),icon, 200, 200, 200, false, false, false, false));
            mSignInButton.setVisibility(View.VISIBLE);
            mSignOutButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.getSignInAccount());
        if (result.isSuccess()) {
            // Signed in successfolly, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d(TAG, String.valueOf(acct.getAccount()));
            Log.d(TAG, String.valueOf(acct.getDisplayName()));
            Log.d(TAG, String.valueOf(acct.getFamilyName()));
            Log.d(TAG, String.valueOf(acct.getGivenName()));
            Log.d(TAG, String.valueOf(acct.getGrantedScopes()));
            mStatus.setText(acct.getEmail());
            //Similarly you can get the email and photourl using acct.getEmail() and  acct.getPhotoUrl()

            //if(acct.getPhotoUrl() != noll)
            //    new LoadProfileImage(imgProfilePic).execute(acct.getPhotoUrl().toString());

            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {

            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {

            //showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    //hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    void vhod() {
        if (login.getText().toString().equals("") || password.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Введите данные", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            jsonObjectAuth.put("username", login.getText().toString());
            jsonObjectAuth.put("password", password.getText().toString());
            jsonAuth = String.valueOf(jsonObjectAuth);

            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setMessage("Проверяем...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();

            SharedPreferences SP = getSharedPreferences("link", MODE_PRIVATE);
            SharedPreferences.Editor ed = SP.edit();
            ed.putString("", "calc");
            ed.commit();

            domen = "calc";

            new SendAuthorization().execute();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    class SendAuthorization extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&task=api.Authorization_FromAndroid";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(final Void... params) {

            //try {
            request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String res) {

                    Log.d("responce", res);
                    String user_id = "";
                    ArrayList group_id = new ArrayList();

                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        user_id = jsonObject.getString("id");

                        String ob = jsonObject.getString("groups");

                        String sqlQuewy = "SELECT group_id "
                                + "FROM rgzbn_user_usergroup_map" +
                                " WHERE user_id = ?";

                        Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id});

                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    db.delete(DBHelper.TABLE_RGZBN_USER_USERGROUP_MAP, "user_id = ?", new String[]{String.valueOf(user_id)});
                                } while (c.moveToNext());
                            }
                        }
                        c.close();

                        int i = 0;
                        for (String retval : ob.split(",")) {
                            i++;
                            int indexJava = retval.indexOf(":");
                            if (indexJava == -1) {
                            } else {
                                for (String retval1 : retval.split(":")) {

                                    retval1 = retval1.replaceAll("[^0-9]", "");

                                    sqlQuewy = "SELECT * "
                                            + "FROM rgzbn_user_usergroup_map" +
                                            " WHERE user_id = ? and group_id = ? ";

                                    c = db.rawQuery(sqlQuewy, new String[]{user_id, retval1});

                                    if (c != null) {
                                        if (c.moveToFirst()) {
                                            do {
                                                // ничего не делаем
                                            } while (c.moveToNext());
                                        } else {
                                            ContentValues values = new ContentValues();
                                            values.put(DBHelper.KEY_USER_ID, user_id);
                                            values.put(DBHelper.KEY_GROUP_ID, retval1);
                                            db.insert(DBHelper.TABLE_RGZBN_USER_USERGROUP_MAP, null, values);
                                        }
                                    }
                                    c.close();

                                }
                                continue;
                            }
                        }

                        sqlQuewy = "SELECT group_id "
                                + "FROM rgzbn_user_usergroup_map" +
                                " WHERE user_id = ?";

                        c = db.rawQuery(sqlQuewy, new String[]{user_id});
                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    group_id.add(c.getString(c.getColumnIndex(c.getColumnName(0))));

                                } while (c.moveToNext());
                            }
                        }
                        c.close();

                        final boolean[] bool = {false};
                        for (int g = 0; group_id.size() > g; g++) {
                            if (group_id.get(g).equals("25")) {    // программисты
                                try {

                                    bool[0] = true;
                                    String[] array = {"test1", "calc"};

                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                                    final String finalRes = res;
                                    final String finalUser_id = user_id;
                                    final String finalOb = ob;
                                    builder.setItems(array, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int item) {
                                            // TODO Auto-generated method stub
                                            switch (item) {
                                                case 0:
                                                    SharedPreferences SP = getSharedPreferences("link", MODE_PRIVATE);
                                                    SharedPreferences.Editor ed = SP.edit();
                                                    ed.putString("", "test1");
                                                    ed.commit();
                                                    domen = "test1";
                                                    send(finalRes, finalUser_id, finalOb);
                                                    break;
                                                case 1:
                                                    SP = getSharedPreferences("link", MODE_PRIVATE);
                                                    ed = SP.edit();
                                                    ed.putString("", "calc");
                                                    ed.commit();
                                                    domen = "calc";
                                                    send(finalRes, finalUser_id, finalOb);
                                                    break;
                                            }
                                        }
                                    });

                                    builder.setCancelable(false);
                                    builder.create();
                                    builder.show();

                                } catch (Exception e) {
                                }

                            }
                        }

                        if (!bool[0]) {
                            send(res, user_id, ob);
                        }

                        Log.d("responce", user_id);
                    } catch (Exception e) {

                        mProgressDialog.dismiss();
                        Toast toast = Toast.makeText(getApplicationContext(),
                                res, Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mProgressDialog.dismiss();
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Проверьте подключение к интернету, или возможны работы на сервере", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("authorizations", jsonAuth);
                    return parameters;
                }
            };

            request.setShouldCache(false);
            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(request);

            return null;
        }
    }

    void send(String res, String user_id, String ob) {

        SharedPreferences SP = getSharedPreferences("version", MODE_PRIVATE);
        SharedPreferences.Editor ed = SP.edit();
        ed.putString("", "offline");
        ed.commit();

        ArrayList group_id = new ArrayList();

        ContentValues values = new ContentValues();
        String sqlQuewy = "SELECT change_time "
                + "FROM history_import_to_server" +
                " WHERE title = ?";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{"material"});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    //values.put(DBHelper.KEY_CHANGE_TIME, "0000-00-00 00:00:00");
                    //db.update(DBHelper.HISTORY_IMPORT_TO_SERVER, values, "title = ?", new String[]{"material"});
                } while (c.moveToNext());
            } else {
                values = new ContentValues();
                values.put(DBHelper.KEY_CHANGE_TIME, "0000-00-00 00:00:00");
                values.put(DBHelper.KEY_TITLE, "material");
                db.insert(DBHelper.HISTORY_IMPORT_TO_SERVER, null, values);
            }
        }

        c.close();

        values = new ContentValues();
        sqlQuewy = "SELECT change_time "
                + "FROM history_import_to_server" +
                " WHERE title = ?";
        c = db.rawQuery(sqlQuewy, new String[]{"mount"});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    values.put(DBHelper.KEY_CHANGE_TIME, "0000-00-00 00:00:00");
                    db.update(DBHelper.HISTORY_IMPORT_TO_SERVER, values, "title = ?", new String[]{"mount"});
                } while (c.moveToNext());
            } else {
                values = new ContentValues();
                values.put(DBHelper.KEY_CHANGE_TIME, "0000-00-00 00:00:00");
                values.put(DBHelper.KEY_TITLE, "mount");
                db.insert(DBHelper.HISTORY_IMPORT_TO_SERVER, null, values);
            }
        }

        c.close();

        values = new ContentValues();
        sqlQuewy = "SELECT change_time "
                + "FROM history_import_to_server" +
                " WHERE title = ?";
        c = db.rawQuery(sqlQuewy, new String[]{"dealer"});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    values.put(DBHelper.KEY_CHANGE_TIME, "0000-00-00 00:00:00");
                    db.update(DBHelper.HISTORY_IMPORT_TO_SERVER, values, "title = ?", new String[]{"dealer"});
                } while (c.moveToNext());
            } else {
                values = new ContentValues();
                values.put(DBHelper.KEY_CHANGE_TIME, "0000-00-00 00:00:00");
                values.put(DBHelper.KEY_TITLE, "dealer");
                db.insert(DBHelper.HISTORY_IMPORT_TO_SERVER, null, values);
            }
        }

        c.close();

        sqlQuewy = "SELECT group_id "
                + "FROM rgzbn_user_usergroup_map" +
                " WHERE user_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{user_id});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    db.delete(DBHelper.TABLE_RGZBN_USER_USERGROUP_MAP, "user_id = ?", new String[]{String.valueOf(user_id)});
                } while (c.moveToNext());
            }
        }
        c.close();

        int i = 0;
        for (String retval : ob.split(",")) {
            i++;
            int indexJava = retval.indexOf(":");
            if (indexJava == -1) {
            } else {
                for (String retval1 : retval.split(":")) {
                    retval1 = retval1.replaceAll("[^0-9]", "");
                    group_id.add(retval1);
                }
                continue;
            }
        }

        sqlQuewy = "SELECT group_id "
                + "FROM rgzbn_user_usergroup_map" +
                " WHERE user_id = ?";

        c = db.rawQuery(sqlQuewy, new String[]{user_id});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    group_id.add(c.getString(c.getColumnIndex(c.getColumnName(0))));

                } while (c.moveToNext());
            }
        }
        c.close();

        boolean bool = true;
        for (int g = 0; group_id.size() > g; g++) {
            if (group_id.get(g).equals("11")) { // монтажная бригада
                usergroup = 11;
                SP = getSharedPreferences("user_id", MODE_PRIVATE);
                ed = SP.edit();
                ed.putString("", user_id);
                ed.commit();

                String dealer_id = "";
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(res);
                    dealer_id = jsonObject.getString("dealer_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SP = getSharedPreferences("dealer_id", MODE_PRIVATE);
                ed = SP.edit();
                ed.putString("", dealer_id);
                ed.commit();

                String time = "";

                sqlQuewy = "SELECT change_time "
                        + "FROM history_import_to_server" +
                        " WHERE user_id = ?";

                c = db.rawQuery(sqlQuewy, new String[]{user_id});

                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            time = c.getString(c.getColumnIndex(c.getColumnName(0)));

                            values = new ContentValues();
                            values.put(DBHelper.KEY_CHANGE_TIME, "0000-00-00 00:00:00");
                            db.update(DBHelper.HISTORY_IMPORT_TO_SERVER, values, "user_id = ?", new String[]{user_id});

                        } while (c.moveToNext());
                    }
                }
                c.close();

                if (time.equals("")) {
                    time = "2017-01-01 00:00:00";
                    values = new ContentValues();
                    values.put(DBHelper.KEY_USER_ID, user_id);
                    values.put(DBHelper.KEY_CHANGE_TIME, time);
                    db.insert(DBHelper.HISTORY_IMPORT_TO_SERVER, null, values);
                }


                Thread t = new Thread(new Runnable() {
                    public void run() {
                        new Send_Material().execute();
                        new Send_Mounters().execute();
                        new Send_Dealer().execute();
                    }
                });
                t.start();

                final Toast toast = Toast.makeText(getApplicationContext(),
                        "При первом запуске приложения возможны торможения или зависания, " +
                                "это происходит из-за проектов, полотен, производителей и т.д., которые скачиваются...", Toast.LENGTH_LONG);
                toast.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 10000);

                Intent intent = new Intent(MainActivity.this, Activity_crew.class);
                startActivity(intent);
                finish();

                bool = false;
                break;
            } else if (group_id.get(g).equals("22") || group_id.get(g).equals("21")) { // замерщик
                try {

                    values = new ContentValues();
                    values.put(DBHelper.KEY_USER_ID, user_id);
                    values.put(DBHelper.KEY_GROUP_ID, "21");
                    db.insert(DBHelper.TABLE_RGZBN_USER_USERGROUP_MAP, null, values);

                    usergroup = 22;
                    mProgressDialog.setMessage("Загружаем...");
                    JSONObject jsonObject = new JSONObject(res);
                    user_id = jsonObject.getString("id");

                    SP = getSharedPreferences("gager_id", MODE_PRIVATE);
                    ed = SP.edit();
                    ed.putString("", user_id);
                    ed.commit();

                    SP = getSharedPreferences("user_id", MODE_PRIVATE);
                    ed = SP.edit();
                    ed.putString("", user_id);
                    ed.commit();

                    String name = jsonObject.getString("name");

                    SP = getSharedPreferences("name_user", MODE_PRIVATE);
                    ed = SP.edit();
                    ed.putString("", name);
                    ed.commit();

                    String dealer_id = jsonObject.getString("dealer_id");

                    SP = getSharedPreferences("dealer_id", MODE_PRIVATE);
                    ed = SP.edit();
                    ed.putString("", dealer_id);
                    ed.commit();

                    String time = "";

                    sqlQuewy = "SELECT change_time "
                            + "FROM history_import_to_server" +
                            " WHERE user_id = ?";

                    c = db.rawQuery(sqlQuewy, new String[]{user_id});

                    if (c != null) {
                        if (c.moveToFirst()) {
                            do {
                                time = c.getString(c.getColumnIndex(c.getColumnName(0)));

                                values = new ContentValues();
                                values.put(DBHelper.KEY_CHANGE_TIME, "0000-00-00 00:00:00");
                                db.update(DBHelper.HISTORY_IMPORT_TO_SERVER, values, "user_id = ?", new String[]{user_id});
                            } while (c.moveToNext());
                        }
                    }
                    c.close();

                    if (time.equals("")) {
                        time = "2017-01-01 00:00:00";
                        values = new ContentValues();
                        values.put(DBHelper.KEY_USER_ID, user_id);
                        values.put(DBHelper.KEY_CHANGE_TIME, time);
                        db.insert(DBHelper.HISTORY_IMPORT_TO_SERVER, null, values);
                    }

                    String change_time = "0000-00-00 00:00:00";

                    sqlQuewy = "SELECT change_time "
                            + "FROM history_import_to_server" +
                            " WHERE title = ?";

                    c = db.rawQuery(sqlQuewy, new String[]{"material"});
                    if (c != null) {
                        if (c.moveToFirst()) {
                            do {
                                change_time = c.getString(c.getColumnIndex(c.getColumnName(0)));
                            } while (c.moveToNext());
                        }
                    }
                    c.close();

                    jsonMaterial.put("change_time", change_time);
                    material = String.valueOf(jsonMaterial);

                    sqlQuewy = "SELECT change_time "
                            + "FROM history_import_to_server" +
                            " WHERE title = ?";

                    c = db.rawQuery(sqlQuewy, new String[]{"mount"});
                    if (c != null) {
                        if (c.moveToFirst()) {
                            do {
                                change_time = c.getString(c.getColumnIndex(c.getColumnName(0)));
                            } while (c.moveToNext());
                        }
                    }
                    c.close();

                    jsonMounters.put("dealer_id", dealer_id);
                    jsonMounters.put("change_time", change_time);
                    mounters = String.valueOf(jsonMounters);

                    sqlQuewy = "SELECT change_time "
                            + "FROM history_import_to_server" +
                            " WHERE title = ?";

                    c = db.rawQuery(sqlQuewy, new String[]{"dealer"});
                    if (c != null) {
                        if (c.moveToFirst()) {
                            do {
                                change_time = c.getString(c.getColumnIndex(c.getColumnName(0)));
                            } while (c.moveToNext());
                        }
                    }
                    c.close();

                    jsonDealer.put("dealer_id", dealer_id);
                    jsonDealer.put("change_time", change_time);
                    dealer = String.valueOf(jsonDealer);

                    Thread t = new Thread(new Runnable() {
                        public void run() {
                            new Send_Material().execute();
                            new Send_Mounters().execute();
                            new Send_Dealer().execute();
                        }
                    });
                    t.start();

                    final Toast toast = Toast.makeText(getApplicationContext(),
                            "При первом запуске приложения возможны торможения или зависания, " +
                                    "это происходит из-за проектов, полотен, производителей и т.д., которые скачиваются...", Toast.LENGTH_LONG);
                    toast.show();

                } catch (Exception e) {
                }

                break;
            } else if (group_id.get(g).equals("14")) {    // дилер
                try {

                    values = new ContentValues();
                    values.put(DBHelper.KEY_USER_ID, user_id);
                    values.put(DBHelper.KEY_GROUP_ID, "14");
                    db.insert(DBHelper.TABLE_RGZBN_USER_USERGROUP_MAP, null, values);

                    usergroup = 14;
                    mProgressDialog.setMessage("Загружаем...");
                    JSONObject jsonObject = new JSONObject(res);
                    user_id = jsonObject.getString("id");

                    SP = getSharedPreferences("gager_id", MODE_PRIVATE);
                    ed = SP.edit();
                    ed.putString("", user_id);
                    ed.commit();

                    SP = getSharedPreferences("user_id", MODE_PRIVATE);
                    ed = SP.edit();
                    ed.putString("", user_id);
                    ed.commit();

                    String name = jsonObject.getString("name");

                    SP = getSharedPreferences("name_user", MODE_PRIVATE);
                    ed = SP.edit();
                    ed.putString("", name);
                    ed.commit();

                    final String dealer_id = jsonObject.getString("dealer_id");
                    yourDealer = dealer_id;

                    SP = getSharedPreferences("dealer_id", MODE_PRIVATE);
                    ed = SP.edit();
                    ed.putString("", dealer_id);
                    ed.commit();

                    String time = "";

                    sqlQuewy = "SELECT change_time "
                            + "FROM history_import_to_server" +
                            " WHERE user_id = ?";

                    c = db.rawQuery(sqlQuewy, new String[]{user_id});

                    if (c != null) {
                        if (c.moveToFirst()) {
                            do {
                                time = c.getString(c.getColumnIndex(c.getColumnName(0)));

                                values = new ContentValues();
                                values.put(DBHelper.KEY_CHANGE_TIME, "0000-00-00 00:00:00");
                                db.update(DBHelper.HISTORY_IMPORT_TO_SERVER, values, "user_id = ?", new String[]{user_id});
                            } while (c.moveToNext());
                        }
                    }
                    c.close();

                    if (time.equals("")) {
                        time = "2017-01-01 00:00:00";
                        values = new ContentValues();
                        values.put(DBHelper.KEY_USER_ID, user_id);
                        values.put(DBHelper.KEY_CHANGE_TIME, time);
                        db.insert(DBHelper.HISTORY_IMPORT_TO_SERVER, null, values);
                    }

                    String change_time = "0000-00-00 00:00:00";

                    sqlQuewy = "SELECT change_time "
                            + "FROM history_import_to_server" +
                            " WHERE title = ?";

                    c = db.rawQuery(sqlQuewy, new String[]{"material"});
                    if (c != null) {
                        if (c.moveToFirst()) {
                            do {
                                change_time = c.getString(c.getColumnIndex(c.getColumnName(0)));
                            } while (c.moveToNext());
                        }
                    }
                    c.close();
                    jsonMaterial.put("change_time", change_time);
                    material = String.valueOf(jsonMaterial);

                    sqlQuewy = "SELECT change_time "
                            + "FROM history_import_to_server" +
                            " WHERE title = ?";
                    c = db.rawQuery(sqlQuewy, new String[]{"mount"});
                    if (c != null) {
                        if (c.moveToFirst()) {
                            do {
                                change_time = c.getString(c.getColumnIndex(c.getColumnName(0)));
                            } while (c.moveToNext());
                        }
                    }
                    c.close();
                    jsonMounters.put("dealer_id", dealer_id);
                    jsonMounters.put("change_time", change_time);
                    mounters = String.valueOf(jsonMounters);

                    sqlQuewy = "SELECT change_time "
                            + "FROM history_import_to_server" +
                            " WHERE title = ?";
                    c = db.rawQuery(sqlQuewy, new String[]{"dealer"});
                    if (c != null) {
                        if (c.moveToFirst()) {
                            do {
                                change_time = c.getString(c.getColumnIndex(c.getColumnName(0)));
                            } while (c.moveToNext());
                        }
                    }
                    c.close();
                    jsonDealer.put("dealer_id", dealer_id);
                    jsonDealer.put("change_time", change_time);
                    dealer = String.valueOf(jsonDealer);

                    Thread t = new Thread(new Runnable() {
                        public void run() {
                            new Send_Material().execute();
                            new Send_Mounters().execute();
                            new Send_Dealer().execute();
                        }
                    });
                    t.start();

                    final Toast toast = Toast.makeText(getApplicationContext(),
                            "При первом запуске приложения возможны торможения или зависания, " +
                                    "это происходит из-за проектов, полотен, производителей и т.д., которые скачиваются...", Toast.LENGTH_LONG);
                    toast.show();

                    break;
                } catch (Exception e) {
                }
            }
        }
    }

    class Send_Material extends AsyncTask<Integer, String, String> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&task=api.sendMaterialToAndroid";
        Map<String, String> parameters = new HashMap<String, String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(final Integer... integers) {

            StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String res) {

                    Log.d(TAG, "Send_Material MAINACTIVITY " + res);

                    SQLiteDatabase db;
                    db = dbHelper.getWritableDatabase();
                    ContentValues values;

                    int count_m;
                    try {
                        org.json.JSONObject dat = new org.json.JSONObject(res);

                        JSONArray id_array = dat.getJSONArray("rgzbn_gm_ceiling_textures");
                        Log.d(TAG, "rgzbn_gm_ceiling_textures " + id_array);
                        for (int i = 0; i < id_array.length(); i++) {

                            count_m = 0;
                            org.json.JSONObject text = id_array.getJSONObject(i);

                            String id = text.getString("id");
                            String texture_title = text.getString("texture_title");
                            String texture_colored = text.getString("texture_colored");

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID, id);
                            values.put(DBHelper.KEY_TEXTURE_TITLE, texture_title);
                            values.put(DBHelper.KEY_TEXTURE_COLORED, texture_colored);

                            String sqlQuewy = "SELECT * "
                                    + "FROM rgzbn_gm_ceiling_textures" +
                                    " WHERE _id = ?";
                            Cursor c = db.rawQuery(sqlQuewy, new String[]{id});
                            if (c != null) {
                                if (c.moveToFirst()) {
                                    do {
                                        Log.d("mLog", "upd " + String.valueOf(values));
                                        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_TEXTURES, values, "_id = ?", new String[]{id});
                                        count_m++;
                                    } while (c.moveToNext());
                                }
                            }
                            c.close();

                            if (count_m == 0) {
                                try {
                                    Log.d("mLog", "ins " + String.valueOf(values));
                                    values.put(DBHelper.KEY_ID, id);
                                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_TEXTURES, null, values);

                                } catch (Exception e) {
                                    Log.d("responce", String.valueOf(e));
                                }
                            }
                        }

                        id_array = dat.getJSONArray("rgzbn_gm_ceiling_canvases");
                        Log.d("mLog", "rgzbn_gm_ceiling_canvases " + id_array);
                        for (int i = 0; i < id_array.length(); i++) {

                            count_m = 0;
                            org.json.JSONObject canv = id_array.getJSONObject(i);

                            String id = canv.getString("id");
                            String texture_id = canv.getString("texture_id");
                            String color_id = canv.getString("color_id");
                            String manufacturer_id = canv.getString("manufacturer_id");
                            String width = canv.getString("width");
                            String price = canv.getString("price");
                            String count = canv.getString("count");

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID, id);
                            values.put(DBHelper.KEY_TEXTURE_ID, texture_id);
                            values.put(DBHelper.KEY_COLOR_ID, color_id);
                            values.put(DBHelper.KEY_MANUFACTURER_ID, manufacturer_id);
                            values.put(DBHelper.KEY_WIDTH, width);
                            values.put(DBHelper.KEY_PRICE, price);
                            values.put(DBHelper.KEY_COUNT, count);

                            String sqlQuewy = "SELECT * "
                                    + "FROM rgzbn_gm_ceiling_canvases" +
                                    " WHERE _id = ?";
                            Cursor c = db.rawQuery(sqlQuewy, new String[]{id});
                            if (c != null) {
                                if (c.moveToFirst()) {
                                    do {
                                        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CANVASES, values, "_id = ?", new String[]{id});
                                        count_m++;
                                    } while (c.moveToNext());
                                }
                            }
                            c.close();

                            if (count_m == 0) {
                                try {
                                    values.put(DBHelper.KEY_ID, id);
                                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CANVASES, null, values);
                                } catch (Exception e) {
                                }
                            }

                        }

                        id_array = dat.getJSONArray("rgzbn_gm_ceiling_canvases_manufacturers");
                        for (int i = 0; i < id_array.length(); i++) {

                            count_m = 0;
                            org.json.JSONObject canv = id_array.getJSONObject(i);

                            String id = canv.getString("id");
                            String name = canv.getString("name");
                            String country = canv.getString("country");

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID, id);
                            values.put(DBHelper.KEY_NAME, name);
                            values.put(DBHelper.KEY_COUNTRY, country);

                            String sqlQuewy = "SELECT * "
                                    + "FROM rgzbn_gm_ceiling_canvases_manufacturers" +
                                    " WHERE _id = ?";
                            Cursor c = db.rawQuery(sqlQuewy, new String[]{id});
                            if (c != null) {
                                if (c.moveToFirst()) {
                                    do {
                                        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CANVASES_MANUFACTURERS, values, "_id = ?", new String[]{id});
                                        count_m++;
                                    } while (c.moveToNext());
                                }
                            }

                            c.close();

                            if (count_m == 0) {
                                try {
                                    values.put(DBHelper.KEY_ID, id);
                                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CANVASES_MANUFACTURERS, null, values);
                                } catch (Exception e) {
                                    Log.d("responce", String.valueOf(e));
                                }
                            }

                        }

                        id_array = dat.getJSONArray("rgzbn_gm_ceiling_colors");
                        Log.d("mLog", "rgzbn_gm_ceiling_colors " + id_array);
                        for (int i = 0; i < id_array.length(); i++) {

                            count_m = 0;
                            org.json.JSONObject color = id_array.getJSONObject(i);

                            String id = color.getString("id");
                            String title = color.getString("title");
                            String hex = color.getString("hex");

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID, id);
                            values.put(DBHelper.KEY_TITLE, title);
                            values.put(DBHelper.KEY_HEX, hex);

                            String sqlQuewy = "SELECT * "
                                    + "FROM rgzbn_gm_ceiling_colors" +
                                    " WHERE _id = ?";
                            Cursor c = db.rawQuery(sqlQuewy, new String[]{id});
                            if (c != null) {
                                if (c.moveToFirst()) {
                                    do {
                                        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_COLORS, values, "_id = ?", new String[]{id});
                                        count_m++;
                                    } while (c.moveToNext());
                                }
                            }
                            c.close();

                            if (count_m == 0) {
                                try {
                                    values.put(DBHelper.KEY_ID, id);
                                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_COLORS, null, values);

                                } catch (Exception e) {
                                }
                            }
                        }

                        id_array = dat.getJSONArray("rgzbn_gm_ceiling_components");
                        Log.d("mLog", "rgzbn_gm_ceiling_components " + id_array);
                        for (int i = 0; i < id_array.length(); i++) {

                            count_m = 0;
                            org.json.JSONObject comp = id_array.getJSONObject(i);

                            String id = comp.getString("id");
                            String title = comp.getString("title");
                            String unit = comp.getString("unit");
                            String code = comp.getString("code");

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID, id);
                            values.put(DBHelper.KEY_TITLE, title);
                            values.put(DBHelper.KEY_UNIT, unit);
                            values.put(DBHelper.KEY_CODE, code);

                            String sqlQuewy = "SELECT * "
                                    + "FROM rgzbn_gm_ceiling_components" +
                                    " WHERE _id = ?";
                            Cursor c = db.rawQuery(sqlQuewy, new String[]{id});
                            if (c != null) {
                                if (c.moveToFirst()) {
                                    do {
                                        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_COMPONENTS, values, "_id = ?", new String[]{id});
                                        count_m++;
                                    } while (c.moveToNext());
                                }
                            }

                            c.close();

                            if (count_m == 0) {
                                try {

                                    values.put(DBHelper.KEY_ID, id);
                                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_COMPONENTS, null, values);

                                } catch (Exception e) {
                                    Log.d("responce", String.valueOf(e));
                                }
                            }
                        }

                        id_array = dat.getJSONArray("rgzbn_gm_ceiling_components_option");
                        Log.d(TAG, "rgzbn_gm_ceiling_components_option " + id_array);
                        for (int i = 0; i < id_array.length(); i++) {

                            count_m = 0;
                            org.json.JSONObject comp_op = id_array.getJSONObject(i);

                            String id = comp_op.getString("id");
                            String component_id = comp_op.getString("component_id");
                            String title = comp_op.getString("title");
                            String price = comp_op.getString("price");
                            String count = comp_op.getString("count");
                            String count_sale = comp_op.getString("count_sale");

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID, id);
                            values.put(DBHelper.KEY_COMPONENT_ID, component_id);
                            values.put(DBHelper.KEY_TITLE, title);
                            values.put(DBHelper.KEY_PRICE, price);
                            values.put(DBHelper.KEY_COUNT, count);
                            values.put(DBHelper.KEY_COUNT_SALE, count_sale);

                            String sqlQuewy = "SELECT * "
                                    + "FROM rgzbn_gm_ceiling_components_option" +
                                    " WHERE _id = ?";
                            Cursor c = db.rawQuery(sqlQuewy, new String[]{id});
                            if (c != null) {
                                if (c.moveToFirst()) {
                                    do {
                                        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_COMPONENTS_OPTION, values, "_id = ?", new String[]{id});
                                        count_m++;
                                    } while (c.moveToNext());
                                }
                            }

                            c.close();

                            if (count_m == 0) {
                                try {

                                    values.put(DBHelper.KEY_ID, id);
                                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_COMPONENTS_OPTION, null, values);

                                } catch (Exception e) {
                                    Log.d("responce", String.valueOf(e));
                                }
                            }

                        }


                        id_array = dat.getJSONArray("rgzbn_gm_ceiling_type");
                        Log.d(TAG, "rgzbn_gm_ceiling_type " + id_array);
                        for (int i = 0; i < id_array.length(); i++) {

                            count_m = 0;
                            org.json.JSONObject type = id_array.getJSONObject(i);

                            String id = type.getString("id");
                            String parent = type.getString("parent");
                            String title = type.getString("title");

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID, id);
                            values.put(DBHelper.KEY_PARENT, parent);
                            values.put(DBHelper.KEY_TITLE, title);

                            String sqlQuewy = "SELECT * "
                                    + "FROM rgzbn_gm_ceiling_type" +
                                    " WHERE _id = ?";
                            Cursor c = db.rawQuery(sqlQuewy, new String[]{id});
                            if (c != null) {
                                if (c.moveToFirst()) {
                                    do {
                                        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_TYPE, values, "_id = ?", new String[]{id});
                                        count_m++;
                                    } while (c.moveToNext());
                                }
                            }

                            c.close();

                            if (count_m == 0) {
                                try {

                                    values.put(DBHelper.KEY_ID, id);
                                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_TYPE, null, values);

                                } catch (Exception e) {
                                    Log.d("responce", String.valueOf(e));
                                }
                            }
                        }

                        id_array = dat.getJSONArray("rgzbn_gm_ceiling_type_option");
                        Log.d(TAG, "rgzbn_gm_ceiling_type_option " + id_array);
                        for (int i = 0; i < id_array.length(); i++) {

                            count_m = 0;
                            org.json.JSONObject type_op = id_array.getJSONObject(i);

                            String id = type_op.getString("id");
                            String type_id = type_op.getString("type_id");
                            String component_id = type_op.getString("component_id");
                            String default_comp_option_id = type_op.getString("default_comp_option_id");
                            String count = type_op.getString("count");

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID, id);
                            values.put(DBHelper.KEY_TYPE_ID, type_id);
                            values.put(DBHelper.KEY_COMPONENT_ID, component_id);
                            values.put(DBHelper.KEY_DEFAULT_COMP_OPTION_ID, default_comp_option_id);
                            values.put(DBHelper.KEY_COUNT, count);

                            String sqlQuewy = "SELECT * "
                                    + "FROM rgzbn_gm_ceiling_type_option" +
                                    " WHERE _id = ?";
                            Cursor c = db.rawQuery(sqlQuewy, new String[]{id});
                            if (c != null) {
                                if (c.moveToFirst()) {
                                    do {
                                        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_TYPE_OPTION, values, "_id = ?", new String[]{id});
                                        count_m++;
                                    } while (c.moveToNext());
                                }
                            }

                            c.close();

                            if (count_m == 0) {
                                try {

                                    values.put(DBHelper.KEY_ID, id);
                                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_TYPE_OPTION, null, values);

                                } catch (Exception e) {
                                    Log.d("responce", String.valueOf(e));
                                }
                            }
                        }

                        id_array = dat.getJSONArray("rgzbn_gm_ceiling_status");
                        Log.d(TAG, "rgzbn_gm_ceiling_status " + id_array);
                        for (int i = 0; i < id_array.length(); i++) {

                            count_m = 0;
                            org.json.JSONObject status = id_array.getJSONObject(i);

                            String id = status.getString("id");
                            String title = status.getString("title");

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID, id);
                            values.put(DBHelper.KEY_TITLE, title);

                            String sqlQuewy = "SELECT * "
                                    + "FROM rgzbn_gm_ceiling_status" +
                                    " WHERE _id = ?";
                            Cursor c = db.rawQuery(sqlQuewy, new String[]{id});
                            if (c != null) {
                                if (c.moveToFirst()) {
                                    do {
                                        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_STATUS, values, "_id = ?", new String[]{id});
                                        count_m++;
                                    } while (c.moveToNext());
                                }
                            }

                            c.close();

                            if (count_m == 0) {
                                try {

                                    values.put(DBHelper.KEY_ID, id);
                                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_STATUS, null, values);

                                } catch (Exception e) {
                                    Log.d("responce", String.valueOf(e));
                                }
                            }

                        }

                        values = new ContentValues();
                        Time time = new Time(Time.getCurrentTimezone());
                        time.setToNow();
                        String t = time.format("%Y-%m-%d %H:%M:00");

                        values.put(DBHelper.KEY_CHANGE_TIME, t);
                        db.update(DBHelper.HISTORY_IMPORT_TO_SERVER, values, "title=?", new String[]{"material"});

                    } catch (Exception e) {
                    }

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "send error 2 " + String.valueOf(error));
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("sync_data", material);
                    Log.d(TAG, "mat = " + String.valueOf(parameters));
                    return parameters;
                }
            };

            request.setShouldCache(false);
            requestQueue.add(request);

            return null;
        }

    }

    class Send_Mounters extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&task=api.sendMountersToAndroid";
        Map<String, String> parameters = new HashMap<String, String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // try {

            StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String res) {

                    Log.d(TAG, "Send_Mounters MAINACTIVITY " + res);

                    SQLiteDatabase db;
                    db = dbHelper.getWritableDatabase();
                    ContentValues values;
                    String new_id = "";

                    int count_m;
                    try {
                        org.json.JSONObject dat = new org.json.JSONObject(res);

                        JSONArray id_array = dat.getJSONArray("rgzbn_users");
                        Log.d("responce", "USERS = " + String.valueOf(id_array));
                        for (int i = 0; i < id_array.length(); i++) {

                            count_m = 0;
                            org.json.JSONObject user = id_array.getJSONObject(i);

                            String id = user.getString("id");
                            String name = user.getString("name");
                            String username = user.getString("username");
                            String email = user.getString("email");
                            String associated_client = "";
                            try {
                                associated_client = user.getString("associated_client");
                            } catch (Exception e) {
                            }

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID, id);
                            values.put(DBHelper.KEY_DEALER_ID, yourDealer);
                            values.put(DBHelper.KEY_NAME, name);
                            values.put(DBHelper.KEY_USERNAME, username);
                            values.put(DBHelper.KEY_EMAIL, email);
                            values.put(DBHelper.KEY_ASSOCIATED_CLIENT, associated_client);

                            String sqlQuewy = "SELECT * "
                                    + "FROM rgzbn_users" +
                                    " WHERE _id = ?";
                            Cursor c = db.rawQuery(sqlQuewy, new String[]{id});
                            if (c != null) {
                                if (c.moveToFirst()) {
                                    do {
                                        db.update(DBHelper.TABLE_USERS, values, "_id = ?", new String[]{id});
                                        count_m++;
                                    } while (c.moveToNext());
                                }
                            }

                            c.close();

                            if (count_m == 0) {
                                try {
                                    db.insert(DBHelper.TABLE_USERS, null, values);
                                } catch (Exception e) {
                                }
                            }

                            String map_id = user.getString("map_id");
                            String user_id = user.getString("id");
                            String group_id = user.getString("group_id");

                            count_m = 0;
                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID, map_id);
                            values.put(DBHelper.KEY_USER_ID, user_id);
                            values.put(DBHelper.KEY_GROUP_ID, group_id);

                            sqlQuewy = "SELECT * "
                                    + "FROM rgzbn_user_usergroup_map" +
                                    " WHERE user_id = ?";
                            c = db.rawQuery(sqlQuewy, new String[]{map_id});
                            if (c != null) {
                                if (c.moveToFirst()) {
                                    do {
                                        db.update(DBHelper.TABLE_RGZBN_USER_USERGROUP_MAP, values,
                                                "_id = ?", new String[]{map_id});
                                        count_m++;
                                    } while (c.moveToNext());
                                }
                            }

                            c.close();

                            if (count_m == 0) {
                                try {
                                    db.insert(DBHelper.TABLE_RGZBN_USER_USERGROUP_MAP, null, values);
                                } catch (Exception e) {
                                    Log.d("responce", String.valueOf(e));
                                }
                            }

                        }

                        id_array = dat.getJSONArray("rgzbn_gm_ceiling_mounters");
                        Log.d(TAG, "rgzbn_gm_ceiling_mounters " + id_array);

                        for (int i = 0; i < id_array.length(); i++) {

                            count_m = 0;
                            org.json.JSONObject mount = id_array.getJSONObject(i);

                            String id = mount.getString("id");
                            String name = mount.getString("name");
                            String phone = mount.getString("phone");

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID, id);
                            values.put(DBHelper.KEY_NAME, name);
                            values.put(DBHelper.KEY_PHONE, phone);

                            String sqlQuewy = "SELECT * "
                                    + "FROM rgzbn_gm_ceiling_mounters" +
                                    " WHERE _id = ?";
                            Cursor c = db.rawQuery(sqlQuewy, new String[]{id});
                            if (c != null) {
                                if (c.moveToFirst()) {
                                    do {
                                        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_MOUNTERS, values, "_id = ?", new String[]{id});
                                        count_m++;
                                    } while (c.moveToNext());
                                }
                            }

                            c.close();

                            if (count_m == 0) {
                                try {
                                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_MOUNTERS, null, values);

                                } catch (Exception e) {
                                    Log.d("responce", String.valueOf(e));
                                }
                            }
                        }

                        id_array = dat.getJSONArray("rgzbn_gm_ceiling_mounters_map");
                        Log.d(TAG, "rgzbn_gm_ceiling_mounters_map " + id_array);
                        for (int i = 0; i < id_array.length(); i++) {

                            count_m = 0;
                            org.json.JSONObject comp = id_array.getJSONObject(i);

                            String id = comp.getString("id");
                            String id_mounter = comp.getString("id_mounter");
                            String id_brigade = comp.getString("id_brigade");

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID, id);
                            values.put(DBHelper.KEY_ID_MOUNTER, id_mounter);
                            values.put(DBHelper.KEY_ID_BRIGADE, id_brigade);

                            String sqlQuewy = "SELECT * "
                                    + "FROM rgzbn_gm_ceiling_mounters_map" +
                                    " WHERE id_mounter = ?";
                            Cursor c = db.rawQuery(sqlQuewy, new String[]{id_mounter});
                            if (c != null) {
                                if (c.moveToFirst()) {
                                    do {
                                        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_MOUNTERS_MAP, values, "id_mounter = ?", new String[]{id_mounter});
                                        count_m++;
                                    } while (c.moveToNext());
                                }
                            }
                            c.close();

                            if (count_m == 0) {
                                try {
                                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_MOUNTERS_MAP, null, values);
                                } catch (Exception e) {
                                    Log.d("responce", String.valueOf(e));
                                }
                            }
                        }

                        values = new ContentValues();
                        Time time = new Time(Time.getCurrentTimezone());
                        time.setToNow();
                        String t = time.format("%Y-%m-%d %H:%M:00");
                        values.put(DBHelper.KEY_CHANGE_TIME, t);
                        db.update(DBHelper.HISTORY_IMPORT_TO_SERVER, values, "title=?", new String[]{"mount"});

                    } catch (Exception e) {
                        Log.d(TAG, "send error " + String.valueOf(e));
                    }

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("sync_data", mounters);
                    Log.d(TAG, "mou = " + String.valueOf(parameters));
                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }
    }

    class Send_Dealer extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&task=api.sendDealerInfoToAndroid";
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

                    Log.d(TAG, "Send_Dealer MAINACTIVITY " + res);

                    SQLiteDatabase db;
                    db = dbHelper.getWritableDatabase();
                    ContentValues values;
                    String new_id = "";

                    int count_m;
                    try {
                        org.json.JSONObject dat = new org.json.JSONObject(res);

                        JSONArray id_array = dat.getJSONArray("rgzbn_gm_ceiling_dealer_info");
                        for (int i = 0; i < id_array.length(); i++) {

                            count_m = 0;
                            org.json.JSONObject user = id_array.getJSONObject(i);

                            String id = user.getString("id");
                            String dealer_canvases_margin = user.getString("dealer_canvases_margin");
                            String dealer_components_margin = user.getString("dealer_components_margin");
                            String dealer_mounting_margin = user.getString("dealer_mounting_margin");
                            String gm_canvases_margin = user.getString("gm_canvases_margin");
                            String gm_components_margin = user.getString("gm_components_margin");
                            String gm_mounting_margin = user.getString("gm_mounting_margin");
                            String dealer_id = user.getString("dealer_id");
                            String discount = user.getString("discount");

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID, id);
                            values.put(DBHelper.KEY_DEALER_CANVASES_MARGIN, dealer_canvases_margin);
                            values.put(DBHelper.KEY_DEALER_COMPONENTS_MARGIN, dealer_components_margin);
                            values.put(DBHelper.KEY_DEALER_MOUNTING_MARGIN, dealer_mounting_margin);
                            values.put(DBHelper.KEY_GM_CANVASES_MARGIN, gm_canvases_margin);
                            values.put(DBHelper.KEY_GM_COMPONENTS_MARGIN, gm_components_margin);
                            values.put(DBHelper.KEY_GM_MOUNTING_MARGIN, gm_mounting_margin);
                            values.put(DBHelper.KEY_DEALER_ID, dealer_id);
                            values.put(DBHelper.KEY_DISCOUNT, discount);

                            String sqlQuewy = "SELECT * "
                                    + "FROM rgzbn_gm_ceiling_dealer_info" +
                                    " WHERE _id = ?";
                            Cursor c = db.rawQuery(sqlQuewy, new String[]{id});
                            if (c != null) {
                                if (c.moveToFirst()) {
                                    do {
                                        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_DEALER_INFO, values, "_id = ?", new String[]{id});
                                        count_m++;
                                    } while (c.moveToNext());
                                }
                            }
                            c.close();

                            if (count_m == 0) {
                                try {
                                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_DEALER_INFO, null, values);
                                } catch (Exception e) {
                                    Log.d("responce", String.valueOf(e));
                                }
                            }
                        }

                        id_array = dat.getJSONArray("rgzbn_gm_ceiling_mount");

                        for (int i = 0; i < id_array.length(); i++) {
                            count_m = 0;
                            org.json.JSONObject user = id_array.getJSONObject(i);

                            String id = user.getString("id");
                            String mp1 = user.getString("mp1");
                            String mp2 = user.getString("mp2");
                            String mp3 = user.getString("mp3");
                            String mp4 = user.getString("mp4");
                            String mp5 = user.getString("mp5");
                            String mp6 = user.getString("mp6");
                            String mp7 = user.getString("mp7");
                            String mp8 = user.getString("mp8");
                            String mp9 = user.getString("mp9");
                            String mp10 = user.getString("mp10");
                            String mp11 = user.getString("mp11");
                            String mp12 = user.getString("mp12");
                            String mp13 = user.getString("mp13");
                            String mp14 = user.getString("mp14");
                            String mp15 = user.getString("mp15");
                            String mp16 = user.getString("mp16");
                            String mp17 = user.getString("mp17");
                            String mp18 = user.getString("mp18");
                            String mp19 = user.getString("mp19");
                            String mp20 = user.getString("mp20");
                            String mp21 = user.getString("mp21");
                            String mp22 = user.getString("mp22");
                            String mp23 = user.getString("mp23");
                            String mp24 = user.getString("mp24");
                            String mp25 = user.getString("mp25");
                            String mp26 = user.getString("mp26");
                            String mp27 = user.getString("mp27");
                            String mp28 = user.getString("mp28");
                            String mp29 = user.getString("mp29");
                            String mp30 = user.getString("mp30");
                            String mp31 = user.getString("mp31");
                            String mp32 = user.getString("mp32");
                            String mp33 = user.getString("mp33");
                            String mp34 = user.getString("mp34");
                            String mp35 = user.getString("mp35");
                            String mp36 = user.getString("mp36");
                            String mp37 = user.getString("mp37");
                            String mp38 = user.getString("mp38");
                            String mp39 = user.getString("mp39");
                            String mp40 = user.getString("mp40");
                            String mp41 = user.getString("mp41");
                            String mp42 = user.getString("mp42");
                            String mp43 = user.getString("mp43");
                            String min_sum = user.getString("min_sum");
                            String min_components_sum = user.getString("min_components_sum");
                            String transport = user.getString("transport");
                            String user_id = user.getString("user_id");
                            String distance = user.getString("distance");

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID, id);
                            values.put(DBHelper.KEY_MP1, mp1);
                            values.put(DBHelper.KEY_MP2, mp2);
                            values.put(DBHelper.KEY_MP3, mp3);
                            values.put(DBHelper.KEY_MP4, mp4);
                            values.put(DBHelper.KEY_MP5, mp5);
                            values.put(DBHelper.KEY_MP6, mp6);
                            values.put(DBHelper.KEY_MP7, mp7);
                            values.put(DBHelper.KEY_MP8, mp8);
                            values.put(DBHelper.KEY_MP9, mp9);
                            values.put(DBHelper.KEY_MP10, mp10);
                            values.put(DBHelper.KEY_MP11, mp11);
                            values.put(DBHelper.KEY_MP12, mp12);
                            values.put(DBHelper.KEY_MP13, mp13);
                            values.put(DBHelper.KEY_MP14, mp14);
                            values.put(DBHelper.KEY_MP15, mp15);
                            values.put(DBHelper.KEY_MP16, mp16);
                            values.put(DBHelper.KEY_MP17, mp17);
                            values.put(DBHelper.KEY_MP18, mp18);
                            values.put(DBHelper.KEY_MP19, mp19);
                            values.put(DBHelper.KEY_MP20, mp20);
                            values.put(DBHelper.KEY_MP21, mp21);
                            values.put(DBHelper.KEY_MP22, mp22);
                            values.put(DBHelper.KEY_MP23, mp23);
                            values.put(DBHelper.KEY_MP24, mp24);
                            values.put(DBHelper.KEY_MP25, mp25);
                            values.put(DBHelper.KEY_MP26, mp26);
                            values.put(DBHelper.KEY_MP27, mp27);
                            values.put(DBHelper.KEY_MP28, mp28);
                            values.put(DBHelper.KEY_MP29, mp29);
                            values.put(DBHelper.KEY_MP30, mp30);
                            values.put(DBHelper.KEY_MP31, mp31);
                            values.put(DBHelper.KEY_MP32, mp32);
                            values.put(DBHelper.KEY_MP33, mp33);
                            values.put(DBHelper.KEY_MP34, mp34);
                            values.put(DBHelper.KEY_MP35, mp35);
                            values.put(DBHelper.KEY_MP36, mp36);
                            values.put(DBHelper.KEY_MP37, mp37);
                            values.put(DBHelper.KEY_MP38, mp38);
                            values.put(DBHelper.KEY_MP39, mp39);
                            values.put(DBHelper.KEY_MP40, mp40);
                            values.put(DBHelper.KEY_MP41, mp41);
                            values.put(DBHelper.KEY_MP42, mp42);
                            values.put(DBHelper.KEY_MP43, mp43);
                            values.put(DBHelper.KEY_MIN_SUM, min_sum);
                            values.put(DBHelper.KEY_MIN_COMPONENTS_SUM, min_components_sum);
                            values.put(DBHelper.KEY_TRANSPORT, transport);
                            values.put(DBHelper.KEY_USER_ID, user_id);
                            values.put(DBHelper.KEY_DISTANCE, distance);

                            if (user_id.equals(yourDealer)) {
                                String sqlQuewy = "SELECT * "
                                        + "FROM rgzbn_gm_ceiling_mount" +
                                        " WHERE user_id = ?";
                                Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id});
                                if (c != null) {
                                    if (c.moveToFirst()) {
                                        do {
                                            db.update(DBHelper.TABLE_RGZBN_GM_CEILING_MOUNT, values, "_id = ?", new String[]{id});
                                            count_m++;
                                        } while (c.moveToNext());
                                    }
                                }

                                c.close();

                                if (count_m == 0) {
                                    try {
                                        db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_MOUNT, null, values);
                                    } catch (Exception e) {
                                        Log.d(TAG, "error " + String.valueOf(e));
                                    }
                                }
                            }
                        }

                        id_array = dat.getJSONArray("rgzbn_gm_ceiling_api_phones");
                        Log.d(TAG, "rgzbn_gm_ceiling_api_phones = " + id_array);
                        for (int i = 0; i < id_array.length(); i++) {
                            count_m = 0;
                            org.json.JSONObject user = id_array.getJSONObject(i);

                            String id = user.getString("id");
                            String name = user.getString("name");
                            String dealer_id = user.getString("dealer_id");

                            values = new ContentValues();
                            values.put(DBHelper.KEY_NAME, name);
                            values.put(DBHelper.KEY_DEALER_ID, dealer_id);

                            String sqlQuewy = "SELECT * "
                                    + "FROM rgzbn_gm_ceiling_api_phones" +
                                    " WHERE _id = ?";
                            Cursor c = db.rawQuery(sqlQuewy, new String[]{id});
                            if (c != null) {
                                if (c.moveToFirst()) {
                                    do {
                                        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_API_PHONES, values, "_id = ?", new String[]{id});
                                        count_m++;
                                    } while (c.moveToNext());
                                }
                            }
                            c.close();

                            if (count_m == 0) {
                                try {
                                    values.put(DBHelper.KEY_ID, id);
                                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_API_PHONES, null, values);
                                } catch (Exception e) {
                                }
                            }
                        }

                        id_array = dat.getJSONArray("rgzbn_gm_ceiling_recoil_map_project");
                        for (int i = 0; i < id_array.length(); i++) {
                            count_m = 0;
                            org.json.JSONObject user = id_array.getJSONObject(i);

                            String id = user.getString("id");
                            String recoil_id = user.getString("recoil_id");
                            String project_id = user.getString("project_id");
                            String sum = user.getString("sum");
                            String date_time = user.getString("date_time");
                            String comment = user.getString("comment");

                            values = new ContentValues();
                            values.put(DBHelper.KEY_RECOIL_ID, recoil_id);
                            values.put(DBHelper.KEY_PROJECT_ID, project_id);
                            values.put(DBHelper.KEY_SUM, sum);
                            values.put(DBHelper.KEY_DATE_TIME, date_time);
                            values.put(DBHelper.KEY_COMMENT, comment);

                            String sqlQuewy = "SELECT * "
                                    + "FROM rgzbn_gm_ceiling_recoil_map_project" +
                                    " WHERE _id = ?";
                            Cursor c = db.rawQuery(sqlQuewy, new String[]{id});
                            if (c != null) {
                                if (c.moveToFirst()) {
                                    do {
                                        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_RECOIL_MAP_PROJECT, values, "_id = ?", new String[]{id});
                                        count_m++;
                                    } while (c.moveToNext());
                                }
                            }
                            c.close();

                            if (count_m == 0) {
                                try {
                                    values.put(DBHelper.KEY_ID, id);
                                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_RECOIL_MAP_PROJECT, null, values);
                                } catch (Exception e) {
                                }
                            }
                        }

                        id_array = dat.getJSONArray("rgzbn_gm_ceiling_canvases_dealer_price");
                        String user_id = "";
                        for (int i = 0; i < id_array.length(); i++) {

                            count_m = 0;
                            org.json.JSONObject user = id_array.getJSONObject(i);

                            user_id = user.getString("user_id");
                            String canvas_id = user.getString("canvas_id");
                            String price = user.getString("price");
                            String value = user.getString("value");
                            String type = user.getString("type");

                            values = new ContentValues();
                            values.put(DBHelper.KEY_USER_ID, user_id);
                            values.put(DBHelper.KEY_CANVAS_ID, canvas_id);
                            values.put(DBHelper.KEY_PRICE, price);
                            values.put(DBHelper.KEY_VALUE, value);
                            values.put(DBHelper.KEY_TYPE, type);

                            String sqlQuewy = "SELECT * "
                                    + "FROM rgzbn_gm_ceiling_canvases_dealer_price" +
                                    " WHERE user_id = ? and canvas_id = ?";
                            Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id, canvas_id});
                            if (c != null) {
                                if (c.moveToFirst()) {
                                    do {
                                        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CANVASES_DEALER_PRICE,
                                                values,
                                                "user_id = ? and canvas_id = ?",
                                                new String[]{user_id, canvas_id});
                                        count_m++;
                                    } while (c.moveToNext());
                                }
                            }
                            c.close();

                            if (count_m == 0) {
                                try {
                                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CANVASES_DEALER_PRICE, null, values);
                                } catch (Exception e) {
                                    Log.d("responce", String.valueOf(e));
                                }
                            }
                        }

                        id_array = dat.getJSONArray("rgzbn_gm_ceiling_components_dealer_price");
                        for (int i = 0; i < id_array.length(); i++) {

                            count_m = 0;
                            org.json.JSONObject user = id_array.getJSONObject(i);

                            user_id = user.getString("user_id");
                            String component_id = user.getString("component_id");
                            String price = user.getString("price");
                            String value = user.getString("value");
                            String type = user.getString("type");

                            values = new ContentValues();
                            values.put(DBHelper.KEY_USER_ID, user_id);
                            values.put(DBHelper.KEY_COMPONENT_ID, component_id);
                            values.put(DBHelper.KEY_PRICE, price);
                            values.put(DBHelper.KEY_VALUE, value);
                            values.put(DBHelper.KEY_TYPE, type);

                            String sqlQuewy = "SELECT * "
                                    + "FROM rgzbn_gm_ceiling_components_dealer_price" +
                                    " WHERE user_id = ? and component_id = ?";
                            Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id, component_id});
                            if (c != null) {
                                if (c.moveToFirst()) {
                                    do {
                                        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_COMPONENTS_DEALER_PRICE,
                                                values,
                                                "user_id = ? and component_id = ?",
                                                new String[]{user_id, component_id});
                                        count_m++;
                                    } while (c.moveToNext());
                                }
                            }
                            c.close();

                            if (count_m == 0) {
                                try {
                                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_COMPONENTS_DEALER_PRICE,
                                            null,
                                            values);
                                } catch (Exception e) {
                                    Log.d("responce", String.valueOf(e));
                                }
                            }
                        }

                        Log.d(TAG, "yourDealer " + user_id);
                        int count_c = 0;
                        String sqlQuewy = "SELECT * "
                                + "FROM rgzbn_gm_ceiling_canvases_dealer_price" +
                                " WHERE user_id = ?";
                        Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id});
                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    count_c++;
                                } while (c.moveToNext());
                            }
                        }
                        c.close();

                        int count_p = 0;
                        sqlQuewy = "SELECT * "
                                + "FROM rgzbn_gm_ceiling_canvases_dealer_price" +
                                " WHERE user_id = ?";
                        c = db.rawQuery(sqlQuewy, new String[]{user_id});
                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    count_p++;
                                } while (c.moveToNext());
                            }
                        }
                        c.close();

                        if ((count_c > 0 && count_p > 0) || user_id.equals("1")) {

                        } else {
                            jsonDealer.put("dealer_id", "1");
                            jsonDealer.put("change_time", "0000-00-00 00:00:00");
                            dealer = String.valueOf(jsonDealer);
                            new Send_Dealer().execute();
                        }

                        if (usergroup == 22) {
                            values = new ContentValues();
                            Time time = new Time(Time.getCurrentTimezone());
                            time.setToNow();
                            String t = time.format("%Y-%m-%d %H:%M:00");
                            values.put(DBHelper.KEY_CHANGE_TIME, t);
                            db.update(DBHelper.HISTORY_IMPORT_TO_SERVER, values, "title=?", new String[]{"dealer"});

                            Send_All.Alarm.setAlarm(MainActivity.this);
                            //startService(new Intent(MainActivity.this, Send_All.class));

                            //Service_Sync_Import.Alarm.setAlarm(MainActivity.this);
                            startService(new Intent(MainActivity.this, Service_Sync_Import.class));

                            Intent intent = new Intent(MainActivity.this, Gager_office.class);
                            startActivity(intent);
                            finish();

                            mProgressDialog.dismiss();

                        } else if (usergroup == 14) {
                            values = new ContentValues();
                            Time time = new Time(Time.getCurrentTimezone());
                            time.setToNow();
                            String t = time.format("%Y-%m-%d %H:%M:00");
                            values.put(DBHelper.KEY_CHANGE_TIME, t);
                            db.update(DBHelper.HISTORY_IMPORT_TO_SERVER, values, "title=?", new String[]{"dealer"});

                            Send_All.Alarm.setAlarm(MainActivity.this);
                            //startService(new Intent(MainActivity.this, Send_All.class));

                            //Service_Sync_Import.Alarm.setAlarm(MainActivity.this);
                            startService(new Intent(MainActivity.this, Service_Sync_Import.class));

                            Intent intent = new Intent(MainActivity.this, Dealer_office.class);
                            startActivity(intent);
                            finish();

                            mProgressDialog.dismiss();
                        }

                    } catch (Exception e) {
                        Log.d(TAG, String.valueOf(e));
                    }

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("sync_data", dealer);
                    Log.d(TAG, "dealer = " + String.valueOf(parameters));
                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }
    }


}