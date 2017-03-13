package se.kth.csc.iprog.dinnerplanner.android.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import se.kth.csc.iprog.dinnerplanner.android.R;

/**
 * Created by AZN on 2017-02-08.
 */

public class ActionBarView {
    public TextView appName;
    Context context;
    public ActionBarView(View v){
        this.context = v.getContext();
        this.appName = (TextView) v.findViewById(R.id.appName);
    }
}
