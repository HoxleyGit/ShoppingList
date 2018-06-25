package com.app.fiche.shoppinglist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kamil on 22.06.18.
 */

public class ShoppingListDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ShopList.db";

    public ShoppingListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_SHOP_LISTS_ENTRY);
        db.execSQL(SQL_CREATE_PRODUCTS_ENTRY);
        db.execSQL(SQL_CREATE_SHOP_LISTS_PRODUCTS_ENTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRY_PRODUCTS);
        db.execSQL(SQL_DELETE_ENTRY_SHOP_LISTS);
        db.execSQL(SQL_DELETE_ENTRY_SHOP_LISTS_PRODUCTS);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private static final String SQL_CREATE_SHOP_LISTS_ENTRY =
            "CREATE TABLE " + ShoppingListReaderContract.ShopLists.TABLE_NAME + "(" +
                    ShoppingListReaderContract.ShopLists.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ShoppingListReaderContract.ShopLists.COLUMN_NAME_NAME + " TEXT, " +
                    ShoppingListReaderContract.ShopLists.COLUMN_NAME_DATE + " TEXT)";
    private static final String SQL_CREATE_PRODUCTS_ENTRY =
            "CREATE TABLE " + ShoppingListReaderContract.Products.TABLE_NAME + "(" +
                    ShoppingListReaderContract.Products.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ShoppingListReaderContract.Products.COLUMN_NAME_NAME + " TEXT, " +
                    ShoppingListReaderContract.Products.COLUMN_NAME_PICTURE + " INTEGER, " +
                    ShoppingListReaderContract.Products.COLUMN_NAME_UNIT_MEASURE + " TEXT)";
    private static final String SQL_CREATE_SHOP_LISTS_PRODUCTS_ENTRY =
            "CREATE TABLE " + ShoppingListReaderContract.ShopLists_Products.TABLE_NAME + "(" +
                    ShoppingListReaderContract.ShopLists_Products.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ShoppingListReaderContract.ShopLists_Products.COLUMN_NAME_SHOP_LIST_ID + " INTEGER, " +
                    ShoppingListReaderContract.ShopLists_Products.COLUMN_NAME_PRODUCT_ID + " INTEGER, " +
                    ShoppingListReaderContract.ShopLists_Products.COLUMN_NAME_TO_BUY + " REAL)";

    private static final String SQL_DELETE_ENTRY_SHOP_LISTS =
            "DROP TABLE IF EXISTS " + ShoppingListReaderContract.ShopLists.TABLE_NAME;

    private static final String SQL_DELETE_ENTRY_PRODUCTS =
            "DROP TABLE IF EXISTS " + ShoppingListReaderContract.Products.TABLE_NAME;

    private static final String SQL_DELETE_ENTRY_SHOP_LISTS_PRODUCTS =
            "DROP TABLE IF EXISTS " + ShoppingListReaderContract.ShopLists_Products.TABLE_NAME;



    public List<Product> getProducts(Integer id){
        SQLiteDatabase db = getReadableDatabase();
        String selection = null;
        String[] selectionArgs = null;
        if(id != null){
            selectionArgs = new String[1];
            selection = ShoppingListReaderContract.Products.COLUMN_NAME_ID + " = ?";
            selectionArgs[0] = String.valueOf(id);
        }


        Cursor productCursor = db.query(
                    ShoppingListReaderContract.Products.TABLE_NAME,
                    null,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

        List<Product> products = new ArrayList<>();
        while(productCursor.moveToNext()) {
            int productId = productCursor.getInt(
                    productCursor.getColumnIndexOrThrow(ShoppingListReaderContract.Products.COLUMN_NAME_ID));
            String string = "";
            String productName = productCursor.getString(1);
            int productPicture = productCursor.getInt(2);
            String productUnitMeasure = productCursor.getString(3);
            products.add(new Product(productId, productName, Product.UnitMeasure.valueOf(productUnitMeasure), productPicture));
        }
        if(products.size() == 0) {
            products = null;
        }
        productCursor.close();
        return products;
    }

    public Product getProducts(String productName){
        SQLiteDatabase db = getReadableDatabase();
        String selection = ShoppingListReaderContract.Products.COLUMN_NAME_NAME + " = ?";
        String[] selectionArgs = {productName};


        Cursor productCursor = db.query(
                ShoppingListReaderContract.Products.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Product product = null;
        while(productCursor.moveToNext()) {
            int productId = productCursor.getInt(
                    productCursor.getColumnIndexOrThrow(ShoppingListReaderContract.Products.COLUMN_NAME_ID));
            int productPicture = productCursor.getInt(2);
            String productUnitMeasure = productCursor.getString(3);
            product = new Product(productId, productName, Product.UnitMeasure.valueOf(productUnitMeasure), productPicture);
        }
        productCursor.close();
        return product;
    }

    public List<ShopList> getShopLists(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor shopListsCursor = db.query(
                ShoppingListReaderContract.ShopLists.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        List<ShopList> shopLists = new ArrayList<>();
        while(shopListsCursor.moveToNext()) {
            int shopListId = shopListsCursor.getInt(
                    shopListsCursor.getColumnIndexOrThrow(ShoppingListReaderContract.ShopLists.COLUMN_NAME_ID));
            String shopListName = shopListsCursor.getString(1);
            String shopListDateString = shopListsCursor.getString(2);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date shopListDate = null;
            try{
                shopListDate = formatter.parse(shopListDateString);
            } catch(ParseException e){
                //todo
                e.printStackTrace();
            }
            ShopList.Status status = ShopList.Status.NEW;
            List<ShopListProduct> shopListProducts = getShopListProducts(shopListId);
            int count = 0;
            if(shopListProducts != null) {
                for (ShopListProduct shopListProduct : shopListProducts) {
                    if (shopListProduct.getToBuy() <= 0) {
                        count++;
                    }
                }
                if(count == shopListProducts.size()){
                    status = ShopList.Status.COMPLETED;
                } else if(count > 0){
                    status = ShopList.Status.PARTIALLY_COMPLETED;
                } else {
                    status = ShopList.Status.NEW;
                }
            }

            shopLists.add(new ShopList(
                    shopListId,
                    shopListName,
                    shopListDate,
                    shopListProducts,
                    status));
        }
        if(shopLists.size() == 0){
            shopLists = null;
        }
        shopListsCursor.close();
        return shopLists;
    }

    private List<ShopListProduct> getShopListProducts(long index){
        SQLiteDatabase db = getReadableDatabase();
        String selection = ShoppingListReaderContract.ShopLists_Products.COLUMN_NAME_SHOP_LIST_ID + " = ?";
        String[] selectionArgs = {String.valueOf(index)};
        String sortOrder = ShoppingListReaderContract.ShopLists_Products.COLUMN_NAME_PRODUCT_ID + " ASC";

        Cursor shopListsProductsCursor = db.query(
                ShoppingListReaderContract.ShopLists_Products.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        List<ShopListProduct> shopListProducts = new ArrayList<>();
        while(shopListsProductsCursor.moveToNext()){
            int shopListsProductsProductId = shopListsProductsCursor.getInt(2);
            double shopListsProductsToBuy = shopListsProductsCursor.getDouble(3);
            Product product = getProducts(shopListsProductsProductId).get(0);
            ShopListProduct shopListProduct = new ShopListProduct(
                    product.getId(),
                    product.getName(),
                    product.getUnitMeasure(),
                    product.getPicture(),
                    shopListsProductsToBuy
            );
            shopListProduct.setToBuy(shopListsProductsToBuy);
            shopListProducts.add(shopListProduct);
        }
        if(shopListProducts.size() == 0){
            shopListProducts = null;
        }
        shopListsProductsCursor.close();
        return shopListProducts;
    }

    public long addProduct(Product product){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues productValues = new ContentValues();
        productValues.put(ShoppingListReaderContract.Products.COLUMN_NAME_NAME, product.getName());
        productValues.put(ShoppingListReaderContract.Products.COLUMN_NAME_PICTURE, product.getPicture());
        productValues.put(ShoppingListReaderContract.Products.COLUMN_NAME_UNIT_MEASURE, product.getUnitMeasure().toString());

        return db.insert(ShoppingListReaderContract.Products.TABLE_NAME, null, productValues);
    }

    public long addShopList(ShopList shopList){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues shopListValues = new ContentValues();

        shopListValues.put(ShoppingListReaderContract.ShopLists.COLUMN_NAME_NAME, shopList.getName());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String shopListDateString = dateFormat.format(shopList.getDate());
        shopListValues.put(ShoppingListReaderContract.ShopLists.COLUMN_NAME_DATE, shopListDateString);

        return db.insert(ShoppingListReaderContract.ShopLists.TABLE_NAME, null, shopListValues);
    }

    public void updateShopListInfo(ShopList oldShopList, ShopList newShopList){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues shopListValues = new ContentValues();
        shopListValues.put(ShoppingListReaderContract.ShopLists.COLUMN_NAME_NAME, newShopList.getName());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String shopListDateString = dateFormat.format(newShopList.getDate());
        shopListValues.put(ShoppingListReaderContract.ShopLists.COLUMN_NAME_DATE, shopListDateString);

        String selection = ShoppingListReaderContract.ShopLists.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = {String.valueOf(oldShopList.getId())};

        db.update(ShoppingListReaderContract.ShopLists.TABLE_NAME, shopListValues, selection, selectionArgs);
    }

    public void deleteProductFromShopList(int shopListId, int shopListProductId){
        SQLiteDatabase db = getWritableDatabase();

        String selection = ShoppingListReaderContract.ShopLists_Products.COLUMN_NAME_PRODUCT_ID + " = ? AND " +
                ShoppingListReaderContract.ShopLists_Products.COLUMN_NAME_SHOP_LIST_ID + " = ?";
        String[] selectionArgs = {String.valueOf(shopListProductId), String.valueOf(shopListId)};

        db.delete(ShoppingListReaderContract.ShopLists_Products.TABLE_NAME, selection, selectionArgs);
    }

    public void addProductToShopList(ShopList shopList, ShopListProduct shopListProduct){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues shopListsProductsValues = new ContentValues();
        shopListsProductsValues.put(ShoppingListReaderContract.ShopLists_Products.COLUMN_NAME_SHOP_LIST_ID,
                shopList.getId());
        shopListsProductsValues.put(ShoppingListReaderContract.ShopLists_Products.COLUMN_NAME_PRODUCT_ID,
                shopListProduct.getId());
        shopListsProductsValues.put(ShoppingListReaderContract.ShopLists_Products.COLUMN_NAME_TO_BUY,
                shopListProduct.getToBuy());

        db.insert(ShoppingListReaderContract.ShopLists_Products.TABLE_NAME, null, shopListsProductsValues);
    }

    public long addProductToShopList(int shopListId, ShopListProduct shopListProduct){
        SQLiteDatabase db = getWritableDatabase();
        Product product = getProducts(shopListProduct.getName());


        ContentValues shopListsProductsValues = new ContentValues();
        shopListsProductsValues.put(ShoppingListReaderContract.ShopLists_Products.COLUMN_NAME_SHOP_LIST_ID,
                shopListId);
        shopListsProductsValues.put(ShoppingListReaderContract.ShopLists_Products.COLUMN_NAME_PRODUCT_ID,
                product.getId());
        shopListsProductsValues.put(ShoppingListReaderContract.ShopLists_Products.COLUMN_NAME_TO_BUY,
                shopListProduct.getToBuy());

        return db.insert(ShoppingListReaderContract.ShopLists_Products.TABLE_NAME, null, shopListsProductsValues);
    }

    public void deleteShopList(ShopList shopList){
        SQLiteDatabase db = getWritableDatabase();

        String selection = ShoppingListReaderContract.ShopLists.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = {String.valueOf(shopList.getId())};

        db.delete(ShoppingListReaderContract.ShopLists.TABLE_NAME, selection, selectionArgs);

        selection = ShoppingListReaderContract.ShopLists_Products.COLUMN_NAME_SHOP_LIST_ID + " = ?";
        selectionArgs[0] = String.valueOf(shopList.getId());

        db.delete(ShoppingListReaderContract.ShopLists_Products.TABLE_NAME, selection, selectionArgs);
    }

    public void buyProduct(Product product){
        SQLiteDatabase db = getWritableDatabase();

        String selection = ShoppingListReaderContract.ShopLists_Products.COLUMN_NAME_PRODUCT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(product.getId())};

        ContentValues contentValues = new ContentValues();
        contentValues.put(ShoppingListReaderContract.ShopLists_Products.COLUMN_NAME_TO_BUY, 0.0);

        db.update(ShoppingListReaderContract.ShopLists_Products.TABLE_NAME, contentValues, selection, selectionArgs);
    }

    public void deleteProduct(Product product){
        SQLiteDatabase db = getWritableDatabase();

        String selection = ShoppingListReaderContract.Products.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = {String.valueOf(product.getId())};

        db.delete(ShoppingListReaderContract.Products.TABLE_NAME, selection, selectionArgs);

        selection = ShoppingListReaderContract.ShopLists_Products.COLUMN_NAME_PRODUCT_ID + " = ?";
        selectionArgs[0] = String.valueOf(product.getId());

        db.delete(ShoppingListReaderContract.ShopLists_Products.TABLE_NAME, selection, selectionArgs);
    }
}
