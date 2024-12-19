package com.cassandra.ubicacion;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {

    //cliente de proveedor de ubicacion coordinada
    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView locationTv;

    private static final int REQUEST_CODE_LOCATION_PERMISSION =1;

    //se obtienen los permisos
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState); //contiene el estado guardado de la actividad
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main); //parte de la interfaz de usuario

        //asocia locacion a la interfaz
        locationTv = findViewById(R.id.locationTv);
        //Inicializa un cliente para obtener ubicaciones utilizando geolocalizacion de google
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        //verificamos si tenemos los permisos,sino los tenemos los solicitamos
        getCurrentLocation();
    }

    private void getCurrentLocation(){
        //verificamos los permisos
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            //sino tenemos este permiso, entonces lo solicitamos
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION
            );
            return;
        }

        //trae la ultima posicion guardada
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
            if(location != null){
                //si trae informacion del dispositivo
                locationTv.setText(
                        "Latitud: " + location.getLatitude() + "\n" +
                                "Longuitud: "+ location.getLongitude()
                );

            }else{
                locationTv.setText("No se pudo obtener la ubicacion");
            }
        });
    }

    //Respuesta cuando el usuario valida o deniega los permisos
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //el permiso fue concedido
                getCurrentLocation();
            }else{
                locationTv.setText("Permiso de ubicacion denegado");
            }

        }
    }
}