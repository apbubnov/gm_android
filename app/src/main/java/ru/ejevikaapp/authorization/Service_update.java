package ru.ejevikaapp.authorization;

import android.app.DownloadManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;

import static ru.ejevikaapp.authorization.Service_Sync_Import.ctx;
import static ru.ejevikaapp.authorization.Service_Sync_Import.domen;

public class Service_update extends Service {
    public Service_update() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ctx);
        notificationManager.cancelAll();

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        SharedPreferences SP = getSharedPreferences("new_version", MODE_PRIVATE);
        String version = SP.getString("", "");

        version = version.substring(1,version.length()-1);

        String url = "http://"+domen+".gm-vrn.ru/files/android_app/"+version;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("Загрузка файла...");
        request.setDescription("File is being down...");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        String nameOfFile = URLUtil.guessFileName(url, null, MimeTypeMap.getFileExtensionFromUrl(url));

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nameOfFile);

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
