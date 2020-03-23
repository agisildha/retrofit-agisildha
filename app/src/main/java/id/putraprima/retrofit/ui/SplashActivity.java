package id.putraprima.retrofit.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.AppVersion;

import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    TextView lblAppName, lblAppTittle, lblAppVersion;
    //Untuk mengambil id activity_splash
    ConstraintLayout constraintLayout;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setupLayout();
        if (checkInternetConnection()) {
            checkAppVersion();
        }
        setAppInfo();
    }

    private void setupLayout() {
        lblAppName = findViewById(R.id.lblAppName);
        lblAppTittle = findViewById(R.id.lblAppTittle);
        lblAppVersion = findViewById(R.id.lblAppVersion);
        //Sembunyikan lblAppName dan lblAppVersion pada saat awal dibuka
        lblAppVersion.setVisibility(View.INVISIBLE);
        lblAppName.setVisibility(View.INVISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkInternetConnection() {
        //TODO : 1. Implementasikan proses pengecekan koneksi internet, berikan informasi ke user jika tidak terdapat koneksi internet
        if(isOnline()){
            Toast.makeText(SplashActivity.this, "You are connected to Internet", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(SplashActivity.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        }
        return true;
    }


    private void setAppInfo() {
        //TODO : 5. Implementasikan proses setting app info, app info pada fungsi ini diambil dari shared preferences
        SharedPreferences getApp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        lblAppName.setVisibility(View.VISIBLE);
        lblAppVersion.setVisibility(View.VISIBLE);
        String appName = getApp.getString("app",null);
        lblAppName.setText(appName);
        String appVersion = getApp.getString("version",null);
        lblAppVersion.setText(appVersion);
        //lblAppVersion dan lblAppName dimunculkan embali dengan data dari shared preferences
        //lblAppVersion.setVisibility(View.INVISIBLE);
    }

    private void checkAppVersion() {
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<AppVersion> call = service.getAppVersion();
        call.enqueue(new Callback<AppVersion>() {
            @Override
            public void onResponse(Call<AppVersion> call, Response<AppVersion> response) {
                Toast.makeText(SplashActivity.this, response.body().getApp(), Toast.LENGTH_SHORT).show();
                //Todo : 2. Implementasikan Proses Simpan Data Yang didapat dari Server ke SharedPreferences
                SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preference.edit();
                preference.edit().putString("app", response.body().getApp()).apply();
                preference.edit().putString("version", response.body().getVersion()).apply();

                //Todo : 3. Implementasikan Proses Pindah Ke MainActivity Jika Proses getAppVersion() sukses
                if (response.isSuccessful()){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SplashActivity.this, "Failed for getAppVersion", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AppVersion> call, Throwable t) {
                Toast.makeText(SplashActivity.this, "Gagal Koneksi Ke Server", Toast.LENGTH_SHORT).show();
                //Todo : 4. Implementasikan Cara Notifikasi Ke user jika terjadi kegagalan koneksi ke server silahkan googling cara yang lain selain menggunakan TOAST
                Snackbar.make(constraintLayout, "Gagal Koneksi ke Server",Snackbar.LENGTH_LONG).show();
            }
        });
    }


}