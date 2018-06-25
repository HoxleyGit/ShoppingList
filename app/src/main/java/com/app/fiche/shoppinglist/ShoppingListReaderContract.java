package com.app.fiche.shoppinglist;

import android.provider.BaseColumns;

/**
 * Created by kamil on 22.06.18.
 */

public final class ShoppingListReaderContract {
    private ShoppingListReaderContract(){}

    public static class ShopLists implements BaseColumns{
        public static final String TABLE_NAME = "shop_lists";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_STATUS = "status";
    }

    public static class Products implements BaseColumns{
        public static final String TABLE_NAME = "products";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_UNIT_MEASURE = "unit_measure";
        public static final String COLUMN_NAME_PICTURE = "picture";
    }

    public static class ShopLists_Products implements BaseColumns{
        public static final String TABLE_NAME = "shop_lists_products";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_SHOP_LIST_ID = "shop_list_id";
        public static final String COLUMN_NAME_PRODUCT_ID = "product_id";
        public static final String COLUMN_NAME_TO_BUY = "to_buy";
    }
}
