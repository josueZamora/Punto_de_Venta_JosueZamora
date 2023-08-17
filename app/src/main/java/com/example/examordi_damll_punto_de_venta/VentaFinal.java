package com.example.examordi_damll_punto_de_venta;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class VentaFinal extends AppCompatActivity {

    private ListView listViewSales;
    private TextView tvTotalAmount;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta_final);

        databaseHelper = new DatabaseHelper(this);
        listViewSales = findViewById(R.id.listViewSales);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);

        showSales();
    }

    private void showSales() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String[] projection = {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_NOMBRE,
                DatabaseHelper.COLUMN_PRECIO,
                DatabaseHelper.COLUMN_CANTIDAD,
                DatabaseHelper.COLUMN_IMPORTE
        };

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_VENTA,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        String[] fromColumns = {
                DatabaseHelper.COLUMN_PRODUCTO_ID,
                DatabaseHelper.COLUMN_PRODUCTO_NOMBRE,
                DatabaseHelper.COLUMN_PRODUCTO_PRECIO,
                DatabaseHelper.COLUMN_CANTIDAD,
                DatabaseHelper.COLUMN_IMPORTE
        };

        int[] toViews = {
                R.id.tvProductId,
                R.id.tvProductName,
                R.id.tvProductPrice,
                R.id.tvQuantity,
                R.id.tvTotalAmount
        };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.list_item_venta,
                cursor,
                fromColumns,
                toViews,
                0
        );

        listViewSales.setAdapter(adapter);

        // Calcular el total de la venta
        double totalAmount = 0;
        if (cursor.moveToFirst()) {
            do {
                totalAmount += cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_IMPORTE));
            } while (cursor.moveToNext());
        }

        tvTotalAmount.setText("Total: $" + String.format("%.2f", totalAmount));

        cursor.close();
    }
}