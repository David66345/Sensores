package com.example.a20300663_sensores;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private CircularProgressBar circularProgressBar;
    private TextView centerTextView;

    private int lastMeasurement = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        // Obtener el tipo de sensor desde el intent
        int sensorType = getIntent().getIntExtra("sensorType", -1);

        // Configurar el título con el nombre del sensor
        TextView titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(getSensorName(sensorType));

        // Obtener la CircularProgressBar
        circularProgressBar = findViewById(R.id.circularProgressBar);

        // Obtener el TextView central
        centerTextView = findViewById(R.id.centerTextView);

        // Inicializar el SensorManager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Obtener el sensor específico
        sensor = sensorManager.getDefaultSensor(sensorType);

        // Registrar el listener para recibir actualizaciones del sensor
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.values.length > 0) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    handleAccelerometerSensor(event);
                    break;
                case Sensor.TYPE_PROXIMITY:
                    handleProximitySensor(event);
                    break;
                case 65560:
                    handleGripSensor(event);
                    break;
                case Sensor.TYPE_SIGNIFICANT_MOTION:
                    handleSignificantMotionSensor(event);
                    break;
                case 22:
                    handleTiltDetectorSensor(event);
                    break;
                case 65604:
                    handleCameraLightSensor(event);
                    break;
                case 27:
                    handleScreenOrientationSensor(event);
                    break;
                case 65559:
                    handleMotionSensor(event);
                    break;
                default:
                    // Otros tipos de sensores
                    break;
            }
        }
    }

    private void handleAccelerometerSensor(SensorEvent event) {
        // Lógica de cálculo para el sensor de aceleración
        float xValue = event.values[0];
        float yValue = event.values.length > 1 ? event.values[1] : 0;
        float zValue = event.values.length > 2 ? event.values[2] : 0;
        int measurement = (int) Math.sqrt(xValue * xValue + yValue * yValue + zValue * zValue);

        // Actualizar el TextView central con el valor de medición
        centerTextView.setText(String.valueOf(measurement));

        // Actualizar la CircularProgressBar con el nuevo valor de medición
        updateProgressBar(measurement);
    }

    private void handleProximitySensor(SensorEvent event) {
        // Lógica de cálculo para el sensor de proximidad
        float proximityValue = event.values[0];

        // Actualizar el TextView central con el valor de medición
        centerTextView.setText(String.valueOf(proximityValue));

        // Actualizar la CircularProgressBar con el nuevo valor de medición
        updateProgressBar((int) proximityValue); // Adaptar según la escala de medición del sensor de proximidad
    }

    private void handleGripSensor(SensorEvent event) {
        // Lógica de cálculo para el sensor de agarre
        float gripValue = event.values[0];

        // Actualizar el TextView central con el valor de medición
        centerTextView.setText(String.valueOf(gripValue));

        // Actualizar la CircularProgressBar con el nuevo valor de medición
        updateProgressBar((int) gripValue); // Adaptar según la escala de medición del sensor de agarre
    }

    private void handleSignificantMotionSensor(SensorEvent event) {
        centerTextView.setText("¡Movimiento significativo detectado!");

        updateProgressBar(100);
    }

    private void handleTiltDetectorSensor(SensorEvent event) {
        // Lógica de cálculo para el Tilt Detector
        float tiltValue = event.values[0];

        // Ejemplo: mostrar el ángulo de inclinación en el TextView central
        centerTextView.setText("Ángulo de inclinación: " + tiltValue);

        // Ejemplo: actualizar la CircularProgressBar con un valor específico
        updateProgressBar((int) tiltValue); // Adaptar según la escala de medición
    }

    private void handleCameraLightSensor(SensorEvent event) {
        // Lógica de cálculo para el Camera Light Sensor
        float lightValue = event.values[0];

        // Ejemplo: mostrar la intensidad de la luz en el TextView central
        centerTextView.setText("Intensidad de luz: " + lightValue);

        // Ejemplo: actualizar la CircularProgressBar con un valor específico
        updateProgressBar((int) lightValue); // Adaptar según la escala de medición
    }

    private void handleScreenOrientationSensor(SensorEvent event) {
        // Lógica de cálculo para el Screen Orientation Sensor
        float orientationValue = event.values[0];

        // Ejemplo: mostrar la orientación en el TextView central
        centerTextView.setText("Orientación del dispositivo: " + orientationValue);

        // Ejemplo: actualizar la CircularProgressBar con un valor específico
        updateProgressBar((int) orientationValue); // Adaptar según la escala de medición
    }

    private void handleMotionSensor(SensorEvent event) {
        // Lógica de cálculo para el Motion Sensor
        float motionValue = event.values[0];

        // Ejemplo: mostrar el nivel de movimiento en el TextView central
        centerTextView.setText("Nivel de movimiento: " + motionValue);

        // Ejemplo: actualizar la CircularProgressBar con un valor específico
        updateProgressBar((int) motionValue); // Adaptar según la escala de medición
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No es necesario implementar esto para este ejemplo
    }

    // Método para actualizar la CircularProgressBar con el nuevo valor de medición
    private void updateProgressBar(final int progress) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                circularProgressBar.setProgress(progress);
                lastMeasurement = progress; // Guardar el último valor de medición
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Detener la actualización del sensor cuando la actividad esté en pausa
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reanudar la actualización del sensor cuando la actividad se reanude
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            // Restaurar el último valor de medición
            updateProgressBar(lastMeasurement);
        }
    }

    // Método para obtener el nombre del sensor según su tipo
    private String getSensorName(int sensorType) {
        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                return "Acelerómetro";
            case Sensor.TYPE_PROXIMITY:
                return "Sensor de Proximidad";
            case 65560:
                return "Sensor de Agarre";
            case Sensor.TYPE_SIGNIFICANT_MOTION:
                return "Detector de Movimiento Significativo";
            case 22:
                return "Detector de Inclinación";
            case 65604:
                return "Sensor de Luz de la Cámara";
            case 27:
                return "Sensor de Rotación";
            case 65559:
                return "Sensor de Movimiento";
            default:
                return "Sensor Desconocido";
        }
    }
}
