package com.example.sql_ejemplo_1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditarActivity extends AppCompatActivity {
    EditText input_nombre, input_telefono;
    Button boton_enviar;

    public int id_actual ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        input_nombre = findViewById(R.id.input_nombre2);
        input_telefono = findViewById(R.id.input_telefono2);
        boton_enviar = findViewById(R.id.boton_enviar2);

        Intent intent_actual = getIntent();
        id_actual = intent_actual.getIntExtra("id_actual", -1);

        cargarContacto(id_actual);

        boton_enviar.setOnClickListener(v -> {
            actualizarContacto(id_actual);
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
            input_nombre.setText(consulta.getString(1));
            input_telefono.setText(consulta.getString(2));
        }
        else
        {
            //no existe valor
        }

        db.close();
        consulta.close();
    }

    public void actualizarContacto(int id)
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = admin.getWritableDatabase();

        String valor_nombre = input_nombre.getText().toString();
        String valor_telefono = input_telefono.getText().toString();

        ContentValues valores = new ContentValues();
        valores.put("nombre", valor_nombre);
        valores.put("telefono", valor_telefono);

        db.update("contactos", valores, "id="+id, null);

        db.close();
        finish();
    }
}