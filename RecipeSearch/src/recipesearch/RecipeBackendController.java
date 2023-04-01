package recipesearch;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;
import se.chalmers.ait.dat215.lab2.SearchFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeBackendController {
    private static String cuisine = null;
    private static String mainIngredient = null;
    private static String difficulty = null;
    private static int maxPrice = 0;
    private static int maxTime = 0;

    public RecipeBackendController(){

    }
    public static List<Recipe> getRecipes(RecipeDatabase db){
        List<Recipe> recipes = db.search(new SearchFilter(difficulty, maxTime, cuisine, maxPrice, mainIngredient));
        return recipes;
    }
    public static void setCuisine(String newCuisine){
        cuisine = newCuisine;
    }
    public static void setMainIngredient(String mainingredient){
        mainIngredient = mainingredient;
    }
    public static void setDifficulty(String newDifficulty){
        difficulty = newDifficulty;
    }
    public static void setMaxPrice(int newMaxPrice){
        maxPrice = newMaxPrice;
    }
    public static void setMaxTime(int newmaxTime){
        maxTime = newmaxTime;
    }

}
