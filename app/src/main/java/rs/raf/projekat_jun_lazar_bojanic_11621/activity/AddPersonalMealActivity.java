package rs.raf.projekat_jun_lazar_bojanic_11621.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
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
                            finish(); // Finish the activity when the personal meal is added successfully
                        });
            }
        });
        buttonCancel.setOnClickListener(v -> {
            finish();
        });
        buttonShowDatePicker.setOnClickListener(v -> {

        });
    }
}