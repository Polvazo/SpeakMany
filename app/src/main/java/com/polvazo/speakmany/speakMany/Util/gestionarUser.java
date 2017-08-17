package com.polvazo.speakmany.speakMany.Util;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.polvazo.speakmany.speakMany.Modelos.user;
import com.polvazo.speakmany.speakMany.constantes.constantes;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by USUARIO on 31/07/2017.
 */

public class gestionarUser {


    private static DatabaseReference mDatabase;
    private static DatabaseReference mUsuario;


    public static void crearUsuarioConectado(Context c) {



        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUsuario = mDatabase.child(constantes.USUARIOS_CONECTADOS);
        Map<String, user> usuarios = new HashMap<String, user>();
        String obtenerusuario = preferencia.obtener(constantes.IDUSUARIO_CONECTADO,c);
        usuarios.put( obtenerusuario, new user(obtenerusuario));
        mUsuario.setValue(usuarios);

        //String obtenerusuario = preferencia.obtener(constantes.IDUSUARIO_CONECTADO,c);
        //DatabaseReference reference =  mDatabase.child(constantes.USUARIOS_CONECTADOS).push().setValue(obtenerusuario);
        //String key = reference.getKey();
        //reference.setValue(key);
        //preferencia.Guardar(constantes.IDUSUARIO_CONECTADO, key, c);

    }

    public static void crearUsuarioChateando(Context context){

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("usuariosConectados").child(preferencia.obtener(constantes.IDUSUARIO_CONECTADO, context)).removeValue();
        DatabaseReference chateando =     mDatabase.child("UsuariosChateando").push();
        String keyChateando = chateando.getKey();
        chateando.setValue(keyChateando);
        preferencia.Guardar(constantes.IDUSUARIO_CHATEANDO, keyChateando, context);


    }
}
