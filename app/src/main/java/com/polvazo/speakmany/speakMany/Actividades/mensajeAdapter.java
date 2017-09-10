package com.polvazo.speakmany.speakMany.Actividades;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.polvazo.speakmany.R;
import com.polvazo.speakmany.speakMany.Modelos.mensaje;

import java.util.List;

class mensajeAdapter extends RecyclerView.Adapter<mensajeAdapter.ViewHolder> {
    private static final int CHAT_END = 1;
    private static final int CHAT_START = 2;

    private List<mensaje> mDataSet;
    private String mId;


    mensajeAdapter(List<mensaje> dataSet, String id) {
        mDataSet = dataSet;
        mId = id;
    }

    @Override
    public mensajeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if (viewType == CHAT_END) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recibido, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_inicio, parent, false);
        }

        return new ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataSet.get(position).getmUsuario().equals(mId)) {
            return CHAT_END;
        }

        return CHAT_START;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mensaje chat = mDataSet.get(position);
        holder.mTextView.setText(chat.getmTexto());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    /**
     * Inner Class for a recycler view
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        ViewHolder(View v) {
            super(v);
            mTextView = (TextView) itemView.findViewById(R.id.tvMessage);
        }
    }
}