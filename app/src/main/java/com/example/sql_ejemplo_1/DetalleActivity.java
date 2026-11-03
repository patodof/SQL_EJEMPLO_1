package com.example.sql_ejemplo_1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetalleActivity extends AppCompatActivity {
    public TextView txt_id, txt_nombre, txt_telefono;
    public Button boton_editar, boton_eliminar;

    public int id_actual ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalle);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txt_telefono = findViewById(R.id.txt_telefono);
        txt_id = findViewById(R.id.txt_id);
        txt_nombre = findViewById(R.id.txt_nombre);

        boton_editar = findViewById(R.id.boton_editar);
        boton_eliminar = findViewById(R.id.boton_eliminar);

        Intent intent_actual = getIntent();
        id_actual = intent_actual.getIntExtra("id_actual", -1);

        cargarContacto(id_actual);

        boton_eliminar.setOnClickListener(v -> {
            eliminarContacto(id_actual);
        });

        boton_editar.setOnClickListener(v -> {
            Intent editar_intent = new Intent(DetalleActivity.this, EditarActivity.class);
            editar_intent.putExtra("id_actual", id_actual);
            startActivity(editar_intent);
        });
    }

    public void cargarContacto(int id)
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = admin.getReadableDatabase();

        Cursor consulta = db.rawQuery("SELECT id,nombre,telefono FROM contactos WHERE id="+id, null);

        if(consulta.moveToFirst())
        {
            //existe valor
            txt_id.setText("# "+consulta.getInt(0));
            txt_nombre.setText("Nombre : "+consulta.getString(1));
            txt_telefono.setText("Telefono : "+consulta.getString(2));
        }
        else
        {
            //no existe valor
        }

        db.close();
        consulta.close();
    }

    public void eliminarContacto(int id)
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = admin.getReadableDatabase();

        db.delete("contactos", "id="+id, null);

        db.close();
        finish();
    }
}