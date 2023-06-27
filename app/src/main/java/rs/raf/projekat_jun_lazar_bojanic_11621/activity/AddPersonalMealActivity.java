package rs.raf.projekat_jun_lazar_bojanic_11621.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.sql.Date;
import java.time.Instant;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.PersonalMeal;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Meal;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.DateConverter;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.PlaceHolders;
import rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel.AddPersonalMealActivityViewModel;

public class AddPersonalMealActivity extends AppCompatActivity {
    private AddPersonalMealActivityViewModel addPersonalMealActivityViewModel;
    private ImageView imageViewMealImage;
    private EditText editTextMealImagePath;
    private EditText editTextStrMeal;
    private TextView textViewStrYoutubeLabel;
    private EditText editTextStrYoutube;
    private TextView textViewDateOfPrepLabel;
    private EditText editTextDateOfPrep;
    private Button buttonShowDatePicker;
    private EditText editTextMealType;
    private TextView textViewInstructionsLabel;
    private EditText editTextInstructions;
    private TextView textViewRecipeLabel;
    private EditText editTextRecipe;
    private TextView textViewStrCategoryLabel;
    private EditText editTextStrCategory;
    private TextView textViewStrIngredientLabel;
    private EditText editTextStrIngredient;
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
        Meal receivedMeal = intent.getSerializableExtra(String.valueOf(R.string.extraMeal), Meal.class);
        if(receivedMeal != null){
            PersonalMeal personalMeal = new PersonalMeal();
            personalMeal.setId(null);
            personalMeal.setIdMeal(receivedMeal.getIdMeal());
            personalMeal.setStrMeal(receivedMeal.getStrMeal());
            personalMeal.setMealType("");
            personalMeal.setInstructions(receivedMeal.getStrInstructions());
            personalMeal.setRecipe(receivedMeal.getRecipe());
            personalMeal.setStrYoutube(receivedMeal.getStrYoutube());
            personalMeal.setMealImagePath(receivedMeal.getStrMealThumb());
            personalMeal.setDateOfPrep(Date.from(Instant.now()));
            personalMeal.setStrCategory(receivedMeal.getStrCategory());
            personalMeal.setStrIngredient(receivedMeal.getStrIngredient1());
            addPersonalMealActivityViewModel.getPersonalMealMutableLiveData().postValue(personalMeal);
            if(receivedMeal.getStrMealThumb() != null){
                Glide.with(this)
                        .load(receivedMeal.getStrMealThumb())
                        .error(PlaceHolders.getInstance().getPlaceHolderImage())
                        .placeholder(PlaceHolders.getInstance().getPlaceHolderImage()).into(imageViewMealImage);
            }
            else{
                Glide.with(this)
                        .load(PlaceHolders.getInstance().getPlaceHolderImage())
                        .error(PlaceHolders.getInstance().getPlaceHolderImage())
                        .placeholder(PlaceHolders.getInstance().getPlaceHolderImage())
                        .into(imageViewMealImage);
            }
            editTextMealImagePath.setText(personalMeal.getMealImagePath());
            editTextStrMeal.setText(personalMeal.getStrMeal());
            editTextMealType.setText(personalMeal.getMealType());
            editTextInstructions.setText(personalMeal.getInstructions());
            editTextRecipe.setText(personalMeal.getRecipe());
            editTextStrYoutube.setText(personalMeal.getStrYoutube());
            editTextDateOfPrep.setText(DateConverter.fromDate(personalMeal.getDateOfPrep()));
            editTextStrCategory.setText(personalMeal.getStrCategory());
            editTextStrIngredient.setText(personalMeal.getStrIngredient());
        }
    }
    private void initializeViews(){
        imageViewMealImage = findViewById(R.id.imageViewMealImage);
        editTextMealImagePath = findViewById(R.id.editTextMealImagePath);
        editTextStrMeal = findViewById(R.id.editTextStrMeal);
        textViewStrYoutubeLabel = findViewById(R.id.textViewStrYoutubeLabel);
        editTextStrYoutube = findViewById(R.id.editTextStrYoutube);
        textViewDateOfPrepLabel = findViewById(R.id.textViewDateOfPrepLabel);
        editTextDateOfPrep = findViewById(R.id.editTextDateOfPrep);
        buttonShowDatePicker = findViewById(R.id.buttonShowDatePicker);
        editTextMealType = findViewById(R.id.editTextMealType);
        textViewInstructionsLabel = findViewById(R.id.textViewInstructionsLabel);
        editTextInstructions = findViewById(R.id.editTextInstructions);
        textViewRecipeLabel = findViewById(R.id.textViewRecipeLabel);
        editTextRecipe = findViewById(R.id.editTextRecipe);
        textViewStrCategoryLabel = findViewById(R.id.textViewStrCategoryLabel);
        editTextStrCategory = findViewById(R.id.editTextStrCategory);
        textViewStrIngredientLabel = findViewById(R.id.textViewStrIngredientLabel);
        editTextStrIngredient = findViewById(R.id.editTextStrIngredient);
        buttonConfirm = findViewById(R.id.buttonConfirm);
        buttonCancel = findViewById(R.id.buttonCancel);
    }
    private void initializeListeners(){
        buttonConfirm.setOnClickListener(v -> {
            PersonalMeal personalMeal = addPersonalMealActivityViewModel.getPersonalMealMutableLiveData().getValue();
            if (personalMeal != null) {
                personalMeal.setMealImagePath(editTextMealImagePath.getText().toString().trim());
                personalMeal.setStrMeal(editTextStrMeal.getText().toString().trim());
                personalMeal.setMealType(editTextMealType.getText().toString().trim());
                personalMeal.setInstructions(editTextInstructions.getText().toString().trim());
                personalMeal.setRecipe(editTextRecipe.getText().toString().trim());
                personalMeal.setStrYoutube(editTextStrYoutube.getText().toString().trim());
                personalMeal.setStrCategory(editTextStrCategory.getText().toString().trim());
                personalMeal.setStrIngredient(editTextStrIngredient.getText().toString().trim());
                personalMeal.setDateOfPrep(DateConverter.fromString(editTextDateOfPrep.getText().toString().trim()));
                addPersonalMealActivityViewModel.addPersonalMeal(personalMeal)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    Log.d("ViewModel", "Personal meal added successfully");
                                    finish();
                                },
                                throwable -> {
                                    Log.e("ViewModel", "Error adding personal meal: ", throwable);
                                }
                        );
            }
        });
        buttonCancel.setOnClickListener(v -> {
            finish();
        });
    }

}