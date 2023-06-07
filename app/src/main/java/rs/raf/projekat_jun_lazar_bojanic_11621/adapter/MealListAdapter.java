package rs.raf.projekat_jun_lazar_bojanic_11621.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Meal;

public class MealListAdapter extends RecyclerView.Adapter<MealListAdapter.MealViewHolder> {
    private List<Meal> mealList;

    public MealListAdapter(List<Meal> mealList) {
        this.mealList = mealList;
    }

    public List<Meal> getMealList() {
        return mealList;
    }

    public void setMealList(List<Meal> mealList) {
        this.mealList = mealList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        holder.bind(meal);
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
        }

        public void bind(Meal meal) {
            textViewName.setText(meal.getStrMeal());
        }
    }
}
