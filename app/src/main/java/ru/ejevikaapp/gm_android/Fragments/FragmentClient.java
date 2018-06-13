package ru.ejevikaapp.gm_android.Fragments;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;

import java.util.ArrayList;

import ru.ejevikaapp.gm_android.Activity_add_client;
import ru.ejevikaapp.gm_android.Class.Frag_client_schedule_class;
import ru.ejevikaapp.gm_android.Class.HelperClass;
import ru.ejevikaapp.gm_android.DBHelper;
import ru.ejevikaapp.gm_android.Dealer.Activity_client;
import ru.ejevikaapp.gm_android.Dealer.Activity_for_spisok;
import ru.ejevikaapp.gm_android.R;
import ru.ejevikaapp.gm_android.Service_Sync_Import;

import static android.content.Context.MODE_PRIVATE;

public class FragmentClient extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    DBHelper dbHelper;
    ListView list_clients;
    ArrayList<Frag_client_schedule_class> client_mas = new ArrayList<>();

    SharedPreferences SP;
    String SAVED_ID = "", user_id;

    Button btn_search, btn_add_client;
    EditText c_search;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    View view;

    public FragmentClient() {
    }

    public static FragmentClient newInstance() {
        return new FragmentClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_client, container, false);

        btn_search = (Button) view.findViewById(R.id.btn_search);
        btn_add_client = (Button) view.findViewById(R.id.btn_add_client);
        c_search = (EditText) view.findViewById(R.id.c_search);

        btn_search.setOnClickListener(this);
        btn_add_client.setOnClickListener(this);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorBlue);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                clients(c_search.getText().toString());
                break;
            case R.id.btn_add_client:
                Intent intent = new Intent(getActivity(), Activity_add_client.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRefresh() {
        if (HelperClass.isOnline(getActivity())) {
            getActivity().startService(new Intent(getActivity(), Service_Sync_Import.class));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onResume();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 3000);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity().getApplicationContext(),
                    "проверьте подключение к интернету", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            client_mas.clear();
            list_clients = (ListView) view.findViewById(R.id.list_client);
            clients("");
        } catch (Exception e) {
            Log.d("mLog", String.valueOf(e));
        }
    }

    void clients(String client_name) {

        client_mas.clear();

        SP = getActivity().getSharedPreferences("user_id", MODE_PRIVATE);
        user_id = SP.getString("", "");

        SP = getActivity().getSharedPreferences("dealer_id", MODE_PRIVATE);
        String dealer_id = SP.getString("", "");

        dbHelper = new DBHelper(getActivity());

        final SQLiteDatabase db = dbHelper.getReadableDatabase();

        ArrayList client = new ArrayList();

        if (client_name.equals("")) {
            String sqlQuewy = "SELECT _id, client_name, created "
                    + "FROM rgzbn_gm_ceiling_clients " +
                    "where dealer_id = ? and deleted_by_user = ?" +
                    "group by client_name";
            Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id, "0"});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {

                        String id_client = c.getString(c.getColumnIndex(c.getColumnName(0)));

                        if (HelperClass.associated_client(getActivity(), user_id, id_client)) {

                        } else {

                            sqlQuewy = "SELECT project_info, project_status "
                                    + "FROM rgzbn_gm_ceiling_projects " +
                                    "where client_id = ?";
                            Cursor cc = db.rawQuery(sqlQuewy, new String[]{id_client});
                            if (cc != null) {
                                if (cc.moveToLast()) {

                                    String status = cc.getString(cc.getColumnIndex(cc.getColumnName(1)));

                                    sqlQuewy = "SELECT title "
                                            + "FROM rgzbn_gm_ceiling_status " +
                                            "where _id = ? ";
                                    Cursor c1 = db.rawQuery(sqlQuewy, new String[]{status});
                                    if (c1 != null) {
                                        if (c1.moveToFirst()) {
                                            do {
                                                status = c1.getString(c1.getColumnIndex(c1.getColumnName(0)));
                                            } while (c1.moveToNext());
                                        }
                                    }
                                    c1.close();

                                    Frag_client_schedule_class fc = new Frag_client_schedule_class(null,
                                            c.getString(c.getColumnIndex(c.getColumnName(1))),
                                            cc.getString(cc.getColumnIndex(cc.getColumnName(0))),
                                            c.getString(c.getColumnIndex(c.getColumnName(0))),
                                            status,
                                            c.getString(c.getColumnIndex(c.getColumnName(2))));
                                    client_mas.add(fc);

                                } else {
                                    Frag_client_schedule_class fc = new Frag_client_schedule_class(null,
                                            c.getString(c.getColumnIndex(c.getColumnName(1))),
                                            "-",
                                            c.getString(c.getColumnIndex(c.getColumnName(0))),
                                            "-",
                                            c.getString(c.getColumnIndex(c.getColumnName(2))));
                                    client_mas.add(fc);
                                }
                                cc.close();
                            }
                        }

                    } while (c.moveToNext());
                }
            }
            c.close();
        } else {
            String sqlQuewy = "SELECT _id, client_name, created "
                    + "FROM rgzbn_gm_ceiling_clients " +
                    "where dealer_id = ? and client_name LIKE '%" + client_name + "%' " +
                    "group by client_name ";
            Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {

                        String id_client = c.getString(c.getColumnIndex(c.getColumnName(0)));

                        if (HelperClass.associated_client(getActivity(), user_id, id_client)) {
                        } else {
                            sqlQuewy = "SELECT project_info, project_status "
                                    + "FROM rgzbn_gm_ceiling_projects " +
                                    "where client_id = ?";
                            Cursor cc = db.rawQuery(sqlQuewy, new String[]{id_client});
                            if (cc != null) {
                                if (cc.moveToLast()) {

                                    String status = cc.getString(cc.getColumnIndex(cc.getColumnName(1)));

                                    sqlQuewy = "SELECT title "
                                            + "FROM rgzbn_gm_ceiling_status " +
                                            "where _id = ? ";
                                    Cursor c1 = db.rawQuery(sqlQuewy, new String[]{status});
                                    if (c1 != null) {
                                        if (c1.moveToFirst()) {
                                            do {
                                                status = c1.getString(c1.getColumnIndex(c1.getColumnName(0)));
                                            } while (c1.moveToNext());
                                        }
                                    }
                                    c1.close();

                                    Frag_client_schedule_class fc = new Frag_client_schedule_class(null,
                                            c.getString(c.getColumnIndex(c.getColumnName(1))),
                                            cc.getString(cc.getColumnIndex(cc.getColumnName(0))),
                                            c.getString(c.getColumnIndex(c.getColumnName(0))),
                                            status,
                                            c.getString(c.getColumnIndex(c.getColumnName(2))));
                                    client_mas.add(fc);

                                } else {
                                    Frag_client_schedule_class fc = new Frag_client_schedule_class(null,
                                            c.getString(c.getColumnIndex(c.getColumnName(1))),
                                            "-",
                                            c.getString(c.getColumnIndex(c.getColumnName(0))),
                                            "-",
                                            c.getString(c.getColumnIndex(c.getColumnName(2))));
                                    client_mas.add(fc);
                                }
                                cc.close();
                            }
                        }

                    } while (c.moveToNext());
                }
            }
            c.close();
        }

        BindDictionary<Frag_client_schedule_class> dict = new BindDictionary<>();

        dict.addStringField(R.id.c_number, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getCreate();
            }
        });
        dict.addStringField(R.id.c_address, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getFio();
            }
        });
        dict.addStringField(R.id.c_price, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getAddress();
            }
        });
        dict.addStringField(R.id.c_income, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getStatus();
            }
        });
        FunDapter adapter = new FunDapter(getActivity(), client_mas, R.layout.clients_item44, dict);
        list_clients.setAdapter(adapter);
        list_clients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Frag_client_schedule_class selectedid = client_mas.get(position);
                String p_id = selectedid.getId_client();

                SP = getActivity().getSharedPreferences("activity_client", MODE_PRIVATE);
                SharedPreferences.Editor ed = SP.edit();
                ed.putString("", String.valueOf(p_id));
                ed.commit();

                Intent intent = new Intent(getActivity(), Activity_for_spisok.class);
                startActivity(intent);
            }
        });

        list_clients.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub

                Frag_client_schedule_class selectedid = client_mas.get(pos);
                final String cId = selectedid.getId_client();
                String name = selectedid.getFio();

                AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                ad.setMessage("Удалить клиента " + name + "?"); // сообщение
                ad.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {

                        ContentValues values = new ContentValues();
                        values.put(DBHelper.KEY_DELETED_BY_USER, "1");
                        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS, values, "_id = ?",
                                new String[]{cId});

                        values = new ContentValues();
                        values.put(DBHelper.KEY_ID_OLD, cId);
                        values.put(DBHelper.KEY_ID_NEW, "0");
                        values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_clients");
                        values.put(DBHelper.KEY_SYNC, "0");
                        values.put(DBHelper.KEY_TYPE, "send");
                        values.put(DBHelper.KEY_STATUS, "1");
                        db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                        String sqlQuewy = "select _id "
                                + "FROM rgzbn_gm_ceiling_projects " +
                                "where client_id = ?";
                        Cursor c = db.rawQuery(sqlQuewy, new String[]{cId});
                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    String id = c.getString(c.getColumnIndex(c.getColumnName(0)));
                                    values = new ContentValues();
                                    values.put(DBHelper.KEY_DELETED_BY_USER, "1");
                                    db.update(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, values, "_id = ?",
                                            new String[]{id});

                                    values = new ContentValues();
                                    values.put(DBHelper.KEY_ID_OLD, id);
                                    values.put(DBHelper.KEY_ID_NEW, "0");
                                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_projects");
                                    values.put(DBHelper.KEY_SYNC, "0");
                                    values.put(DBHelper.KEY_TYPE, "send");
                                    values.put(DBHelper.KEY_STATUS, "1");
                                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                } while (c.moveToNext());
                            }
                        }
                        c.close();

                        onResume();
                    }
                });
                ad.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {

                    }
                });
                ad.setCancelable(true);
                ad.show();
                return true;
            }
        });

    }

}