package com.aplicacion.incidencias.viewModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aplicacion.incidencias.model.Incidencias;
import com.aplicacion.incidencias.model.Login;
import com.aplicacion.incidencias.utils.AdapterListIncidentes;
import com.aplicacion.incidencias.utils.IncidenciasDB;
import com.aplicacion.incidencias.utils.SqliteHelper;

import java.util.ArrayList;

public class ConsultaViewModel extends ViewModel {

    private MutableLiveData<Incidencias> incidenciasMutableLiveData;

    public MutableLiveData<Incidencias> getIncidenciasMutableLiveData() {
        if (incidenciasMutableLiveData == null) {
            incidenciasMutableLiveData = new MutableLiveData<>();
        }
        return incidenciasMutableLiveData;
    }

    @SuppressLint("Range")
    public AdapterListIncidentes consultaDatos(Context c) {
        Cursor cursor = null;
        SqliteHelper sql = new SqliteHelper(c);
        SQLiteDatabase bd = sql.getWritableDatabase();
        AdapterListIncidentes adapterMutableLiveData = null;
        try {
            cursor = bd.rawQuery(" SELECT * from " + IncidenciasDB.IncidenciasEntidad.TABLA, null);
            ArrayList<Incidencias> lstIncidencias = new ArrayList<>();
            while (cursor.moveToNext()) {
                Incidencias obj = new Incidencias();
                obj.setDescripcion(cursor.getString(cursor.getColumnIndex(IncidenciasDB.IncidenciasEntidad.DESCRIPCION)));
                obj.setImagen(cursor.getString(cursor.getColumnIndex(IncidenciasDB.IncidenciasEntidad.IMAGEN)));
                lstIncidencias.add(obj);
            }
            adapterMutableLiveData = new AdapterListIncidentes(c, lstIncidencias);
        }catch (Exception e){
            e.printStackTrace();
        }
        return adapterMutableLiveData;
    }
}