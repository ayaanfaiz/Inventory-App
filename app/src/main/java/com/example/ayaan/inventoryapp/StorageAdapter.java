package com.example.ayaan.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView product = (TextView)view.findViewById(R.id.product);
        TextView quantity = (TextView)view.findViewById(R.id.quantity);
        TextView price = (TextView)view.findViewById(R.id.price);
        ImageView image = (ImageView)view.findViewById(product_image);
        TextView number = (TextView)view.findViewById(R.id.phone_number);

        String product_name = cursor.getString(cursor.getColumnIndex(StorageEntry.COLUMN_PRODUCT_NAME));
        final int product_quantity = cursor.getInt(cursor.getColumnIndex(StorageEntry.COLUMN_PRODUCT_QUANTITY));
        String product_price = cursor.getString(cursor.getColumnIndex(StorageEntry.COLUMN_PRODUCT_PRICE));
        String product_image = cursor.getString(cursor.getColumnIndex(StorageEntry.COLUMN_PRODUCT_IMAGE));
        Log.v("Adapter: ",product_image);
        String product_number = cursor.getString(cursor.getColumnIndex(StorageEntry.COLUMN_PHONE_NUMBER));
        final int product_id = cursor.getInt(cursor.getColumnIndex(StorageEntry._ID));
        Button button = (Button)view.findViewById(R.id.sale_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(product_quantity>0) {
                    int pr_quantity = product_quantity - 1;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(StorageEntry.COLUMN_PRODUCT_QUANTITY, pr_quantity);
                    Uri uri = ContentUris.withAppendedId(StorageEntry.CONTENT_URI, product_id);
                    context.getContentResolver().update(uri, contentValues, null, null);
                    Log.e("button", "edfg");
                }
                else
                    Toast.makeText(view.getContext(),"Cannot Sale as the product is finished",Toast.LENGTH_SHORT).show();
            }
        });
        String quan = Integer.toString(product_quantity);
        product.setText(product_name);
        quantity.setText(quan);
        price.setText(product_price);
        image.setImageURI(Uri.parse(product_image));
        number.setText(product_number);
    }
}
