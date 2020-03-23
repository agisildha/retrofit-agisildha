package id.putraprima.retrofit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.AppVersion;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    TextView mainName, mainVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainName = findViewById(R.id.mainTxtAppName);
        mainVersion = findViewById(R.id.mainTxtAppVersion);

        SharedPreferences getApp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mainName.setVisibility(View.VISIBLE);
        mainVersion.setVisibility(View.VISIBLE);
        String appName = getApp.getString("app",null);
        mainName.setText(appName);
        String appVersion = getApp.getString("version",null);
        mainVersion.setText(appVersion);
    }

}
