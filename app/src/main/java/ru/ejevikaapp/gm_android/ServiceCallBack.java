package ru.ejevikaapp.gm_android;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import ru.ejevikaapp.gm_android.Class.HelperClass;
import ru.ejevikaapp.gm_android.Dealer.CallBack;

public class ServiceCallBack extends Service {

    private static final String TAG = "callback";
    static DBHelper dbHelper;

    public ServiceCallBack() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "Service started!");
        // TODO: тут делаем всё что нам хочется...
        return super.onStartCommand(intent, flags, startId);
    }

    public static boolean isRunning(Context ctx) {
        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (ServiceCallBack.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "Service stopped!");
    }

    public static class Alarm extends BroadcastReceiver {

        public static final String ALARM_EVENT = "net.multipi.ALARM";
        public static final int ALARM_INTERVAL_SEC = 5;

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v(TAG, "Alarm received: " + intent.getAction());
            if (!isRunning(context)) {
                context.startService(new Intent(context, ServiceCallBack.class));
            } else {
                Log.v(TAG, "don't start service: already running...");

                SharedPreferences SP_end = context.getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id = SP_end.getString("", "");

                dbHelper = new DBHelper(context);
                SQLiteDatabase db = dbHelper.getReadableDatabase();

                String sqlQuewy = "SELECT client_id, date_time, comment "
                        + "FROM rgzbn_gm_ceiling_callback " +
                        "order by date_time DESC";
                Cursor c = db.rawQuery(sqlQuewy, new String[]{});
                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            String client_id = c.getString(c.getColumnIndex(c.getColumnName(0)));
                            String date_time = c.getString(c.getColumnIndex(c.getColumnName(1)));
                            String comment = c.getString(c.getColumnIndex(c.getColumnName(2)));

                            String client_name = "";
                            sqlQuewy = "SELECT _id, client_name "
                                    + "FROM rgzbn_gm_ceiling_clients" +
                                    " WHERE dealer_id = ? and _id = ?";
                            Cursor cc = db.rawQuery(sqlQuewy, new String[]{user_id, client_id});
                            if (cc != null) {
                                if (cc.moveToFirst()) {
                                    client_name = cc.getString(cc.getColumnIndex(cc.getColumnName(1)));
                                }
                            }
                            cc.close();

                            if (client_name.equals("")) {
                                // не тот клиент
                            } else {

                                String now_date = HelperClass.now_date(context);
                                //String now_date = "2018-06-14 11:49";
                                now_date = now_date.substring(0, now_date.length());

                                Date one = null;
                                Date two = null;

                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                                try {
                                    one = format.parse(date_time);
                                    two = format.parse(now_date);
                                } catch (Exception e) {
                                }

                                long difference = one.getTime() - two.getTime();

                                Log.d(TAG, "one " + one);
                                Log.d(TAG, "two " + two);

                                int min =  (int)(difference / (60 * 1000)); // миллисекунды / (24ч * 60мин * 60сек * 1000мс)

                                Log.d(TAG, "min " + min);

                                if (min == 10) {

                                    String phone = "";
                                    sqlQuewy = "SELECT phone "
                                            + "FROM rgzbn_gm_ceiling_clients_contacts" +
                                            " WHERE client_id = ?";
                                    cc = db.rawQuery(sqlQuewy, new String[]{client_id});
                                    if (cc != null) {
                                        if (cc.moveToFirst()) {
                                            phone = cc.getString(cc.getColumnIndex(cc.getColumnName(0)));
                                        }
                                    }
                                    cc.close();

                                    String message = "Комментарий: " + comment + "\nФИО клиента: " + client_name;
                                    Intent resultIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+" + phone));

                                    PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                                            PendingIntent.FLAG_UPDATE_CURRENT);
                                    NotificationCompat.Builder builder =
                                            new NotificationCompat.Builder(context)
                                                    .setAutoCancel(true)
                                                    .setTicker("Звонок")
                                                    .setWhen(System.currentTimeMillis())
                                                    .setDefaults(Notification.DEFAULT_ALL)
                                                    .setSmallIcon(R.raw.itc_icon)
                                                    .addAction(R.raw.itc_icon, "Позвонить", resultPendingIntent)
                                                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                                                    .setContentTitle("IT-Ceiling")
                                                    .setContentText(message);
                                    Notification notification = builder.build();

                                    NotificationManager notificationManager = (NotificationManager) context
                                            .getSystemService(Context.NOTIFICATION_SERVICE);

                                    notificationManager.notify(2, notification);

                                }
                            }

                        } while (c.moveToNext());
                    }
                }
                c.close();


            }
        }

        public static void setAlarm(Context context) {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, new Intent(ALARM_EVENT), 0);
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * ALARM_INTERVAL_SEC, pi);
        }

        public static void cancelAlarm(Context context) {
            PendingIntent sender = PendingIntent.getBroadcast(context, 0, new Intent(ALARM_EVENT), 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(sender);
        }
    }

}
