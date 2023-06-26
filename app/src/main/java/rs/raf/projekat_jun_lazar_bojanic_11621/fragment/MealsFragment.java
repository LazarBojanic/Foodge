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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.activity.MealDetailsActivity;
import rs.raf.projekat_jun_lazar_bojanic_11621.adapter.MealListAdapter;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.PersonalMeal;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Meal;
import rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel.MealsFragmentViewModel;

public class MealsFragment extends Fragment {
    public static Integer PAGE_SIZE = 10;
    private Integer currentPage;
    private Integer pageCount;
    private MealsFragmentViewModel mealsFragmentViewModel;
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
    private RadioGroup radioGroupRepositoryOptions;
    private RadioButton radioButtonPersonal;
    private RadioButton radioButtonWeb;
    //private Spinner spinnerTagFilter;
    ArrayAdapter<String> spinnerCategoryFilterAdapter;
    ArrayAdapter<String> spinnerAreaFilterAdapter;
    ArrayAdapter<String> spinnerIngredientFilterAdapter;
    //ArrayAdapter<String> spinnerTagFilterAdapter;
    private Button buttonPrevPage;
    private Button buttonNextPage;
    private Boolean useRemoteRepository;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mealsFragmentViewModel = new ViewModelProvider(this).get(MealsFragmentViewModel.class);
        initializeViewModel();

