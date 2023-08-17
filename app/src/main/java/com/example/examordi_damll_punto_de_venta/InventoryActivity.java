package com.example.examordi_damll_punto_de_venta;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class InventoryActivity extends AppCompatActivity {

    private Button btnInsertProduct;
    private Button btnDeleteProduct;
    private Button btnUpdateProduct;
    private Button btnSelectImage; // Nuevo botón para seleccionar imagen
    private DatabaseHelper databaseHelper;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        databaseHelper = new DatabaseHelper(this);

        btnInsertProduct = findViewById(R.id.btnInsertProduct);
        btnDeleteProduct = findViewById(R.id.btnDeleteProduct);
        btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        btnSelectImage = findViewById(R.id.btnSelectImage);

        btnInsertProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInsertProductDialog();
            }
        });

        btnDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteProductDialog();
            }
        });

        btnUpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateProductDialog();
            }
        });

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void showInsertProductDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_insert_product, null);
        dialogBuilder.setView(dialogView);

        final EditText etNombre = dialogView.findViewById(R.id.etNombre);
        final EditText etPrecio = dialogView.findViewById(R.id.etPrecio);
        final EditText etCantidad = dialogView.findViewById(R.id.etCantidad);
        final EditText etImagen = dialogView.findViewById(R.id.etImagen);

        dialogBuilder.setTitle("Insertar Producto");
        dialogBuilder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String nombre = etNombre.getText().toString();
                String precioStr = etPrecio.getText().toString();
                String cantidadStr = etCantidad.getText().toString();
                String imagen = etImagen.getText().toString();

                if (!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(precioStr) && !TextUtils.isEmpty(cantidadStr)) {
                    double precio = Double.parseDouble(precioStr);
                    int cantidad = Integer.parseInt(cantidadStr);

                    insertProduct(nombre, precio, cantidad, imagen);
                } else {
                    showToast("Por favor, completa todos los campos");
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void insertProduct(String nombre, double precio, int cantidad, String imagen) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NOMBRE, nombre);
        values.put(DatabaseHelper.COLUMN_PRECIO, precio);
        values.put(DatabaseHelper.COLUMN_CANTIDAD, cantidad);
        values.put(DatabaseHelper.COLUMN_IMAGEN, imagen);

        long newRowId = db.insert(DatabaseHelper.TABLE_NAME, null, values);
        if (newRowId != -1) {
            showToast("Producto insertado correctamente");
        } else {
            showToast("Error al insertar producto");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showDeleteProductDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_delete_product, null);
        dialogBuilder.setView(dialogView);

        final EditText etProductName = dialogView.findViewById(R.id.etProductName);

        dialogBuilder.setTitle("Eliminar Producto");
        dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String productName = etProductName.getText().toString();

                if (!TextUtils.isEmpty(productName)) {
                    deleteProduct(productName);
                } else {
                    showToast("Por favor, ingresa el nombre del producto a eliminar");
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void deleteProduct(String productName) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String selection = DatabaseHelper.COLUMN_NOMBRE + "=?";
        String[] selectionArgs = { productName };

        int deletedRows = db.delete(DatabaseHelper.TABLE_NAME, selection, selectionArgs);
        if (deletedRows > 0) {
            showToast("Producto eliminado correctamente");
        } else {
            showToast("Producto no encontrado o error al eliminar");
        }
    }

    private void showUpdateProductDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_update_product, null);
        dialogBuilder.setView(dialogView);

        final EditText etProductName = dialogView.findViewById(R.id.etProductName);
        final EditText etNewPrice = dialogView.findViewById(R.id.etNewPrice);
        final EditText etNewQuantity = dialogView.findViewById(R.id.etNewQuantity);

        dialogBuilder.setTitle("Actualizar Producto");
        dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String productName = etProductName.getText().toString();
                String newPriceStr = etNewPrice.getText().toString();
                String newQuantityStr = etNewQuantity.getText().toString();

                if (!TextUtils.isEmpty(productName) && !TextUtils.isEmpty(newPriceStr) && !TextUtils.isEmpty(newQuantityStr)) {
                    double newPrice = Double.parseDouble(newPriceStr);
                    int newQuantity = Integer.parseInt(newQuantityStr);

                    updateProduct(productName, newPrice, newQuantity);
                } else {
                    showToast("Por favor, completa todos los campos");
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void updateProduct(String productName, double newPrice, int newQuantity) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PRECIO, newPrice);
        values.put(DatabaseHelper.COLUMN_CANTIDAD, newQuantity);

        String selection = DatabaseHelper.COLUMN_NOMBRE + "=?";
        String[] selectionArgs = { productName };

        int updatedRows = db.update(DatabaseHelper.TABLE_NAME, values, selection, selectionArgs);
        if (updatedRows > 0) {
            showToast("Producto actualizado correctamente");
        } else {
            showToast("Producto no encontrado o error al actualizar");
        }
    }

    private void selectImage() {
        final CharSequence[] options = { "Tomar Foto", "Elegir de Galería", "Cancelar" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar Imagen");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Tomar Foto")) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                } else if (options[item].equals("Elegir de Galería")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
                } else if (options[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    // Obtener la imagen tomada por la cámara
                    // La imagen se almacena en extras como un objeto Bitmap
                    // Aquí puedes guardar la imagen en la base de datos o hacer lo que desees con ella.
                    showToast("Imagen tomada correctamente");
                }
            } else if (requestCode == REQUEST_IMAGE_GALLERY) {
                if (data != null) {
                    // Obtener la imagen seleccionada de la galería
                    // La imagen seleccionada se representa como una Uri
                    // Aquí puedes obtener la Uri de la imagen y guardarla en la base de datos o hacer lo que desees con ella.
                    imageUri = data.getData();
                    showToast("Imagen seleccionada correctamente");
                }
            }
        }
    }
}