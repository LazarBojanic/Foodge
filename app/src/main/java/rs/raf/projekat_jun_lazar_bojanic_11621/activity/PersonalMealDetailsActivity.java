package rs.raf.projekat_jun_lazar_bojanic_11621.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import java.io.File;

import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.PersonalMeal;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.DateConverter;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.PlaceHolders;
import rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel.PersonalMealDetailsActivityViewModel;

public class PersonalMealDetailsActivity extends AppCompatActivity {
    private PersonalMealDetailsActivityViewModel personalMealDetailsActivityViewModel;
    private ImageView imageViewMealImage;
    private TextView textViewMealImagePath;
    private TextView textViewStrMeal;
    private TextView textViewStrCategory;
    private TextView textViewMealType;
    private TextView textViewStrYoutube;
    private TextView textViewDateOfPrep;
    private TextView textViewInstructions;
    private TextView textViewRecipe;
    private PersonalMeal receivedPersonalMeal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_meal_details);
        personalMealDetailsActivityViewModel = new ViewModelProvider(this).get(PersonalMealDetailsActivityViewModel.class);
        imageViewMealImage = findViewById(R.id.imageViewMealImage);
        textViewMealImagePath = findViewById(R.id.textViewMealImagePath);
        textViewStrMeal = findViewById(R.id.textViewStrMeal);
        textViewStrCategory = findViewById(R.id.textViewStrCategory);
        textViewMealType = findViewById(R.id.textViewMealType);
        textViewDateOfPrep = findViewById(R.id.textViewDateOfPrep);
        textViewInstructions = findViewById(R.id.textViewInstructions);
        textViewRecipe = findViewById(R.id.textViewRecipe);
        textViewStrYoutube = findViewById(R.id.textViewStrYoutube);
        Intent intent = getIntent();
        receivedPersonalMeal = intent.getSerializableExtra(String.valueOf(R.string.extraPersonalMeal), PersonalMeal.class);
        personalMealDetailsActivityViewModel.getFullPersonalMealLiveData().postValue(receivedPersonalMeal);
        textViewStrYoutube.setOnClickListener(v -> {
            String youtubeLink = textViewStrYoutube.getText().toString();
            if (!TextUtils.isEmpty(youtubeLink)) {
                openYoutubeVideo(youtubeLink);
            }
        });

        personalMealDetailsActivityViewModel.getFullPersonalMealLiveData().observe(this, personalMeal -> {
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
            textViewStrMeal.setText(personalMeal.getStrMeal());
            textViewStrCategory.setText(personalMeal.getStrCategory());
            textViewMealType.setText(personalMeal.getMealType());
            textViewDateOfPrep.setText(DateConverter.fromDate(personalMeal.getDateOfPrep()));
            textViewInstructions.setText(personalMeal.getStrInstructions());
            textViewRecipe.setText(personalMeal.getRecipe());
            textViewStrYoutube.setText(personalMeal.getStrYoutube());
            makeLinkClickable(textViewStrYoutube);
        });
    }
    private void openYoutubeVideo(String youtubeLink) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink));
        intent.setPackage("com.google.android.youtube");
        startActivity(intent);
    }
    private void makeLinkClickable(TextView textView) {
        SpannableString spannableString = new SpannableString(textView.getText());
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.linkColor)), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}