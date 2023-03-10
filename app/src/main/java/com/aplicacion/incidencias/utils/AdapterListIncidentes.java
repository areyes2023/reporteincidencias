package com.aplicacion.incidencias.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aplicacion.incidencias.model.Incidencias;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import com.aplicacion.incidencias.R;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class AdapterListIncidentes extends BaseAdapter {
    private Context context;
    private ArrayList<Incidencias> dataModelArrayList;

    public AdapterListIncidentes(Context context, ArrayList<Incidencias> dataModelArrayList) {
        this.context = context;
        this.dataModelArrayList = dataModelArrayList;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return dataModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lista_incidentes, null, true);
            holder.iv_imagen = (ImageView) convertView.findViewById(R.id.iv_imagen);
            holder.tv_nombres = (TextView) convertView.findViewById(R.id.lb_modulo);
            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }
        //Picasso.get().load(dataModelArrayList.get(position).getImagen()).into(holder.iv_imagen);
        Glide.with(context).load(dataModelArrayList.get(position).getImagen()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_imagen);
        holder.tv_nombres.setText(dataModelArrayList.get(position).getDescripcion());
        return convertView;
    }

    private class ViewHolder {
        protected TextView tv_nombres;
        protected ImageView iv_imagen;
    }
}
