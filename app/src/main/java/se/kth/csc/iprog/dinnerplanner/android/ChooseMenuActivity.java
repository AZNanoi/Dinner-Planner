package se.kth.csc.iprog.dinnerplanner.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

import se.kth.csc.iprog.dinnerplanner.android.view.ActionBarView;
import se.kth.csc.iprog.dinnerplanner.android.view.ActionBarViewCtrl;
import se.kth.csc.iprog.dinnerplanner.android.view.ChooseMenuLayout;
import se.kth.csc.iprog.dinnerplanner.android.view.ChooseMenuLayoutController;
import se.kth.csc.iprog.dinnerplanner.model.DinnerModel;

public class ChooseMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.choose_menu_layout);

        DinnerModel model = ((DinnerPlannerApplication) this.getApplication()).getModel();

        // Creating the view class instance
        ChooseMenuLayout chooseMenuView = new ChooseMenuLayout(findViewById(R.id.choose_menu_layout_id), model);
        ChooseMenuLayoutController chooseMenuCtrl = new ChooseMenuLayoutController(model, chooseMenuView);

        // Creating the action bar view class instance and its controller
        ActionBarView actionBarView = new ActionBarView(findViewById(R.id.action_bar_view_id));
        ActionBarViewCtrl actionBarViewCtrl = new ActionBarViewCtrl(actionBarView);
    }
}
