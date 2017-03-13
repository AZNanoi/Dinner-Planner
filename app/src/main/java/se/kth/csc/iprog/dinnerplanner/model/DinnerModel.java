package se.kth.csc.iprog.dinnerplanner.model;


import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;
import java.util.Set;

import cz.msebera.android.httpclient.Header;
import se.kth.csc.iprog.dinnerplanner.android.api.AsyncData;
import se.kth.csc.iprog.dinnerplanner.android.api.SpoonacularAPIClient;

public class DinnerModel extends Observable implements IDinnerModel{


	Set<Dish> dishes = new HashSet<Dish>();

	private int numberOfGuests;
	public Set<Dish> selectedDishes = new HashSet<Dish>();

	/**
	 * TODO: For Lab2 you need to implement the IDinnerModel interface.
	 * When you do this you will have all the needed fields and methods
	 * for the dinner planner (number of guests, selected dishes, etc.).
	 */

	public int getNumberOfGuests(){
		return this.numberOfGuests;
	}

	public void setNumberOfGuests(int numberOfGuests){
		Log.d("Click", "setNumberOfGuests:" + String.valueOf(numberOfGuests));
		this.numberOfGuests = numberOfGuests;
		setChanged();
		notifyObservers("numberOfGuests");
	}

	/**
	 * Returns the dish that is on the menu for selected type (1 = starter, 2 = main, 3 = desert).
	 */
	public Dish getSelectedDish(int type){
		Dish result = new Dish(0, "null", 0, "null", "null");
		for(Dish d: this.selectedDishes){
			if(d.getType() == type){
				result = d;
			}
		}
		return result;
	}

	/**
	 * Returns all the dishes on the menu.
	 */
	public Set<Dish> getFullMenu(){
		return this.selectedDishes;
	}

	/**
	 * Returns all ingredients for all the dishes on the menu.
	 */
	public Set<Ingredient> getAllIngredients(){
		Set<Ingredient> allIngredients = new HashSet<Ingredient>();
		for(Dish d: this.selectedDishes){
            System.out.println("setTagIngModel:::"+d.getIngredients().size());
			allIngredients.addAll(d.getIngredients());
		}
		return allIngredients;
	}

	/**
	 * Returns the total price of the menu (all the ingredients multiplied by number of guests).
	 */
	public float getTotalMenuPrice(){
		float totalPrice = 0;
		Set<Ingredient> allIngredients = this.getAllIngredients();
        System.out.println("setTagIngModelTotalP:::"+allIngredients.size());
		Iterator<Ingredient> iteration = allIngredients.iterator();
		while(iteration.hasNext()){
			Ingredient ing = iteration.next();
			totalPrice += ing.getQuantity();
		}
		return totalPrice * this.numberOfGuests;
	}

	/**
	 * Adds the passed dish to the menu. If the dish of that type already exists on the menu
	 * it is removed from the menu and the new one added.
	 */
	public void addDishToMenu(Dish dish){
		boolean dishExist = false;

		for(Iterator<Dish> i = this.selectedDishes.iterator(); i.hasNext();){
			Dish d = i.next();
			if(d.getType() == dish.getType()){
				i.remove();
				if(d.getName().equals(dish.getName())){
					dishExist = true;
				}
			}
		}
		if(dishExist){
			System.out.println("This dish is already selected!");
		}else{
			this.selectedDishes.add(dish);
			setChanged();
			notifyObservers("dishAddedToMenu");
		}
	}

	/**
	 * Remove dish from menu
	 */
	public void removeDishFromMenu(Dish dish){
		for(Iterator<Dish> i = this.selectedDishes.iterator(); i.hasNext();){
			Dish d = i.next();
			if(d.getName().equals(dish.getName()) && d.getType() == dish.getType()){
				i.remove();
			}
		}
	}

	/**
	 * The constructor of the overall model. Set the default values here
	 */
	public DinnerModel(){




	/*	//Adding some example data, you can add more
		Dish dish4 = new Dish("Ice Cream",Dish.DESERT,"icecream.jpg","In a large mixing bowl, beat the eggs. Add the milk, brown sugar and nutmeg; stir well to combine. Soak bread slices in the egg mixture until saturated. Heat a lightly oiled griddle or frying pan over medium high heat. Brown slices on both sides, sprinkle with cinnamon and serve hot.");
		Ingredient dish4ing2 = new Ingredient("milk",10,"ml",5);
		Ingredient dish4ing3 = new Ingredient("brown sugar",2,"g",1);
		dish4.addIngredient(dish4ing2);
		dish4.addIngredient(dish4ing3);
		dishes.add(dish4);

		//Adding some example data, you can add more
		Dish dish5 = new Dish("Sour Dough",Dish.DESERT,"sourdough.jpg","In a large mixing bowl, beat the eggs. Add the milk, brown sugar and nutmeg; stir well to combine. Soak bread slices in the egg mixture until saturated. Heat a lightly oiled griddle or frying pan over medium high heat. Brown slices on both sides, sprinkle with cinnamon and serve hot.");
		Ingredient dish5ing2 = new Ingredient("milk",5,"ml",5);
		Ingredient dish5ing3 = new Ingredient("brown sugar",3,"g",1);
		dish5.addIngredient(dish5ing2);
		dish5.addIngredient(dish5ing3);
		dishes.add(dish5);*/

		this.numberOfGuests = 0;
	}

	/**
	 * Returns the set of dishes of specific type. (1 = starter, 2 = main, 3 = desert).
	 */
	public void getDishes(final AsyncData callback){
		SpoonacularAPIClient.get("recipes/search", null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[]headers, JSONObject response) {
				System.out.println(response.toString());
				callback.onData(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				System.out.println(responseString);
				callback.onError(responseString);
			}
		});
	}

	/**
	 * Returns the set of dishes of specific type. (1 = starter, 2 = main, 3 = desert).
	 */
	public void getDishesOfType(String type, final AsyncData callback){
		SpoonacularAPIClient.get("recipes/search?type="+type, null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[]headers, JSONObject response) {
				System.out.println(response.toString());
				callback.onData(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				System.out.println(responseString);
				callback.onError(responseString);
			}
		});
	}

	/**
	 * Returns the set of dishes of specific type, that contain filter in their name
	 * or name of any ingredient.
	 */
	public Set<Dish> filterDishesOfType(int type, String filter){
		Set<Dish> result = new HashSet<Dish>();
		for(Dish d : dishes){
			if(d.getType() == type && d.contains(filter)){
				result.add(d);
			}
		}
		return result;
	}

	public void getDishInfoById(int id, final AsyncData callback){
		SpoonacularAPIClient.get("recipes/"+id+"/information", null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[]headers, JSONObject response) {
				System.out.println(response.toString());
				callback.onData(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				System.out.println(responseString);
				callback.onError(responseString);
			}
		});
	}



}
