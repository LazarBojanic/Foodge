package rs.raf.projekat_jun_lazar_bojanic_11621.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.FoodgeDatabaseHelper;
import rs.raf.projekat_jun_lazar_bojanic_11621.model.ServiceUser;

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
                ServiceUser serviceUser = new ServiceUser(-1, editTextEmail.getText().toString(), editTextUsername.getText().toString(), editTextPass.getText().toString());

                Completable registrationCompletable = FoodgeDatabaseHelper.getInstance(this).registerUser(this, serviceUser);

                registrationCompletable.subscribe(
                        () -> {
                            Log.i(getResources().getString(R.string.foodgeTag), "Registration Successful.");
                            Toast.makeText(this, "Registration Successful.", Toast.LENGTH_SHORT).show();
                        },
                        error -> {
                            Log.i(getResources().getString(R.string.foodgeTag), "Registration Failed. " + error.getMessage());
                            Toast.makeText(this, "Registration Failed.", Toast.LENGTH_SHORT).show();
                        }
                ).dispose();




                /*if(FoodgeDatabaseHelper.getInstance(this).registerUser(this, serviceUser)){
                    Log.i(getResources().getString(R.string.foodgeTag), "Registration Successful.");
                    Toast.makeText(this, "Registration Successful.", Toast.LENGTH_SHORT).show()
                }
                else{
                    Log.i(getResources().getString(R.string.foodgeTag), "Registration Failed.");
                    Toast.makeText(this, "Registration Failed.", Toast.LENGTH_SHORT).show();
                }*/
            }
            catch(Exception e){
                Log.i(getResources().getString(R.string.foodgeTag), "Registration Failed. " + e.getMessage());
                Toast.makeText(this, "Registration Failed. " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        buttonLogin.setOnClickListener(view -> {
            try{
                ServiceUser serviceUser = new ServiceUser(-1, editTextEmail.getText().toString(), editTextUsername.getText().toString(), editTextPass.getText().toString());
                if(FoodgeDatabaseHelper.getInstance(this).loginUser(this, serviceUser)){
                    /*Log.i(getResources().getString(R.string.foodgeTag), "Login Successful.");
                    Toast.makeText(this, "Login Successful.", Toast.LENGTH_SHORT).show();*/
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    /*Log.i(getResources().getString(R.string.foodgeTag), "Login Failed.");
                    Toast.makeText(this, "Login Failed.", Toast.LENGTH_SHORT).show();*/
                }
            }
            catch(Exception e){
                Log.i(getResources().getString(R.string.foodgeTag), "Login Failed. " + e.getMessage());
                Toast.makeText(this, "Login Failed. " + e.getMessage(), Toast.LENGTH_SHORT).show();
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