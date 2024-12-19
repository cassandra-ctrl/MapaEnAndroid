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

    //cliente de proveedor de ubicacion
    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView locationTv;
    //
    private static final int REQUEST_CODE_LOCATION_PERMISSION =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        locationTv = findViewById(R.id.locationTv);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //verificamos si tenemos los permisos,sino los tenemos los solicitamos
        getCurrentLocation();


    }

    private void getCurrentLocation(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_DENIED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_DENIED){
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
                locationTv.setText(
                        "Latitud: " + location.getLatitude() + "\n" +
                                "Longuitud: "+ location.getLongitude()
                );

            }else{
                locationTv.setText("No se pudo obtener la ubicacion");
            }
        });
    }

    //Respuesta de los permisos, validacion
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }else{
                locationTv.setText("Permiso de ubicacion denegado");
            }

        }
    }
}