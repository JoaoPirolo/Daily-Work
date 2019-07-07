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
import android.widget.Spinner;

import com.joaopirolo.diario_de_trabalho.model.CategoriaDeServicos;
import com.joaopirolo.diario_de_trabalho.model.Servicos;
import com.joaopirolo.diario_de_trabalho.persistence.DiarioDeTrabalhoDatabase;
import com.joaopirolo.diario_de_trabalho.utils.UtilsAviso;

import java.util.List;

public class ServicosActivity extends AppCompatActivity {

    private EditText editTextNumBa;
    private EditText editTextCity;
    private EditText editTextArd;

    private ArrayAdapter<CategoriaDeServicos> categoriaAdapter;

    private Spinner spinnerCategoriaDeServicos;

    private List<CategoriaDeServicos> categoriadeservicos;
    private int modo;


    private Servicos servicos;
    private CategoriaDeServicos categoriaDeServicos;
    private ConstraintLayout layout;

    public static final String MODO = "MODO";
    public static final String ID = "ID";

    public static final String Servicos = "SERVICOS";

   public static final int NEW = 1;
   public static final int UPDATE = 2;

public static void novoServico(Activity activity, int requestCode){
    Intent intent = new Intent(activity, ServicosActivity.class);
    intent.putExtra(MODO, NEW);
    activity.startActivityForResult(intent, requestCode);
}
public static void alterarServico(Activity activity, int requestCode, Servicos servicos){
    Intent intent = new Intent (activity, ServicosActivity.class);
    intent.putExtra(MODO, UPDATE);
    intent.putExtra(ID, servicos.getId());
    activity.startActivityForResult(intent, requestCode);
}
private void carregaCor(){
    layout.setBackgroundColor(PrincipalActivity.selecionaCor);
}
public void salvarServico(){
    String numba = UtilsAviso.validaCampo(this, editTextNumBa, R.string.numBa_vazio);
    if(numba == null)
        return;
    String city = UtilsAviso.validaCampo(this, editTextCity,R.string.cidade_vazio);
    if(city == null)
        return;
    String ard = UtilsAviso.validaCampo(this, editTextArd, R.string.armario_vazio);
    if(ard == null)
        return;


    servicos.setNumberBa(numba);
    servicos.setCity(city);
    servicos.setArd(ard);


    CategoriaDeServicos categoriaDeServicos = (CategoriaDeServicos) spinnerCategoriaDeServicos.getSelectedItem();
    if(categoriaDeServicos != null){
        servicos.setTipoDeServicoId(categoriaDeServicos.getId());
    }
    AsyncTask.execute(new Runnable() {
        @Override
        public void run() {
            DiarioDeTrabalhoDatabase base = DiarioDeTrabalhoDatabase.getDatabase(ServicosActivity.this);
            if(modo == NEW){
                int newId = (int) base.servicosDao().insert(servicos);
                servicos.setId(newId);
            }
            else{
                base.servicosDao().update(servicos);
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
public void salvarServicoClick(View view){

    salvarServico();
}

private void mostrarCategoriasDeServicos(){
    AsyncTask.execute(new Runnable() {
        @Override
        public void run() {
            DiarioDeTrabalhoDatabase base = DiarioDeTrabalhoDatabase.getDatabase(ServicosActivity.this);

           categoriadeservicos = base.categoriaDeServicosDao().queryAll();

            ServicosActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ArrayAdapter<CategoriaDeServicos> spinnerAdapter = new ArrayAdapter<>(ServicosActivity.this,
                            android.R.layout.simple_list_item_1, categoriadeservicos);
                    spinnerCategoriaDeServicos.setAdapter(spinnerAdapter);
                }
            });
        }
    });
}
private int ordenaTiposDeServico(int categoriaDeServicosId){
    for(int ordem = 0; ordem < categoriadeservicos.size(); ordem ++){
        CategoriaDeServicos categoria = categoriadeservicos.get(ordem);
        if(categoria.getId() == categoriaDeServicosId){
            return ordem;
        }
    }
    return -1;
}
@Override
public void onBackPressed(){
    cancelar();
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicos);

        editTextNumBa = findViewById(R.id.editTextNumBa);
        editTextCity  = findViewById(R.id.editTextCity);
        editTextArd   = findViewById(R.id.editTextArd);
        spinnerCategoriaDeServicos = findViewById(R.id.spinnerCategoriaDeServicos);
        layout = findViewById(R.id.LayoutServicos);

        mostrarCategoriasDeServicos();

        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        if(bundle != null){
            modo = bundle.getInt(MODO, NEW);
                if(modo == NEW){
                    setTitle(R.string.novo_servico);
                    servicos = new Servicos("", "","");
                }
                else{
                    setTitle(R.string.editarServico);
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            int id = bundle.getInt(ID);
                            DiarioDeTrabalhoDatabase base = DiarioDeTrabalhoDatabase.getDatabase(ServicosActivity.this);
                            servicos = base.servicosDao().queryForId(id);
                            categoriaDeServicos = base.categoriaDeServicosDao().queryForId(servicos.getTipoDeServicoId());

                            ServicosActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    editTextNumBa.setText(servicos.getNumberBa());
                                    editTextCity.setText(servicos.getCity());
                                    editTextArd.setText(servicos.getArd());
                                    int ordem = ordenaTiposDeServico(servicos.getTipoDeServicoId());
                                    spinnerCategoriaDeServicos.setSelection(ordem);
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
}
