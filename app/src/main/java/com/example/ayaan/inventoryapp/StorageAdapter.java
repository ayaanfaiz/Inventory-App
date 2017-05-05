package com.example.ayaan.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ayaan.inventoryapp.data.StorageContract;
import com.example.ayaan.inventoryapp.data.StorageContract.StorageEntry;

import static com.example.ayaan.inventoryapp.R.id.product_image;

/**
 * Created by AYAAN on 5/4/2017.
 */

public class StorageAdapter extends CursorAdapter {
    public StorageAdapter(Context context, Cursor cursor) {
        super(context, cursor,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_view,viewGroup,false);
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView product = (TextView)view.findViewById(R.id.product);
        TextView quantity = (TextView)view.findViewById(R.id.quantity);
        TextView price = (TextView)view.findViewById(R.id.price);
        ImageView image = (ImageView)view.findViewById(product_image);

        String product_name = cursor.getString(cursor.getColumnIndex(StorageEntry.COLUMN_PRODUCT_NAME));
        String product_quantity = cursor.getString(cursor.getColumnIndex(StorageEntry.COLUMN_PRODUCT_QUANTITY));
        String product_price = cursor.getString(cursor.getColumnIndex(StorageEntry.COLUMN_PRODUCT_PRICE));
        int product_image = cursor.getInt(cursor.getColumnIndex(StorageEntry.COLUMN_PRODUCT_IMAGE));


        product.setText(product_name);
        quantity.setText(product_quantity);
        price.setText(product_price);
        image.setImageResource(product_image);
    }
}
