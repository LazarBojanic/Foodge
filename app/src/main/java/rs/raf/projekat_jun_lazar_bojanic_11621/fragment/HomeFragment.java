package rs.raf.projekat_jun_lazar_bojanic_11621.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;

import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.adapter.MealListAdapter;
import rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel.HomeViewModel;

public class HomeFragment extends Fragment {

    private MealListAdapter mealListAdapter;
    private HomeViewModel homeViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the ViewModel
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // Fetch all meals when the HomeFragment is created
        homeViewModel.fetchAllMeals();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerViewMeals = view.findViewById(R.id.recyclerViewMealList);
        recyclerViewMeals.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize the adapter with an empty list
        mealListAdapter = new MealListAdapter(Collections.emptyList());
        recyclerViewMeals.setAdapter(mealListAdapter);

        // Observe the MutableLiveData for the list of meals
        homeViewModel.getMealListLiveData().observe(getViewLifecycleOwner(), mealList -> {
            // Update the adapter with the new list of meals
            mealListAdapter.setMealList(mealList);
        });

        return view;
    }

}