package com.example.weatherapps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherapps.R;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class RetrofitActivity extends AppCompatActivity {
    private TextView tvTemperature, tvKeterangan, tvWindspeed, tvLongitude, tvLatitude, tvLibrary, tvTanggalSekarang;
    private TextView tvTanggal1, tvTanggal2, tvTanggal3, tvTanggal4, tvTanggal5, tvTanggal6;
    private TextView tvKeterangan1, tvKeterangan2, tvKeterangan3, tvKeterangan4, tvKeterangan5, tvKeterangan6;
    private ImageView ramalan1, ramalan2, ramalan3, ramalan4, ramalan5, ramalan6;
    private ImageView iconCuaca;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        tvTemperature = findViewById(R.id.tvCuaca);
        tvKeterangan = findViewById(R.id.tvKeterangan);
        tvWindspeed = findViewById(R.id.tvWindSpeed);
        tvLatitude = findViewById(R.id.tvLatitude);
        tvLongitude = findViewById(R.id.tvLongitude);
        iconCuaca = findViewById(R.id.imgCuaca);
        tvTanggalSekarang = findViewById(R.id.tvTglNow);
        tvTanggal1 = findViewById(R.id.tvTanggal1);
        tvTanggal2 = findViewById(R.id.tvTanggal2);
        tvTanggal3 = findViewById(R.id.tvTanggal3);
        tvTanggal4 = findViewById(R.id.tvTanggal4);
        tvTanggal5 = findViewById(R.id.tvTanggal5);
        tvTanggal6 = findViewById(R.id.tvTanggal6);
        ramalan1 = findViewById(R.id.ramalan1);
        ramalan2 = findViewById(R.id.ramalan2);
        ramalan3 = findViewById(R.id.ramalan3);
        ramalan4 = findViewById(R.id.ramalan4);
        ramalan5 = findViewById(R.id.ramalan5);
        ramalan6 = findViewById(R.id.ramalan6);
        tvKeterangan1 = findViewById(R.id.tvKeterangan1);
        tvKeterangan2 = findViewById(R.id.tvKeterangan2);
        tvKeterangan3 = findViewById(R.id.tvKeterangan3);
        tvKeterangan4 = findViewById(R.id.tvKeterangan4);
        tvKeterangan5 = findViewById(R.id.tvKeterangan5);
        tvKeterangan6 = findViewById(R.id.tvKeterangan6);

        getWeather();
    }
    public class WeatherData {

        @SerializedName("current_weather")
        private CurrentWeather currentweather;
        @SerializedName("daily")
        private Daily daily;
        private String latitude, windspeed;
        private String longitude;
        private int[] weathercode;

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude () {
            return longitude;
        }

        public String getWindspeed(){
            return windspeed;
        }

        public CurrentWeather getCurrent_weather() {
            return currentweather;
        }

        public Daily getDaily() {
            return daily;
        }

    }
    public class Daily {
        @SerializedName("time")
        private List<String> time;

        @SerializedName("weathercode")
        private List<String> wheatercode;
        public List<String> getTime() {
            return time;
        }

        public List<String> getWheatercode() {
            return wheatercode;
        }
    }
    public class CurrentWeather {
        @SerializedName("windspeed")
        private String windspeed;

        @SerializedName("temperature")
        private String temperature;

        @SerializedName("weathercode")
        private String weathercode;

        public String getWindspeed() {
            return windspeed;
        }

        public String getTemperature() {
            return temperature;
        }

        public String getWeathercode() {
            return weathercode;
        }
    }
    public interface WeatherService {
        @GET("forecast")
        Call<WeatherData> getWeatherData(@Query("latitude") double latitude, @Query("longitude") double longitude,
                                         @Query("daily") String daily, @Query("current_weather") boolean currentWeather,
                                         @Query("timezone") String timezone);
    }

    public void getWeather(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.open-meteo.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();



        WeatherService service = retrofit.create(WeatherService.class);
        double latitude = -7.98;
        double longitude = 112.63;
        String daily = "weathercode";
        boolean currentWeather = true;
        String timezone = "auto";
        Call<WeatherData> call = service.getWeatherData(latitude, longitude, daily, currentWeather, timezone);

        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (response.isSuccessful()) {
                    WeatherData weatherData = response.body();
                    tvLatitude.setText(response.body().getLatitude());
                    tvLongitude.setText(response.body().getLongitude());
                    tvWindspeed.setText(response.body().getCurrent_weather().getWindspeed() + " knot");
                    tvTemperature.setText(response.body().getCurrent_weather().getTemperature() + "\u00B0" + "C");
                    tvTanggalSekarang.setText(response.body().getDaily().getTime().get(0));
                    handleIcon(response.body().getCurrent_weather().getWeathercode(),0);

                    for (int i = 1; i <= 6; i++) {
                        String code = response.body().getDaily().getWheatercode().get(i);
                        handleIcon(code, i);
                        String time = response.body().getDaily().getTime().get(i);
                        // Process the retrieved time as needed
                        switch (i){
                            case 1:
                                tvTanggal1.setText(time);
                            case 2:
                                tvTanggal2.setText(time);
                            case 3:
                                tvTanggal3.setText(time);
                            case 4:
                                tvTanggal4.setText(time);
                            case 5:
                                tvTanggal5.setText(time);
                            case 6:
                                tvTanggal6.setText(time);
                        }

                    }
                    tvLibrary.setText("Library by Retrofit");

                } else {
                    // Handle response error

                    tvLongitude.setText("String.valueOf(latitude)");
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                // Handle network failure

            }
        });
    }

    private void handleIcon(String code, int i){
        List<String> hujan = Arrays.asList("51", "53", "55", "56", "57", "61", "63", "65", "66", "67");
        List<String> berawan = Arrays.asList("45","48");
        List<String> badai = Arrays.asList("80", "81" , "82", "95", "96", "99");

        if (berawan.contains(code)){
            switch (i){
                case 0:
                    tvKeterangan.setText("Berawan");
                    iconCuaca.setImageResource(R.drawable.berawan);
                case 1:
                    tvKeterangan1.setText("Berawan");
                    ramalan1.setImageResource(R.drawable.berawan);
                case 2:
                    tvKeterangan2.setText("Berawan");
                    ramalan2.setImageResource(R.drawable.berawan);
                case 3:
                    tvKeterangan3.setText("Berawan");
                    ramalan3.setImageResource(R.drawable.berawan);
                case 4:
                    tvKeterangan4.setText("Berawan");
                    ramalan4.setImageResource(R.drawable.berawan);
                case 5:
                    tvKeterangan5.setText("Berawan");
                    ramalan5.setImageResource(R.drawable.berawan);
                case 6:
                    tvKeterangan6.setText("Berawan");
                    ramalan6.setImageResource(R.drawable.berawan);
            }
        } else  if (code.equals("2") || code.equals("3") ){
            switch (i){
                case 0:
                    tvKeterangan.setText("Cerah");
                    iconCuaca.setImageResource(R.drawable.cerah_berawan);
                case 1:
                    tvKeterangan1.setText("Cerah");
                    ramalan1.setImageResource(R.drawable.cerah_berawan);
                case 2:
                    tvKeterangan2.setText("Cerah ");
                    ramalan2.setImageResource(R.drawable.cerah_berawan);
                case 3:
                    tvKeterangan3.setText("Cerah ");
                    ramalan3.setImageResource(R.drawable.cerah_berawan);
                case 4:
                    tvKeterangan4.setText("Cerah ");
                    ramalan4.setImageResource(R.drawable.cerah_berawan);
                case 5:
                    tvKeterangan5.setText("Cerah ");
                    ramalan5.setImageResource(R.drawable.cerah_berawan);
                case 6:
                    tvKeterangan6.setText("Cerah ");
                    ramalan6.setImageResource(R.drawable.cerah_berawan);
            }
        } else  if (code.equals("1") || code.equals("0") ){
            switch (i){
                case 0:
                    tvKeterangan.setText("Cerah bgt");
                    iconCuaca.setImageResource(R.drawable.cerah);
                case 1:
                    tvKeterangan1.setText("Cerah bgt");
                    ramalan1.setImageResource(R.drawable.cerah);
                case 2:
                    tvKeterangan2.setText("Cerah bgt ");
                    ramalan2.setImageResource(R.drawable.cerah);
                case 3:
                    tvKeterangan3.setText("Cerah bgt ");
                    ramalan3.setImageResource(R.drawable.cerah);
                case 4:
                    tvKeterangan4.setText("Cerah bgt ");
                    ramalan4.setImageResource(R.drawable.cerah);
                case 5:
                    tvKeterangan5.setText("Cerah bgt ");
                    ramalan5.setImageResource(R.drawable.cerah);
                case 6:
                    tvKeterangan6.setText("Cerah bgt ");
                    ramalan6.setImageResource(R.drawable.cerah);
            }
        }else  if (hujan.contains(code)){
            switch (i){
                case 0:
                    tvKeterangan.setText("Hujan");
                    iconCuaca.setImageResource(R.drawable.hujan);
                case 1:
                    tvKeterangan1.setText("Hujan");
                    ramalan1.setImageResource(R.drawable.hujan);
                case 2:
                    tvKeterangan2.setText("Hujan");
                    ramalan2.setImageResource(R.drawable.hujan);
                case 3:
                    tvKeterangan3.setText("Hujan");
                    ramalan3.setImageResource(R.drawable.hujan);
                case 4:
                    tvKeterangan4.setText("Hujan");
                    ramalan4.setImageResource(R.drawable.hujan);
                case 5:
                    tvKeterangan5.setText("Hujan");
                    ramalan5.setImageResource(R.drawable.hujan);
                case 6:
                    tvKeterangan6.setText("Hujan");
                    ramalan6.setImageResource(R.drawable.hujan);
            }
        }else  if (badai.contains(code)){
            switch (i){
                case 0:
                    tvKeterangan.setText("Badai");
                    iconCuaca.setImageResource(R.drawable.badai);
                case 1:
                    tvKeterangan1.setText("Badai");
                    ramalan1.setImageResource(R.drawable.badai);
                case 2:
                    tvKeterangan2.setText("Badai");
                    ramalan2.setImageResource(R.drawable.badai);
                case 3:
                    tvKeterangan3.setText("Badai");
                    ramalan3.setImageResource(R.drawable.badai);
                case 4:
                    tvKeterangan4.setText("Badai");
                    ramalan4.setImageResource(R.drawable.badai);
                case 5:
                    tvKeterangan5.setText("Badai");
                    ramalan5.setImageResource(R.drawable.badai);
                case 6:
                    tvKeterangan6.setText("Badai");
                    ramalan6.setImageResource(R.drawable.badai);
            }
        }
    }

//        Muhammad Adam Ibrahim
//        215150700111034

}






