package rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.response;

import java.util.List;

import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Meal;

public class MealsResponse {
    private List<Meal> meals;

    public MealsResponse() {
    }

    public MealsResponse(List<Meal> meals) {
        this.meals = meals;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    @Override
    public String toString() {
        return "MealsResponse{" +
                "meals=" + meals +
                '}';
    }
}
