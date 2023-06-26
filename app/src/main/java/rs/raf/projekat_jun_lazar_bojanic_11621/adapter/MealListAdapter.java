package rs.raf.projekat_jun_lazar_bojanic_11621.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.PersonalMeal;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Meal;

public class MealListAdapter extends RecyclerView.Adapter<MealListAdapter.MealViewHolder> {
    private List<Meal> mealListRemote;
    private List<PersonalMeal> personalMealList;
    private OnMealClickListener onMealClickListener;
    public void setOnMealClickListener(OnMealClickListener onMealClickListener) {
        this.onMealClickListener = onMealClickListener;
    }

    public MealListAdapter(List<Meal> mealListRemote, List<PersonalMeal> personalMealList) {
        this.mealListRemote = mealListRemote;
        this.personalMealList = personalMealList;
    }

    public List<PersonalMeal> getPersonalMealList() {
        return personalMealList;
    }

    public List<Meal> getMealListRemote() {
        return mealListRemote;
    }

    public void setMealListRemote(List<Meal> mealListRemote) {
        this.mealListRemote = mealListRemote;
        this.personalMealList = Collections.emptyList();
        notifyDataSetChanged();
    }
    public void setMealListLocal(List<PersonalMeal> personalMealList) {
        this.mealListRemote = Collections.emptyList();;
        this.personalMealList = personalMealList;
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
        if(mealListRemote.isEmpty()){
            PersonalMeal personalMeal = personalMealList.get(position);
            holder.bindLocal(personalMeal);
        }
        else if(personalMealList.isEmpty()){
            Meal meal = mealListRemote.get(position);
            holder.bindRemote(meal);
        }
    }

    @Override
    public int getItemCount() {
        return mealListRemote.size();
    }
    public interface OnMealClickListener {
        void onMealClick(Meal meal);
    }
    class MealViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewMealName;
        private ImageView imageViewMealImageThumbnail;
        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMealName = itemView.findViewById(R.id.textViewMealName);
            imageViewMealImageThumbnail = itemView.findViewById(R.id.imageViewMealImageThumbnail);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onMealClickListener != null) {
                    Meal meal = mealListRemote.get(position);
                    onMealClickListener.onMealClick(meal);
                }
            });
        }


        public void bindRemote(Meal meal) {
            textViewMealName.setText(meal.getStrMeal());
            imageViewMealImageThumbnail.setImageBitmap(meal.getMealImageThumbnail());
        }
        public void bindLocal(PersonalMeal personalMeal) {
            textViewMealName.setText(personalMeal.getStrMeal());
            imageViewMealImageThumbnail.setImageBitmap(personalMeal.getMealImage());
        }
    }
}
