package com.polvazo.speakmany.speakMany.Util;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.polvazo.speakmany.speakMany.constantes.constantes;

/**
 * Created by USUARIO on 31/07/2017.
 */

public class gestionarUser {


    private static DatabaseReference mDatabase;



    public static void crearUsuarioConectado(Context c) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference reference =  mDatabase.child("usuariosConectados").push();
        String key = reference.getKey();
        reference.setValue(key);
        preferencia.Guardar(constantes.IDUSUARIO_CONECTADO, key, c);

    }

    public static void crearUsuarioChateando(Context context){

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("usuariosConectados").child(preferencia.obtener(constantes.IDUSUARIO_CONECTADO, context)).removeValue();
        DatabaseReference chateando =  mDatabase.child("UsuariosChateando").push();
        String keyChateando = chateando.getKey();
        chateando.setValue(keyChateando);
        preferencia.Guardar(constantes.IDUSUARIO_CHATEANDO, keyChateando, context);


    }
}
