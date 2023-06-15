package rs.raf.projekat_jun_lazar_bojanic_11621.fragment;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
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
import rs.raf.projekat_jun_lazar_bojanic_11621.activity.MealsSearchActivity;
import rs.raf.projekat_jun_lazar_bojanic_11621.adapter.CategoryListAdapter;
import rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel.HomeFragmentViewModel;

public class HomeFragment extends Fragment {

    private CategoryListAdapter categoryListAdapter;
    private HomeFragmentViewModel homeFragmentViewModel;
    private ProgressBar progressBarLoading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeFragmentViewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerViewCategoryList = view.findViewById(R.id.recyclerViewCategoryList);
        recyclerViewCategoryList.setLayoutManager(new LinearLayoutManager(requireContext()));
        progressBarLoading = view.findViewById(R.id.progressBarLoading);
        categoryListAdapter = new CategoryListAdapter(Collections.emptyList());
        recyclerViewCategoryList.setAdapter(categoryListAdapter);
        categoryListAdapter.setOnCategoryClickListener(category -> {
            Intent intent = new Intent(requireActivity(), MealsSearchActivity.class);
            intent.putExtra(String.valueOf(R.string.extraStrCategory), category.getStrCategory());
            startActivity(intent);
        });
        homeFragmentViewModel.getCategoryListLiveData().observe(getViewLifecycleOwner(), categoryList -> {
            categoryListAdapter.setCategoryList(categoryList);
        });
        homeFragmentViewModel.getLoadingStatusLiveData().observe(getViewLifecycleOwner(), isLoading -> {
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
        homeFragmentViewModel.setMainActivityViewModel(mainActivity.getMainActivityViewModel());

        homeFragmentViewModel.fetchAllCategories()
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
