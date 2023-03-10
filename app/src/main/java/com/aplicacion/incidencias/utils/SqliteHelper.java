package com.aplicacion.incidencias.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;
    public static final String BASE_DATOS = "incidencias.db";

    public SqliteHelper(Context context) {
        super(context, BASE_DATOS, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + IncidenciasDB.IncidenciasEntidad.TABLA + " ("
                + IncidenciasDB.IncidenciasEntidad.DESCRIPCION + " TEXT,"
                + IncidenciasDB.IncidenciasEntidad.IMAGEN + " TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + IncidenciasDB.IncidenciasEntidad.TABLA);
        onCreate(db);
    }
}