        currentPage = 1;
        pageCount = 1;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meals, container, false);
        initializeViews(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        editTextSearch = view.findViewById(R.id.editTextSearch);
        radioGroupRepositoryOptions = view.findViewById(R.id.radioGroupRepositoryOptions);
        radioButtonPersonal = view.findViewById(R.id.radioButtonPersonal);
        radioButtonWeb = view.findViewById(R.id.radioButtonWeb);
        RecyclerView recyclerViewMealList = view.findViewById(R.id.recyclerViewMealList);
        recyclerViewMealList.setLayoutManager(new LinearLayoutManager(requireActivity()));
        mealListAdapter = new MealListAdapter(Collections.emptyList(), Collections.emptyList());
        recyclerViewMealList.setAdapter(mealListAdapter);
    }

    private void initializeViewModel() {
        mealsFragmentViewModel = new ViewModelProvider(this).get(MealsFragmentViewModel.class);
        mealsFragmentViewModel.getMealListLiveData().observe(this, mealList -> {
            currentPage = 1;
            List<Meal> mealsInPage = getItemsForPage(mealList, currentPage);
            mealListAdapter.setMealListRemote(mealsInPage);
            pageCount = mealList.size() / PAGE_SIZE;
            updatePaginationButtons();
        });
        mealsFragmentViewModel.getPersonalMealListLiveData().observe(this, personalMealList -> {
            currentPage = 1;
            List<PersonalMeal> personalMealsInPage = getItemsForPageLocal(personalMealList, currentPage);
            mealListAdapter.setMealListLocal(personalMealsInPage);
            pageCount = personalMealList.size() / PAGE_SIZE;
            updatePaginationButtons();
        });
        mealsFragmentViewModel.getLoadingStatusLiveData().observe(this, isLoading -> {
            if (isLoading) {
                showLoadingView();
            } else {
                hideLoadingView();
            }
        });
        mealsFragmentViewModel.getSimpleCategoryListLiveData().observe(this, categories -> {
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
        mealsFragmentViewModel.getSimpleAreaListLiveData().observe(this, areas -> {
            spinnerAreaFilterAdapter = new ArrayAdapter<>(requireActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, areas);
            spinnerAreaFilterAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
            spinnerAreaFilter.setAdapter(spinnerAreaFilterAdapter);
        });
        mealsFragmentViewModel.getSimpleIngredientListLiveData().observe(this, ingredients -> {
            spinnerIngredientFilterAdapter = new ArrayAdapter<>(requireActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, ingredients);
            spinnerIngredientFilterAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
            spinnerIngredientFilter.setAdapter(spinnerIngredientFilterAdapter);
        });
        /*mealsSearchActivityViewModel.getSimpleCategoryListLiveData().observe(this, categories -> {
            spinnerTagFilterAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categories);
            spinnerTagFilterAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
            spinnerCategoryFilter.setAdapter(spinnerTagFilterAdapter);
        });*/
        mealsFragmentViewModel.setupSearchObserverRemote();
    }

    private void initializeListeners() {
        radioGroupRepositoryOptions.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId == R.id.radioButtonPersonal){
                useRemoteRepository = false;
            }
            else if(checkedId == R.id.radioButtonWeb){
                useRemoteRepository = true;
            }
        });
        mealListAdapter.setOnMealClickListener(meal -> {
            Intent intent = new Intent(requireActivity(), MealDetailsActivity.class);
            intent.putExtra(String.valueOf(R.string.extraIdMeal), meal.getIdMeal());
            startActivity(intent);
        });
        spinnerCategoryFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStrCategory = spinnerCategoryFilter.getItemAtPosition(position).toString();
                if(useRemoteRepository){
                    mealsFragmentViewModel.fetchAllMealsForCategoryRemote(selectedStrCategory)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe();
                }
                else{
                    mealsFragmentViewModel.fetchAllMealsForCategoryLocal(selectedStrCategory)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerAreaFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStrArea = spinnerAreaFilter.getItemAtPosition(position).toString();
                if(useRemoteRepository){
                    mealsFragmentViewModel.fetchAllMealsForAreaRemote(selectedStrArea)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe();
                }
                else{
                    mealsFragmentViewModel.fetchAllMealsForAreaLocal(selectedStrArea)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerIngredientFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStrIngredient = spinnerIngredientFilter.getItemAtPosition(position).toString();
                if(useRemoteRepository){
                    mealsFragmentViewModel.fetchAllMealsForIngredientRemote(selectedStrIngredient)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe();
                }
                else{
                    mealsFragmentViewModel.fetchAllMealsForIngredientLocal(selectedStrIngredient)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe();
                }
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
                mealsFragmentViewModel.onSearchTextChanged(s.toString());
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
                List<Meal> previousPageItems = getItemsForPage(Objects.requireNonNull(mealsFragmentViewModel.getMealListLiveData().getValue()), currentPage);
                mealListAdapter.setMealListRemote(previousPageItems);
            }
        });

        buttonNextPage.setOnClickListener(v -> {
            if (currentPage < pageCount) {
                currentPage++;
                updatePaginationButtons();
                List<Meal> nextPageItems = getItemsForPage(Objects.requireNonNull(mealsFragmentViewModel.getMealListLiveData().getValue()), currentPage);
                mealListAdapter.setMealListRemote(nextPageItems);
            }
        });
    }

    private void fetchInitialData() {
        useRemoteRepository = true;
        Bundle receivedArguments = getArguments();
        if(receivedArguments == null || receivedArguments.isEmpty()){
            receivedStrCategory = "Dessert";
        }
        else{
            receivedStrCategory = receivedArguments.getString(getString(R.string.extraStrCategory));
        }
        Log.i(String.valueOf(R.string.foodgeTag), receivedStrCategory);

        mealsFragmentViewModel.fetchAllCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        mealsFragmentViewModel.fetchAllAreas()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        mealsFragmentViewModel.fetchAllIngredients()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

        mealsFragmentViewModel.fetchAllMealsForCategoryRemote(receivedStrCategory)
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
    private List<PersonalMeal> getItemsForPageLocal(List<PersonalMeal> personalMeals, int page) {
        int startIndex = (page - 1) * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, personalMeals.size());
        return personalMeals.subList(startIndex, endIndex);
    }
    private void updatePaginationButtons() {
        buttonPrevPage.setEnabled(currentPage > 1);
        buttonNextPage.setEnabled(currentPage < pageCount);
    }
}