package com.example.frutiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText et_nombre;
    TextView tv_bestscore;
    ImageView iv_personaje;
    Button play;
    MediaPlayer mp;

    int num_aleatorio = (int)(Math.random() * 10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_nombre=(EditText)findViewById(R.id.et_nombre);
        tv_bestscore=(TextView)findViewById(R.id.tv_bestscore);
        iv_personaje=(ImageView)findViewById(R.id.iv_personaje);
        play=(Button)findViewById(R.id.play);

        //poner icono en la parte de arriba de la app
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        //bloque para poner imagen de inicio de modo aleatorio
        int id;
        if (num_aleatorio == 0 || num_aleatorio == 10){
            id = getResources().getIdentifier("mango", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        }else if (num_aleatorio == 1 || num_aleatorio == 9){
            id = getResources().getIdentifier("fresa", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        }else if (num_aleatorio == 2 || num_aleatorio == 8){
            id = getResources().getIdentifier("manzana", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        }else if (num_aleatorio == 3 || num_aleatorio == 7){
            id = getResources().getIdentifier("sandia", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        }else if (num_aleatorio == 4 || num_aleatorio == 5 || num_aleatorio == 6){
            id = getResources().getIdentifier("uva", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        }
        /******************************************************************************************/

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD", null, 1);
        SQLiteDatabase BD = admin.getWritableDatabase();

        Cursor consulta = BD.rawQuery("select * from puntaje where score = (select max(score) from puntaje)",null);

        if (consulta.moveToFirst()){
            String temp_nombre = consulta.getString(0);
            String temp_score = consulta.getString(1);
            tv_bestscore.setText("record: "+temp_score+" de "+temp_nombre);
            BD.close();
        }else {
            BD.close();
        }

        //aqui ponemos la cancion para este activity
        mp = MediaPlayer.create(this, R.raw.alphabet_song);
        mp.start();
        mp.setLooping(true);
        //*****************************************************************************************
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jugar();
            }
        });

    }

    public void jugar(){
        String nombre = et_nombre.getText().toString();

        if (!nombre.equals("")){
            mp.stop();
            mp.release();

            Intent intent = new Intent(this, nivel1.class);
            intent.putExtra("jugador", nombre);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(this, "primero debes escribir tu nombre", Toast.LENGTH_SHORT).show();

            et_nombre.requestFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et_nombre, InputMethodManager.SHOW_IMPLICIT);

        }
    }
    //muy importatnte cada vez que se presione un boton como regresar
    @Override
    public void onBackPressed(){

    }
}
