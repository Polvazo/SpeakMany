package com.polvazo.speakmany.speakMany.Util;

import android.content.Context;
import android.content.SharedPreferences;


public class preferencia {
    private static final String PREFFERS_NAME = "usuarioAndroid";


    public static void Guardar(String clave, String valor, Context context){

        SharedPreferences prefs = context.getSharedPreferences(PREFFERS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(clave, valor);
        editor.commit();
    }

    public static String obtener (String clave, Context context){

        SharedPreferences prefs = context.getSharedPreferences(PREFFERS_NAME, Context.MODE_PRIVATE);
        String valor = prefs.getString(clave, null);
        return valor;

    }

}
