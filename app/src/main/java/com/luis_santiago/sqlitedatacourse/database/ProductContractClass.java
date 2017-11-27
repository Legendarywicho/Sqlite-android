package com.luis_santiago.sqlitedatacourse.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Luis Fernando Santiago Ruiz on 11/21/17.
 */

public class ProductContractClass {

    private ProductContractClass(){}

    //This is to query from our content provider
    public static final String CONTENT_AUTHORITY = "com.luis_santiago.sqlitedatacourse";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ CONTENT_AUTHORITY);
    public static final String PATH_PRODUCTS = "products";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI , PATH_PRODUCTS);

    public static class ProductEntry implements BaseColumns{

        //Database attributes
        public static final String _ID = BaseColumns._ID;
        public static final String TABLE_NAME  = "products";
        public static final String COLUMN_NAME_ = "productName";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_SUPPLIER_NAME = "supplierName";
        public static final String COLUMN_SUPPLIER_EMAIL = "supplierEmail";
        public static final String COLUMN_SUPPLIER_NUMBER = "supplierNumber";

        //commands for database;
        public static String SQL_CREATE_ENTRIES =
                "CREATE TABLE "+ TABLE_NAME + "( "+
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + " ," +
                        COLUMN_NAME_ + " TEXT ," +
                        COLUMN_PRICE + " INTEGER ," +
                        COLUMN_QUANTITY + " INTEGER ," +
                        COLUMN_SUPPLIER_EMAIL + " TEXT ," +
                        COLUMN_SUPPLIER_NAME + " TEXT ," +
                        COLUMN_SUPPLIER_NUMBER + " INTEGER);";

        public static String DELETE_ENTRIES = "DELETE TABLE IF EXISTS" + TABLE_NAME;

        //MIME types
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + ProductContractClass.PATH_PRODUCTS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + ProductContractClass.PATH_PRODUCTS;

    }
}
