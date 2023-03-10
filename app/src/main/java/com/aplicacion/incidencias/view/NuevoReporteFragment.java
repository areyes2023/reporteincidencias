package com.aplicacion.incidencias.view;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.BuildConfig;

import com.aplicacion.incidencias.databinding.FragmentNuevoreporteBinding;
import com.aplicacion.incidencias.utils.IncidenciasDB;
import com.aplicacion.incidencias.utils.SqliteHelper;
import com.aplicacion.incidencias.viewModel.NuevoReporteViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aplicacion.incidencias.R;

public class NuevoReporteFragment extends Fragment {

    private NuevoReporteViewModel nuevoReporteViewModel;
    private FragmentNuevoreporteBinding binding;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PHOTO_SELECTED = 2;
    String currentPhotoPath;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        nuevoReporteViewModel = new ViewModelProvider(this).get(NuevoReporteViewModel.class);

        binding = FragmentNuevoreporteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        nuevoReporteViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        binding.btnCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(valida()){
                    guardarImagen();
                }
            }
        });

        binding.btnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirGaleria();
            }
        });

        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(valida()){
                    guardarDatosSQLite();
                }
            }
        });

        binding.btnlimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.edtDescripcion.setText("");
                binding.imageView.setImageBitmap(null);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /*private void abrirCamara() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }*/

    private boolean valida(){
        if(binding.edtDescripcion.getText().toString().trim().length() == 0){
            Toast.makeText(getContext(), "Debe ingresar la descripción." , Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void abrirGaleria(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_SELECTED);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
                galleryAddPic();
                File f = new File(currentPhotoPath);
                Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                binding.imageView.setImageBitmap(myBitmap);

            }else if (requestCode == PHOTO_SELECTED && resultCode == getActivity().RESULT_OK) {
                Uri selectedImage = data.getData();
                InputStream imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                Bitmap bmp = BitmapFactory.decodeStream(imageStream);
                binding.imageView.setImageBitmap(bmp);
                currentPhotoPath = data.getData().getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void guardarImagen() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(), "com.aplicacion.incidencias.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    private void guardarDatosSQLite(){
        SqliteHelper sql = new SqliteHelper(getActivity());
        SQLiteDatabase bd = sql.getWritableDatabase();
        Long datosInsertados = new Long(0);
        try {
            ContentValues datos = new ContentValues();
            datos.put(IncidenciasDB.IncidenciasEntidad.DESCRIPCION, binding.edtDescripcion.getText().toString());
            datos.put(IncidenciasDB.IncidenciasEntidad.IMAGEN, currentPhotoPath);
            datosInsertados = bd.insert(IncidenciasDB.IncidenciasEntidad.TABLA, null, datos);
        } catch (Exception e) {
            Toast.makeText(getContext(), "guardarDatosSQLite: " + e.toString(), Toast.LENGTH_LONG).show();
        } finally {
            bd.close();
        }
        if(datosInsertados == new Long(-1) ){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(Html.fromHtml("<font color='#FF0000'><b>Error en Registro de datos</b></font>"));
            builder.setIcon(R.drawable.ic_close_24);
            builder.setMessage("No se registraron los datos correctamente.");
            builder.setCancelable(false);
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }else{
            binding.edtDescripcion.setText("");
            binding.imageView.setImageBitmap(null);
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(Html.fromHtml("<font color='#FF0000'><b>Registro de datos</b></font>"));
            builder.setIcon(R.drawable.ic_check_24);
            builder.setMessage("Transaccion registrada con exito!!!");
            builder.setCancelable(false);
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }
}