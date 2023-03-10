package com.aplicacion.incidencias.view;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.aplicacion.incidencias.databinding.FragmentConsultaBinding;
import com.aplicacion.incidencias.model.Incidencias;
import com.aplicacion.incidencias.utils.AdapterListIncidentes;
import com.aplicacion.incidencias.utils.IncidenciasDB;
import com.aplicacion.incidencias.utils.SqliteHelper;
import com.aplicacion.incidencias.viewModel.ConsultaViewModel;

import java.util.ArrayList;
import java.util.List;

public class ConsultaFragment extends Fragment {

    public ConsultaViewModel consultaViewModel;
    public FragmentConsultaBinding binding;
    public AdapterListIncidentes adapterListInci;
    public ArrayList<Incidencias> lstIncidencias = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentConsultaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        consultaViewModel = new ViewModelProvider(this).get(ConsultaViewModel.class);

        consultaViewModel.getIncidenciasMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Incidencias>() {
            @Override
            public void onChanged(Incidencias incidencias) {

            }
        });
        consultaDatos();
        if(lstIncidencias != null && !lstIncidencias.isEmpty()){
            adapterListInci = new AdapterListIncidentes(getActivity(), lstIncidencias);
            binding.listview.setAdapter(adapterListInci);
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @SuppressLint("Range")
    private void consultaDatos(){
        Cursor cursor = null;
        SqliteHelper sql = new SqliteHelper(getActivity());
        SQLiteDatabase bd = sql.getWritableDatabase();
        try {
            cursor = bd.rawQuery(" SELECT * from " + IncidenciasDB.IncidenciasEntidad.TABLA, null);
            lstIncidencias.clear();
            while (cursor.moveToNext()) {
                Incidencias obj = new Incidencias();
                obj.setDescripcion(cursor.getString(cursor.getColumnIndex(IncidenciasDB.IncidenciasEntidad.DESCRIPCION)));
                obj.setImagen(cursor.getString(cursor.getColumnIndex(IncidenciasDB.IncidenciasEntidad.IMAGEN)));
                lstIncidencias.add(obj);
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "consultaDatosSqLite: " + e.toString(), Toast.LENGTH_LONG).show();
        } finally {
            bd.close();
        }
    }
}