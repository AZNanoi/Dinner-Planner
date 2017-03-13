package se.kth.csc.iprog.dinnerplanner.model;

import java.util.Set;

public interface IDinnerModel {

	public int getNumberOfGuests();
	public void setNumberOfGuests(int numberOfGuests);
	
	/**
	 * Returns the dish that is on the menu for selected type (1 = starter, 2 = main, 3 = desert).
	 */
	public Dish getSelectedDish(int type);
	
	/**
	 * Returns all the dishes on the menu.
	 */
	public Set<Dish> getFullMenu();
	
	/**
	 * Returns all ingredients for all the dishes on the menu.
	 */
	public Set<Ingredient> getAllIngredients();
	
	/**
	 * Returns the total price of the menu (all the ingredients multiplied by number of guests).
	 */
	public float getTotalMenuPrice();

    /**
     * Adds the passed dish to the menu. If the dish of that type already exists on the menu
     * it is removed from the menu and the new one added.
     */
    public void addDishToMenu(Dish dish);

    /**
     * Remove dish from menu
     */
    public void removeDishFromMenu(Dish dish);

}
