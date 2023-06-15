package rs.raf.projekat_jun_lazar_bojanic_11621.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Category;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Meal;

public class MealListAdapter extends RecyclerView.Adapter<MealListAdapter.MealViewHolder> {
    private List<Meal> mealList;

    private OnMealClickListener onMealClickListener;
    public void setOnMealClickListener(OnMealClickListener onMealClickListener) {
        this.onMealClickListener = onMealClickListener;
    }

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
    public interface OnMealClickListener {
        void onMealClick(Meal meal);
    }
    class MealViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewMealName;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMealName = itemView.findViewById(R.id.textViewMealName);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onMealClickListener != null) {
                    Meal meal = mealList.get(position);
                    onMealClickListener.onMealClick(meal);
                }
            });
        }

        public void bind(Meal meal) {
            textViewMealName.setText(meal.getStrMeal());
        }
    }
}
