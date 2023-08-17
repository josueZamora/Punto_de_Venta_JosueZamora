package com.example.examordi_damll_punto_de_venta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnGoToInventory;
    Button btnGoToSale;
    Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGoToInventory = findViewById(R.id.btnIrInventory);
        btnGoToSale = findViewById(R.id.btnIrVenta);
        btnExit = findViewById(R.id.btnSalir);

        btnGoToInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInventoryActivity();
            }
        });

        btnGoToSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSaleActivity();
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cierra la actividad actual y vuelve a la pantalla anterior (o cierra la app si no hay pantallas anteriores)
            }
        });
    }

    private void openInventoryActivity() {
        Intent intent = new Intent(this, InventoryActivity.class);
        startActivity(intent);
    }

    private void openSaleActivity() {
        Intent intent = new Intent(this, VentaActivity.class);
        startActivity(intent);
    }
}