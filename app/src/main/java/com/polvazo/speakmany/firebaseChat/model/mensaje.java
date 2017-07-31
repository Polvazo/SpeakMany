package com.polvazo.speakmany.firebaseChat.model;

import java.util.Date;

/**
 * Created by USUARIO on 31/07/2017.
 */

public class mensaje {
    private String mTexto;
    private String mUsuario;
    private Date date;

    public mensaje(String mUsuario){
        this.mUsuario=mUsuario;
    }

    public String getmTexto() {
        return mTexto;
    }

    public void setmTexto(String mTexto) {
        this.mTexto = mTexto;
    }

    public String getmUsuario() {
        return mUsuario;
    }

    public void setmUsuario(String mUsuario) {
        this.mUsuario = mUsuario;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
