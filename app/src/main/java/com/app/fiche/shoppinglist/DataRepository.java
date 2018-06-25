package com.app.fiche.shoppinglist;


import android.content.Context;

import java.util.List;


/**
 * Created by kamil on 23.06.18.
 */

public class DataRepository {
    private List<Product> products;
    private List<ShopList> shopLists;
    private ShoppingListDbHelper dbHelper;

    public DataRepository(Context context) {
        dbHelper = new ShoppingListDbHelper(context);
        refreshDataRepository();
    }

    public void refreshDataRepository(){
        Integer integer = null;
        products = dbHelper.getProducts(integer);
        shopLists = dbHelper.getShopLists();
    }

    public void addProduct(Product product){
        dbHelper.addProduct(product);
        refreshDataRepository();
    }

    public void addShopList(ShopList shopList){
        long rowId = dbHelper.addShopList(shopList);
        refreshDataRepository();
    }

    public void addProductToShopList(ShopList shopList, ShopListProduct shopListProduct){
        dbHelper.addProductToShopList(shopList, shopListProduct);
        refreshDataRepository();
    }

    public void addProductToShopList(int shopListId, ShopListProduct shopListProduct){
        long rowId = dbHelper.addProductToShopList(shopListId, shopListProduct);
        refreshDataRepository();
    }

    public void buyProduct(Product product){
        dbHelper.buyProduct(product);
        refreshDataRepository();
    }

    public void updateShopListInfo(ShopList oldShopList, ShopList newShopList){
        dbHelper.updateShopListInfo(oldShopList, newShopList);
        refreshDataRepository();
    }

    public void deleteShopList(ShopList shopList){
        dbHelper.deleteShopList(shopList);
        refreshDataRepository();
    }

    public void deleteProductFromShopList(int shopListId, int shopListProductId){
        dbHelper.deleteProductFromShopList(shopListId, shopListProductId);
        refreshDataRepository();
    }

    public void setPredefinedProducts(List<Product> products){
        for(Product product : products){
            dbHelper.addProduct(product);
        }
        refreshDataRepository();
    }

    public void deleteProduct(Product product){
        dbHelper.deleteProduct(product);
        refreshDataRepository();
    }

    public ShopList findShopListById(int id){
        for(ShopList shopList : shopLists){
            if(id == shopList.getId()){
                return shopList;
            }
        }
        return null;
    }



    //getters and setters
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<ShopList> getShopLists() {
        return shopLists;
    }

    public void setShopLists(List<ShopList> shopLists) {
        this.shopLists = shopLists;
    }

    public ShopList getShopListById (int id){
        if(shopLists != null) {
            for (ShopList sL : shopLists){
                if(sL.getId() == id){
                    return sL;
                }
            }
        }
        return null;
    }
}
