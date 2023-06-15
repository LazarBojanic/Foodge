package rs.raf.projekat_jun_lazar_bojanic_11621.fragment;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.Collections;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.activity.MainActivity;
import rs.raf.projekat_jun_lazar_bojanic_11621.adapter.MealListAdapter;
import rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel.MealsFragmentViewModel;

public class MealsFragment extends Fragment {

    private MealListAdapter mealListAdapter;
    private MealsFragmentViewModel mealsFragmentViewModel;
    private ProgressBar progressBarLoading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mealsFragmentViewModel = new ViewModelProvider(this).get(MealsFragmentViewModel.class);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meals, container, false);
        RecyclerView recyclerViewMealList = view.findViewById(R.id.recyclerViewMealList);
        recyclerViewMealList.setLayoutManager(new LinearLayoutManager(requireContext()));
        progressBarLoading = view.findViewById(R.id.progressBarLoading);
        mealListAdapter = new MealListAdapter(Collections.emptyList());
        recyclerViewMealList.setAdapter(mealListAdapter);
        mealsFragmentViewModel.getMealListLiveData().observe(getViewLifecycleOwner(), mealList -> {
            mealListAdapter.setMealList(mealList);
        });
        mealsFragmentViewModel.getLoadingStatusLiveData().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                showLoadingView();
            } else {
                hideLoadingView();
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity mainActivity = (MainActivity)requireActivity();
        mealsFragmentViewModel.setMainActivityViewModel(mainActivity.getMainActivityViewModel());

        mealsFragmentViewModel.fetchAllMeals()
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