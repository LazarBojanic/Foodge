package rs.raf.projekat_jun_lazar_bojanic_11621.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.PersonalMeal;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Meal;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.DateConverter;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.PlaceHolders;
import rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel.AddPersonalMealActivityViewModel;
import rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel.RemoteMealDetailsActivityViewModel;

public class AddPersonalMealActivity extends AppCompatActivity {
    private AddPersonalMealActivityViewModel addPersonalMealActivityViewModel;
    private Meal receivedMeal;
    private ImageView imageViewMealImage;
    private TextView textViewMealImagePath;
    private EditText editTextStrMeal;
    private EditText editTextStrCategory;
    private EditText editTextMealType;
    private EditText editTextStrInstructions;
    private EditText editTextRecipe;
    private EditText editTextStrYoutube;
    private EditText editTextDateOfPrep;
    private Button buttonShowDatePicker;
    private Button buttonConfirm;
    private Button buttonCancel;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int GALLERY_REQUEST_CODE = 201;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_personal_meal);
        addPersonalMealActivityViewModel = new ViewModelProvider(this).get(AddPersonalMealActivityViewModel.class);
        initializeViews();
        initializeListeners();
        Intent intent = getIntent();
        receivedMeal = intent.getSerializableExtra(String.valueOf(R.string.extraMeal), Meal.class);
        if(receivedMeal != null){
            PersonalMeal personalMeal = new PersonalMeal();
            personalMeal.setId(null);
            personalMeal.setStrMeal(receivedMeal.getStrMeal());
            personalMeal.setMealType("");
            personalMeal.setStrInstructions(receivedMeal.getStrInstructions());
            personalMeal.setRecipe(receivedMeal.getRecipe());
            personalMeal.setStrYoutube(receivedMeal.getStrYoutube());
            personalMeal.setMealImagePath(receivedMeal.getStrMealThumb());
            personalMeal.setDateOfPrep(Date.from(Instant.now()));
            personalMeal.setStrCategory(receivedMeal.getStrCategory());
            addPersonalMealActivityViewModel.getPersonalMealMutableLiveData().postValue(personalMeal);
        }
        addPersonalMealActivityViewModel.getPersonalMealMutableLiveData().observe(this, personalMeal -> {
            if(personalMeal.getMealImagePath() != null){
                if(personalMeal.getMealImagePath().contains("http")){
                    Glide.with(this)
                            .load(personalMeal.getMealImagePath())
                            .error(PlaceHolders.getInstance().getPlaceHolderImage())
                            .placeholder(PlaceHolders.getInstance().getPlaceHolderImage())
                            .into(imageViewMealImage);
                }
                else{
                    File file = new File(personalMeal.getMealImagePath());
                    Glide.with(this)
                            .load(file)
                            .error(PlaceHolders.getInstance().getPlaceHolderImage())
                            .placeholder(PlaceHolders.getInstance().getPlaceHolderImage())
                            .into(imageViewMealImage);
                }
            }
            else{
                Glide.with(this)
                        .load(PlaceHolders.getInstance().getPlaceHolderImage())
                        .error(PlaceHolders.getInstance().getPlaceHolderImage())
                        .placeholder(PlaceHolders.getInstance().getPlaceHolderImage())
                        .into(imageViewMealImage);
            }
            textViewMealImagePath.setText(personalMeal.getMealImagePath());
            editTextStrMeal.setText(personalMeal.getStrMeal());
            editTextStrCategory.setText(personalMeal.getStrCategory());
            editTextMealType.setText(personalMeal.getMealType());
            editTextStrInstructions.setText(personalMeal.getStrInstructions());
            editTextRecipe.setText(personalMeal.getRecipe());
            editTextStrYoutube.setText(personalMeal.getStrYoutube());
            editTextDateOfPrep.setText(DateConverter.fromDate(personalMeal.getDateOfPrep()));
        });
    }
    private void initializeViews(){
        imageViewMealImage = findViewById(R.id.imageViewMealImage);
        textViewMealImagePath = findViewById(R.id.textViewMealImagePath);
        editTextStrMeal = findViewById(R.id.editTextStrMeal);
        editTextStrCategory = findViewById(R.id.editTextStrCategory);
        editTextMealType = findViewById(R.id.editTextMealType);
        editTextStrInstructions = findViewById(R.id.editTextStrInstructions);
        editTextRecipe = findViewById(R.id.editTextRecipe);
        editTextStrYoutube = findViewById(R.id.editTextStrYoutube);
        editTextDateOfPrep = findViewById(R.id.editTextDateOfPrep);
        buttonShowDatePicker = findViewById(R.id.buttonShowDatePicker);
        buttonConfirm = findViewById(R.id.buttonConfirm);
        buttonCancel = findViewById(R.id.buttonCancel);
    }
    private void initializeListeners(){
        buttonConfirm.setOnClickListener(v -> {
            PersonalMeal personalMeal = addPersonalMealActivityViewModel.getPersonalMealMutableLiveData().getValue();
            if (personalMeal != null) {
                personalMeal.setId(null);
                personalMeal.setStrMeal(editTextStrMeal.getText().toString().trim());
                personalMeal.setStrCategory(editTextStrCategory.getText().toString().trim());
                personalMeal.setMealType(editTextMealType.getText().toString().trim());
                personalMeal.setStrInstructions(editTextStrInstructions.getText().toString().trim());
                personalMeal.setRecipe(editTextRecipe.getText().toString().trim());
                personalMeal.setStrYoutube(editTextStrYoutube.getText().toString().trim());
                personalMeal.setMealImagePath(textViewMealImagePath.getText().toString().trim());
                personalMeal.setDateOfPrep(DateConverter.fromString(editTextDateOfPrep.getText().toString().trim()));
                addPersonalMealActivityViewModel.addPersonalMeal(personalMeal)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Toast.makeText(this, "Personal meal successfully added!", Toast.LENGTH_SHORT).show();
                            finish(); // Finish the activity when the personal meal is added successfully
                        });
            }
        });
        buttonCancel.setOnClickListener(v -> {
            finish();
        });
        imageViewMealImage.setOnClickListener(v -> {
            if (isCameraPermissionGranted()) {
                openCamera();
            } else {
                requestCameraPermission();
            }
        });
        buttonShowDatePicker.setOnClickListener(v -> {

        });
    }
    private boolean isCameraPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Bitmap imageBitmap = (Bitmap) bundle.get("data");
                    saveImage(imageBitmap);
                }
            } else if (requestCode == GALLERY_REQUEST_CODE) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    try {
                        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        saveImage(imageBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void saveImage(Bitmap imageBitmap) {
        File imageFile = new File(getExternalFilesDir(null), "personal_meal_" + addPersonalMealActivityViewModel.getPersonalMealMutableLiveData().getValue().getStrMeal() + ".png");
        try {
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            PersonalMeal currentPersonalMeal = addPersonalMealActivityViewModel.getPersonalMealMutableLiveData().getValue();
            currentPersonalMeal.setMealImagePath(imageFile.getAbsolutePath());
            addPersonalMealActivityViewModel.getPersonalMealMutableLiveData().postValue(currentPersonalMeal);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}