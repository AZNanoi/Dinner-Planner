package se.kth.csc.iprog.dinnerplanner.android.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import se.kth.csc.iprog.dinnerplanner.android.R;
import se.kth.csc.iprog.dinnerplanner.android.api.AsyncData;
import se.kth.csc.iprog.dinnerplanner.model.DinnerModel;
import se.kth.csc.iprog.dinnerplanner.model.Dish;
import se.kth.csc.iprog.dinnerplanner.model.Ingredient;

/**
 * Created by AZN on 2017-01-23.
 */

public class ChooseMenuLayout implements Observer {

    View view;
    DinnerModel model;
    Context context;
    TextView plusButton;
    TextView minusButton;
    TextView numberOfGuestsV;
    Spinner spinner;
    TextView totalCostV;
    ArrayList<ImageView> dishesView = new ArrayList<>();
    View popupView;
    PopupWindow popupWindow;
    Button create_menu_button;

    public ChooseMenuLayout(View view, DinnerModel model) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        // store in the class the reference to the Android View
        this.view = view;

        // and the reference to the model
        this.model = model;

        this.context = view.getContext();

        this.plusButton = (TextView) view.findViewById(R.id.plusButton);
        this.minusButton = (TextView) view.findViewById(R.id.minusButton);
        this.numberOfGuestsV = (TextView) view.findViewById(R.id.numberOfGuests);
        this.numberOfGuestsV.setText(model.getNumberOfGuests() + " pers");
        this.spinner = (Spinner) view.findViewById(R.id.spinner);
        this.spinner.setSelection(this.model.getNumberOfGuests());
        this.create_menu_button = (Button) view.findViewById(R.id.create_menu_button);

        // Setup the rest of the view abs_layout
        this.totalCostV = (TextView) view.findViewById(R.id.totalCost);
        double totalMenuPrice = Math.round(model.getTotalMenuPrice() * Math.pow(10, 2)) / Math.pow(10, 2);
        this.totalCostV.setText("Total price: " + totalMenuPrice + "kr");

        this.retrieveData();

        //We need to get the instance of the LayoutInflater, use the context of this activity
        LayoutInflater inflater = LayoutInflater.from(context);
        //Inflate the view from a predefined XML layout
        this.popupView = inflater.inflate(R.layout.dish_popup_layout, (ViewGroup) view.findViewById(R.id.popup_element));
        // create a 300px width and 470px height PopupWindow
        this.popupWindow = new PopupWindow(this.popupView, 900, 1000, true);

