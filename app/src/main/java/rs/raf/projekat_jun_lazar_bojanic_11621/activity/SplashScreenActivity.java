package rs.raf.projekat_jun_lazar_bojanic_11621.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.fasterxml.jackson.core.JsonProcessingException;

import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.FoodgeDatabase;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.FoodgeDatabaseHelper;
import rs.raf.projekat_jun_lazar_bojanic_11621.model.ServiceUser;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.Util;

public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSplashScreen();
        setContentView(R.layout.activity_splash_screen);
    }

    public void initSplashScreen(){
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        splashScreen.setKeepOnScreenCondition(() -> {
            try {
                Thread.sleep(500);
                ServiceUser serviceUser = Util.getUserSharedPreference(this);
                if(serviceUser != null){
                    if(FoodgeDatabaseHelper.getInstance(this).loginUserWithSharedPreferences(this, serviceUser)){
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(this, LoginAndRegisterActivity.class);
                        startActivity(intent);
                    }
                }
                else{
                    Log.i(getResources().getString(R.string.foodgeTag), "Login failed. Shared preference doesn't exist.");
                    Toast.makeText(this, "Login failed. Shared preference doesn't exist.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, LoginAndRegisterActivity.class);
                    startActivity(intent);
                }
            }
            catch (Exception e) {
                Log.i(getResources().getString(R.string.foodgeTag), "Login Failed. " + e.getMessage());
                Toast.makeText(this, "Login failed. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginAndRegisterActivity.class);
                startActivity(intent);
            }
            return false;
        });
    }
}
