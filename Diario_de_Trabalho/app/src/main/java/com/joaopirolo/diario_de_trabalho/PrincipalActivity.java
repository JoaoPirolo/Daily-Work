package com.joaopirolo.diario_de_trabalho;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.joaopirolo.diario_de_trabalho.model.CategoriaDeServicos;

public class PrincipalActivity extends AppCompatActivity {


    private ConstraintLayout layout;

    private static final String CONFIG = "com.joaopirolo.diario_de_trabalho.PREFERENCIA_CORES";
    private static final String CORES = "CORES";
    public static int selecionaCor = Color.TRANSPARENT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        layout = findViewById(R.id.principal);
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
    public void selecaoCor(){
        CharSequence opcoes[] = new CharSequence[]{getString(R.string.red),getString(R.string.green),getString(R.string.gray)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Escolha de Cor");

        builder.setItems(opcoes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    salvarPreferencia(Color.RED);}
                 else if(which == 1){
                     salvarPreferencia(Color.GREEN);
                    }else if(which == 2){
                     salvarPreferencia(Color.GRAY);
                    }

            }
        });
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
            case R.id.servicos:
                Intent novaIntent = new Intent(this, ListaServicosActivity.class);
                startActivityForResult(novaIntent, 0);
                return true;
            case R.id.tiposServicos:
                novaIntent = new Intent(this, ListaCategoriaDeServicosActivity.class);
                startActivityForResult(novaIntent,0);
                return true;
            case R.id.cores:
                selecaoCor();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }
    }