        model.addObserver(this);
    }

    public void retrieveData(){
        Integer[] types = new Integer[] { 1, 2 ,3 };

        for(final int t: types){
            final Set<Dish> dishesOftype;
            final String type;
            final Dish selected_dish = model.getSelectedDish(t);
            final View v = this.view;

            //Get dishes with specific type and layout container for the dishes
            if(t == 1){
                type = "appetizer";
            }else if(t == 2){
                type = "main+course";
            }else{
                type = "dessert";
            }

            model.getDishesOfType(type, new AsyncData() {
                @Override
                public void onData(JSONObject response) {
                    final LinearLayout listItemsLayout;
                    //Get dishes with specific type and layout container for the dishes
                    int typeN;
                    if(type.equals("appetizer")){
                        listItemsLayout = (LinearLayout) v.findViewById(R.id.starterListItems);
                        typeN = 1;
                    }else if(type.equals("main+course")){
                        listItemsLayout = (LinearLayout) v.findViewById(R.id.mainCourseListItems);
                        typeN = 2;
                    }else{
                        listItemsLayout = (LinearLayout) v.findViewById(R.id.dessertListItems);
                        typeN = 3;
                    }
                    try{
                        JSONArray dishes = response.getJSONArray("results");

                        for(int i = 0; i < dishes.length(); i++){
                            JSONObject el = (JSONObject) dishes.get(i);
                            Dish d = new Dish(el.getInt("id"), el.getString("title"), typeN, response.getString("baseUri")+el.getString("image"), "");

                            View itemView = LayoutInflater.from(context).inflate(R.layout.dish_layout,null);
                            ImageView imageView = (ImageView) itemView.findViewById(R.id.dish_image);
                            TextView name = (TextView) itemView.findViewById(R.id.dish_name);

                            //Get resource id for image
                            /*Resources resources = context.getResources();
                            String imgName = d.getImage().replace(".jpg","").trim();
                            final int resourceId = resources.getIdentifier(imgName, "drawable", context.getPackageName());*/

                            try{
                                URL url = new URL(d.getImage());
                                /*InputStream is = (InputStream) url.getContent();
                                Drawable drawable = Drawable.createFromStream(is, "src name");
                                imageView.setImageDrawable(drawable);*/
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = 2;
                                Bitmap bitmap = BitmapFactory.decodeStream((InputStream) url.getContent(), null, options);
                                imageView.setImageBitmap(bitmap);

                            }catch (Exception e){
                                System.out.println("Error:"+e);
                            }

                            //Set tag and name
                            imageView.setTag(d);

                            if(d.getName().length() >= 10){
                                name.setText(d.getName().substring(0,10));
                            }else{
                                name.setText(d.getName());
                            }

                            //Set border for selected dishes
                            if(d.getName().equals(selected_dish.getName())){
                                itemView.setBackgroundResource(R.drawable.border);
                                if(d.getType() == 1){
                                    itemView.setTag("Selected Starter");
                                }else if(d.getType() == 2){
                                    itemView.setTag("Selected Main");
                                }else if(d.getType() == 3){
                                    itemView.setTag("Selected Dessert");
                                }
                            }

                            //listItemsLayout.findViewWithTag("loader").setVisibility(View.INVISIBLE);
                            View loader = listItemsLayout.findViewWithTag("loader");
                            listItemsLayout.removeView(loader);

                            listItemsLayout.addView(itemView);

                            //Store displayed dishes to a Dish array
                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    displayPopup(v);
                                }
                            });
                        }
                    }catch (JSONException e){
                        Log.e("MYAPP", "unexpected JSON exception", e);
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(context, "There was error retrieving data.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void displayPopup(View v){
        View dish_container = (View) v.getParent();
        dish_container.setTag("Pending");

        ImageView dishv = (ImageView) v;

        final Dish dish = (Dish) dishv.getTag();
        System.out.println(dish.getName());
        Drawable dishImg = dishv.getDrawable();
        //ImageView img = (ImageView) view.popupView.findViewById(R.id.imageView6);
        ((ImageView) popupView.findViewById(R.id.imageView6)).setImageDrawable(dishImg);
        ((TextView) popupView.findViewById(R.id.textView17)).setText(dish.getName());

        if(dish.getIngredients().size() <= 0){
            //Calculate dish price
            model.getDishInfoById(dish.getId(), new AsyncData() {
                @Override
                public void onData(JSONObject response) {
                    try{
                        JSONArray ingredients = response.getJSONArray("extendedIngredients");
                        float dish_cost = 0;
                        for(int i = 0; i < ingredients.length(); i++){
                            JSONObject ing = (JSONObject) ingredients.get(i);
                            Ingredient ingO = new Ingredient(ing.getString("name"), ing.getInt("amount"), ing.getString("unit"), ing.getInt("amount"));
                            dish.addIngredient(ingO);
                            dish_cost += ing.getInt("amount");
                            if(i == ingredients.length()-1){
                                System.out.println("setTagIng:::"+dish.getIngredients().size());
                                ((Button) popupView.findViewById(R.id.select_dish_button)).setTag(dish);
                            }
                        }
                        ((TextView) popupView.findViewById(R.id.total_dish_cost)).setText("Cost: " + String.valueOf(dish_cost * model.getNumberOfGuests()) + "kr");
                        ((TextView) popupView.findViewById(R.id.cost_per_person)).setText("(" + String.valueOf(dish_cost) + "kr / Person)");
                    }catch (JSONException e){
                        Log.e("MYAPP", "unexpected JSON exception", e);
                    }

                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(view.getContext(), "There was error retrieving data.", Toast.LENGTH_LONG).show();
                }
            });
        }else{
            float dish_cost = 0;
            Set<Ingredient> allIngredients = dish.getIngredients();
            for(Ingredient ing: allIngredients){
                dish_cost += ing.getQuantity();
            }
            ((Button) popupView.findViewById(R.id.select_dish_button)).setTag(dish);
            ((TextView) popupView.findViewById(R.id.total_dish_cost)).setText("Cost: " + String.valueOf(dish_cost * model.getNumberOfGuests()) + "kr");
            ((TextView) popupView.findViewById(R.id.cost_per_person)).setText("(" + String.valueOf(dish_cost) + "kr / Person)");
        }


        /*Set<Ingredient> ingredients = dish.getIngredients();
        Iterator<Ingredient> iterate_ingredients = ingredients.iterator();
        while (iterate_ingredients.hasNext()){
            Ingredient ing = iterate_ingredients.next();
            dish_cost += ing.getQuantity() * ing.getPrice();
            System.out.println("getQuantity:" + ing.getQuantity() + "getPrice:" + ing.getPrice());
        }*/

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        View popupParentContainer = (View) popupWindow.getContentView().getParent();
        WindowManager wm = (WindowManager) this.context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) popupParentContainer.getLayoutParams();
        lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lp.dimAmount = 0.6f;
        wm.updateViewLayout(popupParentContainer, lp);

        /**
         FrameLayout layout_MainMenu = (FrameLayout) view.view.findViewById( R.id.main_menu);
         layout_MainMenu.getForeground().setAlpha( 220);
         */
    }

    @Override
    public void update(Observable o, Object arg){

        if(arg.equals("numberOfGuests")){
            this.numberOfGuestsV.setText(this.model.getNumberOfGuests() + " pers");
            this.spinner.setSelection(this.model.getNumberOfGuests());
            this.totalCostV.setText("Total price: " + model.getTotalMenuPrice()+ "kr");
        }
        if(arg.equals("dishAddedToMenu")){
            System.out.println("Total price: " + model.getTotalMenuPrice()+"getNumberOfGuests: " + model.getNumberOfGuests());
            this.totalCostV.setText("Total price: " + model.getTotalMenuPrice()+ "kr");
        }
    }
}
