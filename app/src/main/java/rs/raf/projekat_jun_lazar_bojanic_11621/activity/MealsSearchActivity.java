package rs.raf.projekat_jun_lazar_bojanic_11621.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.adapter.MealListAdapter;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Meal;
import rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel.MealsSearchActivityViewModel;

public class MealsSearchActivity extends AppCompatActivity {
    public static Integer PAGE_SIZE = 10;
    private Integer currentPage;
    private Integer pageCount;
    private MealsSearchActivityViewModel mealsSearchActivityViewModel;
    private MealListAdapter mealListAdapter;
    private ProgressBar progressBarLoading;
    private String receivedStrCategory;
    private String selectedStrCategory;
    private String selectedStrArea;
    private String selectedStrIngredient;
    private Spinner spinnerCategoryFilter;
    private Spinner spinnerAreaFilter;
    private Spinner spinnerIngredientFilter;
    private EditText editTextSearch;
    //private Spinner spinnerTagFilter;
    ArrayAdapter<String> spinnerCategoryFilterAdapter;
    ArrayAdapter<String> spinnerAreaFilterAdapter;
    ArrayAdapter<String> spinnerIngredientFilterAdapter;
    //ArrayAdapter<String> spinnerTagFilterAdapter;
    private Button buttonPrevPage;
    private Button buttonNextPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals_search);
        initializeViews();
        initializeViewModel();
        initializeListeners();
        fetchInitialData();
        currentPage = 1;
        pageCount = 1;
    }

    private void initializeViews() {
        progressBarLoading = findViewById(R.id.progressBarLoading);
        spinnerCategoryFilter = findViewById(R.id.spinnerCategoryFilter);
        spinnerAreaFilter = findViewById(R.id.spinnerAreaFilter);
        spinnerIngredientFilter = findViewById(R.id.spinnerIngredientFilter);
        //spinnerTagFilter = findViewById(R.id.spinnerTagFilter);
        buttonPrevPage = findViewById(R.id.buttonPrevPage);
        buttonNextPage = findViewById(R.id.buttonNextPage);
        editTextSearch = findViewById(R.id.editTextSearch);
        RecyclerView recyclerViewMealList = findViewById(R.id.recyclerViewMealList);
        recyclerViewMealList.setLayoutManager(new LinearLayoutManager(this));
        mealListAdapter = new MealListAdapter(Collections.emptyList());
        recyclerViewMealList.setAdapter(mealListAdapter);
    }

    private void initializeViewModel() {
        mealsSearchActivityViewModel = new ViewModelProvider(this).get(MealsSearchActivityViewModel.class);
        mealsSearchActivityViewModel.getMealListLiveData().observe(this, mealList -> {
            currentPage = 1;
            List<Meal> mealsInPage = getItemsForPage(mealList, currentPage);
            mealListAdapter.setMealList(mealsInPage);
            pageCount = mealList.size() / PAGE_SIZE;
            updatePaginationButtons();
        });
        mealsSearchActivityViewModel.getLoadingStatusLiveData().observe(this, isLoading -> {
            if (isLoading) {
                showLoadingView();
            } else {
                hideLoadingView();
            }
        });
        mealsSearchActivityViewModel.getSimpleCategoryListLiveData().observe(this, categories -> {
            spinnerCategoryFilterAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categories);
            spinnerCategoryFilterAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
            spinnerCategoryFilter.setAdapter(spinnerCategoryFilterAdapter);
            if (receivedStrCategory != null && !receivedStrCategory.isEmpty()) {
                int index = spinnerCategoryFilterAdapter.getPosition(receivedStrCategory);
                if (index != -1) {
                    spinnerCategoryFilter.setSelection(index);
                }
            }
        });
        mealsSearchActivityViewModel.getSimpleAreaListLiveData().observe(this, areas -> {
            spinnerAreaFilterAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, areas);
            spinnerAreaFilterAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
            spinnerAreaFilter.setAdapter(spinnerAreaFilterAdapter);
        });
        mealsSearchActivityViewModel.getSimpleIngredientListLiveData().observe(this, ingredients -> {
            spinnerIngredientFilterAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, ingredients);
            spinnerIngredientFilterAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
            spinnerIngredientFilter.setAdapter(spinnerIngredientFilterAdapter);
        });
        /*mealsSearchActivityViewModel.getSimpleCategoryListLiveData().observe(this, categories -> {
            spinnerTagFilterAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categories);
            spinnerTagFilterAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
            spinnerCategoryFilter.setAdapter(spinnerTagFilterAdapter);
        });*/
        mealsSearchActivityViewModel.setupSearchObserver();
    }

    private void initializeListeners() {
        mealListAdapter.setOnMealClickListener(meal -> {
            //handle on click
        });
        spinnerCategoryFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStrCategory = spinnerCategoryFilter.getItemAtPosition(position).toString();
                mealsSearchActivityViewModel.fetchAllMealsForCategory(selectedStrCategory)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerAreaFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStrArea = spinnerAreaFilter.getItemAtPosition(position).toString();
                mealsSearchActivityViewModel.fetchAllMealsForArea(selectedStrArea)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerIngredientFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStrIngredient = spinnerIngredientFilter.getItemAtPosition(position).toString();
                mealsSearchActivityViewModel.fetchAllMealsForIngredient(selectedStrIngredient)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        /*spinnerCategoryFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });*/
        buttonPrevPage.setOnClickListener(v -> {

        });
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(String.valueOf(R.string.foodgeTag), s.toString());
                mealsSearchActivityViewModel.onSearchTextChanged(s.toString());
                mealListAdapter.notifyDataSetChanged();
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
                List<Meal> previousPageItems = getItemsForPage(Objects.requireNonNull(mealsSearchActivityViewModel.getMealListLiveData().getValue()), currentPage);
                mealListAdapter.setMealList(previousPageItems);
            }
        });

        buttonNextPage.setOnClickListener(v -> {
            if (currentPage < pageCount) {
                currentPage++;
                updatePaginationButtons();
                List<Meal> nextPageItems = getItemsForPage(Objects.requireNonNull(mealsSearchActivityViewModel.getMealListLiveData().getValue()), currentPage);
                mealListAdapter.setMealList(nextPageItems);
            }
        });
    }

    private void fetchInitialData() {
        Intent intent = getIntent();
        receivedStrCategory = intent.getStringExtra(String.valueOf(R.string.extraStrCategory));
        Log.i(String.valueOf(R.string.foodgeTag), receivedStrCategory);

        mealsSearchActivityViewModel.fetchAllCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        mealsSearchActivityViewModel.fetchAllAreas()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        mealsSearchActivityViewModel.fetchAllIngredients()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

        mealsSearchActivityViewModel.fetchAllMealsForCategory(receivedStrCategory)
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
    private List<Meal> getItemsForPage(List<Meal> meals, int page) {
        int startIndex = (page - 1) * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, meals.size());
        return meals.subList(startIndex, endIndex);
    }
    private void updatePaginationButtons() {
        buttonPrevPage.setEnabled(currentPage > 1);
        buttonNextPage.setEnabled(currentPage < pageCount);
    }
}
