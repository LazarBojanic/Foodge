package rs.raf.projekat_jun_lazar_bojanic_11621.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.PersonalMeal;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Meal;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.DateConverter;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.PlaceHolders;
import rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel.AddPersonalMealActivityViewModel;
import rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel.EditPersonalMealActivityViewModel;

public class EditPersonalMealActivity extends AppCompatActivity {
    private EditPersonalMealActivityViewModel editPersonalMealActivityViewModel;
    private PersonalMeal receivedPersonalMeal;
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
        editPersonalMealActivityViewModel = new ViewModelProvider(this).get(EditPersonalMealActivityViewModel.class);
        initializeViews();
        initializeListeners();
        Intent intent = getIntent();
        receivedPersonalMeal = intent.getSerializableExtra(String.valueOf(R.string.extraPersonalMeal), PersonalMeal.class);
        if(receivedPersonalMeal != null){
            PersonalMeal personalMeal = new PersonalMeal();
            personalMeal.setId(receivedPersonalMeal.getId());
            personalMeal.setStrMeal(receivedPersonalMeal.getStrMeal());
            personalMeal.setMealType(receivedPersonalMeal.getMealType());
            personalMeal.setStrInstructions(receivedPersonalMeal.getStrInstructions());
            personalMeal.setRecipe(receivedPersonalMeal.getRecipe());
            personalMeal.setStrYoutube(receivedPersonalMeal.getStrYoutube());
            personalMeal.setMealImagePath(receivedPersonalMeal.getMealImagePath());
            personalMeal.setDateOfPrep(receivedPersonalMeal.getDateOfPrep());
            personalMeal.setStrCategory(receivedPersonalMeal.getStrCategory());
            editPersonalMealActivityViewModel.getPersonalMealMutableLiveData().postValue(personalMeal);
        }
        editPersonalMealActivityViewModel.getPersonalMealMutableLiveData().observe(this, personalMeal -> {
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
            PersonalMeal personalMeal = editPersonalMealActivityViewModel.getPersonalMealMutableLiveData().getValue();
            if (personalMeal != null) {
                personalMeal.setStrMeal(editTextStrMeal.getText().toString().trim());
                personalMeal.setStrCategory(editTextStrCategory.getText().toString().trim());
                personalMeal.setMealType(editTextMealType.getText().toString().trim());
                personalMeal.setStrInstructions(editTextStrInstructions.getText().toString().trim());
                personalMeal.setRecipe(editTextRecipe.getText().toString().trim());
                personalMeal.setStrYoutube(editTextStrYoutube.getText().toString().trim());
                personalMeal.setMealImagePath(textViewMealImagePath.getText().toString().trim());
                personalMeal.setDateOfPrep(DateConverter.fromString(editTextDateOfPrep.getText().toString().trim()));
                editPersonalMealActivityViewModel.updatePersonalMeal(personalMeal)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Toast.makeText(this, "Personal meal updated successfully!", Toast.LENGTH_SHORT).show();
                            finish();
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
            String currentDateString = editTextDateOfPrep.getText().toString().trim();
            int currentYear, currentMonth, currentDay;
            Calendar calendar = Calendar.getInstance();
            try{
                Date currentDate = DateConverter.fromString(currentDateString);
                if(currentDate != null){
                    calendar.setTime(currentDate);
                }
            }
            catch (ParseException e){
                calendar.setTime(Date.from(Instant.now()));
            }
            currentYear = calendar.get(Calendar.YEAR);
            currentMonth = calendar.get(Calendar.MONTH);
            currentDay = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> {
                        String formattedMonth = String.format(Locale.getDefault(), "%02d", month + 1);
                        String formattedDay = String.format(Locale.getDefault(), "%02d", dayOfMonth);
                        String selectedDateString = year + "-" + formattedMonth + "-" + formattedDay;
                        editTextDateOfPrep.setText(selectedDateString);
                    },
                    currentYear,
                    currentMonth,
                    currentDay
            );
            datePickerDialog.show();
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
        File imageFile = new File(getExternalFilesDir(null), "personal_meal_" + editPersonalMealActivityViewModel.getPersonalMealMutableLiveData().getValue().getStrMeal() + ".png");
        try {
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            PersonalMeal currentPersonalMeal = editPersonalMealActivityViewModel.getPersonalMealMutableLiveData().getValue();
            currentPersonalMeal.setMealImagePath(imageFile.getAbsolutePath());
            editPersonalMealActivityViewModel.getPersonalMealMutableLiveData().postValue(currentPersonalMeal);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}