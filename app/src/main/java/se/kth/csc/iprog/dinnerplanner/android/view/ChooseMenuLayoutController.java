package se.kth.csc.iprog.dinnerplanner.android.view;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import se.kth.csc.iprog.dinnerplanner.android.MenuLayoutActivity;
import se.kth.csc.iprog.dinnerplanner.android.R;
import se.kth.csc.iprog.dinnerplanner.model.DinnerModel;
import se.kth.csc.iprog.dinnerplanner.model.Dish;
import se.kth.csc.iprog.dinnerplanner.model.Ingredient;

/**
 * Created by AZN on 2017-01-30.
 */

public class ChooseMenuLayoutController implements OnClickListener{
    DinnerModel model;
    ChooseMenuLayout view;

    public ChooseMenuLayoutController(DinnerModel model, ChooseMenuLayout view){
        this.model = model;
        this.view = view;

        //Here we setup the listeners
        view.plusButton.setOnClickListener(this);
        view.minusButton.setOnClickListener(this);
        view.create_menu_button.setOnClickListener(this);

        view.popupView.findViewById(R.id.closeButton).setOnClickListener(this);
        view.popupView.findViewById(R.id.select_dish_button).setOnClickListener(this);


        /*System.out.println(view.dishesView);
        for(ImageView v: view.dishesView){
            v.setOnClickListener(this);
        }*/
    }

    // TODO: Update dishes when back button is clicked in menu layout
    // TODO: Convert total price to two decimals
    // TODO: Change border on images in menu layout when clicked

    // This is the method of that we need to implement when implementing
    // the OnClickListener. Notice that the View here is an Android View
    // class (parent class of all the components), not the view we talk
    // about in the lab instructions.
    @Override
    public void onClick(View v){
        if(v == view.plusButton){
            Log.d("Click", "plusButton");
            int newNr = model.getNumberOfGuests() + 1;
            //We update the model
            if(newNr <= 10){
                model.setNumberOfGuests(newNr);
            }
        }else if(v == view.minusButton){
            Log.d("Click", "minusButton");
            int newNr = model.getNumberOfGuests() - 1;
            if(newNr >= 0){
                model.setNumberOfGuests(newNr);
            }
        }else if(v == view.popupView.findViewById(R.id.closeButton)){
            view.popupWindow.dismiss();
        }else if (v == view.create_menu_button){
            Set<Dish> selectedDishes = model.getFullMenu();
            if(selectedDishes.size() > 0){
                Intent intent = new Intent(view.context, MenuLayoutActivity.class);
                view.context.startActivity(intent);
            }else{
                Toast.makeText(view.context, "Menu is empty. Please select a dish!", Toast.LENGTH_SHORT).show();
            }

        }else if(v == view.popupView.findViewById(R.id.select_dish_button)){
            view.popupWindow.dismiss();
            Dish dish = (Dish) v.getTag();
            System.out.println("setTagIng2:::"+dish.getIngredients().size());
            model.addDishToMenu(dish);

            LinearLayout dishView = (LinearLayout) view.view.findViewWithTag(dish).getParent();
            LinearLayout dishViewContainer = (LinearLayout) dishView.getParent();
            View previousSelected = dishViewContainer.findViewWithTag("Selected");
            if(previousSelected != null){
                previousSelected.setBackgroundResource(0);
                previousSelected.setTag("");
            }
            dishView.setTag("Selected");
            dishView.setBackgroundResource(R.drawable.border);
        }
    }
}
