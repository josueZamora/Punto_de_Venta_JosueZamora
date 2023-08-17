package com.example.examordi_damll_punto_de_venta;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String COLUMN_PRODUCTO_ID = "productId";
    public static final String COLUMN_PRODUCTO_NOMBRE = "productName";
    public static final String COLUMN_PRODUCTO_PRECIO = "productPrice";
    public static final String COLUMN_IMPORTE = "totalAmount";
    public static final String TABLE_VENTA = "VENTA";

    // Nombre de la base de datos
    private static final String DATABASE_NAME = "productos.db";
    // Nombre de la tabla de productos
    public static final String TABLE_NAME = "productos";
    // Versión de la base de datos (puedes incrementarla si cambias la estructura de la tabla)
    private static final int DATABASE_VERSION = 1;

    // Nombres de las columnas de la tabla
    public static final String COLUMN_ID = "productId";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_PRECIO = "precio";
    public static final String COLUMN_CANTIDAD = "cantidad";
    public static final String COLUMN_IMAGEN = "imagen";
    public static final String COLUMN_FECHA = "fecha";

    // Sentencia SQL para crear la tabla de productos
    private static final String CREATE_TABLE_PRODUCTS =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NOMBRE + " TEXT, " +
                    COLUMN_PRECIO + " REAL, " +
                    COLUMN_CANTIDAD + " INTEGER, " +
                    COLUMN_IMAGEN + " TEXT, " +
                    COLUMN_FECHA + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ")";

    // Sentencia SQL para crear la tabla "venta"
    private static final String SQL_CREATE_TABLE_SALE =
            "CREATE TABLE " + TABLE_VENTA + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_PRODUCTO_ID + " INTEGER," +
                    COLUMN_PRODUCTO_NOMBRE + " TEXT," +
                    COLUMN_PRODUCTO_PRECIO + " REAL," +
                    COLUMN_CANTIDAD + " INTEGER," +
                    COLUMN_IMPORTE + " REAL" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(SQL_CREATE_TABLE_SALE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
