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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.PlaceHolders;
import rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel.RemoteMealDetailsActivityViewModel;

public class RemoteMealDetailsActivity extends AppCompatActivity {
    private RemoteMealDetailsActivityViewModel remoteMealDetailsActivityViewModel;
    private ImageView imageViewMealImage;
    private TextView textViewMealName;
    private TextView textViewCategory;
    private TextView textViewArea;
    private TextView textViewInstructions;
    private TextView textViewRecipe;
    private TextView textViewYoutubeLink;
    private FloatingActionButton floatingActionButtonAddMeal;
    private String receivedIdMeal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_meal_details);
        remoteMealDetailsActivityViewModel = new ViewModelProvider(this).get(RemoteMealDetailsActivityViewModel.class);
        imageViewMealImage = findViewById(R.id.imageViewMealImage);
        textViewMealName = findViewById(R.id.textViewMealName);
        textViewCategory = findViewById(R.id.textViewCategory);
        textViewArea = findViewById(R.id.textViewArea);
        textViewInstructions = findViewById(R.id.textViewInstructions);
        textViewRecipe = findViewById(R.id.textViewRecipe);
        textViewYoutubeLink = findViewById(R.id.textViewYoutubeLink);
        floatingActionButtonAddMeal = findViewById(R.id.floatingActionButtonAddMeal);
        Intent intent = getIntent();
        receivedIdMeal = intent.getStringExtra(String.valueOf(R.string.extraIdMeal));

        textViewYoutubeLink.setOnClickListener(v -> {
            String youtubeLink = textViewYoutubeLink.getText().toString();
            if (!TextUtils.isEmpty(youtubeLink)) {
                openYoutubeVideo(youtubeLink);
            }
        });
        floatingActionButtonAddMeal.setOnClickListener(v -> {
            Intent addPersonalMealIntent = new Intent(this, AddPersonalMealActivity.class);
            addPersonalMealIntent.putExtra(String.valueOf(R.string.extraMeal), remoteMealDetailsActivityViewModel.getFullMealLiveData().getValue());
            startActivity(addPersonalMealIntent);
        });
        remoteMealDetailsActivityViewModel.getFullMealLiveData().observe(this, meal -> {
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
            textViewRecipe.setText(meal.getRecipe());
            textViewYoutubeLink.setText(meal.getStrYoutube());
            makeLinkClickable(textViewYoutubeLink);
        });

        remoteMealDetailsActivityViewModel.fetchMealDetails(receivedIdMeal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

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