package rs.raf.projekat_jun_lazar_bojanic_11621.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.Collections;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.adapter.MealListAdapter;
import rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel.MealsSearchActivityViewModel;

public class MealsSearchActivity extends AppCompatActivity {
    private MealsSearchActivityViewModel mealsSearchActivityViewModel;
    private MealListAdapter mealListAdapter;
    private ProgressBar progressBarLoading;
    private String strCategory;

    private String selectedStrCategory;
    private Spinner spinnerCategoryFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals_search);
        initializeViews();
        initializeViewModel();
        initializeListeners();
        fetchInitialData();
    }

    private void initializeViews() {
        spinnerCategoryFilter = findViewById(R.id.spinnerCategoryFilter);
        RecyclerView recyclerViewMealList = findViewById(R.id.recyclerViewMealList);
        recyclerViewMealList.setLayoutManager(new LinearLayoutManager(this));
        progressBarLoading = findViewById(R.id.progressBarLoading);
        mealListAdapter = new MealListAdapter(Collections.emptyList());
        recyclerViewMealList.setAdapter(mealListAdapter);
    }

    private void initializeViewModel() {
        mealsSearchActivityViewModel = new ViewModelProvider(this).get(MealsSearchActivityViewModel.class);
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
        mealsSearchActivityViewModel.getSimpleCategoryListLiveData().observe(this, categories -> {
            ArrayAdapter<String> spinnerCategoryFilterAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categories);
            spinnerCategoryFilterAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
            spinnerCategoryFilter.setAdapter(spinnerCategoryFilterAdapter);

            // Set selected item in spinner
            if (strCategory != null && !strCategory.isEmpty()) {
                int index = spinnerCategoryFilterAdapter.getPosition(strCategory);
                if (index != -1) {
                    spinnerCategoryFilter.setSelection(index);
                }
            }
        });
    }

    private void initializeListeners() {
        mealListAdapter.setOnMealClickListener(meal -> {
            //handle on click
        });
        spinnerCategoryFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStrCategory = spinnerCategoryFilter.getItemAtPosition(position).toString();
                Log.i(String.valueOf(R.string.foodgeTag), selectedStrCategory);
                mealsSearchActivityViewModel.fetchAllMealsForCategory(selectedStrCategory)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void fetchInitialData() {
        Intent intent = getIntent();
        strCategory = intent.getStringExtra(String.valueOf(R.string.extraStrCategory));
        Log.i(String.valueOf(R.string.foodgeTag), strCategory);

        mealsSearchActivityViewModel.fetchAllCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

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
