package rs.raf.projekat_jun_lazar_bojanic_11621.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel.FoodsViewModel;

public class FoodsFragment extends Fragment {

    private FoodsViewModel mViewModel;

    public static FoodsFragment newInstance() {
        return new FoodsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_foods, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FoodsViewModel.class);
        // TODO: Use the ViewModel
        
    }

}