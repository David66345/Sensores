package com.example.a20300663_sensores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Sensor> sensorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtener el servicio del sensor
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // Obtener la lista de sensores
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        // Crear una lista de nombres de sensores
        List<String> sensorNames = new ArrayList<>();
        for (Sensor sensor : sensorList) {
            sensorNames.add(sensor.getName());
            Log.d("SENSOR_INFO", "Nombre: " + sensor.getName() +
                    ", Tipo: " + sensor.getType() +
                    ", Fabricante: " + sensor.getVendor());
        }

        // Configurar el adaptador para la ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sensorNames);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // Configurar el clic en el elemento de la lista
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openSensorActivity(sensorList.get(position).getType()); // Pasar el tipo de sensor
            }
        });
    }

    private void openSensorActivity(int sensorType) {
        // Crear un intent para pasar a la nueva actividad
        Intent intent = new Intent(this, SensorActivity.class);
        // Pasar el tipo de sensor como extra
        intent.putExtra("sensorType", sensorType);
        // Iniciar la nueva actividad
        startActivity(intent);
    }
}