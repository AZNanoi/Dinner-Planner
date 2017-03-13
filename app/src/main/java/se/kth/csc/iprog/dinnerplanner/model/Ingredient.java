package se.kth.csc.iprog.dinnerplanner.model;

public class Ingredient {
	
	String name;
	double quantity;
	String unit; // can be empty, for example in case of eggs (there is not unit)
	double price;
	
	public Ingredient(String name,double quantity, String unit, double price){
		this.name = name;
		this.quantity=quantity;
		this.unit = unit;
		this.price = price;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}

}
