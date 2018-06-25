package com.app.fiche.shoppinglist;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by kamil on 22.06.18.
 */

public class ShopList {
    private int id;
    private String name;
    private Date date;
    private List<ShopListProduct> shopListProducts;
    private Status status;

    public ShopList(int id, String name, Date date, List<ShopListProduct> shopListProducts, Status status) {
        this.id = id;
        this.name = name;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = dateFormat.format(new Date());
        Date formattedDate = null;
        try {
            formattedDate = dateFormat.parse(dateInString);
        } catch (ParseException e){
            //todo
            e.printStackTrace();
        }
        this.date = formattedDate;
        this.shopListProducts = shopListProducts;
        this.status = status;
    }

    public ShopList(String name, Date date) {
        this(-1, name, date, null, Status.NEW);
    }



    public enum Status {
        NEW,
        PARTIALLY_COMPLETED,
        COMPLETED
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<ShopListProduct> getShopListProducts() {
        return shopListProducts;
    }

    public void setProducts(List<ShopListProduct> shopListProducts) {
        this.shopListProducts = shopListProducts;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


}
