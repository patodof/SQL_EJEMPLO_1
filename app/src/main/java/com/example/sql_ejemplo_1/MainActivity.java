package com.example.sql_ejemplo_1;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Datos del XML
    EditText input_nombre, input_telefono;
    Button boton_enviar;
    ListView lista_contactos;

    //Datos necesarios para ListView
    List<String> datos_contactos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //incializar variables
        input_nombre = findViewById(R.id.input_nombre);
        input_telefono = findViewById(R.id.input_telefono);
        boton_enviar = findViewById(R.id.boton_enviar);
        lista_contactos = findViewById(R.id.lista_contactos);

        //cargo mis datos desde la base de datos
        cargarContactos();

        boton_enviar.setOnClickListener(v -> {
            crearContacto();
        });
    }

    public void cargarContactos()
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = admin.getReadableDatabase();

        datos_contactos = new ArrayList<>();
        //CONSULTAR DATOS A LA BASE DE DATOS
        Cursor fila = db.rawQuery("SELECT id,nombre,telefono FROM contactos", null);

        //validar que tenga por lo menos un dato
        if (fila.moveToFirst())
        {
            //tiene como minimo un dato
            do {
                //obtener los datos de la fila
                int id_actual = fila.getInt(0);
                String nombre_actual = fila.getString(1);
                String telefono_actual = fila.getString(2);

                String contacto_actual = "# "+id_actual+" nombre "+nombre_actual+" telefono "+telefono_actual;
                datos_contactos.add(contacto_actual);
                
            } while (fila.moveToNext());
        }
        else
        {
            //si no retorna nada de la base de datos
            datos_contactos.add("No existen contactos en la base de datos");
        }

        //adapter
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,datos_contactos );
        lista_contactos.setAdapter(adapter);
    }

    public void crearContacto()
    {
        String nombre_actual = String.valueOf(input_nombre.getText());
        String telefono_actual = String.valueOf(input_telefono.getText());

        if (nombre_actual.isEmpty() == false & telefono_actual.isEmpty() == false )
        {
            //si nombre tiene valor y telefono tiene valor entonces agregamos
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
            SQLiteDatabase db = admin.getWritableDatabase();

            ContentValues nuevo_dato = new ContentValues();
            nuevo_dato.put("nombre", nombre_actual);
            nuevo_dato.put("telefono", telefono_actual);

            db.insert("contactos", null, nuevo_dato);

            //reiniciar los inputs
            input_nombre.setText("");
            input_telefono.setText("");

            db.close();

            cargarContactos();

        }
        else
        {
            Toast.makeText(this, "Ingrese un nombre y telefono para agregar", Toast.LENGTH_LONG).show();
        }
    }
















}