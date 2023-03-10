package com.aplicacion.incidencias.utils;

import android.provider.BaseColumns;

public class IncidenciasDB {

    public static abstract class IncidenciasEntidad implements BaseColumns {
        public static final String TABLA ="rep_incidencias";

        public static final String DESCRIPCION = "descripcion";
        public static final String IMAGEN = "imagen";
        }
}
