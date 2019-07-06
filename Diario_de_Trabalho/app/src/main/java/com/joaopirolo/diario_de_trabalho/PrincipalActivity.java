package com.joaopirolo.diario_de_trabalho;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

public class PrincipalActivity extends AppCompatActivity {


    private ConstraintLayout layout;

    private static final String CONFIG = "com.joaopirolo.diario_de_trabalho.PREFERENCIA_CORES";
    private static final String CORES = "CORES";
    public static int selecionaCor = Color.TRANSPARENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        layout = findViewById(R.id.configs);
        mostrarPreferenciaDeCor();
    }
    public void mostrarPreferenciaDeCor(){
        SharedPreferences sharedPreferences = getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        selecionaCor = sharedPreferences.getInt(CORES, selecionaCor);
        mostraCores();
    }
    private void mostraCores(){
        layout.setBackgroundColor(selecionaCor);
    }
    private void salvarPreferencia(int selecionaCor){
        SharedPreferences sharedPreferences = getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(CORES, selecionaCor);
        editor.commit();

        selecionaCor = selecionaCor;
        mostraCores();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.info:
                Intent intent = new Intent (this, InfoActivity.class);
                startActivityForResult(intent, 0);
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }
    }

