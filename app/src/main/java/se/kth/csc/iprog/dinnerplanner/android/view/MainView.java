package se.kth.csc.iprog.dinnerplanner.android.view;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import se.kth.csc.iprog.dinnerplanner.android.R;
import se.kth.csc.iprog.dinnerplanner.model.DinnerModel;

public class MainView {

	View view;
	DinnerModel model;
	public Button start_button;

	public MainView(View view, DinnerModel model) {

		// store in the class the reference to the Android View
		this.view = view;

		// and the reference to the model
		this.model = model;

		TextView welcome_message = (TextView) view.findViewById(R.id.welcome_message);

		this.start_button = (Button) view.findViewById(R.id.start_button);

		// Setup the rest of the view abs_layout
	}

}
