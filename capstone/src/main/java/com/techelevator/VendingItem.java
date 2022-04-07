package com.techelevator;

public class VendingItem {
    private String name;

    private double price;
    private String slot;
    private String type;
    private int quantity = 5;
    private String eatingSounds;

    public VendingItem(String slot, String name, double price, String type) {
        this.name = name;
        this.price = price;
        this.slot = slot;
        this.type = type;
        if (type.equals("Chip")){
            this.eatingSounds = "Crunch Crunch, Yum!";
        }
        else if(type.equals("Candy")){
            this.eatingSounds = "Munch Munch, Yum!";
        }
        else if(type.equals("Drink")){
            this.eatingSounds = "Glug Glug, Yum!";
        }
        else if(type.equals("Gum")){
            this.eatingSounds = "Chew Chew, Yum!";
        }
    }
    public void buyItem(){
        if(quantity>0){
            quantity-=1;
        }
        else{
            System.out.println("Sold out.");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double  price) {
        this.price = price;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }
    public String getEatingSounds(){
        return eatingSounds;
    }
}
