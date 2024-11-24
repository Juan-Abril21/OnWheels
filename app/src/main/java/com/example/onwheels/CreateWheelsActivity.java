package com.example.onwheels;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apachat.loadingbutton.core.customViews.CircularProgressButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CreateWheelsActivity extends AppCompatActivity {
    private static final String API_URL = "https://api.opencagedata.com/geocode/v1/json";
    private static final String API_KEY = "8dde3b66dd884fb8907db581f0849c24";

    private EditText etSearch, etSearchDestination;
    private CircularProgressButton btnSearch, btnSearchDestination;
    private RecyclerView rvPlaces, rvPlacesDestination;
    private PlaceAdapter placeAdapter, placeAdapterDestination;
    private List<Place> placeList = new ArrayList<>();
    private List<Place> placeListDestination = new ArrayList<>();
    private Button previous_button;
    private EditText placa_edit_text;
    private NumberPicker cupos_number_picker;
    private DatePickerDialog datePickerDialog;
    private Button date_picker_button;
    private String selectedPlace = "";
    private String selectedDestination = "";
    private Button timeButton;
    int hour, minute;
    private CircularProgressButton create_wheels_button;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wheels);
        initDatePicker();
        date_picker_button = findViewById(R.id.date_picker_button);
        date_picker_button.setText(getTodaysDate());
        previous_button = findViewById(R.id.previous_button);
        placa_edit_text = findViewById(R.id.placa_edit_text);
        etSearch = findViewById(R.id.etSearch);
        etSearchDestination = findViewById(R.id.etSearchDestination);
        btnSearch = findViewById(R.id.btnSearch);
        btnSearchDestination = findViewById(R.id.btnSearchDestination);
        rvPlaces = findViewById(R.id.rvPlaces);
        rvPlacesDestination = findViewById(R.id.rvPlacesDestination);
        timeButton = findViewById(R.id.time_button);
        create_wheels_button = findViewById(R.id.create_wheels_button);
        cupos_number_picker = findViewById(R.id.cupos_number_picker);
        cupos_number_picker.setMinValue(1);
        cupos_number_picker.setMaxValue(6);
        cupos_number_picker.setWrapSelectorWheel(true);
        profile_image = findViewById(R.id.profile_image);
        profile_image.setOnClickListener(view -> {
            startActivity(new Intent(CreateWheelsActivity.this, AccountInfoActivity.class));
        });
        previous_button.setOnClickListener(view -> {
            startActivity(new Intent(CreateWheelsActivity.this, HomeActivity.class));
        });
        placa_edit_text.setFilters(new InputFilter[]{
                new InputFilter.AllCaps(),
                new InputFilter.LengthFilter(6)
        });
        rvPlaces.setLayoutManager(new LinearLayoutManager(this));
        rvPlacesDestination.setLayoutManager(new LinearLayoutManager(this));
        placeAdapter = new PlaceAdapter(placeList);
        placeAdapterDestination = new PlaceAdapter(placeListDestination);
        placeAdapter.setOnPlaceClickListener(new PlaceAdapter.OnPlaceClickListener() {
            @Override
            public void onPlaceClick(Place place) {
                selectedPlace = place.getFormatted();
                etSearch.setText(selectedPlace);
                placeList.clear();
                placeAdapter.notifyDataSetChanged();
            }
        });
        placeAdapterDestination.setOnPlaceClickListener(new PlaceAdapter.OnPlaceClickListener() {
            @Override
            public void onPlaceClick(Place place) {
                selectedDestination = place.getFormatted();
                etSearchDestination.setText(selectedDestination);
                placeListDestination.clear();
                placeAdapterDestination.notifyDataSetChanged();
            }
        });
        rvPlaces.setAdapter(placeAdapter);
        rvPlacesDestination.setAdapter(placeAdapterDestination);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnSearch.startAnimation();
                        String query = etSearch.getText().toString();
                        if (!query.isEmpty()) {
                            searchPlaces(query, true);
                        } else {
                            btnSearch.revertAnimation();
                            Toast.makeText(CreateWheelsActivity.this, "Por favor, ingresa un lugar", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 1);

            }
        });
        btnSearchDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnSearchDestination.startAnimation();
                        String query = etSearchDestination.getText().toString();
                        if (!query.isEmpty()) {
                            searchPlaces(query, false);
                        } else {
                            btnSearchDestination.revertAnimation();
                            Toast.makeText(CreateWheelsActivity.this, "Por favor, ingresa un lugar", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 1);
            }
        });
        create_wheels_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placa = placa_edit_text.getText().toString();
                String fecha = date_picker_button.getText().toString();
                String hora = timeButton.getText().toString();
                String puntoInicio = etSearch.getText().toString();
                String puntoLlegada = etSearchDestination.getText().toString();
                int cupos = cupos_number_picker.getValue();
                Intent intent = getIntent();
                String usuario = SessionManager.getInstance(CreateWheelsActivity.this).getUsername();

                if (!placa.isEmpty() && placa.length() == 6) {
                    if (!fecha.isEmpty()) {
                        if (!hora.isEmpty()) {
                            if (!puntoInicio.isEmpty()) {
                                if (!puntoLlegada.isEmpty()) {
                                    if (!cupos_number_picker.toString().isEmpty()) {
                                        create_wheels_button.startAnimation();
                                        Wheels ruta = new Wheels(placa, fecha, hora, puntoInicio, puntoLlegada, usuario, cupos);
                                        db.collection("wheels").add(ruta);
                                        intent = new Intent(CreateWheelsActivity.this, InfoCreationActivity.class);
                                        intent.putExtra("placa", placa);
                                        intent.putExtra("fecha", fecha);
                                        intent.putExtra("hora", hora);
                                        intent.putExtra("puntoInicio", puntoInicio);
                                        intent.putExtra("puntoLlegada", puntoLlegada);
                                        intent.putExtra("cupos", cupos);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(CreateWheelsActivity.this, "Por favor, ingresa un número de cupos", Toast.LENGTH_SHORT).show();
                                        create_wheels_button.revertAnimation();
                                    }
                                } else {
                                    Toast.makeText(CreateWheelsActivity.this, "Por favor, ingresa un punto de llegada", Toast.LENGTH_SHORT).show();
                                    create_wheels_button.revertAnimation();
                                }
                            } else {
                                Toast.makeText(CreateWheelsActivity.this, "Por favor, ingresa un punto de inicio", Toast.LENGTH_SHORT).show();
                                create_wheels_button.revertAnimation();
                            }
                        } else {
                            Toast.makeText(CreateWheelsActivity.this, "Por favor, ingresa una hora", Toast.LENGTH_SHORT).show();
                            create_wheels_button.revertAnimation();
                        }
                    } else {
                        Toast.makeText(CreateWheelsActivity.this, "Por favor, ingresa una fecha", Toast.LENGTH_SHORT).show();
                        create_wheels_button.revertAnimation();
                    }
                } else {
                    Toast.makeText(CreateWheelsActivity.this, "Por favor, ingresa una placa valida", Toast.LENGTH_SHORT).show();
                    create_wheels_button.revertAnimation();
                }
            }
        });
    }

    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minuteOfHour) {
                hour = hourOfDay;
                minute = minuteOfHour;
                String amPm = (hourOfDay >= 12) ? "PM" : "AM";
                if (hourOfDay > 12) {
                    hourOfDay -= 12;
                } else if (hourOfDay == 0) {
                    hourOfDay = 12;
                }
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d %s", hourOfDay, minuteOfHour, amPm));
            }
        };
        int style = AlertDialog.THEME_HOLO_DARK;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minute, false);
        timePickerDialog.setTitle("Selecciona la hora");
        timePickerDialog.show();
    }

    private void searchPlaces(String query, boolean isStart) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(API_URL).newBuilder();
        urlBuilder.addQueryParameter("q", query);
        urlBuilder.addQueryParameter("key", API_KEY);
        urlBuilder.addQueryParameter("limit", "10");
        urlBuilder.addQueryParameter("language", "es");

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> Toast.makeText(CreateWheelsActivity.this, "Error al buscar lugares", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    parsePlaces(jsonResponse, isStart);
                } else {
                    runOnUiThread(() -> {
                        btnSearch.revertAnimation();
                        btnSearchDestination.revertAnimation();
                        Toast.makeText(CreateWheelsActivity.this, "Error en la respuesta", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    private void parsePlaces(String jsonResponse, boolean isStart) {
        Gson gson = new Gson();
        OpenCageResponse openCageResponse = gson.fromJson(jsonResponse, OpenCageResponse.class);

        runOnUiThread(() -> {
            if (isStart) {
                placeList.clear();
                placeList.addAll(openCageResponse.getResults());
                placeAdapter.notifyDataSetChanged();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnSearch.revertAnimation();
                    }
                }, 1);
            } else {
                placeListDestination.clear();
                placeListDestination.addAll(openCageResponse.getResults());
                placeAdapterDestination.notifyDataSetChanged();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnSearchDestination.revertAnimation();
                    }
                }, 1);
            }
        });
    }

    public static class OpenCageResponse {
        private List<Place> results;

        public List<Place> getResults() {
            return results;
        }

        public void setResults(List<Place> results) {
            this.results = results;
        }
    }

    private String getTodaysDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month = month + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                date_picker_button.setText(date);
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + "/" + day + "/" + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1)
            return "ENE";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "ABR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AGO";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DIC";
        return "ENE";
    }

    public void openDatePicker(android.view.View view) {
        datePickerDialog.show();
    }

    public class Place {
        private String formatted;

        public String getFormatted() {
            return formatted;
        }

        public void setFormatted(String formatted) {
            this.formatted = formatted;
        }
    }
}