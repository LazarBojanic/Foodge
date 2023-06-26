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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.adapter.PersonalMealListAdapter;
import rs.raf.projekat_jun_lazar_bojanic_11621.adapter.RemoteMealListAdapter;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.PersonalMeal;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Meal;
import rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel.PersonalMealsActivityViewModel;
import rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel.RemoteMealsFragmentViewModel;

public class PersonalMealsActivity extends AppCompatActivity {
    public static Integer PAGE_SIZE = 10;
    private Integer currentPage;
    private Integer pageCount;
    private PersonalMealsActivityViewModel personalMealsActivityViewModel;
    private PersonalMealListAdapter personalMealListAdapter;
    private ProgressBar progressBarLoading;
    private EditText editTextSearchCategory;
    private EditText editTextSearchIngredient;
    private EditText editTextSearchName;
    private Button buttonPrevPage;
    private Button buttonNextPage;
    RecyclerView recyclerViewMealList;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_meals);
        currentPage = 1;
        pageCount = 1;
        initializeViewModel();
        initializeViews();
        initializeListeners();
        fetchInitialData();

    }

    private void initializeViews() {
        progressBarLoading = findViewById(R.id.progressBarLoading);
        buttonPrevPage = findViewById(R.id.buttonPrevPage);
        buttonNextPage = findViewById(R.id.buttonNextPage);
        editTextSearchName = findViewById(R.id.editTextSearchName);
        editTextSearchCategory = findViewById(R.id.editTextSearchCategory);
        editTextSearchIngredient = findViewById(R.id.editTextSearchIngredient);
        recyclerViewMealList = findViewById(R.id.recyclerViewMealList);
        recyclerViewMealList.setLayoutManager(new LinearLayoutManager(this));
        personalMealListAdapter = new PersonalMealListAdapter(Collections.emptyList());
        recyclerViewMealList.setAdapter(personalMealListAdapter);
    }

    private void initializeViewModel() {
        personalMealsActivityViewModel = new ViewModelProvider(this).get(PersonalMealsActivityViewModel.class);
        personalMealsActivityViewModel.getPersonalMealListLiveData().observe(this, mealList -> {
            currentPage = 1;
            List<PersonalMeal> mealsInPage = getItemsForPage(mealList, currentPage);
            personalMealListAdapter.setMealListLocal(mealsInPage);
            pageCount = (int) Math.ceil((double) mealList.size() / PAGE_SIZE);
            updatePaginationButtons();
        });
        personalMealsActivityViewModel.getLoadingStatusLiveData().observe(this, isLoading -> {
            if (isLoading) {
                showLoadingView();
            } else {
                hideLoadingView();
            }
        });
        personalMealsActivityViewModel.setupNameSearchObserver();
        personalMealsActivityViewModel.setupCategorySearchObserver();
        personalMealsActivityViewModel.setupIngredientSearchObserver();
    }

    private void initializeListeners() {
        personalMealListAdapter.setOnPersonalMealClickListener(meal -> {
            Intent intent = new Intent(this, RemoteMealDetailsActivity.class);
            intent.putExtra(String.valueOf(R.string.extraIdMeal), meal.getIdMeal());
            startActivity(intent);
        });
        editTextSearchCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(String.valueOf(R.string.foodgeTag), s.toString());
                personalMealsActivityViewModel.onCategorySearchTextChanged(s.toString());
                personalMealListAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });
        editTextSearchIngredient.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(String.valueOf(R.string.foodgeTag), s.toString());
                personalMealsActivityViewModel.onIngredientSearchTextChanged(s.toString());
                personalMealListAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });
        editTextSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(String.valueOf(R.string.foodgeTag), s.toString());
                personalMealsActivityViewModel.onNameSearchTextChanged(s.toString());
                personalMealListAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });
        buttonPrevPage.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                updatePaginationButtons();
                List<PersonalMeal> previousPageItems = getItemsForPage(Objects.requireNonNull(personalMealsActivityViewModel.getPersonalMealListLiveData().getValue()), currentPage);
                personalMealListAdapter.setMealListLocal(previousPageItems);
            }
        });

        buttonNextPage.setOnClickListener(v -> {
            if (currentPage < pageCount) {
                currentPage++;
                updatePaginationButtons();
                List<PersonalMeal> previousPageItems = getItemsForPage(Objects.requireNonNull(personalMealsActivityViewModel.getPersonalMealListLiveData().getValue()), currentPage);
                personalMealListAdapter.setMealListLocal(previousPageItems);
            }
        });
    }

    private void fetchInitialData() {
        personalMealsActivityViewModel.fetchAllMealsLocal()
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
    private List<PersonalMeal> getItemsForPage(List<PersonalMeal> meals, int page) {
        int startIndex = (page - 1) * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, meals.size());
        return meals.subList(startIndex, endIndex);
    }
    private void updatePaginationButtons() {
        buttonPrevPage.setEnabled(currentPage > 1);
        buttonNextPage.setEnabled(currentPage < pageCount);
    }




}