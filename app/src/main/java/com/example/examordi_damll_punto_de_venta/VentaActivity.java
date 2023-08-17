package com.example.examordi_damll_punto_de_venta;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class VentaActivity extends AppCompatActivity {

    private ListView listViewProducts;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta);

        databaseHelper = new DatabaseHelper(this);
        listViewProducts = findViewById(R.id.listViewProducts);

        showProducts();
    }

    private void showProducts() {
        ArrayList<String> productList = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] projection = {
                DatabaseHelper.COLUMN_NOMBRE,
                DatabaseHelper.COLUMN_PRECIO,
                DatabaseHelper.COLUMN_CANTIDAD
        };

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String productName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOMBRE));
                double productPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRECIO));
                int productQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CANTIDAD));

                productList.add(productName + " - Precio: " + productPrice + " - Cantidad: " + productQuantity);
            } while (cursor.moveToNext());

            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item_product, productList);
        listViewProducts.setAdapter(adapter);
    }
}