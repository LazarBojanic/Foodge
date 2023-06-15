package rs.raf.projekat_jun_lazar_bojanic_11621.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.Collections;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.adapter.CategoryListAdapter;
import rs.raf.projekat_jun_lazar_bojanic_11621.adapter.MealListAdapter;
import rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel.MainActivityViewModel;
import rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel.MealsSearchActivityViewModel;

public class MealsSearchActivity extends AppCompatActivity {
    private MealsSearchActivityViewModel mealsSearchActivityViewModel;
    private MealListAdapter mealListAdapter;
    private ProgressBar progressBarLoading;
    private String strCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals_search);
        Intent intent = getIntent();
        strCategory = intent.getStringExtra(String.valueOf(R.string.extraStrCategory));
        Log.i(String.valueOf(R.string.foodgeTag), strCategory);
        mealsSearchActivityViewModel = new ViewModelProvider(this).get(MealsSearchActivityViewModel.class);


        RecyclerView recyclerViewMealList = findViewById(R.id.recyclerViewMealList);
        recyclerViewMealList.setLayoutManager(new LinearLayoutManager(this));
        progressBarLoading = findViewById(R.id.progressBarLoading);
        mealListAdapter = new MealListAdapter(Collections.emptyList());
        recyclerViewMealList.setAdapter(mealListAdapter);
        mealListAdapter.setOnMealClickListener(meal -> {
            //handle on click
        });
        mealsSearchActivityViewModel.getMealListLiveData().observe(this, mealList -> {
            mealListAdapter.setMealList(mealList);
        });
        mealsSearchActivityViewModel.getLoadingStatusLiveData().observe(this, isLoading -> {
            if (isLoading) {
                showLoadingView();
            } else {
                hideLoadingView();
            }
        });

        mealsSearchActivityViewModel.fetchAllMealsForCategory(strCategory)
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
}