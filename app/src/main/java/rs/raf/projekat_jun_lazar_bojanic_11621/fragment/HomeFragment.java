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
import android.widget.ProgressBar;

import java.util.Collections;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.adapter.CategoryListAdapter;
import rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel.HomeViewModel;

public class HomeFragment extends Fragment {

    private CategoryListAdapter categoryListAdapter;
    private HomeViewModel homeViewModel;
    private ProgressBar progressBarLoading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the ViewModel
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerViewCategoryList = view.findViewById(R.id.recyclerViewCategoryList);
        recyclerViewCategoryList.setLayoutManager(new LinearLayoutManager(requireContext()));
        progressBarLoading = view.findViewById(R.id.progressBarLoading);
        categoryListAdapter = new CategoryListAdapter(Collections.emptyList());
        recyclerViewCategoryList.setAdapter(categoryListAdapter);
        homeViewModel.getCategoryListLiveData().observe(getViewLifecycleOwner(), categoryList -> {
            categoryListAdapter.setCategoryList(categoryList);
        });
        homeViewModel.getLoadingStatusLiveData().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                showLoadingView();
            } else {
                hideLoadingView();
            }
        });
        homeViewModel.fetchAllCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

        return view;
    }
    private void showLoadingView() {
        progressBarLoading.setVisibility(View.VISIBLE);
    }
    private void hideLoadingView() {
        progressBarLoading.setVisibility(View.GONE);
    }
}
