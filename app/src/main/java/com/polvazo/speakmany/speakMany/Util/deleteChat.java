package com.polvazo.speakmany.speakMany.Util;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.polvazo.speakmany.speakMany.constantes.constantes;

/**
 * Created by USUARIO on 05/09/2017.
 */

public class deleteChat {

    public static void EliminarDestroy(DatabaseReference databaseReference, String roomDelete, String roomDelete2) {
        Log.i("valorDataReference", "" + databaseReference);
        if (databaseReference != null) {
            databaseReference.child(constantes.SALA_CHAT_OCUPADO).child(roomDelete2).removeValue();
            databaseReference.child(constantes.SALA_CHAT_DISPONIBLE).child(roomDelete).removeValue();

        }
    }



    public static void eliminarDisponibilidadSala(DatabaseReference databaseReference, String roomDelete) {
        Log.i("valorDataReference", "" + databaseReference);
        if (databaseReference != null) {
            databaseReference.child(constantes.SALA_CHAT_DISPONIBLE).child(roomDelete).removeValue();
        }

    }

    public static void eliminarDisponibilidadSalaOcupada(DatabaseReference databaseReference, String roomDelete) {
        Log.i("valorDataReference", "" + databaseReference);
        if (databaseReference != null) {
            databaseReference.child(constantes.SALA_CHAT_OCUPADO).child(roomDelete).removeValue();
        }

    }

}
