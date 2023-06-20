package rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Area;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Ingredient;

public class IngredientsResponse {
    @JsonProperty("meals")
    private List<Ingredient> ingredients;

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "IngredientsResponse{" +
                "ingredients=" + ingredients +
                '}';
    }
}
