package com.luis_santiago.sqlitedatacourse.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Luis Fernando Santiago Ruiz on 11/21/17.
 */

public class ProductReaderHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "products.db";

    public ProductReaderHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String commandToCreateDatabase = ProductContractClass.ProductEntry.SQL_CREATE_ENTRIES;
        Log.e("Sql lite " , commandToCreateDatabase);
        sqLiteDatabase.execSQL(commandToCreateDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(ProductContractClass.ProductEntry.DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }


}
