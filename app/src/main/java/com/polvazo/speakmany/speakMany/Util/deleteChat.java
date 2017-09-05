package com.polvazo.speakmany.speakMany.Util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.polvazo.speakmany.speakMany.constantes.constantes;

/**
 * Created by USUARIO on 05/09/2017.
 */

public class deleteChat{


    public static void eliminarDisponibilidadSala(DatabaseReference databaseReference, String roomDelete){

                databaseReference.child(constantes.SALA_CHAT_DISPONIBLE).child(roomDelete).removeValue();

}
    public static void eliminarDisponibilidadSalaOcupada(DatabaseReference databaseReference, String roomDelete){

        databaseReference.child(constantes.SALA_CHAT_OCUPADO).child(roomDelete).removeValue();

    }

   }
