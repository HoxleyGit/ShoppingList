package com.app.fiche.shoppinglist;

/**
 * Created by kamil on 22.06.18.
 */


public class Product {
    private int id;
    private String name;
    private UnitMeasure unitMeasure;
    private int picture;

    public Product(int id, String name, UnitMeasure unitMeasure, int picture) {
        this.id = id;
        this.name = name;
        this.unitMeasure = unitMeasure;
        this.picture = picture;
    }

    public Product(String name, UnitMeasure unitMeasure, int picture){
        this(-1, name, unitMeasure, picture);
    }

    public enum UnitMeasure{
        KG,
        ML,
        PC
    }





    //getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UnitMeasure getUnitMeasure() {
        return unitMeasure;
    }

    public void setUnitMeasure(UnitMeasure unitMeasure) {
        this.unitMeasure = unitMeasure;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }
}
