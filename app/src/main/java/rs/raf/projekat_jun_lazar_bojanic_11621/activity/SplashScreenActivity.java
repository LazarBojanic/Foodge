package rs.raf.projekat_jun_lazar_bojanic_11621.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.FoodgeApp;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.service.implementation.ServiceUserService;

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
            try{
                Thread.sleep(500);
                FoodgeApp foodgeApp = (FoodgeApp) getApplicationContext();
                ServiceUserService serviceUserService = foodgeApp.getFoodgeAppComponent().getServiceUserService();
                serviceUserService.loginWithSharedPreferences()
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }
                            @Override
                            public void onComplete() {
                                Log.i(getResources().getString(R.string.foodgeTag),"Login successful.");
                                Toast.makeText(getApplicationContext(), "Login successful.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.e(getResources().getString(R.string.foodgeTag), e.getMessage());
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SplashScreenActivity.this, LoginAndRegisterActivity.class);
                                startActivity(intent);
                            }
                        });
            }
            catch(Exception e){
                Log.e(getResources().getString(R.string.foodgeTag), e.getMessage());
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return false;
        });
    }
}
