package se.kth.csc.iprog.dinnerplanner.android.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import se.kth.csc.iprog.dinnerplanner.android.R;
import se.kth.csc.iprog.dinnerplanner.model.DinnerModel;
import se.kth.csc.iprog.dinnerplanner.model.Dish;
import se.kth.csc.iprog.dinnerplanner.model.Ingredient;

/**
 * Created by AZN on 2017-01-23.
 */

public class MenuLayout implements Observer {

    View view;
    DinnerModel model;
    TextView numberOfGuestsV;
    TextView totalCostV;
    public Button back_button;
    public ImageView starter_dish;
    public TextView starter_dish_name;
    public ImageView main_dish;
    public TextView main_dish_name;
    public ImageView dessert_dish;
    public TextView dessert_dish_name;
    public LinearLayout textDisplay;
    public ImageView ingredient;
    public TextView textDisplayTitle;
    public TextView numberOfGuests;
    Context context;

    public MenuLayout(View view, DinnerModel model) {

        // store in the class the reference to the Android View
        this.view = view;

        // and the reference to the model
        this.model = model;
        this.context = view.getContext();

        this.numberOfGuestsV = (TextView) view.findViewById(R.id.numberOfGuests);
        this.numberOfGuestsV.setText(model.getNumberOfGuests() + " pers");

        this.totalCostV = (TextView) view.findViewById(R.id.totalCost);
        this.totalCostV.setText("Total price: " + model.getTotalMenuPrice() + "kr");

        this.back_button = (Button) view.findViewById(R.id.back_button);

        this.starter_dish = (ImageView)  view.findViewById(R.id.starter_dish);
        this.starter_dish_name = (TextView)  view.findViewById(R.id.starter_dish_name);
        this.main_dish = (ImageView)  view.findViewById(R.id.main_dish);
        this.main_dish_name = (TextView)  view.findViewById(R.id.main_dish_name);
        this.dessert_dish = (ImageView)  view.findViewById(R.id.dessert_dish);
        this.dessert_dish_name = (TextView)  view.findViewById(R.id.dessert_dish_name);
        this.textDisplay = (LinearLayout) view.findViewById(R.id.textDisplay);
        this.ingredient = (ImageView) view.findViewById(R.id.ingredient);
        this.textDisplayTitle = (TextView)  view.findViewById(R.id.textDisplayTitle);
        this.numberOfGuests = (TextView)  view.findViewById(R.id.numberOfGuests);

        dishplayDishes();
        displayIngredients();

        // Setup the rest of the view abs_layout

        model.addObserver(this);

    }

    private void dishplayDishes(){
        Integer[] dishTypes = new Integer[] {1,2,3};

        //Display selected dishes
        for(Integer type: dishTypes){
            Dish d = model.getSelectedDish(type);
            System.out.println(d);
            if(!d.getName().equals("null")){
                /*Resources resources = this.context.getResources();
                String imgName = d.getImage().replace(".jpg", "").trim();
                final int resourceId = resources.getIdentifier(imgName, "drawable", this.context.getPackageName());*/

                try{
                    URL url = new URL(d.getImage());
                    InputStream is = (InputStream) url.getContent();
                    Drawable drawable = Drawable.createFromStream(is, "src name");
                    if(d.getType() == 1){
                        displayDishes(d, starter_dish_name, starter_dish, drawable);
                    }else if(d.getType() == 2){
                        displayDishes(d, main_dish_name, main_dish, drawable);
                    }else if(d.getType() == 3){
                        displayDishes(d, dessert_dish_name, dessert_dish, drawable);
                    }
                }catch (Exception e){
                    System.out.println("Error:"+e);
                }
            }
        }
    }

    //Display name, image and set tag for tag dish element
    private void displayDishes(Dish d, TextView name_container, ImageView img_container, Drawable drawable){
        if(d.getName().length() >= 10){
            name_container.setText(d.getName().substring(0,10));
        }else{
            name_container.setText(d.getName());
        }
        img_container.setImageDrawable(drawable);
        img_container.setTag(d);
    }

    public void removeImageViewBorder(){
        RelativeLayout layout = (RelativeLayout) view;
        for(int i = 0; i < layout.getChildCount(); i++){
            View childView = layout.getChildAt(i);
            if(childView instanceof ImageView){
                childView.setBackgroundResource(0);
            }
        }
    }

    public void displayIngredients(){
        //Display ingredients for all the dishes on the menu.
        textDisplayTitle.setText(R.string.ingredients);
        String noGuests = String.valueOf(model.getNumberOfGuests()) + " pers";
        numberOfGuestsV.setText(noGuests);

        if(textDisplay.getChildCount() > 0)
            textDisplay.removeAllViews();

        Set<Ingredient> all_ingredients = model.getAllIngredients();
        for(Ingredient ing: all_ingredients){
            View ingredient_view_row = LayoutInflater.from(context).inflate(R.layout.ingredient_view_row, null);
            TextView ingName = (TextView) ingredient_view_row.findViewWithTag("Ingredient Name");
            TextView ingAmountUnit = (TextView) ingredient_view_row.findViewWithTag("Amount and Unit");

            ingName.setText(ing.getName());
            String amountUnit = String.valueOf(ing.getQuantity() * model.getNumberOfGuests()) + ing.getUnit();
            ingAmountUnit.setText(amountUnit);

            textDisplay.addView(ingredient_view_row);
        }
    }

    public void displayInstruction(Dish dish, int title){
        textDisplayTitle.setText(title);
        numberOfGuestsV.setText("");

        TextView dishName = new TextView(context);
        dishName.setText(dish.getName());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,0,15);
        dishName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        dishName.setLayoutParams(params);

        TextView instruction = new TextView(context);
        instruction.setText(dish.getDescription());

        if(textDisplay.getChildCount() > 0)
            textDisplay.removeAllViews();

        textDisplay.addView(dishName);
        textDisplay.addView(instruction);
    }


    @Override
    public void update(Observable observable, Object arg){
        if(arg.equals("numberOfGuests")){
            System.out.println("setnumberOfGuests111");
            String nGuests = this.model.getNumberOfGuests() + " pers";
            this.numberOfGuestsV.setText(nGuests);
            this.totalCostV.setText("Total price: " + model.getTotalMenuPrice()+ "kr");
        }
        if(arg.equals("dishAddedToMenu")){
            this.totalCostV.setText("Total price: " + model.getTotalMenuPrice()+ "kr");
            dishplayDishes();
            displayIngredients();
        }
    }

}
