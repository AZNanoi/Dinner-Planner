package se.kth.csc.iprog.dinnerplanner.android.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import se.kth.csc.iprog.dinnerplanner.android.MainActivity;

/**
 * Created by AZN on 2017-02-08.
 */

public class ActionBarViewCtrl implements View.OnClickListener{
    private ActionBarView view;

    public ActionBarViewCtrl(ActionBarView view){
        this.view = view;
        view.appName.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == view.appName){
            Intent intent = new Intent(view.context, MainActivity.class);
            view.context.startActivity(intent);
        }
    }
}
