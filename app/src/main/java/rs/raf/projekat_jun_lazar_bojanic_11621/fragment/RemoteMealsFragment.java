package rs.raf.projekat_jun_lazar_bojanic_11621.fragment;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import rs.raf.projekat_jun_lazar_bojanic_11621.activity.PersonalMealsActivity;
import rs.raf.projekat_jun_lazar_bojanic_11621.activity.RemoteMealDetailsActivity;
import rs.raf.projekat_jun_lazar_bojanic_11621.adapter.RemoteMealListAdapter;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Meal;
import rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel.RemoteMealsFragmentViewModel;

public class RemoteMealsFragment extends Fragment {
    private boolean isCategorySpinnerFirstSelection = true;
    private boolean isAreaSpinnerFirstSelection = true;
    private boolean isIngredientSpinnerFirstSelection = true;
    public static Integer PAGE_SIZE = 10;
    private Integer currentPage;
    private Integer pageCount;
    private RemoteMealsFragmentViewModel remoteMealsFragmentViewModel;
    private RemoteMealListAdapter remoteMealListAdapter;
    private ProgressBar progressBarLoading;
    private String receivedStrCategory;
    private String selectedStrCategory;
    private String selectedStrArea;
    private String selectedStrIngredient;
    private Spinner spinnerCategoryFilter;
    private Spinner spinnerAreaFilter;
    private Spinner spinnerIngredientFilter;
    private Button buttonViewPersonalMeals;
    private EditText editTextSearchName;
    //private Spinner spinnerTagFilter;
    ArrayAdapter<String> spinnerCategoryFilterAdapter;
    ArrayAdapter<String> spinnerAreaFilterAdapter;
    ArrayAdapter<String> spinnerIngredientFilterAdapter;
    //ArrayAdapter<String> spinnerTagFilterAdapter;
    private Button buttonPrevPage;
    private Button buttonNextPage;
    RecyclerView recyclerViewMealList;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViewModel();
        currentPage = 1;
        pageCount = 1;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remote_meals, container, false);
        initializeViews(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        initializeListeners();
        fetchInitialData();
    }

    private void initializeViews(View view) {
        progressBarLoading = view.findViewById(R.id.progressBarLoading);
        spinnerCategoryFilter = view.findViewById(R.id.spinnerCategoryFilter);
        spinnerAreaFilter = view.findViewById(R.id.spinnerAreaFilter);
        spinnerIngredientFilter = view.findViewById(R.id.spinnerIngredientFilter);
        //spinnerTagFilter = findViewById(R.id.spinnerTagFilter);
        buttonPrevPage = view.findViewById(R.id.buttonPrevPage);
        buttonNextPage = view.findViewById(R.id.buttonNextPage);
        buttonViewPersonalMeals = view.findViewById(R.id.buttonViewPersonalMeals);
        editTextSearchName = view.findViewById(R.id.editTextSearch);
        recyclerViewMealList = view.findViewById(R.id.recyclerViewMealList);
        recyclerViewMealList.setLayoutManager(new LinearLayoutManager(requireActivity()));
        remoteMealListAdapter = new RemoteMealListAdapter(Collections.emptyList());
        recyclerViewMealList.setAdapter(remoteMealListAdapter);
    }

    private void initializeViewModel() {
        remoteMealsFragmentViewModel = new ViewModelProvider(this).get(RemoteMealsFragmentViewModel.class);
        remoteMealsFragmentViewModel.getRemoteMealListLiveData().observe(this, mealList -> {
            for(Meal meal : mealList){
                Log.i(String.valueOf(R.string.foodgeTag), meal.toString());
            }
            currentPage = 1;
            List<Meal> mealsInPage = getItemsForPage(mealList, currentPage);
            remoteMealListAdapter.setMealListRemote(mealsInPage);
            pageCount = (int) Math.ceil((double) mealList.size() / PAGE_SIZE);
            updatePaginationButtons();
        });
        remoteMealsFragmentViewModel.getLoadingStatusLiveData().observe(this, isLoading -> {
            if (isLoading) {
                showLoadingView();
            } else {
                hideLoadingView();
            }
        });
        remoteMealsFragmentViewModel.getSimpleCategoryListLiveData().observe(this, categories -> {
            spinnerCategoryFilterAdapter = new ArrayAdapter<>(requireActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categories);
            spinnerCategoryFilterAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
            spinnerCategoryFilter.setAdapter(spinnerCategoryFilterAdapter);
            if (receivedStrCategory != null && !receivedStrCategory.isEmpty()) {
                int index = spinnerCategoryFilterAdapter.getPosition(receivedStrCategory);
                if (index != -1) {
                    spinnerCategoryFilter.setSelection(index);
                }
            }
        });
        remoteMealsFragmentViewModel.getSimpleAreaListLiveData().observe(this, areas -> {
            spinnerAreaFilterAdapter = new ArrayAdapter<>(requireActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, areas);
            spinnerAreaFilterAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
            spinnerAreaFilter.setAdapter(spinnerAreaFilterAdapter);
        });
        remoteMealsFragmentViewModel.getSimpleIngredientListLiveData().observe(this, ingredients -> {
            spinnerIngredientFilterAdapter = new ArrayAdapter<>(requireActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, ingredients);
            spinnerIngredientFilterAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
            spinnerIngredientFilter.setAdapter(spinnerIngredientFilterAdapter);
        });
        /*mealsSearchActivityViewModel.getSimpleCategoryListLiveData().observe(this, categories -> {
            spinnerTagFilterAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categories);
            spinnerTagFilterAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
            spinnerCategoryFilter.setAdapter(spinnerTagFilterAdapter);
        });*/
        remoteMealsFragmentViewModel.setupNameSearchObserver();
    }

    private void initializeListeners() {
        remoteMealListAdapter.setOnMealClickListener(meal -> {
            Intent intent = new Intent(requireActivity(), RemoteMealDetailsActivity.class);
            intent.putExtra(String.valueOf(R.string.extraIdMeal), meal.getIdMeal());
            startActivity(intent);
        });
        buttonViewPersonalMeals.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), PersonalMealsActivity.class);
            startActivity(intent);
        });


        spinnerCategoryFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isCategorySpinnerFirstSelection) {
                    // Skip the first selection event
                    isCategorySpinnerFirstSelection = false;
                    return;
                }

                selectedStrCategory = spinnerCategoryFilter.getItemAtPosition(position).toString();
                remoteMealsFragmentViewModel.fetchAllMealsForCategoryRemote(selectedStrCategory)
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
                if (isAreaSpinnerFirstSelection) {
                    // Skip the first selection event
                    isAreaSpinnerFirstSelection = false;
                    return;
                }

                selectedStrArea = spinnerAreaFilter.getItemAtPosition(position).toString();
                remoteMealsFragmentViewModel.fetchAllMealsForAreaRemote(selectedStrArea)
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
                if (isIngredientSpinnerFirstSelection) {
                    // Skip the first selection event
                    isIngredientSpinnerFirstSelection = false;
                    return;
                }

                selectedStrIngredient = spinnerIngredientFilter.getItemAtPosition(position).toString();
                remoteMealsFragmentViewModel.fetchAllMealsForIngredientRemote(selectedStrIngredient)
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
        editTextSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(String.valueOf(R.string.foodgeTag), s.toString());
                remoteMealsFragmentViewModel.onNameSearchTextChanged(s.toString());
                remoteMealListAdapter.notifyDataSetChanged();
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
                List<Meal> previousPageItems = getItemsForPage(Objects.requireNonNull(remoteMealsFragmentViewModel.getRemoteMealListLiveData().getValue()), currentPage);
                remoteMealListAdapter.setMealListRemote(previousPageItems);
            }
        });

        buttonNextPage.setOnClickListener(v -> {
            if (currentPage < pageCount) {
                currentPage++;
                updatePaginationButtons();
                List<Meal> nextPageItems = getItemsForPage(Objects.requireNonNull(remoteMealsFragmentViewModel.getRemoteMealListLiveData().getValue()), currentPage);
                remoteMealListAdapter.setMealListRemote(nextPageItems);
            }
        });
    }

    private void fetchInitialData() {
        Bundle receivedArguments = getArguments();
        if(receivedArguments == null || receivedArguments.isEmpty()){
            receivedStrCategory = "Dessert";
        }
        else{
            receivedStrCategory = receivedArguments.getString(getString(R.string.extraStrCategory));
        }
        Log.i(String.valueOf(R.string.foodgeTag), receivedStrCategory);

        remoteMealsFragmentViewModel.fetchAllCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        remoteMealsFragmentViewModel.fetchAllAreas()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        remoteMealsFragmentViewModel.fetchAllIngredients()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

        remoteMealsFragmentViewModel.fetchAllMealsForCategoryRemote(receivedStrCategory)
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