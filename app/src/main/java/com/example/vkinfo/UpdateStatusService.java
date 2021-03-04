package com.example.vkinfo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class UpdateStatusService  extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
 /*       if ((flags & START_FLAG_REDELIVERY ) == 0) {
            // TODO Если это повторный запуск, выполнить какие-то действия.
        }
        else {
            // TODO Альтернативные действия в фоновом режиме.
        }
        return Service.START_NOT_STICKY ;*/
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
