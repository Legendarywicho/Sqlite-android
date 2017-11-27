package com.luis_santiago.sqlitedatacourse.tools;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;

import com.luis_santiago.sqlitedatacourse.R;
import com.luis_santiago.sqlitedatacourse.database.ProductContractClass;
import com.luis_santiago.sqlitedatacourse.model.Product;

import java.util.List;

/**
 * Created by Luis Fernando Santiago Ruiz on 11/22/17.
 */

public class ProductCursorAdapter extends CursorAdapter {

    public ProductCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor , 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_tem , parent , false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView productTextView = view.findViewById(R.id.product_name);
        TextView quantityTextView = view.findViewById(R.id.product_quantity);
        TextView priceTextview = view.findViewById(R.id.product_price);
        Button saleButton = view.findViewById(R.id.sale_button);

        final int id = cursor.getInt(cursor.getColumnIndexOrThrow(ProductContractClass.ProductEntry._ID));
        String product  = cursor.getString(cursor.getColumnIndexOrThrow(ProductContractClass.ProductEntry.COLUMN_NAME_));
        final int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(ProductContractClass.ProductEntry.COLUMN_QUANTITY));
        int price = cursor.getInt(cursor.getColumnIndexOrThrow(ProductContractClass.ProductEntry.COLUMN_PRICE));


        productTextView.setText(product);
        quantityTextView.setText(String.format("%s%s", context.getString(R.string.quantity_text), String.valueOf(quantity)));
        priceTextview.setText(String.format("%s%s", context.getString(R.string.price_new), String.valueOf(price)));
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newQuantity = quantity - 1;
                if(newQuantity>=0){
                    ContentValues values = new ContentValues();
                    Uri currentProductUri = ContentUris.withAppendedId(ProductContractClass.CONTENT_URI , id);
                    values.put(ProductContractClass.ProductEntry.COLUMN_QUANTITY , newQuantity);
                    int rowsAffected = context.getContentResolver().update(currentProductUri, values, null, null);
                    if (rowsAffected == 0) {
                        Toast.makeText(context,"Update failed",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Update Succesful",
                                Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context, "There are no products",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
