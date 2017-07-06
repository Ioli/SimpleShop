package com.example.android.simpleshop;


import android.content.ContentUris;
import android.content.ContentValues;

import android.content.Intent;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.simpleshop.data.ProductContract;
import com.example.android.simpleshop.data.ProductContract.ProductEntry;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int PRODUCT_LOADER = 0;

    ProductCursorAdapter mCursorAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    View emptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        /**
         // Find the ListView which will be populated with the product data
         ListView productListView = (ListView) findViewById(R.id.list);

         // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
         View emptyView = findViewById(R.id.empty_view);
         productListView.setEmptyView(emptyView);

         mCursorAdaptor = new ProductCursorAdapter(this, null);
         productListView.setAdapter(mCursorAdaptor);

         //Set up the item click listener
         productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
        Uri currentPetUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);
        intent.setData(currentPetUri);
        startActivity(intent);
        }
        });

         getLoaderManager().initLoader(PRODUCT_LOADER, null, this);**/
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mLayoutManager = new LinearLayoutManager(CatalogActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        emptyView = findViewById(R.id.empty_view);

        mCursorAdapter = new ProductCursorAdapter(this, null);
        mRecyclerView.setAdapter(mCursorAdapter);

        getSupportLoaderManager().initLoader(PRODUCT_LOADER, null, this);

    }


    public void onItemClick(long id) {
        Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);

        Uri currentProductUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, id);
        intent.setData(currentProductUri);

        startActivity(intent);
    }


    /**
     * Helper method to insert hardcoded pet data into the database. For debugging purposes only.
     */
    private void insertProduct() {
        // Create a ContentValues object where column names are the keys,
        // and waters's product attributes are the values.
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME,"Water");
        values.put(ProductEntry.COLUMN_PRODUCT_IMG,"android.resource://com.example.android.inventory/drawable/img1_product");
        values.put(ProductEntry.COLUMN_PRODUCT_INFO,"Fresh water from mountain Olympus. In big bottle.");
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME,"Mountain");
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_MAIL,"olympuswater@gmail.com");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE,"0.50");
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY,"7");

        // Insert a new row for Water into the provider using the ContentResolver.
        // Use the {@link ProductEntry#CONTENT_URI} to indicate that we want to insert
        // into the products database table.
        // Receive the new content URI that will allow us to access Water's data in the future.
        Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);
        Log.v("CatalogActivity", "Uri of new product: " + newUri);
    }

    public void onBuyClick(long id, int quantity) {
        Uri currentProductUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, id);
        Log.v("CatalogActivity", "Uri: " + currentProductUri);
        quantity--;
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);
        getContentResolver().update(currentProductUri, values, null, null);
    }


    /**
     * Helper method to delete all products in the database.
     */
    private void deleteAllProducts() {
        int rowsDeleted = getContentResolver().delete(ProductEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from product database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertProduct();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllProducts();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_IMG,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_MAIL,
                ProductEntry.COLUMN_PRODUCT_INFO};

        return new CursorLoader(this,
                ProductEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);

    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }


}