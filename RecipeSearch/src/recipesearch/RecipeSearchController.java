
package recipesearch;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.SplitPane;

import java.util.List;

public class RecipeSearchController implements Initializable {

    RecipeDatabase db = RecipeDatabase.getSharedInstance();

    @FXML
    private AnchorPane searchPane;
    @FXML
    private AnchorPane recipeDetailPane;
    @FXML
    private SplitPane splitPane;
    @FXML
    private Slider tidSlider;
    @FXML
    private Spinner<Integer> prisSpinner;
    @FXML
    private ComboBox kökComboBox;
    @FXML
    private ComboBox huvudingrediensComboBox;
    @FXML
    private RadioButton SA;
    @FXML
    private RadioButton SL;
    @FXML
    private RadioButton SM;
    @FXML
    private RadioButton SS;
    @FXML
    private  AnchorPane detailPane;
    @FXML
    private Label tidVisare;
    @FXML
    private Label bigRecipeName;
    @FXML
    private  ImageView bigRecipeImage;
    @FXML
    private FlowPane recipeListFlowPane;

    private Map<String, RecipeListItem> recipeListItemMap = new HashMap<String, RecipeListItem>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (Recipe recipe : RecipeBackendController.getRecipes(db)) {
            RecipeListItem recipeListItem = new RecipeListItem(recipe, this);
            recipeListItemMap.put(recipe.getName(), recipeListItem);
        }
        updateRecipeList();
        RadioButton[] radioButtonList = new RadioButton[]{SA, SL, SM, SS };
        huvudingrediensComboBox.getItems().addAll("Visa alla", "Kött", "Kyckling", "Fisk", "Vegetarisk");
        huvudingrediensComboBox.getSelectionModel().select("Visa alla");
        kökComboBox.getItems().addAll("Visa alla","Sverige", "Frankrike", "Grekland", "Asien", "Indien", "Afrika"); //kankse stavat fel
        kökComboBox.getSelectionModel().select("Visa alla");
        huvudingrediensComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                RecipeBackendController.setMainIngredient(newValue);
                updateRecipeList();
            }
        });
        ToggleGroup difficultyToggleGroup = new ToggleGroup();
        for( RadioButton button : radioButtonList ){
            button.setToggleGroup(difficultyToggleGroup);
        }
        kökComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                RecipeBackendController.setCuisine(newValue);
                updateRecipeList();
            }
        });



        SA.setSelected(true);
        difficultyToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {

                if (difficultyToggleGroup.getSelectedToggle() != null) {
                    RadioButton selected = (RadioButton) difficultyToggleGroup.getSelectedToggle();
                    RecipeBackendController.setDifficulty(selected.getText());
                    updateRecipeList();
                }
            }
        });
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 150, 10, 5);
        prisSpinner.setValueFactory(valueFactory);

        prisSpinner.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                int currentValue = 0;
                currentValue  = prisSpinner.getValue();
                RecipeBackendController.setMaxPrice(currentValue);
                updateRecipeList();

            }
        });
        prisSpinner.focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if(newValue){
                    //focusgained - do nothing
                }
                else{
                    Integer value = Integer.valueOf(prisSpinner.getEditor().getText());
                    RecipeBackendController.setMaxPrice(value);
                    updateRecipeList();
                }

            }
        });
        tidSlider.valueProperty().addListener(new ChangeListener<Number>() {
            private Number oldValue;
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                Number newValue = tidSlider.getValue();
                int time = number.intValue();
                time = (((time + 5)/10)*10); // tiotal
                tidVisare.setText(String.valueOf(time));
                if(newValue != null && !newValue.equals(oldValue) && !tidSlider.isValueChanging()) {
                    RecipeBackendController.setMaxTime(time);
                    updateRecipeList();
                }
                oldValue = newValue;
            }
        });
    }


    private void updateRecipeList(){
        recipeListFlowPane.getChildren().clear();
        List<Recipe> recipeList = RecipeBackendController.getRecipes(db);
        for (Recipe recipe : recipeList) {
            RecipeListItem recipeListItem = recipeListItemMap.get(recipe.getName());
            recipeListFlowPane.getChildren().add(recipeListItem);
            }
        }

    private  void populateRecipeDetailView(Recipe recipe){
        bigRecipeName.setText(recipe.getName());
        bigRecipeImage.setImage(recipe.getFXImage());
    }

    @FXML
    public void closeRecipeView(){
        splitPane.toFront();
    }

    public void openRecipeView(Recipe recipe){
        populateRecipeDetailView(recipe);
        detailPane.toFront();
    }

}
