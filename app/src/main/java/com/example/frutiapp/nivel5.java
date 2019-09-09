package com.example.frutiapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
;

public class nivel5 extends AppCompatActivity {

    TextView tv_nombre, tv_score;
    ImageView iv_auno, iv_ados, iv_vidas;
    EditText et_respuesta;
    MediaPlayer mp, mp_great, mp_bad;
    Button btn2;

    int score, numaleatorio_uno, numaleatorio_dos, resultado, vidas=3;
    String nombre_jugador, string_score, string_vidas;

    String numero[] = {"cero","uno","dos","tres","cuatro","cinco","seis","siete","ocho","nueve"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nivel5);

        Toast.makeText(this,"nivel 5 - multiplicacion", Toast.LENGTH_SHORT).show();

        tv_nombre=(TextView)findViewById(R.id.tv1);
        tv_score=(TextView)findViewById(R.id.tv_score);
        iv_vidas=(ImageView)findViewById(R.id.iv_vidas);
        iv_auno=(ImageView)findViewById(R.id.iv_num1);
        iv_ados=(ImageView)findViewById(R.id.iv_num2);
        et_respuesta=(EditText)findViewById(R.id.et_resultado);
        btn2=(Button)findViewById(R.id.btn2);

        //se reciben parametros de el otro activity
        nombre_jugador = getIntent().getStringExtra("jugador");
        tv_nombre.setText("jugador: "+nombre_jugador);

        string_score = getIntent().getStringExtra("score");
        score = Integer.parseInt(string_score);
        tv_score.setText("Score: "+score);

        string_vidas = getIntent().getStringExtra("vidas");
        vidas = Integer.parseInt(string_vidas);
        if (vidas==3){
            iv_vidas.setImageResource(R.drawable.tresvidas);
        } if (vidas == 2){
            iv_vidas.setImageResource(R.drawable.dosvidas);
        } if (vidas == 1){
            iv_vidas.setImageResource(R.drawable.unavida);
        }

        //se agrega el icono en el action var
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        //agregar los audios
        //mp.stop();
        mp = MediaPlayer.create(this, R.raw.goats);
        mp.start();
        mp.setLooping(true);

        //pequeños audios que todavia no se ejecutan
        mp_great = MediaPlayer.create(this, R.raw.wonderful);
        mp_bad = MediaPlayer.create(this, R.raw.bad);

        numAleatorio();

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comparar();
            }
        });
    }

    public void comparar(){
        String respuesta = et_respuesta.getText().toString();

        if (!respuesta.equals("")){

            int respuesta_jugador = Integer.parseInt(respuesta);
            if (resultado == respuesta_jugador){

                mp_great.start();
                score++;
                tv_score.setText("Score: "+score);
                et_respuesta.setText("");
                BaseDeDatos();

            }else {
                mp_bad.start();
                vidas--;
                BaseDeDatos();

                switch (vidas){
                    case 3:
                        iv_vidas.setImageResource(R.drawable.tresvidas);
                        break;
                    case 2:
                        Toast.makeText(this,"te quedan 2 manzanas", Toast.LENGTH_SHORT).show();
                        iv_vidas.setImageResource(R.drawable.dosvidas);
                        break;
                    case 1:
                        Toast.makeText(this,"te quedan 1 manzanas", Toast.LENGTH_SHORT).show();
                        iv_vidas.setImageResource(R.drawable.unavida);
                        break;
                    case 0:
                        Toast.makeText(this,"perdiste dejopen", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        mp.stop();
                        mp.release();
                        break;
                }
                et_respuesta.setText("");
            }

            numAleatorio();

        }else{
            Toast.makeText(this,"escribe tu respuesta", Toast.LENGTH_SHORT).show();
        }
    }

    public void numAleatorio(){
        if (score <= 49){
            numaleatorio_uno = (int) (Math.random() * 10);
            numaleatorio_dos = (int) (Math.random() * 10);

            resultado = numaleatorio_uno * numaleatorio_dos;

            if (resultado >= 0){

                for (int i=0; i<numero.length; i++){
                    int id = getResources().getIdentifier(numero[i], "drawable", getPackageName());
                    if (numaleatorio_uno == i){
                        iv_auno.setImageResource(id);
                    }if (numaleatorio_dos == i){
                        iv_ados.setImageResource(id);
                    }
                }
            }else {
                numAleatorio();
            }


        }else {
            Intent intent= new Intent(this, nivel6.class);

            string_score= String.valueOf(score);
            string_vidas= String.valueOf(vidas);
            intent.putExtra("jugador", nombre_jugador);
            intent.putExtra("score", string_score);
            intent.putExtra("vidas", string_vidas);

            startActivity(intent);
            finish();
            mp.stop();
            mp.release();
        }
    }
    public void BaseDeDatos(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD", null, 1);
        SQLiteDatabase BD = admin.getWritableDatabase();

        Cursor consulta = BD.rawQuery("select * from puntaje where score = (select max(score) from puntaje)",null);
        if (consulta.moveToFirst()){
            String temp_nombre = consulta.getString(0);
            String temp_score = consulta.getString(1);

            int best_score = Integer.parseInt(temp_score);

            if (score > best_score){
                ContentValues modificacion = new ContentValues();
                modificacion.put("nombre", nombre_jugador);
                modificacion.put("score", score);

                BD.update("puntaje", modificacion, "score="+best_score, null);
            }
            BD.close();
        }else {
            ContentValues insertar = new ContentValues();

            insertar.put("nombre",nombre_jugador);
            insertar.put("score",score);

            BD.insert("puntaje", null, insertar);
            BD.close();
        }
    }

    public void onBackPressed(){

    }
}

