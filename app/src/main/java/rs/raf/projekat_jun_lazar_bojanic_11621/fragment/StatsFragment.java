package rs.raf.projekat_jun_lazar_bojanic_11621.fragment;

import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.PersonalMealCountByDate;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.DateConverter;
import rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel.StatsFragmentViewModel;

public class StatsFragment extends Fragment {
    private BarChart barChartStats;
    private StatsFragmentViewModel statsFragmentViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statsFragmentViewModel = new ViewModelProvider(requireActivity()).get(StatsFragmentViewModel.class);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        barChartStats = view.findViewById(R.id.barChartStats);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeObservers();
        fetchInitialData();
    }
    private void initializeObservers(){
        statsFragmentViewModel.getPersonalMealCountByDateListLiveData().observe(requireActivity(), personalMealCountByDates -> {
            List<BarEntry> entries = new ArrayList<>();
            List<String> labels = new ArrayList<>(); // List to store x-axis labels
            int index = 0;

            for (PersonalMealCountByDate personalMealCountByDate : personalMealCountByDates) {
                float xValue = index;
                float yValue = personalMealCountByDate.getPersonalMealCount();

                if(!labels.contains(personalMealCountByDate.getDateOfPrep())){
                    entries.add(new BarEntry(xValue, yValue));
                    labels.add(personalMealCountByDate.getDateOfPrep());
                    index++;
                }
            }
            BarDataSet dataSet = new BarDataSet(entries, "Meal Counts");
            dataSet.setValueTextColor(getResources().getColor(R.color.colorOnSecondary));
            dataSet.setColor(Color.CYAN);
            BarData barData = new BarData(dataSet);

            // Set custom labels on the x-axis
            XAxis xAxis = barChartStats.getXAxis();
            xAxis.setTextSize(12);
            xAxis.setTextColor(getResources().getColor(R.color.colorOnSecondary));
            xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
            xAxis.setLabelCount(labels.size());
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            YAxis yAxisLeft = barChartStats.getAxisLeft();
            yAxisLeft.setTextColor(getResources().getColor(R.color.colorOnSecondary));
            YAxis yAxisRight = barChartStats.getAxisRight();
            yAxisRight.setTextColor(getResources().getColor(R.color.colorOnSecondary));

            barChartStats.getLegend().setTextColor(getResources().getColor(R.color.colorOnSecondary));
            barChartStats.setData(barData);
            barChartStats.getDescription().setEnabled(false);
            barChartStats.invalidate();
            Log.i(String.valueOf(R.string.foodgeTag), labels.toString());
        });
    }
    private void fetchInitialData(){
        statsFragmentViewModel.updateChart();
    }

}