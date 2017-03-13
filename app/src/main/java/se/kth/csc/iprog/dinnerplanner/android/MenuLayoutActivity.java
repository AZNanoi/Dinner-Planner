package se.kth.csc.iprog.dinnerplanner.android;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Set;

import se.kth.csc.iprog.dinnerplanner.android.view.ActionBarView;
import se.kth.csc.iprog.dinnerplanner.android.view.ActionBarViewCtrl;
import se.kth.csc.iprog.dinnerplanner.android.view.MenuLayout;
import se.kth.csc.iprog.dinnerplanner.model.DinnerModel;
import se.kth.csc.iprog.dinnerplanner.model.Dish;
import se.kth.csc.iprog.dinnerplanner.model.Ingredient;

/**
 * Created by AZN on 2017-01-23.
 */

public class MenuLayoutActivity extends Activity {
    DinnerModel model;
    MenuLayout menuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.menu_layout);

        this.model = ((DinnerPlannerApplication) this.getApplication()).getModel();

        // Creating the view class instance
        this.menuView = new MenuLayout(findViewById(R.id.menu_layout_id), model);

        // Creating the action bar view class instance and its controller
        ActionBarView actionBarView = new ActionBarView(findViewById(R.id.action_bar_view_id));
        ActionBarViewCtrl actionBarViewCtrl = new ActionBarViewCtrl(actionBarView);

        menuView.back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuLayoutActivity.this, ChooseMenuActivity.class);
                startActivity(intent);
            }
        });

        menuView.ingredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuView.removeImageViewBorder();
                v.setBackgroundResource(R.drawable.border);
                menuView.displayIngredients();
            }
        });

        menuView.starter_dish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuView.removeImageViewBorder();
                v.setBackgroundResource(R.drawable.border);
                menuView.displayInstruction((Dish) v.getTag(), R.string.starter);
            }
        });

        menuView.main_dish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuView.removeImageViewBorder();
                v.setBackgroundResource(R.drawable.border);
                menuView.displayInstruction((Dish) v.getTag(), R.string.main_course);
            }
        });

        menuView.dessert_dish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuView.removeImageViewBorder();
                v.setBackgroundResource(R.drawable.border);
                menuView.displayInstruction((Dish) v.getTag(), R.string.dessert);
            }
        });
    }
}
