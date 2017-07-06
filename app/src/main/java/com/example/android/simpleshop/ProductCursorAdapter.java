/*
 * Copyright (C) 2017 The Android Open Source Project
 * This app "Simple Shop" or "periptero" in Greek
 * is an app to control products storage for a simple shop
 * Is created with android studio 2.3.1
 * as exercise for Android Basics by Google Nanodegree Program
 * "Inventory app " by Dimitra Christina Nikolaidou
 */
package com.example.android.simpleshop;


import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.simpleshop.data.ProductContract;

/**
 * {@link ProductCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of product data as its data source. This adapter knows
 * how to create list items for each row of product data in the {@link Cursor}.
 */
public class ProductCursorAdapter extends CursorRecyclerAdapter<ProductCursorAdapter.ViewHolder> {
    private CatalogActivity activity = new CatalogActivity();


    /**
     * Constructs a new {@link ProductCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ProductCursorAdapter(CatalogActivity context, Cursor c) {
        super(context, c);
        this.activity = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView nameTextView;
        protected TextView priceTextView;
        protected TextView quantityTextView;
        protected ImageView buy;
        protected ImageView productImageView;


        public ViewHolder(View view) {
            super(view);
            // Find individual views that we want to modify in the list item layout

            nameTextView = (TextView) view.findViewById(R.id.item_name);
            productImageView = (ImageView) view.findViewById(R.id.product_image);
            priceTextView = (TextView) view.findViewById(R.id.product_price);
            quantityTextView = (TextView) view.findViewById(R.id.txt_quantity);
            buy = (ImageView) view.findViewById(R.id.buyNow);

        }

    }


    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param viewType The cursor from which to get the data. The cursor is already
     *                 moved to the correct position.
     * @param parent   The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    /**
     * This method binds the product data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current product can be set on the name TextView
     * in the list item layout.
     *
     * @param view   Existing view, returned earlier by newView() method
     * @param cursor The cursor from which to get the data. The cursor is already moved to the
     *               correct row.
     */

    @Override
    public void onBindViewHolder(ViewHolder view, Cursor cursor) {
        final int mQuantity;


        // Find the columns of product attributes that we're interested in
        final long id = cursor.getLong(cursor.getColumnIndex(ProductContract.ProductEntry._ID));
        int imgColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_IMG);
        int nameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);

        // Read the product attributes from the Cursor for the current product
        String productName = cursor.getString(nameColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);
        int quantity = cursor.getInt(quantityColumnIndex);
        String imageUriString = cursor.getString(imgColumnIndex);
        Uri imageUri = Uri.parse(imageUriString);
        mQuantity = quantity;

        // Update the TextViews with the attributes for the current product
        view.nameTextView.setText(productName);
        view.productImageView.setImageURI(imageUri);
        view.productImageView.invalidate();
        view.priceTextView.setText(productPrice);
        view.quantityTextView.setText(String.valueOf(quantity));

        view.quantityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onItemClick(id);
            }
        });

        view.priceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onItemClick(id);
            }
        });

        view.productImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onItemClick(id);
            }
        });
        view.nameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onItemClick(id);
            }
        });

        view.buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mQuantity > 0) {
                    activity.onBuyClick(id, mQuantity);
                } else {
                    Toast.makeText(activity, "Quantity Unavailable", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
