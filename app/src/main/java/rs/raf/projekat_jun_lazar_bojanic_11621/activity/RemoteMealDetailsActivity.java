package rs.raf.projekat_jun_lazar_bojanic_11621.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.PlaceHolders;
import rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel.MealDetailsViewModel;

public class RemoteMealDetailsActivity extends AppCompatActivity {
    private MealDetailsViewModel mealDetailsViewModel;
    private ProgressBar progressBarLoading;
    private ImageView imageViewMealImage;
    private TextView textViewMealName;
    private TextView textViewCategory;
    private TextView textViewArea;
    private TextView textViewInstructions;
    private LinearLayout ingredientsContainer;
    private TextView textViewYoutubeLink;
    private FloatingActionButton floatingActionButtonAddMeal;
    String receivedIdMeal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_meal_details);
        mealDetailsViewModel = new ViewModelProvider(this).get(MealDetailsViewModel.class);

        progressBarLoading = findViewById(R.id.progressBarLoading);
        imageViewMealImage = findViewById(R.id.imageViewMealImage);
        textViewMealName = findViewById(R.id.textViewMealName);
        textViewCategory = findViewById(R.id.textViewCategory);
        textViewArea = findViewById(R.id.textViewArea);
        textViewInstructions = findViewById(R.id.textViewInstructions);
        ingredientsContainer = findViewById(R.id.ingredientsContainer);
        textViewYoutubeLink = findViewById(R.id.textViewYoutubeLink);
        floatingActionButtonAddMeal = findViewById(R.id.floatingActionButtonAddMeal);
        Intent intent = getIntent();
        receivedIdMeal = intent.getStringExtra(String.valueOf(R.string.extraIdMeal));

        mealDetailsViewModel.getLoadingStatusLiveData().observe(this, isLoading -> {
            if (isLoading) {
                showLoadingView();
            } else {
                hideLoadingView();
            }
        });
        textViewYoutubeLink.setOnClickListener(v -> {
            String youtubeLink = textViewYoutubeLink.getText().toString();
            if (!TextUtils.isEmpty(youtubeLink)) {
                openYoutubeVideo(youtubeLink);
            }
        });
        floatingActionButtonAddMeal.setOnClickListener(v -> {
            Log.i(String.valueOf(R.string.foodgeTag), "clicked");
        });
        mealDetailsViewModel.getFullMealLiveData().observe(this, meal -> {
            if(meal.getStrMealThumb() != null){
                Glide.with(this)
                        .load(meal.getStrMealThumb())
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
            textViewMealName.setText(meal.getStrMeal());
            textViewCategory.setText(meal.getStrCategory());
            textViewArea.setText(meal.getStrArea());
            textViewInstructions.setText(meal.getStrInstructions());

            for (int i = 1; i <= 20; i++) {
                String ingredient = meal.getIngredientForNumber(i);
                String measure =  meal.getMeasureForNumber(i);

                if (!TextUtils.isEmpty(ingredient) && !TextUtils.isEmpty(measure)) {
                    TextView textViewIngredient = new TextView(this);
                    textViewIngredient.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    textViewIngredient.setText("- " + ingredient + " (" + measure + ")");
                    ingredientsContainer.addView(textViewIngredient);
                }

            }
            textViewYoutubeLink.setText(meal.getStrYoutube());
            makeLinkClickable(textViewYoutubeLink);
        });

        mealDetailsViewModel.fetchMealDetails(receivedIdMeal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }

    private void showLoadingView() {
        progressBarLoading.setVisibility(View.VISIBLE);
    }

    private void hideLoadingView() {
        progressBarLoading.setVisibility(View.GONE);
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