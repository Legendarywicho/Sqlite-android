package com.luis_santiago.sqlitedatacourse.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Selection;
import android.util.Log;

/**
 * Created by Luis Fernando Santiago Ruiz on 11/22/17.
 */

public class ProductProvider extends ContentProvider {

    private static final int PRODUCTS = 100;
    private static final int PRODUCTS_ID = 101;
    private ProductReaderHelper mDBHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static{

        sUriMatcher.addURI(ProductContractClass.CONTENT_AUTHORITY ,ProductContractClass.PATH_PRODUCTS , PRODUCTS );
        sUriMatcher.addURI(ProductContractClass.CONTENT_AUTHORITY ,ProductContractClass.PATH_PRODUCTS + "/#" , PRODUCTS_ID );
    }

    @Override
    public boolean onCreate() {
        mDBHelper = new ProductReaderHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projections, @Nullable String selection, @Nullable String[] selectionsArgs, @Nullable String sortOrder) {

        SQLiteDatabase database = mDBHelper.getReadableDatabase();

        Cursor cursor;

        //Figuring out if the URI matcher can catch the specific code
        int match = sUriMatcher.match(uri);
        switch (match){
            case PRODUCTS:{
                cursor = database.query(ProductContractClass.ProductEntry.TABLE_NAME , projections , selection , selectionsArgs , null, null, sortOrder );
                Log.e("URI MATCHES" , "Its the product case");
                break;
            }

            case PRODUCTS_ID : {
                Log.e("URI MATCHES", "its the id product case");
                selection = ProductContractClass.ProductEntry._ID + "=?";
                selectionsArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                //Returning a query with the data selected
                cursor = database.query(ProductContractClass.ProductEntry.TABLE_NAME , projections , selection , selectionsArgs , null, null , sortOrder);
                break;
            }

            default: throw new IllegalArgumentException("Cannot query uri" + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return ProductContractClass.ProductEntry.CONTENT_LIST_TYPE;
            case PRODUCTS_ID:
                return ProductContractClass.ProductEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return insertPet(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        int rowsChanged = 0;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                // Delete all rows that match the selection and selection args
                rowsChanged = database.delete(ProductContractClass.ProductEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsChanged != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsChanged;

            case PRODUCTS_ID:
                // Delete a single row given by the ID in the URI
                selection = ProductContractClass.ProductEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsChanged = database.delete(ProductContractClass.ProductEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsChanged != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsChanged;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return updateProduct(uri, contentValues, selection, selectionArgs);
            case PRODUCTS_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = ProductContractClass.ProductEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateProduct(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private Uri insertPet(Uri uri, ContentValues values) {
        String productName = values.getAsString(ProductContractClass.ProductEntry.COLUMN_NAME_);
        Integer price = values.getAsInteger(ProductContractClass.ProductEntry.COLUMN_PRICE);
        Integer quantity = values.getAsInteger(ProductContractClass.ProductEntry.COLUMN_QUANTITY);
        String supplierName = values.getAsString(ProductContractClass.ProductEntry.COLUMN_SUPPLIER_NAME);
        String supplierEmail = values.getAsString(ProductContractClass.ProductEntry.COLUMN_SUPPLIER_EMAIL);
        Integer supplierNumber = values.getAsInteger(ProductContractClass.ProductEntry.COLUMN_SUPPLIER_NUMBER);

        if(productName == null){
            throw new IllegalArgumentException("Product require a name");
        }

        if(price == null){
            throw new IllegalArgumentException("Product require a price");
        }

        if(quantity == null){
            throw new IllegalArgumentException("Product require a quantity");
        }

        if(supplierName == null){
            throw new IllegalArgumentException("Product require a supplier name");
        }

        if(supplierEmail == null){
            throw new IllegalArgumentException("Product require a supplier Email");
        }

        if(supplierNumber == null){
            throw new IllegalArgumentException("Product require a supplier number");
        }

        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        Log.e("PROVIDER", "INSERTING ELEMENT");
        long id = database.insert(ProductContractClass.ProductEntry.TABLE_NAME, null, values);

        getContext().getContentResolver().notifyChange(uri , null);

        Log.e("RESULT" , "RESULT: " + id);
        return ContentUris.withAppendedId(uri, id);
    }

    private int updateProduct (Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(ProductContractClass.ProductEntry.COLUMN_NAME_)) {
            String name = values.getAsString(ProductContractClass.ProductEntry.COLUMN_NAME_);
            if (name == null) {
                throw new IllegalArgumentException("product requires a name");
            }
        }

        if (values.containsKey(ProductContractClass.ProductEntry.COLUMN_PRICE)) {
            Integer price = values.getAsInteger(ProductContractClass.ProductEntry.COLUMN_PRICE);
            if (price == null) {
                throw new IllegalArgumentException("product requires a price");
            }
        }

        if (values.containsKey(ProductContractClass.ProductEntry.COLUMN_QUANTITY)) {
            Integer quantity = values.getAsInteger(ProductContractClass.ProductEntry.COLUMN_QUANTITY);
            if (quantity == null) {
                throw new IllegalArgumentException("product requires a quantity");
            }
        }

        if(values.containsKey(ProductContractClass.ProductEntry.COLUMN_SUPPLIER_NAME)) {
            String nameSupplier = values.getAsString(ProductContractClass.ProductEntry.COLUMN_SUPPLIER_NAME);
            if (nameSupplier == null) {
                throw new IllegalArgumentException("product requires a name supplier");
            }
        }

        if(values.containsKey(ProductContractClass.ProductEntry.COLUMN_SUPPLIER_NUMBER)) {
            Integer numberSupplier = values.getAsInteger(ProductContractClass.ProductEntry.COLUMN_SUPPLIER_NUMBER);
            if (numberSupplier == null) {
                throw new IllegalArgumentException("product requires a number supplier");
            }
        }

        if(values.containsKey(ProductContractClass.ProductEntry.COLUMN_SUPPLIER_EMAIL)) {
            String email = values.getAsString(ProductContractClass.ProductEntry.COLUMN_SUPPLIER_EMAIL);
            if (email == null) {
                throw new IllegalArgumentException("product requires a email supplier");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDBHelper.getWritableDatabase();

        int rowsUpdated = database.update(ProductContractClass.ProductEntry.TABLE_NAME, values, selection, selectionArgs);
        if(rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Returns the number of database rows affected by the update statement
        return rowsUpdated;
    }
}
