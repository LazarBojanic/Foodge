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
import rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel.StatsViewModel;

public class StatsFragment extends Fragment {

    private StatsViewModel mViewModel;

    public static StatsFragment newInstance() {
        return new StatsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StatsViewModel.class);
        // TODO: Use the ViewModel
    }

}