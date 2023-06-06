package rs.raf.projekat_jun_lazar_bojanic_11621.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.FoodgeApp;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.ServiceUser;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.service.implementation.ServiceUserService;

public class LoginAndRegisterActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextUsername;
    private EditText editTextPass;
    private Button buttonLogin;
    private Button buttonRegister;
    private Button buttonExit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_register);

        initView();
        initListeners();
    }

    private void initView(){
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPass = findViewById(R.id.editTextPass);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonExit = findViewById(R.id.buttonExit);
    }

    private void initListeners(){
        buttonRegister.setOnClickListener(view -> {
            try{
                ServiceUser serviceUser = new ServiceUser(null, editTextEmail.getText().toString(), editTextUsername.getText().toString(), editTextPass.getText().toString());
                FoodgeApp foodgeApp = (FoodgeApp) getApplicationContext();
                ServiceUserService serviceUserService = foodgeApp.getFoodgeAppComponent().getServiceUserService();
                serviceUserService.register(serviceUser)
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }
                            @Override
                            public void onComplete() {
                                Log.i(getResources().getString(R.string.foodgeTag), "Registration successful.");
                                Toast.makeText(getApplicationContext(), "Registration successful.", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.e(getResources().getString(R.string.foodgeTag), e.getMessage());
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            catch(Exception e){
                Log.e(getResources().getString(R.string.foodgeTag), e.getMessage());
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        buttonLogin.setOnClickListener(view -> {
            try{
                ServiceUser serviceUser = new ServiceUser(null, editTextEmail.getText().toString(), editTextUsername.getText().toString(), editTextPass.getText().toString());
                FoodgeApp foodgeApp = (FoodgeApp) getApplicationContext();
                ServiceUserService serviceUserService = foodgeApp.getFoodgeAppComponent().getServiceUserService();
                serviceUserService.login(serviceUser)
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }
                            @Override
                            public void onComplete() {
                                Log.i(getResources().getString(R.string.foodgeTag), "Login successful.");
                                Toast.makeText(getApplicationContext(), "Login successful.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginAndRegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.e(getResources().getString(R.string.foodgeTag), e.getMessage());
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            catch(Exception e){
                Log.e(getResources().getString(R.string.foodgeTag), e.getMessage());
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        buttonExit.setOnClickListener(v -> {
            finish();
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}