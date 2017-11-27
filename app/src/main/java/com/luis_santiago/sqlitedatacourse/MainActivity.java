package com.luis_santiago.sqlitedatacourse;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.luis_santiago.sqlitedatacourse.database.ProductContractClass;
import com.luis_santiago.sqlitedatacourse.database.ProductContractClass.ProductEntry;
import com.luis_santiago.sqlitedatacourse.model.Product;
import com.luis_santiago.sqlitedatacourse.tools.ProductCursorAdapter;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private FloatingActionButton mFab;
    private ListView mListview;
    private View emptyView;
    private ProductCursorAdapter productCursorAdapter;
    public static final int LOADER_PRODUCT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this , NewProductActivity.class));
            }
        });
        mListview.setEmptyView(emptyView);
        productCursorAdapter = new ProductCursorAdapter(this , null);
        mListview.setAdapter(productCursorAdapter);
        mListview.setOnItemClickListener(this);

        //Setting loader
        getLoaderManager().initLoader(LOADER_PRODUCT , null ,this);
    }

    private void init(){
        mFab = findViewById(R.id.button);
        mListview = findViewById(R.id.list);
        emptyView = findViewById(R.id.empty_view);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String [] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_NAME_,
                ProductEntry.COLUMN_PRICE,
                ProductEntry.COLUMN_QUANTITY,
                ProductEntry.COLUMN_PRICE,
                ProductEntry.COLUMN_SUPPLIER_NAME,
                ProductEntry.COLUMN_SUPPLIER_EMAIL,
                ProductEntry.COLUMN_SUPPLIER_NUMBER,
        };
        return new CursorLoader(this,ProductContractClass.CONTENT_URI , projection , null , null , null );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        productCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        productCursorAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
        Log.e("TAG" , "Touching");
        Intent intent = new Intent(MainActivity.this, NewProductActivity.class);
        Uri currentProductUri = ContentUris.withAppendedId(ProductContractClass.CONTENT_URI , id);
        intent.setData(currentProductUri);
        startActivity(intent);
    }
}
