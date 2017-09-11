package com.polvazo.speakmany.speakMany.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.polvazo.speakmany.speakMany.Util.deleteChat;
import com.polvazo.speakmany.speakMany.Util.preferencia;
import com.polvazo.speakmany.speakMany.constantes.constantes;

public class MyService extends Service {
    DatabaseReference mdatabase1;
    private Binder binder = new MyBinder();
    private static final String TAG = MyService.class.getSimpleName();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand()");
        mdatabase1 = FirebaseDatabase.getInstance().getReference();
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.i(TAG, "onLowMemory()");
    }

    public void terminar() {
        Log.e("estado", "escriir");

        String numerodeSalaDisponible = preferencia.obtener(constantes.ID_KEY_NUMERO_SALA, getApplicationContext());
        String numerodeSalaDisponibleOcupada = preferencia.obtener(constantes.ID_NUMERO_SALA, getApplicationContext());
        Log.i("disponible", numerodeSalaDisponible);
        Log.i("ocupado", numerodeSalaDisponibleOcupada);

        deleteChat.EliminarDestroy(mdatabase1, numerodeSalaDisponible, numerodeSalaDisponibleOcupada);

        Log.e("estado", "escriir");
        Log.i(TAG, "onDestroy()");
        Log.i(TAG, "onTaskRemoved()");
        stopSelf();
    }

    public class MyBinder extends Binder {

        public MyService getService() {
            return MyService.this;
        }
    }
}