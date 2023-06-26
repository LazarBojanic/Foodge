package rs.raf.projekat_jun_lazar_bojanic_11621.adapter;

import android.app.Person;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rs.raf.projekat_jun_lazar_bojanic_11621.FoodgeApp;
import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.activity.EditPersonalMealActivity;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.model.PersonalMeal;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.local.repository.PersonalMealDao;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.PlaceHolders;

public class PersonalMealListAdapter extends RecyclerView.Adapter<PersonalMealListAdapter.MealViewHolder> {
    private List<PersonalMeal> mealListLocal;
    private OnPersonalMealClickListener onPersonalMealClickListener;
    public void setOnPersonalMealClickListener(OnPersonalMealClickListener onPersonalMealClickListener) {
        this.onPersonalMealClickListener = onPersonalMealClickListener;
    }

    public PersonalMealListAdapter(List<PersonalMeal> mealListRemote) {
        this.mealListLocal = mealListRemote;
    }

    public List<PersonalMeal> getMealListLocal() {
        return mealListLocal;
    }

    public void setMealListLocal(List<PersonalMeal> mealListLocal) {
        this.mealListLocal = mealListLocal;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_personal_meal, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        PersonalMeal personalMeal = mealListLocal.get(position);
        holder.bindLocal(personalMeal);
    }

    @Override
    public int getItemCount() {
        return mealListLocal.size();
    }
    public interface OnPersonalMealClickListener {
        void onPersonalMealClick(PersonalMeal personalMeal);
    }
    class MealViewHolder extends RecyclerView.ViewHolder {
        private PersonalMealDao personalMealDao;
        private PersonalMeal personalMeal;
        private TextView textViewMealName;
        private ImageView imageViewMealImage;
        private Button buttonEditPersonalMeal;
        private Button buttonDeletePersonalMeal;
        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            personalMealDao = FoodgeApp.getInstance().getLocalAppComponent().getPersonalMealDao();
            textViewMealName = itemView.findViewById(R.id.textViewMealName);
            imageViewMealImage = itemView.findViewById(R.id.imageViewMealImage);
            buttonEditPersonalMeal = itemView.findViewById(R.id.buttonEditPersonalMeal);
            buttonDeletePersonalMeal = itemView.findViewById(R.id.buttonDeletePersonalMeal);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onPersonalMealClickListener != null) {
                    PersonalMeal clickedPersonalMeal = mealListLocal.get(position);
                    onPersonalMealClickListener.onPersonalMealClick(clickedPersonalMeal);
                }
            });

            buttonEditPersonalMeal.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), EditPersonalMealActivity.class);
                intent.putExtra(String.valueOf(R.string.extraPersonalMeal), personalMeal);
                itemView.getContext().startActivity(intent);
            });
            buttonDeletePersonalMeal.setOnClickListener(v -> {
                personalMealDao.deletePersonalMealById(personalMeal.getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            personalMealDao.getAllMeals()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(personalMeals -> {
                                        setMealListLocal(personalMeals);
                                        notifyDataSetChanged();
                                    });
                        });
            });

        }
        public void bindLocal(PersonalMeal personalMeal) {
            this.personalMeal = personalMeal;
            textViewMealName.setText(this.personalMeal.getStrMeal());
            File file = new File(personalMeal.getMealImagePath());
            Glide.with(itemView)
                    .load(file)
                    .error(PlaceHolders.getInstance().getPlaceHolderImage())
                    .placeholder(PlaceHolders.getInstance().getPlaceHolderImage())
                    .into(imageViewMealImage);
        }
    }
}
