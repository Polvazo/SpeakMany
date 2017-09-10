package com.polvazo.speakmany.speakMany.Service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;


import com.polvazo.speakmany.R;

import java.io.IOException;

import static android.R.style.Widget;
import static android.R.style.Widget_DeviceDefault_ActionBar_TabView;
import static android.R.style.Widget_DeviceDefault_Light_ProgressBar;
import static android.R.style.Widget_DeviceDefault_Light_ProgressBar_Horizontal;

/**
 * Verificar internet y proseguir
 */
public class VerificarInternet extends AsyncTask<Void, Void, Boolean> {

    // esta interface define como se procesara el resultado
    // es una forma personalizada de pasar a la AsyncTask tareas que
    // debe realizar cuando termine, en este caso tiene 2 métodos,
    // que invocaremos según el resultado del ping.
    public interface EntoncesHacer {
        void cuandoHayInternet();
        void cuandoNOHayInternet();
    }

    // variables necesarias, el dialogo de progreso, el contexto de la actividad
    // y una instancia de EntoncesHacer, donde se ejecutan las acciones.
    private ProgressDialog dialog;
    private Context context;
    private EntoncesHacer accion;

    // Constructor, recibe el contexto de la actividad actual,
    // y la instancia de EntoncesHacer
    public VerificarInternet(Context context, EntoncesHacer accion) {
        this.context = context;
        this.accion = accion;
    }

    // corre en el Thread de la UI antes de empezar la tarea en segundo plano.
    // aquí aprovechamos y mostramos el progress...
    @Override
    protected void onPreExecute() {
        // preparamos el cuadro de dialogo
        dialog = new ProgressDialog(context);
        dialog.setMessage(context.getString(R.string.s_ver_dialogoInternt_message));
        dialog.setCancelable(false);
        dialog.setTitle(R.string.s_ver_dialogoInternt_tittle);
        dialog.setIcon(R.mipmap.iconconexion);
        dialog.show();

        // llamamos al padre
        super.onPreExecute();
    }

    // Esta es la tarea en segundo plano en si.
    @Override
    protected Boolean doInBackground(Void... arg0) {
        // esta es una version modificada de tu codigo original, pero hace
        // mas o menos lo mismo.
        // la diferencia esta en la forma que invoca a ping, intenta mas
        // veces y espera mas tiempo.
        // si la conexion a internet esta un poco lenta, el otro mecanismo
        // puede dar falsos negativos, este es un poco mas robusto al
        // reintentar 2 veces y esperar mas tiempo.
        // si la conexion es normal, sera muy rapid
        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("ping -c 2 -w 4 google.com");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }

    // Esta es la tarea a realizar cuando termina la tarea en segundo plano
    // aquí de acuerdo al resultado llamaremos a uno u otro método
    // de la interface EntoncesHacer
    @Override
    protected void onPostExecute(Boolean resultado) {
        // llamamos al padre
        super.onPostExecute(resultado);

        // cerramos el cuadro de progreso
        dialog.dismiss();

        // de acuerdo al resultado del ping, se ejecuta una acción o la otra
        if (resultado) {
            accion.cuandoHayInternet();
        } else {
            accion.cuandoNOHayInternet();
        }
    }
}