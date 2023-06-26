package rs.raf.projekat_jun_lazar_bojanic_11621.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Meal;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.PlaceHolders;

public class RemoteMealListAdapter extends RecyclerView.Adapter<RemoteMealListAdapter.MealViewHolder> {
    private List<Meal> mealListRemote;
    private OnRemoteMealClickListener onRemoteMealClickListener;
    public void setOnMealClickListener(OnRemoteMealClickListener onRemoteMealClickListener) {
        this.onRemoteMealClickListener = onRemoteMealClickListener;
    }

    public RemoteMealListAdapter(List<Meal> mealListRemote) {
        this.mealListRemote = mealListRemote;
    }

    public List<Meal> getMealListRemote() {
        return mealListRemote;
    }

    public void setMealListRemote(List<Meal> mealListRemote) {
        this.mealListRemote = mealListRemote;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_remote_meal, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = mealListRemote.get(position);
        holder.bindRemote(meal);
    }

    @Override
    public int getItemCount() {
        return mealListRemote.size();
    }
    public interface OnRemoteMealClickListener {
        void onRemoteMealClick(Meal meal);
    }
    class MealViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewMealName;
        private ImageView imageViewMealImage;

        public MealViewHolder(@NonNull View view) {
            super(view);
            textViewMealName = itemView.findViewById(R.id.textViewMealName);
            imageViewMealImage = itemView.findViewById(R.id.imageViewMealImage);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onRemoteMealClickListener != null) {
                    Meal clickedMeal = mealListRemote.get(position);
                    onRemoteMealClickListener.onRemoteMealClick(clickedMeal);
                }
            });
        }
        public void bindRemote(Meal meal) {
            textViewMealName.setText(meal.getStrMeal());
            if(meal.getStrMealThumb() != null){
                Glide.with(itemView)
                        .load(meal.getStrMealThumb())
                        .error(PlaceHolders.getInstance().getPlaceHolderImage())
                        .placeholder(PlaceHolders.getInstance().getPlaceHolderImage()).into(imageViewMealImage);
            }
            else{
                Glide.with(itemView)
                        .load(PlaceHolders.getInstance().getPlaceHolderImage())
                        .error(PlaceHolders.getInstance().getPlaceHolderImage())
                        .placeholder(PlaceHolders.getInstance().getPlaceHolderImage())
                        .into(imageViewMealImage);
            }
        }
    }
}
