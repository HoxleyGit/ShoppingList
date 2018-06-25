package com.app.fiche.shoppinglist;

/**
 * Created by kamil on 23.06.18.
 */

public class ShopListProduct extends Product {
    private double toBuy;

    public ShopListProduct(int id, String name, UnitMeasure unitMeasure, int picture, double toBuy) {
        super(id, name, unitMeasure, picture);
        this.toBuy = toBuy;
    }

    public ShopListProduct(String name, UnitMeasure unitMeasure, int picture, double toBuy){
        this(-1, name, unitMeasure, picture, toBuy);
    }



    //getters and setters
    public double getToBuy() {
        return toBuy;
    }

    public void setToBuy(double toBuy) {
        this.toBuy = toBuy;
    }
}
