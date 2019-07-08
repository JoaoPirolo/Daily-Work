package com.joaopirolo.diario_de_trabalho;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.joaopirolo.diario_de_trabalho.model.CategoriaDeServicos;
import com.joaopirolo.diario_de_trabalho.persistence.DiarioDeTrabalhoDatabase;
import com.joaopirolo.diario_de_trabalho.utils.UtilsAviso;

import java.util.List;

public class CategoriasDeServicosActivity extends AppCompatActivity {

    public static final String TIPOS_SERVICO = "TIPOS_SERVICO";
    public static final String MODO = "MODO";
    public static final String ID = "ID";
    public static final int NEW = 1;
    public static final int UPDATE = 2;

    private EditText editTextTipoServico;
    private List<CategoriaDeServicos> listaCategoriasDeServicos;
    private ArrayAdapter<CategoriaDeServicos> categoriasServicosAdapter;
    private int modo;

    private ConstraintLayout layout;
    private CategoriaDeServicos categoriaDeServicos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias_de_servicos);

        editTextTipoServico = findViewById(R.id.editTextCategoriaServicos);
        layout = findViewById(R.id.addCategoriaServicoLayout);

        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        if(bundle != null){
            modo = bundle.getInt(MODO, NEW);
            if(modo == NEW){
                setTitle(R.string.nova_categoria);
                categoriaDeServicos = new CategoriaDeServicos("");
            }
            else{
                setTitle(R.string.atualiza_categoria);
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        int id = bundle.getInt(ID);
                        DiarioDeTrabalhoDatabase base = DiarioDeTrabalhoDatabase.getDatabase(CategoriasDeServicosActivity.this);
                        categoriaDeServicos = base.categoriaDeServicosDao().queryForId(id);
                        CategoriasDeServicosActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                editTextTipoServico.setText(categoriaDeServicos.getTipoDeServico());
                            }
                        });
                    }
                });
            }
        }
        carregaCor();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
    }

    public static void addCategoria(Activity activity, int requestCode){
        Intent intent = new Intent(activity, CategoriasDeServicosActivity.class);
        intent.putExtra(MODO, NEW);
        activity.startActivityForResult(intent, requestCode);
    }
    public static void editCategoria(Activity activity, int requestCode, CategoriaDeServicos categoriaDeServicos){
        Intent intent = new Intent(activity, CategoriasDeServicosActivity.class);
        intent.putExtra(MODO, UPDATE);
        intent.putExtra(ID, categoriaDeServicos.getId());
        activity.startActivityForResult(intent, requestCode);
    }
    private void carregaCor(){

        layout.setBackgroundColor(PrincipalActivity.selecionaCor);
    }
    public void salvarComBotao(View view){
        salvarCategoriaDeServico();
    }
    private void salvarCategoriaDeServico(){
        String tipoDeServico = UtilsAviso.validaCampo(this,
                                                      editTextTipoServico,
                                                      R.string.tipo_servico_vazio);
        if(tipoDeServico == null)
            return;
        categoriaDeServicos.setTipoDeServico(tipoDeServico);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                DiarioDeTrabalhoDatabase base = DiarioDeTrabalhoDatabase.getDatabase(CategoriasDeServicosActivity.this);
                if(modo == NEW){
                  int auxId = (int) base.categoriaDeServicosDao().insert(categoriaDeServicos);
                  categoriaDeServicos.setId(auxId);
                }
                else{
                    base.categoriaDeServicosDao().update(categoriaDeServicos);
                }
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }
    private void cancelar(){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
    @Override
    public void onBackPressed(){
        cancelar();
    }
}
